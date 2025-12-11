/*
 * @ (#) KhachHang_DAO.java   1.0 1/10/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.antam.app.dao;

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
            String sql = "SELECT * FROM KhachHang WHERE DeleteAt = 0";
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

    /**
     * Thêm khách hàng mới vào CSDL
     * @param kh KhachHang
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean insertKhachHang(KhachHang kh) {
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "INSERT INTO KhachHang (MaKH, TenKH, SoDienThoai, DeleteAt) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getSoDienThoai());
            ps.setBoolean(4, kh.isDeleteAt());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật thông tin khách hàng trong CSDL
     * @param kh KhachHang
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean updateKhachHang(KhachHang kh) {
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "UPDATE KhachHang SET TenKH = ?, SoDienThoai = ? WHERE MaKH = ? AND DeleteAt = 0";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, kh.getTenKH());
            ps.setString(2, kh.getSoDienThoai());
            ps.setString(3, kh.getMaKH());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public KhachHang getKhachHangTheoMa(String maKH){
        KhachHang kh = new KhachHang(maKH);
        String sql = "SELECT * FROM KhachHang WHERE MaKH = ? AND DeleteAt = 0";
        try{
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maKH);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                kh.setTenKH(rs.getString("TenKH"));
                kh.setSoDienThoai(rs.getString("SoDienThoai"));
                kh.setDeleteAt(rs.getBoolean("DeleteAt"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kh;
    }

    /**
     * Tìm khách hàng theo số điện thoại
     * @param soDienThoai Số điện thoại khách hàng
     * @return KhachHang nếu tìm thấy, null nếu không tìm thấy
     */
    public KhachHang getKhachHangTheoSoDienThoai(String soDienThoai) {
        String sql = "SELECT * FROM KhachHang WHERE SoDienThoai = ? AND DeleteAt = 0";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, soDienThoai);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                KhachHang kh = new KhachHang(rs.getString("MaKH"));
                kh.setTenKH(rs.getString("TenKH"));
                kh.setSoDienThoai(rs.getString("SoDienThoai"));
                kh.setDeleteAt(rs.getBoolean("DeleteAt"));
                return kh;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Trả về danh sách tất cả khách hàng trong CSDL
     */
    public ArrayList<KhachHang> getAllKhachHang() {
        return loadBanFromDB();
    }

    public static List<KhachHang> loadKhachHangFromDB() {
        List<KhachHang> dsKhachHang = new ArrayList<>();

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            String sql = """
                SELECT kh.MaKH, kh.TenKH, kh.SoDienThoai, kh.DeleteAt,
                       COALESCE(SUM(hd.TongTien), 0) as TongChiTieu,
                       COUNT(hd.MaHD) as SoDonHang,
                       MAX(hd.NgayTao) as NgayMuaGanNhat
                FROM KhachHang kh
                LEFT JOIN HoaDon hd ON kh.MaKH = hd.MaKH AND hd.deleteAt = 0
                WHERE kh.DeleteAt = 0
                GROUP BY kh.MaKH, kh.TenKH, kh.SoDienThoai, kh.DeleteAt
                ORDER BY kh.TenKH
                """;

            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                KhachHang kh = new KhachHang(rs.getString("MaKH"));
                kh.setTenKH(rs.getString("TenKH"));
                kh.setSoDienThoai(rs.getString("SoDienThoai"));
                kh.setDeleteAt(rs.getBoolean("DeleteAt"));

                // Set thêm thông tin thống kê
                kh.setTongChiTieu(rs.getDouble("TongChiTieu"));
                kh.setSoDonHang(rs.getInt("SoDonHang"));

                Date ngayMuaGanNhat = rs.getDate("NgayMuaGanNhat");
                if (ngayMuaGanNhat != null) {
                    kh.setNgayMuaGanNhat(ngayMuaGanNhat.toLocalDate());
                }

                dsKhachHang.add(kh);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsKhachHang;
    }

    public static List<KhachHang> searchKhachHangByName(String tenKH) {
        List<KhachHang> dsKhachHang = new ArrayList<>();

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            String sql = """
                SELECT kh.MaKH, kh.TenKH, kh.SoDienThoai, kh.DeleteAt,
                       COALESCE(SUM(hd.TongTien), 0) as TongChiTieu,
                       COUNT(hd.MaHD) as SoDonHang,
                       MAX(hd.NgayTao) as NgayMuaGanNhat
                FROM KhachHang kh
                LEFT JOIN HoaDon hd ON kh.MaKH = hd.MaKH AND hd.deleteAt = 0
                WHERE kh.DeleteAt = 0 AND kh.TenKH LIKE ?
                GROUP BY kh.MaKH, kh.TenKH, kh.SoDienThoai, kh.DeleteAt
                ORDER BY kh.TenKH
                """;

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + tenKH + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                KhachHang kh = new KhachHang(rs.getString("MaKH"));
                kh.setTenKH(rs.getString("TenKH"));
                kh.setSoDienThoai(rs.getString("SoDienThoai"));
                kh.setDeleteAt(rs.getBoolean("DeleteAt"));

                // Set thêm thông tin thống kê
                kh.setTongChiTieu(rs.getDouble("TongChiTieu"));
                kh.setSoDonHang(rs.getInt("SoDonHang"));

                Date ngayMuaGanNhat = rs.getDate("NgayMuaGanNhat");
                if (ngayMuaGanNhat != null) {
                    kh.setNgayMuaGanNhat(ngayMuaGanNhat.toLocalDate());
                }

                dsKhachHang.add(kh);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsKhachHang;
    }

    public static int getTongKhachHang() {
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            String sql = "SELECT COUNT(*) FROM KhachHang WHERE DeleteAt = 0";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getTongKhachHangVIP() {
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            String sql = """
                SELECT COUNT(*) FROM (
                    SELECT kh.MaKH
                    FROM KhachHang kh
                    LEFT JOIN HoaDon hd ON kh.MaKH = hd.MaKH AND hd.deleteAt = 0
                    WHERE kh.DeleteAt = 0
                    GROUP BY kh.MaKH
                    HAVING COALESCE(SUM(hd.TongTien), 0) >= 1000000
                ) as VipCustomers
                """;
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getTongDonHang() {
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            String sql = "SELECT COUNT(*) FROM HoaDon WHERE deleteAt = 0";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static double getTongDoanhThu() {
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            String sql = "SELECT COALESCE(SUM(TongTien), 0) FROM HoaDon WHERE deleteAt = 0";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
