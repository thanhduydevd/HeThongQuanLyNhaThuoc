/*
 * @ (#) ChiTietPhieuDatThuoc.java   1.0 9/25/2025
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
public class ChiTietPhieuDatThuoc {
    private PhieuDatThuoc maPhieu;
    private Thuoc soDangKy;
    private int soLuong;
    private DonViTinh donViTinh;

    public ChiTietPhieuDatThuoc() {
        this.maPhieu = new PhieuDatThuoc();
        this.soDangKy = new Thuoc();
        this.soLuong = 0;
        this.donViTinh = new DonViTinh();
    }

    public ChiTietPhieuDatThuoc(PhieuDatThuoc maPhieu, Thuoc soDangKy, int soLuong, DonViTinh donViTinh) {
        this.maPhieu = maPhieu;
        this.soDangKy = soDangKy;
        setSoLuong(soLuong);
        this.donViTinh = donViTinh;
    }

    public PhieuDatThuoc getMaPhieu() {
        return maPhieu;
    }
    public void setMaPhieu(PhieuDatThuoc maPhieu) {
        this.maPhieu = maPhieu;
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
        if (soLuong < 0) {
            throw new IllegalArgumentException("Số lượng không được âm");
        }
        this.soLuong = soLuong;
    }
    public DonViTinh getDonViTinh() {
        return donViTinh;
    }
    public void setDonViTinh(DonViTinh donViTinh) {
        this.donViTinh = donViTinh;
    }
    public double tinhThanhTien() {
        return soLuong * soDangKy.getGiaBan();
    }

    @Override
    public String toString() {
        return "ChiTietPhieuDatThuoc{" +
                "maPhieu=" + maPhieu +
                ", soDangKy=" + soDangKy +
                ", soLuong=" + soLuong +
                ", donViTinh=" + donViTinh +
                '}';
    }
}
