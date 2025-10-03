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
    private double tongTien;
    private boolean deleteAt;

    public PhieuDatThuoc() {
        this.maPhieu = "";
        this.ngayTao = LocalDate.now();
        this.isThanhToan = false;
        this.maNV = new NhanVien();
        this.maKH = new KhachHang();
        this.maKM = new KhuyenMai();
        this.tongTien = 0;
        this.deleteAt = false;
    }

    public PhieuDatThuoc(String maPhieu, LocalDate ngayTao, boolean isThanhToan, NhanVien maNV, KhachHang maKH, KhuyenMai maKM, boolean deleteAt) {
        this.maPhieu = maPhieu;
        setNgayTao(ngayTao);
        this.isThanhToan = isThanhToan;
        this.maNV = maNV;
        this.maKH = maKH;
        this.maKM = maKM;
        this.tongTien = 0;
        this.deleteAt = deleteAt;
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
    public double getTongTien() {
        return tongTien;
    }
    public void setTongTien(double tongTien) {
        if (tongTien < 0) {
            throw new IllegalArgumentException("Tổng tiền không được âm");
        }
        this.tongTien = tongTien;
    }
    public boolean isDeleteAt() {
        return deleteAt;
    }
    public void setDeleteAt(boolean deleteAt) {
        this.deleteAt = deleteAt;
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
                ", tongTien=" + tongTien +
                ", deleteAt=" + deleteAt +
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
