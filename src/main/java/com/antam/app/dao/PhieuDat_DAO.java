package com.antam.app.dao;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 02/10/2025
 * @version: 1.0
 */

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.NhanVien;
import com.antam.app.entity.PhieuDatThuoc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class PhieuDat_DAO {
    public static ArrayList<PhieuDatThuoc> list ;

    public static ArrayList<PhieuDatThuoc> getAllPhieuDatThuocFromDBS(){
        ArrayList<PhieuDatThuoc> ds = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Connection con = ConnectDB.getConnection();

        try {
            String sql = "select * form PhieuDatThuoc";
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
}
