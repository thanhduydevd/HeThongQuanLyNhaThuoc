package com.antam.app.controller;

import com.antam.app.dao.ThongKeDoanhThu_DAO;
import com.antam.app.dao.ThongKeTrangChinh_DAO;
import com.antam.app.entity.NhanVien;
import com.antam.app.entity.ThongKeDoanhThu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class ThongKeDoanhThuController implements Initializable {

    @FXML private Button btnXuatBaoCao;
    @FXML private ComboBox<String> cmbThoiGian;
    @FXML private ComboBox<String> cmbNhanVien;

    @FXML private Text txtDoanhThuKy;
    @FXML private Text txtSoDonHang;
    @FXML private Text txtDonHangTB;
    @FXML private Text txtSoKhachHang;

    @FXML private Button btnDoanhThuChange;
    @FXML private Button btnDonHangChange;
    @FXML private Button btnDonHangTBChange;
    @FXML private Button btnKhachHangChange;

    @FXML private LineChart<String, Number> chartDoanhThu;
    @FXML private BarChart<String, Number> chartTopSanPham;

    @FXML private TableView<ThongKeDoanhThu> tableChiTiet;
    @FXML private TableColumn<ThongKeDoanhThu, LocalDate> colNgay;
    @FXML private TableColumn<ThongKeDoanhThu, Integer> colSoDonHang;
    @FXML private TableColumn<ThongKeDoanhThu, Double> colDoanhThu;
    @FXML private TableColumn<ThongKeDoanhThu, Double> colDonHangTB;
    @FXML private TableColumn<ThongKeDoanhThu, Integer> colKhachHangMoi;
    @FXML private TableColumn<ThongKeDoanhThu, String> colNhanVienBan;

    @FXML private Button btnRefresh;

    private ThongKeDoanhThu_DAO thongKeDAO;
    private ThongKeTrangChinh_DAO thongKeTrangChinhDAO;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private String selectedNhanVien = null;
    private DecimalFormat formatter = new DecimalFormat("#,###");
    private DecimalFormat percentFormatter = new DecimalFormat("#0.0");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        thongKeDAO = new ThongKeDoanhThu_DAO();
        thongKeTrangChinhDAO = new ThongKeTrangChinh_DAO();

        // Khởi tạo ComboBox thời gian
        cmbThoiGian.setItems(FXCollections.observableArrayList(
            "Hôm nay", "7 ngày qua", "30 ngày qua", "Tháng này", "Tháng trước", "Năm nay"
        ));
        cmbThoiGian.setValue("7 ngày qua");

        // Load danh sách nhân viên
        loadEmployeeList();

        // Khởi tạo bảng
        setupTableView();

        // Tính thời gian mặc định
        calculateDefaultTimeRange();

        // Load dữ liệu
        loadData();
    }

    private void setupTableView() {
        // Thêm placeholder cho bảng khi không có dữ liệu
        Label placeholderLabel = new Label("Không có dữ liệu");
        placeholderLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b;");
        tableChiTiet.setPlaceholder(placeholderLabel);

        colNgay.setCellValueFactory(new PropertyValueFactory<>("ngay"));
        colNgay.setCellFactory(column -> new TableCell<ThongKeDoanhThu, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
            }
        });

        colSoDonHang.setCellValueFactory(new PropertyValueFactory<>("soDonHang"));

        colDoanhThu.setCellValueFactory(new PropertyValueFactory<>("doanhThu"));
        colDoanhThu.setCellFactory(column -> new TableCell<ThongKeDoanhThu, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item) + " đ");
                }
            }
        });

        colDonHangTB.setCellValueFactory(new PropertyValueFactory<>("donHangTB"));
        colDonHangTB.setCellFactory(column -> new TableCell<ThongKeDoanhThu, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item) + " đ");
                }
            }
        });

        colKhachHangMoi.setCellValueFactory(new PropertyValueFactory<>("khachHangMoi"));
        colNhanVienBan.setCellValueFactory(new PropertyValueFactory<>("nhanVienBan"));
    }

    private void loadEmployeeList() {
        ArrayList<NhanVien> dsNhanVien = thongKeDAO.getDanhSachNhanVien();
        ObservableList<String> items = FXCollections.observableArrayList("Tất cả");
        for (NhanVien nv : dsNhanVien) {
            items.add(nv.getMaNV() + " - " + nv.getHoTen());
        }
        cmbNhanVien.setItems(items);
        cmbNhanVien.setValue("Tất cả");
    }

    /*
        * Tính toán khoảng thời gian mặc định dựa trên lựa chọn trong ComboBox thời gian
    * */
    private void calculateDefaultTimeRange() {
        String thoiGian = cmbThoiGian.getValue();
        denNgay = LocalDate.now();

        switch (thoiGian) {
            case "Hôm nay":
                tuNgay = LocalDate.now();
                break;
            case "7 ngày qua":
                tuNgay = LocalDate.now().minusDays(6);
                break;
            case "30 ngày qua":
                tuNgay = LocalDate.now().minusDays(29);
                break;
            case "Tháng này":
                tuNgay = LocalDate.now().withDayOfMonth(1);
                break;
            case "Tháng trước":
                tuNgay = LocalDate.now().minusMonths(1).withDayOfMonth(1);
                denNgay = LocalDate.now().withDayOfMonth(1).minusDays(1);
                break;
            case "Năm nay":
                tuNgay = LocalDate.now().withDayOfYear(1);
                break;
            default:
                tuNgay = LocalDate.now().minusDays(6);
        }
    }

    @FXML
    private void locTheoThoiGian(ActionEvent event) {
        calculateDefaultTimeRange();
        loadData();
    }

    @FXML
    private void locTheoNhanVien(ActionEvent event) {
        String selected = cmbNhanVien.getValue();
        if (selected != null && !selected.equals("Tất cả")) {
            selectedNhanVien = selected.split(" - ")[0];
        } else {
            selectedNhanVien = null;
        }
        loadData();
    }

    @FXML
    private void refreshData(ActionEvent event) {
        loadData();
    }

    private void loadData() {
        // Load dữ liệu tổng quan
        loadTongQuan();

        // Load biểu đồ
        loadChartDoanhThu();
        loadTopProductsChart();

        // Load bảng chi tiết
        loadTableData();
    }

    private void loadTongQuan() {
        double tongDoanhThu = thongKeDAO.getTongDoanhThu(tuNgay, denNgay, selectedNhanVien);
        int tongDonHang = thongKeDAO.getTongDonHang(tuNgay, denNgay, selectedNhanVien);
        int soKhachHang = thongKeDAO.getSoKhachHangMoi(tuNgay, denNgay, selectedNhanVien);
        double donHangTB = tongDonHang > 0 ? tongDoanhThu / tongDonHang : 0;

        // Cập nhật text
        txtDoanhThuKy.setText(formatCurrency(tongDoanhThu));
        txtSoDonHang.setText(String.valueOf(tongDonHang));
        txtDonHangTB.setText(formatCurrency(donHangTB));
        txtSoKhachHang.setText(String.valueOf(soKhachHang));

        // Tính % thay đổi so với kỳ trước (đơn giản hóa - có thể cải thiện)
        calculateChange();
    }

    /*
    * Tính % thay đổi so với kỳ trước
    * */
    private void calculateChange() {
        // Tính kỳ trước
        long days = java.time.temporal.ChronoUnit.DAYS.between(tuNgay, denNgay) + 1;
        LocalDate tuNgayTruoc = tuNgay.minusDays(days);
        LocalDate denNgayTruoc = denNgay.minusDays(days);

        double doanhThuHienTai = thongKeDAO.getTongDoanhThu(tuNgay, denNgay, selectedNhanVien);
        double doanhThuTruoc = thongKeDAO.getTongDoanhThu(tuNgayTruoc, denNgayTruoc, selectedNhanVien);

        int donHangHienTai = thongKeDAO.getTongDonHang(tuNgay, denNgay, selectedNhanVien);
        int donHangTruoc = thongKeDAO.getTongDonHang(tuNgayTruoc, denNgayTruoc, selectedNhanVien);

        int khachHangHienTai = thongKeDAO.getSoKhachHangMoi(tuNgay, denNgay, selectedNhanVien);
        int khachHangTruoc = thongKeDAO.getSoKhachHangMoi(tuNgayTruoc, denNgayTruoc, selectedNhanVien);

        updateChangeButton(btnDoanhThuChange, doanhThuHienTai, doanhThuTruoc);
        updateChangeButton(btnDonHangChange, donHangHienTai, donHangTruoc);
        updateChangeButton(btnDonHangTBChange,
            donHangHienTai > 0 ? doanhThuHienTai / donHangHienTai : 0,
            donHangTruoc > 0 ? doanhThuTruoc / donHangTruoc : 0);
        updateChangeButton(btnKhachHangChange, khachHangHienTai, khachHangTruoc);
    }

    /*
    * Cập nhật nút hiển thị % thay đổi
    * */
    private void updateChangeButton(Button btn, double current, double previous) {
        if (previous == 0) {
            btn.setText("N/A");
            return;
        }

        double change = ((current - previous) / previous) * 100;
        String text = (change >= 0 ? "+" : "") + percentFormatter.format(change) + "%";
        btn.setText(text);

        if (change >= 0) {
            btn.setStyle("-fx-background-color: #d1fae5; -fx-text-fill: #059669; -fx-background-radius: 50px;");
        } else {
            btn.setStyle("-fx-background-color: #fee2e2; -fx-text-fill: #dc2626; -fx-background-radius: 50px;");
        }
    }

//    * Load dữ liệu biểu đồ doanh thu
    private void loadChartDoanhThu() {
        chartDoanhThu.getData().clear();

        // Kiểm tra xem có phải hiển thị theo tháng không (cho khoảng thời gian dài như năm)
        String thoiGian = cmbThoiGian.getValue();
        boolean hienThiTheoThang = "Năm nay".equals(thoiGian);

        ArrayList<ThongKeDoanhThu> dsThongKe;
        if (hienThiTheoThang) {
            // Lấy dữ liệu theo tháng
            dsThongKe = thongKeDAO.getDoanhThuTheoThang(tuNgay, denNgay, selectedNhanVien);
        } else {
            // Lấy dữ liệu theo ngày
            dsThongKe = thongKeDAO.getDoanhThuTheoThoiGian(tuNgay, denNgay, selectedNhanVien);
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu");

        DateTimeFormatter dateFormatter = hienThiTheoThang
            ? DateTimeFormatter.ofPattern("MM/yyyy")
            : DateTimeFormatter.ofPattern("dd/MM");
        DateTimeFormatter fullDateFormatter = hienThiTheoThang
            ? DateTimeFormatter.ofPattern("'Tháng' MM/yyyy")
            : DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (hienThiTheoThang) {
            // Hiển thị theo tháng - không cần tạo đầy đủ các tháng trống
            Map<String, ThongKeDoanhThu> displayMap = new java.util.LinkedHashMap<>();

            for (ThongKeDoanhThu tk : dsThongKe) {
                String shortDate = tk.getNgay().format(dateFormatter);
                displayMap.put(shortDate, tk);
                series.getData().add(new XYChart.Data<>(shortDate, tk.getDoanhThu()));
            }

            chartDoanhThu.getData().add(series);

            // Thêm tooltip cho từng điểm dữ liệu
            for (XYChart.Data<String, Number> data : series.getData()) {
                ThongKeDoanhThu tk = displayMap.get(data.getXValue());
                if (tk != null) {
                    Tooltip tooltip = new Tooltip(
                        tk.getNgay().format(fullDateFormatter) + "\n" +
                        "Doanh thu: " + formatter.format(data.getYValue().doubleValue()) + " đ\n" +
                        "Số đơn hàng: " + tk.getSoDonHang() + "\n" +
                        "Đơn hàng TB: " + formatter.format(tk.getDonHangTB()) + " đ\n" +
                        "Khách hàng: " + tk.getKhachHangMoi()
                    );

                    tooltip.setShowDelay(javafx.util.Duration.millis(100));
                    tooltip.setStyle(
                        "-fx-font-size: 12px; " +
                        "-fx-background-color: rgba(30, 58, 138, 0.95); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 8px; " +
                        "-fx-background-radius: 6px;"
                    );

                    javafx.scene.Node node = data.getNode();
                    if (node != null) {
                        Tooltip.install(node, tooltip);

                        node.setOnMouseEntered(e -> {
                            node.setStyle(
                                "-fx-cursor: hand; " +
                                "-fx-background-color: #059669; " +
                                "-fx-background-radius: 8px;"
                            );
                            node.setScaleX(1.5);
                            node.setScaleY(1.5);
                        });
                        node.setOnMouseExited(e -> {
                            node.setStyle("");
                            node.setScaleX(1.0);
                            node.setScaleY(1.0);
                        });
                    }
                }
            }
        } else {
            // Hiển thị theo ngày - tạo đầy đủ các ngày
            Map<LocalDate, ThongKeDoanhThu> dataMap = new java.util.HashMap<>();
            for (ThongKeDoanhThu tk : dsThongKe) {
                dataMap.put(tk.getNgay(), tk);
            }

            Map<String, ThongKeDoanhThu> displayMap = new java.util.LinkedHashMap<>();

            LocalDate currentDate = tuNgay;
            while (!currentDate.isAfter(denNgay)) {
                ThongKeDoanhThu tk = dataMap.get(currentDate);

                if (tk == null) {
                    tk = new ThongKeDoanhThu(currentDate, 0, 0.0, 0.0, 0, "");
                }

                String shortDate = currentDate.format(dateFormatter);
                displayMap.put(shortDate, tk);
                series.getData().add(new XYChart.Data<>(shortDate, tk.getDoanhThu()));

                currentDate = currentDate.plusDays(1);
            }

            chartDoanhThu.getData().add(series);

            // Thêm tooltip cho từng điểm dữ liệu
            for (XYChart.Data<String, Number> data : series.getData()) {
                ThongKeDoanhThu tk = displayMap.get(data.getXValue());
                if (tk != null) {
                    Tooltip tooltip = new Tooltip(
                        "Ngày: " + tk.getNgay().format(fullDateFormatter) + "\n" +
                        "Doanh thu: " + formatter.format(data.getYValue().doubleValue()) + " đ\n" +
                        "Số đơn hàng: " + tk.getSoDonHang() + "\n" +
                        "Đơn hàng TB: " + formatter.format(tk.getDonHangTB()) + " đ\n" +
                        "Khách hàng: " + tk.getKhachHangMoi()
                    );

                    tooltip.setShowDelay(javafx.util.Duration.millis(100));
                    tooltip.setStyle(
                        "-fx-font-size: 12px; " +
                        "-fx-background-color: rgba(30, 58, 138, 0.95); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 8px; " +
                        "-fx-background-radius: 6px;"
                    );

                    javafx.scene.Node node = data.getNode();
                    if (node != null) {
                        Tooltip.install(node, tooltip);

                        node.setOnMouseEntered(e -> {
                            node.setStyle(
                                "-fx-cursor: hand; " +
                                "-fx-background-color: #059669; " +
                                "-fx-background-radius: 8px;"
                            );
                            node.setScaleX(1.5);
                            node.setScaleY(1.5);
                        });
                        node.setOnMouseExited(e -> {
                            node.setStyle("");
                            node.setScaleX(1.0);
                            node.setScaleY(1.0);
                        });
                    }
                }
            }
        }

        // Tùy chỉnh style cho LineChart
        chartDoanhThu.setCreateSymbols(true);
        chartDoanhThu.setLegendVisible(true);
    }

    /**
     * ==================== BIỂU ĐỒ TOP SẢN PHẨM ====================
     * Load biểu đồ top sản phẩm bán chạy
     */
    private void loadTopProductsChart() {
        if (chartTopSanPham == null) return;

        try {
            // *** CHỈNH SỐ SẢN PHẨM: Thay đổi số 5 để hiển thị nhiều/ít sản phẩm hơn ***
            Map<String, Integer> topProducts = thongKeTrangChinhDAO.getTopSanPhamBanChay(5);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Số lượng bán");

            // Map để lưu tên đầy đủ của thuốc
            Map<String, String> fullNamesMap = new java.util.HashMap<>();

            for (Map.Entry<String, Integer> entry : topProducts.entrySet()) {
                String tenThuocDayDu = entry.getKey();
                String tenThuoc = tenThuocDayDu;

                // *** CHỈNH ĐỘ DÀI TÊN: Thay đổi số 15 (độ dài tối đa) và 12 (độ dài cắt) ***
                // Rút ngắn tên thuốc nếu quá dài
                if (tenThuoc.length() > 15) {
                    tenThuoc = tenThuoc.substring(0, 12) + "...";
                }

                // Lưu tên đầy đủ
                fullNamesMap.put(tenThuoc, tenThuocDayDu);

                XYChart.Data<String, Number> data = new XYChart.Data<>(tenThuoc, entry.getValue());
                series.getData().add(data);
            }

            chartTopSanPham.getData().clear();
            chartTopSanPham.getData().add(series);

            // Thêm tooltip cho từng thanh trong biểu đồ
            for (XYChart.Data<String, Number> data : series.getData()) {
                String tenThuocHienThi = data.getXValue();
                String tenThuocDayDu = fullNamesMap.getOrDefault(tenThuocHienThi, tenThuocHienThi);
                int soLuong = data.getYValue().intValue();

                javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(
                        "Thuốc: " + tenThuocDayDu + "\n" +
                                "Số lượng bán: " + formatter.format(soLuong)
                );

                // *** CHỈNH MÀU TOOLTIP: Thay đổi màu nền và màu chữ ***
                tooltip.setShowDelay(javafx.util.Duration.millis(100));
                tooltip.setStyle(
                        "-fx-font-size: 12px; " +
                                "-fx-background-color: #1e3a8a; " +    // Màu nền tooltip (xanh lá)
                                "-fx-text-fill: white;"                 // Màu chữ tooltip (trắng)
                );

                // Gắn tooltip vào node của data point (thanh)
                javafx.scene.Node node = data.getNode();
                if (node != null) {
                    javafx.scene.control.Tooltip.install(node, tooltip);

                    // *** CHỈNH HIỆU ỨNG HOVER: Thay đổi độ mờ khi rê chuột ***
                    node.setOnMouseEntered(e -> {
                        node.setStyle(
                                "-fx-cursor: hand; " +
                                        "-fx-opacity: 0.8;"             // Độ mờ khi hover (0.0 - 1.0)
                        );
                    });
                    node.setOnMouseExited(e -> {
                        node.setStyle("-fx-opacity: 1.0;");
                    });
                }
            }

        } catch (Exception e) {
            System.err.println("Error loading top products chart: " + e.getMessage());
        }
    }

    private void loadTableData() {
        ArrayList<ThongKeDoanhThu> dsThongKe = thongKeDAO.getDoanhThuTheoThoiGian(tuNgay, denNgay, selectedNhanVien);
        ObservableList<ThongKeDoanhThu> data = FXCollections.observableArrayList(dsThongKe);
        tableChiTiet.setItems(data);
    }

    @FXML
    private void exportReport(ActionEvent event) {
        try {
            // Tạo FileChooser để chọn nơi lưu file
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Xuất báo cáo thống kê doanh thu");
            fileChooser.setInitialFileName("BaoCaoDoanhThu_" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".csv");

            // Thiết lập filter cho file CSV
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().add(extFilter);

            // Hiển thị dialog để chọn nơi lưu
            File file = fileChooser.showSaveDialog(btnXuatBaoCao.getScene().getWindow());

            if (file != null) {
                // Xuất dữ liệu ra file CSV
                exportToCSV(file);

                // Hiển thị thông báo thành công
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Xuất báo cáo thành công");
                alert.setHeaderText(null);
                alert.setContentText("Báo cáo đã được xuất thành công!\nĐường dẫn: " + file.getAbsolutePath());
                alert.showAndWait();
            }
        } catch (Exception e) {
            // Hiển thị thông báo lỗi
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi xuất báo cáo");
            alert.setHeaderText(null);
            alert.setContentText("Có lỗi xảy ra khi xuất báo cáo: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    /* *
     * Xuất báo cáo thống kê doanh thu ra file CSV
     * @param file File đích để lưu báo cáo
     * @throws IOException Nếu có lỗi trong quá trình ghi file
    * */
    private void exportToCSV(File file) throws IOException {
        // Kiểm tra xem có phải hiển thị theo tháng không
        String thoiGian = cmbThoiGian.getValue();
        boolean hienThiTheoThang = "Năm nay".equals(thoiGian);

        // Lấy dữ liệu
        ArrayList<ThongKeDoanhThu> dsThongKe;
        if (hienThiTheoThang) {
            dsThongKe = thongKeDAO.getDoanhThuTheoThang(tuNgay, denNgay, selectedNhanVien);
        } else {
            dsThongKe = thongKeDAO.getDoanhThuTheoThoiGian(tuNgay, denNgay, selectedNhanVien);
        }

        // Tạo FileWriter với UTF-8 BOM để Excel hiển thị đúng tiếng Việt
        try (FileWriter writer = new FileWriter(file)) {
            // Thêm UTF-8 BOM
            writer.write('\ufeff');

            // Ghi header thông tin báo cáo
            writer.append("BAO CAO THONG KE DOANH THU\n");
            writer.append("Thoi gian: ").append(thoiGian).append("\n");
            writer.append("Tu ngay: ").append(tuNgay.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
            writer.append("Den ngay: ").append(denNgay.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");

            String nhanVienText = selectedNhanVien != null ? selectedNhanVien : "Tat ca";
            writer.append("Nhan vien: ").append(nhanVienText).append("\n");
            writer.append("\n");

            // Ghi thông tin tổng quan
            writer.append("THONG TIN TONG QUAN\n");
            double tongDoanhThu = thongKeDAO.getTongDoanhThu(tuNgay, denNgay, selectedNhanVien);
            int tongDonHang = thongKeDAO.getTongDonHang(tuNgay, denNgay, selectedNhanVien);
            int soKhachHang = thongKeDAO.getSoKhachHangMoi(tuNgay, denNgay, selectedNhanVien);
            double donHangTB = tongDonHang > 0 ? tongDoanhThu / tongDonHang : 0;

            writer.append("Tong doanh thu,").append(String.format("%.2f", tongDoanhThu)).append(" VND\n");
            writer.append("Tong don hang,").append(String.valueOf(tongDonHang)).append("\n");
            writer.append("Don hang trung binh,").append(String.format("%.2f", donHangTB)).append(" VND\n");
            writer.append("So khach hang,").append(String.valueOf(soKhachHang)).append("\n");
            writer.append("\n");

            // Ghi header cho bảng chi tiết
            writer.append("CHI TIET DOANH THU\n");
            if (hienThiTheoThang) {
                writer.append("Thang,So don hang,Doanh thu (VND),Don hang TB (VND),Khach hang moi,Nhan vien ban\n");
            } else {
                writer.append("Ngay,So don hang,Doanh thu (VND),Don hang TB (VND),Khach hang moi,Nhan vien ban\n");
            }

            // Ghi dữ liệu chi tiết
            DateTimeFormatter dateFormatter = hienThiTheoThang
                ? DateTimeFormatter.ofPattern("MM/yyyy")
                : DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (ThongKeDoanhThu tk : dsThongKe) {
                writer.append(tk.getNgay().format(dateFormatter)).append(",");
                writer.append(String.valueOf(tk.getSoDonHang())).append(",");
                writer.append(String.format("%.2f", tk.getDoanhThu())).append(",");
                writer.append(String.format("%.2f", tk.getDonHangTB())).append(",");
                writer.append(String.valueOf(tk.getKhachHangMoi())).append(",");
                writer.append(tk.getNhanVienBan() != null ? tk.getNhanVienBan() : "").append("\n");
            }

            writer.append("\n");

            // Ghi top sản phẩm bán chạy
            writer.append("TOP SAN PHAM BAN CHAY\n");
            writer.append("Ten thuoc,So luong ban\n");

            Map<String, Integer> topSanPham = thongKeDAO.getTopSanPhamBanChay(tuNgay, denNgay, 10);
            for (Map.Entry<String, Integer> entry : topSanPham.entrySet()) {
                writer.append(entry.getKey()).append(",");
                writer.append(String.valueOf(entry.getValue())).append("\n");
            }

            writer.flush();
        }
    }

    private String formatCurrency(double amount) {
        if (amount >= 1_000_000_000) {
            return percentFormatter.format(amount / 1_000_000_000) + "B";
        } else if (amount >= 1_000_000) {
            return percentFormatter.format(amount / 1_000_000) + "M";
        } else if (amount >= 1_000) {
            return percentFormatter.format(amount / 1_000) + "K";
        }
        return formatter.format(amount);
    }
}

