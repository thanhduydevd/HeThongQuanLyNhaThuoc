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

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThemThuocController {
    private DangDieuChe_DAO ddc_dao;
    private DonViTinh_DAO dvt_dao;
    private Thuoc_DAO thuoc_dao;
    private static QuyDoi_DAO quyDoi_dao = new QuyDoi_DAO();
    private Ke_DAO ke_dao;
    @FXML
    private DialogPane dialogPane;
    @FXML
    private TextField txtAddMaThuoc, txtAddTenThuoc, txtAddTonKho, txtAddHamLuong;
    @FXML
    private Text txtAddGiaByDV, notification_addThuoc, txtAddQuyDoi1, txtAddQuyDoi2, txtAddTitleQuyDoi1, txtAddTitleQuyDoi2;
    @FXML
    private DatePicker dpAddNgaySanXuat, dpAddHanSuDung;
    @FXML
    private Spinner<Double> spAddGiaGoc, spAddGiaBan, spAddThue;
    @FXML
    private Spinner<Integer> spAddQuyDoi1, spAddQuyDoi2;
    @FXML
    private ComboBox<DonViTinh> cbAddDVCS;
    @FXML
    private ComboBox<DangDieuChe> cbAddDangDieuChe;
    @FXML
    private ComboBox<Ke> cbAddKe;


    public ThemThuocController() {
    }

    public void initialize() {
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Lưu", ButtonData.APPLY);
        this.dialogPane.getButtonTypes().add(cancelButton);
        this.dialogPane.getButtonTypes().add(applyButton);


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
                int tonKho = Integer.parseInt(txtAddTonKho.getText());
                int tonKhoInt = 0;
                LocalDate ngaySanXuat = dpAddNgaySanXuat.getValue();
                LocalDate hanSuDung = dpAddHanSuDung.getValue();
                Double giaGoc = spAddGiaGoc.getValue();
                Double giaBan = spAddGiaBan.getValue();
                Double thue = spAddThue.getValue();
                Ke ke = cbAddKe.getValue();
                String title1 = extractUnit(txtAddTitleQuyDoi1.getText());
                String title2 = extractUnit(txtAddTitleQuyDoi2.getText());
                String unit1 = txtAddQuyDoi1.getText();
                String unit2 = txtAddQuyDoi2.getText();
                int donVi1 = spAddQuyDoi1.getValue();
                int donVi2 = spAddQuyDoi2.getValue();

                Thuoc thuoc = new Thuoc(maThuoc, tenThuoc, hanSuDung, ngaySanXuat, tonKhoInt, hamLuong, giaBan, giaGoc, thue.floatValue(), false,
                        dangDieuChe, donViCoSo, ke);
                thuoc_dao = new Thuoc_DAO();
                boolean success = thuoc_dao.themThuoc(thuoc);

                if (success) {
                    if (donVi1 != 0 && title1 != null && unit1 != null) {
                        QuyDoi qd1 = new QuyDoi(thuoc, dvt_dao.getDVTTheoTen(title1), dvt_dao.getDVTTheoTen(unit1), donVi1);
                        if (quyDoi_dao.themQuyDoi(qd1) == false) {
                            notification_addThuoc.setText("Thêm thuốc thất bại!");
                            event.consume();
                        }
                    }
                    if (donVi2 != 0 && title2 != null && unit2 != null) {
                        QuyDoi qd2 = new QuyDoi(thuoc, dvt_dao.getDVTTheoTen(title2), dvt_dao.getDVTTheoTen(unit2), donVi2);
                        if (quyDoi_dao.themQuyDoi(qd2) == false) {
                            notification_addThuoc.setText("Thêm thuốc thất bại!");
                            event.consume();
                        }
                    }
                    int tonKhoCoSo = quyDoiVeCoSo(maThuoc, tonKho, donViCoSo.getMaDVT());
                    thuoc.setTonKho(tonKhoCoSo);
                    thuoc_dao.capNhatThuoc(thuoc);
                } else {
                    notification_addThuoc.setText("Thêm thuốc thất bại!");
                    event.consume();
                }

            }

        });

        try { Connection con = ConnectDB.getInstance().connect(); }
        catch (SQLException e) { throw new RuntimeException(e); }

        // su kien up down spinner
        SpinnerValueFactory.DoubleSpinnerValueFactory valueFactoryGiaGoc =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(
                        0,
                        1_000_000,
                        0,
                        1000
                );

        spAddGiaGoc.setValueFactory(valueFactoryGiaGoc);
        spAddGiaGoc.setEditable(true);

        SpinnerValueFactory.DoubleSpinnerValueFactory valueFactoryGiaBan =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(
                        0,
                        1_000_000,
                        0,
                        1000
                );
        spAddGiaBan.setValueFactory(valueFactoryGiaBan);
        spAddGiaBan.setEditable(true);

        SpinnerValueFactory.DoubleSpinnerValueFactory valueFactoryThue =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(
                        0,
                        1,
                        0,
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
        eventComboBoxDVCS();
        cbAddDVCS.setOnAction(event -> eventComboBoxDVCS());
    }

    // ham validate kiem tra du lieu truyen vao dung hay ko
    public boolean validate(){
        String maThuoc = txtAddMaThuoc.getText();
        String tenThuoc = txtAddTenThuoc.getText();
        String donViCoSo = cbAddDVCS.getValue().getTenDVT();
        String dangDieuChe = cbAddDangDieuChe.getValue().getTenDDC();
        String hamLuong = txtAddHamLuong.getText();
        String tonKho = txtAddTonKho.getText();
        int tonKhoInt = 0;
        LocalDate ngaySanXuat = dpAddNgaySanXuat.getValue();
        LocalDate hanSuDung = dpAddHanSuDung.getValue();
        Double giaGoc = 0.0;
        Double giaBan = 0.0;
        Double thue = 0.0;
        String ke = cbAddKe.getValue().getTenKe();

        if(maThuoc.isEmpty()){
            notification_addThuoc.setText("Vui lòng điền mã thuốc!");
            return false;
        }else{
            if (maThuoc.matches("^VN-\\d{5}-\\d{2}$")) {
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
        if (tonKho.isEmpty()){
            notification_addThuoc.setText("Vui lòng điền đầy đủ tồn kho!");
            return false;
        }else{
            try {
                tonKhoInt = Integer.parseInt(tonKho);
                if (tonKhoInt < 0) {
                    notification_addThuoc.setText("Tồn kho không được âm!");
                    return false;
                }
            } catch (NumberFormatException e) {
                notification_addThuoc.setText("Tồn kho phải là số nguyên!");
                return false;
            }
        }
        if (ngaySanXuat == null){
            notification_addThuoc.setText("Vui lòng chọn ngày sản xuất!");
            return false;
        }else{
            if (ngaySanXuat.isAfter(LocalDate.now())) {
                notification_addThuoc.setText("Ngày sản xuất phải trước ngày hiện tại!");
                return false;
            }
        }
        if (hanSuDung == null){
            notification_addThuoc.setText("Vui lòng chọn hạn sử dụng!");
            return false;
        }else{
            if (hanSuDung.isBefore(ngaySanXuat)) {
                notification_addThuoc.setText("Hạn sử dụng phải sau ngày sản xuất!");
                return false;
            }
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

    // Cat chuoi
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

    // ham event thay doi cac text theo combobox don vi co so
    public void eventComboBoxDVCS(){
        String donViCoSo =  cbAddDVCS.getValue().getTenDVT();
        txtAddGiaByDV.setText(donViCoSo);
        switch (donViCoSo) {
            case "Viên":
                spAddQuyDoi1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 10, 1));
                spAddQuyDoi1.setEditable(true);
                spAddQuyDoi2.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 10, 1));
                spAddQuyDoi2.setEditable(true);
                txtAddQuyDoi1.setText("Vỉ");
                txtAddTitleQuyDoi1.setText("1 Hộp =");
                txtAddQuyDoi2.setText("Viên");
                txtAddTitleQuyDoi2.setText("1 Vỉ =");
                break;
            case "Vỉ":
                spAddQuyDoi1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 10, 1));
                spAddQuyDoi1.setEditable(true);
                spAddQuyDoi2.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0, 0));
                spAddQuyDoi2.setEditable(false);
                txtAddQuyDoi1.setText("Vĩ");
                txtAddTitleQuyDoi1.setText("1 Hộp =");
                txtAddQuyDoi2.setText("");
                txtAddTitleQuyDoi2.setText("");
                break;
            case "Hộp":
                spAddQuyDoi1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0, 0));
                spAddQuyDoi1.setEditable(true);
                spAddQuyDoi2.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0, 0));
                spAddQuyDoi2.setEditable(true);
                txtAddQuyDoi1.setText("");
                txtAddTitleQuyDoi1.setText("");
                txtAddQuyDoi2.setText("");
                txtAddTitleQuyDoi2.setText("");
                break;
            case "Chai":
                spAddQuyDoi1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1, 1, 0));
                spAddQuyDoi1.setEditable(true);
                spAddQuyDoi2.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0, 0));
                spAddQuyDoi2.setEditable(false);
                txtAddQuyDoi1.setText("Chai");
                txtAddTitleQuyDoi1.setText("1 Hộp =");
                txtAddQuyDoi2.setText("");
                txtAddTitleQuyDoi2.setText("");
                break;
            case "Lọ":
                spAddQuyDoi1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1, 1));
                spAddQuyDoi1.setEditable(true);
                spAddQuyDoi2.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 10, 1));
                spAddQuyDoi2.setEditable(true);
                txtAddQuyDoi1.setText("Lọ");
                txtAddTitleQuyDoi1.setText("1 Hộp =");
                txtAddQuyDoi2.setText("Viên");
                txtAddTitleQuyDoi2.setText("1 Lọ =");
                break;
            case "Ống":
                spAddQuyDoi1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1, 1));
                spAddQuyDoi1.setEditable(true);
                spAddQuyDoi2.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0, 0));
                spAddQuyDoi2.setEditable(false);
                txtAddQuyDoi1.setText("Ống");
                txtAddTitleQuyDoi1.setText("1 Hộp =");
                txtAddQuyDoi2.setText("");
                txtAddTitleQuyDoi2.setText("");
                break;
            default:
                spAddQuyDoi1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1, 1, 0));
                spAddQuyDoi1.setEditable(true);
                spAddQuyDoi2.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0, 0));
                spAddQuyDoi2.setEditable(false);
                txtAddQuyDoi1.setText("Tuýp");
                txtAddTitleQuyDoi1.setText("1 Hộp =");
                txtAddQuyDoi2.setText("");
                txtAddTitleQuyDoi2.setText("");
                break;
        }



    }
    // Ham quy doi so luong thuoc ve don vi co so
    public static int quyDoiVeCoSo(String maThuoc, int soLuong, int donViHienTai) {
        Map<String, List<QuyDoi>> map = quyDoi_dao.getAllQuyDoi();
        List<QuyDoi> list = map.get(maThuoc);
        int soLuongCoSo = soLuong;
        if (list == null || list.size() == 0) {
            return soLuongCoSo;
        }

        while (true) {
            QuyDoi qd = null;
            for (QuyDoi x : list) {
                if (x.getMaDVTCha().getMaDVT() == donViHienTai) {
                    qd = x;
                    break;
                }
            }
            if (qd == null) {
                break;
            }
            soLuongCoSo *= qd.getTiLeQuyDoi();
            donViHienTai = qd.getMaDVTCon().getMaDVT();
        }
        return soLuongCoSo;
    }

}
