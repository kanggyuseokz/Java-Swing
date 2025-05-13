package coinmockproject.gui;

import coinmockproject.db.*;
import coinmockproject.model.*;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
public class LoginPanel extends JPanel {
	public LoginPanel(MainWindow mainWindow) {
		setLayout(null);
		setBounds(0, 20, 800, 480); // 프레임 내부에서 명확히 보이게
		
		// 입력창 및 버튼 선언
		JTextField idField = new JTextField();
		JPasswordField pwField = new JPasswordField();
		JButton loginBtn = new JButton("로그인");

		// 위치 설정
		idField.setBounds(300, 180, 200, 30);
		pwField.setBounds(300, 220, 200, 30);
		loginBtn.setBounds(350, 270, 100, 30);

		// 컴포넌트 추가
		add(idField);
		add(pwField);
		add(loginBtn);

		// 로그인 버튼 클릭 시 실행
		loginBtn.addActionListener(e -> {
			// 여기에 로그인 로직 추가 필요 (DB 연동 등)
			String username = idField.getText();

			// 임시로 유저 객체 생성 후 메인화면으로 이동
			User user = new User(1, username, "dummy");
			mainWindow.showMainUI(user);
		});
	}
}