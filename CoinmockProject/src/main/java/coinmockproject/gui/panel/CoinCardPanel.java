package coinmockproject.gui.panel;

import coinmockproject.model.Coin;
import coinmockproject.model.User;
import coinmockproject.service.CoinAPIService;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CoinCardPanel extends JPanel {
    private final JPanel cardsContainer;
    private final Timer refreshTimer;
    private final User user;

    public CoinCardPanel(User user) {
    	this.user = user;
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

    /** 개별 코인 정보를 담는 카드 컴포넌트 생성: 텍스트를 완전히 가운데 정렬 */
    private JPanel createCard(Coin coin) {
        // 1) 카드 JPanel 기본 설정
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(180, 180));
        card.setBackground(new Color(60, 63, 65));
        card.setBorder(new CompoundBorder(
                new EmptyBorder(8, 8, 8, 8),
                new LineBorder(Color.LIGHT_GRAY, 1, true)
        ));

        // 2) 이미지 로딩 및 JLabel 생성
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(new EmptyBorder(5, 5, 5, 5));

        ImageIcon icon = loadCoinIcon(coin.getSymbol());
        if (icon != null) {
            // 아이콘 크기 조정 (너비 64px, 높이 64px)
            Image scaled = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
        }
        card.add(imageLabel, BorderLayout.NORTH);

        // 3) 코인명, 심볼, 가격 표시용 패널 (BoxLayout.Y_AXIS)
        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        // 3-1) 코인명
        JLabel nameLabel = new JLabel(coin.getName());
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // ← 수평 중앙 정렬

        // 3-2) 심볼
        JLabel symbolLabel = new JLabel(coin.getSymbol());
        symbolLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        symbolLabel.setForeground(Color.LIGHT_GRAY);
        symbolLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // ← 수평 중앙 정렬

        // 3-3) 가격
        String priceText = coin.getPriceUsd() < 0
                ? "Error"
                : String.format("$%,.2f", coin.getPriceUsd());
        JLabel priceLabel = new JLabel(priceText);
        priceLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        priceLabel.setForeground(new Color(100, 255, 100));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // ← 수평 중앙 정렬

        // 4) info 패널에 순서대로 추가 (간격을 위해 스트럿 사용)
        info.add(Box.createVerticalStrut(8));
        info.add(nameLabel);
        info.add(Box.createVerticalStrut(4));
        info.add(symbolLabel);
        info.add(Box.createVerticalStrut(6));
        info.add(priceLabel);
        info.add(Box.createVerticalGlue());

        card.add(info, BorderLayout.CENTER);
        
        //    - 커서 모양은 손가락 모양으로 변경
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
        	 public void mouseClicked(MouseEvent e) {
                 // 카드 클릭 시 PurchaseFrame을 띄우기
                 SwingUtilities.invokeLater(() -> {
                     PurchaseFrame purchaseFrame = new PurchaseFrame(user, coin);
                     purchaseFrame.setLocationRelativeTo(card); // 카드 위치 근처에 프레임 띄우기
                     purchaseFrame.setVisible(true);
                 });
             }
             @Override
             public void mouseEntered(MouseEvent e) {
                 card.setBackground(new Color(75, 78, 80)); // hover 시 약간 밝게
             }
             @Override
             public void mouseExited(MouseEvent e) {
                 card.setBackground(new Color(60, 63, 65)); // 원래 색상으로 복구
             }
         });
        return card;
    }

    /**
     * 코인 id에 해당하는 아이콘을 클래스패스(/img/)에서 로드해서 반환합니다.
     * coin.getId() 값이 파일명(확장자 제외)과 일치해야 합니다.
     * 예) "bitcoin" → "/img/bitcoin.png"
     * 일치하는 리소스가 없으면 "/img/error.png"를 대신 반환합니다.
     */
    private ImageIcon loadCoinIcon(String coinId) {
        String basePath = "/img/";
        String fileName = coinId + ".png";           
        String resourcePath = basePath + fileName;

        java.net.URL imgUrl = getClass().getResource(resourcePath);
        if (imgUrl == null) {
            imgUrl = getClass().getResource(basePath + "error.png");
        }

        if (imgUrl != null) {
            return new ImageIcon(imgUrl);
        } else {
            return null;
        }
    }
}
