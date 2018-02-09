package proba;

import java.sql.*;

public class ConnectionDb {

    public static Connection con() throws ClassNotFoundException, SQLException {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/test";
        String username = "postgres";
        String password = "postgres";
        Class.forName("org.postgresql.Driver");
        Connection conn = null;

        if (conn == null){
          return conn = DriverManager.getConnection(jdbcUrl, username, password);
        }
        return conn;

    }


}
