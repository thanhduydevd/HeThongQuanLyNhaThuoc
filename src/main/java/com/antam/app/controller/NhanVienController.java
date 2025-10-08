//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller;

import com.antam.app.dao.NhanVien_DAO;
import com.antam.app.entity.NhanVien;
import com.antam.app.gui.ShowDialog;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.util.ArrayList;

public class EmployeeController {
    @FXML
    private Button btnAddEmployee;
    @FXML
    private TableView<NhanVien> tbNhanVien;
    @FXML
    private Button btnFindNV;
    @FXML
    private TextField txtFindNV;
    @FXML
    private TableColumn<NhanVien, String> colMaNV, colHoTen, colChucVu, colSDT, colDiaChi,colEmail;
    @FXML
    private TableColumn<NhanVien, Double> colLuong;


    ArrayList<NhanVien> listNV = NhanVien_DAO.getDsNhanVienformDBS();
    ObservableList<NhanVien> TVNhanVien = FXCollections.observableArrayList();

    public EmployeeController() {
    }

    public void initialize() {
        this.btnAddEmployee.setOnAction((e) -> {
            (new ShowDialog("themnhanvien")).showAndWait();
        });

        btnFindNV.setOnAction( e -> {
            if(!txtFindNV.getText().isBlank()){
                String x = txtFindNV.getText();
                for (NhanVien a : tbNhanVien.getItems()){
                    if (a.getMaNV().toLowerCase().contains(x.toLowerCase())){
                        tbNhanVien.getSelectionModel().select(a);
                        tbNhanVien.scrollTo(a);
                    }
                }
            }
        });

        //setup dữ liệu cho table view
        setupTableNhanVien();
        // Load dữ liệu nhân viên
        loadNhanVien();
        //Thêm sự kiện;
        setupListener();
    }

    private void setupTableNhanVien() {
        colMaNV.setCellValueFactory(t-> new SimpleStringProperty(t.getValue().getMaNV()));
        colHoTen.setCellValueFactory(t-> new SimpleStringProperty(t.getValue().getHoTen()));
        colChucVu.setCellValueFactory(t-> new SimpleStringProperty(t.getValue().isQuanLy()?"Nhân viên quản lý":"Nhân viên"));
        colSDT.setCellValueFactory(t-> new SimpleStringProperty(t.getValue().getSoDienThoai()));
        colDiaChi.setCellValueFactory(t-> new SimpleStringProperty(t.getValue().getDiaChi()));
        colEmail.setCellValueFactory(t-> new SimpleStringProperty(t.getValue().getEmail()));
        colLuong.setCellValueFactory(t -> new SimpleObjectProperty<>(t.getValue().getLuongCoBan()));
    }

    public void loadNhanVien(){
        TVNhanVien = FXCollections.observableArrayList(listNV);
        tbNhanVien.setItems(TVNhanVien);
    }

    public void setupListener(){

        txtFindNV.textProperty().addListener((ob , oldT, newT) -> {
            tbNhanVien.getSelectionModel().clearSelection();
            for (NhanVien a : tbNhanVien.getItems()){
                if (a.getMaNV().toLowerCase().contains(newT.toLowerCase())){
                    tbNhanVien.getSelectionModel().select(a);
                    tbNhanVien.scrollTo(a);
                }
            }
        });

    }
}
