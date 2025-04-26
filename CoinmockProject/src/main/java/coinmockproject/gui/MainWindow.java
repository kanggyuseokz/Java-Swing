package coinmockproject.gui;

import coinmockproject.model.Coin;
import coinmockproject.service.CoinAPIService;
import coinmockproject.model.*;
import coinmockproject.db.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainWindow extends JFrame {
    private JPanel mainPanel;

    public MainWindow() {
        setTitle("코인 모의투자");
        setSize(500, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        mainPanel = new JPanel(new CardLayout());
        setContentPane(mainPanel);

        showLoginPanel();
    }

    // 로그인 화면 표시
    public void showLoginPanel() {
        mainPanel.removeAll();
        mainPanel.add(new LoginPanel(this));
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // 회원가입 화면 표시
    public void showRegisterPanel() {
        mainPanel.removeAll();
        mainPanel.add(new RegisterPanel(this));
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // 로그인 성공 후 실제 앱 기능으로 이동
    public void onLoginSuccess(User user) {
        mainPanel.removeAll();
        // 예시: 코인 시세, 포트폴리오, 거래 내역 패널 등 통합
        mainPanel.add(new JLabel("로그인 환영, " + user.getUsername() + "님!"));
        // 실제로는 CoinTablePanel, PortfolioPanel 등 붙이면 됨
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}