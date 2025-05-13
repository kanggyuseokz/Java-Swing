package coinmockproject.gui;

import coinmockproject.model.Coin;
import coinmockproject.service.CoinAPIService;
import coinmockproject.model.*;
import coinmockproject.db.*;
import coinmockproject.gui.Frame;
import coinmockproject.gui.CoinTablePanel;

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
        panel.setBounds(0, 20, 800, 480); // titlePanel 아래 영역 고려
        getContentPane().add(panel);
        mainPanel.revalidate();
        repaint();
    }

    public void showMainUI(User user) {
    	mainPanel.removeAll();
    	mainPanel.add(new CoinTablePanel(user));
        revalidate();
        repaint();
    }

    public void showRegisterPanel() {
    	mainPanel.removeAll();
    	mainPanel.add(new RegisterPanel(this));
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        new MainWindow();
    }
}