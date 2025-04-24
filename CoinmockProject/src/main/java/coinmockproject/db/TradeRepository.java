package coinmockproject.db;

import coinmockproject.model.Trade;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TradeRepository {

    // 거래 저장 (insert)
    public void saveTrade(Trade trade) {
        String sql = "INSERT INTO trade (coin, type, price, amount, time) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trade.getCoin());
            ps.setString(2, trade.getType());
            ps.setDouble(3, trade.getPrice());
            ps.setDouble(4, trade.getAmount());
            ps.setTimestamp(5, Timestamp.valueOf(trade.getTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 거래 내역 전체 조회
    public List<Trade> findAllTrades() {
        List<Trade> trades = new ArrayList<>();
        String sql = "SELECT coin, type, price, amount, time FROM trade";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                trades.add(new Trade(
                    rs.getString("coin"),
                    rs.getString("type"),
                    rs.getDouble("price"),
                    rs.getDouble("amount"),
                    rs.getTimestamp("time").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trades;
    }
}