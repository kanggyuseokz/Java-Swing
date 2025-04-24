package coinmockproject.gui;

import coinmockproject.model.Coin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CoinTablePanel extends JPanel {
    private DefaultTableModel tableModel;

    public CoinTablePanel() {
        setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(new Object[]{"코인명", "심볼", "가격(USD)"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
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