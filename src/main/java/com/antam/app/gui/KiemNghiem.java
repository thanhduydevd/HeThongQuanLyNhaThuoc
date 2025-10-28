package com.antam.app.gui;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 15/10/2025
 * @version: 1.0
 */

import com.antam.app.dao.PhieuDat_DAO;
import com.antam.app.entity.ChiTietPhieuDatThuoc;
import com.antam.app.entity.PhieuDatThuoc;
import com.antam.app.helper.MaKhoaMatKhau;

import java.util.ArrayList;

public class KiemNghiem {

    private static String getHashPD() {
        String hash = PhieuDat_DAO.getMaxHash();
        if (hash == null){
            return "";
        }else{
            int soThuTu = Integer.parseInt(hash) + 1;
            return String.format("PD%04d", soThuTu);
        }
    }
    public static void main(String[] args) {
        ArrayList<ChiTietPhieuDatThuoc> list = PhieuDat_DAO.getChiTietTheoPhieu("PDT001");
        for (ChiTietPhieuDatThuoc p : list) {
            System.out.println(p.toString());
        }
    }
}
