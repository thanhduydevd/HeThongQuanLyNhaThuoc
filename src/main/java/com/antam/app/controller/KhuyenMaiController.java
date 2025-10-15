//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller;

import com.antam.app.controller.dialog.ChiTietThuocController;
import com.antam.app.controller.dialog.TuyChinhKhuyenMaiController;
import com.antam.app.dao.KhuyenMai_DAO;
import com.antam.app.dao.Thuoc_DAO;
import com.antam.app.entity.KhuyenMai;
import com.antam.app.entity.Thuoc;
import com.antam.app.gui.GiaoDienCuaSo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.ArrayList;

public class KhuyenMaiController {
    @FXML
    private Button btnAddPromotion;
    @FXML
    private ComboBox<String> cbLoaiKhuyenMai, cbTrangThai;
    @FXML
    private DatePicker dpTuNgay, dpDenNgay;
    @FXML
    private TextField txtTiemKiemKhuyenMai;
    @FXML
    private Button btnTimKiem;
    @FXML
    private TableView<KhuyenMai> tableKhuyenMai;
    @FXML
    private TableColumn<KhuyenMai, String> colMaKhuyenMai, colTenKhuyenMai, colLoaiKhuyenMai, colSo, colSoLuongToiDa, colTinhTrang;
    private ObservableList<KhuyenMai> khuyenMaiList = FXCollections.observableArrayList();
    private ArrayList<KhuyenMai> arrayKhuyenMai = new ArrayList<>();
    private KhuyenMai_DAO khuyenMai_dao = new KhuyenMai_DAO();
    public KhuyenMaiController() {
    }

    public void initialize() {
        this.btnAddPromotion.setOnAction((e) -> {
            new GiaoDienCuaSo("themkhuyenmai").showAndWait();
            this.updateTableKhuyenMai();
        });

        // cau hinh table
        colMaKhuyenMai.setCellValueFactory(new PropertyValueFactory<>("MaKM"));
        colTenKhuyenMai.setCellValueFactory(new PropertyValueFactory<>("TenKM"));
        colLoaiKhuyenMai.setCellValueFactory(celldata -> {
            KhuyenMai km = celldata.getValue();
            if (km.getLoaiKhuyenMai() != null) {
                return new SimpleStringProperty(km.getLoaiKhuyenMai().getTenLKM());
            } else {
                return new SimpleStringProperty("Không xác định");
            }
        });
        colSo.setCellValueFactory(new PropertyValueFactory<>("so"));
        colSoLuongToiDa.setCellValueFactory(new PropertyValueFactory<>("soLuongToiDa"));
        colTinhTrang.setCellValueFactory(cellData -> {
            KhuyenMai km = cellData.getValue();
            if (LocalDate.now().isBefore(km.getNgayBatDau())) {
                return new SimpleStringProperty("Chưa bắt đầu");
            } else if (LocalDate.now().isAfter(km.getNgayKetThuc())) {
                return new SimpleStringProperty("Đã kết thúc");
            } else {
                return new SimpleStringProperty("Đang diễn ra");
            }
        });
        // load du lieu
        khuyenMai_dao = new KhuyenMai_DAO();
        arrayKhuyenMai = khuyenMai_dao.getAllKhuyenMaiChuaXoa();
        khuyenMaiList.setAll(arrayKhuyenMai);
        tableKhuyenMai.setItems(khuyenMaiList);
        // them combobox
        addCombobox();
        // su kien loc va tim kiem
        btnTimKiem.setOnAction(e -> fiterAndSearch());
        cbLoaiKhuyenMai.setOnAction(e -> fiterAndSearch());
        cbTrangThai.setOnAction(e -> fiterAndSearch());
        dpTuNgay.setOnAction(e -> fiterAndSearch());
        dpDenNgay.setOnAction(e -> fiterAndSearch());
        txtTiemKiemKhuyenMai.setOnKeyReleased(e -> fiterAndSearch());

        tableKhuyenMai.setRowFactory(tv -> {
            TableRow<KhuyenMai> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    KhuyenMai selectKM  = row.getItem();
                    // Mở dialog
                    GiaoDienCuaSo dialog = new GiaoDienCuaSo("tuychinhkhuyenmai");
                    // Lấy controller và set Thuoc vào
                    TuyChinhKhuyenMaiController controller = dialog.getController();
                    controller.setKhuyenMai(selectKM);
                    controller.showdata(selectKM);
                    // Show dialog
                    dialog.showAndWait();
                    updateTableKhuyenMai();
                }
            });
            return row;
        });
    }

    public void addCombobox() {
        cbLoaiKhuyenMai.getItems().addAll("Tất cả", "Giảm theo phần trăm", "Giảm theo số tiền");
        cbLoaiKhuyenMai.getSelectionModel().selectFirst();

        cbTrangThai.getItems().addAll("Tất cả", "Chưa bắt đầu", "Đang diễn ra", "Đã kết thúc");
        cbTrangThai.getSelectionModel().selectFirst();
    }

    public void fiterAndSearch() {
        String loaiKhuyenMai = cbLoaiKhuyenMai.getValue();
        String trangThai = cbTrangThai.getValue();
        LocalDate tuNgay = dpTuNgay.getValue();
        LocalDate denNgay = dpDenNgay.getValue();
        String tuKhoa = txtTiemKiemKhuyenMai.getText().toLowerCase();
        ObservableList<KhuyenMai> filteredList = FXCollections.observableArrayList();
        for (KhuyenMai km : arrayKhuyenMai) {
            boolean matchesLoai = loaiKhuyenMai.equals("Tất cả") ||
                    (loaiKhuyenMai.equals("Giảm theo phần trăm") && km.getLoaiKhuyenMai().getTenLKM().equals("Giảm theo phần trăm")) ||
                    (loaiKhuyenMai.equals("Giảm theo số tiền") && km.getLoaiKhuyenMai().getTenLKM().equals("Giảm theo số tiền"));

            boolean matchesTrangThai = trangThai.equals("Tất cả") ||
                    (trangThai.equals("Chưa bắt đầu") && LocalDate.now().isBefore(km.getNgayBatDau())) ||
                    (trangThai.equals("Đang diễn ra") && !LocalDate.now().isBefore(km.getNgayBatDau()) && !LocalDate.now().isAfter(km.getNgayKetThuc())) ||
                    (trangThai.equals("Đã kết thúc") && LocalDate.now().isAfter(km.getNgayKetThuc()));

            boolean matchesTuNgay = tuNgay == null || !km.getNgayKetThuc().isBefore(tuNgay);
            boolean matchesDenNgay = denNgay == null || !km.getNgayBatDau().isAfter(denNgay);

            boolean matchesTuKhoa = tuKhoa.isEmpty() || km.getMaKM().toLowerCase().contains(tuKhoa) || km.getTenKM().toLowerCase().contains(tuKhoa);

            if (matchesLoai && matchesTrangThai && matchesTuNgay && matchesDenNgay && matchesTuKhoa) {
                filteredList.add(km);
            }
        }
        khuyenMaiList.clear();
        khuyenMaiList.addAll(filteredList);
        tableKhuyenMai.setItems(khuyenMaiList);
    }
    public void updateTableKhuyenMai(){
        khuyenMaiList.clear();
        tableKhuyenMai.refresh();
        arrayKhuyenMai = khuyenMai_dao.getAllKhuyenMaiChuaXoa();
        khuyenMaiList.addAll(arrayKhuyenMai);
        tableKhuyenMai.setItems(khuyenMaiList);
    }
}
