//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.phieudat;

import com.antam.app.dao.NhanVien_DAO;
import com.antam.app.dao.PhieuDat_DAO;
import com.antam.app.entity.NhanVien;
import com.antam.app.entity.PhieuDatThuoc;
import com.antam.app.gui.GiaoDienCuaSo;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ThemPhieuDatController {
    @FXML
    private Button btnAddPurchaseOrder;
    @FXML
    private ComboBox<NhanVien> cbNhanVien;
    @FXML
    private ComboBox<String> cbTrangThai,cbGia;
    @FXML
    private DatePicker dpstart,dpend;
    @FXML
    private TextField txtFind;
    @FXML
    private Button btnFind;
    @FXML
    private TableView<PhieuDatThuoc> tvPhieuDat;
    @FXML
    private TableColumn<PhieuDatThuoc,String> colTotal,colMaPhieu,colNgay,colKhach,colSDT,colNhanVien,colStatus;


    ArrayList<PhieuDatThuoc> listPDT = PhieuDat_DAO.getAllPhieuDatThuocFromDBS();
    ArrayList<NhanVien> listNV = NhanVien_DAO.getDsNhanVienformDBS();
    ObservableList<PhieuDatThuoc> origin;
    ObservableList<PhieuDatThuoc> filter= FXCollections.observableArrayList();

    public ThemPhieuDatController() {
    }

    public void initialize() {
        this.btnAddPurchaseOrder.setOnAction((e) -> {
            (new GiaoDienCuaSo("themphieudat")).showAndWait();
        });

        //cài đặt và load data vào giao diện
        loadDataComboBox();
        setupBang();
        loadDataVaoBang();

        //sự kiện nút tìm kiếm phiếu đặt
        btnFind.setOnAction(e->{
            if(!txtFind.getText().isBlank()){
                tvPhieuDat.setItems(origin);
                for (PhieuDatThuoc a : tvPhieuDat.getItems()){
                    if (a.getMaPhieu().toLowerCase().contains(txtFind.getText().toLowerCase())
                    ||a.getKhachHang().getTenKH().toLowerCase().contains(txtFind.getText().toLowerCase())){
                        tvPhieuDat.getSelectionModel().select(a);
                        tvPhieuDat.scrollTo(a);
                    }
                }
            }
        });
        //sự kiện lọc
        setupListenerFind();
        cbGia.setOnAction(e -> setupListenerComboBox());
        cbTrangThai.setOnAction(e -> setupListenerComboBox());
        cbNhanVien.setOnAction(e -> setupListenerComboBox());
        dpstart.setOnAction(e-> setupListenerComboBox());
        dpend.setOnAction(e->setupListenerComboBox());
    }

    private void setupListenerComboBox() {
        // Lấy lựa chọn hiện tại
        String gia = cbGia.getSelectionModel().getSelectedItem();
        String trangThai = cbTrangThai.getSelectionModel().getSelectedItem();
        NhanVien nv = cbNhanVien.getSelectionModel().getSelectedItem();
        LocalDate start = dpstart.getValue();
        LocalDate end = dpend.getValue();

        // Nếu tất cả đều là "Tất cả" hoặc chưa chọn gì => hiển thị gốc
        if ((gia == null || gia.equals("Tất cả")) &&
                (trangThai == null || trangThai.equals("Tất cả")) &&
                (nv == null || nv.getHoTen().equals("Tất cả")) &&
                (start == null && end == null)) {
            loadDataVaoBang();
//            System.out.println("All đk");
            return;
        }

        // Xác định khoảng giá
        double min, max;
        switch (gia) {
            case "Dưới 500.000đ": min = 0; max = 500000; break;
            case "Từ 500.000đ đến 1.000.000đ": min = 500000; max = 1000000; break;
            case "Từ 1.000.000đ đến 2.000.000đ": min = 1000000; max = 2000000; break;
            case "Trên 2.000.000đ": min = 2000000; max = Double.MAX_VALUE; break;
            default: min = 0; max = Double.MAX_VALUE;
        }

        // Xác định trạng thái cần lọc
        Boolean filStatus = null;
        if ("Đã thanh toán".equals(trangThai)) filStatus = true;
        else if ("Chờ thanh toán".equals(trangThai)) filStatus = false;

        // Bắt đầu lọc
        ObservableList<PhieuDatThuoc> filter = FXCollections.observableArrayList();

        for (PhieuDatThuoc e : origin) {
            boolean match = true;

            // Lọc theo giá
            if (!(e.getTongTien() >= min && e.getTongTien() <= max)) match = false;

            // Lọc theo nhân viên
            if (nv != null && nv.getHoTen() != null &&
                    !nv.getMaNV().equals("Tất cả") &&
                    !e.getNhanVien().getMaNV().equals(nv.getMaNV())) match = false;

            // Lọc theo trạng thái
            if (filStatus != null && e.isThanhToan() != filStatus) match = false;

            if (match) filter.add(e);
            // Lọc theo ngày (giả sử e.getNgayLap() là LocalDate)
            if (start != null && e.getNgayTao().isBefore(start))
                match = false;
            if (end != null && e.getNgayTao().isAfter(end))
                match = false;

            if (match) filter.add(e);
        }

        tvPhieuDat.setItems(filter);
    }

    private void setupListenerFind() {
        txtFind.textProperty().addListener( (obj, oldT ,newT) ->{
            tvPhieuDat.getSelectionModel().clearSelection();
            if(newT.isBlank()){
                tvPhieuDat.setItems(filter);
                return ;
            }
            ObservableList<PhieuDatThuoc> filter1 = FXCollections.observableArrayList(filter);
            String key = newT.toLowerCase();
            for (PhieuDatThuoc e : listPDT){
                if (e.getMaPhieu().toLowerCase().contains(key)
                ||e.getKhachHang().getTenKH().toLowerCase().contains(key)){
                    filter1.add(e);
                }else{
                    filter1.remove(e);
                }
            }
            tvPhieuDat.setItems(filter1);
        });
    }

    private void setupBang() {
        colMaPhieu.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getMaPhieu()));
        colNgay.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getNgayTao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        colKhach.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getKhachHang().getTenKH()));
        colSDT.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getKhachHang().getSoDienThoai()));
        colNhanVien.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getNhanVien().getHoTen()));
        colStatus.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().isThanhToan() ? "Đã thanh toán":"Chưa thanh toán"));
        colTotal.setCellValueFactory(t -> new SimpleStringProperty( dinhDangTien(t.getValue().getTongTien()) ));
    }
    private String dinhDangTien(double tien){
        DecimalFormat df = new DecimalFormat("#,### đ");
        return df.format(tien);
    }

    private void loadDataVaoBang() {
        origin = FXCollections.observableArrayList(listPDT);
        filter = FXCollections.observableArrayList(origin);
        tvPhieuDat.setItems(filter);
    }


    public void loadDataComboBox(){
        NhanVien all = new NhanVien("Tất cả",false);
        cbNhanVien.getItems().add(all);
        for (NhanVien e : listNV){
            cbNhanVien.getItems().add(e);
        }
        cbTrangThai.getItems().add("Tất cả");
        cbTrangThai.getItems().add("Chờ thanh toán");
        cbTrangThai.getItems().add("Đã thanh toán");
        cbGia.getItems().add("Tất cả");
        cbGia.getItems().add("Dưới 500.000đ");
        cbGia.getItems().add("Từ 500.000đ đến 1.000.000đ");
        cbGia.getItems().add("Từ 1.000.000đ đến 2.000.000đ");
        cbGia.getItems().add("Trên 2.000.000đ");
        cbGia.getSelectionModel().selectFirst();
        cbNhanVien.getSelectionModel().selectFirst();
        cbTrangThai.getSelectionModel().selectFirst();
    }


}
