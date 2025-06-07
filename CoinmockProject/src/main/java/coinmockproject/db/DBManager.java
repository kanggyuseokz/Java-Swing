package coinmockproject.db;

import coinmockproject.util.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private static final String URL = EnvLoader.get("DB_URL");
    private static final String USER = EnvLoader.get("DB_USER");
    private static final String PASSWORD = EnvLoader.get("DB_PASSWORD");
    private static final String DRIVER   = System.getenv("DB_DRIVER"); 
    
    static {
        try {
            if (DRIVER != null) {
                Class.forName(DRIVER);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("JDBC Driver 로딩 실패: " + DRIVER, e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}