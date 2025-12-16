CREATE DATABASE QuanLyNhaThuoc;
GO
USE QuanLyNhaThuoc;
GO

-- ===============================
-- BẢNG LOẠI KHUYẾN MÃI
-- ===============================
CREATE TABLE LoaiKhuyenMai (
	MaLKM INT IDENTITY(1,1) PRIMARY KEY,
	TenLKM NVARCHAR(20)
);

-- ===============================
-- BẢNG KHUYẾN MÃI
-- ===============================
CREATE TABLE KhuyenMai (
    MaKM NVARCHAR(10) PRIMARY KEY CHECK (MaKM LIKE 'KM[0-9][0-9][0-9]'),
    TenKM NVARCHAR(100) NOT NULL,
    NgayBatDau DATE NOT NULL,
    NgayKetThuc DATE NOT NULL,
    LoaiKhuyenMai INT NULL,
    So FLOAT CHECK (So >= 0),
    SoLuongToiDa INT CHECK (SoLuongToiDa >= 0),
	DeleteAt BIT NOT NULL DEFAULT 0,
	CONSTRAINT CK_KhuyenMai_Ngay CHECK (NgayKetThuc > NgayBatDau),
	CONSTRAINT FK_KhuyenMai_LoaiKhuyenMai FOREIGN KEY (LoaiKhuyenMai) REFERENCES LoaiKhuyenMai(MaLKM)
);

-- ===============================
-- BẢNG NHÂN VIÊN
-- ===============================
CREATE TABLE NhanVien (
    MaNV NVARCHAR(20) PRIMARY KEY CHECK (MaNV LIKE 'NV[0-9][0-9][0-9][0-9][0-9]'),
    HoTen NVARCHAR(100) NOT NULL,
    SoDienThoai NVARCHAR(15),
    Email NVARCHAR(100),
    DiaChi NVARCHAR(200),
    LuongCoBan FLOAT CHECK (LuongCoBan > 0),
    TaiKhoan NVARCHAR(60) UNIQUE,
    MatKhau NVARCHAR(60),
    IsQuanLi BIT NOT NULL DEFAULT 0,
	DeleteAt BIT NOT NULL DEFAULT 0
);

-- ===============================
-- BẢNG KHÁCH HÀNG
-- ===============================
CREATE TABLE KhachHang (
    MaKH NVARCHAR(20) PRIMARY KEY CHECK (MaKH LIKE 'KH[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    TenKH NVARCHAR(100) NOT NULL,
    SoDienThoai NVARCHAR(15) UNIQUE,
	DeleteAt BIT NOT NULL DEFAULT 0
);

-- ===============================
-- BẢNG KỆ THUỐC
-- ===============================
CREATE TABLE KeThuoc (
    MaKe NVARCHAR(10) PRIMARY KEY CHECK (MaKe LIKE 'KE[0-9][0-9][0-9][0-9]'),
    TenKe NVARCHAR(100),
    LoaiKe NVARCHAR(50),
	DeleteAt BIT NOT NULL DEFAULT 0
);

-- ===============================
-- BẢNG ĐƠN VỊ TÍNH
-- ===============================
CREATE TABLE DonViTinh (
    MaDVT INT IDENTITY(1,1) PRIMARY KEY,
    TenDVT NVARCHAR(50) NOT NULL,
	DeleteAt BIT NOT NULL DEFAULT 0
);

-- ===============================
-- BẢNG DẠNG ĐIỀU CHẾ
-- ===============================
CREATE TABLE DangDieuChe (
	MaDDC INT IDENTITY(1,1) PRIMARY KEY,
	TenDDC NVARCHAR(50),
	DeleteAt BIT NOT NULL DEFAULT 0
);

-- ===============================
-- BẢNG THUỐC
-- ===============================
CREATE TABLE Thuoc (
    MaThuoc NVARCHAR(20) PRIMARY KEY CHECK (MaThuoc LIKE 'VN-[0-9][0-9][0-9][0-9][0-9]-[0-9][0-9]'),
    TenThuoc NVARCHAR(100) NOT NULL,
    DangDieuChe INT NULL,
    HamLuong NVARCHAR(50),
    GiaBan FLOAT CHECK (GiaBan > 0),
    GiaGoc FLOAT CHECK (GiaGoc > 0),
    Thue FLOAT CHECK (Thue >= 0),
	MaDVTCoso INT NOT NULL,
    MaKe NVARCHAR(10) NOT NULL,
	DeleteAt BIT NOT NULL DEFAULT 0,
    CONSTRAINT FK_Thuoc_Ke FOREIGN KEY (MaKe) REFERENCES KeThuoc(MaKe),
    CONSTRAINT FK_Thuoc_DangDieuChe FOREIGN KEY (DangDieuChe) REFERENCES DangDieuChe(MaDDC),
	CONSTRAINT FK_Thuoc_DVTC FOREIGN KEY (MaDVTCoso) REFERENCES DonViTinh(MaDVT)
);


-- ===============================
-- BẢNG PHIẾU NHẬP
-- ===============================
CREATE TABLE PhieuNhap (
    MaPhieuNhap NVARCHAR(10) PRIMARY KEY CHECK (MaPhieuNhap LIKE 'PN[0-9][0-9][0-9][0-9][0-9][0-9]'),
    NhaCungCap NVARCHAR(100),
    NgayNhap DATE NOT NULL CHECK (NgayNhap <= GETDATE()),
	DiaChi NVARCHAR(50) NOT NULL,
	LyDo NVARCHAR(50) NOT NULL,
    MaNV NVARCHAR(20) NOT NULL,
	TongTien FLOAT NOT NULL,
	DeleteAt BIT NOT NULL DEFAULT 0,
    CONSTRAINT FK_PhieuNhap_NhanVien FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV)
);

-- ===============================
-- BẢNG CHI TIẾT PHIẾU NHẬP
-- ===============================
CREATE TABLE ChiTietPhieuNhap (
    MaPN NVARCHAR(10) NOT NULL,
    MaThuoc NVARCHAR(20) NOT NULL,
    MaDVT INT NOT NULL,
    SoLuong INT NOT NULL CHECK (SoLuong > 0),
    GiaNhap FLOAT NOT NULL CHECK (GiaNhap > 0),
	ThanhTien FLOAT NOT NULL CHECK (ThanhTien > 0),
    CONSTRAINT PK_CTPN PRIMARY KEY (MaPN, MaThuoc, MaDVT),
    CONSTRAINT FK_CTPN_PN FOREIGN KEY (MaPN) REFERENCES PhieuNhap(MaPhieuNhap),
    CONSTRAINT FK_CTPN_Thuoc FOREIGN KEY (MaThuoc) REFERENCES Thuoc(MaThuoc),
    CONSTRAINT FK_CTPN_DVT FOREIGN KEY (MaDVT) REFERENCES DonViTinh(MaDVT)
);

-- ===============================
-- BẢNG CHI TIẾT THUỐC
-- ===============================
CREATE TABLE ChiTietThuoc (
	MaCTT INT IDENTITY(1,1) PRIMARY KEY,
	MaPN NVARCHAR(10) NOT NULL,
	MaThuoc NVARCHAR(20) NOT NULL,
	HanSuDung DATE NOT NULL,
    NgaySanXuat DATE NOT NULL CHECK (NgaySanXuat <= GETDATE()),
    TonKho INT CHECK (TonKho >= 0),
	CONSTRAINT FK_CTT_PN FOREIGN KEY (MaPN) REFERENCES PhieuNhap(MaPhieuNhap),
	CONSTRAINT FK_CTT_Thuoc FOREIGN KEY (MaThuoc) REFERENCES Thuoc(MaThuoc)
);

-- ===============================
-- BẢNG HÓA ĐƠN
-- ===============================
CREATE TABLE HoaDon (
    MaHD NVARCHAR(10) PRIMARY KEY CHECK (MaHD LIKE 'HD[0-9][0-9][0-9]'),
    NgayTao DATE NOT NULL,
    MaKH NVARCHAR(20) NOT NULL,
    MaNV NVARCHAR(20) NOT NULL,
    MaKM NVARCHAR(10) NULL,
	TongTien FLOAT NOT NULL,
	DeleteAt BIT NOT NULL DEFAULT 0,
    CONSTRAINT FK_HoaDon_KhachHang FOREIGN KEY (MaKH) REFERENCES KhachHang(MaKH),
    CONSTRAINT FK_HoaDon_NhanVien FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV),
    CONSTRAINT FK_HoaDon_KhuyenMai FOREIGN KEY (MaKM) REFERENCES KhuyenMai(MaKM)
);

-- ===============================
-- BẢNG CHI TIẾT HÓA ĐƠN
-- ===============================
CREATE TABLE ChiTietHoaDon (
    MaHD NVARCHAR(10) NOT NULL,
    MaCTT INT NOT NULL,
    SoLuong INT CHECK (SoLuong > 0),
    MaDVT INT NOT NULL,
    TinhTrang NVARCHAR(20),
	ThanhTien FLOAT NOT NULL CHECK (ThanhTien > 0),
    CONSTRAINT PK_CTHD PRIMARY KEY (MaHD, MaCTT, TinhTrang),
    CONSTRAINT FK_CTHD_HoaDon FOREIGN KEY (MaHD) REFERENCES HoaDon(MaHD),
    CONSTRAINT FK_CTHD_ChiTietThuoc FOREIGN KEY (MaCTT) REFERENCES ChiTietThuoc(MaCTT),
    CONSTRAINT FK_CTHD_DonVi FOREIGN KEY (MaDVT) REFERENCES DonViTinh(MaDVT)
);

-- ===============================
-- BẢNG PHIẾU ĐẶT THUỐC
-- ===============================
CREATE TABLE PhieuDatThuoc (
    MaPDT NVARCHAR(10) PRIMARY KEY CHECK (MaPDT LIKE 'PDT[0-9][0-9][0-9]'),
    NgayTao DATE NOT NULL,
	IsThanhToan BIT NOT NULL DEFAULT 0,
    MaKH NVARCHAR(20) NOT NULL,
    MaNV NVARCHAR(20) NOT NULL,
    MaKM NVARCHAR(10) NULL,
	TongTien FLOAT NOT NULL,
	DeleteAt BIT NOT NULL DEFAULT 0,
    CONSTRAINT FK_PDT_KhachHang FOREIGN KEY (MaKH) REFERENCES KhachHang(MaKH),
    CONSTRAINT FK_PDT_NhanVien FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV),
    CONSTRAINT FK_PDT_KhuyenMai FOREIGN KEY (MaKM) REFERENCES KhuyenMai(MaKM)
);

-- ===============================
-- BẢNG CHI TIẾT PHIẾU ĐẶT THUỐC
-- ===============================
CREATE TABLE ChiTietPhieuDatThuoc (
    MaPDT NVARCHAR(10) NOT NULL,
    MaCTT INT NOT NULL,
    SoLuong INT CHECK (SoLuong > 0),
    MaDVT INT NOT NULL,
    TinhTrang NVARCHAR(20),
    ThanhTien FLOAT NOT NULL CHECK (ThanhTien > 0),
    CONSTRAINT PK_CTPDT PRIMARY KEY (MaPDT, MaCTT),
    CONSTRAINT FK_CTPDT_PDT FOREIGN KEY (MaPDT) REFERENCES PhieuDatThuoc(MaPDT),
    CONSTRAINT FK_CTPDT_ChiTietThuoc FOREIGN KEY (MaCTT) REFERENCES ChiTietThuoc(MaCTT),
    CONSTRAINT FK_CTPDT_DonVi FOREIGN KEY (MaDVT) REFERENCES DonViTinh(MaDVT)
);
GO

-- =========================
-- Dữ liệu mẫu hoàn chỉnh
-- Chạy sau khi đã CREATE TABLE (theo schema bạn gửi)
-- =========================

-- 1) DonViTinh (các đơn vị cơ sở)
INSERT INTO DonViTinh (TenDVT) VALUES
(N'Viên'), (N'Chai'), (N'Lọ'), (N'Ống'), (N'Tuýp');

-- 2) KeThuoc (tạo 5 kệ mẫu)
INSERT INTO KeThuoc (MaKe, TenKe, LoaiKe) VALUES
('KE0001', N'Kệ 1 - Tổng hợp', N'Tổng hợp'),
('KE0002', N'Kệ 2 - Giảm đau', N'Thuốc giảm đau hạ sốt'),
('KE0003', N'Kệ 3 - Kháng sinh', N'Kháng sinh'),
('KE0004', N'Kệ 4 - Tim mạch', N'Tim mạch'),
('KE0005', N'Kệ 5 - Vitamin', N'Vitamin & Khoáng chất');

-- 3) DangDieuChe (đặt thứ tự rõ ràng để tham chiếu bằng ID)
INSERT INTO DangDieuChe (TenDDC) VALUES
(N'Viên nén'),        -- 1
(N'Viên nang cứng'),  -- 2
(N'Viên nang mềm'),   -- 3
(N'Viên sủi'),        -- 4
(N'Viên ngậm'),       -- 5
(N'Viên đặt'),        -- 6
(N'Viên nén bao phim'),-- 7
(N'Viên nén kéo dài'), -- 8
(N'Siro'),             -- 9
(N'Hỗn dịch uống'),    --10
(N'Dung dịch uống'),   --11
(N'Dung dịch nhỏ mắt'),--12
(N'Dung dịch nhỏ mũi'),--13
(N'Dung dịch nhỏ tai'),--14
(N'Thuốc tiêm bột'),   --15
(N'Thuốc tiêm dung dịch'),--16
(N'Thuốc mỡ'),        --17
(N'Kem bôi'),         --18
(N'Gel bôi'),         --19
(N'Xịt mũi họng');    --20

-- 4) LoaiKhuyenMai
INSERT INTO LoaiKhuyenMai (TenLKM) VALUES
(N'Giảm theo phần trăm'), (N'Giảm theo số tiền');

-- 5) KhuyenMai (10)
INSERT INTO KhuyenMai (MaKM, TenKM, NgayBatDau, NgayKetThuc, LoaiKhuyenMai, So, SoLuongToiDa)
VALUES
('KM001', N'Giảm giá 10%', '2025-01-01', '2025-12-31', 1, 10, 100),
('KM002', N'Tặng Vitamin C', '2025-02-01', '2025-06-30', 2, 10000, 200),
('KM003', N'Mua 1 tặng 1', '2025-03-01', '2025-04-30', 1, 15, 50),
('KM004', N'Tích điểm x2', '2025-01-15', '2025-12-15', 2, 2000, 500),
('KM005', N'Giảm 20k cho HĐ >200k', '2025-02-10', '2025-05-10', 2, 20000, 300),
('KM006', N'Flash sale cuối tuần', '2025-03-05', '2025-12-31', 1, 50, 100),
('KM007', N'Combo 3 hộp giảm 15%', '2025-04-01', '2025-09-30', 1, 15, 100),
('KM008', N'Voucher 50k', '2025-01-20', '2025-07-20', 2, 5000, 100),
('KM009', N'Ưu đãi thành viên VIP', '2025-02-25', '2025-12-25', 1, 5, 500),
('KM010', N'Quà sinh nhật khách', '2025-01-01', '2025-12-31', 1, 10, 1000);

-- 6) NhanVien (10)
INSERT INTO NhanVien (MaNV, HoTen, SoDienThoai, Email, DiaChi, LuongCoBan, TaiKhoan, MatKhau, IsQuanLi)
VALUES
('NV00001', N'Nguyễn Văn A', '0901111111', 'a@demo.com', N'Hà Nội', 8000000, 'Admin001', '$2a$10$X/xFV0CNENZygt45r6Mv4uZvpzasse8vZP4JILoCIflAJe/iV1Wo.', 1),
('NV00002', N'Trần Thị B', '0902222222', 'b@demo.com', N'Hồ Chí Minh', 7000000, 'Admin002', '$2a$10$kkpueKFVcVU8G7xt18LjFeLmA9aDVIBQkj7x09r.1/ZgynqagApRS', 0),
('NV00003', N'Lê Văn C', '0903333333', 'c@demo.com', N'Đà Nẵng', 7500000, 'Admin003', '$2a$10$Rab1dh6FYroUgva3Uns2XO0XmTwV6GR6.hu3TntMdsdWDL5ImMyEe', 0),
('NV00004', N'Phạm Thị D', '0904444444', 'd@demo.com', N'Hải Phòng', 6000000, 'Admin004', '$2a$10$tKCsBoJdAMnD0U7lDi3I8.wO5aCyVdgOaGWE5IbOGjCvQESLOaMU.', 0),
('NV00005', N'Đỗ Văn E', '0905555555', 'e@demo.com', N'Cần Thơ', 9000000, 'Admin005', '$2a$10$EoEt3vVfQyjuPifUshnBp.9d2yZXD16W4hgqC7t1n8M7qNlKQYd4.', 1),
('NV00006', N'Hoàng Văn F', '0906666666', 'f@demo.com', N'Nghệ An', 6500000, 'Admin006', '$2a$10$aSuLOsWiB8yKQtUiXq8Hb.YheuIeg.Cl5u/A7Tm4uOt7kclZ8fuSe', 0),
('NV00007', N'Nguyễn Thị G', '0907777777', 'g@demo.com', N'Quảng Ninh', 7200000, 'Admin007', '$2a$10$E0GiCfQc1eMA1fSxxNPlhOyPyPGqjb8L.wFfMOKe3T4hMa7tqcyy.', 0),
('NV00008', N'Trần Văn H', '0908888888', 'h@demo.com', N'Hồ Chí Minh', 8100000, 'Admin008', '$2a$10$yKREYSAWILFTrvWZqKAMaeOknKXPuAzFMNE8GLKzbsVzmfh4PxAPW', 0),
('NV00009', N'Vũ Văn I', '0909999999', 'i@demo.com', N'Hà Nội', 5600000, 'Admin009', '$2a$10$pmvOTNGy5T9XbGx15ZaNLOu65Z204ef7PGCQIEvNgMbiGQS0DIQMi', 0),
('NV00010', N'Lê Thị K', '0910000000', 'k@demo.com', N'Đồng Nai', 7700000, 'Admin010', '$2a$10$NBgxliiMLOwW2noPBBhUOuQkuDCx6.XXg8.5n78SZCHyPS7.ri.fK', 0);

INSERT INTO NhanVien (MaNV, HoTen, SoDienThoai, Email, DiaChi, LuongCoBan, TaiKhoan, MatKhau, IsQuanLi)
VALUES
('NV00011', N'Phạm Đăng Khoa', '0391199992', 'dk@gmail.com', N'Long Định', 15000000, 'dangkhoa123', '$2a$10$.Zk2ghacRW2GFixl.RBHY.FSABSzOGa7RPbq3ubt3NkZRGdVJc7F.', 1);

-- 7) KhachHang (10)
INSERT INTO KhachHang (MaKH, TenKH, SoDienThoai)
VALUES
('KH000000001', N'Nguyễn Văn Nam', '0911111111'),
('KH000000002', N'Trần Thị Lan', '0912222222'),
('KH000000003', N'Lê Văn Minh', '0913333333'),
('KH000000004', N'Phạm Thị Hoa', '0914444444'),
('KH000000005', N'Đỗ Văn An', '0915555555'),
('KH000000006', N'Hoàng Văn Bình', '0916666666'),
('KH000000007', N'Nguyễn Thị Cúc', '0917777777'),
('KH000000008', N'Trần Văn Dũng', '0918888888'),
('KH000000009', N'Vũ Văn Khải', '0919999999'),
('KH000000010', N'Lê Thị Mai', '0920000000');

-- 8) Thuốc (50 dòng)
INSERT INTO Thuoc (MaThuoc, TenThuoc, DangDieuChe, HamLuong, GiaBan, GiaGoc, Thue, MaDVTCoso, MaKe)
VALUES
('VN-10001-01', N'Paracetamol 500mg', 1, N'500mg', 2000, 1500, 0.05, 1, 'KE0001'),
('VN-10002-01', N'Ibuprofen 400mg', 1, N'400mg', 3500, 2800, 0.05, 1, 'KE0002'),
('VN-10003-01', N'Amoxicillin 500mg', 3, N'500mg', 5000, 4000, 0.1, 1, 'KE0003'),
('VN-10004-01', N'Vitamin C 1000mg', 9, N'1000mg', 2500, 2000, 0.05, 1, 'KE0004'),
('VN-10005-01', N'Cefuroxime 250mg', 3, N'250mg', 7000, 5800, 0.1, 1, 'KE0005'),
('VN-10006-01', N'Azithromycin 250mg', 3, N'250mg', 8500, 7000, 0.1, 1, 'KE0001'),
('VN-10007-01', N'Ciprofloxacin 500mg', 3, N'500mg', 6500, 5200, 0.1, 1, 'KE0002'),
('VN-10008-01', N'Loratadine 10mg', 1, N'10mg', 2500, 2000, 0.05, 1, 'KE0003'),
('VN-10009-01', N'Cetirizine 10mg', 1, N'10mg', 2200, 1800, 0.05, 1, 'KE0004'),
('VN-10010-01', N'Metformin 500mg', 1, N'500mg', 3000, 2400, 0.05, 1, 'KE0005'),
('VN-10011-01', N'Glimepiride 2mg', 1, N'2mg', 3500, 2700, 0.05, 1, 'KE0001'),
('VN-10012-01', N'Losartan 50mg', 1, N'50mg', 4000, 3200, 0.05, 1, 'KE0002'),
('VN-10013-01', N'Amlodipine 5mg', 1, N'5mg', 3800, 3000, 0.05, 1, 'KE0003'),
('VN-10014-01', N'Omeprazole 20mg', 1, N'20mg', 2800, 2200, 0.05, 1, 'KE0004'),
('VN-10015-01', N'Pantoprazole 40mg', 1, N'40mg', 3200, 2500, 0.05, 1, 'KE0005'),
('VN-10016-01', N'Domperidone 10mg', 1, N'10mg', 2600, 2100, 0.05, 1, 'KE0001'),
('VN-10017-01', N'Ranitidine 150mg', 1, N'150mg', 2400, 1900, 0.05, 1, 'KE0002'),
('VN-10018-01', N'Simvastatin 20mg', 1, N'20mg', 3000, 2400, 0.05, 1, 'KE0003'),
('VN-10019-01', N'Atorvastatin 10mg', 1, N'10mg', 3400, 2700, 0.05, 1, 'KE0004'),
('VN-10020-01', N'Clopidogrel 75mg', 1, N'75mg', 5000, 4200, 0.1, 1, 'KE0005'),
('VN-10021-01', N'Aspirin 81mg', 1, N'81mg', 1500, 1200, 0.05, 1, 'KE0001'),
('VN-10022-01', N'Metronidazole 250mg', 3, N'250mg', 2700, 2100, 0.05, 1, 'KE0002'),
('VN-10023-01', N'Doxycycline 100mg', 3, N'100mg', 3200, 2500, 0.05, 1, 'KE0003'),
('VN-10024-01', N'Clarithromycin 500mg', 3, N'500mg', 8000, 6500, 0.1, 1, 'KE0004'),
('VN-10025-01', N'Levofloxacin 500mg', 3, N'500mg', 9000, 7500, 0.1, 1, 'KE0005'),
('VN-10026-01', N'Bromhexine 8mg', 1, N'8mg', 2200, 1800, 0.05, 1, 'KE0001'),
('VN-10027-01', N'Ambroxol 30mg', 1, N'30mg', 2300, 1900, 0.05, 1, 'KE0002'),
('VN-10028-01', N'Guaifenesin 100mg', 1, N'100mg', 2000, 1600, 0.05, 1, 'KE0003'),
('VN-10029-01', N'Salbutamol 2mg', 1, N'2mg', 2600, 2100, 0.05, 1, 'KE0004'),
('VN-10030-01', N'Montelukast 10mg', 1, N'10mg', 5000, 4200, 0.1, 1, 'KE0005'),
('VN-10031-01', N'Cetrimonium Chloride 5%', 9, N'5%', 25000, 20000, 0.05, 1, 'KE0001'),
('VN-10032-01', N'Sodium Chloride 0.9%', 4, N'0.9%', 10000, 8000, 0.05, 1, 'KE0002'),
('VN-10033-01', N'Glucose 5%', 4, N'5%', 9500, 7500, 0.05, 1, 'KE0003'),
('VN-10034-01', N'Ringer Lactate', 4, N'—', 12000, 9500, 0.05, 1, 'KE0004'),
('VN-10035-01', N'Vitamin B1 100mg', 9, N'100mg', 2000, 1500, 0.05, 1, 'KE0005'),
('VN-10036-01', N'Vitamin B6 50mg', 9, N'50mg', 1800, 1400, 0.05, 2, 'KE0001'),
('VN-10037-01', N'Vitamin B12 500mcg', 9, N'500mcg', 2500, 1900, 0.05, 1, 'KE0002'),
('VN-10038-01', N'Calcium Carbonate 500mg', 1, N'500mg', 3000, 2500, 0.05, 1, 'KE0003'),
('VN-10039-01', N'Ferrous Sulfate 325mg', 1, N'325mg', 2800, 2300, 0.05, 1, 'KE0004'),
('VN-10040-01', N'Folic Acid 5mg', 1, N'5mg', 2500, 2000, 0.05, 1, 'KE0005'),
('VN-10041-01', N'Zinc Gluconate 10mg', 1, N'10mg', 2200, 1800, 0.05, 1, 'KE0001'),
('VN-10042-01', N'Magnesium B6', 1, N'—', 3000, 2400, 0.05, 1, 'KE0002'),
('VN-10043-01', N'Probiotic 3Billion CFU', 9, N'3 tỷ CFU', 4500, 3500, 0.05, 1, 'KE0003'),
('VN-10044-01', N'Loperamide 2mg', 1, N'2mg', 2700, 2100, 0.05, 1, 'KE0004'),
('VN-10045-01', N'ORS Gói', 7, N'20.5g', 1500, 1200, 0.05, 1, 'KE0005'),
('VN-10046-01', N'Clotrimazole 1%', 6, N'1%', 5000, 4200, 0.05, 4, 'KE0001'),
('VN-10047-01', N'Mupirocin 2%', 6, N'2%', 6500, 5500, 0.05, 4, 'KE0002'),
('VN-10048-01', N'Hydrocortisone Cream 1%', 6, N'1%', 4800, 4000, 0.05, 4, 'KE0003'),
('VN-10049-01', N'Betamethasone 0.05%', 6, N'0.05%', 5500, 4500, 0.05, 4, 'KE0004'),
('VN-10050-01', N'Silver Sulfadiazine 1%', 6, N'1%', 6000, 5000, 0.05, 4, 'KE0005');


-- 9) PhieuNhap (10)
INSERT INTO PhieuNhap (MaPhieuNhap, NhaCungCap, NgayNhap, DiaChi, LyDo, MaNV, TongTien)
VALUES
('PN000001', N'Công ty Dược A', '2024-05-02', N'Hà Nội', N'Nhập hàng định kỳ', 'NV00001', 1500000),
('PN000002', N'Công ty Dược B', '2024-06-16', N'Hồ Chí Minh', N'Bổ sung kho', 'NV00002', 2400000),
('PN000003', N'Công ty Dược C', '2024-04-21', N'Đà Nẵng', N'Nhập thử', 'NV00003', 1800000),
('PN000004', N'Công ty Dược D', '2024-03-11', N'Hải Phòng', N'Nhập hàng', 'NV00004', 900000),
('PN000005', N'Công ty Dược E', '2024-02-02', N'Cần Thơ', N'Nhập hàng', 'NV00005', 1100000),
('PN000006', N'Công ty Dược F', '2024-01-25', N'Nghệ An', N'Nhập hàng', 'NV00006', 1250000),
('PN000007', N'Công ty Dược G', '2024-05-20', N'Quảng Ninh', N'Nhập hàng', 'NV00007', 980000),
('PN000008', N'Công ty Dược H', '2024-06-05', N'Hồ Chí Minh', N'Nhập hàng', 'NV00008', 2100000),
('PN000009', N'Công ty Dược I', '2024-04-30', N'Hà Nội', N'Nhập hàng', 'NV00009', 500000),
('PN000010', N'Công ty Dược J', '2024-03-18', N'Đồng Nai', N'Nhập hàng', 'NV00010', 1750000),
('PN000011', N'Công ty Dược A', '2024-05-02', N'Hà Nội', N'Nhập hàng định kỳ', 'NV00001', 1500000);

-- 10) ChiTietPhieuNhap (50 dòng, chuẩn hóa)
INSERT INTO ChiTietPhieuNhap (MaPN, MaThuoc, MaDVT, SoLuong, GiaNhap, ThanhTien)
VALUES
-- PN000001 (5 dòng)
('PN000001', 'VN-10001-01', 1, 200, 1200, 240000),
('PN000001', 'VN-10002-01', 1, 150, 1500, 225000),
('PN000001', 'VN-10003-01', 1, 120, 2000, 240000),
('PN000001', 'VN-10004-01', 1, 300, 2400, 720000),
('PN000001', 'VN-10005-01', 1, 180, 3000, 540000),

-- PN000002 (5 dòng)
('PN000002', 'VN-10006-01', 1, 160, 2600, 416000),
('PN000002', 'VN-10007-01', 1, 190, 3500, 665000),
('PN000002', 'VN-10008-01', 1, 140, 2000, 280000),
('PN000002', 'VN-10009-01', 1, 130, 1500, 320000),
('PN000002', 'VN-10010-01', 1, 170, 2500, 378000),

-- PN000003 (5 dòng)
('PN000003', 'VN-10011-01', 1, 150, 1800, 360000),
('PN000003', 'VN-10012-01', 1, 190, 2500, 250000),
('PN000003', 'VN-10013-01', 1, 160, 2000, 300000),
('PN000003', 'VN-10014-01', 1, 120, 2700, 324000),
('PN000003', 'VN-10015-01', 1, 140, 3000, 240000),

-- PN000004 (5 dòng)
('PN000004', 'VN-10016-01', 1, 180, 2400, 260000),
('PN000004', 'VN-10017-01', 1, 200, 2000, 196000),
('PN000004', 'VN-10018-01', 1, 130, 2500, 270000),
('PN000004', 'VN-10019-01', 1, 120, 3000, 432000),
('PN000004', 'VN-10020-01', 1, 160, 4000, 500000),

-- PN000005 (5 dòng)
('PN000005', 'VN-10021-01', 1, 300, 1000, 300000),
('PN000005', 'VN-10022-01', 1, 180, 2500, 280000),
('PN000005', 'VN-10023-01', 1, 190, 3000, 280000),
('PN000005', 'VN-10024-01', 1, 100, 4500, 270000),
('PN000005', 'VN-10025-01', 1, 90, 3200, 288000),

-- PN000006 (5 dòng)
('PN000006', 'VN-10026-01', 1, 160, 1600, 448000),
('PN000006', 'VN-10027-01', 1, 200, 1800, 348000),
('PN000006', 'VN-10028-01', 1, 170, 1000, 420000),
('PN000006', 'VN-10029-01', 1, 120, 2000, 450000),
('PN000006', 'VN-10030-01', 1, 130, 3200, 520000),

-- PN000007 (5 dòng)
('PN000007', 'VN-10031-01', 1, 100, 17000, 360000),
('PN000007', 'VN-10032-01', 1, 120, 7000, 420000),
('PN000007', 'VN-10033-01', 1, 150, 6800, 476000),
('PN000007', 'VN-10034-01', 1, 200, 6600, 528000),
('PN000007', 'VN-10035-01', 1, 180, 1400, 576000),

-- PN000008 (5 dòng)
('PN000008', 'VN-10036-01', 1, 170, 1200, 820000),
('PN000008', 'VN-10037-01', 1, 160, 1800, 546000),
('PN000008', 'VN-10038-01', 1, 150, 2500, 450000),
('PN000008', 'VN-10039-01', 1, 140, 2000, 400000),
('PN000008', 'VN-10040-01', 1, 120, 1500, 340000),

-- PN000009 (5 dòng)
('PN000009', 'VN-10041-01', 1, 130, 1400, 1400000),
('PN000009', 'VN-10042-01', 1, 200, 1300, 1040000),
('PN000009', 'VN-10043-01', 1, 150, 2000, 840000),
('PN000009', 'VN-10044-01', 1, 140, 2500, 1125000),
('PN000009', 'VN-10045-01', 1, 300, 1000, 810000),

-- PN000010 (5 dòng)
('PN000010', 'VN-10046-01', 4, 90, 3800, 3600000),
('PN000010', 'VN-10047-01', 4, 100, 5000, 3000000),
('PN000010', 'VN-10048-01', 4, 110, 3000, 3750000),
('PN000010', 'VN-10049-01', 4, 95, 3200, 3600000),
('PN000010', 'VN-10050-01', 4, 85, 2200, 2200000),

('PN000011', 'VN-10001-01', 1, 100, 1200, 120000),
('PN000011', 'VN-10002-01', 1, 150, 1500, 225000);


-- 11) Chi tiết thuốc (50 dòng)
INSERT INTO ChiTietThuoc (MaPN, MaThuoc, HanSuDung, NgaySanXuat, TonKho)
VALUES
('PN000001', 'VN-10001-01', '2027-12-31', '2024-05-01', 200),
('PN000011', 'VN-10001-01', '2028-12-31', '2025-05-01', 100),
('PN000001', 'VN-10002-01', '2026-11-30', '2024-06-15', 150),
('PN000011', 'VN-10002-01', '2027-11-30', '2025-06-15', 150),
('PN000001', 'VN-10003-01', '2027-10-15', '2024-04-20', 120),
('PN000001', 'VN-10004-01', '2026-09-01', '2024-03-10', 300),
('PN000001', 'VN-10005-01', '2027-08-01', '2024-05-22', 180),
('PN000002', 'VN-10006-01', '2027-06-30', '2024-06-01', 160),
('PN000002', 'VN-10007-01', '2027-05-15', '2024-04-30', 190),
('PN000002', 'VN-10008-01', '2026-12-31', '2024-03-05', 140),
('PN000002', 'VN-10009-01', '2027-02-28', '2024-05-10', 130),
('PN000002', 'VN-10010-01', '2028-01-15', '2024-04-01', 170),
('PN000003', 'VN-10011-01', '2028-03-01', '2024-05-25', 150),
('PN000003', 'VN-10012-01', '2027-11-30', '2024-06-10', 190),
('PN000003', 'VN-10013-01', '2027-12-15', '2024-03-20', 160),
('PN000003', 'VN-10014-01', '2026-10-30', '2024-04-15', 120),
('PN000003', 'VN-10015-01', '2027-09-01', '2024-03-25', 140),
('PN000004', 'VN-10016-01', '2027-07-31', '2024-05-01', 180),
('PN000004', 'VN-10017-01', '2027-08-31', '2024-06-05', 200),
('PN000004', 'VN-10018-01', '2028-02-28', '2024-04-10', 130),
('PN000004', 'VN-10019-01', '2027-12-31', '2024-05-20', 120),
('PN000004', 'VN-10020-01', '2028-01-15', '2024-04-01', 160),
('PN000005', 'VN-10021-01', '2027-11-15', '2024-03-25', 300),
('PN000005', 'VN-10022-01', '2027-10-10', '2024-05-01', 180),
('PN000005', 'VN-10023-01', '2027-09-09', '2024-04-15', 190),
('PN000005', 'VN-10024-01', '2027-12-30', '2024-03-10', 100),
('PN000005', 'VN-10025-01', '2027-08-31', '2024-06-20', 90),
('PN000006', 'VN-10026-01', '2026-12-01', '2024-04-30', 160),
('PN000006', 'VN-10027-01', '2027-03-01', '2024-03-15', 200),
('PN000006', 'VN-10028-01', '2027-05-15', '2024-04-12', 170),
('PN000006', 'VN-10029-01', '2027-06-01', '2024-05-01', 120),
('PN000006', 'VN-10030-01', '2027-09-30', '2024-06-01', 130),
('PN000007', 'VN-10031-01', '2026-12-31', '2024-03-10', 100),
('PN000007', 'VN-10032-01', '2026-11-30', '2024-04-05', 120),
('PN000007', 'VN-10033-01', '2026-10-31', '2024-03-01', 150),
('PN000007', 'VN-10034-01', '2027-01-31', '2024-04-10', 200),
('PN000007', 'VN-10035-01', '2027-04-30', '2024-05-20', 180),
('PN000008', 'VN-10036-01', '2027-06-30', '2024-03-15', 170),
('PN000008', 'VN-10037-01', '2027-08-15', '2024-04-05', 160),
('PN000008', 'VN-10038-01', '2027-10-01', '2024-06-25', 150),
('PN000008', 'VN-10039-01', '2027-12-31', '2024-05-10', 140),
('PN000008', 'VN-10040-01', '2028-01-15', '2024-04-01', 120),
('PN000009', 'VN-10041-01', '2027-09-09', '2024-03-25', 130),
('PN000009', 'VN-10042-01', '2027-07-31', '2024-05-10', 200),
('PN000009', 'VN-10043-01', '2027-06-15', '2024-06-01', 150),
('PN000009', 'VN-10044-01', '2027-05-01', '2024-03-10', 140),
('PN000009', 'VN-10045-01', '2027-03-31', '2024-04-10', 300),
('PN000010', 'VN-10046-01', '2027-02-15', '2024-05-01', 90),
('PN000010', 'VN-10047-01', '2027-09-30', '2024-04-25', 100),
('PN000010', 'VN-10048-01', '2027-10-15', '2024-06-01', 110),
('PN000010', 'VN-10049-01', '2027-11-01', '2024-03-30', 95),
('PN000010', 'VN-10050-01', '2028-01-01', '2024-05-05', 85);

-- 12) HoaDon (10)
INSERT INTO HoaDon (MaHD, NgayTao, MaKH, MaNV, MaKM, TongTien) VALUES
('HD001', '2025-01-10', 'KH000000001', 'NV00001', 'KM001', 18900),
('HD002', '2025-01-15', 'KH000000002', 'NV00002', 'KM002', 63500),
('HD003', '2025-02-05', 'KH000000003', 'NV00003', 'KM003', 70125),
('HD004', '2025-02-10', 'KH000000004', 'NV00004', 'KM004', 29500),
('HD005', '2025-03-01', 'KH000000005', 'NV00005', 'KM005', 118600),
('HD006', '2025-03-12', 'KH000000006', 'NV00006', 'KM006', 116875),
('HD007', '2025-04-05', 'KH000000007', 'NV00007', 'KM007', 182325),
('HD008', '2025-04-18', 'KH000000008', 'NV00008', 'KM008', 21250),
('HD009', '2025-05-01', 'KH000000009', 'NV00009', 'KM009', 17556),
('HD010', '2025-05-10', 'KH000000010', 'NV00010', 'KM010', 42525);

-- 13) ChiTietHoaDon (10)
INSERT INTO ChiTietHoaDon (MaHD, MaCTT, SoLuong, MaDVT, TinhTrang, ThanhTien) VALUES
('HD001','1',10,1, N'Bán', 21000),
('HD002','3',20,1, N'Bán', 73500),
('HD003','5',15,1, N'Bán', 82500),
('HD004','6',12,1, N'Bán', 31500),
('HD005','7',18,1, N'Bán', 138600),
('HD006','8',25,1, N'Bán', 233750),
('HD007','9',30,1, N'Bán', 214500),
('HD008','10',10,1, N'Bán', 26250),
('HD009','11',8,1, N'Bán', 18480),
('HD010','12',15,1, N'Bán', 47250);

-- 14) PhieuDatThuoc (10)
INSERT INTO PhieuDatThuoc (MaPDT, NgayTao, IsThanhToan, MaKH, MaNV, MaKM, TongTien) VALUES
('PDT001', '2025-01-05', 0, 'KH000000002', 'NV00002', NULL, 17600),
('PDT002', '2025-01-20', 1, 'KH000000003', 'NV00003', NULL, 9900),
('PDT003', '2025-02-18', 0, 'KH000000004', 'NV00001', NULL, 10500),
('PDT004', '2025-03-10', 1, 'KH000000005', 'NV00004', NULL, 55000),
('PDT005', '2025-03-15', 0, 'KH000000006', 'NV00005', NULL, 7875),
('PDT006', '2025-03-20', 1, 'KH000000007', 'NV00006', NULL, 28280),
('PDT007', '2025-04-02', 0, 'KH000000008', 'NV00007', NULL, 6300),
('PDT008', '2025-04-14', 1, 'KH000000009', 'NV00008', NULL, 2310),
('PDT009', '2025-05-03', 0, 'KH000000010', 'NV00009', NULL, 2940),
('PDT010', '2025-05-09', 1, 'KH000000001', 'NV00010', NULL, 26250);

-- 15) ChiTietPhieuDatThuoc (10+)
INSERT INTO ChiTietPhieuDatThuoc VALUES
('PDT001', 26, 2, 1, N'Đặt', 17600),
('PDT001', 27, 1, 1, N'Đặt', 9900),
('PDT002', 1, 5, 1, N'Đặt', 10500),
('PDT003', 5, 10, 1, N'Đặt', 55000),
('PDT004', 7, 3, 1, N'Đặt', 7875),
('PDT005', 9, 4, 1, N'Đặt', 28280),
('PDT006', 12, 2, 1, N'Đặt', 6300),
('PDT007', 11, 1, 1, N'Đặt', 2310),
('PDT008', 41, 1, 1, N'Đặt', 2940),
('PDT009', 33, 1, 1, N'Đặt', 26250)
GO

select * from ChiTietHoaDon

