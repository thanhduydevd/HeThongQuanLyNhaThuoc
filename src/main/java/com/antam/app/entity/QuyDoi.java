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
    private Thuoc maThuoc;
    private DonViTinh maDVTCha;
    private DonViTinh maDVTCon;
    private int tiLeQuyDoi;

    public QuyDoi() {
        maThuoc = new Thuoc();
        maDVTCha = new DonViTinh();
        maDVTCon = new DonViTinh();
        tiLeQuyDoi = 0;
    }
    public QuyDoi(Thuoc soDangKy, DonViTinh maDVTCha, DonViTinh maDVTCon, int tiLeQuyDoi) {
        this.maThuoc = soDangKy;
        this.maDVTCha = maDVTCha;
        this.maDVTCon = maDVTCon;
        setTiLeQuyDoi(tiLeQuyDoi);
    }
    public Thuoc getMaThuoc() {
        return maThuoc;
    }
    public void setMaThuoc(Thuoc soDangKy) {
        this.maThuoc = soDangKy;
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
    public int getTiLeQuyDoi() {
        return tiLeQuyDoi;
    }
    public void setTiLeQuyDoi(int tiLeQuyDoi) {
        if (tiLeQuyDoi <= 0) {
            throw new IllegalArgumentException("Tỉ lệ quy đổi phải lớn hơn 0");
        }
        this.tiLeQuyDoi = tiLeQuyDoi;
    }
    @Override
    public String toString() {
        return "QuyDoi{" +
                "soDangKy=" + maThuoc +
                ", maDVTCha=" + maDVTCha +
                ", maDVTCon=" + maDVTCon +
                ", tiLeQuyDoi=" + tiLeQuyDoi +
                '}';
    }

}
