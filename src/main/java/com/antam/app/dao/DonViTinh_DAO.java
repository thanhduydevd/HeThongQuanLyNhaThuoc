/*
 * @ (#) DonViTinh_DAO.java   1.0 10/1/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dao;

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.DonViTinh;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/1/2025
 * version: 1.0
 */
public class DonViTinh_DAO {

    /**
     * Duy - Lấy tất cả đơn vị tính từ database
     * @return danh sách đơn vị tính
     */
    public ArrayList<DonViTinh> getTatCaDonViTinh() {
        ArrayList<DonViTinh> list = new ArrayList<>();
        try (Connection con = ConnectDB.getInstance().connect();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM DonViTinh")) {
            while (rs.next()) {
                int maDVT = rs.getInt("MaDVT");
                String tenDVT = rs.getString("TenDVT");
                DonViTinh dvt = new DonViTinh(maDVT, tenDVT);
                list.add(dvt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /**
     * Lấy tất cả đơn vị tính từ database
     * @return danh sách đơn vị tính
     */
    public ArrayList<DonViTinh> getAllDonViTinh() {
        ArrayList<DonViTinh> listDVT = new ArrayList<>();
        String sql = "SELECT * FROM DonViTinh";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            Statement statement = con.createStatement();
            var rs = statement.executeQuery(sql);
            while (rs.next()) {
                int maDVT = rs.getInt("MaDVT");
                String tenDVT = rs.getString("TenDVT");
                DonViTinh dvt = new DonViTinh(maDVT, tenDVT);
                listDVT.add(dvt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listDVT;
    }

    /**
     * Lấy đơn vị tính theo tên
     * @param ten tên đơn vị tính
     * @return đơn vị tính
     */
    public DonViTinh getDVTTheoTen(String ten) {
        DonViTinh dvt = null;
        String sql = "SELECT * FROM DonViTinh WHERE TenDVT = ?";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            var state = con.prepareStatement(sql);
            state.setString(1, ten);
            var rs = state.executeQuery();
            if (rs.next()) {
                int maDVT = rs.getInt("MaDVT");
                String tenDVT = rs.getString("TenDVT");
                dvt = new DonViTinh(maDVT, tenDVT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dvt;
    }
    /**
     * Lấy đơn vị tính theo mã
     * @param ma mã đơn vị tính
     * @return đơn vị tính
     */
    public DonViTinh getDVTTheoMa(int ma) {
        DonViTinh dvt = null;
        String sql = "SELECT * FROM DonViTinh WHERE MaDVT = ?";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            var state = con.prepareStatement(sql);
            state.setInt(1, ma);
            var rs = state.executeQuery();
            if (rs.next()) {
                int maDVT = rs.getInt("MaDVT");
                String tenDVT = rs.getString("TenDVT");
                dvt = new DonViTinh(maDVT, tenDVT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dvt;
    }
}
