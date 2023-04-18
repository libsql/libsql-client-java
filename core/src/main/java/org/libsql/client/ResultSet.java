package org.libsql.client;

import java.util.ArrayList;
import java.util.List;

public class ResultSet {
  public List<String> columns;
  public List<Row> rows;

  public static class Row extends ArrayList<Object> {
    public int getInt(int columnIdx) {
      return (int) get(columnIdx);
    }

    public String getString(int columnIdx) {
      return (String) get(columnIdx);
    }
  }
}