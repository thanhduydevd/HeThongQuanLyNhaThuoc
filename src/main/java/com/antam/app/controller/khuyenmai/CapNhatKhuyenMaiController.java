//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.khuyenmai;

import com.antam.app.connect.ConnectDB;
import com.antam.app.controller.dialog.TuyChinhKhuyenMaiController;
import com.antam.app.dao.KhuyenMai_DAO;
import com.antam.app.entity.KhuyenMai;
import com.antam.app.gui.GiaoDienCuaSo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;

public class CapNhatKhuyenMaiController {
    @FXML
    private Button btnTuyChon;
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
    public CapNhatKhuyenMaiController() {
    }

    public void initialize() {
        try {
            Connection con = ConnectDB.getInstance().connect();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.btnTuyChon.setOnAction((e) -> {
            // Lấy khuyến mãi được chọn
            KhuyenMai selectKM = tableKhuyenMai.getSelectionModel().getSelectedItem();
            if (selectKM == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Chưa chọn khuyến mãi");
                alert.setContentText("Vui lòng chọn ít nhất một khuyến mãi.");
                alert.showAndWait();
                return;
            }
            // Mở dialog
            GiaoDienCuaSo dialog = new GiaoDienCuaSo("capnhatkhuyenmai");
            // Lấy controller và set Thuoc vào
            CapNhatKhuyenMaiFormController controller = dialog.getController();
            controller.setKhuyenMai(selectKM);
            controller.showdata(selectKM);
            // Show dialog
            dialog.showAndWait();
            updateTableKhuyenMai();
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
        String tuKhoa = txtTiemKiemKhuyenMai.getText().trim().toLowerCase();

        if (tuNgay != null && denNgay != null && denNgay.isBefore(tuNgay)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Lỗi nhập ngày");
            alert.setHeaderText(null);
            alert.setContentText("Ngày kết thúc không được nhỏ hơn ngày bắt đầu!");
            alert.showAndWait();

            khuyenMaiList.clear();
            tableKhuyenMai.setItems(khuyenMaiList);
            return;
        }

        ObservableList<KhuyenMai> filteredList = FXCollections.observableArrayList();

        for (KhuyenMai km : arrayKhuyenMai) {
            boolean matchesLoai =
                    loaiKhuyenMai.equals("Tất cả") ||
                            (loaiKhuyenMai.equals("Giảm theo phần trăm") && km.getLoaiKhuyenMai().getTenLKM().equals("Giảm theo phần trăm")) ||
                            (loaiKhuyenMai.equals("Giảm theo số tiền") && km.getLoaiKhuyenMai().getTenLKM().equals("Giảm theo số tiền"));

            LocalDate today = LocalDate.now();
            boolean matchesTrangThai =
                    trangThai.equals("Tất cả") ||
                            (trangThai.equals("Chưa bắt đầu") && today.isBefore(km.getNgayBatDau())) ||
                            (trangThai.equals("Đang diễn ra") && !today.isBefore(km.getNgayBatDau()) && !today.isAfter(km.getNgayKetThuc())) ||
                            (trangThai.equals("Đã kết thúc") && today.isAfter(km.getNgayKetThuc()));

            boolean matchesNgay = true;
            if (tuNgay != null && denNgay != null) {
                matchesNgay = !(km.getNgayKetThuc().isBefore(tuNgay) || km.getNgayBatDau().isAfter(denNgay));
            } else if (tuNgay != null) {
                matchesNgay = !km.getNgayKetThuc().isBefore(tuNgay);
            } else if (denNgay != null) {
                matchesNgay = !km.getNgayBatDau().isAfter(denNgay);
            }

            boolean matchesTuKhoa =
                    tuKhoa.isEmpty() ||
                            km.getMaKM().toLowerCase().contains(tuKhoa) ||
                            km.getTenKM().toLowerCase().contains(tuKhoa);

            if (matchesLoai && matchesTrangThai && matchesNgay && matchesTuKhoa) {
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
