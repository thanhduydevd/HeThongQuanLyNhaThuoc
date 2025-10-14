//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.dialog;

import com.antam.app.dao.NhanVien_DAO;
import com.antam.app.entity.NhanVien;
import com.antam.app.helper.MaKhoaMatKhau;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;

import java.text.DecimalFormat;

public class ThemNhanVienController {
    @FXML
    private DialogPane dialogPane;
    @FXML
    private TextField txtMaNV, txtHoTen, txtSDT, txtEmail,txtDiaChi, txtTaiKhoan;
    @FXML
    private PasswordField txtPass;
    @FXML
    private ComboBox<String> cbChucVu;
    @FXML
    private Spinner<Double> luong;


    public ThemNhanVienController() {
    }

    public void initialize() {
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Lưu", ButtonData.APPLY);
        this.dialogPane.getButtonTypes().add(cancelButton);
        this.dialogPane.getButtonTypes().add(applyButton);

        Button btnThem = (Button) dialogPane.lookupButton(applyButton);
        Button btnHuy = (Button) dialogPane.lookupButton(applyButton);

        // Gán ValueFactory mặc định nếu chưa có
        if (luong.getValueFactory() == null) {
            SpinnerValueFactory<Double> valueFactory =
                    new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100_000_000.0, 5_000_000.0, 500_000.0);
            luong.setValueFactory(valueFactory);
        }

        // tắt trạng thái của mã nhân viên
        txtMaNV.setText(getHashMaNV());
        txtMaNV.setEditable(false);
        //focus vào tên nhân viên
        txtHoTen.requestFocus();

        //setup cho comboxBox quản lí
        cbChucVu.getItems().add("Nhân viên");
        cbChucVu.getItems().add("Nhân viên quản lí");
        cbChucVu.getSelectionModel().selectFirst();
        //sự kiện thêm nhân viên
        btnThem.setOnAction(e -> {
            if (setupThemNhanVien() != null){
                NhanVien_DAO.themNhanVien(setupThemNhanVien());
            }else{

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

    /**
     * Chuẩn bị nhân viên trước khi đưa vào database
     * @return NhanVien
     */
    private NhanVien setupThemNhanVien() {
        // Kiểm tra nếu ValueFactory chưa được gán
        if (luong.getValueFactory() == null) {
            System.out.println("ValueFactory chưa được gán");
            return null;
        }

        Double value = luong.getValueFactory().getValue();
        double l = (value != null) ? value.doubleValue() : 0.0;

        //kiểm tra rỗng trên các textField và kiểm tra định dạng
        if (txtHoTen.getText().isBlank() ||
                txtMaNV.getText().isBlank() ||
                txtPass.getText().isBlank() ||
                txtEmail.getText().isBlank() ||
                txtSDT.getText().isBlank() ||
                txtDiaChi.getText().isBlank() ||
                txtTaiKhoan.getText().isBlank()) {
            System.out.println("Các field rỗng");
            return null;
        } else if (
                !txtSDT.getText().matches("^0[35679]\\d{8}$") ||
                        !txtEmail.getText().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")
        ) {
            System.out.println("Không đúng định dạng");
            return null;
        }

        // độ khó là 10 phù hợp với các dự án nhỏ và làm tăng hiệu suất
        String pass = MaKhoaMatKhau.hashPassword(txtPass.getText(), 10);

        return new NhanVien(
                txtMaNV.getText(),
                txtHoTen.getText(),
                txtSDT.getText(),
                txtEmail.getText(),
                txtDiaChi.getText(),
                l,
                txtTaiKhoan.getText(),
                pass,
                isNVQuanLy()
        );
    }

    public boolean isNVQuanLy(){
        return cbChucVu.getSelectionModel()
                .getSelectedItem()
                .equals("Nhân viên quản lí") ? true : false;
    }

    /**
     * Tạo mã nhân viên tự động và tăng 1 giá trị mỗi khi thêm nhân viên.
     * @return String(Chuỗi mã nhân viên được tạo tự động)
     */
    public String getHashMaNV(){
        String hash = NhanVien_DAO.getMaxHashNhanVien();
        int maxHash = Integer.parseInt(hash);
        DecimalFormat deFomat = new DecimalFormat("00000");
        return String.format("NV%s",deFomat.format(++maxHash));
    }

    /**
     * Hiện thông báo cho một sự kiện với lời nhắn cho trước
     * @param message(Tin nhắn cần thông báo)
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
