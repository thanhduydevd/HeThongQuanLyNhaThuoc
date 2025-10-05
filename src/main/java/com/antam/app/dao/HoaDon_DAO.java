/*
 * @ (#) HoaDon_DAO.java   1.0 1/10/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.antam.app.dao;

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.HoaDon;
import com.antam.app.entity.Ke;
import com.antam.app.entity.NhanVien;
import com.antam.app.entity.KhachHang;
import com.antam.app.entity.KhuyenMai;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * @description
 * @author: Tran Tuan Hung
 * @date: 1/10/25
 * @version: 1.0
 */
public class HoaDon_DAO {
    public ArrayList<HoaDon> getAllHoaDon() {
        // Đảm bảo đã kết nối DB
        if (ConnectDB.getConnection() == null) {
            try {
                ConnectDB.getInstance().connect();
            } catch (Exception e) {
                throw new RuntimeException("Không thể kết nối database", e);
            }
        }
        ArrayList<HoaDon> dsHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE DeleteAt = 0";
        try {
            Connection con = ConnectDB.getConnection();
            Statement state = con.createStatement();
            ResultSet rs = state.executeQuery(sql);
            while (rs.next()) {
                String maHD = rs.getString("MaHD");
                Date ngayTaoDate = rs.getDate("NgayTao");
                String maKH = rs.getString("MaKH");
                String maNV = rs.getString("MaNV");
                String maKM = rs.getString("MaKM");
                double tongTien = rs.getDouble("TongTien");
                boolean deleteAt = rs.getBoolean("DeleteAt");

                NhanVien nhanVien = new NhanVien(maNV);
                KhachHang khachHang = new KhachHang(maKH);
                KhuyenMai khuyenMai = null;
                if (maKM != null) {
                    khuyenMai = new KhuyenMai();
                    // set only MaKM, other fields default
                    java.lang.reflect.Field f = khuyenMai.getClass().getDeclaredField("MaKM");
                    f.setAccessible(true);
                    f.set(khuyenMai, maKM);
                }
                HoaDon hoaDon = new HoaDon(maHD, ngayTaoDate.toLocalDate(), nhanVien, khachHang, khuyenMai, deleteAt);
                hoaDon.setTongTien(tongTien);
                dsHoaDon.add(hoaDon);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dsHoaDon;
    }

    /**
     * Tìm kiếm hóa đơn theo mã hóa đơn (MaHD) - tìm tương đối (LIKE).
     * @param maHd Mã hóa đơn cần tìm (có thể là một phần mã)
     * @return Danh sách hóa đơn có mã chứa chuỗi nhập vào
     */
    public ArrayList<HoaDon> searchHoaDonByMaHd(String maHd) {
        ArrayList<HoaDon> dsHoaDon = new ArrayList<>();
        if (maHd == null || maHd.trim().isEmpty()) {
            return dsHoaDon; // Trả về rỗng nếu không truyền mã
        }
        String sql = "SELECT * FROM HoaDon WHERE MaHD LIKE ? AND DeleteAt = 0";
        try {
            Connection con = ConnectDB.getConnection();
            java.sql.PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + maHd + "%"); // Tìm tương đối
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String maHD = rs.getString("MaHD");
                Date ngayTaoDate = rs.getDate("NgayTao");
                String maKH = rs.getString("MaKH");
                String maNVRes = rs.getString("MaNV");
                String maKM = rs.getString("MaKM");
                double tongTien = rs.getDouble("TongTien");
                boolean deleteAt = rs.getBoolean("DeleteAt");

                NhanVien nhanVien = new NhanVien(maNVRes);
                KhachHang khachHang = new KhachHang(maKH);
                KhuyenMai khuyenMai = null;
                if (maKM != null) {
                    khuyenMai = new KhuyenMai();
                    java.lang.reflect.Field f = khuyenMai.getClass().getDeclaredField("MaKM");
                    f.setAccessible(true);
                    f.set(khuyenMai, maKM);
                }
                HoaDon hoaDon = new HoaDon(maHD, ngayTaoDate.toLocalDate(), nhanVien, khachHang, khuyenMai, deleteAt);
                hoaDon.setTongTien(tongTien);
                dsHoaDon.add(hoaDon);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dsHoaDon;
    }
}
