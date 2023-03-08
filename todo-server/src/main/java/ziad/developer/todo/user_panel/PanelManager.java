package ziad.developer.todo.user_panel;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Tuple;

public class PanelManager {
  private final String PREFIX = "PanelManager:::";

  private final PanelConnector panelConnector;

  public PanelManager() {
    this.panelConnector = new PanelConnector();
  }

  public void addTask(String username, JsonObject data, Handler<AsyncResult<String>> addTaskHandler) {
    Tuple params = Tuple.tuple()
      .addString(username)
      .addString(data.getString("title"))
      .addString(data.getString("dueDate"))
      .addString(data.getString("note"))
      .addString(data.getString("creationDate"));
    this.panelConnector.addTaskRequest(params, addTaskResult -> {
      if(addTaskResult.succeeded())
        addTaskHandler.handle(Future.succeededFuture(addTaskResult.result()));
      else
        addTaskHandler.handle(Future.failedFuture(addTaskResult.cause()));
    });
  }
}
