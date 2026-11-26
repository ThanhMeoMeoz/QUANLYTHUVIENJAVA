package view;

import dao.SachDAO;
import dao.TheLoaiDAO;
import model.Sach;
import model.TheLoai;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class BookPanel extends JPanel {
    private JTextField txtMaSach, txtTenSach, txtTacGia, txtNXB, txtNamXB, txtSoLuong, txtSoLuongCon, txtTimKiem;
    private JComboBox<String> cboTheLoai;
    private JTable table;
    private DefaultTableModel tableModel;

    public BookPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form nhập liệu
        JPanel inputPanel = new JPanel(new GridLayout(4, 4, 10, 8));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin chi tiết sách"));

        inputPanel.add(new JLabel("Mã sách:"));
        txtMaSach = new JTextField();
        inputPanel.add(txtMaSach);

        inputPanel.add(new JLabel("Tên sách:"));
        txtTenSach = new JTextField();
        inputPanel.add(txtTenSach);

        inputPanel.add(new JLabel("Tác giả:"));
        txtTacGia = new JTextField();
        inputPanel.add(txtTacGia);

        inputPanel.add(new JLabel("Nhà xuất bản:"));
        txtNXB = new JTextField();
        inputPanel.add(txtNXB);

        inputPanel.add(new JLabel("Năm xuất bản:"));
        txtNamXB = new JTextField();
        inputPanel.add(txtNamXB);

        inputPanel.add(new JLabel("Mã thể loại:"));
        cboTheLoai = new JComboBox<>();
        loadTheLoai();
        inputPanel.add(cboTheLoai);

        inputPanel.add(new JLabel("Tổng số lượng:"));
        txtSoLuong = new JTextField();
        inputPanel.add(txtSoLuong);

        inputPanel.add(new JLabel("Số lượng còn:"));
        txtSoLuongCon = new JTextField();
        inputPanel.add(txtSoLuongCon);

        // Thanh chức năng (Thêm, Sửa, Xóa, Làm mới, Tìm kiếm)
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton btnThem = new JButton("Thêm sách");
        JButton btnSua = new JButton("Cập nhật");
        JButton btnXoa = new JButton("Xóa sách");
        JButton btnLamMoi = new JButton("Làm mới");

        actionPanel.add(btnThem);
        actionPanel.add(btnSua);
        actionPanel.add(btnXoa);
        actionPanel.add(btnLamMoi);

        actionPanel.add(new JLabel("   Tìm kiếm (Mã/Tên/Tác giả):"));
        txtTimKiem = new JTextField(18);
        JButton btnTim = new JButton("Tìm kiếm");
        actionPanel.add(txtTimKiem);
        actionPanel.add(btnTim);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(actionPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] cols = {"Mã Sách", "Tên Sách", "Tác Giả", "Nhà Xuất Bản", "Năm XB", "Mã Thể Loại", "Tổng SL", "SL Còn"};
        tableModel = new DefaultTableModel(cols, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData(SachDAO.getInstance().selectAll());

        // Sự kiện click dòng bảng
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtMaSach.setText(tableModel.getValueAt(row, 0).toString());
                    txtTenSach.setText(tableModel.getValueAt(row, 1).toString());
                    txtTacGia.setText(tableModel.getValueAt(row, 2).toString());
                    txtNXB.setText(tableModel.getValueAt(row, 3) != null ? tableModel.getValueAt(row, 3).toString() : "");
                    txtNamXB.setText(tableModel.getValueAt(row, 4).toString());
                    cboTheLoai.setSelectedItem(tableModel.getValueAt(row, 5).toString());
                    txtSoLuong.setText(tableModel.getValueAt(row, 6).toString());
                    txtSoLuongCon.setText(tableModel.getValueAt(row, 7).toString());
                }
            }
        });

        btnThem.addActionListener(e -> {
            try {
                Sach s = getSachFromForm();
                if (SachDAO.getInstance().insert(s) > 0) {
                    JOptionPane.showMessageDialog(this, "Thêm sách thành công!");
                    loadData(SachDAO.getInstance().selectAll());
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại (Trùng mã hoặc sai ràng buộc)!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng kiểm tra định dạng số liệu!");
            }
        });

        btnSua.addActionListener(e -> {
            try {
                Sach s = getSachFromForm();
                if (SachDAO.getInstance().update(s) > 0) {
                    JOptionPane.showMessageDialog(this, "Cập nhật sách thành công!");
                    loadData(SachDAO.getInstance().selectAll());
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng kiểm tra định dạng số liệu!");
            }
        });

        btnXoa.addActionListener(e -> {
            String ma = txtMaSach.getText().trim();
            if (ma.isEmpty()) return;
            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa sách " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Sach s = new Sach();
                s.setMaSach(ma);
                if (SachDAO.getInstance().delete(s) > 0) {
                    JOptionPane.showMessageDialog(this, "Đã xóa sách!");
                    loadData(SachDAO.getInstance().selectAll());
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể xóa sách (đang có phiếu mượn liên kết)!");
                }
            }
        });

        btnLamMoi.addActionListener(e -> {
            clearForm();
            loadTheLoai();
            loadData(SachDAO.getInstance().selectAll());
        });

        btnTim.addActionListener(e -> {
            String kw = txtTimKiem.getText().trim();
            loadData(SachDAO.getInstance().search(kw));
        });
    }

    private void loadTheLoai() {
        cboTheLoai.removeAllItems();
        ArrayList<TheLoai> list = TheLoaiDAO.getInstance().selectAll();
        for (TheLoai tl : list) {
            cboTheLoai.addItem(tl.getMaTheLoai());
        }
        if (cboTheLoai.getItemCount() == 0) {
            cboTheLoai.addItem("TL01");
            cboTheLoai.addItem("TL02");
            cboTheLoai.addItem("TL03");
        }
    }

    private Sach getSachFromForm() {
        return new Sach(
            txtMaSach.getText().trim(),
            txtTenSach.getText().trim(),
            txtTacGia.getText().trim(),
            txtNXB.getText().trim(),
            Integer.parseInt(txtNamXB.getText().trim()),
            cboTheLoai.getSelectedItem().toString(),
            Integer.parseInt(txtSoLuong.getText().trim()),
            Integer.parseInt(txtSoLuongCon.getText().trim())
        );
    }

    private void clearForm() {
        txtMaSach.setText("");
        txtTenSach.setText("");
        txtTacGia.setText("");
        txtNXB.setText("");
        txtNamXB.setText("");
        txtSoLuong.setText("");
        txtSoLuongCon.setText("");
        txtTimKiem.setText("");
    }

    private void loadData(ArrayList<Sach> list) {
        tableModel.setRowCount(0);
        for (Sach s : list) {
            tableModel.addRow(new Object[]{
                s.getMaSach(), s.getTenSach(), s.getTacGia(), s.getNhaXuatBan(),
                s.getNamXuatBan(), s.getMaTheLoai(), s.getSoLuong(), s.getSoLuongCon()
            });
        }
    }
}
