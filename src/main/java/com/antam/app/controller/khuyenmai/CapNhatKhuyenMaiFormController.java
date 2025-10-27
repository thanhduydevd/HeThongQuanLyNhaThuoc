/*
 * @ (#) TuyChinhKhuyenMaiController.java   1.0 10/15/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.controller.khuyenmai;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.KhuyenMai_DAO;
import com.antam.app.dao.LoaiKhuyenMai_DAO;
import com.antam.app.entity.KhuyenMai;
import com.antam.app.entity.LoaiKhuyenMai;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.util.ArrayList;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/15/2025
 * version: 1.0
 */
public class CapNhatKhuyenMaiFormController {
    @FXML
    private DialogPane dialogPane;
    @FXML
    private TextField txtMaKhuyenMai, txtTenKhuyenMai;
    @FXML
    private ComboBox<LoaiKhuyenMai> cbLoaiKhuyenMai;
    @FXML
    private Spinner<Integer> spSo, spSoLuongToiDa;
    @FXML
    private DatePicker dpNgayBacDau, dpNgayKetThuc;
    @FXML
    private Text txtThongBao;
    private KhuyenMai_DAO khuyenMai_dao = new KhuyenMai_DAO();
    private LoaiKhuyenMai_DAO loaiKhuyenMai_dao = new LoaiKhuyenMai_DAO();
    private KhuyenMai khuyenMai;

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }
    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }
    public void showdata(KhuyenMai khuyenMai) {
        if (khuyenMai != null) {
            txtMaKhuyenMai.setText(khuyenMai.getMaKM());
            txtTenKhuyenMai.setText(khuyenMai.getTenKM());
            cbLoaiKhuyenMai.setValue(khuyenMai.getLoaiKhuyenMai());
            if (khuyenMai.getLoaiKhuyenMai().getMaLKM() == 1) { // Phần trăm
                SpinnerValueFactory<Integer> valueFactorySo = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 10, 1);
                spSo.setValueFactory(valueFactorySo);
            } else if (khuyenMai.getLoaiKhuyenMai().getMaLKM() == 2) { // Tiền mặt
                SpinnerValueFactory<Integer> valueFactorySo = new SpinnerValueFactory.IntegerSpinnerValueFactory(1000, 1000000, 10000, 1000);
                spSo.setValueFactory(valueFactorySo);
            }
            SpinnerValueFactory<Integer> valueFactorySoLuongToiDa = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 10, 1);
            spSoLuongToiDa.setValueFactory(valueFactorySoLuongToiDa);
            spSo.getValueFactory().setValue((int) khuyenMai.getSo());
            spSoLuongToiDa.getValueFactory().setValue(khuyenMai.getSoLuongToiDa());
            dpNgayBacDau.setValue(khuyenMai.getNgayBatDau());
            dpNgayKetThuc.setValue(khuyenMai.getNgayKetThuc());
        }
    }
    public CapNhatKhuyenMaiFormController() {
    }

    public void initialize() {
        // them button vao dialog
        ButtonType closeButton = new ButtonType("Đóng", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType applyButtonUpdate = new ButtonType("Sửa", ButtonBar.ButtonData.APPLY);
        ButtonType applyButtonDelete = new ButtonType("Xóa", ButtonBar.ButtonData.APPLY);
        // them button vao dialog
        dialogPane.getButtonTypes().addAll(applyButtonUpdate, applyButtonDelete, closeButton);

        try {
            Connection con = ConnectDB.getInstance().getConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        // su kien button
        Button applyBtnUpdate = (Button) dialogPane.lookupButton(applyButtonUpdate);
        Button applyBtnDelete = (Button) dialogPane.lookupButton(applyButtonDelete);

        applyBtnUpdate.addEventFilter(ActionEvent.ACTION, e -> {
            if (!validate()) {
                e.consume();
            } else {
                String maKM = txtMaKhuyenMai.getText().trim();
                String tenKM = txtTenKhuyenMai.getText().trim();
                LoaiKhuyenMai loaiKM = cbLoaiKhuyenMai.getSelectionModel().getSelectedItem();
                int so = spSo.getValue();
                int soLuongToiDa = spSoLuongToiDa.getValue();
                java.time.LocalDate ngayBatDau = dpNgayBacDau.getValue();
                java.time.LocalDate ngayKetThuc = dpNgayKetThuc.getValue();

                KhuyenMai updatedKhuyenMai = new KhuyenMai(maKM, tenKM, ngayBatDau, ngayKetThuc, loaiKM, so, soLuongToiDa, false);
                boolean success = khuyenMai_dao.capNhatKhuyenMai(updatedKhuyenMai);
                if (success) {
                    txtThongBao.setText("Cập nhật khuyến mãi thành công");
                    khuyenMai = updatedKhuyenMai;
                } else {
                    txtThongBao.setText("Cập nhật khuyến mãi thất bại");
                    e.consume();
                }
            }
        });

        applyBtnDelete.addEventFilter(ActionEvent.ACTION, e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xóa");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có chắc chắn muốn xóa khuyến mãi này?");
            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
            if (result != ButtonType.OK) {
                e.consume();
            } else {
                String maKM = txtMaKhuyenMai.getText().trim();
                boolean success = khuyenMai_dao.xoaKhuyenMai(maKM);
                if (success) {
                    txtThongBao.setText("Xóa khuyến mãi thành công");
                } else {
                    txtThongBao.setText("Xóa khuyến mãi thất bại");
                    e.consume();
                }
            }
        });
        // set ma khuyen mai
        txtMaKhuyenMai.setEditable(false);
        // load loai khuyen mai
        loadLoaiKhuyenMai();
        // set su kien loai khuyen mai
        cbLoaiKhuyenMai.setOnAction(e -> {
            LoaiKhuyenMai selectedLoai = cbLoaiKhuyenMai.getSelectionModel().getSelectedItem();
            if (selectedLoai != null) {
                if (selectedLoai.getMaLKM() == 1) { // Phần trăm
                    SpinnerValueFactory<Integer> value = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 10, 1);
                    spSo.setValueFactory(value);
                } else if (selectedLoai.getMaLKM() == 2) { // Tiền mặt
                    SpinnerValueFactory<Integer> value = new SpinnerValueFactory.IntegerSpinnerValueFactory(1000, 1000000, 10000, 1000);
                    spSo.setValueFactory(value);
                }
            }
        });
    }


    public void loadLoaiKhuyenMai(){
        ArrayList<LoaiKhuyenMai> listLoaiKhuyenMai = loaiKhuyenMai_dao.getAllLoaiKhuyenMai();
        cbLoaiKhuyenMai.getItems().clear();
        cbLoaiKhuyenMai.getItems().addAll(listLoaiKhuyenMai);
        cbLoaiKhuyenMai.getSelectionModel().selectFirst();
    }

    public boolean validate(){
        String tenKM = txtTenKhuyenMai.getText().trim();
        if (tenKM.isEmpty()) {
            txtThongBao.setText("Tên khuyến mãi không được để trống");
            return false;
        }
        if (cbLoaiKhuyenMai.getValue() == null) {
            txtThongBao.setText("Vui lòng chọn loại khuyến mãi");
            return false;
        }
        if (dpNgayBacDau.getValue() == null) {
            txtThongBao.setText("Vui lòng chọn ngày bắt đầu");
            return false;
        }
        if (dpNgayKetThuc.getValue() == null) {
            txtThongBao.setText("Vui lòng chọn ngày kết thúc");
            return false;
        }
        if (dpNgayBacDau.getValue().isAfter(dpNgayKetThuc.getValue())) {
            txtThongBao.setText("Ngày bắt đầu phải trước ngày kết thúc");
            return false;
        }
        if (spSo.getValue() == null || spSo.getValue() <= 0) {
            txtThongBao.setText("Giá trị khuyến mãi phải lớn hơn 0");
            return false;
        }
        if (spSoLuongToiDa.getValue() == null || spSoLuongToiDa.getValue() <= 0) {
            txtThongBao.setText("Số lượng tối đa phải lớn hơn 0");
            return false;
        }
        return true;
    }
}
