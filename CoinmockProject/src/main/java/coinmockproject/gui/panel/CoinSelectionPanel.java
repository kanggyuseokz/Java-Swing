package coinmockproject.gui.panel;

import coinmockproject.model.Coin;
import coinmockproject.service.CoinAPIService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.*;

public class CoinSelectionPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> coinBox;
    private JButton addBtn;
    private Set<String> selectedCoins = new LinkedHashSet<>();

    public CoinSelectionPanel() {
        setLayout(new BorderLayout(10, 10));

        // 테이블 초기화
        model = new DefaultTableModel(new Object[]{"코인명", "심볼", "가격"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 상단 선택 패널
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        coinBox = new JComboBox<>(new String[]{
                "bitcoin", "ethereum", "ripple", "dogecoin", "solana",
                "cardano", "chainlink", "polkadot", "litecoin"
        });
        addBtn = new JButton("추가");

        topPanel.add(new JLabel("코인 선택: "));
        topPanel.add(coinBox);
        topPanel.add(addBtn);

        add(topPanel, BorderLayout.NORTH);

        // 버튼 클릭 이벤트
        addBtn.addActionListener((ActionEvent e) -> {
            String selected = (String) coinBox.getSelectedItem();
            if (selectedCoins.contains(selected)) {
                JOptionPane.showMessageDialog(this, "이미 추가된 코인입니다.");
                return;
            }
            selectedCoins.add(selected);
            updateTable();
        });
    }

    private void updateTable() {
        Coin[] coins = CoinAPIService.fetchCoins(new ArrayList<>(selectedCoins));
        model.setRowCount(0);
        for (Coin coin : coins) {
            model.addRow(new Object[]{coin.getName(), coin.getSymbol(), String.format("$%,.2f", coin.getPriceUsd())});
        }
    }
}
