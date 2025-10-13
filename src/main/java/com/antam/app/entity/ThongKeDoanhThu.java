/*
 * @ (#) ThongKeDoanhThu.java   1.0 13/10/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.antam.app.entity;

import java.time.LocalDate;

/*
 * @description Entity cho thống kê doanh thu
 * @author: Tran Tuan Hung
 * @date: 13/10/25
 * @version: 1.0
 */
public class ThongKeDoanhThu {
    private LocalDate ngay;
    private int soDonHang;
    private double doanhThu;
    private double donHangTrungBinh;
    private int khachHangMoi;
    private String nhanVienBan;
    private double tyLeThayDoi;

    public ThongKeDoanhThu() {}

    public ThongKeDoanhThu(LocalDate ngay, int soDonHang, double doanhThu,
                          double donHangTrungBinh, int khachHangMoi, String nhanVienBan) {
        this.ngay = ngay;
        this.soDonHang = soDonHang;
        this.doanhThu = doanhThu;
        this.donHangTrungBinh = donHangTrungBinh;
        this.khachHangMoi = khachHangMoi;
        this.nhanVienBan = nhanVienBan;
    }

    // Getters and Setters
    public LocalDate getNgay() { return ngay; }
    public void setNgay(LocalDate ngay) { this.ngay = ngay; }

    public int getSoDonHang() { return soDonHang; }
    public void setSoDonHang(int soDonHang) { this.soDonHang = soDonHang; }

    public double getDoanhThu() { return doanhThu; }
    public void setDoanhThu(double doanhThu) { this.doanhThu = doanhThu; }

    public double getDonHangTrungBinh() { return donHangTrungBinh; }
    public void setDonHangTrungBinh(double donHangTrungBinh) { this.donHangTrungBinh = donHangTrungBinh; }

    public int getKhachHangMoi() { return khachHangMoi; }
    public void setKhachHangMoi(int khachHangMoi) { this.khachHangMoi = khachHangMoi; }

    public String getNhanVienBan() { return nhanVienBan; }
    public void setNhanVienBan(String nhanVienBan) { this.nhanVienBan = nhanVienBan; }

    public double getTyLeThayDoi() { return tyLeThayDoi; }
    public void setTyLeThayDoi(double tyLeThayDoi) { this.tyLeThayDoi = tyLeThayDoi; }
}
