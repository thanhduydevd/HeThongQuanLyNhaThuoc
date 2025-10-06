/*
 * @ (#) HoaDon_DAO.java   1.0 10/3/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dao;

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.HoaDon;
import com.antam.app.entity.KhachHang;
import com.antam.app.entity.KhuyenMai;
import com.antam.app.entity.NhanVien;

import java.sql.Connection;
import java.sql.PreparedStatement;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/3/2025
 * version: 1.0
 */
public class HoaDon_DAO {
    public HoaDon getHoaDonTheoMa(String maHD){
        HoaDon hd = new HoaDon(maHD);
        String sql = "SELECT * FROM HoaDon WHERE MaHD = ?";
        Connection con = ConnectDB.getConnection();
        try{
            java.sql.PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maHD);
            java.sql.ResultSet rs = statement.executeQuery();
            if(rs.next()){
                hd.setMaKH(new KhachHang(rs.getString("MaKH")));
                hd.setMaNV(new NhanVien(rs.getString("MaNV")));
                hd.setNgayTao(rs.getDate("NgayTao").toLocalDate());
                hd.setTongTien(rs.getDouble("TongTien"));
                hd.setMaKM(rs.getString("MaKM") != null ? new KhuyenMai(rs.getString("MaKM")) : null);
                hd.setDeleteAt(rs.getBoolean("DeleteAt"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hd;
    }
    public boolean CapNhatTongTienHoaDon(String maHD, double tongTien){
        String sql = "UPDATE HoaDon SET TongTien = ? WHERE MaHD = ?";
        Connection con = ConnectDB.getConnection();
        try{
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setDouble(1, tongTien);
            statement.setString(2, maHD);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean xoaMemHoaDon(String maHD){
        String sql = "UPDATE HoaDon SET DeleteAt = 1 WHERE MaHD = ?";
        Connection con = ConnectDB.getConnection();
        try{
            java.sql.PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(2, maHD);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
