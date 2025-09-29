/*
 * @ (#) HoaDon.java   1.0 9/25/2025
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
public class HoaDon {
    private final String MaHD;
    private LocalDate ngayTao;
    private NhanVien maNV;
    private KhachHang maKH;
    private KhuyenMai maKM;

    public HoaDon() {
        MaHD = "";
        ngayTao = LocalDate.now();
        maNV = new NhanVien();
        maKH = new KhachHang();
        maKM = null;
    }
    public HoaDon(String maHD, LocalDate ngayTao, NhanVien maNV, KhachHang maKH, KhuyenMai maKM) {
        MaHD = maHD;
        setNgayTao(ngayTao);
        this.maNV = maNV;
        this.maKH = maKH;
        this.maKM = maKM;
    }
    public String getMaHD() {
        return MaHD;
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
        return "HoaDon{" +
                "MaHD='" + MaHD + '\'' +
                ", ngayTao=" + ngayTao +
                ", maNV=" + maNV +
                ", maKH=" + maKH +
                ", maKM=" + maKM +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HoaDon hoaDon = (HoaDon) o;
        return Objects.equals(MaHD, hoaDon.MaHD);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(MaHD);
    }
}
