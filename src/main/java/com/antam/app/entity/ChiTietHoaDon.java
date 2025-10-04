/*
 * @ (#) ChiTietHoaDon.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.entity;

import com.antam.app.controller.dialog.ThemThuocController;

import static com.antam.app.controller.dialog.ThemThuocController.quyDoiVeCoSo;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 9/25/2025
 * version: 1.0
 */
public class ChiTietHoaDon {
    private HoaDon MaHD;
    private Thuoc maThuoc;
    private int soLuong;
    private DonViTinh maDVT;
    private String tinhTrang;
    private double thanhTien;

    public ChiTietHoaDon() {
        this.MaHD = new HoaDon();
        this.maThuoc = new Thuoc();
        this.soLuong = 0;
        this.maDVT = new DonViTinh();
        this.tinhTrang = "";
        this.thanhTien = 0;
    }
    public ChiTietHoaDon(HoaDon maHD, Thuoc maThuoc, int soLuong, DonViTinh maDVT, String tinhTrang) {
        this.MaHD = maHD;
        this.maThuoc = maThuoc;
        setSoLuong(soLuong);
        this.maDVT = maDVT;
        setTinhTrang(tinhTrang);
        setThanhTien();
    }
    public HoaDon getMaHD() {
        return MaHD;
    }
    public void setMaHD(HoaDon maHD) {
        MaHD = maHD;
    }
    public Thuoc getMaThuoc() {
        return maThuoc;
    }
    public void setMaThuoc(Thuoc soDangKy) {
        this.maThuoc = soDangKy;
    }
    public int getSoLuong() {
        return soLuong;
    }
    public void setSoLuong(int soLuong) {
        if (soLuong <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
        this.soLuong = soLuong;
        setThanhTien();
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
    public void setThanhTien() {
        this.thanhTien = tinhThanhTien();
    }
    public double getThanhTien() {
        return thanhTien;
    }
    public double tinhThanhTien() {
        double giaBan = maThuoc.getGiaBan();
        float thue = maThuoc.getThue();
        int soLuongCoSo = quyDoiVeCoSo(maThuoc.getMaThuoc(), soLuong, maDVT.getMaDVT());
        return soLuongCoSo  * giaBan * (1 + thue);
    }
    @Override
    public String toString() {
        return "ChiTietHoaDon{" +
                "MaHD=" + MaHD +
                ", soDangKy=" + maThuoc +
                ", soLuong=" + soLuong +
                ", maDVT=" + maDVT +
                ", tinhTrang='" + tinhTrang + '\'' +
                '}';
    }

}
