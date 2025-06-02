package coinmockproject.gui;

import coinmockproject.model.Coin;
import coinmockproject.service.CoinAPIService;
import coinmockproject.model.*;
import coinmockproject.db.*;
import coinmockproject.gui.*;
import coinmockproject.gui.panel.CoinCardPanel;
import coinmockproject.gui.panel.LoginPanel;
import coinmockproject.gui.panel.PortfolioPanel;
import coinmockproject.gui.panel.RegisterPanel;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainWindow extends Frame {
    private User loggedInUser;
	public MainWindow() {
		setTitleBar();
		showLogin(); // 시작 시 로그인 화면
		setVisible(true); // Frame에서 호출하지 않을 경우 필요
	}

	public void showLogin() {
		mainPanel.removeAll();
		getContentPane().setLayout(null); // 중요: 수동 배치할 때 필요
		LoginPanel panel = new LoginPanel(this);
		panel.setBounds(0, 0, 800, 480); // titlePanel 아래 영역 고려
		panel.setBackground(ColorTheme.GREY);
		mainPanel.add(panel);
		mainPanel.revalidate();
		mainPanel.repaint();
	}

	public void showCoinCardPanel(User user) {
        this.loggedInUser = user;
        mainPanel.removeAll();
        CoinCardPanel coinPanel = new CoinCardPanel(this, loggedInUser);
        coinPanel.setBounds(0, 0, 800, 480);
        mainPanel.add(coinPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
	
    public void showPortfolioPanel() {
        mainPanel.removeAll();
        PortfolioPanel portfolioPanel = new PortfolioPanel(this, loggedInUser);
        portfolioPanel.setBounds(0, 0, 800, 480);
        mainPanel.add(portfolioPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

	public void showRegisterPanel() {
		mainPanel.removeAll();
		mainPanel.add(new RegisterPanel(this));
		revalidate();
		repaint();
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new FlatDarkLaf());
		} catch (Exception ex) {
			System.err.println("FlatLaf 설정 실패: " + ex);
		}
		new MainWindow();
	}
}