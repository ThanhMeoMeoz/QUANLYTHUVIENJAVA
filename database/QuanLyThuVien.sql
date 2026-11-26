CREATE DATABASE QuanLyThuVien;
GO
USE QuanLyThuVien;
GO

CREATE TABLE THELOAI (
    MaTheLoai VARCHAR(10) PRIMARY KEY,
    TenTheLoai NVARCHAR(100) NOT NULL UNIQUE,
    MoTa NVARCHAR(255)
);

CREATE TABLE SACH (
    MaSach VARCHAR(10) PRIMARY KEY,
    TenSach NVARCHAR(200) NOT NULL,
    TacGia NVARCHAR(150) NOT NULL,
    NhaXuatBan NVARCHAR(150),
    NamXuatBan INT CHECK (NamXuatBan BETWEEN 1450 AND 2100),
    MaTheLoai VARCHAR(10) NOT NULL,
    SoLuong INT NOT NULL CHECK (SoLuong >= 0),
    SoLuongCon INT NOT NULL CHECK (SoLuongCon >= 0 AND SoLuongCon <= SoLuong),
    CONSTRAINT FK_SACH_THELOAI FOREIGN KEY (MaTheLoai) REFERENCES THELOAI(MaTheLoai)
);

CREATE TABLE DOCGIA (
    MaDocGia VARCHAR(10) PRIMARY KEY,
    HoTen NVARCHAR(150) NOT NULL,
    NgaySinh DATE,
    GioiTinh NVARCHAR(10),
    SoDienThoai VARCHAR(15),
    Email VARCHAR(120),
    DiaChi NVARCHAR(255),
    NgayDangKy DATE NOT NULL DEFAULT GETDATE()
);

CREATE TABLE PHIEUMUON (
    MaPhieu VARCHAR(12) PRIMARY KEY,
    MaDocGia VARCHAR(10) NOT NULL,
    NgayMuon DATE NOT NULL DEFAULT GETDATE(),
    HanTra DATE NOT NULL,
    TrangThai NVARCHAR(30) NOT NULL DEFAULT N'Đang mượn',
    CONSTRAINT FK_PM_DOCGIA FOREIGN KEY (MaDocGia) REFERENCES DOCGIA(MaDocGia),
    CONSTRAINT CK_PM_NGAY CHECK (HanTra >= NgayMuon)
);

CREATE TABLE CT_PHIEUMUON (
    MaPhieu VARCHAR(12) NOT NULL,
    MaSach VARCHAR(10) NOT NULL,
    SoLuongMuon INT NOT NULL CHECK (SoLuongMuon > 0),
    NgayTra DATE NULL,
    GhiChu NVARCHAR(255),
    CONSTRAINT PK_CT_PHIEUMUON PRIMARY KEY (MaPhieu, MaSach),
    CONSTRAINT FK_CT_PHIEU FOREIGN KEY (MaPhieu) REFERENCES PHIEUMUON(MaPhieu),
    CONSTRAINT FK_CT_SACH FOREIGN KEY (MaSach) REFERENCES SACH(MaSach)
);

-- Dữ liệu mẫu
INSERT INTO THELOAI(MaTheLoai, TenTheLoai, MoTa) VALUES
('TL01', N'Công nghệ thông tin', N'Sách tin học và lập trình'),
('TL02', N'Văn học', N'Tác phẩm văn học trong và ngoài nước'),
('TL03', N'Kinh tế', N'Sách quản trị kinh doanh và tài chính');

INSERT INTO SACH(MaSach, TenSach, TacGia, NhaXuatBan, NamXuatBan, MaTheLoai, SoLuong, SoLuongCon) VALUES
('S001', N'Lập trình Java cơ bản', N'Nguyễn Văn A', N'NXB Giáo dục', 2024, 'TL01', 10, 10),
('S002', N'Cơ sở dữ liệu SQL Server', N'Trần Thị B', N'NXB Khoa học kỹ thuật', 2023, 'TL01', 7, 7),
('S003', N'Nhà Giả Kim', N'Paulo Coelho', N'NXB Hội Nhà Văn', 2020, 'TL02', 15, 15);

INSERT INTO DOCGIA(MaDocGia, HoTen, NgaySinh, GioiTinh, SoDienThoai, Email, DiaChi) VALUES
('DG01', N'Trần Tiến Thành', '2005-05-12', N'Nam', '0987654321', 'thanh.tt@gmail.com', N'Hà Nội'),
('DG02', N'Lê Thị Mai', '2005-08-20', N'Nữ', '0912345678', 'mai.lt@gmail.com', N'Ninh Bình');
