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


}
