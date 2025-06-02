	package coinmockproject.gui.panel;

import coinmockproject.model.Coin;
import coinmockproject.model.User;
import coinmockproject.service.PortfolioService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 코인 구매 전용 프레임 (PortfolioService를 이용해 실제 구매 로직 연동)
 */
public class PurchaseFrame extends JFrame {
    private final User user;
    private final Coin coin;

    private JLabel nameLabel;
    private JLabel priceLabel;
    private JTextField amountField;
    private JButton buyButton;
    private JLabel balanceLabel;

    public PurchaseFrame(User user, Coin coin) {
        this.user = user;
        this.coin = coin;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Purchase - " + coin.getName());
        setSize(400, 300);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 1) 코인 정보 + 사용자 잔액 패널 (TOP)
        JPanel coinInfoPanel = new JPanel();
        coinInfoPanel.setLayout(new BoxLayout(coinInfoPanel, BoxLayout.Y_AXIS));
        coinInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        nameLabel = new JLabel("코인: " + coin.getName());
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        priceLabel = new JLabel(
                "현재 가격: " + (coin.getPriceUsd() < 0
                        ? "Error"
                        : String.format("$%,.2f", coin.getPriceUsd()))
        );
        priceLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // **추가** - User의 현재 잔액을 표시
        balanceLabel = new JLabel(
                String.format("잔액(USD): $%,.2f", user.getBalance())
        );
        balanceLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        balanceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        coinInfoPanel.add(nameLabel);
        coinInfoPanel.add(Box.createVerticalStrut(8));
        coinInfoPanel.add(priceLabel);
        coinInfoPanel.add(Box.createVerticalStrut(8));
        coinInfoPanel.add(balanceLabel);

        add(coinInfoPanel, BorderLayout.NORTH);

        // 2) 입력 필드 패널 (CENTER)
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel amountLabel = new JLabel("구매 수량:");
        amountLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(amountLabel, gbc);

        amountField = new JTextField(12);
        amountField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(amountField, gbc);

        add(inputPanel, BorderLayout.CENTER);

        // 3) 버튼 패널 (BOTTOM)
        JPanel buttonPanel = new JPanel();
        buyButton = new JButton("구매하기");
        buyButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        buyButton.setBackground(new Color(70, 130, 180));
        buyButton.setForeground(Color.WHITE);
        buyButton.setFocusPainted(false);

        // 버튼 클릭 시 실제 구매 로직 수행
        buyButton.addActionListener(e -> onBuyClicked());

        buttonPanel.add(buyButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // 창을 화면 중앙에 띄우기
        setLocationRelativeTo(null);
    }

    /** “구매하기” 버튼 클릭 핸들러 (PortfolioService와 연동) */
    private void onBuyClicked() {
        String input = amountField.getText().trim();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "구매 수량을 입력해주세요.",
                    "입력 오류",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(input);
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "유효한 숫자(0보다 큰 실수)를 입력해주세요.",
                    "입력 오류",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // 실제 구매 로직 호출: PortfolioService.buyCoin(user, coin, amount)
        boolean success = PortfolioService.getInstance().buyCoin(user, coin, amount);
        if (!success) {
            // 잔액 부족 또는 기타 오류
            JOptionPane.showMessageDialog(
                    this,
                    "잔액이 부족하거나 구매할 수 없습니다.\n현재 잔액: $" 
                    + String.format("%,.2f", user.getBalance()),
                    "구매 실패",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // 구매가 성공했을 때:  
        // 1) User의 잔액, 보유 코인 정보가 이미 PortfolioService에서 업데이트됨  
        // 2) 화면에 새 잔액을 표시  
        balanceLabel.setText(String.format("잔액(USD): $%,.2f", user.getBalance()));

        // 3) 성공 메시지
        JOptionPane.showMessageDialog(
                this,
                String.format(
                    "[구매 완료]\n코인: %s\n수량: %.6f\n총 금액: $%,.2f\n\n남은 잔액: $%,.2f",
                    coin.getName(),
                    amount,
                    amount * coin.getPriceUsd(),
                    user.getBalance()
                ),
                "구매 완료",
                JOptionPane.INFORMATION_MESSAGE
        );

        // (선택) DB에 반영되었는지 확인용  
        PortfolioService.getInstance().printUserPortfolioAndHistory(user);

        // 4) 구매 후 창 닫기 또는 입력 필드 초기화
        dispose();
    }
}
