package coinmockproject.db;

import coinmockproject.model.Trade;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * TradeRepository (JDBC)
 *  - trade 테이블: (id, username, coin, type, price, amount, time) 구조
 *  - saveTrade(...)           : 매수/매도 이력 INSERT
 *  - findAllTrades(...)       : 전체 거래 내역 조회(옵션)
 *  - findTradesByUsername(...) : 특정 사용자 거래 내역 조회
 *  - findCurrentHoldings(...) : `trade` 이력만으로 현재 보유량 계산
 */
public class TradeRepository {

    /**
     * 거래 저장 (insert)
     */
    public void saveTrade(Trade trade) {
        String sql = "INSERT INTO trade "
                   + " (username, coin, type, price, amount, time) "
                   + " VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, trade.getUsername());
            ps.setString(2, trade.getCoin());
            ps.setString(3, trade.getType());
            ps.setDouble(4, trade.getPrice());
            ps.setDouble(5, trade.getAmount());
            ps.setTimestamp(6, Timestamp.valueOf(trade.getTime()));
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 전체 거래 내역 조회 (옵션)
     */
    public List<Trade> findAllTrades() {
        List<Trade> trades = new ArrayList<>();
        String sql = "SELECT username, coin, type, price, amount, time "
                   + "FROM trade "
                   + "ORDER BY time DESC";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                trades.add(new Trade(
                    rs.getString("username"),
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

    /**
     * 특정 사용자(username) 거래 내역 조회
     */
    public List<Trade> findTradesByUsername(String username) {
        List<Trade> trades = new ArrayList<>();
        String sql = "SELECT username, coin, type, price, amount, time "
                   + "FROM trade "
                   + "WHERE username = ? "
                   + "ORDER BY time DESC";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    trades.add(new Trade(
                        rs.getString("username"),
                        rs.getString("coin"),
                        rs.getString("type"),
                        rs.getDouble("price"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("time").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trades;
    }

    /**
     * 현재 보유량 계산 (username 별 coin 당 net amount)
     *
     * 예시 SQL:
     *   SELECT coin,
     *     SUM(CASE WHEN type='BUY'  THEN amount
     *              WHEN type='SELL' THEN -amount
     *              ELSE 0 END) AS net_amount
     *   FROM trade
     *  WHERE username = ?
     *  GROUP BY coin
     *  HAVING net_amount <> 0
     *
     * @param username 조회할 사용자 아이디
     * @return Map<coin_symbol, netAmount>
     */
    public Map<String, Double> findCurrentHoldings(String username) {
        String sql = ""
            + "SELECT coin, "
            + "       SUM(CASE WHEN type = 'BUY' THEN amount ELSE -amount END) AS net_amount "
            + "  FROM trade "
            + " WHERE username = ? "
            + " GROUP BY coin "
            + " HAVING net_amount <> 0";

        Map<String, Double> holdings = new HashMap<>();
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String coin      = rs.getString("coin");
                    double netAmount = rs.getDouble("net_amount");
                    holdings.put(coin, netAmount);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return holdings;
    }

}
