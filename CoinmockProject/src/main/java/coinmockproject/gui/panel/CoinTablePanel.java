package coinmockproject.gui.panel;

import coinmockproject.model.Coin;
import coinmockproject.model.User;
import coinmockproject.service.CoinAPIService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CoinTablePanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private Timer refreshTimer;

    /**
     * User가 있을 수도, 없을 수도 있는 단일 생성자.
     * user == null 이면 상단 라벨은 생략.
     */
    public CoinTablePanel(User user) {
        // 1) 레이아웃 한 번만 설정
        setLayout(new BorderLayout());

        // 2) (선택) User 라벨
        if (user != null) {
            JLabel label = new JLabel(user.getUsername() + "님의 보유 코인");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setFont(new Font("맑은 고딕", Font.BOLD, 16));
            label.setForeground(Color.WHITE);
            add(label, BorderLayout.NORTH);
        }

        // 3) 테이블 & 모델 초기화
        tableModel = new DefaultTableModel(new Object[]{"코인명", "심볼", "가격(USD)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 4) 즉시 데이터 로드
        refreshData();

        // 5) 주기적 갱신 (10초마다)
        refreshTimer = new Timer(10_000, e -> refreshData());
        refreshTimer.start();
    }

    /** 
     * CoinAPIService에서 가져온 배열로 테이블을 갱신.
     * 반드시 EDT에서 호출되도록 invokeLater 사용.
     */
    private void refreshData() {
        SwingUtilities.invokeLater(() -> {
            Coin[] coins;
			try {
				coins = CoinAPIService.fetchCoins();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(
			            this,
			            "시세를 불러오는 중 오류 발생:\n" + e.getMessage(),
			            "Error",
			            JOptionPane.ERROR_MESSAGE
			        );
			        return;  // 예외 시 더 이상 진행하지 않도록
			}
            updateTable(coins);
        });
    }

    public void updateTable(Coin[] coins) {
        tableModel.setRowCount(0);
        for (Coin coin : coins) {
            tableModel.addRow(new Object[]{
                coin.getName(),
                coin.getSymbol(),
                coin.getPriceUsd() < 0
                    ? "Error"
                    : String.format("$%,.2f", coin.getPriceUsd())
            });
        }
    }
}
