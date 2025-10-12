/*
 * @ (#) KhachHang_DAO.java   1.0 1/10/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.antam.app.dao;

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.KhachHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * @description
 * @author: Tran Tuan Hung
 * @date: 1/10/25
 * @version: 1.0
 */
public class KhachHang_DAO {
    public static ArrayList<KhachHang> loadBanFromDB(){
        ArrayList<KhachHang> list = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "select * from KhachHang";
            PreparedStatement state = con.prepareStatement(sql);
            ResultSet re = state.executeQuery();

            while (re.next()) {
                String maKH = re.getString("MaKH");
                String tenKH = re.getString("TenKH");
                String soDienThoai = re.getString("SoDienThoai");
                boolean deleteAt = re.getBoolean("DeleteAt");
                list.add(new KhachHang(maKH, tenKH, soDienThoai, deleteAt));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /**
     * Thêm khách hàng mới vào CSDL
     * @param kh KhachHang
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean insertKhachHang(KhachHang kh) {
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "INSERT INTO KhachHang (MaKH, TenKH, SoDienThoai, DeleteAt) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getSoDienThoai());
            ps.setBoolean(4, kh.isDeleteAt());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public KhachHang getKhachHangTheoMa(String maKH){
        KhachHang kh = new KhachHang(maKH);
        String sql = "SELECT * FROM KhachHang WHERE MaKH = ?";
        Connection con = ConnectDB.getConnection();
        try{
            java.sql.PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maKH);
            java.sql.ResultSet rs = statement.executeQuery();
            if(rs.next()){
                kh.setTenKH(rs.getString("TenKH"));
                kh.setSoDienThoai(rs.getString("SoDienThoai"));
                kh.setDeleteAt(rs.getBoolean("DeleteAt"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kh;
    }

    /**
     * Trả về danh sách tất cả khách hàng trong CSDL
     */
    public ArrayList<KhachHang> getAllKhachHang() {
        return loadBanFromDB();
    }
}
