/*
 * @ (#) ThongKe_DAO.java   1.0 1/10/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.antam.app.dao;

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.ThongKeDoanhThu;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * @description DAO cho thống kê doanh thu - Xử lý các truy vấn liên quan đến báo cáo và thống kê
 * @author: Tran Tuan Hung
 * @date: 1/10/25
 * @version: 1.0
 */
public class ThongKe_DAO {

    /**
     * Lấy tổng doanh thu theo khoảng thời gian và nhân viên
     * @param tuNgay Ngày bắt đầu tính doanh thu
     * @param denNgay Ngày kết thúc tính doanh thu
     * @param maNV Mã nhân viên (null nếu tính cho tất cả nhân viên)
     * @return Tổng doanh thu trong khoảng thời gian
     */
    public double getTongDoanhThu(LocalDate tuNgay, LocalDate denNgay, String maNV) {
        double tongDoanhThu = 0;
        // Câu SQL cơ bản - tính tổng tiền từ bảng HoaDon
        String sql = "SELECT SUM(TongTien) as TongDoanhThu FROM HoaDon WHERE NgayTao BETWEEN ? AND ? AND DeleteAt = 0";

        // Nếu có chọn nhân viên cụ thể thì thêm điều kiện lọc theo mã nhân viên
        if (maNV != null && !maNV.isEmpty() && !maNV.equals("Tất cả")) {
            sql += " AND MaNV = ?";
        }

        try (Connection con = ConnectDB.getInstance().connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Set tham số ngày bắt đầu và ngày kết thúc
            ps.setDate(1, Date.valueOf(tuNgay));
            ps.setDate(2, Date.valueOf(denNgay));

            // Set tham số mã nhân viên nếu có
            if (maNV != null && !maNV.isEmpty() && !maNV.equals("Tất cả")) {
                ps.setString(3, maNV);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tongDoanhThu = rs.getDouble("TongDoanhThu");
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy tổng doanh thu: " + e.getMessage());
            e.printStackTrace();
        }

        return tongDoanhThu;
    }

    /**
     * Lấy tổng số đơn hàng theo khoảng thời gian và nhân viên
     * @param tuNgay Ngày bắt đầu đếm đơn hàng
     * @param denNgay Ngày kết thúc đếm đơn hàng
     * @param maNV Mã nhân viên (null nếu đếm cho tất cả nhân viên)
     * @return Tổng số đơn hàng trong khoảng thời gian
     */
    public int getTongDonHang(LocalDate tuNgay, LocalDate denNgay, String maNV) {
        int tongDonHang = 0;
        // Đếm số lượng hóa đơn (không tính hóa đơn đã bị xóa)
        String sql = "SELECT COUNT(*) as TongDonHang FROM HoaDon WHERE NgayTao BETWEEN ? AND ? AND DeleteAt = 0";

        // Thêm điều kiện lọc theo nhân viên nếu có
        if (maNV != null && !maNV.isEmpty() && !maNV.equals("Tất cả")) {
            sql += " AND MaNV = ?";
        }

        try (Connection con = ConnectDB.getInstance().connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(tuNgay));
            ps.setDate(2, Date.valueOf(denNgay));

            if (maNV != null && !maNV.isEmpty() && !maNV.equals("Tất cả")) {
                ps.setString(3, maNV);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tongDonHang = rs.getInt("TongDonHang");
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy tổng đơn hàng: " + e.getMessage());
            e.printStackTrace();
        }

        return tongDonHang;
    }

    /**
     * Lấy số khách hàng mới trong khoảng thời gian
     * Khách hàng mới = khách hàng có lần mua đầu tiên trong khoảng thời gian này
     * @param tuNgay Ngày bắt đầu
     * @param denNgay Ngày kết thúc
     * @return Số lượng khách hàng mới
     */
    public int getSoKhachHangMoi(LocalDate tuNgay, LocalDate denNgay) {
        int soKhachHangMoi = 0;
        // Query phức tạp: tìm khách hàng có lần mua đầu tiên trong khoảng thời gian
        String sql = """
            SELECT COUNT(DISTINCT h.MaKH) as SoKhachHangMoi 
            FROM HoaDon h
            WHERE h.NgayTao BETWEEN ? AND ? 
            AND h.DeleteAt = 0
            AND h.MaKH IN (
                SELECT MaKH FROM HoaDon 
                WHERE NgayTao >= ? 
                GROUP BY MaKH 
                HAVING MIN(NgayTao) BETWEEN ? AND ?
            )
        """;

        try (Connection con = ConnectDB.getInstance().connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Set 5 tham số cho query (do có subquery phức tạp)
            ps.setDate(1, Date.valueOf(tuNgay));
            ps.setDate(2, Date.valueOf(denNgay));
            ps.setDate(3, Date.valueOf(tuNgay));
            ps.setDate(4, Date.valueOf(tuNgay));
            ps.setDate(5, Date.valueOf(denNgay));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    soKhachHangMoi = rs.getInt("SoKhachHangMoi");
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy số khách hàng mới: " + e.getMessage());
            e.printStackTrace();
        }

        return soKhachHangMoi;
    }

    /**
     * Lấy chi tiết doanh thu theo từng ngày trong khoảng thời gian
     * Trả về danh sách các ngày với thông tin: số đơn hàng, doanh thu, đơn hàng TB, khách hàng mới
     * @param tuNgay Ngày bắt đầu
     * @param denNgay Ngày kết thúc
     * @param maNV Mã nhân viên (null nếu lấy cho tất cả)
     * @return Danh sách thống kê theo ngày
     */
    public ArrayList<ThongKeDoanhThu> getDoanhThuTheoNgay(LocalDate tuNgay, LocalDate denNgay, String maNV) {
        ArrayList<ThongKeDoanhThu> danhSach = new ArrayList<>();
        // Group by NgayTao để tính thống kê theo từng ngày
        String sql = """
            SELECT 
                NgayTao,
                COUNT(*) as SoDonHang,
                SUM(TongTien) as DoanhThu,
                AVG(TongTien) as DonHangTB
            FROM HoaDon 
            WHERE NgayTao BETWEEN ? AND ? AND DeleteAt = 0
        """;

        // Thêm điều kiện lọc theo nhân viên nếu có
        if (maNV != null && !maNV.isEmpty() && !maNV.equals("Tất cả")) {
            sql += " AND MaNV = ?";
        }

        sql += " GROUP BY NgayTao ORDER BY NgayTao DESC"; // Sắp xếp ngày mới nhất trước

        try (Connection con = ConnectDB.getInstance().connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(tuNgay));
            ps.setDate(2, Date.valueOf(denNgay));

            if (maNV != null && !maNV.isEmpty() && !maNV.equals("Tất cả")) {
                ps.setString(3, maNV);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate ngay = rs.getDate("NgayTao").toLocalDate();
                    int soDonHang = rs.getInt("SoDonHang");
                    double doanhThu = rs.getDouble("DoanhThu");
                    double donHangTB = rs.getDouble("DonHangTB");

                    // Gọi method khác để lấy số khách hàng mới trong ngày này
                    int khachHangMoi = getSoKhachHangMoi(ngay, ngay);

                    // Gọi method khác để lấy nhân viên bán nhiều nhất trong ngày này
                    String nhanVienBan = getNhanVienBanNhieuNhat(ngay, ngay);

                    // Tạo object ThongKeDoanhThu và thêm vào danh sách
                    ThongKeDoanhThu thongKe = new ThongKeDoanhThu(ngay, soDonHang, doanhThu, donHangTB, khachHangMoi, nhanVienBan);
                    danhSach.add(thongKe);
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy doanh thu theo ngày: " + e.getMessage());
            e.printStackTrace();
        }

        return danhSach;
    }

    /**
     * Tìm nhân viên bán được nhiều đơn hàng nhất trong khoảng thời gian
     * @param tuNgay Ngày bắt đầu
     * @param denNgay Ngày kết thúc
     * @return Tên nhân viên + số đơn hàng (ví dụ: "Nguyễn Văn A (5 đơn)")
     */
    public String getNhanVienBanNhieuNhat(LocalDate tuNgay, LocalDate denNgay) {
        String nhanVienBan = "";
        // Join bảng HoaDon với NhanVien, group by nhân viên, sắp xếp theo số đơn hàng giảm dần
        String sql = """
            SELECT TOP 1 nv.HoTen, COUNT(*) as SoDonHang
            FROM HoaDon h
            JOIN NhanVien nv ON h.MaNV = nv.MaNV
            WHERE h.NgayTao BETWEEN ? AND ? AND h.DeleteAt = 0 AND nv.DeleteAt = 0
            GROUP BY h.MaNV, nv.HoTen
            ORDER BY COUNT(*) DESC
        """;

        try (Connection con = ConnectDB.getInstance().connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(tuNgay));
            ps.setDate(2, Date.valueOf(denNgay));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Format: "Tên nhân viên (X đơn)"
                    nhanVienBan = rs.getString("HoTen") + " (" + rs.getInt("SoDonHang") + " đơn)";
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy nhân viên bán nhiều nhất: " + e.getMessage());
            e.printStackTrace();
        }

        return nhanVienBan.isEmpty() ? "Không có dữ liệu" : nhanVienBan;
    }

    /**
     * Lấy danh sách top sản phẩm bán chạy nhất
     * @param tuNgay Ngày bắt đầu
     * @param denNgay Ngày kết thúc
     * @param top Số lượng sản phẩm top cần lấy (ví dụ: top 10)
     * @return Map với key=tên thuốc, value=số lượng bán
     */
    public Map<String, Integer> getTopSanPhamBanChay(LocalDate tuNgay, LocalDate denNgay, int top) {
        Map<String, Integer> topSanPham = new HashMap<>();
        // Query phức tạp: join 4 bảng để lấy tên thuốc và tổng số lượng bán
        // ChiTietHoaDon -> HoaDon -> ChiTietThuoc -> Thuoc
        String sql = String.format("""
            SELECT TOP %d t.TenThuoc, SUM(ct.SoLuong) as TongSoLuong
            FROM ChiTietHoaDon ct
            JOIN HoaDon h ON ct.MaHD = h.MaHD
            JOIN ChiTietThuoc ctt ON ct.MaCTT = ctt.MaCTT
            JOIN Thuoc t ON ctt.MaThuoc = t.MaThuoc
            WHERE h.NgayTao BETWEEN ? AND ? AND h.DeleteAt = 0 AND ct.TinhTrang = 'Bán'
            GROUP BY t.TenThuoc
            ORDER BY SUM(ct.SoLuong) DESC
        """, top); // Sử dụng String.format để chèn số top vào SQL

        try (Connection con = ConnectDB.getInstance().connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(tuNgay));
            ps.setDate(2, Date.valueOf(denNgay));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Thêm vào Map: tên thuốc -> số lượng bán
                    topSanPham.put(rs.getString("TenThuoc"), rs.getInt("TongSoLuong"));
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy top sản phẩm bán chạy: " + e.getMessage());
            e.printStackTrace();
        }

        return topSanPham;
    }
    /**
     * Lấy doanh thu theo từng tháng trong năm (dùng cho biểu đồ theo tháng)
     * @param nam Năm cần lấy thống kê (ví dụ: 2025)
     * @return Map với key=tên tháng, value=doanh thu tháng đó
     */
    public Map<String, Double> getDoanhThuTheoThang(int nam) {
        Map<String, Double> doanhThuTheoThang = new HashMap<>();
        String sql = """
            SELECT 
                MONTH(NgayTao) as Thang,
                SUM(TongTien) as DoanhThu
            FROM HoaDon 
            WHERE YEAR(NgayTao) = ? AND DeleteAt = 0
            GROUP BY MONTH(NgayTao)
            ORDER BY MONTH(NgayTao)
        """;

        try (Connection con = ConnectDB.getInstance().connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, nam);

            try (ResultSet rs = ps.executeQuery()) {
                String[] tenThang = {"", "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12"};
                while (rs.next()) {
                    int thang = rs.getInt("Thang");
                    double doanhThu = rs.getDouble("DoanhThu");
                    doanhThuTheoThang.put(tenThang[thang], doanhThu);
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy doanh thu theo tháng: " + e.getMessage());
            e.printStackTrace();
        }

        return doanhThuTheoThang;
    }

    /**
     * Lấy danh sách chi tiết doanh thu với thông tin đầy đủ cho bảng hiển thị
     * Bao gồm thông tin chi tiết hơn so với getDoanhThuTheoNgay
     * @param tuNgay Ngày bắt đầu
     * @param denNgay Ngày kết thúc
     * @param maNV Mã nhân viên (null nếu lấy tất cả)
     * @return Danh sách chi tiết thống kê doanh thu
     */
    public ArrayList<ThongKeDoanhThu> getDanhSachChiTietDoanhThu(LocalDate tuNgay, LocalDate denNgay, String maNV) {
        ArrayList<ThongKeDoanhThu> danhSach = new ArrayList<>();

        // Tạo SQL động dựa trên việc có lọc nhân viên hay không
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("""
            SELECT 
                h.NgayTao,
                COUNT(h.MaHD) as SoDonHang,
                SUM(h.TongTien) as DoanhThu,
                AVG(h.TongTien) as DonHangTB
            FROM HoaDon h
            LEFT JOIN NhanVien nv ON h.MaNV = nv.MaNV
            WHERE h.NgayTao BETWEEN ? AND ? 
            AND h.DeleteAt = 0 
            AND (nv.DeleteAt = 0 OR nv.DeleteAt IS NULL)
        """);

        // Thêm điều kiện lọc theo nhân viên nếu có
        if (maNV != null && !maNV.isEmpty() && !maNV.equals("Tất cả")) {
            sqlBuilder.append(" AND h.MaNV = ?");
        }

        sqlBuilder.append("""
            GROUP BY h.NgayTao
            ORDER BY h.NgayTao DESC
        """);

        try (Connection con = ConnectDB.getInstance().connect();
             PreparedStatement ps = con.prepareStatement(sqlBuilder.toString())) {

            // Set parameters
            ps.setDate(1, Date.valueOf(tuNgay));
            ps.setDate(2, Date.valueOf(denNgay));

            if (maNV != null && !maNV.isEmpty() && !maNV.equals("Tất cả")) {
                ps.setString(3, maNV);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate ngay = rs.getDate("NgayTao").toLocalDate();
                    int soDonHang = rs.getInt("SoDonHang");
                    double doanhThu = rs.getDouble("DoanhThu");
                    double donHangTB = rs.getDouble("DonHangTB");

                    // Lấy số khách hàng mới trong ngày này
                    int khachHangMoi = getSoKhachHangMoiTrongNgay(ngay);

                    // Lấy thông tin nhân viên cho ngày này
                    String nhanVienHienThi = getNhanVienTheoNgay(ngay, maNV);

                    // Tạo object ThongKeDoanhThu
                    ThongKeDoanhThu thongKe = new ThongKeDoanhThu(
                        ngay, soDonHang, doanhThu, donHangTB, khachHangMoi, nhanVienHienThi
                    );

                    danhSach.add(thongKe);
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy danh sách chi tiết doanh thu: " + e.getMessage());
            e.printStackTrace();
        }

        return danhSach;
    }

    /**
     * Lấy thông tin nhân viên làm việc trong một ngày cụ thể
     * @param ngay Ngày cần kiểm tra
     * @param maNV Mã nhân viên lọc (null nếu lấy tất cả)
     * @return String thông tin nhân viên
     */
    private String getNhanVienTheoNgay(LocalDate ngay, String maNV) {
        StringBuilder result = new StringBuilder();
        String sql = """
            SELECT nv.HoTen, COUNT(h.MaHD) as SoDonHang
            FROM HoaDon h
            LEFT JOIN NhanVien nv ON h.MaNV = nv.MaNV
            WHERE h.NgayTao = ? 
            AND h.DeleteAt = 0 
            AND (nv.DeleteAt = 0 OR nv.DeleteAt IS NULL)
        """;

        // Thêm điều kiện lọc theo nhân viên nếu có
        if (maNV != null && !maNV.isEmpty() && !maNV.equals("Tất cả")) {
            sql += " AND h.MaNV = ?";
        }

        sql += " GROUP BY nv.HoTen ORDER BY COUNT(h.MaHD) DESC";

        try (Connection con = ConnectDB.getInstance().connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(ngay));
            if (maNV != null && !maNV.isEmpty() && !maNV.equals("Tất cả")) {
                ps.setString(2, maNV);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String hoTen = rs.getString("HoTen");
                    int soDonHang = rs.getInt("SoDonHang");

                    if (result.length() > 0) {
                        result.append(", ");
                    }

                    if (hoTen != null && !hoTen.trim().isEmpty()) {
                        result.append(hoTen).append(" (").append(soDonHang).append(" đơn)");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy thông tin nhân viên theo ngày: " + e.getMessage());
        }

        return result.length() > 0 ? result.toString() : "Không có dữ liệu";
    }

    /**
     * Lấy số khách hàng mới trong một ngày cụ thể
     * @param ngay Ngày cần kiểm tra
     * @return Số khách hàng mới trong ngày
     */
    private int getSoKhachHangMoiTrongNgay(LocalDate ngay) {
        int soKhachHangMoi = 0;
        String sql = """
            SELECT COUNT(DISTINCT h.MaKH) as SoKhachHangMoi 
            FROM HoaDon h
            WHERE h.NgayTao = ? 
            AND h.DeleteAt = 0
            AND h.MaKH IN (
                SELECT MaKH FROM HoaDon 
                WHERE DeleteAt = 0
                GROUP BY MaKH 
                HAVING MIN(NgayTao) = ?
            )
        """;

        try (Connection con = ConnectDB.getInstance().connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(ngay));
            ps.setDate(2, Date.valueOf(ngay));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    soKhachHangMoi = rs.getInt("SoKhachHangMoi");
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy số khách hàng mới trong ngày: " + e.getMessage());
        }

        return soKhachHangMoi;
    }
}
