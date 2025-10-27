package com.antam.app.dao;

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.NhanVien;
import com.antam.app.entity.PhieuNhap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class PhieuNhap_DAO {

    /* Lấy danh sách phiếu nhập */
    public ArrayList<PhieuNhap> getDanhSachPhieuNhap(){
        ArrayList<PhieuNhap> list = new ArrayList<>();
        String sql = "SELECT * FROM PhieuNhap";

        try{
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String maPhieuNhap = rs.getString("MaPhieuNhap");
                String nhaCungCap = rs.getString("NhaCungCap");
                LocalDate ngayNhap = rs.getDate("NgayNhap").toLocalDate();
                String diaChi = rs.getString("DiaChi");
                String maNhanVien = rs.getString("MaNV");
                String lyDo = rs.getString("LyDo");
                double tongTien = rs.getDouble("TongTien");
                boolean deleteAt = rs.getBoolean("DeleteAt");

                PhieuNhap pn = new PhieuNhap(maPhieuNhap, nhaCungCap, ngayNhap, diaChi, lyDo, new NhanVien(maNhanVien), tongTien, deleteAt);

                list.add(pn);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
