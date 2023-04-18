package org.libsql.examples;

import org.libsql.client.Client;
import org.libsql.client.ResultSet;

public final class Example {
    public static void main(String[] args) {
        String url = "<insert URL here>";
        String authToken = "<insert auth token here>";
        Client client = Client.builder(url).authToken(authToken).build();
        ResultSet rs = client.execute("SELECT * FROM users");
        System.out.println(rs.columns);
        for (ResultSet.Row row : rs.rows) {
            System.out.println(row.toString());
        }
    }
}