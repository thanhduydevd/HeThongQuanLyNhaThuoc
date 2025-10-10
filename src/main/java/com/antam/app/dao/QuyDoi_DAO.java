/*
 * @ (#) QuyDoi_DAO.java   1.0 10/2/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dao;

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.DonViTinh;
import com.antam.app.entity.QuyDoi;
import com.antam.app.entity.Thuoc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/2/2025
 * version: 1.0
 */
public class QuyDoi_DAO {
    /**
     * Lấy tất cả quy đổi đơn vị từ database
     * @return bản đồ quy đổi đơn vị
     */
    public Map<String, List<QuyDoi>> getAllQuyDoi() {
        Map<String, List<QuyDoi>> tatCaQuyDoi = new HashMap<>();
        String sql = "SELECT * FROM QuyDoiDonVi";
        try {
            Connection con = ConnectDB.getConnection();
            Statement state = con.createStatement();
            ResultSet rs = state.executeQuery(sql);
            while (rs.next()) {
                String maThuoc = rs.getString("MaThuoc");
                int maDVTCha = rs.getInt("MaDVTCha");
                int maDVTCon = rs.getInt("MaDVTCon");
                int tiLe = rs.getInt("TyLe");
                List<QuyDoi> dsQuyDoi = tatCaQuyDoi.getOrDefault(maThuoc, new ArrayList<>());
                dsQuyDoi.add(new QuyDoi(new Thuoc(maThuoc), new DonViTinh(maDVTCha), new  DonViTinh(maDVTCon), tiLe));
                tatCaQuyDoi.put(maThuoc, dsQuyDoi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tatCaQuyDoi;
    }

    /**
     * Thêm quy đổi đơn vị vào database
     * @param qd Quy đổi đơn vị
     * @return true nếu thêm thành công, false nếu thêm thất bại
     */
    public boolean themQuyDoi(QuyDoi qd) {
        String sql = "INSERT INTO QuyDoiDonVi (MaThuoc, MaDVTCha, MaDVTCon, TyLe) VALUES (?, ?, ?, ?)";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1, qd.getMaThuoc().getMaThuoc());
            state.setInt(2, qd.getMaDVTCha().getMaDVT());
            state.setInt(3, qd.getMaDVTCon().getMaDVT());
            state.setInt(4, qd.getTiLeQuyDoi());
            int rowsInserted = state.executeUpdate();
            return rowsInserted > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Cập nhật quy đổi đơn vị trong database
     * @param qd Quy đổi đơn vị
     * @return true nếu cập nhật thành công, false nếu cập nhật thất bại
     */
    public boolean capNhatQuyDoi(QuyDoi qd) {
        String sql = "UPDATE QuyDoiDonVi SET TyLe = ? WHERE MaThuoc = ? AND MaDVTCha = ? AND MaDVTCon = ?";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(2, qd.getMaThuoc().getMaThuoc());
            state.setInt(3, qd.getMaDVTCha().getMaDVT());
            state.setInt(4, qd.getMaDVTCon().getMaDVT());
            state.setInt(1, qd.getTiLeQuyDoi());
            int rowsInserted = state.executeUpdate();
            return rowsInserted > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Lấy quy đổi đơn vị theo mã thuốc từ database
     * @param id mã thuốc
     * @return danh sách quy đổi đơn vị
     */
    public ArrayList<QuyDoi> getQuyDoiTheoMa(String id) {
        ArrayList<QuyDoi> dsQuyDoi = new ArrayList<>();
        String sql = "SELECT * FROM QuyDoiDonVi WHERE MaThuoc = ?";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1, id);
            ResultSet rs = state.executeQuery();
            while (rs.next()) {
                String maThuoc = rs.getString("MaThuoc");
                int maDVTCha = rs.getInt("maDVTCha");
                int maDVTCon = rs.getInt("maDVTCon");
                int tiLe = rs.getInt("TyLe");

                QuyDoi qd = new QuyDoi(
                        new Thuoc(maThuoc),
                        new DonViTinh(maDVTCha),
                        new DonViTinh(maDVTCon),
                        tiLe
                );
                dsQuyDoi.add(qd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsQuyDoi;
    }

    /**
     * Xóa quy đổi đơn vị theo mã thuốc từ database
     * @param ma mã thuốc
     * @return true nếu xóa thành công, false nếu xóa thất bại
     */
    public boolean xoaQuyDoiTheoMaThuoc(String ma) {
        String sql = "DELETE FROM QuyDoiDonVi WHERE MaThuoc = ?";
        Connection con = ConnectDB.getConnection();
        PreparedStatement statement = null;
        try{
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1, ma);
            int rowsDeleted = state.executeUpdate();
            return rowsDeleted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



}
