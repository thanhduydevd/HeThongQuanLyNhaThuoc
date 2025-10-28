//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.phieudat;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.*;
import com.antam.app.entity.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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
    private TextField txtMa,txtTenKhach;
    @FXML
    private VBox vbThuoc;
    @FXML
    private TextArea taGhiChu;
    @FXML
    private Button btnThem;
    @FXML
    private Text txtTotal;
    @FXML
    private ComboBox<KhuyenMai> cbKhuyenMai;

    private Thuoc_DAO thuoc_dao = new Thuoc_DAO();
    private  DonViTinh_DAO donViTinh_dao = new DonViTinh_DAO();
    private ArrayList<Thuoc> dsThuoc;
    private ArrayList<DonViTinh> dsDonViTinh;
    private DecimalFormat decimalFormat = new DecimalFormat("#,### đ");
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

        //load ds đơn vị tính.
        dsDonViTinh = donViTinh_dao.getTatCaDonViTinh();
        dsThuoc = thuoc_dao.getAllThuoc();

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

        Button btnApply = (Button)this.dialogPane.lookupButton(applyButton);
        Button btnCancel = (Button)this.dialogPane.lookupButton(cancelButton);

        btnApply.addEventFilter(ActionEvent.ACTION, event -> {
            if (!checkDuLieu()){
                event.consume();
            }else{
                for (Node node : vbThuoc.getChildren()) {
                    if (node instanceof HBox hBox) {

                        // Lấy từng VBox con trong HBox
                        VBox vboxThuoc = (VBox) hBox.getChildren().get(0);
                        VBox vboxDonVi = (VBox) hBox.getChildren().get(1);
                        VBox vboxSoLuong = (VBox) hBox.getChildren().get(2);
                        VBox vboxGiaNhap = (VBox) hBox.getChildren().get(3);

                        // Lấy control trong từng VBox
                        ComboBox<Thuoc> cbDanhSachThuoc = (ComboBox<Thuoc>) vboxThuoc.getChildren().get(1);
                        ComboBox<DonViTinh> cbDonViTinh = (ComboBox<DonViTinh>) vboxDonVi.getChildren().get(1);
                        Spinner<Integer> spSoLuong = (Spinner<Integer>) vboxSoLuong.getChildren().get(1);
                        Spinner<Integer> spGiaNhap = (Spinner<Integer>) vboxGiaNhap.getChildren().get(1);

                        // Kiểm tra dữ liệu
                        if (cbDanhSachThuoc.getSelectionModel().getSelectedItem() != null && cbDonViTinh.getSelectionModel().getSelectedItem() != null
                                && spGiaNhap.getValue() != null && spSoLuong.getValue() != null){
                            if (spSoLuong.getValue().equals(0)){
                                showMess("Số lượng không hợp lệ", "Số lượng phải lớn hơn 0");
                                event.consume();
                                return;
                            }
                        }else{
                            showMess("Thông tin thuốc không đầy đủ", "Hãy điền đầy đủ vào tất cả các thông tin của thuốc");
                            event.consume();
                            return;
                        }

                        themPhieuDat();
                    }
                }
            }
        });
        btnThem.setOnAction(  e ->   loadHBoxThuoc());
    }

    private void themPhieuDat() {
        String hashPD = getHashPD();
        NhanVien tenNguoiDat = PhienNguoiDung.getMaNV();
        KhuyenMai km = null;
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
                km,
                tongTien);
        PhieuDat_DAO.themPhieuDatThuocVaoDBS(i);

        //tạo chi tiết phiếu đặt
        for (Node node : vbThuoc.getChildren()) {
            if (node instanceof HBox hBox) {

                // Lấy từng VBox con trong HBox
                VBox vboxThuoc = (VBox) hBox.getChildren().get(0);
                VBox vboxDonVi = (VBox) hBox.getChildren().get(1);
                VBox vboxSoLuong = (VBox) hBox.getChildren().get(2);
                VBox vboxGiaBan = (VBox) hBox.getChildren().get(3);

                // Lấy control trong từng VBox
                ComboBox<Thuoc> cbDanhSachThuoc = (ComboBox<Thuoc>) vboxThuoc.getChildren().get(1);
                ComboBox<DonViTinh> cbDonViTinh = (ComboBox<DonViTinh>) vboxDonVi.getChildren().get(1);
                Spinner<Integer> spSoLuong = (Spinner<Integer>) vboxSoLuong.getChildren().get(1);
                TextField txtGiaBan = (TextField) vboxGiaBan.getChildren().get(1);

                Thuoc thuoc = cbDanhSachThuoc.getSelectionModel().getSelectedItem();
                DonViTinh donViTinh = cbDonViTinh.getSelectionModel().getSelectedItem();
                int soLuong = spSoLuong.getValue();
                double giaBan = Double.parseDouble(txtGiaBan.getText());

                ChiTietPhieuDatThuoc ctPDT = new ChiTietPhieuDatThuoc(i, thuoc, soLuong, donViTinh,giaBan);
                PhieuDat_DAO.themChiTietPhieuDatVaoDBS(ctPDT);
            }
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

    public void loadHBoxThuoc(){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setSpacing(20);
        hBox.setStyle(
                "-fx-background-color: #f8fafc;" +
                        "-fx-padding: 10;"
        );
        //Danh sách thuốc
        ComboBox<Thuoc> cbDanhSachThuoc = new ComboBox<>();
        cbDanhSachThuoc.getStylesheets().add(
                getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm()
        );
        cbDanhSachThuoc.setPrefWidth(150);
        cbDanhSachThuoc.setPromptText("Chọn thuốc");
        for (Thuoc thuoc : dsThuoc){
            cbDanhSachThuoc.getItems().add(thuoc);
        }

        VBox vbChonThuoc = new VBox();
        vbChonThuoc.getChildren().addAll(new Text("Thuốc nhập:"), cbDanhSachThuoc);

        //Đơn vị tính
        ComboBox<DonViTinh> cbDonViTinh = new ComboBox<>();
        cbDonViTinh.getStylesheets().add(
                getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm()
        );
        cbDonViTinh.setPrefWidth(150);
        cbDonViTinh.setPromptText("Chọn đơn vị tính");

//        System.out.println(dsDonViTinh.size());
        cbDonViTinh.getItems().addAll(dsDonViTinh);


        VBox vbDonViTinh = new VBox();
        vbDonViTinh.getChildren().addAll(new Text("Đơn vị tính:"), cbDonViTinh);

            //Số lượng
        Spinner<Integer> spSoLuong = new Spinner<>();
        spSoLuong.getStylesheets().add(
                getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm()
        );
        spSoLuong.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50000, 1, 1));
        spSoLuong.setPrefWidth(100);
        spSoLuong.setPromptText("Số lượng");
        spSoLuong.valueProperty().addListener(e ->{
            txtTotal.setText("Tổng tiền: " + decimalFormat.format(tinhTongTien()));
        });
        VBox vbSoLuong = new VBox();
        vbSoLuong.getChildren().addAll(new Text("Số lượng:"), spSoLuong);

        //Giá bán
        TextField txtGia = new TextField();
        txtGia.getStylesheets().add(
                getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm()
        );
        txtGia.setPrefWidth(150);
        txtGia.setEditable(false);
        txtGia.setPromptText("Giá Bán");
        VBox vbGia = new VBox();
        vbGia.getChildren().addAll(new Text("Giá bán:"), txtGia);

        //thêm sự kiện cho lúc chọn thuốc

        //lấy đơn vị tính theo thuốc được chọn
        cbDanhSachThuoc.setOnAction(event -> {
            Thuoc selectedThuoc = cbDanhSachThuoc.getSelectionModel().getSelectedItem();
            if (selectedThuoc != null) {
                // Gán giá
                txtGia.setText(String.valueOf(selectedThuoc.getGiaBan()));
                txtGia.setEditable(false);

                // Gán đơn vị tính
                int maDVT = selectedThuoc.getMaDVTCoSo().getMaDVT();
                DonViTinh dvt = dvtDAO.getDVTTheoMaDVT(maDVT);

                if (dvt != null) {
                    cbDonViTinh.getSelectionModel().select(
                            dsDonViTinh.stream()
                                    .filter(x -> x.getMaDVT() == dvt.getMaDVT())
                                    .findFirst()
                                    .orElse(null)
                    );
                    cbDonViTinh.setDisable(true);
                } else {
                    cbDonViTinh.getSelectionModel().clearSelection();
                    cbDonViTinh.setDisable(false);
                }
            } else {
                txtGia.clear();
                cbDonViTinh.getSelectionModel().clearSelection();
                cbDonViTinh.setDisable(false);
            }
        });

        //Nút xoá
        Button btnXoa = new Button("X");
        btnXoa.setStyle(
                "-fx-padding: 0 5 0 5;" +
                        "-fx-background-color: #ef4444;" +
                        "-fx-background-radius: 50%;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: BOLD;"
        );

        btnXoa.setOnAction(e ->
                {
                    vbThuoc.getChildren().remove(hBox);
                }
        );

        //gọi cbThuoc đầu tiên để chạy sk lần đầu
        cbDanhSachThuoc.getSelectionModel().selectFirst();
        // Kích hoạt sự kiện bằng tay
        cbDanhSachThuoc.getOnAction().handle(new ActionEvent());

        hBox.getChildren().addAll(vbChonThuoc, vbDonViTinh, vbSoLuong, vbGia, btnXoa);
        vbThuoc.getChildren().add(hBox);
    }

    public double tinhTongTien(){
        double tongTien = 0;
        for (Node node : vbThuoc.getChildren()) {
            if (node instanceof HBox hBox) {

                // Lấy từng VBox con trong HBox
                VBox vboxSoLuong = (VBox) hBox.getChildren().get(2);
                VBox vboxGiaNhap = (VBox) hBox.getChildren().get(3);

                // Lấy control trong từng VBox
                Spinner<Integer> spSoLuong = (Spinner<Integer>) vboxSoLuong.getChildren().get(1);
                TextField txtGia = (TextField) vboxGiaNhap.getChildren().get(1);

                // Cộng dồn tổng tiền
                int soLuong = spSoLuong.getValue();
                double giaBan = 0;
                try {
                    giaBan = Double.parseDouble(txtGia.getText());
                } catch (NumberFormatException ex) {
                    giaBan = 0;
                }
                KhuyenMai km = null;
                if (cbKhuyenMai.getSelectionModel().getSelectedItem() != null &&
                        !cbKhuyenMai.getSelectionModel().getSelectedItem().getMaKM().isEmpty()) {
                    km = cbKhuyenMai.getSelectionModel().getSelectedItem();
                    if (km.getLoaiKhuyenMai().getMaLKM() == 1){
                        giaBan = giaBan - (giaBan * km.getSo() / 100);
                    }else if (km.getLoaiKhuyenMai().getMaLKM() == 2){
                        giaBan = giaBan - km.getSo();
                }
                tongTien += soLuong * giaBan;
            }}
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


    private boolean checkDuLieu() {
        return true;
    }

    public String dinhDangTien(double tien){
        DecimalFormat df = new DecimalFormat("#,###đ");
        return df.format(tien);
    }
}
