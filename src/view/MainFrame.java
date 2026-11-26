package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("HỆ THỐNG QUẢN LÝ THƯ VIỆN SÁCH - JAVA SWING & SQL SERVER");
        setSize(1050, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 75, 135));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        JLabel lblHeader = new JLabel("PHẦN MỀM QUẢN LÝ THƯ VIỆN SÁCH (JDBC - DAO - MVC)");
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerPanel.add(lblHeader, BorderLayout.WEST);

        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginForm().setVisible(true);
        });
        headerPanel.add(btnLogout, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.addTab("Quản Lý Sách", new BookPanel());
        tabbedPane.addTab("Quản Lý Độc Giả", new ReaderPanel());
        tabbedPane.addTab("Quản Lý Mượn - Trả Sách", new BorrowReturnPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }
}
