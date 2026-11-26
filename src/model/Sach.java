package model;

public class Sach {
    private String maSach;
    private String tenSach;
    private String tacGia;
    private String nhaXuatBan;
    private int namXuatBan;
    private String maTheLoai;
    private int soLuong;
    private int soLuongCon;

    public Sach() {}

    public Sach(String maSach, String tenSach, String tacGia, String nhaXuatBan,
                int namXuatBan, String maTheLoai, int soLuong, int soLuongCon) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.tacGia = tacGia;
        this.nhaXuatBan = nhaXuatBan;
        this.namXuatBan = namXuatBan;
        this.maTheLoai = maTheLoai;
        this.soLuong = soLuong;
        this.soLuongCon = soLuongCon;
    }

    public String getMaSach() { return maSach; }
    public void setMaSach(String maSach) { this.maSach = maSach; }
    public String getTenSach() { return tenSach; }
    public void setTenSach(String tenSach) { this.tenSach = tenSach; }
    public String getTacGia() { return tacGia; }
    public void setTacGia(String tacGia) { this.tacGia = tacGia; }
    public String getNhaXuatBan() { return nhaXuatBan; }
    public void setNhaXuatBan(String nhaXuatBan) { this.nhaXuatBan = nhaXuatBan; }
    public int getNamXuatBan() { return namXuatBan; }
    public void setNamXuatBan(int namXuatBan) { this.namXuatBan = namXuatBan; }
    public String getMaTheLoai() { return maTheLoai; }
    public void setMaTheLoai(String maTheLoai) { this.maTheLoai = maTheLoai; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    public int getSoLuongCon() { return soLuongCon; }
    public void setSoLuongCon(int soLuongCon) { this.soLuongCon = soLuongCon; }
}
