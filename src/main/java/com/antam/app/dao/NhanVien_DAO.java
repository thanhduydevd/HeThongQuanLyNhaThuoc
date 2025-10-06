/*
 * @ (#) NhanVien_DAO.java   1.0 10/1/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dao;

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.Ke;
import com.antam.app.entity.NhanVien;

import java.sql.*;
import java.util.ArrayList;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/1/2025
 * version: 1.0
 */
public class NhanVien_DAO {
    public NhanVien getNhanVien(String id) {
        NhanVien nhanVien = null;
        String sql = "SELECT * FROM NhanVien WHERE TaiKhoan = ?";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1, id);
            ResultSet rs = state.executeQuery();
            if (rs.next()) {
                String maNV = rs.getString("MaNV");
                nhanVien = new NhanVien(maNV);
                nhanVien.setHoTen(rs.getString("HoTen"));
                nhanVien.setSoDienThoai(rs.getString("SoDienThoai"));
                nhanVien.setEmail(rs.getString("Email"));
                nhanVien.setDiaChi(rs.getString("DiaChi"));
                nhanVien.setLuongCoBan(rs.getDouble("LuongCoBan"));
                nhanVien.setTaiKhoan(rs.getString("TaiKhoan"));
                nhanVien.setMatKhau(rs.getString("MatKhau"));
                nhanVien.setQuanLy(rs.getBoolean("IsQuanLi"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nhanVien;
    }

    public ArrayList<NhanVien> getAllNhanVien() {
        ArrayList<NhanVien> ds = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";
        try {
            Connection con = ConnectDB.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String maNV = rs.getString("MaNV");
                NhanVien nv = new NhanVien(maNV);
                nv.setHoTen(rs.getString("HoTen"));
                nv.setSoDienThoai(rs.getString("SoDienThoai"));
                nv.setEmail(rs.getString("Email"));
                nv.setDiaChi(rs.getString("DiaChi"));
                nv.setLuongCoBan(rs.getDouble("LuongCoBan"));
                nv.setTaiKhoan(rs.getString("TaiKhoan"));
                nv.setMatKhau(rs.getString("MatKhau"));
                nv.setQuanLy(rs.getBoolean("IsQuanLi"));
                ds.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
}
