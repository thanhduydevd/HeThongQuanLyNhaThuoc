/*
 * @ (#) ThongKeDoanhThuController.java   1.0 13/10/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.antam.app.controller;

import com.antam.app.dao.NhanVien_DAO;
import com.antam.app.dao.ThongKe_DAO;
import com.antam.app.entity.NhanVien;
import com.antam.app.entity.ThongKeDoanhThu;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

/*
 * @description Controller cho trang thống kê doanh thu - Quản lý giao diện và xử lý sự kiện
 * @author: Tran Tuan Hung
 * @date: 13/10/25
 * @version: 1.0
 */
public class ThongKeDoanhThuController implements Initializable {

    // ===== CÁC CONTROL TRÊN GIAO DIỆN =====

    // ComboBox để người dùng chọn bộ lọc
    @FXML private ComboBox<String> cmbThoiGian;  // Chọn khoảng thời gian (hôm nay, 7 ngày, tháng này...)
    @FXML private ComboBox<String> cmbNhanVien;  // Chọn nhân viên cụ thể hoặc "Tất cả"

    // Các Text hiển thị số liệu thống kê tổng quan
    @FXML private Text txtDoanhThuKy;    // Hiển thị tổng doanh thu kỳ
    @FXML private Text txtSoDonHang;     // Hiển thị tổng số đơn hàng
    @FXML private Text txtDonHangTB;     // Hiển thị giá trị đơn hàng trung bình
    @FXML private Text txtSoKhachHang;   // Hiển thị số khách hàng mới

    // Các Button hiển thị % thay đổi so với kỳ trước (màu xanh=tăng, đỏ=giảm)
    @FXML private Button btnDoanhThuChange;   // % thay đổi doanh thu
    @FXML private Button btnDonHangChange;    // % thay đổi số đơn hàng
    @FXML private Button btnDonHangTBChange;  // % thay đổi đơn hàng TB
    @FXML private Button btnKhachHangChange;  // % thay đổi khách hàng mới

    // Các biểu đồ hiển thị dữ liệu trực quan
    @FXML private ScatterChart<String, Number> chartDoanhThu;    // Biểu đồ doanh thu theo thời gian
    @FXML private BarChart<String, Number> chartTopSanPham;      // Biểu đồ top sản phẩm bán chạy

    // Bảng hiển thị chi tiết doanh thu theo ngày
    @FXML private TableView<ThongKeDoanhThu> tableChiTiet;
    @FXML private TableColumn<ThongKeDoanhThu, String> colNgay;         // Cột ngày
    @FXML private TableColumn<ThongKeDoanhThu, String> colSoDonHang;    // Cột số đơn hàng
    @FXML private TableColumn<ThongKeDoanhThu, String> colDoanhThu;     // Cột doanh thu
    @FXML private TableColumn<ThongKeDoanhThu, String> colDonHangTB;    // Cột đơn hàng TB
    @FXML private TableColumn<ThongKeDoanhThu, String> colKhachHangMoi; // Cột khách hàng mới
    @FXML private TableColumn<ThongKeDoanhThu, String> colNhanVienBan;  // Cột nhân viên bán

    // Các nút chức năng
    @FXML private Button btnXuatBaoCao;  // Nút xuất báo cáo CSV
    @FXML private Button btnRefresh;     // Nút làm mới dữ liệu

    // ===== CÁC BIẾN VÀ ĐỐI TƯỢNG HỖ TRỢ =====

    // Các DAO để truy vấn dữ liệu từ database
    private ThongKe_DAO thongKeDAO;      // DAO chính cho thống kê
    private NhanVien_DAO nhanVienDAO;    // DAO để lấy danh sách nhân viên

    // Các formatter để định dạng hiển thị dữ liệu
    private final DecimalFormat formatter = new DecimalFormat("#,###");                    // Format số tiền (1,000,000)
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Format ngày tháng

    // Các biến lưu trữ bộ lọc hiện tại
    private LocalDate tuNgay;           // Ngày bắt đầu của khoảng thời gian đang xem
    private LocalDate denNgay;          // Ngày kết thúc của khoảng thời gian đang xem
    private String maNhanVienSelected;  // Mã nhân viên được chọn (null = tất cả nhân viên)

    /**
     * Method khởi tạo được gọi tự động khi load FXML
     * Thiết lập các thành phần giao diện và load dữ liệu ban đầu
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Khởi tạo các DAO
        thongKeDAO = new ThongKe_DAO();
        nhanVienDAO = new NhanVien_DAO();

        // Thiết lập các thành phần giao diện
        setupComboBoxes();      // Thiết lập dữ liệu cho ComboBox
        setupTableColumns();    // Thiết lập cách hiển thị cho các cột bảng
        setupDefaultFilters();  // Thiết lập bộ lọc mặc định (7 ngày qua)
        loadData();            // Load và hiển thị dữ liệu lần đầu
    }

    /**
     * Thiết lập dữ liệu cho các ComboBox (dropdown)
     */
    private void setupComboBoxes() {
        // Thiết lập ComboBox chọn thời gian
        ObservableList<String> thoiGianOptions = FXCollections.observableArrayList(
            "Hôm nay", "7 ngày qua", "30 ngày qua", "Tháng này", "Tháng trước", "Năm này"
        );
        cmbThoiGian.setItems(thoiGianOptions);
        cmbThoiGian.setValue("7 ngày qua"); // Mặc định chọn 7 ngày qua

        // Thêm event listener cho ComboBox thời gian
        cmbThoiGian.setOnAction(e -> locTheoThoiGian());

        // Thiết lập ComboBox chọn nhân viên
        ObservableList<String> nhanVienOptions = FXCollections.observableArrayList();
        nhanVienOptions.add("Tất cả"); // Option đầu tiên để xem tất cả nhân viên

        // Lấy danh sách nhân viên từ database và thêm vào ComboBox
        try {
            ArrayList<NhanVien> danhSachNhanVien = NhanVien_DAO.getDsNhanVienformDBS();
            for (NhanVien nv : danhSachNhanVien) {
                // Format: "Tên nhân viên (Mã nhân viên)" để dễ nhận biết
                nhanVienOptions.add(nv.getHoTen() + " (" + nv.getMaNV() + ")");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi load danh sách nhân viên: " + e.getMessage());
        }

        cmbNhanVien.setItems(nhanVienOptions);
        cmbNhanVien.setValue("Tất cả"); // Mặc định xem tất cả nhân viên

        // Thêm event listener cho ComboBox nhân viên
        cmbNhanVien.setOnAction(e -> locTheoNhanVien());
    }

    /**
     * Thiết lập cách hiển thị dữ liệu cho các cột trong bảng chi tiết
     */
    private void setupTableColumns() {
        // Cột ngày: chuyển LocalDate thành String với format dd/MM/yyyy
        colNgay.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getNgay().format(dateFormatter)));

        // Cột số đơn hàng: chuyển int thành String
        colSoDonHang.setCellValueFactory(cellData ->
            new SimpleStringProperty(String.valueOf(cellData.getValue().getSoDonHang())));

        // Cột doanh thu: format số tiền và thêm ký hiệu ₫
        colDoanhThu.setCellValueFactory(cellData ->
            new SimpleStringProperty(formatter.format(cellData.getValue().getDoanhThu()) + " ₫"));

        // Cột đơn hàng trung bình: format số tiền và thêm ký hiệu ₫
        colDonHangTB.setCellValueFactory(cellData ->
            new SimpleStringProperty(formatter.format(cellData.getValue().getDonHangTrungBinh()) + " ₫"));

        // Cột khách hàng mới: chuyển int thành String
        colKhachHangMoi.setCellValueFactory(cellData ->
            new SimpleStringProperty(String.valueOf(cellData.getValue().getKhachHangMoi())));

        // Cột nhân viên bán: hiển thị trực tiếp String
        colNhanVienBan.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getNhanVienBan()));
    }

    /**
     * Thiết lập bộ lọc mặc định khi khởi động ứng dụng
     * Mặc định: xem dữ liệu 7 ngày qua, tất cả nhân viên
     */
    private void setupDefaultFilters() {
        LocalDate today = LocalDate.now();
        tuNgay = today.minusDays(6);  // 7 ngày qua = hôm nay - 6 ngày
        denNgay = today;              // Đến hôm nay
        maNhanVienSelected = null;    // Tất cả nhân viên
    }

    /**
     * Xử lý sự kiện khi người dùng chọn khoảng thời gian khác
     * Tính toán lại tuNgay và denNgay dựa trên lựa chọn
     */
    @FXML
    private void locTheoThoiGian() {
        String selected = cmbThoiGian.getValue();
        LocalDate today = LocalDate.now();

        // Tính toán khoảng thời gian dựa trên lựa chọn của người dùng
        switch (selected) {
            case "Hôm nay":
                tuNgay = today;
                denNgay = today;
                break;
            case "7 ngày qua":
                tuNgay = today.minusDays(6); // Hôm nay + 6 ngày trước = 7 ngày
                denNgay = today;
                break;
            case "30 ngày qua":
                tuNgay = today.minusDays(29); // Hôm nay + 29 ngày trước = 30 ngày
                denNgay = today;
                break;
            case "Tháng này":
                tuNgay = today.withDayOfMonth(1);  // Ngày 1 của tháng hiện tại
                denNgay = today;                   // Đến hôm nay
                break;
            case "Tháng trước":
                LocalDate lastMonth = today.minusMonths(1);
                tuNgay = lastMonth.withDayOfMonth(1);                           // Ngày 1 tháng trước
                denNgay = lastMonth.withDayOfMonth(lastMonth.lengthOfMonth());  // Ngày cuối tháng trước
                break;
            case "Năm này":
                tuNgay = today.withDayOfYear(1);  // Ngày 1/1 của năm hiện tại
                denNgay = today;                  // Đến hôm nay
                break;
        }
        // Sau khi thay đổi khoảng thời gian, load lại dữ liệu
        loadData();
    }

    /**
     * Xử lý sự kiện khi người dùng chọn nhân viên khác
     * Trích xuất mã nhân viên từ text hiển thị
     */
    @FXML
    private void locTheoNhanVien() {
        String selected = cmbNhanVien.getValue();
        if (selected.equals("Tất cả")) {
            maNhanVienSelected = null; // null = xem tất cả nhân viên
        } else {
            // Trích xuất mã nhân viên từ format "Tên (Mã)"
            int startIndex = selected.lastIndexOf("(") + 1;
            int endIndex = selected.lastIndexOf(")");
            maNhanVienSelected = selected.substring(startIndex, endIndex);
        }
        // Sau khi thay đổi nhân viên, load lại dữ liệu
        loadData();
    }

    /**
     * Xử lý sự kiện khi người dùng click nút "Refresh"
     * Đơn giản chỉ load lại dữ liệu với bộ lọc hiện tại
     */
    @FXML
    private void refreshData() {
        loadData();
    }

    /**
     * Method chính để load tất cả dữ liệu lên giao diện
     * Gọi các method con để load từng phần
     */
    private void loadData() {
        loadStatistics();  // Load 4 card thống kê tổng quan
        loadTable();       // Load bảng chi tiết theo ngày
        loadCharts();      // Load 2 biểu đồ
    }

    /**
     * Load dữ liệu cho 4 card thống kê tổng quan ở phía trên
     * Bao gồm: doanh thu, số đơn hàng, đơn hàng TB, khách hàng mới
     * Đồng thời tính % thay đổi so với kỳ trước
     */
    private void loadStatistics() {
        try {
            // Lấy dữ liệu kỳ hiện tại
            double doanhThuHienTai = thongKeDAO.getTongDoanhThu(tuNgay, denNgay, maNhanVienSelected);
            int donHangHienTai = thongKeDAO.getTongDonHang(tuNgay, denNgay, maNhanVienSelected);
            int khachHangMoi = thongKeDAO.getSoKhachHangMoi(tuNgay, denNgay);
            double donHangTB = donHangHienTai > 0 ? doanhThuHienTai / donHangHienTai : 0;

            // Tính khoảng thời gian của kỳ trước để so sánh
            long soNgay = java.time.temporal.ChronoUnit.DAYS.between(tuNgay, denNgay) + 1;
            LocalDate tuNgayTruoc = tuNgay.minusDays(soNgay);
            LocalDate denNgayTruoc = tuNgay.minusDays(1);

            // Lấy dữ liệu kỳ trước
            double doanhThuTruoc = thongKeDAO.getTongDoanhThu(tuNgayTruoc, denNgayTruoc, maNhanVienSelected);
            int donHangTruoc = thongKeDAO.getTongDonHang(tuNgayTruoc, denNgayTruoc, maNhanVienSelected);
            int khachHangMoiTruoc = thongKeDAO.getSoKhachHangMoi(tuNgayTruoc, denNgayTruoc);
            double donHangTBTruoc = donHangTruoc > 0 ? doanhThuTruoc / donHangTruoc : 0;

            // Cập nhật giao diện với dữ liệu mới (bao gồm % thay đổi)
            updateStatisticDisplay(txtDoanhThuKy, btnDoanhThuChange, doanhThuHienTai, doanhThuTruoc, true);
            updateStatisticDisplay(txtSoDonHang, btnDonHangChange, donHangHienTai, donHangTruoc, false);
            updateStatisticDisplay(txtDonHangTB, btnDonHangTBChange, donHangTB, donHangTBTruoc, true);
            updateStatisticDisplay(txtSoKhachHang, btnKhachHangChange, khachHangMoi, khachHangMoiTruoc, false);

        } catch (Exception e) {
            System.err.println("Lỗi khi load thống kê: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cập nhật một card thống kê cụ thể (giá trị + % thay đổi + màu sắc)
     * @param valueText Text hiển thị giá trị chính
     * @param changeButton Button hiển thị % thay đổi
     * @param current Giá trị kỳ hiện tại
     * @param previous Giá trị kỳ trước
     * @param isCurrency true nếu là tiền tệ (cần format), false nếu là số lượng
     */
    private void updateStatisticDisplay(Text valueText, Button changeButton, double current, double previous, boolean isCurrency) {
        // Cập nhật giá trị chính
        if (isCurrency) {
            valueText.setText(formatter.format(current)); // Format tiền tệ: 1,000,000
        } else {
            valueText.setText(String.valueOf((int)current)); // Hiển thị số nguyên: 50
        }

        // Tính % thay đổi so với kỳ trước
        double changePercent = 0;
        if (previous > 0) {
            changePercent = ((current - previous) / previous) * 100;
        } else if (current > 0) {
            changePercent = 100; // Tăng 100% nếu kỳ trước = 0, kỳ này > 0
        }

        // Cập nhật text hiển thị % thay đổi
        String changeText = String.format("%.1f%%", Math.abs(changePercent));
        changeButton.setText(changeText);

        // Đổi màu button theo xu hướng thay đổi
        if (changePercent > 0) {
            // Tăng -> màu xanh
            changeButton.setStyle("-fx-background-color: #86db8c; -fx-text-fill: green; -fx-background-radius: 50px;");
        } else if (changePercent < 0) {
            // Giảm -> màu đỏ
            changeButton.setStyle("-fx-background-color: #fee2e2; -fx-text-fill: red; -fx-background-radius: 50px;");
        } else {
            // Không đổi -> màu xám
            changeButton.setStyle("-fx-background-color: #e5e7eb; -fx-text-fill: gray; -fx-background-radius: 50px;");
        }
    }

    /**
     * Load dữ liệu cho bảng chi tiết doanh thu theo ngày
     */
    private void loadTable() {
        try {
            // Lấy danh sách thống kê chi tiết theo ngày từ DAO
            ArrayList<ThongKeDoanhThu> danhSach = thongKeDAO.getDanhSachChiTietDoanhThu(tuNgay, denNgay, maNhanVienSelected);
            System.out.println("Đã tải " + danhSach.size() + " bản ghi doanh thu chi tiết");

            // Chuyển đổi thành ObservableList để hiển thị trong TableView
            ObservableList<ThongKeDoanhThu> data = FXCollections.observableArrayList(danhSach);
            tableChiTiet.setItems(data);

            // Refresh table để đảm bảo hiển thị dữ liệu mới
            tableChiTiet.refresh();
        } catch (Exception e) {
            System.err.println("Lỗi khi load bảng chi tiết: " + e.getMessage());
            e.printStackTrace();

            // Hiển thị bảng trống khi có lỗi
            tableChiTiet.setItems(FXCollections.observableArrayList());
        }
    }

    /**
     * Load dữ liệu cho tất cả các biểu đồ
     */
    private void loadCharts() {
        try {
            loadRevenueChart();      // Biểu đồ doanh thu theo thời gian
            loadTopProductsChart();  // Biểu đồ top sản phẩm bán chạy
        } catch (Exception e) {
            System.err.println("Lỗi khi load biểu đồ: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load dữ liệu cho biểu đồ doanh thu theo thời gian (ScatterChart)
     */
    private void loadRevenueChart() {
        chartDoanhThu.getData().clear(); // Xóa dữ liệu cũ
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu"); // Tên series hiển thị trong legend

        // Lấy dữ liệu theo ngày và thêm vào biểu đồ
        ArrayList<ThongKeDoanhThu> data = thongKeDAO.getDoanhThuTheoNgay(tuNgay, denNgay, maNhanVienSelected);
        for (ThongKeDoanhThu item : data) {
            // Trục X: ngày (format dd/MM), Trục Y: doanh thu
            series.getData().add(new XYChart.Data<>(
                item.getNgay().format(DateTimeFormatter.ofPattern("dd/MM")),
                item.getDoanhThu()
            ));
        }

        chartDoanhThu.getData().add(series);
    }

    /**
     * Load dữ liệu cho biểu đồ top sản phẩm bán chạy (BarChart)
     */
    private void loadTopProductsChart() {
        chartTopSanPham.getData().clear(); // Xóa dữ liệu cũ
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Số lượng bán"); // Tên series hiển thị trong legend

        // Lấy top 10 sản phẩm bán chạy từ DAO
        Map<String, Integer> topProducts = thongKeDAO.getTopSanPhamBanChay(tuNgay, denNgay, 10);
        for (Map.Entry<String, Integer> entry : topProducts.entrySet()) {
            // Cắt tên sản phẩm nếu quá dài để tránh làm rối biểu đồ
            String productName = entry.getKey().length() > 15 ? entry.getKey().substring(0, 15) + "..." : entry.getKey();
            // Trục X: tên sản phẩm, Trục Y: số lượng bán
            series.getData().add(new XYChart.Data<>(productName, entry.getValue()));
        }

        chartTopSanPham.getData().add(series);
    }

    /**
     * Xử lý sự kiện khi người dùng click nút "Xuất báo cáo"
     * Mở dialog cho phép chọn vị trí lưu file CSV
     */
    @FXML
    private void xuatBaoCao() {
        try {
            // Tạo dialog chọn file
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Lưu báo cáo thống kê");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
            );
            // Tên file mặc định có ngày hiện tại
            fileChooser.setInitialFileName("thong_ke_doanh_thu_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy")) + ".csv");

            // Hiển thị dialog và lấy file được chọn
            File file = fileChooser.showSaveDialog(btnXuatBaoCao.getScene().getWindow());
            if (file != null) {
                exportToCSV(file); // Gọi method xuất CSV
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi xuất báo cáo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Xuất dữ liệu thống kê ra file CSV
     * @param file File được chọn để lưu
     */
    private void exportToCSV(File file) throws IOException {
        try (FileWriter writer = new FileWriter(file, java.nio.charset.StandardCharsets.UTF_8)) {
            // Viết header thông tin báo cáo
            writer.append("Báo cáo thống kê doanh thu\n");
            writer.append("Từ ngày: " + tuNgay.format(dateFormatter) + " đến ngày: " + denNgay.format(dateFormatter) + "\n");
            writer.append("Nhân viên: " + (maNhanVienSelected != null ? cmbNhanVien.getValue() : "Tất cả") + "\n\n");

            // Viết thống kê tổng quan
            writer.append("Tổng quan\n");
            writer.append("Doanh thu kỳ," + txtDoanhThuKy.getText() + "\n");
            writer.append("Số đơn hàng," + txtSoDonHang.getText() + "\n");
            writer.append("Đơn hàng trung bình," + txtDonHangTB.getText() + "\n");
            writer.append("Khách hàng mới," + txtSoKhachHang.getText() + "\n\n");

            // Viết chi tiết theo ngày
            writer.append("Chi tiết theo ngày\n");
            writer.append("Ngày,Số đơn hàng,Doanh thu,Đơn hàng TB,Khách hàng mới,Nhân viên bán\n");

            // Duyệt qua từng dòng trong bảng và ghi vào CSV
            for (ThongKeDoanhThu item : tableChiTiet.getItems()) {
                writer.append(item.getNgay().format(dateFormatter))
                      .append(",").append(String.valueOf(item.getSoDonHang()))
                      .append(",").append(formatter.format(item.getDoanhThu()))
                      .append(",").append(formatter.format(item.getDonHangTrungBinh()))
                      .append(",").append(String.valueOf(item.getKhachHangMoi()))
                      .append(",").append(item.getNhanVienBan())
                      .append("\n");
            }

            System.out.println("Đã xuất báo cáo thành công: " + file.getAbsolutePath());
        }
    }
}
