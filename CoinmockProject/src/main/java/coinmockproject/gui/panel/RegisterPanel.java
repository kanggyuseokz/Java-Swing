package coinmockproject.gui.panel;

import coinmockproject.db.*;
import coinmockproject.gui.*;
import coinmockproject.model.User;
import coinmockproject.util.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
@AllArgsConstructor
public class RegisterPanel extends JPanel {
	private JTextField usernameField;
	private JPasswordField passwordField, confirmPasswordField;
	private JButton registerBtn, goLoginBtn;
	private UserRepository userRepo = new UserRepository();
	private MainWindow mainWindow;

	public RegisterPanel(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		setLayout(null);
		setBounds(0, 0, 800, 480);
		setBackground(ColorTheme.GREY);
		JLabel title = new JLabel("Sign Up");
		title.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		title.setBounds(360, 60, 120, 30);

		JLabel idLabel = new JLabel("ID");
		idLabel.setBounds(300, 110, 60, 20);
		usernameField = new JTextField();
		usernameField.setBounds(300, 130, 200, 30);
		usernameField.setBorder(null);
		usernameField.setOpaque(true);

		JLabel pwLabel = new JLabel("PASSWORD");
		pwLabel.setBounds(300, 170, 120, 20);
		passwordField = new JPasswordField();
		passwordField.setBounds(300, 190, 200, 30);
		passwordField.setBorder(null);
		passwordField.setOpaque(true);

		confirmPasswordField = new JPasswordField();
		confirmPasswordField.setBounds(300, 230, 200, 30);
		confirmPasswordField.setBorder(null);

		registerBtn = new JButton("Sign Up");
		registerBtn.setBounds(320, 280, 160, 30);
		registerBtn.setBackground(ColorTheme.SLATE_GREY);
		registerBtn.setFont(new Font("맑은 고딕", Font.BOLD, 13));
		registerBtn.setForeground(Color.WHITE);

		goLoginBtn = new JButton("Back to Login");
		goLoginBtn.setBounds(320, 320, 160, 30);
		goLoginBtn.setBackground(ColorTheme.SLATE_GREY);
		goLoginBtn.setForeground(Color.WHITE);
		goLoginBtn.setFont(new Font("맑은 고딕", Font.BOLD, 13));

		// 버튼 동작
		registerBtn.addActionListener(e -> {
			String username = usernameField.getText().trim();
			String password = new String(passwordField.getPassword()).trim();
			String confirm = new String(confirmPasswordField.getPassword()).trim();

			// 입력값 검증
			if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
			    DialogUtil.showWarning(this, "모든 입력 필드를 채워주세요.");
			    return;
			}

			if (!password.equals(confirm)) {
			    DialogUtil.showError(this, "비밀번호가 일치하지 않습니다.");
			    return;
			}

			// 회원가입 시도
			User registered = userRepo.registerUser(username, password);
			if (registered != null) {
			    DialogUtil.showSuccess(this, "회원가입 성공!");
			    mainWindow.showLogin();
			} else {
			    DialogUtil.showError(this, "회원가입 실패: 아이디가 이미 존재하거나 DB 오류");
			}
		});

		goLoginBtn.addActionListener(e -> mainWindow.showLogin());

		// 컴포넌트 추가 
		add(title);
		add(idLabel);
		add(usernameField);
		add(pwLabel);
		add(passwordField);
		add(registerBtn);
		add(goLoginBtn);
		add(confirmPasswordField);
	}
}