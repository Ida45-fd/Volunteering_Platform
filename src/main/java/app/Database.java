package app;

import java.sql.*;

public class Database {
private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/ngo_app?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
private static final String MYSQL_USER = "ngoapp";
private static final String MYSQL_PASS = "ngo123";


    // If you prefer SQLite, comment MySQL above and uncomment below
    // private static final String SQLITE_URL = "jdbc:sqlite:./ngo_app.db";

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            // MySQL driver
            connection = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
            // SQLite: connection = DriverManager.getConnection(SQLITE_URL);
        }
        return connection;
    }
}

