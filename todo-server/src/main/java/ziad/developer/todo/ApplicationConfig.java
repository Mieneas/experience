package ziad.developer.todo;

import io.vertx.core.json.JsonObject;

public class ApplicationConfig {

  public static String ENV_DATABASE_CONFIG = "DATABASE_CONFIG";
  public static JsonObject DEFAULT_DATABASE_CONFIG = new JsonObject()
    .put("host", "localhost")
    .put("port", 5432)
    .put("database", "todoapp")
    .put("user", "todoapp")
    .put("password", "todoapp");

  public static String ENV_SERVER_CONFIG = "SERVER_CONFIG";
  public static JsonObject DEFAULT_SERVER_CONFIG = new JsonObject()
    .put("host", "localhost")
    .put("port", 8081);

}
