//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller;

import com.antam.app.dao.NhanVien_DAO;
import com.antam.app.entity.NhanVien;
import com.antam.app.gui.ShowDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class EmployeeController {
    @FXML
    private Button btnAddEmployee;

    @FXML
    private TableView<NhanVien> tbNhanVien;
    @FXML
    private Button btnFindNV;
    @FXML
    private TextField textFindNV;
    @FXML
    private TableColumn<NhanVien, String> colMaNV, colHoTen, colChucVu, colSDT, colDiaChi,colEmail;
    @FXML
    private TableColumn<NhanVien, Double> colLuong;

    ArrayList<NhanVien> list = NhanVien_DAO.getDsNhanVienformDBS();

    public EmployeeController() {
    }

    public void initialize() {
        this.btnAddEmployee.setOnAction((e) -> {
            (new ShowDialog("themnhanvien")).showAndWait();
        });
        this.btnFindNV.setOnAction((e)->{

        });
    }

    public void loadNhanVien(){
//        FXMLLoader load = FXMLLoader.get
    }
}
