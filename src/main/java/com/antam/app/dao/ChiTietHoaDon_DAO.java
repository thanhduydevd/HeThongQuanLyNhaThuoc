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
import java.util.ArrayList;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/3/2025
 * version: 1.0
 */
public class ChiTietHoaDon_DAO {
    /**
     * Lấy tất cả chi tiết hóa đơn theo mã hóa đơn
     * @param maHD mã hóa đơn
     * @return danh sách chi tiết hóa đơn
     */
    public ArrayList<ChiTietHoaDon> getAllChiTietHoaDonTheoMaHD(String maHD){
        ArrayList<ChiTietHoaDon> ds = new ArrayList<ChiTietHoaDon>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE MaHD = ?";
        Connection con = ConnectDB.getConnection();
        ChiTietThuoc_DAO chiTietThuocDAO = new ChiTietThuoc_DAO();
        try{
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
        Connection con = ConnectDB.getConnection();
        try{
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
    public boolean xoaMemChiTietHoaDon(String maHD, int maCTT, String tinhTrang){
        String sql = "UPDATE ChiTietHoaDon SET TinhTrang = ? WHERE MaHD = ? AND MaCTT = ?";
        Connection con = ConnectDB.getConnection();
        try{ PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, tinhTrang);
            statement.setString(2, maHD);
            statement.setInt(3, maCTT);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
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
        String sql = "INSERT INTO ChiTietHoaDon (MaHD, MaCTT, SoLuong, MaDVT, TinhTrang, ThanhTien) VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = ConnectDB.getConnection();
        try{
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, cthd.getMaHD().getMaHD());
            statement.setInt(2, cthd.getMaCTT().getMaCTT());
            statement.setInt(3, cthd.getSoLuong());
            statement.setInt(4, cthd.getMaDVT().getMaDVT());
            statement.setString(5, cthd.getTinhTrang());
            statement.setDouble(6, cthd.getThanhTien());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}