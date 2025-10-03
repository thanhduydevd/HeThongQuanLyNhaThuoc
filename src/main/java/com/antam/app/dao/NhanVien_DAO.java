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
import java.util.stream.Collectors;

public class NhanVien_DAO {

    //danh sách nhân viên truy xuất trực tiếp khi vào tầng Application.
    public static ArrayList<NhanVien> dsNhanViens = NhanVien_DAO.getDsNhanVienformDBS();

    public NhanVien findNhanVienVoiMa(String maVN){
        return dsNhanViens.stream().
                filter(t-> t.getMaNV().equalsIgnoreCase(maVN))
                .findFirst()
                .orElse(null);
    }
    /**
     * Phương thức lấy toàn bộ thông tin Nhân viên từ DBS. trả về 1 mảng nhân viên
     * @return Array[NhanVien]
     */

    public static ArrayList<NhanVien> getDsNhanVienformDBS(){
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

    /**
     * thêm nhân viên mới vào DBS. Phưong thức trước tiên sẽ kiểm tra nhân viên có tồn tại trong hệ thống hay chưa.
     * Nếu có sẽ kiểm tra tiếp đến trạng thái hoạt động, nếu vẫn đang hoạt động thì sẽ trả kết quả false và thoát chức năng.
     * Nếu không thì sẽ cập nhật lại và bật trạng thái hoạt động của nhân viên.
     * Nếu nhân viên chưa có trong hệ thống thì sẽ tạo mới vào trong DBS.
     * @param nv NhanVien
     * @return true nếu có nhân viên được thêm hoặc cập nhật.
     *  false nếu nhân viên đã tồn tại trong hệ thống
     */
    public static boolean themNhanVien(NhanVien nv){
        int kq = 0;
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        PreparedStatement state = null;
        Connection con = ConnectDB.getConnection();

        //kiểm tra đã tồn tại hay chưa
        try {
            String sql = "select DeleteAt From NhanVien where MaNV = ?";
            state = con.prepareStatement(sql);
            state.setString(1, nv.getMaNV());
            ResultSet rs = state.executeQuery();
            if (rs.next()){
                int status = rs.getInt("DeleteAt");
                state.close();
                //Kiểm tra vẫn còn hoạt đông hay không
                if (status == 0){
                    //Vẫn hoạt động -> thoát phương thức
                    return false;
                }else{
                    //không hoạt động -> cập nhật nhân viên
                    String updateSQL = "Update NhanVien Set HoTen = ?,SoDienThoai = ?,Email = ?,DiaChi =?,LuongCoBan =?,TaiKhoan=?," +
                            "MatKhau=?,IsQuanLy=?,DeleteAt = ? where MaNV = ?";
                    state = con.prepareStatement(updateSQL);
                    state.setString(10, nv.getMaNV());
                    state.setString(1, nv.getHoTen());
                    state.setString(2, nv.getSoDienThoai());
                    state.setString(3, nv.getEmail());
                    state.setString(4, nv.getDiaChi());
                    state.setDouble(5, nv.getLuongCoBan());
                    state.setString(6, nv.getTaiKhoan());
                    state.setString(7, nv.getMatKhau());
                    state.setBoolean(8, nv.isQuanLy());
                    state.setBoolean(9,false);
                    kq = state.executeUpdate();
                    return kq>0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // chưa có thì thêm mới
        try {
            String sql = "INSERT INTO NhanVien VALUES (?,?,?,?,?,?,?,?,?,?);";
            state = con.prepareStatement(sql);
            state.setString(1, nv.getMaNV());
            state.setString(2, nv.getHoTen());
            state.setString(3, nv.getSoDienThoai());
            state.setString(4, nv.getEmail());
            state.setString(5, nv.getDiaChi());
            state.setDouble(6, nv.getLuongCoBan());
            state.setString(7, nv.getTaiKhoan());
            state.setString(8, nv.getMatKhau());
            state.setBoolean(9, nv.isQuanLy());
            state.setBoolean(10,false);
            kq = state.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if (state != null) {
                    state.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return kq > 0;
    }

    /**
     * Cập nhật nhân viên với thông tin mới.
     * @param nv NhanVien
     * @return true nếu có nhân viên được cập nhật.
     *          false nếu không có nhân viên nào được cập nhật hoặc nhân viên không tồn tại.
     */
    public static boolean updateNhanVienTrongDBS(NhanVien nv){
        int kq = 0;
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Connection con = ConnectDB.getConnection();
        PreparedStatement state = null;
        String updateSQL = "Update NhanVien Set HoTen = ?,SoDienThoai = ?,Email = ?,DiaChi =?,LuongCoBan =?,TaiKhoan=?," +
                "MatKhau=?,IsQuanLy=?,DeleteAt = ? where MaNV = ?";
        try {
            state = con.prepareStatement(updateSQL);
            state.setString(10, nv.getMaNV());
            state.setString(1, nv.getHoTen());
            state.setString(2, nv.getSoDienThoai());
            state.setString(3, nv.getEmail());
            state.setString(4, nv.getDiaChi());
            state.setDouble(5, nv.getLuongCoBan());
            state.setString(6, nv.getTaiKhoan());
            state.setString(7, nv.getMatKhau());
            state.setBoolean(8, nv.isQuanLy());
            state.setBoolean(9,false);
            kq = state.executeUpdate();
            return kq >0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Là phương thức sử dụng để tắt trạng thái hoạt đông trong dbs sử dụng mã nhân viên.
     * @param manv
     * @return true nếu có nhân viên bị cập nhật ở dbs. false nếu không có nhân viên nào cập nhật trạng thái.
     */
    public static boolean xoaNhanVienTrongDBS(String manv){
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        PreparedStatement state = null;
        Connection con = ConnectDB.getConnection();
        String sql = "Update TenBang set DeleteAt = 0 where MaNV = ?";
        int kq =0;
        try {
            state = con.prepareStatement(sql);
            state.setString(1,manv);
            kq = state.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (state != null) {
                    state.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return kq > 0;
    }
}
