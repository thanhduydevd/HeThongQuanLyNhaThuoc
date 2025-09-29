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
    private Thuoc soDangKy;
    private int soLuong;
    private DonViTinh maDVT;
    private String tinhTrang;

    public ChiTietHoaDon() {
        this.MaHD = new HoaDon();
        this.soDangKy = new Thuoc();
        this.soLuong = 0;
        this.maDVT = new DonViTinh();
        this.tinhTrang = "";
    }
    public ChiTietHoaDon(HoaDon maHD, Thuoc soDangKy, int soLuong, DonViTinh maDVT, String tinhTrang) {
        this.MaHD = maHD;
        this.soDangKy = soDangKy;
        setSoLuong(soLuong);
        this.maDVT = maDVT;
        setTinhTrang(tinhTrang);
    }
    public HoaDon getMaHD() {
        return MaHD;
    }
    public void setMaHD(HoaDon maHD) {
        MaHD = maHD;
    }
    public Thuoc getSoDangKy() {
        return soDangKy;
    }
    public void setSoDangKy(Thuoc soDangKy) {
        this.soDangKy = soDangKy;
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
    public double tinhThanhTien() {
        double giaBan = soDangKy.getGiaBan();
        float thue = soDangKy.getThue();
        return soLuong * giaBan * (1 + thue);
    }
    @Override
    public String toString() {
        return "ChiTietHoaDon{" +
                "MaHD=" + MaHD +
                ", soDangKy=" + soDangKy +
                ", soLuong=" + soLuong +
                ", maDVT=" + maDVT +
                ", tinhTrang='" + tinhTrang + '\'' +
                '}';
    }

}
