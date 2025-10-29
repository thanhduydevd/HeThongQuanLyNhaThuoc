//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.kethuoc;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.Ke_DAO;
import com.antam.app.entity.Ke;
import com.antam.app.entity.PhieuNhap;
import com.antam.app.gui.GiaoDienCuaSo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TimKeThuocController {

    @FXML
    private TableView<Ke> tbKeThuoc;

    @FXML
    private ComboBox<String> cbLuaChon;

    @FXML
    private Button btnTimKiem, btnXoaRong;

    @FXML
    private TextField tfTimKiem;

    private Ke_DAO ke_DAO = new Ke_DAO();

    /* Lấy dữ liệu từ DAO */
    private ArrayList<Ke> dsKeThuoc = new ArrayList<>();
    private ObservableList<Ke> data = FXCollections.observableArrayList();

    public TimKeThuocController() {

    }

    public void initialize() {
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        dsKeThuoc =  ke_DAO.getAllKe();
        data.setAll(dsKeThuoc);
        tbKeThuoc.setItems(data);

        loadDanhSachKeThuoc();
        //ComboBox lựa chọn
        cbLuaChon.setPromptText("Lựa chọn");
        cbLuaChon.getItems().addAll("Tất cả", "Mã kệ", "Tên kệ", "Loại kệ");

        //Sự kiện nút tìm kiếm
        tfTimKiem.setOnKeyReleased(e -> {
            filterAndSearch();
        });
        btnTimKiem.setOnAction(e ->{
            filterAndSearch();
        });
        //Nút xoá rỗng
        btnXoaRong.setOnAction(e ->{
            tfTimKiem.clear();
            cbLuaChon.getSelectionModel().clearSelection();
            cbLuaChon.setPromptText("Lựa chọn");
            filterAndSearch();
        });
    }

    public void loadDanhSachKeThuoc(){

        /* Tên cột */
        TableColumn<Ke, String> colMaKe = new TableColumn<>("Mã Kệ");
        colMaKe.setCellValueFactory(new PropertyValueFactory<>("MaKe"));

        TableColumn<Ke, String> colTenKe = new TableColumn<>("Tên Kệ");
        colTenKe.setCellValueFactory(new PropertyValueFactory<>("tenKe"));

        TableColumn<Ke, Date> colLoaiKe = new TableColumn<>("Loại Kệ");
        colLoaiKe.setCellValueFactory(new PropertyValueFactory<>("LoaiKe"));

        TableColumn<Ke, String> colTrangThai = new TableColumn<>("Trạng Thái");
        colTrangThai.setCellValueFactory(cellData -> {
            Ke ke = cellData.getValue();
            return new SimpleStringProperty(ke.isDeleteAt() ? "Đã xoá" : "Hoạt động");
        });

        tbKeThuoc.getColumns().addAll(colMaKe, colTenKe, colLoaiKe, colTrangThai);
    }

    public void filterAndSearch() {
        String selectedOption = cbLuaChon.getValue();
        String searchText = tfTimKiem.getText().trim().toLowerCase();

        ObservableList<Ke> filteredData = FXCollections.observableArrayList();

        for (Ke ke : dsKeThuoc) {
            boolean match = false;

            if (selectedOption == null || selectedOption.equals("Tất cả")) {
                match = true;
            } else {
                switch (selectedOption) {
                    case "Mã kệ":
                        match = ke.getMaKe().toLowerCase().contains(searchText);
                        break;
                    case "Tên kệ":
                        match = ke.getTenKe().toLowerCase().contains(searchText);
                        break;
                    case "Loại kệ":
                        match = ke.getLoaiKe().toLowerCase().contains(searchText);
                        break;
                }
            }

            if (match) {
                filteredData.add(ke);
            }
        }

        data.setAll(filteredData);
        tbKeThuoc.setItems(data);
    }


}
