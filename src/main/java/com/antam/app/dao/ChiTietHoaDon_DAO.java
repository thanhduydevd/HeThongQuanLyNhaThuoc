/*
 * @ (#) ChiTietHoaDon_DAO.java   1.0 6/10/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.antam.app.dao;

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.ChiTietHoaDon;
import com.antam.app.entity.DonViTinh;
import com.antam.app.entity.Thuoc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho bảng ChiTietHoaDon
 * @author: Tran Tuan Hung
 * @date: 6/10/25
 */
public class ChiTietHoaDon_DAO {
    /**
     * Lấy danh sách chi tiết hóa đơn theo mã hóa đơn
     * @param maHD mã hóa đơn
     * @return danh sách chi tiết hóa đơn
     */
    public List<ChiTietHoaDon> getChiTietHoaDonByMaHD(String maHD) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT cthd.*, t.TenThuoc, t.GiaBan, dvt.TenDVT FROM ChiTietHoaDon cthd " +
                "JOIN Thuoc t ON cthd.MaThuoc = t.MaThuoc " +
                "JOIN DonViTinh dvt ON cthd.MaDVT = dvt.MaDVT " +
                "WHERE cthd.MaHD = ?";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maHD);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String maThuoc = rs.getString("MaThuoc");
                        int soLuong = rs.getInt("SoLuong");
                        int maDVT = rs.getInt("MaDVT");
                        String tinhTrang = rs.getString("TinhTrang");
                        double thanhTien = rs.getDouble("ThanhTien");
                        String tenThuoc = rs.getString("TenThuoc");
                        double giaBan = rs.getDouble("GiaBan");
                        String tenDVT = rs.getString("TenDVT");
                        Thuoc thuoc = new Thuoc(maThuoc);
                        thuoc.setTenThuoc(tenThuoc);
                        thuoc.setGiaBan(giaBan);
                        DonViTinh dvt = new DonViTinh(maDVT);
                        dvt.setTenDVT(tenDVT);
                        ChiTietHoaDon cthd = new ChiTietHoaDon(maHD, thuoc, soLuong, dvt, tinhTrang, thanhTien);
                        list.add(cthd);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }


}
