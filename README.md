# Java SDK for LibSQL

[![License](https://img.shields.io/badge/license-MIT-blue)](https://github.com/libsql/libsql-client-java/blob/main/LICENSE)

This is the source repository of the Java SDK for LibSQL. You can either connect to a local SQLite/LibSQL database (embedded in the client) or to a remote LibSQL server.

## Features

* Batch transactions
* Authentication
* Local database (coming soon)
* Interactive transactions (coming soon)

## Getting Started

```java
import org.libsql.client.Client;
import org.libsql.client.ResultSet;

// ...

String url = "<insert URL here>";
String authToken = "<insert auth token here>";
Client client = Client.builder(url).authToken(authToken).build();
ResultSet rs = client.execute("SELECT * FROM users");
System.out.println(rs.columns);
for (ResultSet.Row row : rs.rows) {
    System.out.println(row.toString());
}

```
## License

This project is licensed under the MIT license.

### Contribution

Unless you explicitly state otherwise, any contribution intentionally submitted for inclusion in this project by you, shall be licensed as MIT, without any additional terms or conditions.
