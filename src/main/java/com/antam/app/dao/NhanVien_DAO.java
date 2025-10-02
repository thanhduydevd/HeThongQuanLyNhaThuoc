package com.antam.app.dao;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 01/10/2025
 * @version: 1.0
 */

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.NhanVien;

import java.sql.*;
import java.util.ArrayList;

public class NhanVien_DAO {

    public static ArrayList<NhanVien> dsNhanVien(){
        ArrayList<NhanVien> list = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Connection con = ConnectDB.getConnection();
        String sql = "Select * from NhanVien";
        try {
            PreparedStatement state = con.prepareStatement(sql);
            ResultSet result = state.executeQuery();
            while (result.next()){
                boolean isXoa = result.getBoolean("DeleteAt");
                if (!isXoa){
                    String maNV = result.getNString("MaNV");
                    String hoTen = result.getNString("HoTen");
                    String soDT = result.getNString("SoDienThoai");
                    String email = result.getNString("Email");
                    String diaChi = result.getNString("DiaChi");
                    double luongCb = result.getDouble("LuongCoBan");
                    String taiKhoan = result.getNString("TaiKhoan");
                    String matKhau = result.getNString("MatKhau");
                    boolean isQL = result.getBoolean("IsQuanLi");
                    NhanVien e = new NhanVien(maNV,hoTen,soDT,email,diaChi,luongCb
                    ,taiKhoan,matKhau,isQL);
                    list.add(e);
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
