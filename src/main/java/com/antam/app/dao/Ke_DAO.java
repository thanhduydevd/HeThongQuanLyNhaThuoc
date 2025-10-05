/*
 * @ (#) Ke_DAO.java   1.0 10/1/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dao;

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.Ke;

import java.sql.*;
import java.util.ArrayList;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/1/2025
 * version: 1.0
 */
public class Ke_DAO {
    public ArrayList<Ke> getAllKe() {
        ArrayList<Ke> listKe = new ArrayList<>();
        String sql = "SELECT * FROM KeThuoc WHERE DeleteAt = 0";
        try{
            Connection con = ConnectDB.getConnection();
            Statement state = con.createStatement();
            ResultSet rs = state.executeQuery(sql);
            while (rs.next()) {
                String maKe = rs.getString("MaKe");
                String tenKe = rs.getString("TenKe");
                String loaiKe = rs.getString("LoaiKe");
                boolean deleteAt = rs.getBoolean("DeleteAt");
                Ke ke = new Ke(maKe, tenKe, loaiKe, deleteAt);
                listKe.add(ke);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listKe;
    }

    public Ke getKeTheoMa(String ma) {
        Ke ke = null;
        String sql = "SELECT * FROM KeThuoc WHERE MaKe = ? AND DeleteAt = 0";
        try{
            Connection con = ConnectDB.getConnection();
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1, ma);
            ResultSet rs = state.executeQuery();
            if (rs.next()) {
                String maKe = rs.getString("MaKe");
                String tenKe = rs.getString("TenKe");
                String loaiKe = rs.getString("LoaiKe");
                boolean deleteAt = rs.getBoolean("DeleteAt");
                ke = new Ke(maKe, tenKe, loaiKe, deleteAt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ke;
    }
    public Ke getKeTheoName(String name) {
        Ke ke = null;
        String sql = "SELECT * FROM KeThuoc WHERE TenKe = ? AND DeleteAt = 0";
        try{
            Connection con = ConnectDB.getConnection();
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1, name);
            ResultSet rs = state.executeQuery();
            if (rs.next()) {
                String maKe = rs.getString("MaKe");
                String tenKe = rs.getString("TenKe");
                String loaiKe = rs.getString("LoaiKe");
                boolean deleteAt = rs.getBoolean("DeleteAt");
                ke = new Ke(maKe, tenKe, loaiKe, deleteAt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ke;
    }
}
