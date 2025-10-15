package com.antam.app.gui;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 15/10/2025
 * @version: 1.0
 */

import com.antam.app.helper.MaKhoaMatKhau;

public class KiemNghiem {
    public static void main(String[] args) {
        String hash1 = MaKhoaMatKhau.hashPassword("@Admin001",10);
        String hash2 = MaKhoaMatKhau.hashPassword("@Admin002",10);
        String hash3 = MaKhoaMatKhau.hashPassword("@Admin003",10);
        String hash4 = MaKhoaMatKhau.hashPassword("@Admin004",10);
        String hash5 = MaKhoaMatKhau.hashPassword("@Admin005",10);
        String hash6 = MaKhoaMatKhau.hashPassword("@Admin006",10);
        String hash7 = MaKhoaMatKhau.hashPassword("@Admin007",10);
        String hash8 = MaKhoaMatKhau.hashPassword("@Admin008",10);
        String hash9 = MaKhoaMatKhau.hashPassword("@Admin009",10);
        String hash10 = MaKhoaMatKhau.hashPassword("@Admin010",10);
        System.out.println("mật khẩu: " + hash1);
        System.out.println("mật khẩu: " + hash2);
        System.out.println("mật khẩu: " + hash3);
        System.out.println("mật khẩu: " + hash4);
        System.out.println("mật khẩu: " + hash5);
        System.out.println("mật khẩu: " + hash6);
        System.out.println("mật khẩu: " + hash7);
        System.out.println("mật khẩu: " + hash8);
        System.out.println("mật khẩu: " + hash9);
        System.out.println("mật khẩu: " + hash10);

    }
}
