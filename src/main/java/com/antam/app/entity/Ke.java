/*
 * @ (#) Ke.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.entity;

import java.util.Objects;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 9/25/2025
 * version: 1.0
 */
public class Ke {
    private final String MaKe;
    private String tenKe;
    private String LoaiKe;

    public Ke() {
        MaKe = "";
        tenKe = "";
        LoaiKe = "";
    }
    public Ke(String maKe, String tenKe, String loaiKe) {
        MaKe = maKe;
        this.tenKe = tenKe;
        LoaiKe = loaiKe;
    }
    public String getMaKe() {
        return MaKe;
    }
    public String getTenKe() {
        return tenKe;
    }
    public void setTenKe(String tenKe) {
        this.tenKe = tenKe;
    }
    public String getLoaiKe() {
        return LoaiKe;
    }
    public void setLoaiKe(String loaiKe) {
        LoaiKe = loaiKe;
    }

    @Override
    public String toString() {
        return "Ke{" +
                "MaKe=" + MaKe +
                ", tenKe='" + tenKe + '\'' +
                ", LoaiKe='" + LoaiKe + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ke ke = (Ke) o;
        return MaKe == ke.MaKe;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(MaKe);
    }
}
