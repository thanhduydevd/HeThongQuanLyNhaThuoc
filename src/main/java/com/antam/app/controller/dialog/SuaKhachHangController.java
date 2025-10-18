package com.antam.app.controller.dialog;

import com.antam.app.dao.KhachHang_DAO;
import com.antam.app.entity.KhachHang;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SuaKhachHangController implements Initializable {

    @FXML
    private DialogPane dialogPane;

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
    private boolean isUpdated = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        khachHangDAO = new KhachHang_DAO();
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
        if (khachHang != null) {
            txtMaKhachHang.setText(khachHang.getMaKH());
            txtTenKhachHang.setText(khachHang.getTenKH());
            txtSoDienThoai.setText(khachHang.getSoDienThoai());
        }
    }

    @FXML
    private void handleLuu() {
        // Validate input
        if (!validateInput()) {
            return;
        }

        try {
            // Cập nhật thông tin khách hàng
            khachHang.setTenKH(txtTenKhachHang.getText().trim());
            khachHang.setSoDienThoai(txtSoDienThoai.getText().trim());

            // Lưu vào database
            boolean success = khachHangDAO.updateKhachHang(khachHang);

            if (success) {
                isUpdated = true;
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật thông tin khách hàng thành công!");
                closeDialog();
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật thông tin khách hàng. Vui lòng thử lại!");
            }
        } catch (IllegalArgumentException e) {
            // Bắt lỗi validation từ entity
            showAlert(Alert.AlertType.ERROR, "Lỗi", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    @FXML
    private void handleHuy() {
        closeDialog();
    }

    private boolean validateInput() {
        String tenKH = txtTenKhachHang.getText().trim();
        String soDienThoai = txtSoDienThoai.getText().trim();

        if (tenKH.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng nhập tên khách hàng!");
            txtTenKhachHang.requestFocus();
            return false;
        }

        if (soDienThoai.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng nhập số điện thoại!");
            txtSoDienThoai.requestFocus();
            return false;
        }

        // Validate số điện thoại (10 số)
        if (!soDienThoai.matches("\\d{10}")) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số điện thoại phải có 10 chữ số!");
            txtSoDienThoai.requestFocus();
            return false;
        }

        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeDialog() {
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.close();
    }

    public boolean isUpdated() {
        return isUpdated;
    }
}
