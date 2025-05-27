package coinmockproject.util;

import javax.swing.*;
import java.awt.*;

public class DialogUtil {

    public static void showSuccess(Component parent, String message) {
        ImageIcon icon = new ImageIcon(DialogUtil.class.getResource("/img/success.png"));
        showStyledMessage(parent, message, "성공", JOptionPane.INFORMATION_MESSAGE, icon);
    }

    public static void showError(Component parent, String message) {
        ImageIcon icon = new ImageIcon(DialogUtil.class.getResource("/img/error.png"));
        showStyledMessage(parent, message, "오류", JOptionPane.ERROR_MESSAGE, icon);
    }

    public static void showWarning(Component parent, String message) {
        ImageIcon icon = new ImageIcon(DialogUtil.class.getResource("/img/warning.png"));
        showStyledMessage(parent, message, "경고", JOptionPane.WARNING_MESSAGE, icon);
    }

    public static void showInfo(Component parent, String message) {
        ImageIcon icon = new ImageIcon(DialogUtil.class.getResource("/img/info.png"));
        showStyledMessage(parent, message, "안내", JOptionPane.PLAIN_MESSAGE, icon);
    }

    private static void showStyledMessage(Component parent, String message, String title, int type, Icon icon) {
        JLabel label = new JLabel(message);
        label.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(45, 45, 45));
        panel.add(label, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(parent, panel, title, type, icon);
    }
} 
