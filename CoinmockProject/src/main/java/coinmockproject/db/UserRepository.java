package coinmockproject.db;

import coinmockproject.model.User;
import java.sql.*;

public class UserRepository {

    // 회원가입: username/password + balance(=5000) 삽입
    public User registerUser(String username, String password) {
        String sql = "INSERT INTO user (username, password, balance) VALUES (?, ?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setDouble(3, 5000.00);
            int affected = ps.executeUpdate();
            if (affected == 0) {
                return null;
            }
            User user = new User(username, password);
            return user;
        } catch (SQLException e) {
            return null;
        }
    }

    // 로그인: username/password 일치 시 User(username, password, balance) 반환
    public User login(String username, String password) {
        String sql = "SELECT username, password, balance FROM user WHERE username = ? AND password = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setBalance(rs.getDouble("balance"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static User findByUsername(String username) {
        String sql = "SELECT id, username, password, balance FROM user WHERE username = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // User 생성자/세터에 맞춰 객체 생성
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setBalance(rs.getDouble("balance"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    // 잔액 업데이트
    public boolean updateBalance(String username, double newBalance) {
        String sql = "UPDATE user SET balance = ? WHERE username = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, newBalance);
            ps.setString(2, username);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            return false;
        }
    }
}
