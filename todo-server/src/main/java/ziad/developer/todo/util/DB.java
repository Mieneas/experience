package ziad.developer.todo.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DB {

  public static List<JsonObject> transformRowToJson(RowSet<Row> rows) {

    List<JsonObject> result = new ArrayList<>();

    rows.forEach(elem -> {
      int size = elem.size();
      JsonObject object = new JsonObject();

      for(int i = 0; i < size; i++){
        String columnName = elem.getColumnName(i);
        Object value = elem.getValue(i);
        if(value != null){
          //JSON, JSON[], TEXT, BIGINT-INTEGER
          if(value instanceof LocalDateTime)
            object.put(columnName, ((LocalDateTime) value).atZone(ZoneId.systemDefault()).toInstant());
          if(value instanceof JsonObject)
            object.put(columnName, new JsonObject(value.toString()));
          else if(value instanceof JsonObject[])
            object.put(columnName, new JsonArray(Arrays.asList(elem.getArrayOfJsonObjects(i))));
          else if(value instanceof String)
            object.put(columnName, value.toString());
          else
            object.put(columnName, value);
        }
      }
      result.add(object);
    });
    return result;
  }
}
