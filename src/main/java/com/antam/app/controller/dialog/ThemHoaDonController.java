//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.TextField;
import com.antam.app.dao.HoaDon_DAO;
import com.antam.app.dao.KhachHang_DAO;
import com.antam.app.entity.KhachHang;
import java.util.List;
import java.util.stream.Collectors;

public class ThemHoaDonController {
    @FXML
    private DialogPane dialogPane;
    @FXML
    private TextField txtMaHoaDon;
    @FXML
    private TextField txtTenKhachHang;

    public ThemHoaDonController() {
    }

    public void initialize() {
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Tạo hoá đơn", ButtonData.APPLY);
        this.dialogPane.getButtonTypes().add(cancelButton);
        this.dialogPane.getButtonTypes().add(applyButton);
        // Tự động sinh mã hoá đơn mới
        txtMaHoaDon.setText(generateNewMaHoaDon());
    }

    /**
     * Sinh mã hoá đơn mới chưa tồn tại trong CSDL dạng HDxxx
     */
    private String generateNewMaHoaDon() {
        HoaDon_DAO hoaDonDAO = new HoaDon_DAO();
        List<String> allMaHD = hoaDonDAO.getAllHoaDon().stream().map(hd -> hd.getMaHD()).collect(Collectors.toList());
        int max = 0;
        for (String ma : allMaHD) {
            if (ma != null && ma.matches("HD\\d+")) {
                int num = Integer.parseInt(ma.substring(2));
                if (num > max) max = num;
            }
        }
        return String.format("HD%03d", max + 1);
    }

    // Xử lý khi nhấn nút tạo hóa đơn
    private String getOrCreateMaKhachHang() {
        String tenKH = txtTenKhachHang.getText().trim();
        if (tenKH.isEmpty()) return null;
        KhachHang_DAO khachHangDAO = new KhachHang_DAO();
        List<KhachHang> allKH = KhachHang_DAO.loadBanFromDB();
        for (KhachHang kh : allKH) {
            if (kh.getTenKH().equalsIgnoreCase(tenKH)) {
                return kh.getMaKH();
            }
        }
        // Nếu chưa có, sinh mã mới và thêm vào CSDL
        String newMaKH = generateNewMaKhachHang(allKH);
        KhachHang newKH = new KhachHang(newMaKH, tenKH, "0123456789", false); // Số điện thoại mặc định, có thể sửa lại lấy từ input
        khachHangDAO.insertKhachHang(newKH);
        return newMaKH;
    }

    private String generateNewMaKhachHang(List<KhachHang> allKH) {
        int max = 0;
        for (KhachHang kh : allKH) {
            String ma = kh.getMaKH();
            if (ma != null && ma.matches("KH\\d+")) {
                int num = Integer.parseInt(ma.substring(2));
                if (num > max) max = num;
            }
        }
        return String.format("KH%03d", max + 1);
    }
}
