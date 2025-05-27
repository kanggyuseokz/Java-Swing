package coinmockproject.gui;

import coinmockproject.model.Coin;
import coinmockproject.service.CoinAPIService;
import coinmockproject.model.*;
import coinmockproject.db.*;
import coinmockproject.gui.*;
import coinmockproject.gui.panel.CoinTablePanel;
import coinmockproject.gui.panel.LoginPanel;
import coinmockproject.gui.panel.RegisterPanel;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainWindow extends Frame {
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

	public void showMainUI(User user) {
	    mainPanel.removeAll();
	    
	    CoinTablePanel panel = new CoinTablePanel(user);
	    panel.setBounds(0, 0, 800, 480);
	    Coin[] dummyCoins = {
	    	    new Coin("Bitcoin", "BTC", 67000.00),
	    	    new Coin("Ethereum", "ETH", 3500.50)
	    	};
	    panel.updateTable(dummyCoins);
	    
	    mainPanel.add(panel);
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