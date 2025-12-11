package com.antam.app.dao;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 02/10/2025
 * @version: 1.0
 */

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class PhieuDat_DAO {
    public static ArrayList<PhieuDatThuoc> list = PhieuDat_DAO.getAllPhieuDatThuocFromDBS();

    public static Thuoc_DAO thuoc_dao = new Thuoc_DAO();

    /**
     * Lấy toàn bộ danh sách phiếu đặt thuốc với thông tin bao gồm
     * mã, ngày tạo, đã thanh toán hay chưa, mã khách, mã nhân viên tạo
     * thành tiền của phiếu đặt
     * @return Array[PhieuDatThuoc]
     */
    public static ArrayList<PhieuDatThuoc> getAllPhieuDatThuocFromDBS(){
        ArrayList<PhieuDatThuoc> ds = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Connection con = ConnectDB.getConnection();

        try {
            String sql = "select * from PhieuDatThuoc";
            PreparedStatement state = con.prepareStatement(sql);
            ResultSet kq = state.executeQuery();
            while(kq.next()){
                String ma = kq.getString(1);
                LocalDate ngay = kq.getDate(2).toLocalDate();
                boolean isThanhToan = kq.getBoolean(3);
                String maKhach = kq.getString(4);
                String maNV = kq.getString(5);
                String maKM = kq.getString(6);
                double total = kq.getDouble(7);
                PhieuDatThuoc e = new PhieuDatThuoc(ma,ngay,isThanhToan,
                        NhanVien_DAO.dsNhanViens.stream()
                                .filter(t-> t.getMaNV().equalsIgnoreCase(maNV))
                                .findFirst().orElse(null),
                        KhachHang_DAO.loadBanFromDB().stream()
                                .filter(t->t.getMaKH().equalsIgnoreCase(maKhach))
                                .findFirst().orElse(null),
                        KhuyenMai_DAO.getAllKhuyenMaiConHieuLuc().stream()
                                .filter(t->t.getMaKM().equalsIgnoreCase(maKM))
                                .findFirst().orElse(null),total);
                ds.add(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ds;
    }

    public static boolean themPhieuDatThuocVaoDBS(PhieuDatThuoc i) {
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();

            // Kiểm tra đã tồn tại hay chưa
            String sqlCheck = "SELECT * FROM PhieuDatThuoc WHERE MaPDT = ?";
            PreparedStatement checkStmt = con.prepareStatement(sqlCheck);
            checkStmt.setString(1, i.getMaPhieu());
            ResultSet check = checkStmt.executeQuery();
            if (check.next()) {
                return false; // đã tồn tại
            }

            // Câu lệnh thêm mới
            String updateSQL = "INSERT INTO PhieuDatThuoc " +
                    "([MaPDT], [NgayTao], [IsThanhToan], [MaKH], [MaNV], [MaKM], [TongTien]) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement state = con.prepareStatement(updateSQL);
            state.setString(1, i.getMaPhieu());
            state.setDate(2, Date.valueOf(i.getNgayTao()));
            state.setBoolean(3, i.isThanhToan());
            state.setString(4, i.getKhachHang().getMaKH());
            state.setString(5, i.getNhanVien().getMaNV());
            state.setString(6, i.getKhuyenMai() != null ? i.getKhuyenMai().getMaKM() : null);
            state.setDouble(7, i.getTongTien());

            int kq = state.executeUpdate();
            return kq > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * Xoá phiếu đặt thuốc trong DBS bằng cách tắt trạng thái hoạt động.
     * @param maPDT mã phiếu đặt thuốc
     * @return true nếu cập nhật thành công. false nếu không thể cập nhật.
     */
    public static boolean xoaPhieuDatThuocTrongDBS(String maPDT){
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Connection con = ConnectDB.getConnection();

        try {
            String sql = "update PhieuDatThuoc set DeleteAt = 1 where MaPDT = ?";
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1,maPDT);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * Lấy mã hash lớn nhẩt trong database
     * @return String - mã phiếu đặt thuốc mới nhất.
     *          null nếu không có gì trong dbs.
     */
    public static String getMaxHash(){
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            Connection con = ConnectDB.getConnection();
            String sql = "select top 1 MaPDT from PhieuDatThuoc order by MaPDT desc";
            PreparedStatement state = con.prepareStatement(sql);
            ResultSet kq = state.executeQuery();
            while (kq.next()){
                return kq.getString(1).substring(4,6);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    public static void themChiTietPhieuDatVaoDBS(ChiTietPhieuDatThuoc ctPDT) {
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Connection con = ConnectDB.getConnection();

        try {
            String updateSQL = "insert into ChiTietPhieuDatThuoc values(?,?,?,?,?,?)";
            PreparedStatement state = con.prepareStatement(updateSQL);
            state.setString(1,ctPDT.getMaPhieu().getMaPhieu());
            state.setString(2,ctPDT.getSoDangKy().getMaThuoc());
            state.setInt(3,ctPDT.getSoLuong());
            state.setInt(4,ctPDT.getDonViTinh().getMaDVT());
            state.setString(5,"Đặt");
            state.setDouble(6,ctPDT.getThanhTien());
            int kq = state.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<ChiTietPhieuDatThuoc> getChiTietTheoPhieu(String maPDT) {
        ArrayList<ChiTietPhieuDatThuoc> dsChiTiet = new ArrayList<>();

        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();

            String sql = """
            SELECT MaPDT, MaThuoc, SoLuong, MaDVT, TinhTrang, ThanhTien
            FROM ChiTietPhieuDatThuoc
            WHERE MaPDT = ?
        """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maPDT);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String maThuoc = rs.getString("MaThuoc");
                int soLuong = rs.getInt("SoLuong");
                String maDVT = rs.getString("MaDVT");
                String tinhTrang = rs.getString("TinhTrang");
                double thanhTien = rs.getDouble("ThanhTien");
                String phieuDatMa = rs.getString("MaPDT");

                // Gọi thuốc để lấy đối tượng chi tiết
                Thuoc thuoc = thuoc_dao.getThuocTheoMa(maThuoc);
                DonViTinh donVi = thuoc.getMaDVTCoSo();
                PhieuDatThuoc phieu = list.stream()
                        .filter(p -> p.getMaPhieu().equalsIgnoreCase(phieuDatMa))
                        .findFirst()
                        .orElse(null);

                ChiTietPhieuDatThuoc ct = new ChiTietPhieuDatThuoc();
                ct.setMaPhieu(phieu);
                ct.setSoDangKy(thuoc);
                ct.setSoLuong(soLuong);
                ct.setDonViTinh(donVi);
                ct.setThanhToan(!tinhTrang.equalsIgnoreCase("Đặt"));
                dsChiTiet.add(ct);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dsChiTiet;
    }

    public static void capNhatThanhToanPhieuDat(String maPDT) {
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Connection con = ConnectDB.getConnection();

        try {
            String sql = "update PhieuDatThuoc set IsThanhToan = 1 where MaPDT = ?";
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1,maPDT);
            int kq = state.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
