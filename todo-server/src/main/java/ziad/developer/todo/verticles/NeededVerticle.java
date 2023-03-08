package ziad.developer.todo.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeededVerticle extends AbstractVerticle {
  private String PREFIX = "NeededVerticle:::";
  private final Logger LOGGING = LoggerFactory.getLogger(PREFIX);

  @Override
  public void start(Promise<Void> startPromise){
    LOGGING.info("NeededVerticle successfully launched");
    startPromise.complete();
  }
}
