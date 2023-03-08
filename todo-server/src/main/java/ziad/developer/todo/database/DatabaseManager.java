package ziad.developer.todo.database;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Tuple;

import java.util.List;

public class DatabaseManager {
  private final String PREFIX = "DatabaseManager:::";

  private DatabaseConnector dbConnector;
  private JsonObject DBConfig;
  private Vertx vertx;
  private int pollSize;

  public DatabaseManager(Vertx vertx, JsonObject config, int maxPoolSize){
    this.vertx = vertx;
    this.DBConfig = config;
    this.pollSize = maxPoolSize;
    this.connectToDatabase();
  }

  public DatabaseManager(){}

  public void initDb(Handler<AsyncResult<Void>> initDbHandler) {
    InitiateDB initDatabase = new InitiateDB();
    initDatabase.createTables(this.dbConnector, res -> {
      if(res.succeeded())
        initDbHandler.handle(Future.succeededFuture());
      else
        initDbHandler.handle(Future.failedFuture(res.cause()));
    });
  }

  public void executeQuery(String query, Tuple params, Handler<AsyncResult<List<JsonObject>>> executeQueryHandler) {
    this.connectToDatabase();
    this.dbConnector.query(query, params, res -> {
      if(res.succeeded())
        executeQueryHandler.handle(Future.succeededFuture(res.result()));
      else
        executeQueryHandler.handle(Future.failedFuture(res.cause()));
    });
  }

  private void connectToDatabase(){
    this.dbConnector = DatabaseConnector.getInstance();
    this.dbConnector.createConnection(vertx, this.DBConfig, this.pollSize);
  }

}
