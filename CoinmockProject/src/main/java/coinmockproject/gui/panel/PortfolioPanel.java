package coinmockproject.gui.panel;

import coinmockproject.model.User;
import coinmockproject.model.Coin;
import coinmockproject.service.PortfolioService;
import coinmockproject.service.CoinAPIService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PortfolioPanel extends JPanel {
    private User user;
    private PortfolioService portfolioService;

    private JLabel balanceLabel;
    private JLabel roiLabel;
    private JTable holdingsTable;
    private DefaultTableModel tableModel;

    public PortfolioPanel(User user) {
        this.user = user;
        this.portfolioService = PortfolioService.getInstance();

        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30));
        initComponents();
        loadData();
    }

    private void initComponents() {
        // 상단 패널: 사용자명, 잔액, ROI
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.setBackground(new Color(30, 30, 30));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        balanceLabel = new JLabel();
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));

        roiLabel = new JLabel();
        roiLabel.setForeground(Color.WHITE);
        roiLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));

        topPanel.add(balanceLabel);
        topPanel.add(roiLabel);

        add(topPanel, BorderLayout.NORTH);

        // 중앙: 보유 코인 테이블
        String[] columns = {"코인", "수량", "현재가(USD)", "총가치(USD)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        holdingsTable = new JTable(tableModel);
        holdingsTable.setFillsViewportHeight(true);
        holdingsTable.setBackground(new Color(50, 50, 50));
        holdingsTable.setForeground(Color.WHITE);
        holdingsTable.setShowGrid(false);
        holdingsTable.setRowHeight(24);
        holdingsTable.getTableHeader().setBackground(new Color(45, 45, 45));
        holdingsTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(holdingsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadData() {
        // 1. 잔액
        double currentBalance = user.getBalance();
        balanceLabel.setText(user.getUsername() + "님 잔액: $" + String.format("%,.2f", currentBalance));

        // 2. 현재 보유량(Map<coinSymbol, amount>) 조회
        Map<String, Double> holdings = portfolioService.getCurrentHoldings(user);

        // 3. 코인지역 호출: 현재 모든 코인 시세 가져오기
        Coin[] arrayCoin = CoinAPIService.fetchCoins();
        List<Coin> allCoins = Arrays.asList(arrayCoin);

        // 4. 테이블 초기화
        tableModel.setRowCount(0);

        double totalHoldingsValue = 0.0;

        // 5. 보유코인별로 행 추가
        for (Map.Entry<String, Double> entry : holdings.entrySet()) {
            String symbol = entry.getKey();
            double amount = entry.getValue();

            // 현재가 찾기
            double currentPrice = allCoins.stream()
                    .filter(c -> c.getSymbol().equalsIgnoreCase(symbol))
                    .findFirst()
                    .map(Coin::getPriceUsd)
                    .orElse(0.0);

            double value = Math.round(currentPrice * amount * 100.0) / 100.0;
            totalHoldingsValue += value;

            tableModel.addRow(new Object[]{
                symbol,
                String.format("%.6f", amount),
                currentPrice < 0 ? "Error" : String.format("$%,.2f", currentPrice),
                currentPrice < 0 ? "Error" : String.format("$%,.2f", value)
            });
        }

        // 6. ROI 계산: (현재자산총액 - 초기투자금) / 초기투자금 * 100
        //    초기투자금: 5,000 USD (하드코딩)
        double initialInvestment = 5000.00;
        double currentAssets = currentBalance + totalHoldingsValue;
        double roi = Math.round(((currentAssets - initialInvestment) / initialInvestment) * 10000.0) / 100.0;

        // 7. ROI 라벨 업데이트
        String roiText = String.format("ROI: %.2f%%", roi);
        roiLabel.setText(roiText);
        if (roi >= 0) {
            roiLabel.setForeground(new Color(0, 200, 0)); // 녹색
        } else {
            roiLabel.setForeground(new Color(200, 0, 0)); // 빨강
        }
    }
}
