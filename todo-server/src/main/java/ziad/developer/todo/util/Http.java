package ziad.developer.todo.util;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Http {

  public static Set<String> allowedHeaders = new HashSet<>(Arrays
    .asList(
      "x-requested-with",
      "Access-Control-Allow-origin",
      "origin",
      "Content-Type",
      "Accept",
      "HOST"));

  public static Set<HttpMethod> allowedMethods = new HashSet<>(Arrays
    .asList(
      HttpMethod.GET, // Request data.
      HttpMethod.POST, // Sends data to the server to be saved.
      HttpMethod.PUT, // Creates a new resource or replaces.
      HttpMethod.PATCH, // Make partial modifications to a resource.
      HttpMethod.DELETE)); // Delete specific resource.


  public static void reply(Future result, String contentType, String jwt, RoutingContext routingContext) {
    if (result.succeeded()) {
      routingContext.response()
        .setStatusCode(200)
        .putHeader("Content-Type", contentType)
        .putHeader("Access-Control-Allow-origin", "http://server.grykely.de:8090")
        .putHeader("origin", "http://server.grykely.de:8090")
        .putHeader("Authorization", jwt);
      if (contentType.contains("json")) routingContext.json(result.result() == null? new JsonObject() : result.result());
      else routingContext.response().end(result.result() == null? "No Result" : result.result().toString());
    }
    else
      routingContext.response()
        .putHeader("Content-Type", "String")
        .putHeader("Access-Control-Allow-origin", "http://server.grykely.de:8090")
        .putHeader("origin", "http://server.grykely.de:8090")
        .setStatusCode(Integer.parseInt(result.cause().getMessage().split("_")[0]))
        .end(result.cause().getMessage().split("_")[1]);
  }
}
