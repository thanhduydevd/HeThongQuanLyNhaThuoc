/*
 * @ (#) ChiTietHoaDon_DAO.java   1.0 10/3/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dao;

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/3/2025
 * version: 1.0
 */
public class ChiTietHoaDon_DAO {

    /**
     * Lấy danh sách chi tiết hóa đơn theo mã hóa đơn
     * @param maHD mã hóa đơn
     * @return danh sách chi tiết hóa đơn
     */
    public static List<ChiTietHoaDon> getChiTietByMaHD(String maHD) {
        List<ChiTietHoaDon> ds = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE MaHD = ?";
        ChiTietThuoc_DAO chiTietThuocDAO = new ChiTietThuoc_DAO();
        DonViTinh_DAO donViTinhDAO = new DonViTinh_DAO();

        // Sử dụng try-with-resources để quản lý connection tự động
        try (Connection con = ConnectDB.getInstance().connect();
             PreparedStatement statement = con.prepareStatement(sql)) {

            statement.setString(1, maHD);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String maHoaDon = rs.getString("MaHD");
                    int maChiTietThuoc = rs.getInt("MaCTT");
                    int soLuong = rs.getInt("SoLuong");
                    int maDonViTinh = rs.getInt("MaDVT");
                    String tinhTrang = rs.getString("TinhTrang");
                    double thanhTien = rs.getDouble("ThanhTien");

                    // Lấy đầy đủ thông tin ChiTietThuoc (bao gồm cả Thuoc)
                    ChiTietThuoc chiTietThuoc = chiTietThuocDAO.getChiTietThuoc(maChiTietThuoc);

                    // Lấy đầy đủ thông tin DonViTinh
                    DonViTinh donViTinh = donViTinhDAO.getDVTTheoMa(maDonViTinh);
                    if (donViTinh == null) {
                        donViTinh = new DonViTinh(maDonViTinh);
                    }

                    // Tạo HoaDon object
                    HoaDon hoaDon = new HoaDon(maHoaDon);

                    ChiTietHoaDon cthd = new ChiTietHoaDon(hoaDon, chiTietThuoc, soLuong, donViTinh, tinhTrang, thanhTien);
                    ds.add(cthd);
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy chi tiết hóa đơn: " + e.getMessage());
            e.printStackTrace();
        }

        return ds;
    }

    /**
     * Lấy tất cả chi tiết hóa đơn theo mã hóa đơn (phương thức cũ)
     * @param maHD mã hóa đơn
     * @return danh sách chi tiết hóa đơn
     */
    public ArrayList<ChiTietHoaDon> getAllChiTietHoaDonTheoMaHD(String maHD) {
        ArrayList<ChiTietHoaDon> ds = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE MaHD = ?";
        ChiTietThuoc_DAO chiTietThuocDAO = new ChiTietThuoc_DAO();

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maHD);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String maHoaDon = rs.getString("MaHD");
                int maChiTietThuoc = rs.getInt("MaCTT");
                int soLuong = rs.getInt("SoLuong");
                int maDonViTinh = rs.getInt("MaDVT");
                String tinhTrang = rs.getString("TinhTrang");
                double thanhTien = rs.getDouble("ThanhTien");

                // Lấy đầy đủ thông tin ChiTietThuoc (bao gồm cả Thuoc)
                ChiTietThuoc chiTietThuoc = chiTietThuocDAO.getChiTietThuoc(maChiTietThuoc);
                ChiTietHoaDon cthd = new ChiTietHoaDon(new HoaDon(maHD), chiTietThuoc, soLuong, new DonViTinh(maDonViTinh), tinhTrang, thanhTien);
                ds.add(cthd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    /**
     * Lấy tất cả chi tiết hóa đơn theo mã hóa đơn còn trạng thái bán
     * @param maHD mã hóa đơn
     * @return danh sách chi tiết hóa đơn
     */
    public ArrayList<ChiTietHoaDon> getAllChiTietHoaDonTheoMaHDConBan(String maHD){
        ArrayList<ChiTietHoaDon> ds = new ArrayList<ChiTietHoaDon>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE MaHD = ? AND TinhTrang = 'Bán'";
        try{
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maHD);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                String maHoaDon = rs.getString("MaHD");
                int maChiTietThuoc = rs.getInt("MaCTT");
                int soLuong = rs.getInt("SoLuong");
                int maDonViTinh = rs.getInt("MaDVT");
                String tinhTrang = rs.getString("TinhTrang");
                double thanhTien = rs.getDouble("ThanhTien");
                ChiTietHoaDon cthd = new ChiTietHoaDon(new HoaDon(maHD), new ChiTietThuoc(), soLuong, new DonViTinh(maDonViTinh), tinhTrang, thanhTien);
                ds.add(cthd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    /**
     * Xóa mềm chi tiết hóa đơn
     * @param maHD mã hóa đơn
     * @param maCTT mã chi tiết thuốc
     * @param tinhTrang trạng thái mới
     * @return true nếu xóa thành công, false nếu xóa thất bại
     */
    public boolean xoaMemChiTietHoaDon(String maHD, int maCTT, String tinhTrang, int soLuong, double thanhTien){
        String sql = "UPDATE ChiTietHoaDon SET TinhTrang = ? WHERE MaHD = ? AND MaCTT = ? AND TinhTrang <> N'Trả Khi Đổi'";
        String checkSql = "SELECT SoLuong, ThanhTien FROM ChiTietHoaDon WHERE MaHD = ? AND MaCTT = ? AND TinhTrang = N'Trả Khi Đổi'";
        String updateSql = "UPDATE ChiTietHoaDon SET SoLuong = SoLuong + ?, ThanhTien = ThanhTien + ? WHERE MaHD = ? AND MaCTT = ? AND TinhTrang = N'Trả Khi Đổi'";
        int count = 0;
        try{
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            // Kiểm tra xem đã tồn tại chưa
            PreparedStatement checkStmt = con.prepareStatement(checkSql);
            checkStmt.setString(1, maHD);
            checkStmt.setInt(2, maCTT);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                count++;
            }

            if (count > 0) {
                // Đã tồn tại -> UPDATE cộng thêm số lượng và thành tiền
                PreparedStatement updateStmt = con.prepareStatement(updateSql);
                updateStmt.setInt(1, soLuong);
                updateStmt.setDouble(2, thanhTien);
                updateStmt.setString(3, maHD);
                updateStmt.setInt(4, maCTT);
                int rowsAffected = updateStmt.executeUpdate();
                return rowsAffected > 0;
            } else {
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setString(1, tinhTrang);
                statement.setString(2, maHD);
                statement.setInt(3, maCTT);
                int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkChiTietHoaDon(String maHD, int maCTT) {
        String sql = "SELECT COUNT(*) AS count FROM ChiTietHoaDon WHERE MaHD = ? AND MaCTT = ? AND TinhTrang = N'Trả Khi Đổi'";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maHD);
            statement.setInt(2, maCTT);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Thêm chi tiết hóa đơn
     * @param cthd chi tiết hóa đơn
     * @return true nếu thêm thành công, false nếu thêm thất bại
     */
    public boolean themChiTietHoaDon(ChiTietHoaDon cthd){
        // Kiểm tra xem đã tồn tại chi tiết hóa đơn với MaHD và MaCTT này chưa
        String checkSql = "SELECT SoLuong, ThanhTien FROM ChiTietHoaDon WHERE MaHD = ? AND MaCTT = ?";
        String updateSql = "UPDATE ChiTietHoaDon SET SoLuong = SoLuong + ?, ThanhTien = ThanhTien + ? WHERE MaHD = ? AND MaCTT = ?";
        String insertSql = "INSERT INTO ChiTietHoaDon (MaHD, MaCTT, SoLuong, MaDVT, TinhTrang, ThanhTien) VALUES (?, ?, ?, ?, ?, ?)";

        try{
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            // Kiểm tra xem đã tồn tại chưa
            PreparedStatement checkStmt = con.prepareStatement(checkSql);
            checkStmt.setString(1, cthd.getMaHD().getMaHD());
            checkStmt.setInt(2, cthd.getMaCTT().getMaCTT());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Đã tồn tại -> UPDATE cộng thêm số lượng và thành tiền
                PreparedStatement updateStmt = con.prepareStatement(updateSql);
                updateStmt.setInt(1, cthd.getSoLuong());
                updateStmt.setDouble(2, cthd.getThanhTien());
                updateStmt.setString(3, cthd.getMaHD().getMaHD());
                updateStmt.setInt(4, cthd.getMaCTT().getMaCTT());
                int rowsAffected = updateStmt.executeUpdate();
                return rowsAffected > 0;
            } else {
                // Chưa tồn tại -> INSERT mới
                PreparedStatement insertStmt = con.prepareStatement(insertSql);
                insertStmt.setString(1, cthd.getMaHD().getMaHD());
                insertStmt.setInt(2, cthd.getMaCTT().getMaCTT());
                insertStmt.setInt(3, cthd.getSoLuong());
                insertStmt.setInt(4, cthd.getMaDVT().getMaDVT());
                insertStmt.setString(5, cthd.getTinhTrang());
                insertStmt.setDouble(6, cthd.getThanhTien());
                int rowsAffected = insertStmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Thêm chi tiết hóa đơn với trạng thái "Trả Khi Đổi"
     * Giới hạn chỉ được có tối đa 2 dòng với trạng thái này cho mỗi MaHD và MaCTT
     * @param cthd chi tiết hóa đơn
     * @return true nếu thêm thành công, false nếu thêm thất bại
     */

    public boolean themChiTietHoaDon1(ChiTietHoaDon cthd) {
        String insertSql = "INSERT INTO ChiTietHoaDon (MaHD, MaCTT, SoLuong, MaDVT, TinhTrang, ThanhTien) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            PreparedStatement insertStmt = con.prepareStatement(insertSql);
            insertStmt.setString(1, cthd.getMaHD().getMaHD());
            insertStmt.setInt(2, cthd.getMaCTT().getMaCTT());
            insertStmt.setInt(3, cthd.getSoLuong());
            insertStmt.setInt(4, cthd.getMaDVT().getMaDVT());
            insertStmt.setString(5, cthd.getTinhTrang());
            insertStmt.setDouble(6, cthd.getThanhTien());

            int rowsAffected = insertStmt.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}