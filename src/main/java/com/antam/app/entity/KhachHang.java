/*
 * @ (#) KhachHang.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.entity;

import java.util.Objects;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 9/25/2025
 * version: 1.0
 */
public class KhachHang {
    private final String MaKH;
    private String tenKH;
    private String soDienThoai;
    private boolean deleteAt;

    public KhachHang() {
        MaKH = "";
        tenKH = "";
        soDienThoai = "";
        deleteAt = false;
    }
    public KhachHang(String maKH) {
        MaKH = maKH;
        tenKH = "";
        soDienThoai = "";
        deleteAt = false;
    }
    public KhachHang(String maKH, String tenKH, String soDienThoai, boolean deleteAt) {
        MaKH = maKH;
        setTenKH(tenKH);
        setSoDienThoai(soDienThoai);
        this.deleteAt = deleteAt;
    }
    public KhachHang(String maKH) {
        this.MaKH = maKH;
        this.tenKH = "";
        this.soDienThoai = "";
        this.deleteAt = false;
    }
    public String getMaKH() {
        return MaKH;
    }
    public String getTenKH() {
        return tenKH;
    }
    public void setTenKH(String tenKH) {
        if (tenKH == null || tenKH.isEmpty()) {
            throw new IllegalArgumentException("Tên khách hàng không được để trống");
        }
        this.tenKH = tenKH;
    }
    public String getSoDienThoai() {
        return soDienThoai;
    }
    public void setSoDienThoai(String soDienThoai) {
        if (soDienThoai == null || soDienThoai.isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống");
        } else if (!soDienThoai.matches("^0[35679]\\d{8}$")) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        }

        this.soDienThoai = soDienThoai;
    }
    public boolean isDeleteAt() {
        return deleteAt;
    }
    public void setDeleteAt(boolean deleteAt) {
        this.deleteAt = deleteAt;
    }
    @Override
    public String toString() {
        return "KhachHang{" +
                "MaKH='" + MaKH + '\'' +
                ", tenKH='" + tenKH + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", deleteAt=" + deleteAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KhachHang khachHang = (KhachHang) o;
        return Objects.equals(MaKH, khachHang.MaKH);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(MaKH);
    }
}
