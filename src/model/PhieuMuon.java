package model;

import java.sql.Date;

public class PhieuMuon {
    private String maPhieu;
    private String maDocGia;
    private Date ngayMuon;
    private Date hanTra;
    private String trangThai;

    public PhieuMuon() {}

    public PhieuMuon(String maPhieu, String maDocGia, Date ngayMuon, Date hanTra, String trangThai) {
        this.maPhieu = maPhieu;
        this.maDocGia = maDocGia;
        this.ngayMuon = ngayMuon;
        this.hanTra = hanTra;
        this.trangThai = trangThai;
    }

    public String getMaPhieu() { return maPhieu; }
    public void setMaPhieu(String maPhieu) { this.maPhieu = maPhieu; }
    public String getMaDocGia() { return maDocGia; }
    public void setMaDocGia(String maDocGia) { this.maDocGia = maDocGia; }
    public Date getNgayMuon() { return ngayMuon; }
    public void setNgayMuon(Date ngayMuon) { this.ngayMuon = ngayMuon; }
    public Date getHanTra() { return hanTra; }
    public void setHanTra(Date hanTra) { this.hanTra = hanTra; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
