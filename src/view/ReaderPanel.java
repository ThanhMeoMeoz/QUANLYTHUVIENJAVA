package view;

import dao.DocGiaDAO;
import model.DocGia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.ArrayList;

public class ReaderPanel extends JPanel {
    private JTextField txtMaDG, txtHoTen, txtNgaySinh, txtSDT, txtEmail, txtDiaChi, txtTimKiem;
    private JComboBox<String> cboGioiTinh;
    private JTable table;
    private DefaultTableModel tableModel;

    public ReaderPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(4, 4, 10, 8));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin thẻ độc giả"));

        inputPanel.add(new JLabel("Mã độc giả:"));
        txtMaDG = new JTextField();
        inputPanel.add(txtMaDG);

        inputPanel.add(new JLabel("Họ và tên:"));
        txtHoTen = new JTextField();
        inputPanel.add(txtHoTen);

        inputPanel.add(new JLabel("Ngày sinh (yyyy-MM-dd):"));
        txtNgaySinh = new JTextField("2005-01-01");
        inputPanel.add(txtNgaySinh);

        inputPanel.add(new JLabel("Giới tính:"));
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        inputPanel.add(cboGioiTinh);

        inputPanel.add(new JLabel("Số điện thoại:"));
        txtSDT = new JTextField();
        inputPanel.add(txtSDT);

        inputPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        inputPanel.add(txtEmail);

        inputPanel.add(new JLabel("Địa chỉ:"));
        txtDiaChi = new JTextField();
        inputPanel.add(txtDiaChi);

        inputPanel.add(new JLabel(""));
        inputPanel.add(new JLabel(""));

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton btnThem = new JButton("Thêm độc giả");
        JButton btnSua = new JButton("Cập nhật");
        JButton btnXoa = new JButton("Xóa độc giả");
        JButton btnLamMoi = new JButton("Làm mới");

        actionPanel.add(btnThem);
        actionPanel.add(btnSua);
        actionPanel.add(btnXoa);
        actionPanel.add(btnLamMoi);

        actionPanel.add(new JLabel("   Tìm kiếm (Mã/Tên/SĐT):"));
        txtTimKiem = new JTextField(18);
        JButton btnTim = new JButton("Tìm kiếm");
        actionPanel.add(txtTimKiem);
        actionPanel.add(btnTim);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(actionPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        String[] cols = {"Mã ĐG", "Họ Tên", "Ngày Sinh", "Giới Tính", "SĐT", "Email", "Địa Chỉ", "Ngày Đăng Ký"};
        tableModel = new DefaultTableModel(cols, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData(DocGiaDAO.getInstance().selectAll());

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtMaDG.setText(tableModel.getValueAt(row, 0).toString());
                    txtHoTen.setText(tableModel.getValueAt(row, 1).toString());
                    txtNgaySinh.setText(tableModel.getValueAt(row, 2) != null ? tableModel.getValueAt(row, 2).toString() : "");
                    cboGioiTinh.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                    txtSDT.setText(tableModel.getValueAt(row, 4) != null ? tableModel.getValueAt(row, 4).toString() : "");
                    txtEmail.setText(tableModel.getValueAt(row, 5) != null ? tableModel.getValueAt(row, 5).toString() : "");
                    txtDiaChi.setText(tableModel.getValueAt(row, 6) != null ? tableModel.getValueAt(row, 6).toString() : "");
                }
            }
        });

        btnThem.addActionListener(e -> {
            try {
                DocGia dg = getDocGiaFromForm();
                if (DocGiaDAO.getInstance().insert(dg) > 0) {
                    JOptionPane.showMessageDialog(this, "Thêm độc giả thành công!");
                    loadData(DocGiaDAO.getInstance().selectAll());
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm độc giả thất bại!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Định dạng ngày sinh phải là yyyy-MM-dd!");
            }
        });

        btnSua.addActionListener(e -> {
            try {
                DocGia dg = getDocGiaFromForm();
                if (DocGiaDAO.getInstance().update(dg) > 0) {
                    JOptionPane.showMessageDialog(this, "Cập nhật độc giả thành công!");
                    loadData(DocGiaDAO.getInstance().selectAll());
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi dữ liệu đầu vào!");
            }
        });

        btnXoa.addActionListener(e -> {
            String ma = txtMaDG.getText().trim();
            if (ma.isEmpty()) return;
            DocGia dg = new DocGia();
            dg.setMaDocGia(ma);
            if (DocGiaDAO.getInstance().delete(dg) > 0) {
                JOptionPane.showMessageDialog(this, "Đã xóa độc giả!");
                loadData(DocGiaDAO.getInstance().selectAll());
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa độc giả đang có phiếu mượn!");
            }
        });

        btnLamMoi.addActionListener(e -> {
            clearForm();
            loadData(DocGiaDAO.getInstance().selectAll());
        });

        btnTim.addActionListener(e -> {
            loadData(DocGiaDAO.getInstance().search(txtTimKiem.getText().trim()));
        });
    }

    private DocGia getDocGiaFromForm() {
        Date ns = txtNgaySinh.getText().trim().isEmpty() ? null : Date.valueOf(txtNgaySinh.getText().trim());
        return new DocGia(
            txtMaDG.getText().trim(), txtHoTen.getText().trim(), ns,
            cboGioiTinh.getSelectedItem().toString(), txtSDT.getText().trim(),
            txtEmail.getText().trim(), txtDiaChi.getText().trim(),
            new Date(System.currentTimeMillis())
        );
    }

    private void clearForm() {
        txtMaDG.setText("");
        txtHoTen.setText("");
        txtNgaySinh.setText("2005-01-01");
        txtSDT.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        txtTimKiem.setText("");
    }

    private void loadData(ArrayList<DocGia> list) {
        tableModel.setRowCount(0);
        for (DocGia dg : list) {
            tableModel.addRow(new Object[]{
                dg.getMaDocGia(), dg.getHoTen(), dg.getNgaySinh(), dg.getGioiTinh(),
                dg.getSoDienThoai(), dg.getEmail(), dg.getDiaChi(), dg.getNgayDangKy()
            });
        }
    }
}
