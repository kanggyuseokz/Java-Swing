package coinmockproject.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private static final String URL = "jdbc:mysql://localhost:3306/coinmock?serverTimezone=Asia/Seoul";
    private static final String USER = "root"; // 본인 MySQL 아이디로 변경
    private static final String PASSWORD = "1234"; // 본인 MySQL 비밀번호로 변경

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}