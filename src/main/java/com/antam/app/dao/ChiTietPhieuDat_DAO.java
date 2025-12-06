package com.antam.app.dao;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 05/12/2025
 * @version: 1.0
 */

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.ChiTietPhieuDatThuoc;

import java.sql.Connection;
import java.sql.SQLException;

public class ChiTietPhieuDat_DAO {

    public static void themChiTietPhieuDatVaoDBS(ChiTietPhieuDatThuoc chiTietPhieuDatThuoc) {
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql = "INSERT INTO ChiTietPhieuDatThuoc (maPhieu, soDangKy, soLuong, donViTinh, thanhTien) " +
                "VALUES (?, ?, ?, ?, ?)";
        Connection con = ConnectDB.getConnection();
        try {
            var ps = con.prepareStatement(sql);
            ps.setString(1, chiTietPhieuDatThuoc.getMaPhieu().getMaPhieu());
            ps.setString(2, chiTietPhieuDatThuoc.getSoDangKy().getMaThuoc());
            ps.setInt(3, chiTietPhieuDatThuoc.getSoLuong());
            ps.setString(4, chiTietPhieuDatThuoc.getDonViTinh().getTenDVT());
            ps.setDouble(5, chiTietPhieuDatThuoc.getThanhTien());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
