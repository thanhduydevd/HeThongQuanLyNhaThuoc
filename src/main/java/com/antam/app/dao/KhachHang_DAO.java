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
}


