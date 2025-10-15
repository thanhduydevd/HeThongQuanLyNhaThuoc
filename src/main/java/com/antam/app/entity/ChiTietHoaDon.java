/*
 * @ (#) ChiTietHoaDon.java   1.0 9/25/2025
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
public class ChiTietHoaDon {
    private HoaDon MaHD;
    private ChiTietThuoc maCTT;
    private int soLuong;
    private DonViTinh maDVT;
    private String tinhTrang;
    private double thanhTien;

    public ChiTietHoaDon() {
        this.MaHD = new HoaDon();
        this.maCTT = new ChiTietThuoc();
        this.soLuong = 0;
        this.maDVT = new DonViTinh();
        this.tinhTrang = "";
        this.thanhTien = 0;
    }

    public ChiTietHoaDon(HoaDon maHD, ChiTietThuoc maCTT, int soLuong, DonViTinh maDVT, String tinhTrang, double thanhTien) {
        this.MaHD = maHD;
        this.maCTT = maCTT;
        setSoLuong(soLuong);
        this.maDVT = maDVT;
        setTinhTrang(tinhTrang);
        this.thanhTien = thanhTien;
    }
    public HoaDon getMaHD() {
        return MaHD;
    }
    public void setMaHD(HoaDon maHD) {
        MaHD = maHD;
    }

    public ChiTietThuoc getMaCTT() {
        return maCTT;
    }
    public void setMaCTT(ChiTietThuoc maCTT) {
        this.maCTT = maCTT;
    }


    public int getSoLuong() {
        return soLuong;
    }
    public void setSoLuong(int soLuong) {
        if (soLuong <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
        this.soLuong = soLuong;
    }
    public DonViTinh getMaDVT() {
        return maDVT;
    }
    public void setMaDVT(DonViTinh maDVT) {
        this.maDVT = maDVT;
    }
    public String getTinhTrang() {
        return tinhTrang;
    }
    public void setTinhTrang(String tinhTrang) {
        if (tinhTrang == null || tinhTrang.isEmpty()) {
            throw new IllegalArgumentException("Tình trạng không được để trống");
        }
        this.tinhTrang = tinhTrang;
    }
    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }
    public double getThanhTien() {
        return thanhTien;
    }
    @Override
    public String toString() {
        return "ChiTietHoaDon{" +
                "MaHD=" + MaHD +
                ", MaCTT=" + maCTT +
                ", soLuong=" + soLuong +
                ", maDVT=" + maDVT +
                ", tinhTrang='" + tinhTrang + '\'' +
                ", thanhTien=" + thanhTien +
                '}';
    }

}