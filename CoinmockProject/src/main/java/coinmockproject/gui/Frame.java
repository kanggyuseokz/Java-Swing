package coinmockproject.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class Frame extends JFrame {
	Color dimpGrey = new Color(59, 59, 59);
	Color darkGrey = new Color(169, 169, 169);
	Color grey = new Color(128, 128, 128);
	Color silver = new Color(192, 192, 192);
	Color slateGrey = new Color(112, 128, 144);

	private JPanel titlePanel;
	public JPanel mainPanel;
	private JLabel exit, mini, title;

	private Point comPoint;

	public Frame() {
		setUndecorated(true);
		setSize(800, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		getContentPane().setLayout(null);
		
		setTitleBar();
		mainPanel = new JPanel();
		mainPanel.setBounds(0, 20, 800, 480); // titlePanel 아래에 위치
		mainPanel.setLayout(null);
		mainPanel.setBackground(grey);
	    getContentPane().add(mainPanel);

	}

	public void setTitleBar() {
		titlePanel = new JPanel();
		titlePanel.setBounds(0, 0 , 800, 20);
		titlePanel.setBackground(darkGrey);
		titlePanel.setLayout(null);
		titlePanel.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				comPoint = null;
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				comPoint = e.getPoint();
			}
			
		});
		titlePanel.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				Point current = e.getLocationOnScreen();
				setLocation(current.x - comPoint.x, current.y - comPoint.y);
			}
		});
		
		exit = new JLabel("X");
		exit.setBounds(780, 0, 20, 20);
		exit.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		
		title = new JLabel("코인 모의 투자");
		title.setBounds(10, 0, 400, 20);
		
		titlePanel.add(exit);
		titlePanel.add(title); 
		getContentPane().add(titlePanel);
	}

}