package com.antam.app.controller.nhanvien;

import com.antam.app.dao.NhanVien_DAO;
import com.antam.app.entity.NhanVien;
import com.antam.app.gui.GiaoDienCuaSo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ThemNhanVienController {
    @FXML
    private TableView<NhanVien> tbNhanVien;
    @FXML
    private Button btnFindNV,btnXoaTrang,btnAddEmployee;
    @FXML
    private TextField txtFindNV;
    @FXML
    private TableColumn<NhanVien, String> colMaNV, colHoTen, colChucVu, colSDT, colDiaChi, colEmail;
    @FXML
    private TableColumn<NhanVien, String> colLuong;
    @FXML
    private ComboBox<String> cbChucVu, cbLuongCB;
    ArrayList<NhanVien> listNV = NhanVien_DAO.getDsNhanVienformDBS();

    private ObservableList<NhanVien> TVNhanVien;
    private final ObservableList<NhanVien> filteredList = FXCollections.observableArrayList();

    public void initialize() {
        this.btnAddEmployee.setOnAction((e) -> {
            (new GiaoDienCuaSo("themnhanvien")).showAndWait().ifPresent(result -> {
                if ("Lưu".equals(result)) {
                    listNV = NhanVien_DAO.getDsNhanVienformDBS();
                } else {

                }
            });
            listNV = NhanVien_DAO.getDsNhanVienformDBS();
            loadNhanVien();
        });
        setupTableNhanVien();
        loadNhanVien();
        loadComboBox();
        setupListener();

        btnFindNV.setOnAction(e -> timNhanVien());
        setupListener();

        tbNhanVien.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        btnXoaTrang.setOnAction(e -> {
            txtFindNV.clear();
            cbChucVu.getSelectionModel().selectFirst();
            cbLuongCB.getSelectionModel().selectFirst();
            tbNhanVien.setItems(TVNhanVien);
        });
    }

    private void loadNhanVien() {
        listNV = NhanVien_DAO.getDsNhanVienformDBS();
        TVNhanVien = FXCollections.observableArrayList(
                listNV.stream()
                        .filter(nv -> !nv.isDeleteAt())
                        .toList()
        );
        tbNhanVien.setItems(TVNhanVien);
    }


    private void setupTableNhanVien() {
        colMaNV.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getMaNV()));
        colHoTen.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getHoTen()));
        colChucVu.setCellValueFactory(t -> new SimpleStringProperty(
                t.getValue().isQuanLy() ? "Nhân viên quản lý" : "Nhân viên"));
        colSDT.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getSoDienThoai()));
        colDiaChi.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getDiaChi()));
        colEmail.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getEmail()));
        colLuong.setCellValueFactory(t -> new SimpleStringProperty(dinhDangTien(t.getValue().getLuongCoBan())));
    }

    private void loadComboBox() {
        cbChucVu.setItems(FXCollections.observableArrayList(
                "Tất cả", "Nhân viên", "Nhân viên quản lý"));
        cbChucVu.getSelectionModel().selectFirst();
        cbLuongCB.setItems(FXCollections.observableArrayList(
                "Tất cả", "Dưới 5 triệu", "Từ 5 triệu đến 10 triệu", "Trên 10 triệu"));
        cbLuongCB.getSelectionModel().selectFirst();
    }

    private void setupListener() {
        // Khi người dùng gõ tìm kiếm
        txtFindNV.textProperty().addListener((ob, oldT, newT) -> filterNhanVien());

        // Khi chọn chức vụ
        cbChucVu.setOnAction(e -> filterNhanVien());

        // Khi chọn mức lương
        cbLuongCB.setOnAction(e -> filterNhanVien());
    }

    /**
     * Lọc nhân viên theo nhiều điều kiện: tìm kiếm, chức vụ và mức lương
     */
    private void filterNhanVien() {
        filteredList.clear();

        String keyword = txtFindNV.getText() == null ? "" : txtFindNV.getText().toLowerCase();
        String chucVu = cbChucVu.getSelectionModel().getSelectedItem();
        String luongCB = cbLuongCB.getSelectionModel().getSelectedItem();

        for (NhanVien nv : TVNhanVien) {
            boolean matchKeyword = keyword.isBlank() ||
                    nv.getMaNV().toLowerCase().contains(keyword) ||
                    nv.getHoTen().toLowerCase().contains(keyword);

            boolean matchChucVu =
                    chucVu.equals("Tất cả") ||
                            (chucVu.equals("Nhân viên") && !nv.isQuanLy()) ||
                            (chucVu.equals("Nhân viên quản lý") && nv.isQuanLy());

            double l = nv.getLuongCoBan();
            boolean matchLuong =
                    luongCB.equals("Tất cả") ||
                            (luongCB.equals("Dưới 5 triệu") && l < 5_000_000) ||
                            (luongCB.equals("Từ 5 triệu đến 10 triệu") && l >= 5_000_000 && l <= 10_000_000) ||
                            (luongCB.equals("Trên 10 triệu") && l > 10_000_000);

            if (matchKeyword && matchChucVu && matchLuong) {
                filteredList.add(nv);
            }
        }

        tbNhanVien.setItems(filteredList.isEmpty() && !keyword.isBlank() ? FXCollections.observableArrayList() : filteredList);
        if (filteredList.isEmpty() && keyword.isBlank() && chucVu.equals("Tất cả") && luongCB.equals("Tất cả")) {
            tbNhanVien.setItems(TVNhanVien);
        }
    }


    private void timNhanVien() {
        String x = txtFindNV.getText().trim().toLowerCase();
        if (x.isEmpty()) return;

        for (NhanVien a : tbNhanVien.getItems()) {
            if (a.getMaNV().toLowerCase().contains(x) ||
                    a.getHoTen().toLowerCase().contains(x)) {
                tbNhanVien.getSelectionModel().select(a);
                tbNhanVien.scrollTo(a);
            }
        }
    }

    private String dinhDangTien(double tien) {
        DecimalFormat df = new DecimalFormat("#,### đ");
        return df.format(tien);
    }
}
