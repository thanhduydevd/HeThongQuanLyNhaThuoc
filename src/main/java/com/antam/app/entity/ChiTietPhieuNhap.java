/*
 * @ (#) ChiTietPhieuNhap.java   1.0 9/25/2025
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
public class ChiTietPhieuNhap {
    private HoaDon MaPN;
    private Thuoc soDangKy;
    private DonViTinh maDVT;
    private int soLuong;
    private double giaNhap;

    public ChiTietPhieuNhap() {
        this.MaPN = new HoaDon();
        this.soDangKy = new Thuoc();
        this.maDVT = new DonViTinh();
        this.soLuong = 0;
        this.giaNhap = 0;
    }

    public ChiTietPhieuNhap(HoaDon maPN, Thuoc soDangKy, DonViTinh maDVT, int soLuong, double giaNhap) {
        this.MaPN = maPN;
        this.soDangKy = soDangKy;
        this.maDVT = maDVT;
        setSoLuong(soLuong);
        setGiaNhap(giaNhap);
    }

    public HoaDon getMaPN() {
        return MaPN;
    }

    public void setMaPN(HoaDon maPN) {
        MaPN = maPN;
    }

    public Thuoc getSoDangKy() {
        return soDangKy;
    }

    public void setSoDangKy(Thuoc soDangKy) {
        this.soDangKy = soDangKy;
    }

    public DonViTinh getMaDVT() {
        return maDVT;
    }

    public void setMaDVT(DonViTinh maDVT) {
        this.maDVT = maDVT;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        if (soLuong < 0) {
            throw new IllegalArgumentException("Số lượng không được âm");
        }
        this.soLuong = soLuong;
    }

    public double getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(double giaNhap) {
        if (giaNhap < 0) {
            throw new IllegalArgumentException("Giá nhập không được âm");
        }
        this.giaNhap = giaNhap;
    }

    public double thanhTien() {
        return soLuong * giaNhap;
    }
    @Override
    public String toString() {
        return "ChiTietPhieuNhap{" +
                "MaPN=" + MaPN +
                ", soDangKy=" + soDangKy +
                ", maDVT=" + maDVT +
                ", soLuong=" + soLuong +
                ", giaNhap=" + giaNhap +
                '}';
    }
}
