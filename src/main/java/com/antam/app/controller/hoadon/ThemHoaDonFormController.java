//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.hoadon;

import com.antam.app.dao.*;
import com.antam.app.entity.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.stream.Collectors;

public class ThemHoaDonFormController {
    @FXML
    private DialogPane dialogPane;
    @FXML
    private TextField txtMaHoaDon;
    @FXML
    private TextField txtTenKhachHang;
    @FXML
    private ComboBox<Thuoc> cbMedicine;
    @FXML
    private ComboBox<DonViTinh> cb_unit;
    @FXML
    private TextField txt_price;
    @FXML
    private TextField txtQuantity;
    @FXML
    private Text txtTamTinh;
    @FXML
    private Text txtThue;
    @FXML
    private Text txtTongTien;
    @FXML
    private VBox medicineRowsVBox;
    @FXML
    private Button btnThemThuoc;
    @FXML
    private ComboBox<KhuyenMai> cb_promotion;
    @FXML
    private Text txtThongTinKhuyenMai;
    @FXML
    private Text txtWarning;

    // Định dạng tiền tệ kiểu Việt Nam: 1.000đ, 10.000đ
    private static final DecimalFormat VND_FORMAT;
    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        VND_FORMAT = new DecimalFormat("#,##0", symbols);
    }

    public ThemHoaDonFormController() {
    }

    public void initialize() {
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Tạo hoá đơn", ButtonData.APPLY);
        this.dialogPane.getButtonTypes().add(cancelButton);
        this.dialogPane.getButtonTypes().add(applyButton);

        // Tự động sinh mã hoá đơn mới
        txtMaHoaDon.setText(generateNewMaHoaDon());
        txtMaHoaDon.setEditable(false); // Khóa không cho sửa mã hoá đơn

        // Load thuốc vào cbMedicine
        Thuoc_DAO thuocDAO = new Thuoc_DAO();
        var thuocList = FXCollections.observableArrayList(thuocDAO.getAllThuoc());
        cbMedicine.setItems(thuocList);
        cbMedicine.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Thuoc thuoc) {
                return thuoc == null ? "" : thuoc.getTenThuoc();
            }
            @Override
            public Thuoc fromString(String s) {
                return thuocList.stream().filter(t -> t.getTenThuoc().equals(s)).findFirst().orElse(null);
            }
        });

//        cbMedicine.valueProperty().addListener((obs, oldVal, newVal) -> {
//            if (newVal != null) {
//                // Lấy danh sách đơn vị tính quy đổi cho thuốc
//                QuyDoi_DAO quyDoiDAO = new QuyDoi_DAO();
//                DonViTinh_DAO donViTinhDAO = new DonViTinh_DAO();
//                var allQuyDoi = quyDoiDAO.getAllQuyDoi();
//                var quyDoiList = allQuyDoi.getOrDefault(newVal.getMaThuoc(), List.of());
//                HashSet<Integer> dvtIdSet = new HashSet<>();
//                // Thêm đơn vị cơ sở
//                dvtIdSet.add(newVal.getMaDVTCoSo().getMaDVT());
//                // Thêm các đơn vị quy đổi
//                for (QuyDoi qd : quyDoiList) {
//                    dvtIdSet.add(qd.getMaDVTCha().getMaDVT());
//                    dvtIdSet.add(qd.getMaDVTCon().getMaDVT());
//                }
//                HashSet<DonViTinh> dvtSet = new HashSet<>();
//                for (Integer maDVT : dvtIdSet) {
//                    DonViTinh dvt = donViTinhDAO.getDVTTheoMa(maDVT);
//                    if (dvt != null) dvtSet.add(dvt);
//                }
//                cb_unit.setItems(FXCollections.observableArrayList(dvtSet));
//                cb_unit.getSelectionModel().selectFirst();
//                // Hiển thị giá bán
//                txt_price.setText(VND_FORMAT.format(newVal.getGiaBan()) + "đ");
//                updateSummary();
//            } else {
//                cb_unit.getItems().clear();
//                txt_price.clear();
//                txtTamTinh.setText("");
//                txtThue.setText("");
//                txtTongTien.setText("");
//            }
//        });
        txtQuantity.textProperty().addListener((obs, oldVal, newVal) -> updateSummary());
        txt_price.textProperty().addListener((obs, oldVal, newVal) -> updateSummary());
        cb_unit.valueProperty().addListener((obs, oldVal, newVal) -> updateSummary());
        btnThemThuoc.setOnAction(e -> addMedicineRow());
        if (!medicineRowsVBox.getChildren().isEmpty() && medicineRowsVBox.getChildren().get(0) instanceof HBox hbox) {
            setupMedicineRow(hbox);
        }

        // Load khuyến mãi vào cb_promotion
        KhuyenMai_DAO khuyenMaiDAO = new KhuyenMai_DAO();
        var dsKhuyenMai = khuyenMaiDAO.getAllKhuyenMaiConHieuLuc();
        cb_promotion.setItems(FXCollections.observableArrayList(dsKhuyenMai));
        cb_promotion.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(KhuyenMai km) {
                return km == null ? "" : km.getTenKM();
            }
            @Override
            public KhuyenMai fromString(String s) {
                return dsKhuyenMai.stream().filter(km -> km.getTenKM().equals(s)).findFirst().orElse(null);
            }
        });

        // Hiển thị thông tin khuyến mãi khi chọn
        cb_promotion.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtThongTinKhuyenMai.setText(
                    String.format("%s (Từ %s đến %s)\nLoại: %s, Giá trị: %s%s",
                        newVal.getTenKM(),
                        newVal.getNgayBatDau(),
                        newVal.getNgayKetThuc(),
                        newVal.getLoaiKhuyenMai().getTenLKM(),
                        newVal.getSo(),
                        newVal.getLoaiKhuyenMai().getTenLKM().toLowerCase().contains("%") ? "%" : ""
                    )
                );
            } else {
                txtThongTinKhuyenMai.setText("");
            }
            updateSummary();
        });

        // Xử lý khi nhấn nút tạo hoá đơn
        Button btnTaoHoaDon = (Button) dialogPane.lookupButton(applyButton);
        // Validate dữ liệu trước khi tạo hoá đơn
        btnTaoHoaDon.addEventFilter(javafx.event.ActionEvent.ACTION, e -> {
            // Reset warning
            txtWarning.setVisible(false);
            txtWarning.setText("");
            String tenKH = txtTenKhachHang.getText().trim();
            if (tenKH.isEmpty()) {
                txtWarning.setText("Vui lòng nhập tên khách hàng!");
                txtWarning.setVisible(true);
                e.consume();
                return;
            }
            boolean hasMedicine = false;
            for (var node : medicineRowsVBox.getChildren()) {
                if (node instanceof HBox hbox) {
                    ComboBox<Thuoc> cb = (ComboBox<Thuoc>) hbox.getChildren().get(0);
                    if (cb.getValue() != null) {
                        hasMedicine = true;
                        break;
                    }
                }
            }
            if (!hasMedicine) {
                txtWarning.setText("Vui lòng chọn ít nhất một thuốc!");
                txtWarning.setVisible(true);
                e.consume();
                return;
            }
            boolean hasQuantity = false;
            for (var node : medicineRowsVBox.getChildren()) {
                if (node instanceof HBox hbox) {
                    TextField txtQuantity = (TextField) hbox.getChildren().get(2);
                    if (!txtQuantity.getText().trim().isEmpty()) {
                        hasQuantity = true;
                        break;
                    }
                }
            }
            if (!hasQuantity) {
                txtWarning.setText("Vui lòng nhập số lượng thuốc!");
                txtWarning.setVisible(true);
                e.consume();
                return;
            } else {
                // Kiểm tra số lượng có hợp lệ không
                for (var node : medicineRowsVBox.getChildren()) {
                    if (node instanceof HBox hbox) {
                        TextField txtQuantity = (TextField) hbox.getChildren().get(2);
                        String qtyStr = txtQuantity.getText().trim();
                        if (!qtyStr.isEmpty()) {
                            try {
                                int qty = Integer.parseInt(qtyStr);
                                if (qty <= 0) {
                                    txtWarning.setText("Số lượng thuốc phải là số nguyên dương!");
                                    txtWarning.setVisible(true);
                                    e.consume();
                                    return;
                                }
                            } catch (NumberFormatException ex) {
                                txtWarning.setText("Số lượng thuốc phải là số nguyên hợp lệ!");
                                txtWarning.setVisible(true);
                                e.consume();
                                return;
                            }
                        }
                    }
                }
            }
            // Kiểm tra tồn kho cho từng thuốc
            ChiTietThuoc_DAO chiTietThuocDAO = new ChiTietThuoc_DAO();
            boolean enoughStock = true;
            String outOfStockMedicine = null;
            for (var node : medicineRowsVBox.getChildren()) {
                if (node instanceof HBox hbox) {
                    ComboBox<Thuoc> cb = (ComboBox<Thuoc>) hbox.getChildren().get(0);
                    TextField txtQuantity = (TextField) hbox.getChildren().get(2);
                    Thuoc thuoc = cb.getValue();
                    int soLuong = 0;
                    try { soLuong = Integer.parseInt(txtQuantity.getText().trim()); } catch(Exception ex) {}
                    if (thuoc != null && soLuong > 0) {
                        int tongTonKho = chiTietThuocDAO.getTongTonKhoTheoMaThuoc(thuoc.getMaThuoc());
                        if (tongTonKho < soLuong) {
                            enoughStock = false;
                            outOfStockMedicine = thuoc.getTenThuoc();
                            break;
                        }
                    }
                }
            }
            if (!enoughStock) {
                txtWarning.setText("Số lượng tồn không đủ cho thuốc: " + outOfStockMedicine);
                txtWarning.setVisible(true);
                e.consume();
                return;
            }
            // Tiến hành tạo hoá đơn
            txtWarning.setVisible(false);
            txtWarning.setText("");

            // 1. Lấy mã khách hàng (tự động thêm nếu chưa có)
            String maKH = getOrCreateMaKhachHang();
            KhachHang_DAO khachHangDAO = new KhachHang_DAO();
            KhachHang kh = khachHangDAO.getKhachHangTheoMa(maKH);

            // 2. Lấy nhân viên (nếu có ComboBox chọn nhân viên, ví dụ cbEmployee)
            NhanVien_DAO nhanVienDAO = new NhanVien_DAO();
            NhanVien nhanVien = PhienNguoiDung.getMaNV(); // Hardcode mã nhân viên
            if (nhanVien == null || nhanVien.getMaNV() == null) {
                txtWarning.setText("Không tìm thấy nhân viên hợp lệ! Không thể tạo hóa đơn.");
                txtWarning.setVisible(true);
                e.consume();
                return;
            }

            // 3. Lấy khuyến mãi (đã có đối tượng km)
            KhuyenMai km = cb_promotion.getValue();
            // 4. Lấy mã hoá đơn
            String maHD = txtMaHoaDon.getText().trim();
            // 5. Lấy tổng tiền, tạm tính, thuế, khuyến mãi
            double tongTien = 0, tamTinh = 0, thue = 0, giam = 0;
            try { tamTinh = parseCurrency(txtTamTinh.getText()); } catch(Exception ex) {}
            try { thue = parseCurrency(txtThue.getText()); } catch(Exception ex) {}
            try {
                String tong = txtTongTien.getText().replaceAll("[^0-9]", "");
                tongTien = tong.isEmpty() ? 0 : Double.parseDouble(tong);
            } catch(Exception ex) {}
            // 6. Tạo hoá đơn đúng kiểu constructor
            HoaDon_DAO hoaDonDAO = new HoaDon_DAO();
            HoaDon hoaDon = new HoaDon(maHD, java.time.LocalDate.now(), nhanVien, kh, km, tongTien, false);
            boolean hoaDonInserted = hoaDonDAO.insertHoaDon(hoaDon);
            if (!hoaDonInserted) {
                txtWarning.setText("Tạo hóa đơn thất bại! Vui lòng kiểm tra lại thông tin.");
                txtWarning.setVisible(true);
                e.consume();
                return;
            }
            // 5. Thêm chi tiết hoá đơn cho từng thuốc và trừ kho
            ChiTietHoaDon_DAO chiTietHoaDonDAO = new ChiTietHoaDon_DAO();
            for (var node : medicineRowsVBox.getChildren()) {
                if (node instanceof HBox hbox) {
                    ComboBox<Thuoc> cb = (ComboBox<Thuoc>) hbox.getChildren().get(0);
                    ComboBox<DonViTinh> cbUnit = (ComboBox<DonViTinh>) hbox.getChildren().get(1);
                    TextField txtQuantity = (TextField) hbox.getChildren().get(2);
                    TextField txtPrice = (TextField) hbox.getChildren().get(3);
                    txtPrice.setEditable(false); // Khóa không cho sửa giá
                    Thuoc thuoc = cb.getValue();
                    DonViTinh dvt = cbUnit.getValue();
                    int soLuong = 0;
                    double donGia = 0;
                    try { soLuong = Integer.parseInt(txtQuantity.getText().trim()); } catch(Exception ex) {}
                    try { donGia = parseCurrency(txtPrice.getText().trim()); } catch(Exception ex) {}
                    if (thuoc != null && dvt != null && soLuong > 0) {
                        // Lấy danh sách các lô ChiTietThuoc còn tồn kho cho thuốc này
                        List<ChiTietThuoc> listCTT = chiTietThuocDAO.getAllChiTietThuoc().stream()
                            .filter(ctt -> ctt.getMaThuoc().getMaThuoc().equals(thuoc.getMaThuoc()) && ctt.getSoLuong() > 0)
                            .sorted(java.util.Comparator.comparing(ctt -> ctt.getHanSuDung())) // Ưu tiên lô hết hạn trước
                            .collect(Collectors.toList());
                        int soLuongConLai = soLuong;
                        for (ChiTietThuoc ctt : listCTT) {
                            if (soLuongConLai <= 0) break;
                            int soLuongXuat = Math.min(ctt.getSoLuong(), soLuongConLai);
                            if (soLuongXuat <= 0) continue;
                            ChiTietHoaDon cthd = new ChiTietHoaDon(new HoaDon(maHD), ctt, soLuongXuat, dvt, "Bán", soLuongXuat * donGia);
                            chiTietHoaDonDAO.themChiTietHoaDon(cthd);
                            // Trừ kho lô này (nếu có hàm updateTonKho thì gọi ở đây)
                            chiTietThuocDAO.CapNhatSoLuongChiTietThuoc(ctt.getMaCTT(), -soLuongXuat);
                            soLuongConLai -= soLuongXuat;
                        }
                    }
                }
            }
            // Đóng dialog (nếu cần, có thể show alert thành công ở đây)
            dialogPane.getScene().getWindow().hide();
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Tạo hoá đơn thành công");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Hoá đơn đã được tạo thành công!");
            successAlert.showAndWait();
        });
    }

    // Thêm một hàng thuốc mới vào VBox chứa các hàng thuốc
    private void addMedicineRow() {
        HBox hbox = new HBox(10);
        hbox.setStyle("-fx-background-color: #f8fafc;");
        hbox.setPadding(new javafx.geometry.Insets(10));
        ComboBox<Thuoc> cbMedicine = new ComboBox<>();
        cbMedicine.setPrefWidth(150);
        cbMedicine.setPromptText("Chọn thuốc");
        cbMedicine.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        ComboBox<DonViTinh> cb_unit = new ComboBox<>();
        cb_unit.setPrefWidth(150);
        cb_unit.setPromptText("Đơn vị");
        cb_unit.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        TextField txtQuantity = new TextField();
        txtQuantity.setPromptText("Số lượng");
        txtQuantity.getStyleClass().add("text-field");
        txtQuantity.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        TextField txt_price = new TextField();
        txt_price.setPromptText("Đơn giá");
        txt_price.getStyleClass().add("text-field");
        txt_price.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        Button btnRemove = new Button("X");
        btnRemove.setStyle(
            "-fx-padding: 5 8 5 8;" +
            "-fx-background-color: #ef4444;" +
            "-fx-background-radius: 50%;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;"
        );
        btnRemove.setOnAction(e -> medicineRowsVBox.getChildren().remove(hbox));
        hbox.getChildren().addAll(cbMedicine, cb_unit, txtQuantity, txt_price, btnRemove);
        medicineRowsVBox.getChildren().add(hbox);
        setupMedicineRow(hbox);
    }

    // Thiết lập sự kiện và logic cho một hàng thuốc mới thêm vào
    private void setupMedicineRow(HBox hbox) {
        ComboBox<Thuoc> cbMedicine = (ComboBox<Thuoc>) hbox.getChildren().get(0);
        ComboBox<DonViTinh> cb_unit = (ComboBox<DonViTinh>) hbox.getChildren().get(1);
        TextField txtQuantity = (TextField) hbox.getChildren().get(2);
        TextField txt_price = (TextField) hbox.getChildren().get(3);
        txt_price.setEditable(false); // Khóa không cho sửa giá
        Thuoc_DAO thuocDAO = new Thuoc_DAO();
        var thuocList = FXCollections.observableArrayList(thuocDAO.getAllThuoc());
        cbMedicine.setItems(thuocList);
        cbMedicine.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Thuoc thuoc) {
                return thuoc == null ? "" : thuoc.getTenThuoc();
            }
            @Override
            public Thuoc fromString(String s) {
                return thuocList.stream().filter(t -> t.getTenThuoc().equals(s)).findFirst().orElse(null);
            }
        });

        // Khi chọn thuốc, tự động set đơn vị cơ sở và không cho chọn đơn vị khác
        cbMedicine.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                DonViTinh_DAO donViTinhDAO = new DonViTinh_DAO();
                // Chỉ lấy đơn vị cơ sở của thuốc
                DonViTinh dvtCoSo = newVal.getMaDVTCoSo();
                if (dvtCoSo != null) {
                    // Lấy thông tin đầy đủ của đơn vị tính từ database
                    DonViTinh dvtFull = donViTinhDAO.getDVTTheoMa(dvtCoSo.getMaDVT());
                    if (dvtFull != null) {
                        cb_unit.setItems(FXCollections.observableArrayList(dvtFull));
                        cb_unit.getSelectionModel().selectFirst();
                        cb_unit.setDisable(true); // Không cho chọn đơn vị khác
                    }
                }
                // Hiển thị giá bán
                txt_price.setText(VND_FORMAT.format(newVal.getGiaBan()) + "đ");
            } else {
                cb_unit.getItems().clear();
                cb_unit.setDisable(false);
                txt_price.clear();
            }
            updateSummary();
        });

        txtQuantity.textProperty().addListener((obs, oldVal, newVal) -> updateSummary());
        txt_price.textProperty().addListener((obs, oldVal, newVal) -> updateSummary());
        cb_unit.valueProperty().addListener((obs, oldVal, newVal) -> updateSummary());
    }

    //Hàm tính tổng tiền, tạm tính Cập nhật lại phần tóm tắt hoá đơn (tạm tính, thuế, tổng tiền)
    private void updateSummary() {
        double tongTamTinh = 0;
        double tongThue = 0;
        double tongTien = 0;
        for (var node : medicineRowsVBox.getChildren()) {
            if (node instanceof HBox hbox) {
                ComboBox<Thuoc> cbMedicine = (ComboBox<Thuoc>) hbox.getChildren().get(0);
                TextField txtQuantity = (TextField) hbox.getChildren().get(2);
                TextField txt_price = (TextField) hbox.getChildren().get(3);
                txt_price.setEditable(false); // Khóa không cho sửa giá
                int quantity = 0;
                try {
                    quantity = Integer.parseInt(txtQuantity.getText().trim());
                } catch (Exception ignored) {}
                double price = 0;
                try {
                    price = parseCurrency(txt_price.getText().trim());
                } catch (Exception ignored) {}
                double taxPercent = 0;
                Thuoc selectedThuoc = cbMedicine.getValue();
                if (selectedThuoc != null) {
                    taxPercent = selectedThuoc.getThue();
                }
                double tamTinh = quantity * price;
                // Tính thuế VAT: giá chưa bao gồm thuế, thuế = thành tiền × tỷ lệ thuế
                double thue = tamTinh * taxPercent;
                tongTamTinh += tamTinh;
                tongThue += thue;
            }
        }
        tongTien = tongTamTinh + tongThue;
        // Áp dụng khuyến mãi nếu có
        KhuyenMai km = cb_promotion.getValue();
        double giam = 0;
        if (km != null) {
            double so = km.getSo();
            if (so < 100) {
                giam = tongTien * so / 100.0;
            } else if (so >= 1000) {
                giam = so;
            }
            if (giam > tongTien) giam = tongTien;
            tongTien -= giam;
        }
        txtTamTinh.setText(VND_FORMAT.format(tongTamTinh) + "đ");
        txtThue.setText(VND_FORMAT.format(tongThue) + "đ");
        if (km != null && giam > 0) {
            txtTongTien.setText(VND_FORMAT.format(tongTien) + "đ (đã giảm " + VND_FORMAT.format(giam) + "đ)");
        } else {
            txtTongTien.setText(VND_FORMAT.format(tongTien) + "đ");
        }
    }

    /**
     * Sinh mã hoá đơn mới chưa tồn tại trong CSDL dạng HDxxx
     */
    private String generateNewMaHoaDon() {
        HoaDon_DAO hoaDonDAO = new HoaDon_DAO();
        List<String> allMaHD = hoaDonDAO.getAllHoaDon().stream().map(hd -> hd.getMaHD()).collect(Collectors.toList());
        int max = 0;
        for (String ma : allMaHD) {
            if (ma != null && ma.matches("HD\\d+")) {
                int num = Integer.parseInt(ma.substring(2));
                if (num > max) max = num;
            }
        }
        return String.format("HD%03d", max + 1);
    }

    // Xử lý khi nhấn nút tạo hóa đơn
    private String getOrCreateMaKhachHang() {
        String tenKH = txtTenKhachHang.getText().trim();
        if (tenKH.isEmpty()) return null;
        KhachHang_DAO khachHangDAO = new KhachHang_DAO();
        List<KhachHang> allKH = khachHangDAO.getAllKhachHang();
        for (KhachHang kh : allKH) {
            if (kh.getTenKH().equalsIgnoreCase(tenKH)) {
                return kh.getMaKH();
            }
        }
        String newMaKH = generateNewMaKhachHang(allKH);
        // Sinh số điện thoại ngẫu nhiên, đảm bảo không trùng lặp
        String uniquePhone = generateUniquePhoneNumber(allKH);
        KhachHang newKH = new KhachHang(newMaKH, tenKH, uniquePhone, false);
        khachHangDAO.insertKhachHang(newKH);
        return newMaKH;
    }

    // Sinh mã khách hàng mới chưa tồn tại trong CSDL dạng KHxxx hoặc KH000000001
    private String generateNewMaKhachHang(List<KhachHang> allKH) {
        int max = 0;
        int digitCount = 3; // Mặc định nếu chưa có mã nào
        for (KhachHang kh : allKH) {
            String ma = kh.getMaKH();
            if (ma != null && ma.matches("KH\\d+")) {
                String numberPart = ma.substring(2);
                int num = Integer.parseInt(numberPart);
                if (num > max) max = num;
                if (numberPart.length() > digitCount) digitCount = numberPart.length();
            }
        }
        return String.format("KH%0" + digitCount + "d", max + 1);
    }

    // Sinh số điện thoại ngẫu nhiên, đảm bảo không trùng với các khách hàng đã có
    private String generateUniquePhoneNumber(List<KhachHang> allKH) {
        java.util.Set<String> usedPhones = allKH.stream().map(KhachHang::getSoDienThoai).collect(Collectors.toSet());
        String phone;
        do {
            phone = "09" + (long)(Math.random() * 1_000_000_000L);
            phone = phone.substring(0, 10); // Đảm bảo 10 số
        } while (usedPhones.contains(phone));
        return phone;
    }

    // Chuyển chuỗi định dạng tiền tệ về số double
    private double parseCurrency(String currencyString) throws Exception {
        if (currencyString == null || currencyString.trim().isEmpty()) {
            return 0;
        }
        // Loại bỏ ký tự không phải số và không phải dấu chấm thập phân
        String numericString = currencyString.replaceAll("[^0-9]", "");
        return Double.parseDouble(numericString);
    }
}

