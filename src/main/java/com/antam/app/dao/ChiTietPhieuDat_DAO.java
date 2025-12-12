package com.antam.app.dao;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 05/12/2025
 * @version: 1.0
 */

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.ChiTietPhieuDatThuoc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChiTietPhieuDat_DAO {

    public static void themChiTietPhieuDatVaoDBS(ChiTietPhieuDatThuoc ct) {
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sql = "INSERT INTO ChiTietPhieuDatThuoc (MaPDT, MaThuoc, SoLuong, MaDVT, TinhTrang, ThanhTien) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ct.getMaPhieu().getMaPhieu());
            ps.setString(2, ct.getSoDangKy().getMaThuoc());
            ps.setInt   (3, ct.getSoLuong());
            ps.setInt   (4, ct.getDonViTinh().getMaDVT());
            ps.setString(5, "Đặt");
            ps.setDouble(6, ct.getThanhTien());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}