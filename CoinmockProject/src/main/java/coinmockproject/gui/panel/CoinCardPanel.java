package coinmockproject.gui.panel;

import coinmockproject.gui.*;
import coinmockproject.gui.panel.PurchaseFrame;
import coinmockproject.model.*;
import coinmockproject.service.CoinAPIService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * CoinCardPanel: 코인 카드들을 그리드 형태로 보여주며,
 * 상단에 '나의 투자 현황' 라벨을 클릭하면 PortfolioPanel로 전환합니다.
 * 각 코인 카드 전체를 클릭(또는 마우스 오버)하면 스타일이 바뀌고,
 * 클릭 시 PurchaseFrame을 띄워 구매할 수 있도록 합니다.
 */
public class CoinCardPanel extends JPanel {
    private final MainWindow mainWindow;
    private final User user;

    public CoinCardPanel(MainWindow mainWindow, User user) {
        this.mainWindow = mainWindow;
        this.user = user;

        setLayout(new BorderLayout());
        setBackground(ColorTheme.DIMP_GREY);

        initHeader();
        initCoinCards();
    }

    // 1. 상단 헤더: '나의 투자 현황' 라벨
    private void initHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        header.setBackground(ColorTheme.DIMP_GREY);

        JLabel investStatus = new JLabel("나의 투자 현황");
        investStatus.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        investStatus.setForeground(Color.WHITE);
        investStatus.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        investStatus.setBorder(new EmptyBorder(5, 10, 5, 10));

        investStatus.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainWindow.showPortfolioPanel();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                investStatus.setForeground(Color.CYAN);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                investStatus.setForeground(Color.WHITE);
            }
        });

        header.add(investStatus);
        add(header, BorderLayout.NORTH);
    }

    // 2. 중앙: 코인 카드 그리드 표시
    private void initCoinCards() {
        JPanel cardContainer = new JPanel(new GridLayout(0, 3, 10, 10));
        cardContainer.setBackground(ColorTheme.DIMP_GREY);
        cardContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Coin[] coinArray;
		try {
			coinArray = CoinAPIService.fetchCoins();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(
		            this,
		            "시세를 불러오는 중 오류 발생:\n" + e.getMessage(),
		            "Error",
		            JOptionPane.ERROR_MESSAGE
		        );
		        return;  // 예외 시 더 이상 진행하지 않도록
		}
        List<Coin> allCoins = Arrays.asList(coinArray);

        for (Coin coin : allCoins) {
            CoinCard card = new CoinCard(coin);
            cardContainer.add(card);
        }

        JScrollPane scrollPane = new JScrollPane(cardContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    // 3. CoinCard 컴포넌트: 카드 전체 클릭 및 호버 시 스타일 변경 + 구매창 띄우기
    private class CoinCard extends JPanel {
        private final Color normalBg = new Color(50, 50, 50);
        private final Color hoverBg = new Color(70, 70, 70);
        private final Color normalBorder = Color.DARK_GRAY;
        private final Color hoverBorder = Color.CYAN;

        public CoinCard(Coin coin) {
            setPreferredSize(new Dimension(200, 180));
            setBackground(normalBg);
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createLineBorder(normalBorder, 1));

            // 3-1) 이미지 레이블
            JLabel imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setBorder(new EmptyBorder(5, 5, 5, 5));

            ImageIcon icon = loadCoinIcon(coin.getName());
            if (icon != null) {
                Image scaled = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaled));
            }
            add(imageLabel, BorderLayout.NORTH);

            // 3-2) 이름·심볼·가격을 세로로 배치할 infoPanel
            JPanel infoPanel = new JPanel();
            infoPanel.setBackground(normalBg);
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

            JLabel nameLabel = new JLabel(coin.getName(), SwingConstants.CENTER);
            nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            nameLabel.setBorder(new EmptyBorder(3, 0, 3, 0));
            infoPanel.add(nameLabel);

            JLabel symbolLabel = new JLabel(coin.getSymbol().toUpperCase(), SwingConstants.CENTER);
            symbolLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
            symbolLabel.setForeground(Color.LIGHT_GRAY);
            symbolLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            infoPanel.add(symbolLabel);

            JLabel priceLabel = new JLabel("$" + String.format("%,.2f", coin.getPriceUsd()), SwingConstants.CENTER);
            priceLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
            priceLabel.setForeground(Color.GREEN);
            priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            priceLabel.setBorder(new EmptyBorder(3, 0, 3, 0));
            infoPanel.add(priceLabel);

            add(infoPanel, BorderLayout.CENTER);

            // 3-3) 카드 전체에 마우스 리스너 부착
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverBg);
                    setBorder(BorderFactory.createLineBorder(hoverBorder, 1));
                    infoPanel.setBackground(hoverBg);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(normalBg);
                    setBorder(BorderFactory.createLineBorder(normalBorder, 1));
                    infoPanel.setBackground(normalBg);
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    PurchaseFrame purchaseFrame = new PurchaseFrame(user, coin);
                    purchaseFrame.setVisible(true);
                }
            });
        }
    }

    /**
     * coin.getId() 값에 맞춰 클래스패스(/img/)에서 아이콘을 로드합니다.
     * 예: "bitcoin" → "/img/bitcoin.png"
     * 해당 리소스가 없으면 "/img/error.png"로 대신 로드합니다.
     */
    private ImageIcon loadCoinIcon(String coinSymbol) {
        String basePath = "/img/";
        String fileName = coinSymbol + ".png";
        String resourcePath = basePath + fileName;

        URL imgUrl = getClass().getResource(resourcePath);
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
