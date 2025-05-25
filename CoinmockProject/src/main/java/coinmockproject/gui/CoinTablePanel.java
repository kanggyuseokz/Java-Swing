package coinmockproject.gui;

import coinmockproject.model.Coin;
import coinmockproject.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CoinTablePanel extends JPanel {
    private DefaultTableModel tableModel;
    private User user;

    public CoinTablePanel() {
        setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(new Object[]{"코인명", "심볼", "가격(USD)"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
    
    public CoinTablePanel(User user) {
        this(); // 기본 생성자 호출 → 테이블 초기화
        this.user = user;

        setLayout(new BorderLayout());

        // 상단 사용자 표시
        JLabel label = new JLabel(user.getUsername() + "님의 보유 코인");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        label.setForeground(Color.WHITE); // 다크 테마 대응
        add(label, BorderLayout.NORTH);

        // 테이블 설정
        tableModel = new DefaultTableModel(new Object[]{"코인명", "심볼", "가격(USD)"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);
    }

 // 시세 갱신용 메서드
    public void updateTable(Coin[] coins) {
        tableModel.setRowCount(0);
        for (Coin coin : coins) {
            tableModel.addRow(new Object[]{
                coin.getName(),
                coin.getSymbol(),
                coin.getPriceUsd() < 0 ? "오류" : String.format("$%,.2f", coin.getPriceUsd())
            });
        }
    }
}