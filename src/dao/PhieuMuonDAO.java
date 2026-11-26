package dao;

import database.JDBCUtil;
import model.PhieuMuon;
import java.sql.*;
import java.util.ArrayList;

public class PhieuMuonDAO implements DAOInterface<PhieuMuon> {
    public static PhieuMuonDAO getInstance() {
        return new PhieuMuonDAO();
    }

    @Override
    public int insert(PhieuMuon t) {
        int ketQua = 0;
        Connection con = JDBCUtil.getConnection();
        String sql = "INSERT INTO PHIEUMUON (MaPhieu, MaDocGia, NgayMuon, HanTra, TrangThai) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, t.getMaPhieu());
            pst.setString(2, t.getMaDocGia());
            pst.setDate(3, t.getNgayMuon());
            pst.setDate(4, t.getHanTra());
            pst.setString(5, t.getTrangThai());
            ketQua = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }
        return ketQua;
    }

    // Lập phiếu mượn kèm chi tiết sách và trừ tồn kho
    public boolean lapPhieuMuon(PhieuMuon pm, String maSach, int soLuongMuon) {
        Connection con = JDBCUtil.getConnection();
        try {
            con.setAutoCommit(false);
            // 1. Kiểm tra tồn kho
            String checkSql = "SELECT SoLuongCon FROM SACH WHERE MaSach = ?";
            try (PreparedStatement pstCheck = con.prepareStatement(checkSql)) {
                pstCheck.setString(1, maSach);
                ResultSet rs = pstCheck.executeQuery();
                if (!rs.next() || rs.getInt("SoLuongCon") < soLuongMuon) {
                    con.rollback();
                    return false;
                }
            }
            // 2. Thêm phiếu mượn
            String sqlPM = "INSERT INTO PHIEUMUON (MaPhieu, MaDocGia, NgayMuon, HanTra, TrangThai) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstPM = con.prepareStatement(sqlPM)) {
                pstPM.setString(1, pm.getMaPhieu());
                pstPM.setString(2, pm.getMaDocGia());
                pstPM.setDate(3, pm.getNgayMuon());
                pstPM.setDate(4, pm.getHanTra());
                pstPM.setString(5, N_STR("Đang mượn"));
                pstPM.executeUpdate();
            }
            // 3. Thêm chi tiết phiếu mượn
            String sqlCT = "INSERT INTO CT_PHIEUMUON (MaPhieu, MaSach, SoLuongMuon, GhiChu) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstCT = con.prepareStatement(sqlCT)) {
                pstCT.setString(1, pm.getMaPhieu());
                pstCT.setString(2, maSach);
                pstCT.setInt(3, soLuongMuon);
                pstCT.setString(4, "Bình thường");
                pstCT.executeUpdate();
            }
            // 4. Trừ số lượng sách còn
            String sqlUpdateSach = "UPDATE SACH SET SoLuongCon = SoLuongCon - ? WHERE MaSach = ?";
            try (PreparedStatement pstSach = con.prepareStatement(sqlUpdateSach)) {
                pstSach.setInt(1, soLuongMuon);
                pstSach.setString(2, maSach);
                pstSach.executeUpdate();
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            try { if (con != null) con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            JDBCUtil.closeConnection(con);
        }
    }

    private String N_STR(String s) { return s; }

    // Trả sách và hoàn trả tồn kho
    public boolean traSach(String maPhieu) {
        Connection con = JDBCUtil.getConnection();
        try {
            con.setAutoCommit(false);
            // Lấy chi tiết sách đã mượn
            String getCT = "SELECT MaSach, SoLuongMuon FROM CT_PHIEUMUON WHERE MaPhieu = ? AND NgayTra IS NULL";
            try (PreparedStatement pstGet = con.prepareStatement(getCT)) {
                pstGet.setString(1, maPhieu);
                ResultSet rs = pstGet.executeQuery();
                while (rs.next()) {
                    String maSach = rs.getString("MaSach");
                    int sl = rs.getInt("SoLuongMuon");
                    String updSach = "UPDATE SACH SET SoLuongCon = SoLuongCon + ? WHERE MaSach = ?";
                    try (PreparedStatement pstUpd = con.prepareStatement(updSach)) {
                        pstUpd.setInt(1, sl);
                        pstUpd.setString(2, maSach);
                        pstUpd.executeUpdate();
                    }
                }
            }
            // Cập nhật ngày trả thực tế
            String updCT = "UPDATE CT_PHIEUMUON SET NgayTra = GETDATE() WHERE MaPhieu = ?";
            try (PreparedStatement pstCT = con.prepareStatement(updCT)) {
                pstCT.setString(1, maPhieu);
                pstCT.executeUpdate();
            }
            // Cập nhật trạng thái phiếu
            String updPM = "UPDATE PHIEUMUON SET TrangThai = N'Đã trả' WHERE MaPhieu = ?";
            try (PreparedStatement pstPM = con.prepareStatement(updPM)) {
                pstPM.setString(1, maPhieu);
                pstPM.executeUpdate();
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            try { if (con != null) con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            JDBCUtil.closeConnection(con);
        }
    }

    @Override
    public int update(PhieuMuon t) {
        int ketQua = 0;
        Connection con = JDBCUtil.getConnection();
        String sql = "UPDATE PHIEUMUON SET MaDocGia=?, NgayMuon=?, HanTra=?, TrangThai=? WHERE MaPhieu=?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, t.getMaDocGia());
            pst.setDate(2, t.getNgayMuon());
            pst.setDate(3, t.getHanTra());
            pst.setString(4, t.getTrangThai());
            pst.setString(5, t.getMaPhieu());
            ketQua = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }
        return ketQua;
    }

    @Override
    public int delete(PhieuMuon t) {
        int ketQua = 0;
        Connection con = JDBCUtil.getConnection();
        try {
            String delCT = "DELETE FROM CT_PHIEUMUON WHERE MaPhieu=?";
            try (PreparedStatement pst1 = con.prepareStatement(delCT)) {
                pst1.setString(1, t.getMaPhieu());
                pst1.executeUpdate();
            }
            String delPM = "DELETE FROM PHIEUMUON WHERE MaPhieu=?";
            try (PreparedStatement pst2 = con.prepareStatement(delPM)) {
                pst2.setString(1, t.getMaPhieu());
                ketQua = pst2.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }
        return ketQua;
    }

    @Override
    public ArrayList<PhieuMuon> selectAll() {
        ArrayList<PhieuMuon> list = new ArrayList<>();
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT * FROM PHIEUMUON";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new PhieuMuon(
                    rs.getString("MaPhieu"), rs.getString("MaDocGia"),
                    rs.getDate("NgayMuon"), rs.getDate("HanTra"), rs.getString("TrangThai")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }
        return list;
    }

    @Override
    public PhieuMuon selectById(PhieuMuon t) {
        PhieuMuon pm = null;
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT * FROM PHIEUMUON WHERE MaPhieu=?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, t.getMaPhieu());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                pm = new PhieuMuon(
                    rs.getString("MaPhieu"), rs.getString("MaDocGia"),
                    rs.getDate("NgayMuon"), rs.getDate("HanTra"), rs.getString("TrangThai")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }
        return pm;
    }

    @Override
    public ArrayList<PhieuMuon> selectByCondition(String condition) {
        ArrayList<PhieuMuon> list = new ArrayList<>();
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT * FROM PHIEUMUON WHERE " + condition;
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new PhieuMuon(
                    rs.getString("MaPhieu"), rs.getString("MaDocGia"),
                    rs.getDate("NgayMuon"), rs.getDate("HanTra"), rs.getString("TrangThai")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }
        return list;
    }

    public ArrayList<Object[]> getChiTietPhieuMuonView() {
        ArrayList<Object[]> list = new ArrayList<>();
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT pm.MaPhieu, dg.HoTen, s.TenSach, ct.SoLuongMuon, pm.NgayMuon, pm.HanTra, ct.NgayTra, pm.TrangThai " +
                     "FROM PHIEUMUON pm " +
                     "JOIN DOCGIA dg ON pm.MaDocGia = dg.MaDocGia " +
                     "LEFT JOIN CT_PHIEUMUON ct ON pm.MaPhieu = ct.MaPhieu " +
                     "LEFT JOIN SACH s ON ct.MaSach = s.MaSach";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("MaPhieu"), rs.getString("HoTen"), rs.getString("TenSach"),
                    rs.getInt("SoLuongMuon"), rs.getDate("NgayMuon"), rs.getDate("HanTra"),
                    rs.getDate("NgayTra"), rs.getString("TrangThai")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }
        return list;
    }
}
