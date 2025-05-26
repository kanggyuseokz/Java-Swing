package coinmockproject.gui.panel;

import coinmockproject.db.*;
import coinmockproject.model.*;
import coinmockproject.gui.*;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@Getter
@Setter
public class LoginPanel extends JPanel {
	private MainWindow mainWindow;

	public LoginPanel(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		setLayout(null);
		setBounds(0, 20, 800, 480); // 프레임 내부에서 명확히 보이게

		// 입력창 및 버튼 선언
		JTextField idField = new JTextField();
		JPasswordField pwField = new JPasswordField();
		JButton loginBtn = new JButton("Login");
		JLabel regLbl = new JLabel("Sign Up");
		JLabel title = new JLabel("Login");
		JLabel idLabel = new JLabel("ID");
		JLabel pwLabel = new JLabel("PASSWORD");
		loginBtn.setBackground(ColorTheme.SLATE_GREY);

		// 위치 설정
		idLabel.setBounds(300, 130, 120, 20);
		idField.setBorder(null);
		idField.setBounds(300, 150, 200, 30);
		pwLabel.setBounds(300, 180, 120, 20);
		pwField.setBorder(null);
		pwField.setBounds(300, 200, 200, 30);
		loginBtn.setBounds(350, 270, 100, 30);
		loginBtn.setForeground(Color.WHITE);
		loginBtn.setFont(new Font("맑은 고딕", Font.BOLD, 13));
		regLbl.setBounds(450, 230, 60, 20);
		regLbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
		regLbl.setForeground(Color.white);
		regLbl.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		title.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		title.setBounds(370, 60, 120, 30);

		// 컴포넌트 추가
		add(idField);
		add(pwField);
		add(loginBtn);
		add(regLbl);
		add(title);
		add(idLabel);
		add(pwLabel);
		
		// 로그인 버튼 클릭 시 실행
		loginBtn.addActionListener(e -> {
			String username = idField.getText();
			String password = String.valueOf(pwField.getPassword());

			// DB에서 사용자 조회
			User user = UserRepository.findByUsername(username);

			if (user != null && user.getPassword().equals(password)) {
				// 로그인 성공
				JOptionPane.showMessageDialog(this, "로그인 성공!");
				mainWindow.showMainUI(user);
			} else {
				// 로그인 실패
				JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 틀렸습니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
			}

			if (username.isEmpty() || password.isEmpty()) {
				JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 입력해주세요.");
				return;
			}
		});

		regLbl.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				regLbl.setText("Sign Up");
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				regLbl.setText("<html><u>Sign Up</u></html>");
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				mainWindow.showRegisterPanel();
			}
		});
	}
}