package com.antam.app.controller;

import com.antam.app.connect.ConnectDB;
import com.antam.app.controller.dialog.XoaSuaThuocController;
import com.antam.app.dao.DangDieuChe_DAO;
import com.antam.app.dao.Ke_DAO;
import com.antam.app.dao.Thuoc_DAO;
import com.antam.app.entity.DangDieuChe;
import com.antam.app.entity.Ke;
import com.antam.app.entity.Thuoc;
import com.antam.app.gui.GiaoDienCuaSo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class ThuocController {

    private Ke_DAO ke_dao;
    private DangDieuChe_DAO ddc_dao;
    private Thuoc_DAO thuoc_dao;

    @FXML private Button btnAddMedicine;
    @FXML private ComboBox<Ke> cbKe;
    @FXML private ComboBox<DangDieuChe> cbDangDieuChe;
    @FXML private ComboBox<String>cbTonKho, cbHanSuDung;
    @FXML private TextField searchNameThuoc;
    @FXML private Button btnSearchThuoc;
    @FXML private TableView<Thuoc> tableThuoc;
    @FXML private TableColumn<Thuoc, String> colMaThuoc, colTenThuoc, colHamLuong, colDangDieuChe, colGiaBan, colHanSuDung, colKe;
    @FXML private TableColumn<Thuoc, Integer> colTonKho;

    private ObservableList<Thuoc> thuocList = FXCollections.observableArrayList();
    private ArrayList<Thuoc> arrayThuoc = new ArrayList<>();

    public void initialize() {
        // Nút thêm thuốc
        btnAddMedicine.setOnAction(e -> {
            new GiaoDienCuaSo("themthuoc").showAndWait();
            updateTable();
        });


        // Kết nối DB
        try { Connection con = ConnectDB.getInstance().connect(); }
        catch (SQLException e) { throw new RuntimeException(e); }

        // Cấu hình Table
        colMaThuoc.setCellValueFactory(new PropertyValueFactory<>("maThuoc"));
        colTenThuoc.setCellValueFactory(new PropertyValueFactory<>("tenThuoc"));
        colHamLuong.setCellValueFactory(new PropertyValueFactory<>("hamLuong"));
        colGiaBan.setCellValueFactory(new PropertyValueFactory<>("giaBan"));
        colTonKho.setCellValueFactory(new PropertyValueFactory<>("tonKho"));
        colHanSuDung.setCellValueFactory(new PropertyValueFactory<>("hanSuDung"));
        colDangDieuChe.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDangDieuChe().getTenDDC()));
        colKe.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaKe().getTenKe()));

        // Load comboBox
        addComBoBoxKe();
        addComBoBoxDDC();
        addComboboxTonKho();
        addComboboxHanSuDung();

        // Load dữ liệu
        thuoc_dao = new Thuoc_DAO();
        arrayThuoc = thuoc_dao.getAllThuoc();
        thuocList.addAll(arrayThuoc);
        tableThuoc.setItems(thuocList);

        // Event lọc
        cbKe.setOnAction(e -> filterAndSearchThuoc());
        cbDangDieuChe.setOnAction(e -> filterAndSearchThuoc());
        cbTonKho.setOnAction(e -> filterAndSearchThuoc());
        cbHanSuDung.setOnAction(e -> filterAndSearchThuoc());

        // Event tìm kiếm
        btnSearchThuoc.setOnAction(e -> filterAndSearchThuoc());
        searchNameThuoc.setOnKeyReleased(e -> filterAndSearchThuoc());
        // su kien table view
        tableThuoc.setRowFactory(tv -> {
            TableRow<Thuoc> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Thuoc selectedThuoc = row.getItem();
                    // Mở dialog
                    GiaoDienCuaSo dialog = new GiaoDienCuaSo("thongtinthuoc");
                    // Lấy controller và set Thuoc vào
                    XoaSuaThuocController controller = dialog.getController();
                    controller.setThuoc(selectedThuoc);
                    controller.showData(selectedThuoc);
                    // Show dialog
                    dialog.showAndWait();
                    updateTable();
                }
            });
            return row;
        });


    }

    // them value vao combobox ke
    public void addComBoBoxKe() {
        ke_dao = new Ke_DAO();
        ArrayList<Ke> arrayKe = ke_dao.getAllKe();
        cbKe.getItems().clear();
        Ke tatCa = new Ke("KE0001", "Tất cả", "Tất cả", false);
        cbKe.getItems().add(tatCa);
        for (Ke ke : arrayKe) {
            cbKe.getItems().add(ke);
        }
        cbKe.getSelectionModel().selectFirst();
    }

    // them value vao combobox dang dieu che
    public void addComBoBoxDDC() {
        ddc_dao = new DangDieuChe_DAO();
        ArrayList<DangDieuChe> arrayDDC = ddc_dao.getAllDDC();
        cbDangDieuChe.getItems().clear();
        DangDieuChe Tatca = new DangDieuChe(-1, "Tất cả");
        cbDangDieuChe.getItems().add(Tatca);
        for (DangDieuChe ddc : arrayDDC){
            cbDangDieuChe.getItems().add(ddc);
        }
        cbDangDieuChe.getSelectionModel().selectFirst();
    }

    // them value vao combobox ton kho
    public void addComboboxTonKho() {
        cbTonKho.getItems().clear();
        cbTonKho.getItems().addAll("Tất cả","Tồn kho thấp (< 50)","Bình thường (50-200)","Dồi dào (> 200)");
        cbTonKho.getSelectionModel().selectFirst();
    }

    // them value vao combobox Han su dung
    public void addComboboxHanSuDung() {
        cbHanSuDung.getItems().clear();
        cbHanSuDung.getItems().addAll("Tất cả","Đã hết hạn","Sắp hết hạn (< 30 ngày)","Trong 3 tháng","Còn lâu hết hạn");
        cbHanSuDung.getSelectionModel().selectFirst();
    }

    // ham loc va tim kiem thuoc
    public void filterAndSearchThuoc() {
        String selectedKe = cbKe.getSelectionModel().getSelectedItem().getTenKe();
        String selectedDDC = cbDangDieuChe.getSelectionModel().getSelectedItem().getTenDDC();
        String selectedTonKho = (String) cbTonKho.getSelectionModel().getSelectedItem();
        String selectedHanSuDung = (String) cbHanSuDung.getSelectionModel().getSelectedItem();
        String searchText = searchNameThuoc.getText().trim().toLowerCase();

        ObservableList<Thuoc> filteredList = FXCollections.observableArrayList();

        for (Thuoc p : arrayThuoc) { // luôn thao tác trên danh sách gốc
            boolean match = true;

            // Filter Ke
            if (!selectedKe.equals("Tất cả") && !p.getMaKe().getTenKe().equals(selectedKe)) match = false;

            // Filter DDC
            if (!selectedDDC.equals("Tất cả") && !p.getDangDieuChe().getTenDDC().equals(selectedDDC)) match = false;

            // Filter TonKho
            if (!selectedTonKho.equals("Tất cả")) {
                if (selectedTonKho.equals("Tồn kho thấp (< 50)") && p.getTonKho() >= 50) match = false;
                else if (selectedTonKho.equals("Bình thường (50-200)") && (p.getTonKho() < 50 || p.getTonKho() > 200)) match = false;
                else if (selectedTonKho.equals("Dồi dào (> 200)") && p.getTonKho() <= 200) match = false;
            }

            // Filter HanSuDung
            if (!selectedHanSuDung.equals("Tất cả")) {
                LocalDate today = LocalDate.now();
                LocalDate expiry = p.getHanSuDung();
                long daysToExpiry = ChronoUnit.DAYS.between(today, expiry);

                switch (selectedHanSuDung) {
                    case "Đã hết hạn": match = daysToExpiry < 0; break;
                    case "Sắp hết hạn (< 30 ngày)": match = daysToExpiry >= 0 && daysToExpiry < 30; break;
                    case "Trong 3 tháng": match = daysToExpiry >= 30 && daysToExpiry <= 90; break;
                    case "Còn lâu hết hạn": match = daysToExpiry > 90; break;
                }
            }

            // Search theo tên
            if (!searchText.isEmpty() && !p.getTenThuoc().toLowerCase().contains(searchText)) match = false;

            if (match) filteredList.add(p);
        }

        thuocList.setAll(filteredList);
        tableThuoc.setItems(thuocList);
    }

    // ham update Table
    public void updateTable(){
        ArrayList<Thuoc> listThuoc = thuoc_dao.getAllThuoc();
        thuocList.clear();
        tableThuoc.refresh();
        thuocList.setAll(listThuoc);
        tableThuoc.setItems(thuocList);
    }

}
