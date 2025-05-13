package coinmockproject.gui;

import coinmockproject.db.UserRepository;
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
    private JPasswordField passwordField;
    private JButton registerBtn, goLoginBtn;
    private UserRepository userRepo = new UserRepository();

    public RegisterPanel(MainWindow mainWindow) {
        setLayout(new GridLayout(4, 1, 10, 10));
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        registerBtn = new JButton("회원가입");
        goLoginBtn = new JButton("로그인으로");

        add(new JLabel("아이디:"));
        add(usernameField);
        add(new JLabel("비밀번호:"));
        add(passwordField);
        add(registerBtn);
        add(goLoginBtn);

        registerBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            boolean success = userRepo.registerUser(username, password);
            if (success) {
                JOptionPane.showMessageDialog(this, "회원가입 성공! 로그인 해주세요.");
                mainWindow.showLogin();
            } else {
                JOptionPane.showMessageDialog(this, "이미 존재하는 아이디입니다.");
            }
        });

        goLoginBtn.addActionListener(e -> mainWindow.showLogin());
    }
}