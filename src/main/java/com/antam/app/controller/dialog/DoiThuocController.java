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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


import java.util.ArrayList;
import java.util.List;

import static com.antam.app.controller.dialog.ThemThuocController.quyDoiVeCoSo;

public class DoiThuocController {
    @FXML
    private DialogPane dialogPane;

    @FXML
    private VBox vhDSCTHD, vhDSCTHDM;
    @FXML
    private TextField txtMaHoaDonDoi, txtKhachHangDoi;
    @FXML
    private Text txtTongTienDoi, txtTongTienTra, txtTongTienMua;
    @FXML
    private Button btnAddThuocMoi;

    private Thuoc_DAO thuoc_dao = new Thuoc_DAO();
    private HoaDon_DAO hoaDon_dao = new HoaDon_DAO();
    private KhachHang_DAO khachHang_dao = new KhachHang_DAO();
    private ChiTietHoaDon_DAO chiTietHoaDon_dao = new ChiTietHoaDon_DAO();
    private HoaDon hoaDon;
    private ArrayList<ChiTietHoaDon> selectedItems = new ArrayList<>();
    private ArrayList<ChiTietHoaDon> chiTietHoaDons;

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public HoaDon getHoaDOn() {
        return hoaDon;
    }

    public void showData(HoaDon hoaDon) {
        HoaDon hd = hoaDon_dao.getHoaDonTheoMa(hoaDon.getMaHD());
        chiTietHoaDons = chiTietHoaDon_dao.getAllChiTietHoaDonTheoMaHD(hoaDon.getMaHD());
        for (ChiTietHoaDon ct : chiTietHoaDons) {
            HBox hBox = renderChiTietHoaDon(ct);
            vhDSCTHD.getChildren().add(hBox);
        }
    }

    public void initialize() {
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Xác nhận đổi thuốc", ButtonData.APPLY);
        this.dialogPane.getButtonTypes().add(cancelButton);
        this.dialogPane.getButtonTypes().add(applyButton);

        Button btnApply = (Button) this.dialogPane.lookupButton(applyButton);
        btnApply.addEventFilter(ActionEvent.ACTION, event -> {
            if (selectedItems.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Chưa chọn thuốc để đổi");
                alert.setContentText("Vui lòng chọn ít nhất một thuốc để đổi.");
                alert.showAndWait();
                event.consume();
            }
            if (vhDSCTHDM.getChildren().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Chưa thêm thuốc mới để đổi");
                alert.setContentText("Vui lòng thêm ít nhất một thuốc mới để đổi.");
                alert.showAndWait();
                event.consume();
            }
            else {
                for (ChiTietHoaDon ct : selectedItems) {
                    chiTietHoaDon_dao.xoaMemChiTietHoaDon(ct.getMaHD().getMaHD(), ct.getMaThuoc().getMaThuoc(), "Đổi");
                }
                for (Node node : vhDSCTHDM.getChildren()) {
                    if (node instanceof HBox hBox) {
                        ComboBox<Thuoc> comboThuoc = (ComboBox<Thuoc>) hBox.getChildren().get(0);
                        ComboBox<DonViTinh> comboDonVi = (ComboBox<DonViTinh>) hBox.getChildren().get(1);
                        Spinner<Integer> spinnerSoLuong = (Spinner<Integer>) hBox.getChildren().get(2);

                        ChiTietHoaDon cthd = new ChiTietHoaDon(
                                new HoaDon(hoaDon.getMaHD()),
                                comboThuoc.getValue(),
                                spinnerSoLuong.getValue(),
                                comboDonVi.getValue(),
                                "Bán"
                        );
                        int soLuongTon = thuoc_dao.getThuocTheoMa(cthd.getMaThuoc().getMaThuoc()).getTonKho();
                        int soLuongMua = quyDoiVeCoSo(cthd.getMaThuoc().getMaThuoc(), cthd.getSoLuong(), cthd.getMaDVT().getMaDVT());
                        if (soLuongMua > soLuongTon) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Cảnh báo");
                            alert.setHeaderText("Số lượng thuốc trong kho không đủ");
                            alert.setContentText("Số lượng thuốc " + cthd.getMaThuoc().getMaThuoc() + " trong kho không đủ. Vui lòng kiểm tra lại.");
                            alert.showAndWait();
                            event.consume();
                            return;
                        }
                        chiTietHoaDon_dao.themChiTietHoaDon(cthd);
                        thuoc_dao.congVaoTonKho(cthd.getMaThuoc().getMaThuoc(), -cthd.getSoLuong());
                    }
                }

            }
        });
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

        Button btn = new Button("Xóa");
        btn.setStyle(
                "-fx-background-color: #ef4444;" +
                "-fx-cursor: 50%;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;"
        );

        btn.setOnAction(event -> {
            vbox.getChildren().remove(hBox);
        });
        hBox.getChildren().addAll(comboBoxThuoc, comboBoxDVT, spinnerSoLuong, btn);
    }

}
