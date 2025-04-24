package coinmockproject.gui;

import coinmockproject.model.Coin;
import coinmockproject.service.CoinAPIService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainWindow extends JFrame {
    private CoinTablePanel coinTablePanel;
    private Timer timer;
    private JLabel updateTimeLabel;

    public MainWindow() {
        setTitle("코인 시세 테이블");
        setSize(420, 260);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        // 상단 타이틀
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("실시간 코인 시세", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        updateTimeLabel = new JLabel("업데이트: -", SwingConstants.RIGHT);
        topPanel.add(updateTimeLabel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // 코인 테이블 패널
        coinTablePanel = new CoinTablePanel();
        add(coinTablePanel, BorderLayout.CENTER);

        // 수동 새로고침 버튼
        JButton refreshBtn = new JButton("새로고침");
        add(refreshBtn, BorderLayout.SOUTH);
        refreshBtn.addActionListener(e -> updateTable());

        // 5초마다 자동 갱신 타이머
        timer = new Timer(5000, (ActionEvent e) -> updateTable());
        timer.setInitialDelay(0);
        timer.start();
    }

    private void updateTable() {
        SwingWorker<Coin[], Void> worker = new SwingWorker<Coin[], Void>() {
            @Override
            protected Coin[] doInBackground() {
                return CoinAPIService.fetchCoins();
            }
            @Override
            protected void done() {
                try {
                    Coin[] coins = get();
                    coinTablePanel.updateTable(coins);
                    updateTimeLabel.setText("업데이트: " + java.time.LocalTime.now().withNano(0));
                } catch (Exception e) {
                    updateTimeLabel.setText("업데이트 실패: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    public static void main(String[] args) {
        // org.json 라이브러리 classpath 필요!
        SwingUtilities.invokeLater(() -> {
            new MainWindow().setVisible(true);
        });
    }
}