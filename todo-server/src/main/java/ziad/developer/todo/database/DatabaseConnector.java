package ziad.developer.todo.database;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ziad.developer.todo.util.DB;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DatabaseConnector {
  private final String PREFIX = "DatabaseConnector:::";
  private final Logger LOGGING = LoggerFactory.getLogger(PREFIX);

  private PgPool databaseClient;
  private static final  DatabaseConnector DBC = new DatabaseConnector();
  private Vertx vertx;

  protected static DatabaseConnector getInstance() { return DBC; }

  protected DatabaseConnector() {}

  protected void createConnection(Vertx vertx, JsonObject config, int maxPoolSize) {
    this.vertx = vertx;

    if(this.databaseClient == null){
      PgConnectOptions connectOptions = new PgConnectOptions()
        .setPort(config.getInteger("port"))
        .setHost(config.getString("host"))
        .setDatabase(config.getString("database"))
        .setUser(config.getString("user"))
        .setPassword(config.getString("password"));

      PoolOptions poolOptions = new PoolOptions()
        .setMaxSize(maxPoolSize);

      this.databaseClient = PgPool.pool(vertx, connectOptions, poolOptions);
      LOGGING.info("Trying to create Connection for todo-app.");
    }
  }

  protected void query(String query, Tuple params, Handler<AsyncResult<List<JsonObject>>> queryHandler) {

    this.databaseClient.getConnection(conn -> {
      if(conn.succeeded()){
        SqlConnection connection = conn.result();
        connection.preparedQuery(query).execute(params, data -> {
          if(data.succeeded()){
            LOGGING.info("Result got from Database.");
            queryHandler.handle(Future.succeededFuture(DB.transformRowToJson(data.result())));
            connection.close();
          }
          else {
            LOGGING.error("could not get data from DB.\nCause: " + data.cause());
            queryHandler.handle(Future.failedFuture(data.cause()));
            connection.close();
          }
        });
      }
      else {
        LOGGING.error("Could not create connection for DB.\nCause: " + conn.cause());
        queryHandler.handle(Future.failedFuture(conn.cause()));
      }
    });
  }

  protected void initTable(JsonObject columnInfo, String tableName,Handler<AsyncResult<List<JsonObject>>> initTableHandler) {
    this.databaseClient.getConnection(conn -> {
      if(conn.succeeded()){
        SqlConnection connection = conn.result();
        connection.query("SELECT * FROM " + tableName).execute(data -> {
          if(data.succeeded()){
            RowSet<Row> rowSet = data.result();
            List<String> strings = new ArrayList<>();
            String dropColumns = "";
            for(String columnName : rowSet.columnsNames()){
              if(!columnInfo.fieldNames().contains(columnName)){
                dropColumns += "DROP COLUMN " + columnName + ",";
                strings.add(columnName);
              }
            }
            if(!dropColumns.isEmpty()){
              String dropQuery = "ALTER TABLE " + tableName + " " + dropColumns;
              connection.query(dropQuery.substring(0, dropQuery.length() - 1)).execute(delete -> {
                if(delete.succeeded()){
                  LOGGING.info("Deleted columns " + strings);
                  addColumns(connection, tableName, columnInfo.fieldNames(), columnInfo, initTableHandler);
                }
                else {
                  LOGGING.error("Delete columns " + strings + " from DB failed. \nCause: " + delete.cause());
                  initTableHandler.handle(Future.failedFuture(delete.cause()));
                }
              });
            }
            else {
              addColumns(connection, tableName, columnInfo.fieldNames(), columnInfo, initTableHandler);
            }
          }
          else {
            String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (";
            String columns = "";
            for(String key : columnInfo.fieldNames()){
              columns += key + " " + columnInfo.getString(key) + ",";
            }
            columns = columns.substring(0, columns.length() - 1);
            //Create primary keys
            query = query + columns + ")";
            connection.query(query).execute(queryRes -> {
              if(queryRes.succeeded()){
                initTableHandler.handle(Future.succeededFuture());
                connection.close();
              }
              else{
                LOGGING.error("Update columns for DB failed.\nCause: " + queryRes.cause());
                initTableHandler.handle(Future.failedFuture(queryRes.cause()));
                connection.close();
              }
            });
          }
        });
      }
      else {
        LOGGING.error("Could not create connection for DB.\nCause: " + conn.cause());
        initTableHandler.handle(Future.failedFuture(conn.cause()));
      }
    });
  }

  private void addColumns(SqlConnection connection, String tableName, Set<String> columns, JsonObject tableInfo, Handler<AsyncResult<List<JsonObject>>> addColumnsHandler) {
    String updateQuery = "ALTER TABLE " + tableName + " ";
    for(String key : columns){
      updateQuery += "ADD COLUMN IF NOT EXISTS " + key + " " + tableInfo.getString(key) + ",";
    }
    updateQuery = updateQuery.substring(0, updateQuery.length() - 1);
    connection.query(updateQuery).execute(add -> {
      if(add.succeeded()){
        LOGGING.info("Columns added.");
        addColumnsHandler.handle(Future.succeededFuture());
        connection.close();
      }
      else {
        LOGGING.error("Add Columns failed.\nCause: " + add.cause());
        addColumnsHandler.handle(Future.failedFuture(add.cause()));
        connection.close();
      }
    });
  }
}
