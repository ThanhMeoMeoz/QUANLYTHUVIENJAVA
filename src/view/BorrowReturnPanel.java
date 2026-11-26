package view;

import dao.DocGiaDAO;
import dao.PhieuMuonDAO;
import dao.SachDAO;
import model.DocGia;
import model.PhieuMuon;
import model.Sach;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;

public class BorrowReturnPanel extends JPanel {
    private JTextField txtMaPhieu, txtSoLuongMuon, txtNgayMuon, txtHanTra;
    private JComboBox<String> cboDocGia, cboSach;
    private JTable table;
    private DefaultTableModel tableModel;

    public BorrowReturnPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(3, 4, 10, 8));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Lập phiếu mượn & Trả sách"));

        inputPanel.add(new JLabel("Mã phiếu mượn:"));
        txtMaPhieu = new JTextField("PM01");
        inputPanel.add(txtMaPhieu);

        inputPanel.add(new JLabel("Mã độc giả:"));
        cboDocGia = new JComboBox<>();
        inputPanel.add(cboDocGia);

        inputPanel.add(new JLabel("Mã sách mượn:"));
        cboSach = new JComboBox<>();
        inputPanel.add(cboSach);

        inputPanel.add(new JLabel("Số lượng mượn:"));
        txtSoLuongMuon = new JTextField("1");
        inputPanel.add(txtSoLuongMuon);

        inputPanel.add(new JLabel("Ngày mượn (yyyy-MM-dd):"));
        txtNgayMuon = new JTextField(LocalDate.now().toString());
        inputPanel.add(txtNgayMuon);

        inputPanel.add(new JLabel("Hạn trả (yyyy-MM-dd):"));
        txtHanTra = new JTextField(LocalDate.now().plusDays(14).toString());
        inputPanel.add(txtHanTra);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton btnLapPhieu = new JButton("Lập phiếu mượn");
        JButton btnTraSach = new JButton("Xác nhận trả sách");
        JButton btnLamMoi = new JButton("Làm mới danh sách");

        actionPanel.add(btnLapPhieu);
        actionPanel.add(btnTraSach);
        actionPanel.add(btnLamMoi);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(actionPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        String[] cols = {"Mã Phiếu", "Độc Giả", "Tên Sách", "SL Mượn", "Ngày Mượn", "Hạn Trả", "Ngày Trả Thực Tế", "Trạng Thái"};
        tableModel = new DefaultTableModel(cols, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadCombos();
        loadData();

        btnLapPhieu.addActionListener(e -> {
            try {
                String maPhieu = txtMaPhieu.getText().trim();
                String maDG = cboDocGia.getSelectedItem().toString();
                String maSach = cboSach.getSelectedItem().toString();
                int sl = Integer.parseInt(txtSoLuongMuon.getText().trim());
                Date ngayMuon = Date.valueOf(txtNgayMuon.getText().trim());
                Date hanTra = Date.valueOf(txtHanTra.getText().trim());

                if (hanTra.before(ngayMuon)) {
                    JOptionPane.showMessageDialog(this, "Hạn trả không được trước ngày mượn!");
                    return;
                }

                PhieuMuon pm = new PhieuMuon(maPhieu, maDG, ngayMuon, hanTra, "Đang mượn");
                if (PhieuMuonDAO.getInstance().lapPhieuMuon(pm, maSach, sl)) {
                    JOptionPane.showMessageDialog(this, "Lập phiếu mượn thành công & Đã trừ tồn kho!");
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Lập phiếu thất bại (Trùng mã phiếu hoặc không đủ sách tồn kho)!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng kiểm tra lại định dạng ngày tháng và số lượng!");
            }
        });

        btnTraSach.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu mượn trên bảng để trả sách!");
                return;
            }
            String maPhieu = tableModel.getValueAt(row, 0).toString();
            String trangThai = tableModel.getValueAt(row, 7).toString();
            if ("Đã trả".equalsIgnoreCase(trangThai)) {
                JOptionPane.showMessageDialog(this, "Phiếu mượn này đã được trả trước đó!");
                return;
            }
            if (PhieuMuonDAO.getInstance().traSach(maPhieu)) {
                JOptionPane.showMessageDialog(this, "Trả sách thành công & Đã hoàn trả số lượng tồn kho!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật trả sách!");
            }
        });

        btnLamMoi.addActionListener(e -> {
            loadCombos();
            loadData();
        });
    }

    private void loadCombos() {
        cboDocGia.removeAllItems();
        for (DocGia dg : DocGiaDAO.getInstance().selectAll()) {
            cboDocGia.addItem(dg.getMaDocGia());
        }
        cboSach.removeAllItems();
        for (Sach s : SachDAO.getInstance().selectAll()) {
            cboSach.addItem(s.getMaSach());
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        for (Object[] row : PhieuMuonDAO.getInstance().getChiTietPhieuMuonView()) {
            tableModel.addRow(row);
        }
    }
}
