package com.antam.app.controller;

import com.antam.app.connect.ConnectDB;
import com.antam.app.controller.dialog.ChiTietThuocController;
import com.antam.app.controller.dialog.XoaSuaThuocController;
import com.antam.app.dao.ChiTietThuoc_DAO;
import com.antam.app.dao.DangDieuChe_DAO;
import com.antam.app.dao.Ke_DAO;
import com.antam.app.dao.Thuoc_DAO;
import com.antam.app.entity.ChiTietThuoc;
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
import java.util.ArrayList;
import java.util.HashMap;

public class ThuocController {

    private Ke_DAO ke_dao;
    private DangDieuChe_DAO ddc_dao;
    private Thuoc_DAO thuoc_dao;
    private ChiTietThuoc_DAO chiTietThuoc_dao;
    private HashMap<String, Integer> mapTonKho = new HashMap<>();

    @FXML private Button btnAddMedicine;
    @FXML private ComboBox<Ke> cbKe;
    @FXML private ComboBox<DangDieuChe> cbDangDieuChe;
    @FXML private ComboBox<String>cbTonKho;
    @FXML private TextField searchNameThuoc;
    @FXML private Button btnSearchThuoc, btnXoaSua;
    @FXML private TableView<Thuoc> tableThuoc;
    @FXML private TableColumn<Thuoc, String> colMaThuoc, colTenThuoc, colHamLuong, colDangDieuChe, colGiaBan, colKe;
    @FXML private TableColumn<Thuoc, String> colTonKho;

    private ObservableList<Thuoc> thuocList = FXCollections.observableArrayList();
    private ArrayList<Thuoc> arrayThuoc = new ArrayList<>();

    public void initialize() {
        // Nút thêm thuốc
        btnAddMedicine.setOnAction(e -> {
            new GiaoDienCuaSo("themthuoc").showAndWait();
            updateTable();
            loadTonKho();
        });

        btnXoaSua.setOnAction(e ->{
            Thuoc selectedThuoc = tableThuoc.getSelectionModel().getSelectedItem();

            if (selectedThuoc == null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Chưa chọn thuốc");
                alert.setContentText("Vui lòng chọn ít nhất một thuốc.");
                alert.showAndWait();
            }else{
                GiaoDienCuaSo dialog = new GiaoDienCuaSo("xoasuathuoc");
                // Lấy controller và set Thuoc vào
                XoaSuaThuocController controller = dialog.getController();
                controller.setThuoc(selectedThuoc);
                controller.showData(selectedThuoc);
                // Show dialog
                dialog.showAndWait();
                updateTable();
                loadTonKho();
            }
        });

        loadTonKho();

        // Kết nối DB
        try { Connection con = ConnectDB.getInstance().connect(); }
        catch (SQLException e) { throw new RuntimeException(e); }

        // Cấu hình Table
        colMaThuoc.setCellValueFactory(new PropertyValueFactory<>("maThuoc"));
        colTenThuoc.setCellValueFactory(new PropertyValueFactory<>("tenThuoc"));
        colHamLuong.setCellValueFactory(new PropertyValueFactory<>("hamLuong"));
        colGiaBan.setCellValueFactory(new PropertyValueFactory<>("giaBan"));
        colTonKho.setCellValueFactory(data -> {
            int tonKho = TinhTonKho(data.getValue());
            return new SimpleStringProperty(String.valueOf(tonKho));
        });

        colDangDieuChe.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDangDieuChe().getTenDDC()));
        colKe.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaKe().getTenKe()));

        // Load comboBox
        addComBoBoxKe();
        addComBoBoxDDC();
        addComboboxTonKho();

        // Load dữ liệu
        thuoc_dao = new Thuoc_DAO();
        arrayThuoc = thuoc_dao.getAllThuoc();
        thuocList.addAll(arrayThuoc);
        tableThuoc.setItems(thuocList);

        // Event lọc
        cbKe.setOnAction(e -> filterAndSearchThuoc());
        cbDangDieuChe.setOnAction(e -> filterAndSearchThuoc());
        cbTonKho.setOnAction(e -> filterAndSearchThuoc());

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
                    ChiTietThuocController controller = dialog.getController();
                    controller.setThuoc(selectedThuoc);
                    controller.showData();
                    // Show dialog
                    dialog.showAndWait();
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


    // ham loc va tim kiem thuoc
    public void filterAndSearchThuoc() {
        String selectedKe = cbKe.getSelectionModel().getSelectedItem().getTenKe();
        String selectedDDC = cbDangDieuChe.getSelectionModel().getSelectedItem().getTenDDC();
        String selectedTonKho = (String) cbTonKho.getSelectionModel().getSelectedItem();
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
                if (selectedTonKho.equals("Tồn kho thấp (< 50)") && TinhTonKho(p) >= 50) match = false;
                else if (selectedTonKho.equals("Bình thường (50-200)") && TinhTonKho(p) < 50 || TinhTonKho(p) > 200) match = false;
                else if (selectedTonKho.equals("Dồi dào (> 200)") && TinhTonKho(p) <= 200) match = false;
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

    public void loadTonKho() {
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        chiTietThuoc_dao = new ChiTietThuoc_DAO();
        ArrayList<ChiTietThuoc> list = chiTietThuoc_dao.getAllChiTietThuoc();
        mapTonKho.clear();

        for (ChiTietThuoc ct : list) {
            String maThuoc = ct.getMaThuoc().getMaThuoc();
            mapTonKho.put(maThuoc, mapTonKho.getOrDefault(maThuoc, 0) + ct.getSoLuong());
        }
    }


    public int TinhTonKho(Thuoc thuoc) {
        return mapTonKho.getOrDefault(thuoc.getMaThuoc(), 0);
    }


}
