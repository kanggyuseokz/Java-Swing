package coinmockproject.gui.panel;

import coinmockproject.gui.ColorTheme;
import coinmockproject.gui.MainWindow;
import coinmockproject.model.User;
import coinmockproject.model.Coin;
import coinmockproject.service.PortfolioService;
import coinmockproject.service.CoinAPIService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * PortfolioPanel: 사용자의 잔고, ROI, 보유 코인 목록을 보여주며,
 * 하단에 '투자하기' 버튼을 두어 CoinCardPanel로 돌아갈 수 있음
 */
public class PortfolioPanel extends JPanel {
    private MainWindow mainWindow;
    private User user;
    private PortfolioService portfolioService;

    private JLabel balanceLabel;
    private JLabel roiLabel;
    private JTable holdingsTable;
    private DefaultTableModel tableModel;
    private JButton investBtn;

    public PortfolioPanel(MainWindow mainWindow, User user) {
        this.mainWindow = mainWindow;
        this.user = user;
        this.portfolioService = PortfolioService.getInstance();

        setLayout(new BorderLayout());
        setBackground(ColorTheme.GREY);

        initHeader();
        initHoldingsTable();
        initFooter();
        loadData();
    }

    // 1. 상단 헤더: 사용자 잔액과 ROI
    private void initHeader() {
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.setBackground(ColorTheme.DIMP_GREY);
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
    }

    // 2. 중앙: 보유 코인 테이블
    private void initHoldingsTable() {
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

    // 3. 하단: '투자하기' 버튼
    private void initFooter() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(ColorTheme.GREY);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        investBtn = new JButton("투자하기");
        investBtn.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        investBtn.setBackground(new Color(70, 130, 180));
        investBtn.setForeground(Color.WHITE);
        investBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        investBtn.addActionListener(e -> {
            // 클릭 시 CoinCardPanel로 전환
            mainWindow.showCoinCardPanel(user);
        });

        bottomPanel.add(investBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // 4. 데이터 로딩: 잔액, ROI, 보유 코인 목록 업데이트
    private void loadData() {
        // 4-1. 잔액
        double currentBalance = user.getBalance();
        balanceLabel.setText(user.getUsername() + "님 잔액: $" + String.format("%,.2f", currentBalance));

        // 4-2. 현재 보유량 조회
        Map<String, Double> holdings = portfolioService.getCurrentHoldings(user);

        // 4-3. 코인 시세 호출(Coin[] → List<Coin>)
        Coin[] coinArray = CoinAPIService.fetchCoins();
        List<Coin> allCoins = Arrays.asList(coinArray);

        // 4-4. 테이블 초기화
        tableModel.setRowCount(0);

        double totalHoldingsValue = 0.0;

        // 4-5. 보유 코인별 행 추가
        for (Map.Entry<String, Double> entry : holdings.entrySet()) {
            String symbol = entry.getKey();
            double amount = entry.getValue();

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

        // 4-6. ROI 계산: (현재자산총액 - 초기투자금) / 초기투자금 * 100
        double initialInvestment = 5000.00;
        double currentAssets = currentBalance + totalHoldingsValue;
        double roi = Math.round(((currentAssets - initialInvestment) / initialInvestment) * 10000.0) / 100.0;

        String roiText = String.format("ROI: %.2f%%", roi);
        roiLabel.setText(roiText);
        if (roi >= 0) {
            roiLabel.setForeground(new Color(0, 200, 0));
        } else {
            roiLabel.setForeground(new Color(200, 0, 0));
        }
    }
}