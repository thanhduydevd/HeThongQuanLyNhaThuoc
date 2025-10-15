package com.antam.app.dao;

import com.antam.app.entity.KhuyenMai;
import com.antam.app.entity.LoaiKhuyenMai;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.antam.app.connect.ConnectDB;

public class KhuyenMai_DAO {
    public List<KhuyenMai> getAllKhuyenMaiConHieuLuc() {
        List<KhuyenMai> list = new ArrayList<>();
        String sql = "SELECT km.MaKM, km.TenKM, km.NgayBatDau, km.NgayKetThuc, km.LoaiKhuyenMai, km.So, km.SoLuongToiDa, km.deleteAt, lkm.TenLKM " +
                "FROM KhuyenMai km JOIN LoaiKhuyenMai lkm ON km.LoaiKhuyenMai = lkm.MaLKM " +
                "WHERE km.deleteAt = 0 AND km.NgayBatDau <= GETDATE() AND km.NgayKetThuc >= GETDATE()";
        try (Connection con = ConnectDB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String maKM = rs.getString("MaKM");
                String tenKM = rs.getString("TenKM");
                java.sql.Date sqlNgayBatDau = rs.getDate("NgayBatDau");
                LocalDate ngayBatDau = sqlNgayBatDau != null ? sqlNgayBatDau.toLocalDate() : LocalDate.now();
                java.sql.Date sqlNgayKetThuc = rs.getDate("NgayKetThuc");
                LocalDate ngayKetThuc = sqlNgayKetThuc != null ? sqlNgayKetThuc.toLocalDate() : LocalDate.now();
                int maLoaiKM = rs.getInt("LoaiKhuyenMai");
                String tenLoaiKM = rs.getString("TenLKM");
                double so = rs.getDouble("So");
                int soLuongToiDa = rs.getInt("SoLuongToiDa");
                boolean deleteAt = rs.getBoolean("deleteAt");
                LoaiKhuyenMai loai = new LoaiKhuyenMai(maLoaiKM, tenLoaiKM);
                KhuyenMai km = new KhuyenMai(maKM, tenKM, ngayBatDau, ngayKetThuc, loai, so, soLuongToiDa, deleteAt);
                list.add(km);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy thông tin khuyến mãi theo mã
     * @param maKM mã khuyến mãi
     * @return đối tượng KhuyenMai hoặc null nếu không tìm thấy
     */
    public KhuyenMai getKhuyenMaiTheoMa(String maKM) {
        if (maKM == null || maKM.trim().isEmpty()) {
            return null;
        }
        String sql = "SELECT km.MaKM, km.TenKM, km.NgayBatDau, km.NgayKetThuc, km.LoaiKhuyenMai, km.So, km.SoLuongToiDa, km.deleteAt, lkm.TenLKM " +
                "FROM KhuyenMai km JOIN LoaiKhuyenMai lkm ON km.LoaiKhuyenMai = lkm.MaLKM " +
                "WHERE km.MaKM = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKM);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String tenKM = rs.getString("TenKM");
                    java.sql.Date sqlNgayBatDau = rs.getDate("NgayBatDau");
                    LocalDate ngayBatDau = sqlNgayBatDau != null ? sqlNgayBatDau.toLocalDate() : LocalDate.now();
                    java.sql.Date sqlNgayKetThuc = rs.getDate("NgayKetThuc");
                    LocalDate ngayKetThuc = sqlNgayKetThuc != null ? sqlNgayKetThuc.toLocalDate() : LocalDate.now();
                    int maLoaiKM = rs.getInt("LoaiKhuyenMai");
                    String tenLoaiKM = rs.getString("TenLKM");
                    double so = rs.getDouble("So");
                    int soLuongToiDa = rs.getInt("SoLuongToiDa");
                    boolean deleteAt = rs.getBoolean("deleteAt");
                    LoaiKhuyenMai loai = new LoaiKhuyenMai(maLoaiKM, tenLoaiKM);
                    return new KhuyenMai(maKM, tenKM, ngayBatDau, ngayKetThuc, loai, so, soLuongToiDa, deleteAt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
