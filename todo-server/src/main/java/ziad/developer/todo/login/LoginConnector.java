package ziad.developer.todo.login;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ziad.developer.todo.database.DatabaseManager;

import java.util.List;

public class LoginConnector {
  private final String PREFIX = "LoginConnector:::";
  private final Logger LOGGING = LoggerFactory.getLogger(PREFIX);

  private final DatabaseManager databaseManager;

  private final String SELECT_USER_BY_USERNAME = "SELECT * FROM users WHERE userName=$1";

  protected LoginConnector(){
    this.databaseManager = new DatabaseManager();
  }

  protected void logInRequest(String username, Handler<AsyncResult<List<JsonObject>>> logInRequestHandler){
    this.databaseManager.executeQuery(SELECT_USER_BY_USERNAME, Tuple.tuple().addString(username), result -> {
      if(result.succeeded()){
        if (result.result().size() == 0) {
          LOGGING.info("User: not found.");
          logInRequestHandler.handle(Future.failedFuture("401_user not found."));
        }
        else {
          LOGGING.info("User: " + username + " logged in.");
          logInRequestHandler.handle(Future.succeededFuture(result.result()));
        }
      }
      else {
        LOGGING.error("500-Failure with selecting user.\nCause: " + result.cause());
        logInRequestHandler.handle(Future.failedFuture("500_" + result.cause()));
      }
    });
  }
}
