package ziad.developer.todo.user_panel;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ziad.developer.todo.database.DatabaseManager;

public class PanelConnector {
  private final String PREFIX = "PanelConnector:::";
  private final Logger LOGGING = LoggerFactory.getLogger(PREFIX);

  private final DatabaseManager databaseManager;

  private final String INSERT_TO_USERS = "INSERT INTO users VALUES ($1, $2, $3, $4, $5)";
  private final String GET_TASK_BY_USERNAME_TITLE = "SELECT * FROM users WHERE username = $1 AND title = $2";

  protected PanelConnector(){
    this.databaseManager = new DatabaseManager();
  }

  protected void addTaskRequest(Tuple params, Handler<AsyncResult<String>> addTaskRequestHandler) {
    Tuple checkTuple = Tuple.tuple().addString(params.getString(0)).addString(params.getString(1));
    this.databaseManager.executeQuery(GET_TASK_BY_USERNAME_TITLE, checkTuple, queryRes -> {
      if(queryRes.succeeded()){
        if(queryRes.result().size() == 0) {
          this.insertTask(params, insertHandler -> {
            if(insertHandler.succeeded())
              addTaskRequestHandler.handle(Future.succeededFuture(insertHandler.result()));
            else {
              addTaskRequestHandler.handle(Future.failedFuture("500_" + insertHandler.cause()));
            }
          });
        }
        else {
          LOGGING.debug("task already exists for user: " + params.getString(0));
          addTaskRequestHandler.handle(Future.failedFuture("402_task already exists."));
        }
      }
      else {
        LOGGING.error("Failure with selecting Task.\nCause: " + queryRes.cause());
        addTaskRequestHandler.handle(Future.failedFuture("500_" + queryRes.cause()));
      }
    });
  }

  private void insertTask(Tuple params, Handler<AsyncResult<String>> insertTaskHandler){
    this.databaseManager.executeQuery(INSERT_TO_USERS, params, queryRes -> {
      if(queryRes.succeeded()){
        LOGGING.info("task successfully added\n");
        insertTaskHandler.handle(Future.succeededFuture("task successfully added."));
      }
      else {
        LOGGING.error("Failure with adding Task.\nCause: " + queryRes.cause());
        insertTaskHandler.handle(Future.failedFuture(queryRes.cause()));
      }
    });
  }
}
