/*
 * @ (#) DangDieuChe_DAO.java   1.0 10/1/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dao;

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.DangDieuChe;
import com.antam.app.entity.Ke;

import java.sql.*;
import java.util.ArrayList;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/1/2025
 * version: 1.0
 */
public class DangDieuChe_DAO {
    /**
     * Lấy tất cả dạng điều chế từ database
     * @return danh sách dạng điều chế
     */
    public ArrayList<DangDieuChe> getAllDDC() {
        ArrayList<DangDieuChe> listDDC = new ArrayList<>();
        String sql = "SELECT * FROM DangDieuChe";
        try{
            Connection con = ConnectDB.getConnection();
            Statement state = con.createStatement();
            ResultSet rs = state.executeQuery(sql);
            while (rs.next()) {
                int maDDC = rs.getInt("MaDDC");
                String tenDDC = rs.getString("TenDDC");
                DangDieuChe ke = new DangDieuChe(maDDC, tenDDC);
                listDDC.add(ke);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listDDC;
    }
    /**
     * Lấy dạng điều chế theo tên
     * @param name tên dạng điều chế
     * @return dạng điều chế
     */
    public DangDieuChe getDDCTheoName(String name) {
        DangDieuChe ddc = null;
        String sql = "SELECT * FROM DangDieuChe WHERE TenDDC =  ?";
        try{
            Connection con = ConnectDB.getConnection();
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1, name);
            ResultSet rs = state.executeQuery();
            if (rs.next()) {
                int maDDC = rs.getInt("MaDDC");
                String tenDDC = rs.getString("TenDDC");
                ddc = new DangDieuChe(maDDC, tenDDC);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ddc;
    }
}
