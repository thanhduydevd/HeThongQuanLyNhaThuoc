//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.NhanVien_DAO;
import com.antam.app.entity.PhienNguoiDung;
import com.antam.app.gui.GiaoDienChinh;
import com.antam.app.helper.MaKhoaMatKhau;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.SQLException;

public class DangNhapController {
    private NhanVien_DAO nv_dao;
    @FXML
    private Button btnLogin;
    @FXML
    private TextField txtname_login, txtpass_login;
    @FXML
    private Text notification_login;


    public void initialize(){
        btnLogin.setOnAction(e -> {
            onLoginButtonClick();
        });
        txtname_login.setOnKeyPressed(e -> {
            checklogin();
        });
        txtpass_login.setOnKeyPressed(e -> {
            checklogin();
        });
    }

    @FXML
    protected void onLoginButtonClick() {
        if (checklogin()) {
            if (checktnhanvien()) {
                PhienNguoiDung.setMaNV(txtname_login.getText());
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(GiaoDienChinh.class.getResource("/com/antam/app/views/dashboard.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Stage newStage = new Stage();
                    newStage.setTitle("");
                    newStage.setScene(new Scene(root));
                    newStage.setMaximized(true);
                    newStage.initStyle(StageStyle.DECORATED);
                    newStage.show();
                    Stage oldStage = (Stage) this.btnLogin.getScene().getWindow();
                    oldStage.close();
                } catch (Exception var5) {
                    Exception e = var5;
                    e.printStackTrace();
                }
            }
        }
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(GiaoDienChinh.class.getResource("/com/antam/app/views/dashboard.fxml"));
//            Parent root = (Parent) fxmlLoader.load();
//            Stage newStage = new Stage();
//            newStage.setTitle("");
//            newStage.setScene(new Scene(root));
//            newStage.setMaximized(true);
//            newStage.initStyle(StageStyle.DECORATED);
//            newStage.show();
//            Stage oldStage = (Stage) this.btnLogin.getScene().getWindow();
//            oldStage.close();
//        } catch (Exception var5) {
//            Exception e = var5;
//            e.printStackTrace();
//        }
    }
    private boolean checklogin(){
        // Kiểm tra tên đăng nhập
        if (txtname_login.getText().isEmpty()){
            notification_login.setText("Vui lòng nhập tên đăng nhập");
            notification_login.setStyle("-fx-text-fill: red;");
            return false;
        } else {
//            if (!txtname_login.getText().matches("^(?=.*[A-Z])(?=.*\\d)[^\\s]{8,}$")){
//                notification_login.setText("Tên đăng nhập phải chứa ít nhất 1 chữ cái viết hoa, 1 số và 8 ký tự");
//                if (!txtname_login.isFocused()) {
//                    txtname_login.requestFocus();
//                }
//                return false;
//            }
        }

        // Kiểm tra mật khẩu
        if (txtpass_login.getText().isEmpty()) {
            notification_login.setText("Vui lòng nhập mật khẩu");
            return false;
        } else {
//            if (!txtpass_login.getText().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w])[^\\s]{8,}$")){
//                notification_login.setText("Mật khẩu phải có 8 ký tự, 1 chữ hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt.");
//                if (!txtpass_login.isFocused()) {
//                    txtpass_login.requestFocus();
//                }
//                return false;
//            }
        }

        notification_login.setText("");
        return true;
    }

    private boolean checktnhanvien(){
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        nv_dao = new NhanVien_DAO();
        String id = txtname_login.getText();
        String pass = txtpass_login.getText();
        if (nv_dao.getNhanVienTaiKhoan(id) == null){
            notification_login.setText("Tên đăng nhập không tồn tại");
            return false;
        }else if (!MaKhoaMatKhau.verifyPassword(pass,nv_dao.getNhanVienTaiKhoan(id).getMatKhau())) {
            System.out.println("false");
            notification_login.setText("Mật khẩu không đúng");
            return false;
        }
        return true;
    }

}
