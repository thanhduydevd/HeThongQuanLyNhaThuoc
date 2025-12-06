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
    private Thuoc maThuoc;
    private int soLuong;
    private DonViTinh donViTinh;
    private double thanhTien;
    private boolean isThanhToan;

    public ChiTietPhieuDatThuoc() {
        this.maPhieu = new PhieuDatThuoc();
        this.maThuoc = new Thuoc();
        this.soLuong = 0;
        this.donViTinh = new DonViTinh();
        this.thanhTien = 0;
    }

    public ChiTietPhieuDatThuoc(PhieuDatThuoc maPhieu, Thuoc maThuoc, int soLuong, DonViTinh donViTinh) {
        this.maPhieu = maPhieu;
        this.maThuoc = maThuoc;
        setSoLuong(soLuong);
        this.donViTinh = donViTinh;
        setThanhTien();
    }

    //Constructor cho lúc khởi tạo mẫu chi tiết phiếu đặt
    public ChiTietPhieuDatThuoc(Thuoc thuoc, int soLuong, DonViTinh dvt){
        this.maPhieu = new PhieuDatThuoc();
        this.maThuoc = thuoc;
        this.soLuong = soLuong;
        this.donViTinh = dvt;
        this.thanhTien = soLuong * thuoc.getGiaBan() * (1 + thuoc.getThue());
    }

    public ChiTietPhieuDatThuoc(PhieuDatThuoc maPhieu, Thuoc maThuoc, int soLuong, DonViTinh donViTinh, double thanhTien) {
        this.maPhieu = maPhieu;
        this.maThuoc = maThuoc;
        setSoLuong(soLuong);
        this.donViTinh = donViTinh;
        this.thanhTien = thanhTien;
    }

    public PhieuDatThuoc getMaPhieu() {
        return maPhieu;
    }
    public void setMaPhieu(PhieuDatThuoc maPhieu) {
        this.maPhieu = maPhieu;
    }
    public Thuoc getSoDangKy() {
        return maThuoc;
    }
    public void setSoDangKy(Thuoc soDangKy) {
        this.maThuoc = soDangKy;
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
    public DonViTinh getDonViTinh() {
        return donViTinh;
    }
    public void setDonViTinh(DonViTinh donViTinh) {
        this.donViTinh = donViTinh;
    }
    public double getThanhTien() {
        return thanhTien;
    }
    public void setThanhTien() {
        this.thanhTien = tinhThanhTien();
    }
    public double tinhThanhTien() {
        double giaBan = maThuoc.getGiaBan();
        float thue = maThuoc.getThue();
        return soLuong * giaBan * (1 + thue);
    }

    @Override
    public String toString() {
        return "ChiTietPhieuDatThuoc{" +
                "maPhieu=" + maPhieu +
                ", soDangKy=" + maThuoc +
                ", soLuong=" + soLuong +
                ", donViTinh=" + donViTinh +
                '}';
    }

    public boolean isThanhToan() {
        return isThanhToan;
    }

    public void setThanhToan(boolean thanhToan) {
        isThanhToan = thanhToan;
    }
}
