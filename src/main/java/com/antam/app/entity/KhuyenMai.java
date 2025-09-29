/*
 * @ (#) KhuyenMai.java   1.0 9/25/2025
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
public class KhuyenMai {
    private final String MaKM;
    private String TenKM;
    private LocalDate NgayBatDau;
    private LocalDate NgayKetThuc;
    private LoaiKhuyenMai loaiKhuyenMai;
    private double so;
    private int soLuongToiDa;

    public KhuyenMai() {
        MaKM = "";
        TenKM = "";
        NgayBatDau = LocalDate.now();
        NgayKetThuc = LocalDate.now();
        loaiKhuyenMai = new LoaiKhuyenMai();
        so = 0;
        soLuongToiDa = 0;
    }
    public KhuyenMai(String maKM, String tenKM, LocalDate ngayBatDau, LocalDate ngayKetThuc, LoaiKhuyenMai loaiKhuyenMai, double so, int soLuongToiDa) {
        this.MaKM = maKM;
        setTenKM(tenKM);
        setNgayBatDau(ngayBatDau);
        setNgayKetThuc(ngayKetThuc);
        this.loaiKhuyenMai = loaiKhuyenMai;
        setSo(so);
        setSoLuongToiDa(soLuongToiDa);
    }
    public String getMaKM() {
        return MaKM;
    }
    public String getTenKM() {
        return TenKM;
    }
    public void setTenKM(String tenKM) {
        if (tenKM == null || tenKM.isEmpty()) {
            throw new IllegalArgumentException("Tên khuyến mãi không được để trống");
        }
        TenKM = tenKM;
    }
    public LocalDate getNgayBatDau() {
        return NgayBatDau;
    }
    public void setNgayBatDau(LocalDate ngayBatDau) {
        if (ngayBatDau.isAfter(NgayKetThuc)) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc");
        }
        NgayBatDau = ngayBatDau;
    }
    public LocalDate getNgayKetThuc() {
        return NgayKetThuc;
    }
    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        if (ngayKetThuc.isBefore(NgayBatDau)) {
            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");
        }
        NgayKetThuc = ngayKetThuc;
    }
    public LoaiKhuyenMai getLoaiKhuyenMai() {
        return loaiKhuyenMai;
    }
    public void setLoaiKhuyenMai(LoaiKhuyenMai loaiKhuyenMai) {
        this.loaiKhuyenMai = loaiKhuyenMai;
    }
    public double getSo() {
        return so;
    }
    public void setSo(double so) {
        if (so < 0) {
            throw new IllegalArgumentException("Giá trị khuyến mãi không được âm");
        }
        this.so = so;
    }
    public int getSoLuongToiDa() {
        return soLuongToiDa;
    }
    public void setSoLuongToiDa(int soLuongToiDa) {
        if (soLuongToiDa < 0) {
            throw new IllegalArgumentException("Số lượng tối đa không được âm");
        }
        this.soLuongToiDa = soLuongToiDa;
    }
    @Override
    public String toString() {
        return "KhuyenMai{" +
                "MaKM='" + MaKM + '\'' +
                ", TenKM='" + TenKM + '\'' +
                ", NgayBatDau=" + NgayBatDau +
                ", NgayKetThuc=" + NgayKetThuc +
                ", loaiKhuyenMai=" + loaiKhuyenMai +
                ", so=" + so +
                ", soLuongToiDa=" + soLuongToiDa +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KhuyenMai khuyenMai = (KhuyenMai) o;
        return MaKM.equals(khuyenMai.MaKM);
    }
}
