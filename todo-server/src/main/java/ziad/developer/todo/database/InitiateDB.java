package ziad.developer.todo.database;

import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class InitiateDB {
  private final String PREFIX = "InitiateDB:::";
  private final Logger LOGGING = LoggerFactory.getLogger(PREFIX);

  private final JsonObject users = new JsonObject()
    .put("username", "VARCHAR(255)")
    .put("title", "VARCHAR(255)")
    .put("duedate", "VARCHAR(255)")
    .put("note", "TEXT")
    .put("creationdate", "VARCHAR(255)");

  protected InitiateDB() {}

  protected void createTables(DatabaseConnector dbConnector, Handler<AsyncResult<Void>> createTablesHandler){
    Promise<Void> usersTable = Promise.promise();

    dbConnector.initTable(users, "users", t1 -> {
      if(t1.succeeded()) usersTable.complete(); else usersTable.fail(t1.cause());
    });

    List<Future> functions = Arrays.asList(usersTable.future());
    CompositeFuture.all(functions)
      .onSuccess(success -> {
        LOGGING.info("All tables are created.");
        createTablesHandler.handle(Future.succeededFuture());
      })
      .onFailure(fail -> {
        LOGGING.error("Can not create all tables.");
        createTablesHandler.handle(Future.failedFuture(fail.getCause()));
      });
  }

}
