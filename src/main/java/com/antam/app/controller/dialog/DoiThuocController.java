//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.dialog;

import com.antam.app.dao.ChiTietHoaDon_DAO;
import com.antam.app.dao.HoaDon_DAO;
import com.antam.app.dao.KhachHang_DAO;
import com.antam.app.dao.Thuoc_DAO;
import com.antam.app.entity.ChiTietHoaDon;
import com.antam.app.entity.DonViTinh;
import com.antam.app.entity.HoaDon;
import com.antam.app.entity.Thuoc;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;



import java.util.ArrayList;
import java.util.List;

public class DoiThuocController {
    @FXML
    private DialogPane dialogPane;

    @FXML
    private VBox vhDSCTHD, vhDSCTHDM;

    private Thuoc_DAO thuoc_dao = new Thuoc_DAO();
    private HoaDon_DAO hoaDon_dao = new HoaDon_DAO();
    private KhachHang_DAO khachHang_dao = new KhachHang_DAO();
    private ChiTietHoaDon_DAO chiTietHoaDon_dao = new ChiTietHoaDon_DAO();
    private HoaDon hoaDon;
    private ArrayList<ChiTietHoaDon> selectedItems = new ArrayList<>();

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public HoaDon getHoaDOn() {
        return hoaDon;
    }


    public void initialize() {
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Xác nhận đổi thuốc", ButtonData.APPLY);
        this.dialogPane.getButtonTypes().add(cancelButton);
        this.dialogPane.getButtonTypes().add(applyButton);
    }

    public HBox renderChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setStyle(
                "-fx-background-color: #f8fafc;" +
                        "-fx-padding: 10;"
        );

        CheckBox checkBox = new CheckBox();
        checkBox.setOnAction(event -> {
            if (checkBox.isSelected()) {
                selectedItems.add(chiTietHoaDon);
            } else {
                selectedItems.remove(chiTietHoaDon);
            }
        });
        Thuoc t = thuoc_dao.getThuocTheoMa(chiTietHoaDon.getMaThuoc().getMaThuoc());
        Text txtMaThuoc = new Text(t.getMaThuoc());
        txtMaThuoc.setStyle("-fx-font: 15px;");
        Text txtSoLuong = new Text(String.valueOf(chiTietHoaDon.getSoLuong()));
        txtSoLuong.setStyle("-fx-font: 15px;");
        Text txtDonGia = new Text(String.valueOf(chiTietHoaDon.getThanhTien()));
        txtDonGia.setStyle("-fx-font: 15px;");
        Button btn = new Button("Bình thường");
        btn.setStyle(
                "-fx-background-color: #e0f2fe;" +
                        "-fx-text-fill: #0369a1;" +
                        "-fx-cursor: 10px;" +
                        "-fx-font: 12px;"
        );

        hBox.getChildren().addAll(checkBox, txtMaThuoc, txtSoLuong, txtDonGia, btn);
        return hBox;
    }

    public void renderChiTietHoaDonDoi(VBox vbox) {
        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setStyle(
                "-fx-background-color: #f8fafc;" +
                        "-fx-padding: 10;"
        );

        ComboBox<Thuoc> comboBoxThuoc = new ComboBox<>();
        comboBoxThuoc.getStylesheets().add(getClass().getResource("com\\antam\\app\\styles\\dashboard_style.css").toExternalForm());

        ComboBox<DonViTinh> comboBoxDVT = new ComboBox<>();
        comboBoxDVT.getStylesheets().add(getClass().getResource("com\\antam\\app\\styles\\dashboard_style.css").toExternalForm());

        Spinner<Integer> spinnerSoLuong = new Spinner<>();
        spinnerSoLuong.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 0, 1));

        Button btn = new Button();
        btn.setStyle(
                "-fx-background-color: #ef4444;" +
                "-fx-cursor: 50%;"
        );

        btn.setOnAction(event -> {
            vbox.getChildren().remove(hBox);
        });


    }

//    public ArrayList<ChiTietHoaDon> getCTHDM() {
//        List<ChiTietHoaDon> danhSachChiTiet = new ArrayList<>();
//
//        for (Node node : .getChildren()) {
//            if (node instanceof HBox hBox) {
//                ComboBox<String> comboThuoc = (ComboBox<String>) hBox.getChildren().get(0);
//                ComboBox<String> comboDonVi = (ComboBox<String>) hBox.getChildren().get(1);
//                Spinner<Integer> spinnerSoLuong = (Spinner<Integer>) hBox.getChildren().get(2);
//
//                ChiTietHoaDon cthd = new ChiTietHoaDon(
//                        comboThuoc.getValue(),
//                        comboDonVi.getValue(),
//                        spinnerSoLuong.getValue()
//                );
//
//                danhSachChiTiet.add(cthd);
//            }
//        }
//
//
//    }

}
