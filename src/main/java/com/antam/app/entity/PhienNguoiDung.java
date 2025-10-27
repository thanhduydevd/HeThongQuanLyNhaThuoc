/*
 * @ (#) PhienNguoiDung.java   1.0 10/1/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.entity;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/1/2025
 * version: 1.0
 */
public class PhienNguoiDung {
    private static String maNV;

    // Phương thức để lưu tên đăng nhập
    public static void setMaNV(String maNV) {
        maNV = maNV;
    }

    // Phương thức để lấy tên đăng nhập
    public static String getMaNV() {
        return maNV;
    }
}
