/*
 * @ (#) SuaKhachHangController.java   1.0 28/10/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.antam.app.controller.khachhang;

import com.antam.app.dao.KhachHang_DAO;
import com.antam.app.entity.KhachHang;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/*
 * @description Controller for updating customer information
 * @author: Tran Tuan Hung
 * @date: 28/10/25
 * @version: 1.0
 */
public class SuaKhachHangFormController implements Initializable {

    @FXML
    private TextField txtMaKhachHang;

    @FXML
    private TextField txtTenKhachHang;

    @FXML
    private TextField txtSoDienThoai;

    @FXML
    private Button btnLuu;

    @FXML
    private Button btnHuy;

    private KhachHang khachHang;
    private KhachHang_DAO khachHangDAO;

    // Callback interface để thông báo khi lưu thành công
    public interface OnSaveListener {
        void onSaveSuccess();
    }

    private OnSaveListener onSaveListener;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Khởi tạo DAO
        khachHangDAO = new KhachHang_DAO();

        // Thiết lập sự kiện cho nút Lưu
        btnLuu.setOnAction(event -> handleLuu());

        // Thiết lập sự kiện cho nút Hủy
        btnHuy.setOnAction(event -> handleHuy());
    }

    /**
     * Thiết lập callback khi lưu thành công
     */
    public void setOnSaveListener(OnSaveListener listener) {
        this.onSaveListener = listener;
    }

    /**
     * Thiết lập dữ liệu khách hàng để hiển thị trong form
     */
    public void setKhachHang(KhachHang kh) {
        this.khachHang = kh;
        if (khachHang != null) {
            txtMaKhachHang.setText(khachHang.getMaKH());
            txtTenKhachHang.setText(khachHang.getTenKH());
            txtSoDienThoai.setText(khachHang.getSoDienThoai());
        }
    }

    /**
     * Xử lý sự kiện click nút Lưu
     */
    private void handleLuu() {
        // Kiểm tra dữ liệu đầu vào
        if (txtTenKhachHang.getText().trim().isEmpty()) {
            showError("Lỗi", "Tên khách hàng không được để trống");
            return;
        }

        if (txtSoDienThoai.getText().trim().isEmpty()) {
            showError("Lỗi", "Số điện thoại không được để trống");
            return;
        }

        // Kiểm tra định dạng số điện thoại (đơn giản: chỉ chứa số)
        if (!txtSoDienThoai.getText().matches("\\d+")) {
            showError("Lỗi", "Số điện thoại phải chỉ chứa các chữ số");
            return;
        }

        try {
            // Cập nhật thông tin khách hàng
            khachHang.setTenKH(txtTenKhachHang.getText().trim());
            khachHang.setSoDienThoai(txtSoDienThoai.getText().trim());

            // Lưu vào database
            boolean success = khachHangDAO.updateKhachHang(khachHang);

            if (success) {
                showInfo("Thành công", "Cập nhật thông tin khách hàng thành công");

                // Gọi callback khi lưu thành công
                if (onSaveListener != null) {
                    onSaveListener.onSaveSuccess();
                }

                // Đóng dialog
                btnHuy.getScene().getWindow().hide();
            } else {
                showError("Lỗi", "Không thể cập nhật thông tin khách hàng");
            }

        } catch (Exception e) {
            showError("Lỗi", "Lỗi khi cập nhật: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Xử lý sự kiện click nút Hủy
     */
    private void handleHuy() {
        // Đóng dialog mà không lưu
        btnHuy.getScene().getWindow().hide();
    }

    /**
     * Hiển thị dialog lỗi
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Hiển thị dialog thông tin
     */
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
