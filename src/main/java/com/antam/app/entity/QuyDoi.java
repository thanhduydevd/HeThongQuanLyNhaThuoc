/*
 * @ (#) QuyDoi.java   1.0 9/25/2025
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
public class QuyDoi {
    private Thuoc soDangKy;
    private DonViTinh maDVTCha;
    private DonViTinh maDVTCon;
    private float tiLeQuyDoi;

    public QuyDoi() {
        soDangKy = new Thuoc();
        maDVTCha = new DonViTinh();
        maDVTCon = new DonViTinh();
        tiLeQuyDoi = 0;
    }
    public QuyDoi(Thuoc soDangKy, DonViTinh maDVTCha, DonViTinh maDVTCon, float tiLeQuyDoi) {
        this.soDangKy = soDangKy;
        this.maDVTCha = maDVTCha;
        this.maDVTCon = maDVTCon;
        setTiLeQuyDoi(tiLeQuyDoi);
    }
    public Thuoc getSoDangKy() {
        return soDangKy;
    }
    public void setSoDangKy(Thuoc soDangKy) {
        this.soDangKy = soDangKy;
    }
    public DonViTinh getMaDVTCha() {
        return maDVTCha;
    }
    public void setMaDVTCha(DonViTinh maDVTCha) {
        this.maDVTCha = maDVTCha;
    }
    public DonViTinh getMaDVTCon() {
        return maDVTCon;
    }
    public void setMaDVTCon(DonViTinh maDVTCon) {
        this.maDVTCon = maDVTCon;
    }
    public float getTiLeQuyDoi() {
        return tiLeQuyDoi;
    }
    public void setTiLeQuyDoi(float tiLeQuyDoi) {
        if (tiLeQuyDoi <= 0) {
            throw new IllegalArgumentException("Tỉ lệ quy đổi phải lớn hơn 0");
        }
        this.tiLeQuyDoi = tiLeQuyDoi;
    }
    @Override
    public String toString() {
        return "QuyDoi{" +
                "soDangKy=" + soDangKy +
                ", maDVTCha=" + maDVTCha +
                ", maDVTCon=" + maDVTCon +
                ", tiLeQuyDoi=" + tiLeQuyDoi +
                '}';
    }

}
