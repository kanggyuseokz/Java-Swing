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
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginBtn, goRegisterBtn;
    private UserRepository userRepo = new UserRepository();

    public LoginPanel(MainWindow mainWindow) {
        setLayout(new GridLayout(4, 1, 10, 10));
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginBtn = new JButton("로그인");
        goRegisterBtn = new JButton("회원가입");

        add(new JLabel("아이디:"));
        add(usernameField);
        add(new JLabel("비밀번호:"));
        add(passwordField);
        add(loginBtn);
        add(goRegisterBtn);

        loginBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            User user = userRepo.login(username, password);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "로그인 성공!");
                mainWindow.onLoginSuccess(user);
            } else {
                JOptionPane.showMessageDialog(this, "로그인 실패! 아이디/비밀번호 확인");
            }
        });

        goRegisterBtn.addActionListener(e -> mainWindow.showRegisterPanel());
    }
}