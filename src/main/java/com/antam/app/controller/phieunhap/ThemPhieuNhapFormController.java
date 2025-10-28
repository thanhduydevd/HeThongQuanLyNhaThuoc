//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.phieunhap;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.*;
import com.antam.app.entity.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;


public class ThemPhieuNhapFormController {
    @FXML
    private DialogPane dialogPane;

    @FXML
    private TextField tfMaPhieuNhap, tfNhaCungCap, tfDiaChi, tfLyDo;

    @FXML
    private Button btnThemThuoc;

    @FXML
    private VBox vbDanhSachThuocNhap;

    @FXML
    private Text txtTongTien;

    private Thuoc_DAO thuoc_DAO = new Thuoc_DAO();
    private DonViTinh_DAO donViTinh_DAO = new DonViTinh_DAO();
    private PhieuNhap_DAO phieuNhap_DAO = new PhieuNhap_DAO();
    private ChiTietPhieuNhap_DAO chiTietPhieuNhap_DAO = new ChiTietPhieuNhap_DAO();
    private ChiTietThuoc_DAO chiTietThuoc_DAO = new ChiTietThuoc_DAO();

    private ArrayList<Thuoc> dsThuoc;
    private ArrayList<DonViTinh> dsDonViTinh;

    public ThemPhieuNhapFormController() {

    }

    public void initialize() {
        //Kết nối
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        dsThuoc = thuoc_DAO.getAllThuoc();
        dsDonViTinh = donViTinh_DAO.getAllDonViTinh();

        //Tạo mã phiếu nhập tự động
        tfMaPhieuNhap.setText(phieuNhap_DAO.taoMaPhieuNhapTuDong());
        tfMaPhieuNhap.setEditable(false);

        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Lưu", ButtonData.APPLY);
        this.dialogPane.getButtonTypes().add(cancelButton);
        this.dialogPane.getButtonTypes().add(applyButton);

        Button btnAppy = (Button) this.dialogPane.lookupButton(applyButton);
        btnAppy.addEventFilter(ActionEvent.ACTION, event -> {
            if (!checkTruongDuLieu() || !checkChiTietPhieuNhap()){
                event.consume();
            }else{
                boolean kiemTraThanhCong = true;
                PhieuNhap pn = new PhieuNhap(tfMaPhieuNhap.getText(), tfNhaCungCap.getText(), LocalDate.now(), tfDiaChi.getText(), tfLyDo.getText(), PhienNguoiDung.getMaNV(), tinhTongTien(), false);
                if (phieuNhap_DAO.tonTaiMaPhieuNhap(tfMaPhieuNhap.getText())) {
                    showCanhBao("Trùng mã phiếu nhập", "Mã phiếu nhập này đã tồn tại. Vui lòng nhập mã khác!");
                    event.consume();
                    kiemTraThanhCong = false;
                }else{
                    phieuNhap_DAO.themPhieuNhap(pn);
                    kiemTraThanhCong = true;
                }
                for (Node node : vbDanhSachThuocNhap.getChildren()) {
                    if (node instanceof HBox hBox) {

                        // Lấy từng VBox con trong HBox
                        VBox vboxThuoc = (VBox) hBox.getChildren().get(0);
                        VBox vboxDonVi = (VBox) hBox.getChildren().get(1);
                        VBox vboxNgaySX = (VBox) hBox.getChildren().get(2);
                        VBox vboxHanSD = (VBox) hBox.getChildren().get(3);
                        VBox vboxSoLuong = (VBox) hBox.getChildren().get(4);
                        VBox vboxGiaNhap = (VBox) hBox.getChildren().get(5);

                        // Lấy control trong từng VBox
                        ComboBox<Thuoc> cbDanhSachThuocNhap = (ComboBox<Thuoc>) vboxThuoc.getChildren().get(1);
                        ComboBox<DonViTinh> cbDonViTinh = (ComboBox<DonViTinh>) vboxDonVi.getChildren().get(1);
                        DatePicker dpNgaySanXuat = (DatePicker) vboxNgaySX.getChildren().get(1);
                        DatePicker dpHanSuDung = (DatePicker) vboxHanSD.getChildren().get(1);
                        Spinner<Integer> spSoLuong = (Spinner<Integer>) vboxSoLuong.getChildren().get(1);
                        Spinner<Double> spGiaNhap = (Spinner<Double>) vboxGiaNhap.getChildren().get(1);

                        // Nếu tất cả các trường đều hợp lệ
                        ChiTietPhieuNhap ctpt = new ChiTietPhieuNhap(new PhieuNhap(tfMaPhieuNhap.getText()), cbDanhSachThuocNhap.getValue(), cbDonViTinh.getValue(), spSoLuong.getValue(), spGiaNhap.getValue());
                        ChiTietThuoc ctt = new ChiTietThuoc(-1, pn, cbDanhSachThuocNhap.getValue(), spSoLuong.getValue(), dpHanSuDung.getValue(), dpNgaySanXuat.getValue());
                        if(kiemTraThanhCong){
                            chiTietPhieuNhap_DAO.themChiTietPhieuNhap(ctpt);
                            chiTietThuoc_DAO.themChiTietThuoc(ctt);
                        }
                    }
                }
                if(kiemTraThanhCong){
                    showCanhBao("Thành công", "Lưu phiếu nhập thành công!");
                }
            }
        });

        btnThemThuoc.setOnAction(e -> loadVBoxDanhachThuocNhap(vbDanhSachThuocNhap));
    }

    public void loadVBoxDanhachThuocNhap(VBox vbDanhSachThuocNhap){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setSpacing(20);
        hBox.setStyle(
                "-fx-background-color: #f8fafc;" +
                        "-fx-padding: 10;"
        );

        //Danh sách thuốc
        ComboBox<Thuoc> cbDanhSachThuocNhap = new ComboBox<>();
        cbDanhSachThuocNhap.getStylesheets().add(
                getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm()
        );
        cbDanhSachThuocNhap.setPrefWidth(150);
        cbDanhSachThuocNhap.setPromptText("Chọn thuốc");
        for (Thuoc thuoc : dsThuoc){
            cbDanhSachThuocNhap.getItems().add(thuoc);
        }
        VBox vbChonThuoc = new VBox();
        vbChonThuoc.getChildren().addAll(new Text("Thuốc nhập:"), cbDanhSachThuocNhap);

        //Đơn vị tính
        ComboBox<DonViTinh> cbDonViTinh = new ComboBox<>();
        cbDonViTinh.getStylesheets().add(
                getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm()
        );
        cbDonViTinh.setPrefWidth(150);
        cbDonViTinh.setPromptText("Chọn đơn vị tính");

        VBox vbDonViTinh = new VBox();
        vbDonViTinh.getChildren().addAll(new Text("Đơn vị tính:"), cbDonViTinh);

        //Sự kiện chọn thuốc để load đơn vị tính tương ứng
        cbDanhSachThuocNhap.setOnAction(e -> {
            cbDonViTinh.getItems().clear();
            cbDonViTinh.getItems().add(donViTinh_DAO.getDVTTheoMaDVT(cbDanhSachThuocNhap.getValue().getMaDVTCoSo().getMaDVT()));
            cbDonViTinh.getSelectionModel().selectFirst();
        });

        //Ngày sản xuất
        DatePicker dpNgaySanXuat = new DatePicker();
        dpNgaySanXuat.getStylesheets().add(
                getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm()
        );
        dpNgaySanXuat.setPrefWidth(150);
        dpNgaySanXuat.setPromptText("Chọn ngày sản xuất");
        VBox vbNgaySanXuat = new VBox();
        vbNgaySanXuat.getChildren().addAll(new Text("Ngày sản xuất:"), dpNgaySanXuat);

        //Hạn sử dụng
        DatePicker dpHanSuDung = new DatePicker();
        dpHanSuDung.getStylesheets().add(
                getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm()
        );
        dpHanSuDung.setPrefWidth(150);
        dpHanSuDung.setPromptText("Chọn hạn sử dụng");
        VBox vbHanSuDung = new VBox();
        vbHanSuDung.getChildren().addAll(new Text("Hạn sử dụng:"), dpHanSuDung);

        //Số lượng
        Spinner<Integer> spSoLuong = new Spinner<>();
        spSoLuong.getStylesheets().add(
                getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm()
        );
        /* Số lượng ít nhất là 1 và nhiều nhất là 50.000 */
        spSoLuong.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50000, 1, 1));
        spSoLuong.setPrefWidth(100);
        spSoLuong.setEditable(Boolean.TRUE);
        spSoLuong.setPromptText("Số lượng");
        spSoLuong.valueProperty().addListener(e ->{
            txtTongTien.setText("Tổng tiền: " + NumberFormat.getInstance(new Locale("vi", "VN")).format(tinhTongTien()) + " VNĐ");
        });
        VBox vbSoLuong = new VBox();
        vbSoLuong.getChildren().addAll(new Text("Số lượng:"), spSoLuong);

        //Giá nhập
        Spinner<Double> spGiaNhap = new Spinner<>();
        spGiaNhap.getStylesheets().add(
                getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm()
        );
        /* Định dạng tiền Việt Nam (Ít nhất 1.000đ nhiều nhất 999.999.999đ, hiển thị trước 10.000đ và bước nhảy 5.000đ) */
        SpinnerValueFactory.DoubleSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(1_000, 999_999_999, 10_000, 5_000);
        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        valueFactory.setConverter(new StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                if (value == null) return "";
                return currencyFormat.format(value);
            }

            @Override
            public Double fromString(String text) {
                try {
                    Number parsed = currencyFormat.parse(text);
                    return parsed.doubleValue();
                } catch (Exception e) {
                    return 0.0;
                }
            }
        });
        spGiaNhap.setValueFactory(valueFactory);
        spGiaNhap.setEditable(true);
        spGiaNhap.setPrefWidth(120);
        spGiaNhap.setPromptText("Nhập giá nhập");
        spGiaNhap.valueProperty().addListener(e ->{
            txtTongTien.setText("Tổng tiền: " + NumberFormat.getInstance(new Locale("vi", "VN")).format(tinhTongTien()) + " VNĐ");
        });
        VBox vbGiaNhap = new VBox();
        vbGiaNhap.getChildren().addAll(new Text("Giá nhập (VNĐ):"), spGiaNhap);

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
                    vbDanhSachThuocNhap.getChildren().remove(hBox);
                    txtTongTien.setText("Tổng tiền: " + NumberFormat.getInstance(new Locale("vi", "VN")).format(tinhTongTien()) + " VNĐ");
                }
        );

        hBox.getChildren().addAll(vbChonThuoc, vbDonViTinh, vbNgaySanXuat, vbHanSuDung, vbSoLuong, vbGiaNhap, btnXoa);
        vbDanhSachThuocNhap.getChildren().add(hBox);
        txtTongTien.setText("Tổng tiền: " + NumberFormat.getInstance(new Locale("vi", "VN")).format(tinhTongTien()) + " VNĐ");
    }

    /**
     * Kiểm tra các trường dữ liệu
     * @return
     */
    public boolean checkTruongDuLieu(){
        if(!tfMaPhieuNhap.getText().isEmpty()){
            if (!tfMaPhieuNhap.getText().matches("^PN\\d{6}")) {
                showCanhBao("Lỗi định dạng", "Mã phiếu nhập bắt đầu là PN và theo sau là 6 ký tự số");
                tfMaPhieuNhap.requestFocus();
                return false;
            }
        }else {
            showCanhBao("Không được rỗng", "Mã phiếu nhập không được rỗng");
            return false;
        }

        if (tfNhaCungCap.getText().isEmpty()){
            showCanhBao("Không được rỗng", "Nhà cung cấp không được rỗng");
            tfNhaCungCap.requestFocus();
            return false;
        }

        if (tfDiaChi.getText().isEmpty()){
            showCanhBao("Không được rỗng", "Địa chỉ không được rỗng");
            tfDiaChi.requestFocus();
            return false;
        }

        if (tfLyDo.getText().isEmpty()){
            showCanhBao("Không được rỗng", "Lý do nhập không được rỗng");
            tfLyDo.requestFocus();
            return false;
        }

        if (vbDanhSachThuocNhap.getChildren().isEmpty()){
            showCanhBao("Chưa thêm thuốc nhập", "Cần thêm thuốc nhập vào phiếu");
            return false;
        }
        return true;
    }

    /**
     * Hiển thị thông báo cảnh báo
     * @param tieuDe
     * @param vanBan
     */
    public void showCanhBao(String tieuDe, String vanBan){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tieuDe);
        alert.setContentText(vanBan);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public boolean checkChiTietPhieuNhap() {
        for (Node node : vbDanhSachThuocNhap.getChildren()) {
            if (node instanceof HBox hBox) {

                // Lấy từng VBox con trong HBox
                VBox vboxThuoc = (VBox) hBox.getChildren().get(0);
                VBox vboxDonVi = (VBox) hBox.getChildren().get(1);
                VBox vboxNgaySX = (VBox) hBox.getChildren().get(2);
                VBox vboxHanSD = (VBox) hBox.getChildren().get(3);
                VBox vboxSoLuong = (VBox) hBox.getChildren().get(4);
                VBox vboxGiaNhap = (VBox) hBox.getChildren().get(5);

                // Lấy control trong từng VBox
                ComboBox<Thuoc> cbDanhSachThuocNhap = (ComboBox<Thuoc>) vboxThuoc.getChildren().get(1);
                ComboBox<DonViTinh> cbDonViTinh = (ComboBox<DonViTinh>) vboxDonVi.getChildren().get(1);
                DatePicker dpNgaySanXuat = (DatePicker) vboxNgaySX.getChildren().get(1);
                DatePicker dpHanSuDung = (DatePicker) vboxHanSD.getChildren().get(1);
                Spinner<Integer> spSoLuong = (Spinner<Integer>) vboxSoLuong.getChildren().get(1);
                Spinner<Double> spGiaNhap = (Spinner<Double>) vboxGiaNhap.getChildren().get(1);

                // Kiểm tra dữ liệu
                if (cbDanhSachThuocNhap.getSelectionModel().getSelectedItem() != null && cbDonViTinh.getSelectionModel().getSelectedItem() != null && dpNgaySanXuat.getValue() != null && dpHanSuDung.getValue() != null && spGiaNhap.getValue() != null && spSoLuong.getValue() != null) {
                    if (LocalDate.now().isBefore(dpNgaySanXuat.getValue())) {
                        showCanhBao("Ngày sản xuất không hợp lệ", "Ngày sản xuất không được sau ngày hiện tại");
                        return false;
                    } else if (dpNgaySanXuat.getValue().isAfter(dpHanSuDung.getValue()) || dpNgaySanXuat.getValue().equals(dpHanSuDung.getValue())) {
                        showCanhBao("Ngày sản xuất không hợp lệ", "Ngày sản xuất không được sau hạn sử dụng");
                        return false;
                    }

                    if (spSoLuong.getValue().equals(0)) {
                        showCanhBao("Số lượng không hợp lệ", "Số lượng phải lớn hơn 0");
                        return false;
                    }
                } else {
                    showCanhBao("Thông tin thuốc không đầy đủ", "Hãy điền đầy đủ vào tất cả các thông tin của thuốc");
                    return false;
                }
            }
        }
        return true;
    }

    public double tinhTongTien(){
        double tongTien = 0.0;
        for (Node node : vbDanhSachThuocNhap.getChildren()) {
            if (node instanceof HBox hBox) {
                VBox vboxGiaNhap = (VBox) hBox.getChildren().get(5);
                VBox vboxSoLuong = (VBox) hBox.getChildren().get(4);

                Spinner<Integer> spSoLuong = (Spinner<Integer>) vboxSoLuong.getChildren().get(1);
                Spinner<Double> spGiaNhap = (Spinner<Double>) vboxGiaNhap.getChildren().get(1);
                double thanhTien = spSoLuong.getValue() * spGiaNhap.getValue();
                tongTien += thanhTien;
            }
        }
        return tongTien;
    }
}