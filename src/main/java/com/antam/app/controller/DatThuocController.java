//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller;

import com.antam.app.dao.NhanVien_DAO;
import com.antam.app.dao.PhieuDat_DAO;
import com.antam.app.entity.NhanVien;
import com.antam.app.entity.PhieuDatThuoc;
import com.antam.app.gui.GiaoDienCuaSo;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DatThuocController {
    @FXML
    private Button btnAddPurchaseOrder;
    @FXML
    private ComboBox<NhanVien> cbNhanVien;
    @FXML
    private ComboBox<String> cbTrangThai,cbGia;
    @FXML
    private DatePicker DPstart,DPend;
    @FXML
    private TextField txtFind;
    @FXML
    private Button btnFind;
    @FXML
    private TableView<PhieuDatThuoc> tvPhieuDat;
    @FXML
    private TableColumn<PhieuDatThuoc,String> colMaPhieu,colNgay,colKhach,colSDT,colNhanVien,colStatus;
    @FXML
    private TableColumn<PhieuDatThuoc,Double> colTotal;

    ArrayList<PhieuDatThuoc> listPDT = PhieuDat_DAO.getAllPhieuDatThuocFromDBS();
    ArrayList<NhanVien> listNV = NhanVien_DAO.getDsNhanVienformDBS();
    ObservableList<PhieuDatThuoc> obPDT;
    public DatThuocController() {
    }

    public void initialize() {
        this.btnAddPurchaseOrder.setOnAction((e) -> {
            (new GiaoDienCuaSo("themphieudat")).showAndWait();
        });
        loadDataComboBox();
        setupBang();
        loadDataVaoBang();
    }

    private void setupBang() {
        colMaPhieu.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getMaPhieu()));
        colNgay.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getNgayTao().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        colKhach.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getKhachHang().getTenKH()));
        colSDT.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getKhachHang().getSoDienThoai()));
        colNhanVien.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getNhanVien().getMaNV()));
        colTotal.setCellValueFactory(t -> new SimpleDoubleProperty(t.getValue().getTongTien()).asObject());
        colStatus.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().isThanhToan() ? "Đã thanh toán":"Chưa thanh toán"));
    }

    private void loadDataVaoBang() {
        obPDT = FXCollections.observableArrayList(listPDT);
        tvPhieuDat.setItems(obPDT);
    }


    public void loadDataComboBox(){
        for (NhanVien e : listNV){
            cbNhanVien.getItems().add(e);
        }
        cbTrangThai.getItems().add("Chờ thanh toán");
        cbTrangThai.getItems().add("Đã thanh toán");
        cbGia.getItems().add("Dưới 500.000đ");
        cbGia.getItems().add("Từ 500.000đ đến 1.000.000đ");
        cbGia.getItems().add("Từ 1.000.000đ đến 2.000.000đ");
    }


}
