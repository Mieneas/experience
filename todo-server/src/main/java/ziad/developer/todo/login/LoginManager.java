package ziad.developer.todo.login;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class LoginManager {
  private final String PREFIX = "LoginManager:::";

  private final LoginConnector loginConnector;


  public LoginManager() {
    this.loginConnector = new LoginConnector();
  }

  public void login(String username, Handler<AsyncResult<List<JsonObject>>> loginHandler){
    this.loginConnector.logInRequest(username, loginRes -> {
      if (loginRes.succeeded())
        loginHandler.handle(Future.succeededFuture(loginRes.result()));
      else
        loginHandler.handle(Future.failedFuture(loginRes.cause()));
    });
  }
}
