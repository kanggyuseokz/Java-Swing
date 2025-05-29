package coinmockproject.gui.panel;

import coinmockproject.model.Coin;
import coinmockproject.model.User;
import coinmockproject.service.CoinAPIService;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class CoinCardPanel extends JPanel {
    private final JPanel cardsContainer;
    private final Timer refreshTimer;

    /**
     * User가 null 이면 헤더 라벨 없이, 
     * user 정보가 있으면 "username님의 보유 코인"을 상단에 표시합니다.
     */
    public CoinCardPanel(User user) {
        setLayout(new BorderLayout());
        setBackground(new Color(45, 45, 45));

        // 1) Optional Header
        if (user != null) {
            JLabel header = new JLabel(user.getUsername() + "님의 보유 코인");
            header.setHorizontalAlignment(SwingConstants.CENTER);
            header.setFont(new Font("맑은 고딕", Font.BOLD, 16));
            header.setForeground(Color.WHITE);
            header.setBorder(new EmptyBorder(10, 0, 10, 0));
            add(header, BorderLayout.NORTH);
        }

        // 2) 카드 컨테이너: 0행 3열, gap 10px
        cardsContainer = new JPanel(new GridLayout(0, 3, 10, 10));
        cardsContainer.setBackground(new Color(45, 45, 45));
        cardsContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 3) 스크롤 페인에 담기
        JScrollPane scroll = new JScrollPane(cardsContainer,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        // 4) 초기 로드 & 5) 주기 갱신
        refreshData();
        refreshTimer = new Timer(10_000, e -> refreshData());
        refreshTimer.start();
    }

    /** EDT에서 안전하게 코인 데이터를 가져와 카드로 다시 그립니다. */
    private void refreshData() {
        SwingUtilities.invokeLater(() -> {
            cardsContainer.removeAll();

            Coin[] coins = CoinAPIService.fetchCoins();
            for (Coin coin : coins) {
                cardsContainer.add(createCard(coin));
            }

            cardsContainer.revalidate();
            cardsContainer.repaint();
        });
    }

    /** 개별 코인 정보를 담는 카드 컴포넌트 생성 */
    private JPanel createCard(Coin coin) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(180, 120));
        card.setBackground(new Color(60, 63, 65));
        card.setBorder(new CompoundBorder(
                new EmptyBorder(8, 8, 8, 8),
                new LineBorder(Color.LIGHT_GRAY, 1, true)
        ));

        // 코인명
        JLabel name = new JLabel(coin.getName(), SwingConstants.CENTER);
        name.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        name.setForeground(Color.WHITE);

        // 심볼
        JLabel symbol = new JLabel(coin.getSymbol(), SwingConstants.CENTER);
        symbol.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        symbol.setForeground(Color.LIGHT_GRAY);

        // 가격
        String priceText = coin.getPriceUsd() < 0
                ? "Error"
                : String.format("$%,.2f", coin.getPriceUsd());
        JLabel price = new JLabel(priceText, SwingConstants.CENTER);
        price.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        price.setForeground(new Color(100, 255, 100));

        // 내부 레이아웃
        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.add(name);
        info.add(Box.createVerticalStrut(4));
        info.add(symbol);
        info.add(Box.createVerticalStrut(8));
        info.add(price);

        card.add(info, BorderLayout.CENTER);
        return card;
    }
}
