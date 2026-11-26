package dao;

import database.JDBCUtil;
import model.TheLoai;
import java.sql.*;
import java.util.ArrayList;

public class TheLoaiDAO implements DAOInterface<TheLoai> {
    public static TheLoaiDAO getInstance() {
        return new TheLoaiDAO();
    }

    @Override
    public int insert(TheLoai t) {
        int ketQua = 0;
        Connection con = JDBCUtil.getConnection();
        String sql = "INSERT INTO THELOAI (MaTheLoai, TenTheLoai, MoTa) VALUES (?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, t.getMaTheLoai());
            pst.setString(2, t.getTenTheLoai());
            pst.setString(3, t.getMoTa());
            ketQua = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }
        return ketQua;
    }

    @Override
    public int update(TheLoai t) {
        int ketQua = 0;
        Connection con = JDBCUtil.getConnection();
        String sql = "UPDATE THELOAI SET TenTheLoai = ?, MoTa = ? WHERE MaTheLoai = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, t.getTenTheLoai());
            pst.setString(2, t.getMoTa());
            pst.setString(3, t.getMaTheLoai());
            ketQua = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }
        return ketQua;
    }

    @Override
    public int delete(TheLoai t) {
        int ketQua = 0;
        Connection con = JDBCUtil.getConnection();
        String sql = "DELETE FROM THELOAI WHERE MaTheLoai = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, t.getMaTheLoai());
            ketQua = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }
        return ketQua;
    }

    @Override
    public ArrayList<TheLoai> selectAll() {
        ArrayList<TheLoai> list = new ArrayList<>();
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT * FROM THELOAI";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new TheLoai(rs.getString("MaTheLoai"), rs.getString("TenTheLoai"), rs.getString("MoTa")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }
        return list;
    }

    @Override
    public TheLoai selectById(TheLoai t) {
        TheLoai tl = null;
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT * FROM THELOAI WHERE MaTheLoai = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, t.getMaTheLoai());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                tl = new TheLoai(rs.getString("MaTheLoai"), rs.getString("TenTheLoai"), rs.getString("MoTa"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }
        return tl;
    }

    @Override
    public ArrayList<TheLoai> selectByCondition(String condition) {
        ArrayList<TheLoai> list = new ArrayList<>();
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT * FROM THELOAI WHERE " + condition;
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new TheLoai(rs.getString("MaTheLoai"), rs.getString("TenTheLoai"), rs.getString("MoTa")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }
        return list;
    }
}
