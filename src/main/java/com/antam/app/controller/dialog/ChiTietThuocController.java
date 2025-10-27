/*
 * @ (#) ChiTietThuocController.java   1.0 10/6/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.controller.dialog;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.ChiTietThuoc_DAO;
import com.antam.app.entity.ChiTietThuoc;
import com.antam.app.entity.Thuoc;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/6/2025
 * version: 1.0
 */
public class ChiTietThuocController {
    @FXML
    private DialogPane dialogPane;
    @FXML
    private Text txtMaThuoc_CTT, txtTenThuoc_CTT;
    @FXML
    private TableView<ChiTietThuoc> tableChiTietThuoc;
    @FXML
    private TableColumn<ChiTietThuoc, String> colMaPN_CTT, colSoLuong_CTT, colNSX_CTT, colNHH_CTT;
    @FXML
    private TableColumn<ChiTietThuoc, Integer> colSTT_CTT;
    private ObservableList<ChiTietThuoc> listChiTietThuoc = FXCollections.observableArrayList();

    private Thuoc thuoc;
    private ChiTietThuoc_DAO chiTietThuoc_dao = new ChiTietThuoc_DAO();

    public void setThuoc(Thuoc thuoc) {
        this.thuoc = thuoc;
    }

    public Thuoc getThuoc(){
        return thuoc;
    }

    public void showData(){
        txtMaThuoc_CTT.setText("Mã Thuốc: " + thuoc.getMaThuoc());
        txtTenThuoc_CTT.setText("Tên Thuốc: " + thuoc.getTenThuoc());


        ArrayList<ChiTietThuoc> list = chiTietThuoc_dao.getAllCHiTietThuocTheoMaThuoc(thuoc.getMaThuoc());
        listChiTietThuoc.addAll(list);
        tableChiTietThuoc.setItems(listChiTietThuoc);
    }


    public void initialize() {
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonBar.ButtonData.CANCEL_CLOSE);
        this.dialogPane.getButtonTypes().add(cancelButton);

        // Kết nối DB
        try { Connection con = ConnectDB.getInstance().connect(); }
        catch (SQLException e) { throw new RuntimeException(e); }

        colSTT_CTT.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(tableChiTietThuoc.getItems().indexOf(cellData.getValue()) + 1)
        );
        colMaPN_CTT.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMaPN().getMaPhieuNhap())
        );
        colSoLuong_CTT.setCellValueFactory(new PropertyValueFactory("soLuong"));
        colNSX_CTT.setCellValueFactory(new PropertyValueFactory("ngaySanXuat"));
        colNHH_CTT.setCellValueFactory(new PropertyValueFactory("hanSuDung"));
    }

}
