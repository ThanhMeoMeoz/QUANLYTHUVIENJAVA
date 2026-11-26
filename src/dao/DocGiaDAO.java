package dao;

import database.JDBCUtil;
import model.DocGia;
import java.sql.*;
import java.util.ArrayList;

public class DocGiaDAO implements DAOInterface<DocGia> {
    public static DocGiaDAO getInstance() {
        return new DocGiaDAO();
    }

    @Override
    public int insert(DocGia t) {
        int ketQua = 0;
        Connection con = JDBCUtil.getConnection();
        String sql = "INSERT INTO DOCGIA (MaDocGia, HoTen, NgaySinh, GioiTinh, SoDienThoai, Email, DiaChi, NgayDangKy) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, t.getMaDocGia());
            pst.setString(2, t.getHoTen());
            pst.setDate(3, t.getNgaySinh());
            pst.setString(4, t.getGioiTinh());
            pst.setString(5, t.getSoDienThoai());
            pst.setString(6, t.getEmail());
            pst.setString(7, t.getDiaChi());
            pst.setDate(8, t.getNgayDangKy() != null ? t.getNgayDangKy() : new Date(System.currentTimeMillis()));
            ketQua = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }
        return ketQua;
    }

    @Override
    public int update(DocGia t) {
        int ketQua = 0;
        Connection con = JDBCUtil.getConnection();
        String sql = "UPDATE DOCGIA SET HoTen=?, NgaySinh=?, GioiTinh=?, SoDienThoai=?, Email=?, DiaChi=? WHERE MaDocGia=?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, t.getHoTen());
            pst.setDate(2, t.getNgaySinh());
            pst.setString(3, t.getGioiTinh());
            pst.setString(4, t.getSoDienThoai());
            pst.setString(5, t.getEmail());
            pst.setString(6, t.getDiaChi());
            pst.setString(7, t.getMaDocGia());
            ketQua = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }
        return ketQua;
    }

    @Override
    public int delete(DocGia t) {
        int ketQua = 0;
        Connection con = JDBCUtil.getConnection();
        String sql = "DELETE FROM DOCGIA WHERE MaDocGia=?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, t.getMaDocGia());
            ketQua = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }
        return ketQua;
    }

    @Override
    public ArrayList<DocGia> selectAll() {
        ArrayList<DocGia> list = new ArrayList<>();
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT * FROM DOCGIA";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new DocGia(
                    rs.getString("MaDocGia"), rs.getString("HoTen"), rs.getDate("NgaySinh"),
                    rs.getString("GioiTinh"), rs.getString("SoDienThoai"), rs.getString("Email"),
                    rs.getString("DiaChi"), rs.getDate("NgayDangKy")
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
    public DocGia selectById(DocGia t) {
        DocGia dg = null;
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT * FROM DOCGIA WHERE MaDocGia=?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, t.getMaDocGia());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                dg = new DocGia(
                    rs.getString("MaDocGia"), rs.getString("HoTen"), rs.getDate("NgaySinh"),
                    rs.getString("GioiTinh"), rs.getString("SoDienThoai"), rs.getString("Email"),
                    rs.getString("DiaChi"), rs.getDate("NgayDangKy")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }
        return dg;
    }

    @Override
    public ArrayList<DocGia> selectByCondition(String condition) {
        ArrayList<DocGia> list = new ArrayList<>();
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT * FROM DOCGIA WHERE " + condition;
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new DocGia(
                    rs.getString("MaDocGia"), rs.getString("HoTen"), rs.getDate("NgaySinh"),
                    rs.getString("GioiTinh"), rs.getString("SoDienThoai"), rs.getString("Email"),
                    rs.getString("DiaChi"), rs.getDate("NgayDangKy")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }
        return list;
    }

    public ArrayList<DocGia> search(String keyword) {
        ArrayList<DocGia> list = new ArrayList<>();
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT * FROM DOCGIA WHERE MaDocGia LIKE ? OR HoTen LIKE ? OR SoDienThoai LIKE ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            String key = "%" + keyword + "%";
            pst.setString(1, key);
            pst.setString(2, key);
            pst.setString(3, key);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new DocGia(
                    rs.getString("MaDocGia"), rs.getString("HoTen"), rs.getDate("NgaySinh"),
                    rs.getString("GioiTinh"), rs.getString("SoDienThoai"), rs.getString("Email"),
                    rs.getString("DiaChi"), rs.getDate("NgayDangKy")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }
        return list;
    }
}
