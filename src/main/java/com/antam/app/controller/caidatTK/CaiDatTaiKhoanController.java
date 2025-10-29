package com.antam.app.controller.caidatTK;

import com.antam.app.dao.NhanVien_DAO;
import com.antam.app.entity.PhienNguoiDung;
import com.antam.app.helper.MaKhoaMatKhau;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class CaiDatTaiKhoanController {
    @FXML
    private TextField txtMKnow, txtMKnew;
    @FXML
    private Button btnDoiMK;
    @FXML
    private Text txtTK, txtVaiTro;

    public CaiDatTaiKhoanController() {}

    public void initialize() {
        loadThongTin();

        // Sự kiện kiểm tra mật khẩu cũ ngay khi nhập
        txtMKnow.textProperty().addListener((obs, oldText, newText) -> {
            checkPassword();
        });

        // Sự kiện đổi mật khẩu
        btnDoiMK.setOnAction(e -> {
            doiMKButtonClick();
        });
    }

    private void checkPassword() {
        if (PhienNguoiDung.getMaNV() == null) return;

        String mkNow = txtMKnow.getText();
        boolean isCorrect = MaKhoaMatKhau.verifyPassword(
                mkNow,
                PhienNguoiDung.getMaNV().getMatKhau()
        );

        if (isCorrect) {
            txtMKnow.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
        } else {
            txtMKnow.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        }
    }

    private void doiMKButtonClick() {
        if (PhienNguoiDung.getMaNV() == null) {
            showAlert("Lỗi", "Không xác định được người dùng hiện tại!");
            return;
        }

        String mkNow = txtMKnow.getText();
        String mkNew = txtMKnew.getText();

        // Kiểm tra mật khẩu cũ
        if (!MaKhoaMatKhau.verifyPassword(mkNow, PhienNguoiDung.getMaNV().getMatKhau())) {
            showAlert("Lỗi", "Mật khẩu hiện tại không đúng!");
            return;
        }

        if (mkNew.isEmpty() || mkNew.length() < 6) {
            showAlert("Cảnh báo", "Mật khẩu mới phải có ít nhất 6 ký tự!");
            return;
        }

        // Mã hóa và cập nhật
        String hashCode = MaKhoaMatKhau.hashPassword(mkNew, 10);
        PhienNguoiDung.getMaNV().setMatKhau(hashCode);

        boolean result = NhanVien_DAO.updateNhanVienTrongDBS(PhienNguoiDung.getMaNV());
        if (result) {
            showAlert("Thành công", "Đổi mật khẩu thành công!");
            txtMKnow.clear();
            txtMKnew.clear();
        } else {
            showAlert("Thất bại", "Đổi mật khẩu thất bại!");
        }
    }

    private void loadThongTin() {
        if (PhienNguoiDung.getMaNV() == null) return;

        String tk = PhienNguoiDung.getMaNV().getTaiKhoan();
        String cv = PhienNguoiDung.getMaNV().isQuanLy() ? "Nhân viên quản lí" : "Nhân viên";
        txtTK.setText(tk);
        txtVaiTro.setText(cv);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
