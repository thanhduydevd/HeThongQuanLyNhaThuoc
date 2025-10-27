//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.phieunhap;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.NhanVien_DAO;
import com.antam.app.dao.PhieuNhap_DAO;
import com.antam.app.entity.NhanVien;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class TimPhieuNhapController {

    @FXML
    private TableView<PhieuNhap> tbPhieuNhap;

    @FXML
    private ComboBox<NhanVien> cbNhanVienNhap;

    @FXML
    private DatePicker dpTuNgay, dpDenNgay;

    @FXML
    private ComboBox<String> cbKhoangGia;

    @FXML
    private TextField tfTimPhieuNhap;

    private PhieuNhap_DAO phieuNhap_DAO = new PhieuNhap_DAO();
    private NhanVien_DAO nhanVien_DAO = new NhanVien_DAO();

    /* Lấy dữ liệu từ DAO */
    private ArrayList<PhieuNhap> dsPhieuNhap = new ArrayList<>();
    private ObservableList<PhieuNhap> data = FXCollections.observableArrayList();

    public TimPhieuNhapController() {

    }

    public void initialize() {
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        loadDanhSachPhieuNhap();
        loadDanhSachNhanVien();
        loadKhoangGia();

        dsPhieuNhap = phieuNhap_DAO.getDanhSachPhieuNhap();
        data.setAll(dsPhieuNhap);
        tbPhieuNhap.setItems(data);

        cbNhanVienNhap.setOnAction(e -> filterAndSearch());
        dpTuNgay.setOnAction(e -> filterAndSearch());
        dpDenNgay.setOnAction(e -> filterAndSearch());
        cbKhoangGia.setOnAction(e -> filterAndSearch());
        tfTimPhieuNhap.setOnKeyReleased(e -> filterAndSearch());
    }

    public void loadDanhSachNhanVien(){
        ArrayList<NhanVien> dsNhanVien = NhanVien_DAO.getDsNhanVienformDBS();
        for (NhanVien nhanVien : dsNhanVien){
            cbNhanVienNhap.getItems().add(nhanVien);
        }
    }

    public void loadKhoangGia(){
        cbKhoangGia.getItems().addAll("0-100.000d","100.000d-300.000d","300.000d-tro di");
    }


    public void loadDanhSachPhieuNhap(){

        /* Tên cột */
        TableColumn<PhieuNhap, String> colMaPhieuNhap = new TableColumn<>("Mã Phiếu Nhập");
        colMaPhieuNhap.setCellValueFactory(new PropertyValueFactory<>("maPhieuNhap"));

        TableColumn<PhieuNhap, String> colNhaCungCap = new TableColumn<>("Nhà Cung Cấp");
        colNhaCungCap.setCellValueFactory(new PropertyValueFactory<>("nhaCungCap"));

        TableColumn<PhieuNhap, Date> colNgayNhap = new TableColumn<>("Ngày Nhập");
        colNgayNhap.setCellValueFactory(new PropertyValueFactory<>("ngayNhap"));

        TableColumn<PhieuNhap, String> colDiaChi = new TableColumn<>("Địa Chỉ");
        colDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));

        TableColumn<PhieuNhap, String> colLyDo = new TableColumn<>("Lý Do");
        colLyDo.setCellValueFactory(new PropertyValueFactory<>("lyDo"));

        TableColumn<PhieuNhap, String> colHoTenNhanVien = new TableColumn<>("Nhân Viên");
        colHoTenNhanVien.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaNV().getMaNV()));

        TableColumn<PhieuNhap, Double> colTongTien = new TableColumn<>("Tổng tiền");
        colTongTien.setCellValueFactory(new PropertyValueFactory<>("tongTien"));

        tbPhieuNhap.getColumns().addAll(colMaPhieuNhap, colNhaCungCap, colNgayNhap, colDiaChi, colLyDo, colHoTenNhanVien, colTongTien);

    }

    public void filterAndSearch(){
        String tenNhanVien = cbNhanVienNhap.getValue().getMaNV();
        LocalDate tuNgay = dpTuNgay.getValue();
        LocalDate denNgay = dpDenNgay.getValue();
        String khoangGia = cbKhoangGia.getValue();
        String maPhieuNhap = tfTimPhieuNhap.getText().trim();

        ArrayList<PhieuNhap> phieuNhap = new ArrayList();
        for (PhieuNhap ds : dsPhieuNhap){
            boolean check = true;
            if(!tenNhanVien.isEmpty() && !tenNhanVien.equals(ds.getMaNV().getMaNV())){
                check = false;
            }

            if(tuNgay != null && ds.getNgayNhap().isAfter(tuNgay)){
                check = false;
            }

            if(denNgay != null && ds.getNgayNhap().isBefore(denNgay)){
                check = false;
            }

            if(khoangGia != null){
                if(khoangGia.equals("0-100.000d") && ds.getTongTien() > 100000){
                    check = false;
                }else if(khoangGia.equals("100.000d-300.000d") && ds.getTongTien() < 100000 && ds.getTongTien() > 300000){
                    check = false;
                }else if(khoangGia.equals("300.000d-tro di") && ds.getTongTien() < 300000){
                    check = false;
                }
            }

            if(maPhieuNhap != null && !ds.getMaPhieuNhap().contains(maPhieuNhap)){
                    check = false;
            }

            if(check){
                phieuNhap.add(ds);
            }
        }

        data.clear();
        data.setAll(phieuNhap);
        tbPhieuNhap.setItems(data);

    }
}
