/*
 * @ (#) ChiTietPhieuNhap.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.entity;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 9/25/2025
 * version: 1.0
 */
public class ChiTietPhieuNhap {
    private HoaDon MaPN;
    private Thuoc maThuoc;
    private DonViTinh maDVT;
    private int soLuong;
    private double giaNhap;
    private double thanhTien;

    public ChiTietPhieuNhap() {
        this.MaPN = new HoaDon();
        this.maThuoc = new Thuoc();
        this.maDVT = new DonViTinh();
        this.soLuong = 0;
        this.giaNhap = 0;
        this.thanhTien = 0;
    }

    public ChiTietPhieuNhap(HoaDon maPN, Thuoc maThuoc, DonViTinh maDVT, int soLuong, double giaNhap) {
        this.MaPN = maPN;
        this.maThuoc = maThuoc;
        this.maDVT = maDVT;
        setSoLuong(soLuong);
        setGiaNhap(giaNhap);
        setThanhTien();
    }

    public ChiTietPhieuNhap(HoaDon maPN, Thuoc maThuoc, DonViTinh maDVT, int soLuong, double giaNhap, double thanhTien) {
        this.MaPN = maPN;
        this.maThuoc = maThuoc;
        this.maDVT = maDVT;
        setSoLuong(soLuong);
        setGiaNhap(giaNhap);
        this.thanhTien = thanhTien;
    }

    public HoaDon getMaPN() {
        return MaPN;
    }

    public void setMaPN(HoaDon maPN) {
        MaPN = maPN;
    }

    public Thuoc getSoDangKy() {
        return maThuoc;
    }

    public void setSoDangKy(Thuoc soDangKy) {
        this.maThuoc = soDangKy;
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
        setThanhTien();
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

    public void setThanhTien() {
        this.thanhTien = thanhTien();
    }


    public double thanhTien() {
        double giaBan = maThuoc.getGiaBan();
        float thue = maThuoc.getThue();
        return soLuong * giaBan * (1 + thue);
    }
    @Override
    public String toString() {
        return "ChiTietPhieuNhap{" +
                "MaPN=" + MaPN +
                ", soDangKy=" + maThuoc +
                ", maDVT=" + maDVT +
                ", soLuong=" + soLuong +
                ", giaNhap=" + giaNhap +
                '}';
    }
}
