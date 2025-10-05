/*
 * @ (#) Thuoc_DAO.java   1.0 9/24/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dao;

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.DangDieuChe;
import com.antam.app.entity.DonViTinh;
import com.antam.app.entity.Ke;
import com.antam.app.entity.Thuoc;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 9/24/2025
 * version: 1.0
 */
public class Thuoc_DAO {
    public ArrayList<Thuoc> getAllThuoc() {
        ArrayList<Thuoc> listThuoc = new ArrayList<>();
        String sql = """
            SELECT t.MaThuoc, t.TenThuoc, t.HanSuDung, t.NgaySanXuat, t.TonKho,
                   t.HamLuong, t.GiaBan, t.GiaGoc, t.Thue, t.MaDVTCoso, t.deleteAt,
                   ddc.MaDDC, ddc.TenDDC,
                   k.MaKe, k.TenKe, k.LoaiKe
            FROM Thuoc t
            JOIN DangDieuChe ddc ON t.DangDieuChe = ddc.MaDDC
            JOIN KeThuoc k ON t.MaKe = k.MaKe
            WHERE t.deleteAt = 0
        """;

        try (Connection con = ConnectDB.getConnection();
             Statement state = con.createStatement();
             ResultSet rs = state.executeQuery(sql)) {

            while (rs.next()) {
                String maThuoc = rs.getString("MaThuoc");
                String tenThuoc = rs.getString("TenThuoc");
                LocalDate hanSuDung = rs.getDate("HanSuDung").toLocalDate();
                LocalDate ngaySanXuat = rs.getDate("NgaySanXuat").toLocalDate();
                int tonKho = rs.getInt("TonKho");
                String hamLuong = rs.getString("HamLuong");
                double giaBan = rs.getDouble("GiaBan");
                double giaGoc = rs.getDouble("GiaGoc");
                float thue = rs.getFloat("Thue");
                int maDonViTinhCoSo = rs.getInt("MaDVTCoso");
                boolean deleteAt = rs.getBoolean("deleteAt");

                // Tạo object DDC và Ke với đủ thông tin
                DangDieuChe ddc = new DangDieuChe(rs.getInt("MaDDC"), rs.getString("TenDDC"));
                Ke ke = new Ke(rs.getString("MaKe"), rs.getString("TenKe"), rs.getString("LoaiKe"), false);
                DonViTinh dvt = new DonViTinh(maDonViTinhCoSo);

                Thuoc thuoc = new Thuoc(maThuoc, tenThuoc, hanSuDung, ngaySanXuat,
                        tonKho, hamLuong, giaBan, giaGoc, thue,
                        deleteAt, ddc, dvt, ke);
                listThuoc.add(thuoc);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listThuoc;
    }

    public boolean themThuoc(Thuoc t) {
        String sql = "INSERT INTO Thuoc (MaThuoc, TenThuoc, HanSuDung, NgaySanXuat, TonKho, HamLuong, GiaBan, GiaGoc, Thue, DangDieuChe, MaDVTCoso, MaKe, deleteAt) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,0)";
        try {
            Connection con = ConnectDB.getConnection();
            var ps = con.prepareStatement(sql);
            ps.setString(1, t.getMaThuoc());
            ps.setString(2, t.getTenThuoc());
            ps.setDate(3, java.sql.Date.valueOf(t.getHanSuDung()));
            ps.setDate(4, java.sql.Date.valueOf(t.getNgaySanXuat()));
            ps.setInt(5, t.getTonKho());
            ps.setString(6, t.getHamLuong());
            ps.setDouble(7, t.getGiaBan());
            ps.setDouble(8, t.getGiaGoc());
            ps.setFloat(9, t.getThue());
            ps.setInt(10, t.getDangDieuChe().getMaDDC());
            ps.setInt(11, t.getMaDVTCoSo().getMaDVT());
            ps.setString(12, t.getMaKe().getMaKe());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean capNhatThuoc(Thuoc t) {
        String sql = "UPDATE Thuoc SET TenThuoc = ?, HanSuDung = ?, NgaySanXuat = ?, TonKho = ?, HamLuong = ?, GiaBan = ?, GiaGoc = ?, Thue = ?, DangDieuChe = ?, MaDVTCoso = ?, MaKe = ?, DeleteAt = ? WHERE MaThuoc = ?";
        try {
            Connection con = ConnectDB.getConnection();
            var ps = con.prepareStatement(sql);
            ps.setString(1, t.getTenThuoc());
            ps.setDate(2, java.sql.Date.valueOf(t.getHanSuDung()));
            ps.setDate(3, java.sql.Date.valueOf(t.getNgaySanXuat()));
            ps.setInt(4, t.getTonKho());
            ps.setString(5, t.getHamLuong());
            ps.setDouble(6, t.getGiaBan());
            ps.setDouble(7, t.getGiaGoc());
            ps.setFloat(8, t.getThue());
            ps.setInt(9, t.getDangDieuChe().getMaDDC());
            ps.setInt(10, t.getMaDVTCoSo().getMaDVT());
            ps.setString(11, t.getMaKe().getMaKe());
            ps.setBoolean(12, t.isDeleteAt());
            ps.setString(13, t.getMaThuoc());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean xoaThuocTheoMa(String ma) {
        String sql = "UPDATE Thuoc SET DeleteAt = 1 WHERE MaThuoc = ?";
        Connection con = ConnectDB.getConnection();
        PreparedStatement statement = null;
        int n = 0;
        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, ma);
            n = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return n > 0;
    }

}
