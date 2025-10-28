/*
 * @ (#) TimKhachHangController.java   1.0 28/10/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.antam.app.controller.khachhang;

import com.antam.app.dao.HoaDon_DAO;
import com.antam.app.dao.KhachHang_DAO;
import com.antam.app.entity.HoaDon;
import com.antam.app.entity.KhachHang;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/*
 * @description Controller for customer search functionality
 * @author: Tran Tuan Hung
 * @date: 28/10/25
 * @version: 1.0
 */
public class TimKhachHangController implements Initializable {

    @FXML
    private TextField txtSearchEmployee;

    @FXML
    private Button btnSearchEmployee;

    @FXML
    private ComboBox<String> cbSoDonHang; // Số đơn hàng filter

    @FXML
    private ComboBox<String> cbTrangThai; // Loại khách hàng filter

    @FXML
    private ComboBox<String> cbTongChiTieu; // Tổng chi tiêu filter

    @FXML
    private TableView<KhachHang> tableViewKhachHang;

    @FXML
    private TableColumn<KhachHang, String> colMaKH;

    @FXML
    private TableColumn<KhachHang, String> colTenKH;

    @FXML
    private TableColumn<KhachHang, String> colSoDienThoai;

    @FXML
    private TableColumn<KhachHang, Integer> colSoDonHang;

    @FXML
    private TableColumn<KhachHang, Double> colTongChiTieu;

    @FXML
    private TableColumn<KhachHang, String> colDonHangGanNhat;

    @FXML
    private TableColumn<KhachHang, String> colLoaiKhachHang;

    private ObservableList<KhachHang> dsKhachHang;
    private ObservableList<KhachHang> dsKhachHangGoc; // Để lưu danh sách gốc cho việc lọc

    private KhachHang_DAO khachHangDAO;
    private HoaDon_DAO hoaDonDAO;
    private DateTimeFormatter formatter;

    // Định dạng tiền tệ kiểu Việt Nam: 1.000đ, 10.000đ
    private static final DecimalFormat VND_FORMAT;
    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        VND_FORMAT = new DecimalFormat("#,##0", symbols);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Khởi tạo các đối tượng DAO
        khachHangDAO = new KhachHang_DAO();
        hoaDonDAO = new HoaDon_DAO();
        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Thiết lập các cột của bảng
        setupTableColumns();

        // Tải dữ liệu từ database
        loadDataFromDB();

        // Thiết lập các sự kiện
        setupEventHandlers();

        // Thiết lập các giá trị cho ComboBox
        setupComboBoxes();
    }

    /**
     * Thiết lập các cột của bảng
     */
    private void setupTableColumns() {
        colMaKH.setCellValueFactory(new PropertyValueFactory<>("maKH"));
        colTenKH.setCellValueFactory(new PropertyValueFactory<>("tenKH"));
        colSoDienThoai.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        colSoDonHang.setCellValueFactory(new PropertyValueFactory<>("soDonHang"));

        // Định dạng cột Tổng chi tiêu theo VND
        colTongChiTieu.setCellValueFactory(new PropertyValueFactory<>("tongChiTieu"));
        colTongChiTieu.setCellFactory(column -> new TableCell<KhachHang, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.0f VNĐ", item));
                }
            }
        });

        colDonHangGanNhat.setCellValueFactory(cellData -> {
            KhachHang kh = cellData.getValue();
            String dateStr = kh.getNgayMuaGanNhat() != null
                ? kh.getNgayMuaGanNhat().format(formatter)
                : "N/A";
            return new javafx.beans.property.SimpleStringProperty(dateStr);
        });
        colLoaiKhachHang.setCellValueFactory(new PropertyValueFactory<>("loaiKhachHang"));
    }

    /**
     * Tải dữ liệu khách hàng từ database
     */
    private void loadDataFromDB() {
        try {
            // Lấy danh sách khách hàng từ database
            ArrayList<KhachHang> listKhachHang = khachHangDAO.loadBanFromDB();

            // Lấy danh sách hóa đơn để tính toán thống kê
            ArrayList<HoaDon> listHoaDon = hoaDonDAO.getAllHoaDon();

            // Tính toán thống kê cho mỗi khách hàng
            for (KhachHang kh : listKhachHang) {
                // Lọc các hóa đơn của khách hàng này
                List<HoaDon> hoaDonCuaKH = listHoaDon.stream()
                    .filter(hd -> hd.getMaKH().getMaKH().equals(kh.getMaKH()))
                    .collect(Collectors.toList());

                // Tính số đơn hàng
                kh.setSoDonHang(hoaDonCuaKH.size());

                // Tính tổng chi tiêu
                double tongChiTieu = hoaDonCuaKH.stream()
                    .mapToDouble(HoaDon::getTongTien)
                    .sum();
                kh.setTongChiTieu(tongChiTieu);

                // Lấy ngày mua gần nhất
                if (!hoaDonCuaKH.isEmpty()) {
                    java.time.LocalDate ngayGanNhat = hoaDonCuaKH.stream()
                        .map(HoaDon::getNgayTao)
                        .max(Comparator.naturalOrder())
                        .orElse(null);
                    kh.setNgayMuaGanNhat(ngayGanNhat);
                }
            }

            // Chuyển đổi sang ObservableList
            dsKhachHangGoc = FXCollections.observableArrayList(listKhachHang);
            dsKhachHang = FXCollections.observableArrayList(listKhachHang);

            // Gán dữ liệu cho TableView
            tableViewKhachHang.setItems(dsKhachHang);

        } catch (Exception e) {
            showError("Lỗi tải dữ liệu", "Không thể tải dữ liệu khách hàng từ database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Thiết lập các sự kiện cho UI
     */
    private void setupEventHandlers() {
        // Sự kiện tìm kiếm tự động khi gõ vào TextField
        txtSearchEmployee.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());

        // Sự kiện khi thay đổi ComboBox
        cbSoDonHang.setOnAction(event -> applyFilters());
        cbTrangThai.setOnAction(event -> applyFilters());
        cbTongChiTieu.setOnAction(event -> applyFilters());

        // Sự kiện double-click trên hàng trong bảng để chuyển sang chi tiết
        tableViewKhachHang.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                KhachHang selected = tableViewKhachHang.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    openChiTietKhachHangDialog(selected);
                }
            }
        });
    }

    /**
     * Thiết lập các giá trị cho ComboBox
     */
    private void setupComboBoxes() {
        // ComboBox Số đơn hàng
        ObservableList<String> soDonHangOptions = FXCollections.observableArrayList(
            "Chọn số đơn hàng",
            "1-5 đơn hàng",
            "6-10 đơn hàng",
            "11-20 đơn hàng",
            "Trên 20 đơn hàng"
        );
        cbSoDonHang.setItems(soDonHangOptions);
        cbSoDonHang.setValue("Chọn số đơn hàng");

        // ComboBox Loại khách hàng
        ObservableList<String> trangThaiOptions = FXCollections.observableArrayList(
            "Chọn loại khách hàng",
            "VIP",
            "Thường"
        );
        cbTrangThai.setItems(trangThaiOptions);
        cbTrangThai.setValue("Chọn loại khách hàng");

        // ComboBox Tổng chi tiêu
        ObservableList<String> tongChiTieuOptions = FXCollections.observableArrayList(
            "Chọn tổng chi tiêu",
            "Dưới 500,000 VNĐ",
            "500,000 - 1,000,000 VNĐ",
            "1,000,000 - 5,000,000 VNĐ",
            "Trên 5,000,000 VNĐ"
        );
        cbTongChiTieu.setItems(tongChiTieuOptions);
        cbTongChiTieu.setValue("Chọn tổng chi tiêu");
    }

    /**
     * Xử lý sự kiện tìm kiếm
     */
    private void handleSearch() {
        String searchText = txtSearchEmployee.getText().toLowerCase().trim();

        if (searchText.isEmpty()) {
            dsKhachHang.setAll(dsKhachHangGoc);
        } else {
            List<KhachHang> searchResult = dsKhachHangGoc.stream()
                .filter(kh -> kh.getMaKH().toLowerCase().contains(searchText)
                    || kh.getTenKH().toLowerCase().contains(searchText)
                    || kh.getSoDienThoai().contains(searchText))
                .collect(Collectors.toList());

            dsKhachHang.setAll(searchResult);
        }
    }

    /**
     * Áp dụng các bộ lọc
     */
    private void applyFilters() {
        List<KhachHang> filteredList = new ArrayList<>(dsKhachHangGoc);

        // Lọc theo số đơn hàng
        String selectedSoDonHang = cbSoDonHang.getValue();
        if (selectedSoDonHang != null && !selectedSoDonHang.equals("Chọn số đơn hàng")) {
            filteredList = filteredList.stream()
                .filter(kh -> filterBySoDonHang(kh, selectedSoDonHang))
                .collect(Collectors.toList());
        }

        // Lọc theo loại khách hàng
        String selectedTrangThai = cbTrangThai.getValue();
        if (selectedTrangThai != null && !selectedTrangThai.equals("Chọn loại khách hàng")) {
            filteredList = filteredList.stream()
                .filter(kh -> kh.getLoaiKhachHang().equals(selectedTrangThai))
                .collect(Collectors.toList());
        }

        // Lọc theo tổng chi tiêu
        String selectedTongChiTieu = cbTongChiTieu.getValue();
        if (selectedTongChiTieu != null && !selectedTongChiTieu.equals("Chọn tổng chi tiêu")) {
            filteredList = filteredList.stream()
                .filter(kh -> filterByTongChiTieu(kh, selectedTongChiTieu))
                .collect(Collectors.toList());
        }

        dsKhachHang.setAll(filteredList);
    }

    /**
     * Lọc khách hàng theo số đơn hàng
     */
    private boolean filterBySoDonHang(KhachHang kh, String range) {
        int soDonHang = kh.getSoDonHang();
        switch (range) {
            case "1-5 đơn hàng":
                return soDonHang >= 1 && soDonHang <= 5;
            case "6-10 đơn hàng":
                return soDonHang >= 6 && soDonHang <= 10;
            case "11-20 đơn hàng":
                return soDonHang >= 11 && soDonHang <= 20;
            case "Trên 20 đơn hàng":
                return soDonHang > 20;
            default:
                return true;
        }
    }

    /**
     * Lọc khách hàng theo tổng chi tiêu
     */
    private boolean filterByTongChiTieu(KhachHang kh, String range) {
        double tongChiTieu = kh.getTongChiTieu();
        switch (range) {
            case "Dưới 500,000 VNĐ":
                return tongChiTieu < 500000;
            case "500,000 - 1,000,000 VNĐ":
                return tongChiTieu >= 500000 && tongChiTieu <= 1000000;
            case "1,000,000 - 5,000,000 VNĐ":
                return tongChiTieu > 1000000 && tongChiTieu <= 5000000;
            case "Trên 5,000,000 VNĐ":
                return tongChiTieu > 5000000;
            default:
                return true;
        }
    }

    /**
     * Xử lý sự kiện double-click trên hàng
     */
    private void handleRowDoubleClick() {
        KhachHang selected = tableViewKhachHang.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openChiTietKhachHangDialog(selected);
        }
    }

    /**
     * Mở dialog xem chi tiết khách hàng
     */
    private void openChiTietKhachHangDialog(KhachHang khachHang) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/com/antam/app/views/khachhang/dialog/xemChiTietKhachHangForm.fxml")
            );
            javafx.scene.control.DialogPane dialogPane = loader.load();

            // Lấy controller từ FXML loader
            XemChiTietKhachHangController controller = loader.getController();

            // Truyền dữ liệu khách hàng vào controller
            controller.setKhachHang(khachHang);

            // Tạo Dialog
            javafx.scene.control.Dialog<Void> dialog = new javafx.scene.control.Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Chi tiết khách hàng");
            dialog.showAndWait();

        } catch (Exception e) {
            showError("Lỗi", "Không thể mở chi tiết khách hàng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Hiển thị lỗi
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Hiển thị thông tin
     */
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
