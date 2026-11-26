package view;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;

    public LoginForm() {
        setTitle("Đăng Nhập Hệ Thống Quản Lý Thư Viện");
        setSize(420, 260);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("QUẢN LÝ THƯ VIỆN SÁCH", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(25, 75, 135));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 15));
        formPanel.add(new JLabel("Tài khoản thủ thư:"));
        txtUser = new JTextField("admin");
        formPanel.add(txtUser);

        formPanel.add(new JLabel("Mật khẩu:"));
        txtPass = new JPasswordField("123456");
        formPanel.add(txtPass);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setBackground(new Color(25, 75, 135));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);

        JButton btnExit = new JButton("Thoát");

        btnLogin.addActionListener(e -> xuLyDangNhap());
        btnExit.addActionListener(e -> System.exit(0));

        btnPanel.add(btnLogin);
        btnPanel.add(btnExit);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void xuLyDangNhap() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());
        if (user.equals("admin") && pass.equals("123456")) {
            dispose();
            new MainFrame().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu! (Mặc định: admin / 123456)", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
