package ziad.developer.todo;

  import io.vertx.config.ConfigRetriever;
  import io.vertx.core.*;
  import io.vertx.core.Promise;
  import io.vertx.core.http.HttpServerOptions;
  import io.vertx.core.json.JsonObject;
  import io.vertx.ext.auth.JWTOptions;
  import io.vertx.ext.auth.KeyStoreOptions;
  import io.vertx.ext.auth.jwt.JWTAuth;
  import io.vertx.ext.auth.jwt.JWTAuthOptions;
  import io.vertx.ext.web.Router;
  import io.vertx.ext.web.RoutingContext;
  import io.vertx.ext.web.handler.BodyHandler;
  import io.vertx.ext.web.handler.CorsHandler;
  import io.vertx.ext.web.handler.JWTAuthHandler;
  import io.vertx.ext.web.handler.StaticHandler;
  import io.vertx.ext.web.openapi.RouterBuilder;
  import io.vertx.ext.web.validation.BadRequestException;

  import java.util.*;

  import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;
  import ziad.developer.todo.database.DatabaseManager;
  import ziad.developer.todo.login.LoginManager;
  import ziad.developer.todo.user_panel.PanelManager;
  import ziad.developer.todo.util.Http;
  import ziad.developer.todo.verticles.NeededVerticle;
  import ziad.developer.todo.verticles.OtherOne;

public class MainVerticle extends AbstractVerticle {
  private String PREFIX = "MainVerticle:::";
  private final Logger LOGGING = LoggerFactory.getLogger(PREFIX);

  private JsonObject config = new JsonObject();
  private String noJwt = "";

  private DatabaseManager dbManager;
  private LoginManager loginManager;
  private PanelManager panelManager;

  /**
   * Read all configurations files (database, server and clients configuration)
   * @return Future to verify if the process was successfully performed.
   */
  private Future<JsonObject> loadConfig(){
    LOGGING.info("Loading configuration for main...");
    Promise<JsonObject> configPromise = Promise.promise();

    /* To load configuration from a file:
    ConfigStoreOptions fileConfigStore = new ConfigStoreOptions()
      .setType("file")
      .setConfig(new JsonObject().put("path", "config.json"));

    ConfigRetriever retriever = ConfigRetriever
      .create(vertx, new ConfigRetrieverOptions()
        .addStore(fileConfigStore));

    retriever.getConfig(json -> {
      if(json.succeeded()){
        LOGGING.info("configuration loaded for main.");
        configPromise.complete();
        this.config = json.result();
      }
      else {
        LOGGING.error("Can not get the configuration for main.");
        configPromise.fail(json.cause());
      }
    });
    */
    ConfigRetriever retriever = ConfigRetriever.create(vertx);
    retriever.getConfig(json -> {
      if (json.succeeded()) {
        this.config.put("database", json.result().getJsonObject(ApplicationConfig.ENV_DATABASE_CONFIG, ApplicationConfig.DEFAULT_DATABASE_CONFIG));
        this.config.put("server", json.result().getJsonObject(ApplicationConfig.ENV_SERVER_CONFIG, ApplicationConfig.DEFAULT_SERVER_CONFIG));
        configPromise.complete(this.config);
        LOGGING.info("configuration loaded for main.");
      } else {
        LOGGING.error("Can not get the configuration for main.");
        configPromise.fail(json.cause());
      }
    });
    return configPromise.future();
  }

  /**
   * Launch all needed sub-vericles
   * @return Future to verify if the process was successfully performed.
   */
  private Future<Void> deployComponents() {
    DeploymentOptions deploymentOptions = new DeploymentOptions();
    deploymentOptions.setWorker(true);

    Promise<Void> deploymentPromise = Promise.promise();
    Future<Void> deployment = Future.succeededFuture();
    deployment.compose(component1 -> {
        Future<String> adminFuture;
        LOGGING.info("Deploying NeededVerticle component...");
        adminFuture = vertx.deployVerticle(NeededVerticle.class.getName(), deploymentOptions);
        return adminFuture;
      }).compose(component2 -> {
        Future<String> backupFuture;
        LOGGING.info("Deploying OtherOne component...");
        backupFuture = vertx.deployVerticle(OtherOne.class.getName(), deploymentOptions);
        return backupFuture;
      })
      .onSuccess(res -> {
        LOGGING.info("Deployment all components completed");
        deploymentPromise.complete();
      })
      .onFailure(fail -> {
        LOGGING.error("Failure with deployment the components.");
        deploymentPromise.fail(fail);
      });
    return deploymentPromise.future();
  }

  /**
   * initiate needed schema in postgresql database.
   * @return Future to verify if the process was successfully performed.
   */
  private Future<Void> initDbConfiguration(){
    Promise<Void> dbPromise = Promise.promise();
    this.dbManager = new DatabaseManager(vertx, this.config.getJsonObject("database"), 10);

    dbManager.initDb(result -> {
      if(result.succeeded())
        dbPromise.complete();
      else
        dbPromise.fail(result.cause());
    });
    return dbPromise.future();
  }


  /***
   * setup CORS, openapi, routing and start the server
   * @return
   */
  private Future<String> startServer(){
    Promise<String> serverPromise = Promise.promise();
    this.loginManager = new LoginManager();
    this.panelManager = new PanelManager();

    JsonObject serverConfig = this.config.getJsonObject("server");

    RouterBuilder.create(vertx, "api.yaml")
      .onSuccess(routerBuilder -> {
        LOGGING.info("OpenAPI configuration loaded.");

        JWTAuthOptions jwTConfig = new JWTAuthOptions()
          .setKeyStore(new KeyStoreOptions()
            .setType("jks")
            .setPath("JWT-keystore.jks")
            .setPassword("6tgsrggnbsffhvdv45"));
        JWTAuth jwtProvider = JWTAuth.create(vertx, jwTConfig);

        String cors = "http:\\/\\/server.grykely.de:\\d{1,5}";
        routerBuilder.rootHandler(CorsHandler.create()
          .addRelativeOrigin(cors)
          .allowedHeaders(Http.allowedHeaders)
          .allowedMethods(Http.allowedMethods)
        );
        routerBuilder.rootHandler(BodyHandler.create());
        routerBuilder.rootHandler(StaticHandler.create());

        routerBuilder.operation("getTasks").handler(routingContext -> {
          String username = routingContext.request().getParam("username");
          this.loginManager.login(username, res -> {
            if (res.succeeded()) {
              JsonObject payload = new JsonObject()
                .put("sub", username)
                .put("name", username);
              String jwt = jwtProvider.generateToken(payload, new JWTOptions().setExpiresInMinutes(60 * 6));
              Http.reply(Future.succeededFuture(res.result()), "application/json", jwt, routingContext);
            }
            else Http.reply(Future.failedFuture(res.cause()), "String", noJwt, routingContext);
          });
        });

        routerBuilder.securityHandler("bearerAuth", JWTAuthHandler.create(jwtProvider)).operation("saveTask")
          .handler(routingContext -> {
            String username = routingContext.request().getParam("username");
            JsonObject body = routingContext.body().asJsonObject();
            this.panelManager.addTask(username, body, res -> {
              if (res.succeeded()) Http.reply(Future.succeededFuture(res.result()), "String", noJwt, routingContext);
              else Http.reply(Future.failedFuture(res.cause()), "String", noJwt, routingContext);
            });
        });

        Router router = routerBuilder.createRouter();
        router.errorHandler(404, this::errorHandler);

        vertx.createHttpServer(new HttpServerOptions()
            .setPort(serverConfig.getInteger("port"))
            .setHost(serverConfig.getString("host").equals("localhost")? "0.0.0.0" : serverConfig.getString("host")))
          .requestHandler(router)
          .listen()
          .onSuccess(server -> {
            LOGGING.info("HTTP server started on port " + server.actualPort());
            serverPromise.complete();
          })
          .onFailure(server -> {
            LOGGING.error("HTTP server can not be started");
            serverPromise.fail(server);
          });
      })
      .onFailure(err -> {
        LOGGING.error("can not create API.");
        serverPromise.fail(err.getCause().getMessage());
      });

    return serverPromise.future();
  }

  /***
   * To handle all wrong queries.
   * @param context
   */
  private void errorHandler(RoutingContext context){
    if (context.failure() instanceof BadRequestException) {
      JsonObject error = new JsonObject()
        .put("message", context.failure().getMessage())
        .put("error", "Resource not found.")
        .put("status", "error");
      context.response()
        .putHeader("Content-Type", "application/json")
        .end(error.encodePrettily());
    }
  }

  /***
   * Read configurations, use database, setup and start the server
   */
  @Override
  public void start(Promise<Void> startPromise) {
    loadConfig()
      .compose(res -> deployComponents())
      .compose(res2 -> initDbConfiguration())
      .compose(res3 -> startServer())
      .onComplete(handler -> {
        if(handler.succeeded()){
          LOGGING.info("services manager started successfully.");
          startPromise.complete();
        }
        else{
          LOGGING.error("Something goes wrong while starting services manager.");
          startPromise.fail(handler.cause().getMessage());
        }
      });
  }

  public static void main(String[] args){
    String[] params = Arrays.copyOf(args, args.length + 1);
    params[params.length - 1] = MainVerticle.class.getName();
    Launcher.executeCommand("run", params);
  }
}
