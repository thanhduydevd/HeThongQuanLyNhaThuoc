//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller;

import com.antam.app.dao.NhanVien_DAO;
import com.antam.app.entity.NhanVien;
import com.antam.app.gui.GiaoDienCuaSo;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.util.ArrayList;

public class NhanVienController {
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

    public NhanVienController() {
    }

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

        btnFindNV.setOnAction( e -> {
            if(!txtFindNV.getText().isBlank()){
                tbNhanVien.setItems(TVNhanVien);
                String x = txtFindNV.getText();
                for (NhanVien a : tbNhanVien.getItems()){
                    if (a.getMaNV().toLowerCase().contains(x.toLowerCase()) ||
                    a.getHoTen().toLowerCase().contains(x.toLowerCase())){
                        tbNhanVien.getSelectionModel().select(a);
                        tbNhanVien.scrollTo(a);
                    }
                }
            }
        });
        //setup kiểu chọn của tableView
        tbNhanVien.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //setup dữ liệu cho table view
        setupTableNhanVien();
        // Load dữ liệu nhân viên
        loadNhanVien();
        //Thêm sự kiện
        setupListener();
    }

    /**
     * Cài đặt cho các cột ở bảng nhân viên.
     */
    private void setupTableNhanVien() {
        colMaNV.setCellValueFactory(t-> new SimpleStringProperty(t.getValue().getMaNV()));
        colHoTen.setCellValueFactory(t-> new SimpleStringProperty(t.getValue().getHoTen()));
        colChucVu.setCellValueFactory(t-> new SimpleStringProperty(t.getValue().isQuanLy()?"Nhân viên quản lý":"Nhân viên"));
        colSDT.setCellValueFactory(t-> new SimpleStringProperty(t.getValue().getSoDienThoai()));
        colDiaChi.setCellValueFactory(t-> new SimpleStringProperty(t.getValue().getDiaChi()));
        colEmail.setCellValueFactory(t-> new SimpleStringProperty(t.getValue().getEmail()));
        colLuong.setCellValueFactory(t -> new SimpleObjectProperty<>(t.getValue().getLuongCoBan()));
    }

    /**
     * Tải dữ liệu nhân viên lên bảng
     */
    public void loadNhanVien(){
        TVNhanVien = FXCollections.observableArrayList(listNV);
        tbNhanVien.setItems(TVNhanVien);
    }

    /**
     * thực hiện các sự kiện trên giao diện
     */
    public void setupListener() {
        txtFindNV.textProperty().addListener((ob, oldT, newT) -> {
            tbNhanVien.getSelectionModel().clearSelection();

            // Nếu ô tìm kiếm trống -> hiển thị toàn bộ danh sách
            if (newT == null || newT.isBlank()) {
                tbNhanVien.setItems(TVNhanVien);
                return;
            }

            // Tạo danh sách lọc mới mỗi lần nhập
            ObservableList<NhanVien> filteredList = FXCollections.observableArrayList();

            String keyword = newT.toLowerCase();
            for (NhanVien nv : TVNhanVien) {
                if (nv.getMaNV().toLowerCase().contains(keyword) ||
                        nv.getHoTen().toLowerCase().contains(keyword)) {
                    filteredList.add(nv);
                }
            }

            tbNhanVien.setItems(filteredList);
        });
    }
}
