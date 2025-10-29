//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.phieunhap;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.*;
import com.antam.app.entity.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;


public class CapNhatPhieuNhapFormController {
    @FXML
    private DialogPane dialogPane;

    @FXML
    private TextField tfMaPhieuNhap, tfNhaCungCap, tfDiaChi, tfLyDo;

    private PhieuNhap_DAO phieuNhap_DAO = new PhieuNhap_DAO();

    public CapNhatPhieuNhapFormController() {

    }

    public void initialize() {
        //Kết nối
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType deleteButton = new ButtonType("Huỷ Phiếu Nhập", ButtonData.YES);
        ButtonType applyButton = new ButtonType("Lưu", ButtonData.APPLY);
        this.dialogPane.getButtonTypes().add(cancelButton);
        this.dialogPane.getButtonTypes().add(applyButton);
        this.dialogPane.getButtonTypes().add(deleteButton);

        Button btnAppy = (Button) this.dialogPane.lookupButton(applyButton);
        Button btnDelete = (Button) this.dialogPane.lookupButton(deleteButton);
        btnDelete.addEventFilter(ActionEvent.ACTION, e->{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận huỷ phiếu nhập");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có chắc chắn muốn huỷ phiếu nhập này không?");
            ButtonType btnYes = new ButtonType("Có", ButtonData.YES);
            ButtonType btnNo = new ButtonType("Không", ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(btnYes, btnNo);
            alert.showAndWait().ifPresent(type -> {
                if (type == btnYes) {
                    if (phieuNhap_DAO.huyPhieuNhap(tfMaPhieuNhap.getText())){
                        showCanhBao("Thành công", "Huỷ phiếu nhập thành công");
                    }else{
                        showCanhBao("Thất bại", "Huỷ phiếu nhập thất bại");
                        e.consume();
                    }
                } else {
                    e.consume();
                }
            });
        });

        btnAppy.addEventFilter(ActionEvent.ACTION, event -> {
            if (!checkTruongDuLieu()){
                event.consume();
            }else{
                if (phieuNhap_DAO.suaPhieuNhap(new PhieuNhap(tfMaPhieuNhap.getText(), tfNhaCungCap.getText(), LocalDate.now(), tfDiaChi.getText(), tfLyDo.getText(), null, 0.0, false))){
                    showCanhBao("Thành công", "Cập nhật phiếu nhập thành công");
                }else{
                    showCanhBao("Thất bại", "Cập nhật phiếu nhập thất bại");
                    event.consume();
                }
            }
        });
    }

    // Hiển thị thông tin phiếu nhập lên form
    public void showPhieuNhap(PhieuNhap phieuNhap){
        tfMaPhieuNhap.setText(phieuNhap.getMaPhieuNhap());
        tfMaPhieuNhap.setEditable(false);
        tfNhaCungCap.setText(phieuNhap.getNhaCungCap());
        tfDiaChi.setText(phieuNhap.getDiaChi());
        tfLyDo.setText(phieuNhap.getLyDo());
        System.out.println(phieuNhap.getMaPhieuNhap());
    }

    /**
     * Kiểm tra các trường dữ liệu
     * @return
     */
    public boolean checkTruongDuLieu(){
        if(!tfMaPhieuNhap.getText().isEmpty()){
            if (!tfMaPhieuNhap.getText().matches("^PN\\d{6}")) {
                showCanhBao("Lỗi định dạng", "Mã phiếu nhập bắt đầu là PN và theo sau là 6 ký tự số");
                tfMaPhieuNhap.requestFocus();
                return false;
            }
        }else {
            showCanhBao("Không được rỗng", "Mã phiếu nhập không được rỗng");
            return false;
        }

        if (tfNhaCungCap.getText().isEmpty()){
            showCanhBao("Không được rỗng", "Nhà cung cấp không được rỗng");
            tfNhaCungCap.requestFocus();
            return false;
        }

        if (tfDiaChi.getText().isEmpty()){
            showCanhBao("Không được rỗng", "Địa chỉ không được rỗng");
            tfDiaChi.requestFocus();
            return false;
        }

        if (tfLyDo.getText().isEmpty()){
            showCanhBao("Không được rỗng", "Lý do nhập không được rỗng");
            tfLyDo.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Hiển thị thông báo cảnh báo
     * @param tieuDe
     * @param vanBan
     */
    public void showCanhBao(String tieuDe, String vanBan){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tieuDe);
        alert.setContentText(vanBan);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}