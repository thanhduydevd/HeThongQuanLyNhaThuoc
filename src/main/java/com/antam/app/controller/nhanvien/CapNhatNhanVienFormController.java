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
        ButtonType deleteButton = new ButtonType("Xóa", ButtonBar.ButtonData.APPLY);

        dialogPane.getButtonTypes().addAll(cancelButton, updateButton, deleteButton);

        Button btnSua = (Button) dialogPane.lookupButton(updateButton);
        Button btnHuy = (Button) dialogPane.lookupButton(cancelButton);
        Button btnXoa = (Button) dialogPane.lookupButton(deleteButton);

        txtMaNV.setEditable(false);
        luong.setEditable(true);
        loadData();
        //sự kiện cập nhật nhân viên
        btnSua.setOnAction(e -> {
            try {
                String hoTen = txtHoTen.getText();
                String soDienThoai = txtSDT.getText();
                String email = txtEmail.getText();
                String diaChi = txtDiaChi.getText();
                String taiKhoan = txtTaiKhoan.getText();
                boolean quanLy = cbChucVu.getSelectionModel().getSelectedIndex() == 1;
                double luongCoBan = luong.getValue();

                NhanVien nvUpdate = new NhanVien(
                        select.getMaNV(),
                        hoTen,
                        soDienThoai,
                        email,
                        diaChi,
                        luongCoBan,
                       taiKhoan,
                        select.getMatKhau(),
                        quanLy
                );
                boolean result = NhanVien_DAO.updateNhanVienTrongDBS(nvUpdate);
                if (result) {
                    showMess("Cập nhật nhân viên thành công!");
                } else {
                    showMess("Cập nhật nhân viên thất bại!");
                }
            } catch (Exception ex) {
                showMess("Lỗi: " + ex.getMessage());
            }
        });
        //sự kien xóa nhân viên
        btnXoa.setOnAction(e -> {
            try {
                boolean result = NhanVien_DAO.xoaNhanVienTrongDBS(select.getMaNV());
                if (result) {
                    showMess("Xóa nhân viên thành công!");
                } else {
                    showMess("Xóa nhân viên thất bại!");
                }
            } catch (Exception ex) {
                showMess("Lỗi: " + ex.getMessage());
            }
        });
        //sự kiện khi người dùng nhập sai thông tin
        txtEmail.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")
                    || newText.isBlank()) {
                // Hợp lệ -> bỏ viền đỏ
                txtEmail.setStyle("");
            } else {
                // Sai -> tô viền đỏ
                txtEmail.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            }
        });

        //Sự kiện khi người dùng nhập sai trên số điện thoại
        txtSDT.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.matches("^0[35679]\\d{8}$")
                    || newText.isBlank()) {
                // Hợp lệ -> bỏ viền đỏ
                txtSDT.setStyle("");
            } else {
                // Sai -> tô viền đỏ
                txtSDT.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            }
        });
    }

    private void showMess(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }

    private void loadData() {
        txtMaNV.setText(select.getMaNV());
        txtMaNV.setEditable(false);
        txtHoTen.setText(select.getHoTen());
        txtSDT.setText(select.getSoDienThoai());
        txtEmail.setText(select.getEmail());
        txtDiaChi.setText(select.getDiaChi());
        txtTaiKhoan.setText(select.getTaiKhoan());
        //setup cho comboxBox quản lí
        cbChucVu.getItems().add("Nhân viên");
        cbChucVu.getItems().add("Nhân viên quản lí");
        cbChucVu.getSelectionModel().select(select.isQuanLy() ? 1 : 0);
        // Gán ValueFactory mặc định nếu chưa có
        SpinnerValueFactory<Double> valueFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100_000_000.0, select.getLuongCoBan(), 500_000.0);
        luong.setValueFactory(valueFactory);
    }
}
