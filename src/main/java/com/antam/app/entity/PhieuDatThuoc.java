/*
 * @ (#) PhieuDatThuoc.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.entity;

import java.time.LocalDate;
import java.util.Objects;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 9/25/2025
 * version: 1.0
 */
public class PhieuDatThuoc {
    private final String maPhieu;
    private LocalDate ngayTao;
    private boolean isThanhToan;
    private NhanVien maNV;
    private KhachHang maKH;
    private KhuyenMai maKM;

    public PhieuDatThuoc() {
        this.maPhieu = "";
        this.ngayTao = LocalDate.now();
        this.isThanhToan = false;
        this.maNV = new NhanVien();
        this.maKH = new KhachHang();
        this.maKM = new KhuyenMai();
    }

    public PhieuDatThuoc(String maPhieu, LocalDate ngayTao, boolean isThanhToan, NhanVien maNV, KhachHang maKH, KhuyenMai maKM) {
        this.maPhieu = maPhieu;
        setNgayTao(ngayTao);
        this.isThanhToan = isThanhToan;
        this.maNV = maNV;
        this.maKH = maKH;
        this.maKM = maKM;
    }

    public String getMaPhieu() {
        return maPhieu;
    }

    public LocalDate getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDate ngayTao) {
        if (ngayTao.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày tạo không được sau ngày hiện tại");
        }
        this.ngayTao = ngayTao;
    }

    public boolean isThanhToan() {
        return isThanhToan;
    }

    public void setThanhToan(boolean isThanhToan) {
        this.isThanhToan = isThanhToan;
    }

    public NhanVien getMaNV() {
        return maNV;
    }

    public void setMaNV(NhanVien maNV) {
        this.maNV = maNV;
    }

    public KhachHang getMaKH() {
        return maKH;
    }

    public void setMaKH(KhachHang maKH) {
        this.maKH = maKH;
    }

    public KhuyenMai getMaKM() {
        return maKM;
    }

    public void setMaKM(KhuyenMai maKM) {
        this.maKM = maKM;
    }

    @Override
    public String toString() {
        return "PhieuDatThuoc{" +
                "maPhieu='" + maPhieu + '\'' +
                ", ngayTao=" + ngayTao +
                ", isThanhToan=" + isThanhToan +
                ", maNV=" + maNV +
                ", maKH=" + maKH +
                ", maKM=" + maKM +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhieuDatThuoc that = (PhieuDatThuoc) o;
        return Objects.equals(maPhieu, that.maPhieu);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maPhieu);
    }
}
