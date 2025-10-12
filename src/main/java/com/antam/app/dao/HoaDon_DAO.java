/*
 * @ (#) HoaDon_DAO.java   1.0 10/3/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dao;

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.HoaDon;
import com.antam.app.entity.KhachHang;
import com.antam.app.entity.KhuyenMai;
import com.antam.app.entity.NhanVien;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/3/2025
 * version: 1.0
 */
public class HoaDon_DAO {
    // duong
    /**
     * Lấy tất cả hóa đơn từ database
     * @return danh sách hóa đơn
     */
    public ArrayList<HoaDon> getAllHoaDon() {
        ArrayList<HoaDon> dsHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            try (Statement state = con.createStatement(); ResultSet rs = state.executeQuery(sql)) {
                while (rs.next()) {
                    String maHD = rs.getString("MaHD");
                    LocalDate ngayTaoDate = rs.getDate("NgayTao").toLocalDate();
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
                    HoaDon hoaDon = new HoaDon(maHD, ngayTaoDate, nhanVien, khachHang, khuyenMai, tongTien, deleteAt);
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
     * Thêm hóa đơn vào database
     * @param maHD hóa đơn cần thêm
     * @return true nếu thêm thành công, false nếu thêm thất bại
     */
    public HoaDon getHoaDonTheoMa(String maHD){
        HoaDon hd = new HoaDon(maHD);
        String sql = "SELECT * FROM HoaDon WHERE MaHD = ?";
        Connection con = ConnectDB.getConnection();
        try{
            java.sql.PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maHD);
            java.sql.ResultSet rs = statement.executeQuery();
            if(rs.next()){
                hd.setMaKH(new KhachHang(rs.getString("MaKH")));
                hd.setMaNV(new NhanVien(rs.getString("MaNV")));
                hd.setNgayTao(rs.getDate("NgayTao").toLocalDate());
                hd.setTongTien(rs.getDouble("TongTien"));
                hd.setMaKM(rs.getString("MaKM") != null ? new KhuyenMai(rs.getString("MaKM")) : null);
                hd.setDeleteAt(rs.getBoolean("DeleteAt"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hd;
    }

    /**
     * Cập nhật tổng tiền của hóa đơn
     * @param maHD mã hóa đơn cần cập nhật
     * @param tongTien tổng tiền mới
     * @return true nếu cập nhật thành công, false nếu cập nhật thất bại
     */
    public boolean CapNhatTongTienHoaDon(String maHD, double tongTien){
        String sql = "UPDATE HoaDon SET TongTien = ? WHERE MaHD = ?";
        Connection con = ConnectDB.getConnection();
        try{
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setDouble(1, tongTien);
            statement.setString(2, maHD);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Thêm hóa đơn vào database
     * @param maHD hóa đơn cần thêm
     * @return true nếu thêm thành công, false nếu thêm thất bại
     */
    public boolean xoaMemHoaDon(String maHD){
        String sql = "UPDATE HoaDon SET DeleteAt = 1 WHERE MaHD = ?";
        Connection con = ConnectDB.getConnection();
        try{
            java.sql.PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maHD);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    // duong

    //hung
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
                        LocalDate ngayTaoDate = rs.getDate("NgayTao").toLocalDate();
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
                        HoaDon hoaDon = new HoaDon(maHD, ngayTaoDate, nhanVien, khachHang, khuyenMai, tongTien, deleteAt);
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
                    LocalDate ngayTaoDate = rs.getDate("NgayTao").toLocalDate();
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
                    HoaDon hoaDon = new HoaDon(maHD, ngayTaoDate, nhanVien, khachHang, khuyenMai, tongTien, deleteAt);
                    hoaDon.setTongTien(tongTien);
                    dsHoaDon.add(hoaDon);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsHoaDon;
    }

    /**
     * Tìm kiếm hóa đơn theo mã nhân viên
     * @param maNV mã nhân viên
     * @return danh sách hóa đơn của nhân viên đó
     */
    public ArrayList<HoaDon> searchHoaDonByMaNV(String maNV) {
        ArrayList<HoaDon> dsHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE MaNV = ?";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            try (PreparedStatement state = con.prepareStatement(sql)) {
                state.setString(1, maNV);
                try (ResultSet rs = state.executeQuery()) {
                    while (rs.next()) {
                        String maHD = rs.getString("MaHD");
                        LocalDate ngayTaoDate = rs.getDate("NgayTao").toLocalDate();
                        String maKH = rs.getString("MaKH");
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
                        HoaDon hoaDon = new HoaDon(maHD, ngayTaoDate, nhanVien, khachHang, khuyenMai, tongTien, deleteAt);
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
     * Thêm hóa đơn vào database
     * @param hoaDon hóa đơn cần thêm
     * @return true nếu thêm thành công, false nếu thêm thất bại
     */
    public boolean insertHoaDon(HoaDon hoaDon) {
        String sql = "INSERT INTO HoaDon (MaHD, NgayTao, MaNV, MaKH, MaKM, TongTien, DeleteAt) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, hoaDon.getMaHD());
                ps.setDate(2, java.sql.Date.valueOf(hoaDon.getNgayTao()));
                ps.setString(3, hoaDon.getMaNV() != null ? hoaDon.getMaNV().getMaNV() : null);
                ps.setString(4, hoaDon.getMaKH() != null ? hoaDon.getMaKH().getMaKH() : null);
                if (hoaDon.getMaKM() != null) {
                    ps.setString(5, hoaDon.getMaKM().getMaKM());
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
        }
        return false;
    }
}