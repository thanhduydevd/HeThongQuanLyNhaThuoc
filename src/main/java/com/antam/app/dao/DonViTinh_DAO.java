/*
 * @ (#) DonViTinh_DAO.java   1.0 10/1/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dao;

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.DonViTinh;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/1/2025
 * version: 1.0
 */
public class DonViTinh_DAO {
    public ArrayList<DonViTinh> getAllDonViTinh() {
        ArrayList<DonViTinh> listDVT = new ArrayList<>();
        String sql = "SELECT * FROM DonViTinh";
        try {
            Connection con = ConnectDB.getConnection();
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

    public DonViTinh getDVTTheoTen(String ten) {
        DonViTinh dvt = null;
        String sql = "SELECT * FROM DonViTinh WHERE TenDVT = ?";
        try {
            Connection con = ConnectDB.getConnection();
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
    public DonViTinh getDVTTheoMa(int ma) {
        DonViTinh dvt = null;
        String sql = "SELECT * FROM DonViTinh WHERE MaDVT = ?";
        try {
            Connection con = ConnectDB.getConnection();
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
