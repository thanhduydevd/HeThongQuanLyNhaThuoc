package com.antam.app.dao;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 05/12/2025
 * @version: 1.0
 */

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChiTietPhieuDat_DAO {

    public static void themChiTietPhieuDatVaoDBS(ChiTietPhieuDatThuoc ct) {
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sql = "INSERT INTO ChiTietPhieuDatThuoc (MaPDT, MaCTT, SoLuong, MaDVT, TinhTrang, ThanhTien) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ct.getMaPhieu().getMaPhieu());
            ps.setInt(2, ct.getChiTietThuoc().getMaCTT());
            ps.setInt   (3, ct.getSoLuong());
            ps.setInt   (4, ct.getDonViTinh().getMaDVT());
            ps.setString(5, "Đặt");
            ps.setDouble(6, ct.getThanhTien());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Lấy danh sách chi tiết phiếu đặt thuốc theo mã phiếu đặt thuốc
     * @param maPDT - mã phiếu đặt thuốc
     * @return danh sách chi tiết phiếu đặt thuốc
     */
    public static ArrayList<ChiTietPhieuDatThuoc> getChiTietTheoPhieu(String maPDT) {
        ArrayList<ChiTietPhieuDatThuoc> ds = new ArrayList<>();

        String sql = """
        SELECT MaPDT, MaCTT, SoLuong, MaDVT, TinhTrang, ThanhTien
        FROM ChiTietPhieuDatThuoc
        WHERE MaPDT = ?
    """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maPDT);

            try (ResultSet rs = ps.executeQuery()) {

                ChiTietThuoc_DAO ctThuocDAO = new ChiTietThuoc_DAO();
                DonViTinh_DAO dvtDAO = new DonViTinh_DAO();

                PhieuDatThuoc phieu =
                        PhieuDat_DAO.getPhieuDatByMaFromDBS(maPDT);

                while (rs.next()) {

                    int maCTT = rs.getInt("MaCTT");
                    int soLuong = rs.getInt("SoLuong");
                    int maDVT = rs.getInt("MaDVT");
                    String tinhTrang = rs.getString("TinhTrang");

                    ChiTietThuoc ctThuoc = ctThuocDAO.getChiTietThuoc(maCTT);
                    DonViTinh donVi = dvtDAO.getDVTTheoMa(maDVT);

                    ChiTietPhieuDatThuoc ct = new ChiTietPhieuDatThuoc();
                    ct.setMaPhieu(phieu);
                    ct.setChiTietThuoc(ctThuoc);
                    ct.setSoLuong(soLuong);
                    ct.setDonViTinh(donVi);
                    ct.setThanhTien();
                    ct.setThanhToan("Thanh toán".equalsIgnoreCase(tinhTrang));

                    ds.add(ct);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    /**
     * Thanh toán chi tiết phiếu đặt thuốc với mã phiếu đặt thuốc
     * @param maPDT - mã phiếu đặt thuốc
     * @return true nếu thành công, false nếu thất bại
     */
    public static boolean thanhToanChiTietVoiMa(String maPDT) {
        String sql = "UPDATE ChiTietPhieuDatThuoc SET TinhTrang = 'Thanh toán' WHERE MaPDT = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maPDT);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean huyChiTietPhieu(String maPDT) {
        String sql = "UPDATE ChiTietPhieuDatThuoc SET TinhTrang = N'Hủy' WHERE MaPDT = ?";
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maPDT);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean khoiPhucChiTietPhieu(String maPDT) {
        String sql = """
        UPDATE ChiTietPhieuDatThuoc
        SET TinhTrang = 'Đặt'
        WHERE MaPDT = ?
    """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maPDT);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




}