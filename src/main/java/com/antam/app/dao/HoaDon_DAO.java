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
import java.sql.PreparedStatement;
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
        ArrayList<HoaDon> dsHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE DeleteAt = 0";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            try (Statement state = con.createStatement(); ResultSet rs = state.executeQuery(sql)) {
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
                        java.lang.reflect.Field f = khuyenMai.getClass().getDeclaredField("MaKM");
                        f.setAccessible(true);
                        f.set(khuyenMai, maKM);
                    }
                    HoaDon hoaDon = new HoaDon(maHD, ngayTaoDate.toLocalDate(), nhanVien, khachHang, khuyenMai, deleteAt);
                    hoaDon.setTongTien(tongTien);
                    dsHoaDon.add(hoaDon);
                }
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
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            try (java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, "%" + maHd + "%"); // Tìm tương đối
                try (ResultSet rs = ps.executeQuery()) {
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
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dsHoaDon;
    }

    /**
     * Thêm mới một hóa đơn vào cơ sở dữ liệu
     * @param hoaDon Hóa đơn cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean createHoaDon(HoaDon hoaDon) {
        String sql = "INSERT INTO HoaDon (MaHD, NgayTao, MaNV, MaKH, MaKM, TongTien, DeleteAt) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            try (java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, hoaDon.getMaHD());
                ps.setDate(2, java.sql.Date.valueOf(hoaDon.getNgayTao()));
                ps.setString(3, hoaDon.getMaNV().getMaNV());
                ps.setString(4, hoaDon.getMaKH().getMaKH());
                if (hoaDon.getMaKM() != null) {
                    // Lấy giá trị MaKM từ đối tượng KhuyenMai
                    java.lang.reflect.Field f = hoaDon.getMaKM().getClass().getDeclaredField("MaKM");
                    f.setAccessible(true);
                    String maKM = (String) f.get(hoaDon.getMaKM());
                    ps.setString(5, maKM);
                } else {
                    ps.setNull(5, java.sql.Types.VARCHAR);
                }
                ps.setDouble(6, hoaDon.getTongTien());
                ps.setBoolean(7, hoaDon.isDeleteAt());
                int rows = ps.executeUpdate();
                return rows > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Tìm kiếm hóa đơn theo mã nhân viên (MaNV)
     * @param maNV Mã nhân viên cần tìm
     * @return Danh sách hóa đơn của nhân viên có mã tương ứng
     */
    public ArrayList<HoaDon> searchHoaDonByMaNV(String maNV) {
        ArrayList<HoaDon> ds = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE MaNV = ? AND DeleteAt = 0";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, maNV);
                try (ResultSet rs = stmt.executeQuery()) {
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
                        ds.add(hoaDon);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    /**
     * Lọc hóa đơn theo trạng thái: "Tất cả", "Hoạt động", "Đã huỷ"
     */
    public ArrayList<HoaDon> searchHoaDonByStatus(String status) {
        ArrayList<HoaDon> dsHoaDon = new ArrayList<>();
        String sql;
        if ("Tất cả".equals(status)) {
            sql = "SELECT * FROM HoaDon";
        } else if ("Hoạt động".equals(status)) {
            sql = "SELECT * FROM HoaDon WHERE DeleteAt = 0";
        } else if ("Đã huỷ".equals(status)) {
            sql = "SELECT * FROM HoaDon WHERE DeleteAt = 1";
        } else {
            sql = "SELECT * FROM HoaDon";
        }
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            try (Statement state = con.createStatement(); ResultSet rs = state.executeQuery(sql)) {
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
                        java.lang.reflect.Field f = khuyenMai.getClass().getDeclaredField("MaKM");
                        f.setAccessible(true);
                        f.set(khuyenMai, maKM);
                    }
                    HoaDon hoaDon = new HoaDon(maHD, ngayTaoDate.toLocalDate(), nhanVien, khachHang, khuyenMai, deleteAt);
                    hoaDon.setTongTien(tongTien);
                    dsHoaDon.add(hoaDon);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsHoaDon;
    }
}
