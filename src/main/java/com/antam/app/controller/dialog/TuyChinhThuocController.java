package com.antam.app.controller.dialog;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.*;
import com.antam.app.entity.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TuyChinhThuocController {
    private Thuoc thuoc;

    @FXML
    private DialogPane dialogPane;

    @FXML
    private TextField txtDUMaThuoc, txtDUTenThuoc, txtDUHamLuong;

    @FXML
    private Text txtDUGiaByDV, notification_DUThuoc;


    @FXML
    private Spinner<Double> spDUGiaGoc, spDUGiaBan, spDUThue;


    @FXML
    private ComboBox<DonViTinh> cbDUDVCS;

    @FXML
    private ComboBox<DangDieuChe> cbDUDangDieuChe;

    @FXML
    private ComboBox<Ke> cbDUKe;

    private DangDieuChe_DAO ddc_dao = new DangDieuChe_DAO();
    private DonViTinh_DAO dvt_dao = new DonViTinh_DAO();
    private Thuoc_DAO thuoc_dao = new Thuoc_DAO();
    private Ke_DAO ke_dao = new Ke_DAO();

    public void setThuoc(Thuoc thuoc) {
        this.thuoc = thuoc;
    }

    public Thuoc getThuoc(){
        return thuoc;
    }
    // Hiển thị dữ liệu lên dialog
    public void showData(Thuoc t) {
        txtDUMaThuoc.setText(t.getMaThuoc());
        txtDUMaThuoc.setEditable(false);
        txtDUTenThuoc.setText(t.getTenThuoc());
        cbDUDangDieuChe.setValue(t.getDangDieuChe());
        spDUGiaGoc.getValueFactory().setValue(t.getGiaGoc());
        spDUGiaBan.getValueFactory().setValue(t.getGiaBan());
        spDUThue.getValueFactory().setValue((double) t.getThue());
        txtDUHamLuong.setText(t.getHamLuong());
        cbDUKe.setValue(t.getMaKe());
        cbDUDVCS.setValue(t.getMaDVTCoSo());
        txtDUGiaByDV.setText(dvt_dao.getDVTTheoMa(t.getMaDVTCoSo().getMaDVT()).getTenDVT());
    }


    @FXML
    public void initialize() {
        // ket noi csdl
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // them button vao dialog
        ButtonType closeButton = new ButtonType("Đóng", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType applyButtonUpdate = new ButtonType("Sửa", ButtonBar.ButtonData.APPLY);
        ButtonType applyButtonDelete = new ButtonType("Xóa", ButtonBar.ButtonData.APPLY);
        // them button vao dialog
        dialogPane.getButtonTypes().addAll(applyButtonUpdate, applyButtonDelete, closeButton);
        // su kien button sua
        Button applyBtnUpdate = (Button) dialogPane.lookupButton(applyButtonUpdate);
        applyBtnUpdate.addEventFilter(ActionEvent.ACTION, event -> {
            boolean isValid = validate();

            if (!isValid) {
                event.consume();
            }else {
                String maThuoc = txtDUMaThuoc.getText();
                String tenThuoc = txtDUTenThuoc.getText();
                DonViTinh donViCoSo = cbDUDVCS.getValue();
                DangDieuChe dangDieuChe =  cbDUDangDieuChe.getValue();
                String hamLuong = txtDUHamLuong.getText();
                Double giaGoc = spDUGiaGoc.getValue();
                Double giaBan = spDUGiaBan.getValue();
                Double thue = spDUThue.getValue();
                Ke ke = cbDUKe.getValue();
                Thuoc thuoc = new Thuoc(maThuoc, tenThuoc, hamLuong, giaBan, giaGoc, thue.floatValue(), false,
                        dangDieuChe, donViCoSo, ke);
                boolean success = thuoc_dao.capNhatThuoc(thuoc);

            }

        });
        // su kien button xoa
        Button applyBtnDelete = (Button) dialogPane.lookupButton(applyButtonDelete);
        applyBtnDelete.addEventFilter(ActionEvent.ACTION, event -> {
            boolean isValid = showConfirmDeleteDialog(getThuoc().getTenThuoc());

            if (!isValid) {
                event.consume();
            } else {
                if (thuoc_dao.xoaThuocTheoMa(getThuoc().getMaThuoc()) == false) {
                    notification_DUThuoc.setText("Xóa thuốc thất bại!");
                    event.consume();
                }
            }
        });


        // su kien up down spinner gia goc, gia ban, thue
        SpinnerValueFactory.DoubleSpinnerValueFactory valueFactoryGiaGoc =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(
                        0,
                        1_000_000,
                        0,
                        1000
                );

        spDUGiaGoc.setValueFactory(valueFactoryGiaGoc);
        spDUGiaGoc.setEditable(true);

        SpinnerValueFactory.DoubleSpinnerValueFactory valueFactoryGiaBan =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(
                        0,
                        1_000_000,
                        0,
                        1000
                );
        spDUGiaBan.setValueFactory(valueFactoryGiaBan);
        spDUGiaBan.setEditable(true);

        SpinnerValueFactory.DoubleSpinnerValueFactory valueFactoryThue =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(
                        0,
                        1,
                        0,
                        0.01
                );
        spDUThue.setValueFactory(valueFactoryThue);
        spDUThue.setEditable(true);
        //them combo box ke
        addComBoBoxKe();
        // them combo box don vi co so
        addComBoBoxDVCS();
        //them combo box dang dieu che
        addComBoBoxDDC();
        cbDUDVCS.setOnAction(e -> {
            txtDUGiaByDV.setText(cbDUDVCS.getValue().getTenDVT());
        });
    }
    // hien thi hop thoai xac nhan xoa
    public boolean showConfirmDeleteDialog(String tenThuoc) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc muốn xóa thuốc: " + tenThuoc + "?");
        alert.setContentText("Hành động này không thể hoàn tác.");

        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == ButtonType.OK;
    }
    // ham kiem tra du lieu hop le
    public boolean validate(){
        String maThuoc = txtDUMaThuoc.getText();
        String tenThuoc = txtDUTenThuoc.getText();
        String donViCoSo = cbDUDVCS.getValue().getTenDVT();
        String dangDieuChe = cbDUDangDieuChe.getValue().getTenDDC();
        String hamLuong = txtDUHamLuong.getText();
        Double giaGoc = 0.0;
        Double giaBan = 0.0;
        Double thue = 0.0;
        String ke = cbDUKe.getValue().getTenKe();

        if(maThuoc.isEmpty()){
            notification_DUThuoc.setText("Vui lòng điền mã thuốc!");
            return false;
        }else{
            if (maThuoc.matches("^VN-\\d{5}-\\d{2}$")) {
                notification_DUThuoc.setText("");
            } else {
                notification_DUThuoc.setText("Mã thuốc không hợp lệ! (VD: VN-12345-01)");
                return false;
            }
        }
        if (tenThuoc.isEmpty()){
            notification_DUThuoc.setText("Vui lòng điền đầy đủ tên thuốc!");
            return false;
        }
        if (donViCoSo == null){
            notification_DUThuoc.setText("Vui lòng điền đầy đủ đơn vị cơ sở!");
            return false;
        }
        if (dangDieuChe == null){
            notification_DUThuoc.setText("Vui lòng điền dạng điều chế!");
            return false;
        }
        if (hamLuong.isEmpty()){
            notification_DUThuoc.setText("Vui lòng điền đầy đủ hàm lượng!");
            return false;
        }
        try {
            giaGoc = spDUGiaGoc.getValue();
            if (giaGoc <= 0) {
                notification_DUThuoc.setText("Giá gốc phải lớn hơn 0!");
                return false;
            }
        } catch (Exception e) {
            notification_DUThuoc.setText("Giá gốc không hợp lệ!");
            return false;
        }
        try {
            giaBan = spDUGiaBan.getValue();
            if (giaBan <= 0) {
                notification_DUThuoc.setText("Giá bán phải lớn hơn 0!");
                return false;
            }
        } catch (Exception e) {
            notification_DUThuoc.setText("Giá bán không hợp lệ!");
            return false;
        }
        try {
            thue = spDUThue.getValue();
            if (thue < 0) {
                notification_DUThuoc.setText("Thuế không được âm!");
                return false;
            }
        } catch (Exception e) {
            notification_DUThuoc.setText("Thuế không hợp lệ!");
            return false;
        }
        if (ke == null){
            notification_DUThuoc.setText("Vui lòng điền đầy đủ thông tin!");
            return false;
        }
        return true;
    }

    // them value vao combobox ke
    public void addComBoBoxKe() {
        ArrayList<Ke> arrayKe = ke_dao.getAllKe();
        cbDUKe.getItems().clear();
        for (Ke ke : arrayKe) {
            cbDUKe.getItems().add(ke);
        }
        cbDUKe.getSelectionModel().selectFirst();
    }

    // Them value vao combobox dang dieu che
    public void addComBoBoxDDC() {
        ArrayList<DangDieuChe> arrayDDC = ddc_dao.getAllDDC();
        cbDUDangDieuChe.getItems().clear();
        for (DangDieuChe ddc : arrayDDC){
            cbDUDangDieuChe.getItems().add(ddc);
        }
        cbDUDangDieuChe.getSelectionModel().selectFirst();
    }

    // Them value vao combobox don vi tinh
    public void addComBoBoxDVCS() {
        ArrayList<DonViTinh> arrayDVT = dvt_dao.getAllDonViTinh();
        cbDUDVCS.getItems().clear();
        arrayDVT.forEach(dvt -> cbDUDVCS.getItems().add(dvt));
        cbDUDVCS.getSelectionModel().selectFirst();
        txtDUGiaByDV.setText(cbDUDVCS.getValue().toString());
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

}
