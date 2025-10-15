/*
 * @ (#) LoaiKhuyenMai_DAO.java   1.0 10/15/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dao;

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.LoaiKhuyenMai;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/15/2025
 * version: 1.0
 */
public class LoaiKhuyenMai_DAO {
    public ArrayList<LoaiKhuyenMai> getAllLoaiKhuyenMai() {
        ArrayList<LoaiKhuyenMai> list = new ArrayList<>();
        String sql = "SELECT * FROM LoaiKhuyenMai";
        try (Connection con = ConnectDB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                int maLKM = rs.getInt("MaLKM");
                String tenLKM = rs.getString("TenLKM");
                LoaiKhuyenMai lkm = new LoaiKhuyenMai(maLKM, tenLKM);
                list.add(lkm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
