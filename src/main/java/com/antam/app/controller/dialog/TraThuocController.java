//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.dialog;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.ChiTietHoaDon_DAO;
import com.antam.app.dao.HoaDon_DAO;
import com.antam.app.dao.KhachHang_DAO;
import com.antam.app.dao.Thuoc_DAO;
import com.antam.app.entity.ChiTietHoaDon;
import com.antam.app.entity.HoaDon;
import com.antam.app.entity.Thuoc;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.util.ArrayList;

public class TraThuocController {
    @FXML
    private DialogPane dialogPane;
    @FXML
    private VBox vbListChiTietHoaDon;
    @FXML
    private TextField txtMaHoaDonTra, txtKhachHangTra;
    @FXML
    private Text txtTongTienTra;


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

    public void showData(HoaDon hoaDon) {
        HoaDon hd = hoaDon_dao.getHoaDonTheoMa(hoaDon.getMaHD());
        ArrayList<ChiTietHoaDon> chiTietHoaDons = chiTietHoaDon_dao.getAllChiTietHoaDonTheoMaHD(hoaDon.getMaHD());
        txtMaHoaDonTra.setText(hoaDon.getMaHD());
        txtKhachHangTra.setText(khachHang_dao.getKhachHangTheoMa(hd.getMaKH().getMaKH()).getTenKH());
        for (ChiTietHoaDon ct : chiTietHoaDons) {
            HBox hBox = renderChiTietHoaDon(ct);
            vbListChiTietHoaDon.getChildren().add(hBox);
        }
    }

    public void initialize() {
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Xác nhận trả thuốc", ButtonData.APPLY);
        this.dialogPane.getButtonTypes().add(cancelButton);
        this.dialogPane.getButtonTypes().add(applyButton);

        Button applyBtn = (Button) dialogPane.lookupButton(applyButton);

        applyBtn.addEventFilter(ActionEvent.ACTION, event -> {
            if (selectedItems.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Chưa chọn thuốc để trả");
                alert.setContentText("Vui lòng chọn ít nhất một thuốc để trả.");
                alert.showAndWait();
                event.consume();
            } else {
                for (ChiTietHoaDon ct : selectedItems) {
                    chiTietHoaDon_dao.xoaMemChiTietHoaDon(ct.getMaHD().getMaHD(), ct.getMaThuoc().getMaThuoc(), "Trả");
                }
                if (chiTietHoaDon_dao.getAllChiTietHoaDonTheoMaHDConBan(hoaDon.getMaHD()).isEmpty()) {
                    hoaDon_dao.xoaMemHoaDon(hoaDon.getMaHD());
                    hoaDon_dao.CapNhatTongTienHoaDon(hoaDon.getMaHD(), 0);
                } else {
                    double tongTienMoi = 0;
                    ArrayList<ChiTietHoaDon> cths = chiTietHoaDon_dao.getAllChiTietHoaDonTheoMaHDConBan(hoaDon.getMaHD());
                    for (ChiTietHoaDon ct : cths) {
                        tongTienMoi += ct.getThanhTien();
                    }
                    hoaDon_dao.CapNhatTongTienHoaDon(hoaDon.getMaHD(), tongTienMoi);
                }
            }
        });

        try{
            Connection con = ConnectDB.getInstance().getConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void tinhTongTienTra(){
        double tongTien = 0;
        for (ChiTietHoaDon ct : selectedItems){
            tongTien += ct.getThanhTien();
        }
        txtTongTienTra.setText(String.valueOf(tongTien));
    }


    public HBox renderChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setStyle(
                "-fx-background-color: #f8fafc;" +
                "-fx-padding: 10;"
        );

        CheckBox checkBox = new CheckBox();
        if (chiTietHoaDon.getTinhTrang().equals("Trả") || chiTietHoaDon.getTinhTrang().equals("Đổi")) {
            checkBox.setDisable(true);
        }
        checkBox.setOnAction(event -> {
            if (checkBox.isSelected()) {
                selectedItems.add(chiTietHoaDon);
            } else {
                selectedItems.remove(chiTietHoaDon);
            }
            tinhTongTienTra();
        });
        Thuoc t = thuoc_dao.getThuocTheoMa(chiTietHoaDon.getMaThuoc().getMaThuoc());
        Text txtMaThuoc = new Text(t.getMaThuoc());
        txtMaThuoc.setStyle("-fx-font-size: 15px;");
        Text txtSoLuong = new Text(String.valueOf(chiTietHoaDon.getSoLuong()));
        txtSoLuong.setStyle("-fx-font-size: 15px;");
        Text txtDonGia = new Text(String.valueOf(chiTietHoaDon.getThanhTien()));
        txtDonGia.setStyle("-fx-font-size: 15px;");
        String valueBtn = "Bình thường";
        if (chiTietHoaDon.getTinhTrang().equals("Trả")) {
            valueBtn = "Đã trả";
        } else if (chiTietHoaDon.getTinhTrang().equals("Đổi")) {
            valueBtn = "Đã đổi";
        }
        Button btn = new Button(valueBtn);
        btn.setStyle(
                "-fx-background-color: #e0f2fe;" +
                "-fx-text-fill: #0369a1;" +
                "-fx-cursor: 10px;" +
                "-fx-font: 12px;"
        );

        hBox.getChildren().addAll(checkBox, txtMaThuoc, txtSoLuong, txtDonGia, btn);
        return hBox;
    }

}
