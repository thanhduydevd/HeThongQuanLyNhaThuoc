//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.dialog;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.*;
import com.antam.app.entity.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThemThuocController {
    private DangDieuChe_DAO ddc_dao;
    private DonViTinh_DAO dvt_dao;
    private Thuoc_DAO thuoc_dao;
    private Ke_DAO ke_dao;
    @FXML
    private DialogPane dialogPane;
    @FXML
    private TextField txtAddMaThuoc, txtAddTenThuoc, txtAddHamLuong;
    @FXML
    private Text txtAddGiaByDV, notification_addThuoc;
    @FXML
    private Spinner<Double> spAddGiaGoc, spAddGiaBan, spAddThue;
    @FXML
    private ComboBox<DonViTinh> cbAddDVCS;
    @FXML
    private ComboBox<DangDieuChe> cbAddDangDieuChe;
    @FXML
    private ComboBox<Ke> cbAddKe;


    public ThemThuocController() {
    }

    public void initialize() {
        // them button vao dialog
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Lưu", ButtonData.APPLY);
        this.dialogPane.getButtonTypes().add(cancelButton);
        this.dialogPane.getButtonTypes().add(applyButton);

        // ket noi database
        try { Connection con = ConnectDB.getInstance().connect(); }
        catch (SQLException e) { throw new RuntimeException(e); }

        // su kien button luu
        Button applyBtn = (Button) dialogPane.lookupButton(applyButton);

        applyBtn.addEventFilter(ActionEvent.ACTION, event -> {
            boolean isValid = validate();

            if (!isValid) {
                event.consume();
            }else {
                String maThuoc = txtAddMaThuoc.getText();
                String tenThuoc = txtAddTenThuoc.getText();
                DonViTinh donViCoSo = cbAddDVCS.getValue();
                DangDieuChe dangDieuChe =  cbAddDangDieuChe.getValue();
                String hamLuong = txtAddHamLuong.getText();
                Double giaGoc = spAddGiaGoc.getValue();
                Double giaBan = spAddGiaBan.getValue();
                Double thue = spAddThue.getValue();
                Ke ke = cbAddKe.getValue();

                Thuoc thuoc = new Thuoc(maThuoc, tenThuoc, hamLuong, giaBan, giaGoc, thue.floatValue(), false,
                        dangDieuChe, donViCoSo, ke);
                thuoc_dao = new Thuoc_DAO();
                try {
                    ConnectDB.getInstance().connect();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                boolean check = thuoc_dao.themThuoc(thuoc);
                if (!check) {
                    notification_addThuoc.setText("Thêm thuốc thất bại!");
                    event.consume();
                }
            }

        });

        // su kien up down spinner gia goc
        SpinnerValueFactory.DoubleSpinnerValueFactory valueFactoryGiaGoc =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(
                        0,
                        5000000,
                        0,
                        5000
                );
        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        valueFactoryGiaGoc.setConverter(new StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                if (value == null) return "";
                return currencyFormat.format(value);
            }

            @Override
            public Double fromString(String text) {
                try {
                    Number parsed = currencyFormat.parse(text);
                    return parsed.doubleValue();
                } catch (Exception e) {
                    return 0.0;
                }
            }
        });
        spAddGiaGoc.setValueFactory(valueFactoryGiaGoc);
        spAddGiaGoc.setEditable(true);
        // su kien up down spinner gia ban
        SpinnerValueFactory.DoubleSpinnerValueFactory valueFactoryGiaBan =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(
                        0,
                        5000000,
                        0,
                        5000
                );
        valueFactoryGiaBan.setConverter(new StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                if (value == null) return "";
                return currencyFormat.format(value);
            }

            @Override
            public Double fromString(String text) {
                try {
                    Number parsed = currencyFormat.parse(text);
                    return parsed.doubleValue();
                } catch (Exception e) {
                    return 0.0;
                }
            }
        });
        spAddGiaBan.setValueFactory(valueFactoryGiaBan);
        spAddGiaBan.setEditable(true);
        // su kien up down spinner thue
        SpinnerValueFactory.DoubleSpinnerValueFactory valueFactoryThue =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(
                        0,
                        1,
                        0.05,
                        0.01
                );
        spAddThue.setValueFactory(valueFactoryThue);
        spAddThue.setEditable(true);
        //them combo box ke
        addComBoBoxKe();
        // them combo box don vi co so
        addComBoBoxDVCS();
        //them combo box dang dieu che
        addComBoBoxDDC();
    }

    // ham validate kiem tra du lieu truyen vao dung hay ko
    public boolean validate(){
        String maThuoc = txtAddMaThuoc.getText();
        String tenThuoc = txtAddTenThuoc.getText();
        String donViCoSo = cbAddDVCS.getValue().getTenDVT();
        String dangDieuChe = cbAddDangDieuChe.getValue().getTenDDC();
        String hamLuong = txtAddHamLuong.getText();
        Double giaGoc = 0.0;
        Double giaBan = 0.0;
        Double thue = 0.0;
        String ke = cbAddKe.getValue().getTenKe();

        if(maThuoc.isEmpty()){
            notification_addThuoc.setText("Vui lòng điền mã thuốc!");
            return false;
        }else{
            if (maThuoc.matches("^VN-\\d{5}-\\d{2}$")) {
                try {
                    ConnectDB.getInstance().connect();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                thuoc_dao = new Thuoc_DAO();
                if (thuoc_dao.getThuocTheoMa(maThuoc) != null){
                    notification_addThuoc.setText("Mã thuốc đã tồn tại!");
                    return false;
                }
                notification_addThuoc.setText("");
            } else {
                notification_addThuoc.setText("Mã thuốc không hợp lệ! (VD: VN-12345-01)");
                return false;
            }
        }
        if (tenThuoc.isEmpty()){
            notification_addThuoc.setText("Vui lòng điền đầy đủ tên thuốc!");
            return false;
        }
        if (donViCoSo == null){
            notification_addThuoc.setText("Vui lòng điền đầy đủ đơn vị cơ sở!");
            return false;
        }
        if (dangDieuChe == null){
            notification_addThuoc.setText("Vui lòng điền dạng điều chế!");
            return false;
        }
        if (hamLuong.isEmpty()){
            notification_addThuoc.setText("Vui lòng điền đầy đủ hàm lượng!");
            return false;
        }
        try {
            giaGoc = spAddGiaGoc.getValue();
            if (giaGoc <= 0) {
                notification_addThuoc.setText("Giá gốc phải lớn hơn 0!");
                return false;
            }
        } catch (Exception e) {
            notification_addThuoc.setText("Giá gốc không hợp lệ!");
            return false;
        }
        try {
            giaBan = spAddGiaBan.getValue();
            if (giaBan <= 0) {
                notification_addThuoc.setText("Giá bán phải lớn hơn 0!");
                return false;
            }
        } catch (Exception e) {
            notification_addThuoc.setText("Giá bán không hợp lệ!");
            return false;
        }
        try {
            thue = spAddThue.getValue();
            if (thue < 0) {
                notification_addThuoc.setText("Thuế không được âm!");
                return false;
            }
        } catch (Exception e) {
            notification_addThuoc.setText("Thuế không hợp lệ!");
            return false;
        }
        if (ke == null){
            notification_addThuoc.setText("Vui lòng điền đầy đủ thông tin!");
            return false;
        }
        return true;
    }

    // them value vao combobox ke
    public void addComBoBoxKe() {
        ke_dao = new Ke_DAO();
        ArrayList<Ke> arrayKe = ke_dao.getAllKe();
        cbAddKe.getItems().clear();
        for (Ke ke : arrayKe) {
            cbAddKe.getItems().add(ke);
        }
        cbAddKe.getSelectionModel().selectFirst();
    }

    // Them value vao combobox dang dieu che
    public void addComBoBoxDDC() {
        ddc_dao = new DangDieuChe_DAO();
        ArrayList<DangDieuChe> arrayDDC = ddc_dao.getAllDDC();
        cbAddDangDieuChe.getItems().clear();
        for (DangDieuChe ddc : arrayDDC){
            cbAddDangDieuChe.getItems().add(ddc);
        }
        cbAddDangDieuChe.getSelectionModel().selectFirst();
    }

    // Them value vao combobox don vi tinh
    public void addComBoBoxDVCS() {
        dvt_dao = new DonViTinh_DAO();
        ArrayList<DonViTinh> arrayDVT = dvt_dao.getAllDonViTinh();
        cbAddDVCS.getItems().clear();
        arrayDVT.forEach(dvt -> cbAddDVCS.getItems().add(dvt));
        cbAddDVCS.getSelectionModel().selectFirst();
        txtAddGiaByDV.setText(cbAddDVCS.getValue().toString());
    }

    // Cat chuoi lay don vi
    public static String extractUnit(String input) {
        if (input == null) return null;

        String pattern = "\\d+\\s*(\\p{L}+).*";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(input);

        if (m.find()) {
            return m.group(1);
        } else {
            return null;
        }
    }

}
