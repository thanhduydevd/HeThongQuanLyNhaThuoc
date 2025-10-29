package com.antam.app.controller.nhanvien;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 29/10/2025
 * @version: 1.0
 */

import com.antam.app.dao.NhanVien_DAO;
import com.antam.app.entity.NhanVien;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import static com.antam.app.controller.nhanvien.CapNhatNhanVienController.nhanVienSelected;

public class CapNhatNhanVienFormController {
    @FXML
    private DialogPane dialogPane;
    @FXML
    private TextField txtMaNV, txtHoTen, txtSDT, txtEmail,txtDiaChi, txtTaiKhoan;
    @FXML
    private ComboBox<String> cbChucVu;
    @FXML
    private Spinner<Double> luong;

    NhanVien select = nhanVienSelected;
    public CapNhatNhanVienFormController() {
    }

    public void initialize() {
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType updateButton = new ButtonType("Cập nhật", ButtonBar.ButtonData.APPLY);
        ButtonType deleteButton = new ButtonType("Xóa", ButtonBar.ButtonData.NO);

        dialogPane.getButtonTypes().addAll(cancelButton, updateButton, deleteButton);

        Button btnSua = (Button) dialogPane.lookupButton(updateButton);
        Button btnHuy = (Button) dialogPane.lookupButton(cancelButton);
        Button btnXoa = (Button) dialogPane.lookupButton(deleteButton);

        txtMaNV.setEditable(false);
        luong.setEditable(true);
        loadData();

        // Cho phép spinner nhập trực tiếp
        luong.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                double val = Double.parseDouble(newVal);
                luong.getValueFactory().setValue(val);
            } catch (NumberFormatException ignored) {}
        });

        // Nút Hủy
        btnHuy.setOnAction(e -> dialogPane.getScene().getWindow().hide());

        // Cập nhật nhân viên
        btnSua.setOnAction(e -> {
//            try {
                if (txtHoTen.getText().isBlank() || txtSDT.getText().isBlank() || txtEmail.getText().isBlank()) {
                    showMess("Vui lòng nhập đầy đủ thông tin!");
                    return;
                }

                String hoTen = txtHoTen.getText();
                String soDienThoai = txtSDT.getText();
                String email = txtEmail.getText();
                String diaChi = txtDiaChi.getText();
                String taiKhoan = txtTaiKhoan.getText();
                boolean quanLy = "Nhân viên quản lí".equals(cbChucVu.getValue());
                double luongCoBan = luong.getValue();

                NhanVien nvUpdate = new NhanVien(
                        select.getMaNV(), hoTen, soDienThoai, email,
                        diaChi, luongCoBan, taiKhoan, select.getMatKhau(), quanLy
                );
                System.out.println(nvUpdate);
                boolean result = NhanVien_DAO.updateNhanVienTrongDBS(nvUpdate);
                showMess(result ? "Cập nhật nhân viên thành công!" : "Cập nhật nhân viên thất bại!");
//            } catch (Exception ex) {
//                showMess("Lỗi: " + ex.getMessage());
//            }
        });

        // Xóa nhân viên
        btnXoa.setOnAction(e -> {
            try {
                boolean result = NhanVien_DAO.xoaNhanVienTrongDBS(select.getMaNV());
                showMess(result ? "Xóa nhân viên thành công!" : "Xóa nhân viên thất bại!");
            } catch (Exception ex) {
                showMess("Lỗi: " + ex.getMessage());
            }
        });

        // Validation email
        txtEmail.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$") || newText.isBlank())
                txtEmail.setStyle("");
            else
                txtEmail.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        });

        // Validation số điện thoại
        txtSDT.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.matches("^0[35679]\\d{8}$") || newText.isBlank())
                txtSDT.setStyle("");
            else
                txtSDT.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        });
    }

    private void loadData() {
        txtMaNV.setText(select.getMaNV());
        txtHoTen.setText(select.getHoTen());
        txtSDT.setText(select.getSoDienThoai());
        txtEmail.setText(select.getEmail());
        txtDiaChi.setText(select.getDiaChi());
        txtTaiKhoan.setText(select.getTaiKhoan());

        if (cbChucVu.getItems().isEmpty()) {
            cbChucVu.getItems().addAll("Nhân viên", "Nhân viên quản lí");
        }
        cbChucVu.getSelectionModel().select(select.isQuanLy() ? 1 : 0);

        SpinnerValueFactory<Double> valueFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100_000_000.0, select.getLuongCoBan(), 500_000.0);
        luong.setValueFactory(valueFactory);
    }
    private void showMess(String mess) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(mess);
        alert.showAndWait();
    }
}
