package coinmockproject.db;

import coinmockproject.model.User;
import java.sql.*;

public class UserRepository {

    // 회원가입
    public boolean registerUser(String username, String password) {
        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false; // 이미 존재/DB 에러 등
        }
    }

    // 로그인 (존재 여부 확인)
    public User login(String username, String password) {
        String sql = "SELECT id, username, password FROM user WHERE username=? AND password=?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // 로그인 실패
    }
}