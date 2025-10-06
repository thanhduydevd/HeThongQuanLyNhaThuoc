/*
 * @ (#) ChiTietHoaDon_DAO.java   1.0 10/3/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dao;

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.ChiTietHoaDon;
import com.antam.app.entity.DonViTinh;
import com.antam.app.entity.HoaDon;
import com.antam.app.entity.Thuoc;

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
    public ArrayList<ChiTietHoaDon> getAllChiTietHoaDonTheoMaHD(String maHD){
        ArrayList<ChiTietHoaDon> ds = new ArrayList<ChiTietHoaDon>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE MaHD = ?";
        Connection con = ConnectDB.getConnection();
        try{
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maHD);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                String maHoaDon = rs.getString("MaHD");
                String maThuoc = rs.getString("MaThuoc");
                int soLuong = rs.getInt("SoLuong");
                int maDonViTinh = rs.getInt("MaDVT");
                String tinhTrang = rs.getString("TinhTrang");
                double thanhTien = rs.getDouble("ThanhTien");
                ChiTietHoaDon cthd = new ChiTietHoaDon(new HoaDon(maHD), new Thuoc(maThuoc), soLuong, new DonViTinh(maDonViTinh), tinhTrang);
                ds.add(cthd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

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
                String maThuoc = rs.getString("MaThuoc");
                int soLuong = rs.getInt("SoLuong");
                int maDonViTinh = rs.getInt("MaDVT");
                String tinhTrang = rs.getString("TinhTrang");
                double thanhTien = rs.getDouble("ThanhTien");
                ChiTietHoaDon cthd = new ChiTietHoaDon(new HoaDon(maHD), new Thuoc(maThuoc), soLuong, new DonViTinh(maDonViTinh), tinhTrang);
                ds.add(cthd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public boolean xoaMemChiTietHoaDon(String maHD, String maThuoc, String tinhTrang){
        String sql = "UPDATE ChiTietHoaDon SET TinhTrang = ? WHERE MaHD = ? AND MaThuoc = ?";
        Connection con = ConnectDB.getConnection();
        try{
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, tinhTrang);
            statement.setString(2, maHD);
            statement.setString(3, maThuoc);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean themChiTietHoaDon(ChiTietHoaDon cthd){
        String sql = "INSERT INTO ChiTietHoaDon (MaHD, MaThuoc, SoLuong, MaDVT, TinhTrang, ThanhTien) VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = ConnectDB.getConnection();
        try{
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, cthd.getMaHD().getMaHD());
            statement.setString(2, cthd.getMaThuoc().getMaThuoc());
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
