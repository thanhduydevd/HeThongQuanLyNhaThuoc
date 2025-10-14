/*
 * @ (#) DashboardController.java   1.0 14/10/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.antam.app.controller;

import com.antam.app.dao.ThongKeTrangChinh_DAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/*
 * @description Controller for dashboard view
 * @author: Tran Tuan Hung
 * @date: 14/10/25
 * @version: 1.0
 */
public class TrangChuController implements Initializable {

    @FXML private Text txtTongSoThuoc;
    @FXML private Text txtSoNhanVien;
    @FXML private Text txtSoHoaDonHomNay;
    @FXML private Text txtSoKhuyenMai;
    @FXML private LineChart<String, Number> chartDoanhThu;
    @FXML private BarChart<String, Number> chartTopSanPham;
    @FXML private VBox vboxThuocSapHetHan;
    @FXML private VBox vboxThuocTonKhoThap;

    private ThongKeTrangChinh_DAO thongKeDAO;
    private final DecimalFormat formatter = new DecimalFormat("#,###");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        thongKeDAO = new ThongKeTrangChinh_DAO();
        loadDashboardData();
    }

    /**
     * Load tất cả dữ liệu cho dashboard
     */
    public void loadDashboardData() {
        loadBasicStatistics();
        loadRevenueChart();
        loadTopProductsChart();
        loadExpiredMedicines();
        loadLowStockMedicines();
    }

    /**
     * Load các thống kê cơ bản
     */
    private void loadBasicStatistics() {
        try {
            // Tổng số thuốc
            int tongSoThuoc = thongKeDAO.getTongSoThuoc();
            if (txtTongSoThuoc != null) {
                txtTongSoThuoc.setText(formatter.format(tongSoThuoc));
            }

            // Số nhân viên
            int soNhanVien = thongKeDAO.getTongSoNhanVien();
            if (txtSoNhanVien != null) {
                txtSoNhanVien.setText(formatter.format(soNhanVien));
            }

            // Số hóa đơn hôm nay
            int soHoaDonHomNay = thongKeDAO.getSoHoaDonHomNay();
            if (txtSoHoaDonHomNay != null) {
                txtSoHoaDonHomNay.setText(formatter.format(soHoaDonHomNay));
            }

            // Số khuyến mãi áp dụng
            int soKhuyenMai = thongKeDAO.getSoKhuyenMaiApDung();
            if (txtSoKhuyenMai != null) {
                txtSoKhuyenMai.setText(formatter.format(soKhuyenMai));
            }

        } catch (Exception e) {
            System.err.println("Error loading basic statistics: " + e.getMessage());
        }
    }

    /**
     * Load biểu đồ doanh thu 7 ngày
     */
    private void loadRevenueChart() {
        if (chartDoanhThu == null) return;

        try {
            Map<String, Double> doanhThuData = thongKeDAO.getDoanhThu7NgayGanNhat();

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Doanh thu (VNĐ)");

            // Tạo dữ liệu cho 7 ngày gần nhất
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM");
            for (int i = 6; i >= 0; i--) {
                LocalDate date = LocalDate.now().minusDays(i);
                String dateKey = date.toString();
                String displayDate = date.format(dateFormatter);

                double revenue = doanhThuData.getOrDefault(dateKey, 0.0);
                XYChart.Data<String, Number> data = new XYChart.Data<>(displayDate, revenue);
                series.getData().add(data);
            }

            chartDoanhThu.getData().clear();
            chartDoanhThu.getData().add(series);
            chartDoanhThu.setTitle("Doanh thu 7 ngày gần nhất");

            // Thêm tooltip cho từng điểm dữ liệu
            for (XYChart.Data<String, Number> data : series.getData()) {
                javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(
                    "Ngày: " + data.getXValue() + "\n" +
                    "Doanh thu: " + formatter.format(data.getYValue().doubleValue()) + " VNĐ"
                );

                // Cài đặt thời gian hiển thị tooltip
                tooltip.setShowDelay(javafx.util.Duration.millis(100));
                tooltip.setStyle("-fx-font-size: 12px; -fx-background-color: #1e3a8a; -fx-text-fill: white;");

                // Gắn tooltip vào node của data point
                javafx.scene.Node node = data.getNode();
                if (node != null) {
                    javafx.scene.control.Tooltip.install(node, tooltip);

                    // Thêm hiệu ứng hover
                    node.setOnMouseEntered(e -> {
                        node.setStyle("-fx-cursor: hand; -fx-scale-x: 1.5; -fx-scale-y: 1.5;");
                    });
                    node.setOnMouseExited(e -> {
                        node.setStyle("-fx-scale-x: 1.0; -fx-scale-y: 1.0;");
                    });
                }
            }

        } catch (Exception e) {
            System.err.println("Error loading revenue chart: " + e.getMessage());
        }
    }

    /**
     * Load biểu đồ top sản phẩm bán chạy
     */
    private void loadTopProductsChart() {
        if (chartTopSanPham == null) return;

        try {
            Map<String, Integer> topProducts = thongKeDAO.getTopSanPhamBanChay(5);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Số lượng bán");

            // Map để lưu tên đầy đủ của thuốc
            Map<String, String> fullNamesMap = new java.util.HashMap<>();

            for (Map.Entry<String, Integer> entry : topProducts.entrySet()) {
                String tenThuocDayDu = entry.getKey();
                String tenThuoc = tenThuocDayDu;

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
            chartTopSanPham.setTitle("Top 5 sản phẩm bán chạy (tháng này)");

            // Thêm tooltip cho từng thanh trong biểu đồ
            for (XYChart.Data<String, Number> data : series.getData()) {
                String tenThuocHienThi = data.getXValue();
                String tenThuocDayDu = fullNamesMap.getOrDefault(tenThuocHienThi, tenThuocHienThi);
                int soLuong = data.getYValue().intValue();

                javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(
                    "Thuốc: " + tenThuocDayDu + "\n" +
                    "Số lượng bán: " + formatter.format(soLuong)
                );

                // Cài đặt thời gian hiển thị tooltip
                tooltip.setShowDelay(javafx.util.Duration.millis(100));
                tooltip.setStyle("-fx-font-size: 12px; -fx-background-color: #16a34a; -fx-text-fill: white;");

                // Gắn tooltip vào node của data point (thanh)
                javafx.scene.Node node = data.getNode();
                if (node != null) {
                    javafx.scene.control.Tooltip.install(node, tooltip);

                    // Thêm hiệu ứng hover
                    node.setOnMouseEntered(e -> {
                        node.setStyle("-fx-cursor: hand; -fx-opacity: 0.8;");
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

    /**
     * Load danh sách thuốc sắp hết hạn
     */
    private void loadExpiredMedicines() {
        if (vboxThuocSapHetHan == null) return;

        try {
            List<Map<String, Object>> thuocSapHetHan = thongKeDAO.getThuocSapHetHan();

            // Xóa các item cũ (giữ lại header)
            if (vboxThuocSapHetHan.getChildren().size() > 1) {
                vboxThuocSapHetHan.getChildren().removeIf(node ->
                    !(node instanceof Text && ((Text) node).getStyleClass().contains("header")));
            }

            if (thuocSapHetHan.isEmpty()) {
                Label noDataLabel = new Label("Không có thuốc nào sắp hết hạn");
                noDataLabel.setStyle("-fx-text-fill: #64748b; -fx-font-style: italic;");
                vboxThuocSapHetHan.getChildren().add(noDataLabel);
            } else {
                // Hiển thị tối đa 5 thuốc
                int count = Math.min(thuocSapHetHan.size(), 5);
                for (int i = 0; i < count; i++) {
                    Map<String, Object> thuoc = thuocSapHetHan.get(i);
                    createExpiredMedicineItem(thuoc);
                }
            }

        } catch (Exception e) {
            System.err.println("Error loading expired medicines: " + e.getMessage());
        }
    }

    /**
     * Load danh sách thuốc tồn kho thấp
     */
    private void loadLowStockMedicines() {
        if (vboxThuocTonKhoThap == null) return;

        try {
            List<Map<String, Object>> thuocTonKhoThap = thongKeDAO.getThuocTonKhoThap();

            // Xóa các item cũ (giữ lại header)
            if (vboxThuocTonKhoThap.getChildren().size() > 1) {
                vboxThuocTonKhoThap.getChildren().removeIf(node ->
                    !(node instanceof Text && ((Text) node).getStyleClass().contains("header")));
            }

            if (thuocTonKhoThap.isEmpty()) {
                Label noDataLabel = new Label("Tất cả thuốc đều có tồn kho đủ");
                noDataLabel.setStyle("-fx-text-fill: #64748b; -fx-font-style: italic;");
                vboxThuocTonKhoThap.getChildren().add(noDataLabel);
            } else {
                // Hiển thị tối đa 5 thuốc
                int count = Math.min(thuocTonKhoThap.size(), 5);
                for (int i = 0; i < count; i++) {
                    Map<String, Object> thuoc = thuocTonKhoThap.get(i);
                    createLowStockMedicineItem(thuoc);
                }
            }

        } catch (Exception e) {
            System.err.println("Error loading low stock medicines: " + e.getMessage());
        }
    }

    /**
     * Tạo item hiển thị thuốc sắp hết hạn
     */
    private void createExpiredMedicineItem(Map<String, Object> thuoc) {
        try {
            HBox container = new HBox();
            container.setSpacing(10);
            container.setPadding(new Insets(5, 0, 5, 0));
            container.setStyle("-fx-border-color: #e2e8f0; -fx-border-width: 0 0 1 0;");

            VBox leftContent = new VBox();
            leftContent.setSpacing(2);

            Text tenThuoc = new Text((String) thuoc.get("tenThuoc"));
            tenThuoc.setFont(Font.font("System", 14));
            tenThuoc.setFill(Color.web("#1e3a8a"));

            Date hanSuDung = (Date) thuoc.get("hanSuDung");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Text ngayHetHan = new Text("Hết hạn: " + sdf.format(hanSuDung));
            ngayHetHan.setFont(Font.font("System", 12));
            ngayHetHan.setFill(Color.web("#dc2626"));

            leftContent.getChildren().addAll(tenThuoc, ngayHetHan);

            Text soLuong = new Text(formatter.format((Integer) thuoc.get("soLuongTon")) + " đơn vị");
            soLuong.setFont(Font.font("System", 12));
            soLuong.setFill(Color.web("#64748b"));

            container.getChildren().addAll(leftContent, soLuong);
            vboxThuocSapHetHan.getChildren().add(container);

        } catch (Exception e) {
            System.err.println("Error creating expired medicine item: " + e.getMessage());
        }
    }

    /**
     * Tạo item hiển thị thuốc tồn kho thấp
     */
    private void createLowStockMedicineItem(Map<String, Object> thuoc) {
        try {
            HBox container = new HBox();
            container.setSpacing(10);
            container.setPadding(new Insets(5, 0, 5, 0));
            container.setStyle("-fx-border-color: #e2e8f0; -fx-border-width: 0 0 1 0;");

            VBox leftContent = new VBox();
            leftContent.setSpacing(2);

            Text tenThuoc = new Text((String) thuoc.get("tenThuoc"));
            tenThuoc.setFont(Font.font("System", 14));
            tenThuoc.setFill(Color.web("#1e3a8a"));

            Text maThuoc = new Text("Mã: " + (String) thuoc.get("maThuoc"));
            maThuoc.setFont(Font.font("System", 12));
            maThuoc.setFill(Color.web("#64748b"));

            leftContent.getChildren().addAll(tenThuoc, maThuoc);

            Text soLuong = new Text(formatter.format((Integer) thuoc.get("soLuongTon")) + " đơn vị");
            soLuong.setFont(Font.font("System", 12));
            soLuong.setFill(Color.web("#dc2626"));

            container.getChildren().addAll(leftContent, soLuong);
            vboxThuocTonKhoThap.getChildren().add(container);

        } catch (Exception e) {
            System.err.println("Error creating low stock medicine item: " + e.getMessage());
        }
    }

    /**
     * Refresh dữ liệu dashboard
     */
    public void refreshData() {
        loadDashboardData();
    }
}
