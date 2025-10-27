//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.dialog;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.DonViTinh_DAO;
import com.antam.app.dao.Thuoc_DAO;
import com.antam.app.entity.DonViTinh;
import com.antam.app.entity.Thuoc;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
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

public class ThemPhieuNhapController {
    @FXML
    private DialogPane dialogPane;

    @FXML
    private TextField tfMaPhieuNhap, tfNhaCungCap, tfDiaChi, tfLyDo;

    @FXML
    private Button btnThemThuoc;

    @FXML
    private VBox vbDanhSachThuocNhap;

    private Thuoc_DAO thuoc_DAO = new Thuoc_DAO();
    private DonViTinh_DAO donViTinh_DAO = new DonViTinh_DAO();
    private ArrayList<Thuoc> dsThuoc;
    private ArrayList<DonViTinh> dsDonViTinh;

    public ThemPhieuNhapController() {

    }

    public void initialize() {
        //Kết nối
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        dsThuoc = thuoc_DAO.getAllThuoc();
        dsDonViTinh = donViTinh_DAO.getTatCaDonViTinh();

        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Lưu", ButtonData.APPLY);
        this.dialogPane.getButtonTypes().add(cancelButton);
        this.dialogPane.getButtonTypes().add(applyButton);

        Button btnAppy = (Button) this.dialogPane.lookupButton(applyButton);
        btnAppy.addEventFilter(ActionEvent.ACTION, event -> {
            if (!checkTruongDuLieu()){
                event.consume();
            }else{
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
                        Spinner<Integer> spGiaNhap = (Spinner<Integer>) vboxGiaNhap.getChildren().get(1);

                        // Kiểm tra dữ liệu
                        if (cbDanhSachThuocNhap.getSelectionModel().getSelectedItem() != null && cbDonViTinh.getSelectionModel().getSelectedItem() != null && dpNgaySanXuat.getValue() != null && dpHanSuDung.getValue() != null && spGiaNhap.getValue() != null && spSoLuong.getValue() != null){
                            if(LocalDate.now().isBefore(dpNgaySanXuat.getValue())){
                                showCanhBao("Ngày sản xuất không hợp lệ", "Ngày sản xuất không được sau ngày hiện tại");
                                event.consume();
                                return;
                            }else if(dpNgaySanXuat.getValue().isAfter(dpHanSuDung.getValue()) || dpNgaySanXuat.getValue().equals(dpHanSuDung.getValue())){
                                showCanhBao("Ngày sản xuất không hợp lệ", "Ngày sản xuất không được sau hạn sử dụng");
                                event.consume();
                                return;
                            }

                            if (spSoLuong.getValue().equals(0)){
                                showCanhBao("Số lượng không hợp lệ", "Số lượng phải lớn hơn 0");
                                event.consume();
                                return;
                            }


                        }else{
                            showCanhBao("Thông tin thuốc không đầy đủ", "Hãy điền đầy đủ vào tất cả các thông tin của thuốc");
                            event.consume();
                            return;
                        }
                    }
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
        System.out.println(dsDonViTinh.size());
        for (DonViTinh dvt : dsDonViTinh){
            cbDonViTinh.getItems().add(dvt);
        }
        VBox vbDonViTinh = new VBox();
        vbDonViTinh.getChildren().addAll(new Text("Đơn vị tính:"), cbDonViTinh);

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
                }
        );

        hBox.getChildren().addAll(vbChonThuoc, vbDonViTinh, vbNgaySanXuat, vbHanSuDung, vbSoLuong, vbGiaNhap, btnXoa);
        vbDanhSachThuocNhap.getChildren().add(hBox);
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
}
