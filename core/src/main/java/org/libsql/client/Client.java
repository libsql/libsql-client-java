package org.libsql.client;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * The entry point to LibSQL client API.
 */
public class Client {
  private URL url;
  private String authToken;

  /**
   * Build a <code>Client</code> object.
   * 
   * @param url The URL of the libSQL server.
   * 
   * @see Client.Builder#authToken(String)
   */
  public static Builder builder(String url) {
    try {
      return new Builder(new URL(url.replaceFirst("^libsql", "https")));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Construct a <code>Client</code> object.
   *
   * @param url The URL of the libSQL server.
   * @param authToken The authentication token.
   */
  public Client(URL url, String authToken) {
    this.url = url;
    this.authToken = authToken;
  }

  /**
   * Execute a single SQL statement.
   *
   * @param sql The SQL statement.
   * @return The result set.
   */
  public ResultSet execute(String stmt) {
    return batch(new String[] { stmt })[0];
  }

  /**
   * Execute a batch of SQL statements.
   *
   * @param stmts The SQL statements.
   * @return The result sets.
   */
  public ResultSet[] batch(String[] stmts) {
    try {
      HttpURLConnection conn = openConnection();
      conn.setRequestMethod("POST");
      conn.setDoOutput(true);
      String body = query(stmts);
      try (OutputStream os = conn.getOutputStream()) {
        byte[] input = body.getBytes("utf-8");
        os.write(input, 0, input.length);
      }
      try (InputStreamReader in = new InputStreamReader(conn.getInputStream())) {
        Gson gson = new Gson();
        Response[] response = gson.fromJson(in, Response[].class);
        ResultSet[] resultSets = new ResultSet[response.length];
        for (int i = 0; i < response.length; i++) {
          resultSets[i] = response[i].results;
        }
        return resultSets;
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  private HttpURLConnection openConnection() throws IOException {
    HttpURLConnection conn = (HttpURLConnection) this.url.openConnection();
    conn.setRequestProperty("Authorization", "Bearer " + authToken);
    return conn;
  }

  private String query(String[] stmts) {
    StringBuilder sb = new StringBuilder();
    sb.append("{\"statements\": [");
    for (int i = 0; i < stmts.length; i++) {
      sb.append("\"" + stmts[i] + "\"");
      if (i < stmts.length - 1) {
        sb.append(",");
      }
    }
    sb.append("]}");
    return sb.toString();
  }

  public static class Response {
    public ResultSet results;
  }

  /**
   * Client builder.
   */
  public static class Builder {
    private URL url;
    private String authToken;

    Builder(URL url) {
      this.url = url;
    }

    /**
     * Configure the database authentication token.
     * 
     * @param authToken The authentication token.
     */
    public Builder authToken(String authToken) {
      this.authToken = authToken;
      return this;
    }

    public Client build() {
      return new Client(url, authToken);
    }
  }
}
