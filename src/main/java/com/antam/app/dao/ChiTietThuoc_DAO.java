/*
 * @ (#) ChiTietThuoc_DAO.java   1.0 10/6/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dao;

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.ChiTietThuoc;
import com.antam.app.entity.PhieuNhap;
import com.antam.app.entity.Thuoc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/6/2025
 * version: 1.0
 */
public class ChiTietThuoc_DAO {
    /**
     * Lấy tất cả chi tiết thuốc từ database
     * @return danh sách chi tiết thuốc
     */
    public ArrayList<ChiTietThuoc> getAllChiTietThuoc() {
        ArrayList<ChiTietThuoc> listChiTietThuoc = new ArrayList<>();
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT * FROM ChiTietThuoc";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int maCTT = rs.getInt("MaCTT");
                String maPN = rs.getString("MaPN");
                String maThuoc = rs.getString("MaThuoc");
                int soLuong = rs.getInt("TonKho");
                java.sql.Date hanSuDung = rs.getDate("HanSuDung");
                java.sql.Date ngaySanXuat = rs.getDate("NgaySanXuat");

                ChiTietThuoc chiTietThuoc = new ChiTietThuoc(
                        maCTT,
                        new PhieuNhap(maPN),
                        new Thuoc(maThuoc),
                        soLuong,
                        hanSuDung.toLocalDate(),
                        ngaySanXuat.toLocalDate()
                );
                listChiTietThuoc.add(chiTietThuoc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listChiTietThuoc;
    }

    /**
     * Lấy tất cả chi tiết thuốc theo mã thuốc từ database
     * @param ma mã thuốc
     * @return danh sách chi tiết thuốc
     */
    public ArrayList<ChiTietThuoc> getAllCHiTietThuocTheoMaThuoc(String ma) {
        ArrayList<ChiTietThuoc> listChiTietThuoc = new ArrayList<>();
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT * FROM ChiTietThuoc WHERE MaThuoc = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, ma);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int maCTT = rs.getInt("MaCTT");
                String maPN = rs.getString("MaPN");
                String maThuoc = rs.getString("MaThuoc");
                int soLuong = rs.getInt("TonKho");
                java.sql.Date hanSuDung = rs.getDate("HanSuDung");
                java.sql.Date ngaySanXuat = rs.getDate("NgaySanXuat");

                ChiTietThuoc chiTietThuoc = new ChiTietThuoc(
                        maCTT,
                        new PhieuNhap(maPN),
                        new Thuoc(maThuoc),
                        soLuong,
                        hanSuDung.toLocalDate(),
                        ngaySanXuat.toLocalDate()
                );
                listChiTietThuoc.add(chiTietThuoc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listChiTietThuoc;
    }

    /**
     * Lấy chi tiết thuốc theo mã từ database
     * @param ma mã chi tiết thuốc
     * @return chi tiết thuốc
     */
    public ChiTietThuoc getChiTietThuoc(int ma) {
        ChiTietThuoc chiTietThuoc = new ChiTietThuoc();
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT * FROM ChiTietThuoc WHERE MaCTT = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, ma);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int maCTT = rs.getInt("MaCTT");
                String maPN = rs.getString("MaPN");
                String maThuoc = rs.getString("MaThuoc");
                int soLuong = rs.getInt("TonKho");
                java.sql.Date hanSuDung = rs.getDate("HanSuDung");
                java.sql.Date ngaySanXuat = rs.getDate("NgaySanXuat");

                 chiTietThuoc = new ChiTietThuoc(
                        maCTT,
                        new PhieuNhap(maPN),
                        new Thuoc(maThuoc),
                        soLuong,
                        hanSuDung.toLocalDate(),
                        ngaySanXuat.toLocalDate()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chiTietThuoc;
    }

    /**
     * Cập nhật số lượng chi tiết thuốc trong database
     * @param maCTT mã chi tiết thuốc
     * @param soLuong số lượng cần cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean CapNhatSoLuongChiTietThuoc(int maCTT, int soLuong) {
        String sql = "UPDATE ChiTietThuoc SET TonKho = TonKho + ? WHERE MaCTT = ?";
        Connection con = ConnectDB.getConnection();
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, soLuong);
            statement.setInt(2, maCTT);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<ChiTietThuoc> getChiTietThuocHanSuDungGiamDan(String maThuoc) {
        ArrayList<ChiTietThuoc> listChiTietThuoc = new ArrayList<>();
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT * FROM ChiTietThuoc WHERE TonKho > 0 AND MaThuoc = ? ORDER BY HanSuDung ASC";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maThuoc);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int maCTT = rs.getInt("MaCTT");
                String maPN = rs.getString("MaPN");
                String maThuocDB = rs.getString("MaThuoc");
                int soLuong = rs.getInt("TonKho");
                java.sql.Date hanSuDung = rs.getDate("HanSuDung");
                java.sql.Date ngaySanXuat = rs.getDate("NgaySanXuat");

                ChiTietThuoc chiTietThuoc = new ChiTietThuoc(
                        maCTT,
                        new PhieuNhap(maPN),
                        new Thuoc(maThuocDB),
                        soLuong,
                        hanSuDung.toLocalDate(),
                        ngaySanXuat.toLocalDate()
                );
                listChiTietThuoc.add(chiTietThuoc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listChiTietThuoc;
    }

    /**
     * Lấy tổng số lượng tồn kho (TonKho) của một mã thuốc
     * @param maThuoc mã thuốc
     * @return tổng số lượng tồn kho
     */
    public int getTongTonKhoTheoMaThuoc(String maThuoc) {
        int tong = 0;
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT SUM(TonKho) AS TongTonKho FROM ChiTietThuoc WHERE MaThuoc = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maThuoc);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tong = rs.getInt("TongTonKho");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tong;
    }
}
