package model;

public class Database {

    private static Connection conn;

    public static Connection getConnection() throws Exception {
        if (conn == null) {
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/flightdb",
                "root",
                "password"
            );
        }
        return conn;
    }
}
