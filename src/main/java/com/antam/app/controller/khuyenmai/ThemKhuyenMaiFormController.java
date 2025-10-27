//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.khuyenmai;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.KhuyenMai_DAO;
import com.antam.app.dao.LoaiKhuyenMai_DAO;
import com.antam.app.entity.LoaiKhuyenMai;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;

public class ThemKhuyenMaiFormController {
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

    public ThemKhuyenMaiFormController() {
    }

    public void initialize() {
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Lưu", ButtonData.APPLY);
        this.dialogPane.getButtonTypes().add(cancelButton);
        this.dialogPane.getButtonTypes().add(applyButton);
        Button applyBtn = (Button) this.dialogPane.lookupButton(applyButton);

        try {
            Connection con = ConnectDB.getInstance().getConnection();
        }catch (Exception e){
            e.printStackTrace();
        }

        applyBtn.addEventFilter(ActionEvent.ACTION, e -> {
            if (!validate()) {
                e.consume();
            } else {
                String maKM = txtMaKhuyenMai.getText().trim();
                String tenKM = txtTenKhuyenMai.getText().trim();
                LoaiKhuyenMai loaiKM = cbLoaiKhuyenMai.getSelectionModel().getSelectedItem();
                int so = spSo.getValue();
                LocalDate ngayBatDau = dpNgayBacDau.getValue();
                LocalDate ngayKetThuc = dpNgayKetThuc.getValue();
                int soLuongToiDa = spSoLuongToiDa.getValue();

                boolean success = khuyenMai_dao.themKhuyenMai(maKM, tenKM, loaiKM, so, ngayBatDau, ngayKetThuc, soLuongToiDa);
                if (success) {
                    txtThongBao.setStyle("-fx-fill: green;");
                    txtThongBao.setText("Thêm khuyến mãi thành công");
                } else {
                    txtThongBao.setStyle("-fx-fill: red;");
                    txtThongBao.setText("Thêm khuyến mãi thất bại");
                }
            }
        });
        // set ma khuyen mai
        txtMaKhuyenMai.setText(taoMaKhuyenMai());
        txtMaKhuyenMai.setEditable(false);
        // load loai khuyen mai
        loadLoaiKhuyenMai();
        // set spinner
        SpinnerValueFactory<Integer> valueFactorySo = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 10, 1);
        spSo.setValueFactory(valueFactorySo);
        SpinnerValueFactory<Integer> valueFactorySoLuongToiDa = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 10, 1);
        spSoLuongToiDa.setValueFactory(valueFactorySoLuongToiDa);
        // set su kien loai khuyen mai
        cbLoaiKhuyenMai.setOnAction(e -> {
            LoaiKhuyenMai selectedLoai = cbLoaiKhuyenMai.getSelectionModel().getSelectedItem();
            if (selectedLoai != null) {
                if (selectedLoai.getMaLKM() == 1) { // Phần trăm
                    SpinnerValueFactory<Integer> value = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 10, 1);
                    spSo.setValueFactory(valueFactorySo);
                } else if (selectedLoai.getMaLKM() == 2) { // Tiền mặt
                    SpinnerValueFactory<Integer> value = new SpinnerValueFactory.IntegerSpinnerValueFactory(1000, 1000000, 10000, 1000);
                    spSo.setValueFactory(valueFactorySo);
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
        if (spSo.getValue() == null) {
            txtThongBao.setText("Vui lòng nhập số khuyến mãi");
            return false;
        }else{
            if (cbLoaiKhuyenMai.getValue().getMaLKM() == 1) { // Phần trăm
                if (spSo.getValue() <= 0 || spSo.getValue() > 100) {
                    txtThongBao.setText("Số phần trăm khuyến mãi phải từ 1 đến 100");
                    return false;
                }
            } else if (cbLoaiKhuyenMai.getValue().getMaLKM() == 2) { // Tiền mặt
                if (spSo.getValue() <= 0) {
                    txtThongBao.setText("Số tiền khuyến mãi phải là số và lớn hơn 0");
                    return false;
                }
            }
        }
        if (spSoLuongToiDa.getValue() == null) {
            txtThongBao.setText("Vui lòng nhập số lượng tối đa");
            return false;
        }else{
            if (spSoLuongToiDa.getValue() <= 0) {
                txtThongBao.setText("Số lượng tối đa phải là số và lớn hơn 0");
                return false;
            }
        }
        return true;
    }

    public String taoMaKhuyenMai(){
        ArrayList<String> listMaKM = new ArrayList<>();
        khuyenMai_dao.getAllKhuyenMai().forEach(khuyenMai -> listMaKM.add(khuyenMai.getMaKM()));
        int max = 0;
        for (String ma : listMaKM) {
            if (ma != null && ma.matches("KM\\d+")) {
                int num = Integer.parseInt(ma.substring(2));
                if (num > max) max = num;
            }
        }
        return String.format("KM%03d", max + 1);
    }
}
