package dao;

import database.JDBCUtil;
import model.Sach;
import java.sql.*;
import java.util.ArrayList;

public class SachDAO implements DAOInterface<Sach> {
    public static SachDAO getInstance() {
        return new SachDAO();
    }

    @Override
    public int insert(Sach t) {
        int ketQua = 0;
        Connection con = JDBCUtil.getConnection();
        String sql = "INSERT INTO SACH (MaSach, TenSach, TacGia, NhaXuatBan, NamXuatBan, MaTheLoai, SoLuong, SoLuongCon) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, t.getMaSach());
            pst.setString(2, t.getTenSach());
            pst.setString(3, t.getTacGia());
            pst.setString(4, t.getNhaXuatBan());
            pst.setInt(5, t.getNamXuatBan());
            pst.setString(6, t.getMaTheLoai());
            pst.setInt(7, t.getSoLuong());
            pst.setInt(8, t.getSoLuongCon());
            ketQua = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }
        return ketQua;
    }

    @Override
    public int update(Sach t) {
        int ketQua = 0;
        Connection con = JDBCUtil.getConnection();
        String sql = "UPDATE SACH SET TenSach=?, TacGia=?, NhaXuatBan=?, NamXuatBan=?, MaTheLoai=?, SoLuong=?, SoLuongCon=? WHERE MaSach=?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, t.getTenSach());
            pst.setString(2, t.getTacGia());
            pst.setString(3, t.getNhaXuatBan());
            pst.setInt(4, t.getNamXuatBan());
            pst.setString(5, t.getMaTheLoai());
            pst.setInt(6, t.getSoLuong());
            pst.setInt(7, t.getSoLuongCon());
            pst.setString(8, t.getMaSach());
            ketQua = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }
        return ketQua;
    }

    @Override
    public int delete(Sach t) {
        int ketQua = 0;
        Connection con = JDBCUtil.getConnection();
        String sql = "DELETE FROM SACH WHERE MaSach=?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, t.getMaSach());
            ketQua = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }
        return ketQua;
    }

    @Override
    public ArrayList<Sach> selectAll() {
        ArrayList<Sach> list = new ArrayList<>();
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT * FROM SACH";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Sach(
                    rs.getString("MaSach"), rs.getString("TenSach"), rs.getString("TacGia"),
                    rs.getString("NhaXuatBan"), rs.getInt("NamXuatBan"), rs.getString("MaTheLoai"),
                    rs.getInt("SoLuong"), rs.getInt("SoLuongCon")
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
    public Sach selectById(Sach t) {
        Sach s = null;
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT * FROM SACH WHERE MaSach=?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, t.getMaSach());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                s = new Sach(
                    rs.getString("MaSach"), rs.getString("TenSach"), rs.getString("TacGia"),
                    rs.getString("NhaXuatBan"), rs.getInt("NamXuatBan"), rs.getString("MaTheLoai"),
                    rs.getInt("SoLuong"), rs.getInt("SoLuongCon")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }
        return s;
    }

    @Override
    public ArrayList<Sach> selectByCondition(String condition) {
        ArrayList<Sach> list = new ArrayList<>();
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT * FROM SACH WHERE " + condition;
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Sach(
                    rs.getString("MaSach"), rs.getString("TenSach"), rs.getString("TacGia"),
                    rs.getString("NhaXuatBan"), rs.getInt("NamXuatBan"), rs.getString("MaTheLoai"),
                    rs.getInt("SoLuong"), rs.getInt("SoLuongCon")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }
        return list;
    }

    public ArrayList<Sach> search(String keyword) {
        ArrayList<Sach> list = new ArrayList<>();
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT * FROM SACH WHERE MaSach LIKE ? OR TenSach LIKE ? OR TacGia LIKE ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            String key = "%" + keyword + "%";
            pst.setString(1, key);
            pst.setString(2, key);
            pst.setString(3, key);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new Sach(
                    rs.getString("MaSach"), rs.getString("TenSach"), rs.getString("TacGia"),
                    rs.getString("NhaXuatBan"), rs.getInt("NamXuatBan"), rs.getString("MaTheLoai"),
                    rs.getInt("SoLuong"), rs.getInt("SoLuongCon")
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
