/*
 * @ (#) Thuoc.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.entity;

import java.time.LocalDate;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 9/25/2025
 * version: 1.0
 */
public class Thuoc {
    private final String SoDangKy;
    private String TenThuoc;
    private LocalDate HanSuDung;
    private LocalDate NgaySanXuat;
    private int tonKho;
    private String hamLuong;
    private double giaBan;
    private double giaGoc;
    private float thue;
    private DangDieuChe dangDieuChe;
    private DonViTinh maDVTCoSo;
    private Ke maKe;

    public Thuoc() {
        SoDangKy = "";
        TenThuoc = "";
        HanSuDung = LocalDate.now();
        NgaySanXuat = LocalDate.now();
        tonKho = 0;
        hamLuong = "";
        giaBan = 0;
        giaGoc = 0;
        thue = 0;
        dangDieuChe = new DangDieuChe();
        maDVTCoSo = new DonViTinh();
        maKe = new Ke();
    }
    public Thuoc(String soDangKy, String tenThuoc, LocalDate hanSuDung, LocalDate ngaySanXuat, int tonKho, String hamLuong, double giaBan, double giaGoc, float thue, DangDieuChe dangDieuChe, DonViTinh maDVTCoSo, Ke maKe) {
        SoDangKy = soDangKy;
        setTenThuoc(tenThuoc);
        setHanSuDung(hanSuDung);
        setNgaySanXuat(ngaySanXuat);
        setTonKho(tonKho);
        setHamLuong(hamLuong);
        setGiaBan(giaBan);
        setGiaGoc(giaGoc);
        setThue(thue);
        this.dangDieuChe = dangDieuChe;
        this.maDVTCoSo = maDVTCoSo;
        this.maKe = maKe;
    }
    public String getSoDangKy() {
        return SoDangKy;
    }
    public String getTenThuoc() {
        return TenThuoc;
    }
    public void setTenThuoc(String tenThuoc) {
        if (tenThuoc == null || tenThuoc.isEmpty()) {
            throw new IllegalArgumentException("Tên thuốc không được để trống");
        }
        TenThuoc = tenThuoc;
    }
    public LocalDate getHanSuDung() {
        return HanSuDung;
    }
    public void setHanSuDung(LocalDate hanSuDung) {
        if (hanSuDung.isBefore(NgaySanXuat)) {
            throw new IllegalArgumentException("Hạn sử dụng phải sau ngày sản xuất");
        }
        HanSuDung = hanSuDung;
    }
    public LocalDate getNgaySanXuat() {
        return NgaySanXuat;
    }
    public void setNgaySanXuat(LocalDate ngaySanXuat) {
        if (ngaySanXuat.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày sản xuất phải trước hạn sử dụng");
        }
        NgaySanXuat = ngaySanXuat;
    }
    public int getTonKho() {
        return tonKho;
    }
    public void setTonKho(int tonKho) {
        if (tonKho < 0) {
            throw new IllegalArgumentException("Tồn kho không được âm");
        }
        this.tonKho = tonKho;
    }
    public String getHamLuong() {
        return hamLuong;
    }
    public void setHamLuong(String hamLuong) {
        if (hamLuong == null || hamLuong.isEmpty()) {
            throw new IllegalArgumentException("Hàm lượng không được để trống");
        }
        this.hamLuong = hamLuong;
    }
    public double getGiaBan() {
        return giaBan;
    }
    public void setGiaBan(double giaBan) {
        if (giaBan <= 0) {
            throw new IllegalArgumentException("Giá bán không được âm");
        }
        this.giaBan = giaBan;
    }
    public double getGiaGoc() {
        return giaGoc;
    }
    public void setGiaGoc(double giaGoc) {
        if (giaGoc <= 0) {
            throw new IllegalArgumentException("Giá gốc không được âm");
        }
        this.giaGoc = giaGoc;
    }
    public float getThue() {
        return thue;
    }
    public void setThue(float thue) {
        if (thue < 0) {
            throw new IllegalArgumentException("Thuế không được âm");
        }
        this.thue = thue;
    }
    public DangDieuChe getDangDieuChe() {
        return dangDieuChe;
    }
    public void setDangDieuChe(DangDieuChe dangDieuChe) {
        this.dangDieuChe = dangDieuChe;
    }
    public DonViTinh getMaDVTCoSo() {
        return maDVTCoSo;
    }
    public void setMaDVTCoSo(DonViTinh maDVTCoSo) {
        this.maDVTCoSo = maDVTCoSo;
    }
    public Ke getMaKe() {
        return maKe;
    }
    public void setMaKe(Ke maKe) {
        this.maKe = maKe;
    }
    @Override
    public String toString() {
        return "Thuoc{" +
                "SoDangKy='" + SoDangKy + '\'' +
                ", TenThuoc='" + TenThuoc + '\'' +
                ", HanSuDung=" + HanSuDung +
                ", NgaySanXuat=" + NgaySanXuat +
                ", tonKho=" + tonKho +
                ", hamLuong='" + hamLuong + '\'' +
                ", giaBan=" + giaBan +
                ", giaGoc=" + giaGoc +
                ", thue=" + thue +
                ", dangDieuChe=" + dangDieuChe +
                ", maDVTCoSo=" + maDVTCoSo +
                ", maKe=" + maKe +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Thuoc thuoc = (Thuoc) o;
        return SoDangKy.equals(thuoc.SoDangKy);
    }
}
