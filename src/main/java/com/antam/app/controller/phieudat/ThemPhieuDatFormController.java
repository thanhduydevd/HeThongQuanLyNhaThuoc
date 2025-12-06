//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.phieudat;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.*;
import com.antam.app.entity.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;

public class ThemPhieuDatFormController {
    @FXML
    private DialogPane dialogPane;
    @FXML
    private TextField txtMa,txtTenKhach,txtDonGia;
    @FXML
    private Button btnThem;
    @FXML
    private Text txtTotal;
    @FXML
    private ComboBox<KhuyenMai> cbKhuyenMai;
    @FXML
    private ComboBox<DonViTinh> cbDonVi;
    @FXML
    private ComboBox<Thuoc> cbTenThuoc;
    @FXML
    private Spinner<Integer> spSoLuong;
    @FXML
    private TableView<ChiTietPhieuDatThuoc> tbChonThuoc;
    @FXML
    private VBox vbThuoc;
    @FXML
    private TableColumn<ChiTietPhieuDatThuoc, String> colTenThuoc, colDonVi,  colSoLuong,colDonGia,colThanhTien;

    private Thuoc_DAO thuoc_dao = new Thuoc_DAO();
    private  DonViTinh_DAO donViTinh_dao = new DonViTinh_DAO();
    private ArrayList<Thuoc> dsThuoc;
    private ArrayList<DonViTinh> dsDonViTinh;
    private DecimalFormat decimalFormat = new DecimalFormat("#,### đ");
    private ArrayList<ChiTietPhieuDatThuoc> list = new ArrayList<>();
    private ObservableList<ChiTietPhieuDatThuoc> obsThuoc = FXCollections.observableArrayList();


    KhachHang_DAO khachHangDAO = new KhachHang_DAO();
    ArrayList<KhachHang> dsKhach = khachHangDAO.getAllKhachHang();
    KhuyenMai_DAO KhuyenMai_DAO = new KhuyenMai_DAO();
    ArrayList<KhuyenMai> dsKhuyenMai = (ArrayList<KhuyenMai>) KhuyenMai_DAO.getAllKhuyenMaiConHieuLuc();
    DonViTinh_DAO dvtDAO = new DonViTinh_DAO();

    public ThemPhieuDatFormController() {
    }

    public void initialize() {
        //tạo cổng kết nối
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //setup phụ
        txtDonGia.setEditable(false);

        //load ds đơn vị tính.
        dsDonViTinh = donViTinh_dao.getTatCaDonViTinh();
        dsThuoc = thuoc_dao.getAllThuoc();
        cbDonVi.getItems().addAll(FXCollections.observableArrayList(dsDonViTinh));

        // load comboBox thuốc
        cbTenThuoc.getItems().addAll(FXCollections.observableArrayList(dsThuoc));

        // setup mã phiếu
        txtMa.setText(getHashPD());
        txtMa.setEditable(false);

        //cài đặt nút lưu huỷ
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Lưu", ButtonData.APPLY);
        this.dialogPane.getButtonTypes().add(cancelButton);
        this.dialogPane.getButtonTypes().add(applyButton);

        // load ComboBox Khuyến mãi.
        KhuyenMai nothing = new KhuyenMai("","Không áp dụng");
        cbKhuyenMai.getItems().add(nothing);
        cbKhuyenMai.getItems().addAll(FXCollections.observableArrayList(dsKhuyenMai));

        //gọi tính tổng tiền khi chạy giao diện
        loadTongTien();

        //setup table
        setupTable();
        tbChonThuoc.setItems(obsThuoc);
        loadTable();

        Button btnApply = (Button)this.dialogPane.lookupButton(applyButton);
        Button btnCancel = (Button)this.dialogPane.lookupButton(cancelButton);

        //sự kiện thêm phiếu đặt
        btnApply.addEventFilter(ActionEvent.ACTION, event -> {
            if (checkDuLieu()) {
                themPhieuDat();
            } else {
                event.consume();
            }
        });
        btnThem.setOnAction(  e ->   {
            addThuocVaoTable();
            loadTongTien();
        });
//
        cbKhuyenMai.setOnAction(e -> loadTongTien());

        //setup spinner số lượng
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1);
        spSoLuong.setValueFactory(valueFactory);

        //sự kiện khi thay đổi comboBox thuốc
        cbTenThuoc.setOnAction(e -> {
            cbDonVi.getSelectionModel().select(cbTenThuoc.getSelectionModel().getSelectedItem().getMaDVTCoSo());
        });
    }

    private void loadTable() {
        obsThuoc.setAll(list);
    }

    private void addThuocVaoTable() {
        ChiTietPhieuDatThuoc ctPDT = new ChiTietPhieuDatThuoc(cbTenThuoc.getSelectionModel().getSelectedItem(),
                spSoLuong.getValue(),
                cbDonVi.getSelectionModel().getSelectedItem());
        txtDonGia.setText(dinhDangTien(ctPDT.getSoDangKy().getGiaBan()));
        list.add(ctPDT);
        loadTable();
    }

    private void themPhieuDat() {
        String hashPD = getHashPD();
        NhanVien tenNguoiDat = PhienNguoiDung.getMaNV();
        if (tenNguoiDat == null) {
            showMess("Lỗi người dùng", "Không tìm thấy thông tin người đặt");
            txtTenKhach.requestFocus();
            return;
        }
        KhachHang khachMoi = null;
        if (checkKhachHang()) {
            khachMoi = new KhachHang();
            khachMoi.setTenKH(txtTenKhach.getText().trim());
            khachHangDAO.insertKhachHang(khachMoi);
        }else{
            for (KhachHang khachHang : dsKhach) {
                if (khachHang.getTenKH().equalsIgnoreCase(txtTenKhach.getText().trim())) {
                    khachMoi = khachHang;
                }
            }
        }
        double tongTien = tinhTongTien();
        PhieuDatThuoc i = new PhieuDatThuoc(hashPD, LocalDate.now(),
                false,
                tenNguoiDat,
                khachMoi,
                cbKhuyenMai.getSelectionModel().getSelectedItem().getTenKM().equals("Không áp dụng") ? null : cbKhuyenMai.getSelectionModel().getSelectedItem(),
                tongTien);
        PhieuDat_DAO.themPhieuDatThuocVaoDBS(i);

        //tạo chi tiết phiếu đặt

        for (ChiTietPhieuDatThuoc e : tbChonThuoc.getItems()){
            ChiTietPhieuDat_DAO.themChiTietPhieuDatVaoDBS(new ChiTietPhieuDatThuoc(
                    i,
                    e.getSoDangKy(),
                    e.getSoLuong(),
                    e.getDonViTinh()
            ));
        }
    }

    /**
     * Kiểm tra khách hàng đã tồn tại chưa
     * @return true - chưa tồn tại, false - đã tồn tại
     */
    private boolean checkKhachHang(){

        for (KhachHang khachHang : dsKhach) {
            if (khachHang.getTenKH().equalsIgnoreCase(txtTenKhach.getText().trim())) {
                return false;
            }
        }
        return true;
    }

    private String getHashPD() {
        String hash = PhieuDat_DAO.getMaxHash();
        if (hash == null){
            return "";
        }else{
            int soThuTu = Integer.parseInt(hash) + 1;
            return String.format("PDT%03d", soThuTu);
        }
    }

    public double tinhTongTien(){
        double tongTien = 0.0;
        for (Node node : vbThuoc.getChildren()) {
            if (node instanceof HBox hBox) {

                // Lấy từng VBox con trong HBox
                VBox vboxThuoc = (VBox) hBox.getChildren().get(0);
                VBox vboxDonVi = (VBox) hBox.getChildren().get(1);
                VBox vboxSoLuong = (VBox) hBox.getChildren().get(2);
                VBox vboxGia= (VBox) hBox.getChildren().get(3);

                // Lấy control trong từng VBox
                ComboBox<Thuoc> cbDanhSachThuoc = (ComboBox<Thuoc>) vboxThuoc.getChildren().get(1);
                ComboBox<DonViTinh> cbDonViTinh = (ComboBox<DonViTinh>) vboxDonVi.getChildren().get(1);
                Spinner<Integer> spSoLuong = (Spinner<Integer>) vboxSoLuong.getChildren().get(1);
                TextField spGia = (TextField) vboxGia.getChildren().get(1);

                // Tính tổng tiền
                if (cbDanhSachThuoc.getSelectionModel().getSelectedItem() != null && cbDonViTinh.getSelectionModel().getSelectedItem() != null
                        && spGia.getText() != null && spSoLuong.getValue() != null){
                    double giaBan = Double.parseDouble(spGia.getText().trim().replace("đ","").replace(",",""));
                    tongTien += giaBan * spSoLuong.getValue();
                }
            }
        }
        return tongTien;
    }

    public void showMess(String tieuDe, String vanBan){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tieuDe);
        alert.setContentText(vanBan);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void loadTongTien(){
        double tongTien = tinhTongTien();
        txtTotal.setText("Tổng tiền: " + decimalFormat.format(tongTien));
    }
    private boolean checkDuLieu() {
        return true;
    }

    public String dinhDangTien(double tien){
        DecimalFormat df = new DecimalFormat("#,###đ");
        return df.format(tien);
    }


    private void setupTable(){
        colTenThuoc.setCellValueFactory( cellData -> new SimpleStringProperty(cellData.getValue().getSoDangKy().getTenThuoc()));
        colDonVi.setCellValueFactory( cellData -> new SimpleStringProperty(cellData.getValue().getSoDangKy().getMaDVTCoSo().getTenDVT()));
        colSoLuong.setCellValueFactory( cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getSoLuong())));
        colDonGia.setCellValueFactory( cellData -> new SimpleStringProperty(dinhDangTien(cellData.getValue().getSoDangKy().getGiaBan())));
        colThanhTien.setCellValueFactory( cellData -> {
            double thanhTien = cellData.getValue().getSoLuong() * cellData.getValue().getSoDangKy().getGiaBan();
            return new SimpleStringProperty(dinhDangTien(thanhTien));
        } );
    }
}
