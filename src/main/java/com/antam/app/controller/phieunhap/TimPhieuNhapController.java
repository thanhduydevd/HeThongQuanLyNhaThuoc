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
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

    @FXML
    private Button btnXoaRong;

    private PhieuNhap_DAO phieuNhap_DAO = new PhieuNhap_DAO();
    private NhanVien_DAO nhanVien_DAO = new NhanVien_DAO();

    /* Lấy dữ liệu từ DAO */
    private ArrayList<PhieuNhap> dsPhieuNhap = new ArrayList<>();
    private ObservableList<PhieuNhap> data = FXCollections.observableArrayList();

    private PhieuNhap phieuNhapDuocChon;

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

        //Tuỳ chỉnh field
        dpTuNgay.setPromptText("Chọn ngày");
        dpDenNgay.setPromptText("Chọn ngày");
        //Nút xoá rỗng
        btnXoaRong.setOnAction(e -> {
            cbNhanVienNhap.getSelectionModel().clearSelection();
            dpTuNgay.setValue(null);
            dpDenNgay.setValue(null);
            cbKhoangGia.getSelectionModel().clearSelection();
            tfTimPhieuNhap.clear();
            data.setAll(dsPhieuNhap);
            tbPhieuNhap.setItems(data);
        });

        //Khi click double vào 1 phiếu nhập sẽ hiện chi tiết phiếu nhập
        //Sự kiện khi click table
        tbPhieuNhap.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tbPhieuNhap.setOnMousePressed(e -> {
            PhieuNhap selected = tbPhieuNhap.getSelectionModel().getSelectedItem();
            if (selected != null) {
                phieuNhapDuocChon = new PhieuNhap(
                        selected.getMaPhieuNhap(),
                        selected.getNhaCungCap(),
                        selected.getNgayNhap(),
                        selected.getDiaChi(),
                        selected.getLyDo(),
                        selected.getMaNV(),
                        selected.getTongTien(),
                        selected.isDeleteAt()
                );
                GiaoDienCuaSo dialog = new GiaoDienCuaSo("xemchitietphieunhap");
                XemChiTietPhieuNhapFormController controller = dialog.getController();
                controller.showChiTietPhieuNhap(phieuNhapDuocChon);
                dialog.showAndWait();
            }else{
                showMess("Cảnh báo","Hãy chọn một phiếu nhập để xem chi tiết");
            }
        });
    }

    private void showMess(String canhBao, String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(canhBao);
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }

    public void loadDanhSachNhanVien(){
        ArrayList<NhanVien> dsNhanVien = NhanVien_DAO.getDsNhanVienformDBS();
        for (NhanVien nhanVien : dsNhanVien){
            cbNhanVienNhap.getItems().add(nhanVien);
        }
    }

    public void loadKhoangGia(){
        cbKhoangGia.getItems().addAll("0đ-500.000đ","500.000đ-1.000.000đ","1.000.000đ trở lên");
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
        colHoTenNhanVien.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaNV().getHoTen()));

        TableColumn<PhieuNhap, Double> colTongTien = new TableColumn<>("Tổng tiền");
        colTongTien.setCellValueFactory(new PropertyValueFactory<>("tongTien"));
        colTongTien.setCellFactory(column -> new TableCell<PhieuNhap, Double>() {
            private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(value));
                }
            }
        });

        tbPhieuNhap.getColumns().addAll(colMaPhieuNhap, colNhaCungCap, colNgayNhap, colDiaChi, colLyDo, colHoTenNhanVien, colTongTien);

    }

    public void filterAndSearch(){
        NhanVien selectedNV = cbNhanVienNhap.getValue();
        String maNV = selectedNV != null ? selectedNV.getMaNV() : null;

        LocalDate tuNgay = dpTuNgay.getValue();
        LocalDate denNgay = dpDenNgay.getValue();
        String khoangGia = cbKhoangGia.getValue();
        String maPhieuNhap = tfTimPhieuNhap.getText().trim();

        ArrayList<PhieuNhap> phieuNhap = new ArrayList<>();
        for (PhieuNhap ds : dsPhieuNhap) {
            boolean check = true;

            // lọc theo nhân viên
            if (maNV != null && !maNV.equals(ds.getMaNV().getMaNV())) {
                check = false;
            }

            // lọc theo ngày
            if (tuNgay != null && ds.getNgayNhap().isBefore(tuNgay)) {
                check = false;
            }
            if (denNgay != null && ds.getNgayNhap().isAfter(denNgay)) {
                check = false;
            }

            // lọc theo khoảng giá
            if (khoangGia != null) {
                double tongTien = ds.getTongTien();
                if (khoangGia.equals("0đ-500.000đ") && tongTien > 500000) {
                    check = false;
                } else if (khoangGia.equals("500.000đ-1.000.000đ") && (tongTien < 500000 || tongTien > 1000000)) {
                    check = false;
                } else if (khoangGia.equals("1.000.000đ trở lên") && tongTien < 1000000) {
                    check = false;
                }
            }

            // lọc theo mã phiếu nhập
            if (maPhieuNhap != null && !maPhieuNhap.isEmpty() && !ds.getMaPhieuNhap().contains(maPhieuNhap)) {
                check = false;
            }

            if (check) {
                phieuNhap.add(ds);
            }
        }

        data.setAll(phieuNhap);
        tbPhieuNhap.setItems(data);
    }
}
