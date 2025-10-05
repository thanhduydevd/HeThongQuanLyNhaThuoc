package com.antam.app.dao;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 02/10/2025
 * @version: 1.0
 */

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.PhieuDatThuoc;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class PhieuDat_DAO {
    public static ArrayList<PhieuDatThuoc> list = PhieuDat_DAO.getAllPhieuDatThuocFromDBS();

    /**
     * Lấy toàn bộ danh sách phiếu đặt thuốc với thông tin bao gồm
     * mã, ngày tạo, đã thanh toán hay chưa, mã khách, mã nhân viên tạo
     * thành tiền của phiếu đặt
     * @return Array[PhieuDatThuoc]
     */
    public static ArrayList<PhieuDatThuoc> getAllPhieuDatThuocFromDBS(){
        ArrayList<PhieuDatThuoc> ds = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Connection con = ConnectDB.getConnection();

        try {
            String sql = "select * from PhieuDatThuoc";
            PreparedStatement state = con.prepareStatement(sql);
            ResultSet kq = state.executeQuery();
            while(kq.next()){
                String ma = kq.getString(1);
                LocalDate ngay = kq.getDate(2).toLocalDate();
                boolean isThanhToan = kq.getBoolean(3);
                String maKhach = kq.getString(4);
                String maNV = kq.getString(5);
                String maKM = kq.getString(6);
                double total = kq.getDouble(7);
//                PhieuDatThuoc e = new PhieuDatThuoc(ma,ngay,isThanhToan,
//                        NhanVien_DAO.dsNhanViens.stream()
//                                .filter(t-> t.getMaNV().equalsIgnoreCase(maNV))
//                                .findFirst(),khach,km,total);
                ds.add(null);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ds;
    }

    public static boolean themPhieuDatThuocVaoDBS(PhieuDatThuoc i){
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Connection con = ConnectDB.getConnection();
        // kiểm tra đã tồn tại hay chưa
        try {
            String sql = "select * from PhieuDatThuoc where MaPDT = ?";
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1,i.getMaPhieu());
            ResultSet check = state.executeQuery();
            if (check.next()){
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            String updateSQL = "insert from PhieuDatThuoc values , [MaPDT] = ?," +
                    " [NgayTao] = ?,[IsThanhToan] = ?,[MaKH] = ?, [MaNV] = ?, [MaKM] = ?," +
                    "[TongTien] = ?";
            PreparedStatement state = con.prepareStatement(updateSQL);
            state.setString(1,i.getMaPhieu());
            state.setDate(2, Date.valueOf(i.getNgayTao()));
            state.setBoolean(3,i.isThanhToan());
            state.setString(4,i.getKhachHang().getMaKH());
            state.setString(5,i.getNhanVien().getMaNV());
            state.setString(6,i.getKhuyenMai().getMaKM());
            state.setDouble(7,i.getTongTien());
            int kq = state.executeUpdate();
            return kq >0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Xoá phiếu đặt thuốc trong DBS bằng cách tắt trạng thái hoạt động.
     * @param maPDT mã phiếu đặt thuốc
     * @return true nếu cập nhật thành công. false nếu không thể cập nhật.
     */
    public static boolean xoaPhieuDatThuocTrongDBS(String maPDT){
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Connection con = ConnectDB.getConnection();

        try {
            String sql = "update PhieuDatThuoc set DeleteAt = 1 where MaPDT = ?";
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1,maPDT);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }


}
