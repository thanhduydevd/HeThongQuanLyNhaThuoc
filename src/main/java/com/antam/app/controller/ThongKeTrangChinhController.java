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
public class ThongKeTrangChinhController implements Initializable {

    // ==================== CÁC THÀNH PHẦN GIAO DIỆN ====================
    @FXML private Text txtTongSoThuoc;
    @FXML private Text txtSoNhanVien;
    @FXML private Text txtSoHoaDonHomNay;
    @FXML private Text txtSoKhuyenMai;
    @FXML private LineChart<String, Number> chartDoanhThu;        // Biểu đồ đường doanh thu
    @FXML private BarChart<String, Number> chartTopSanPham;       // Biểu đồ cột sản phẩm
    @FXML private VBox vboxThuocSapHetHan;                        // Danh sách thuốc sắp hết hạn
    @FXML private VBox vboxThuocTonKhoThap;                       // Danh sách thuốc tồn kho thấp

    // ==================== CÁC BIẾN CẤU HÌNH ====================
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
     * ==================== BIỂU ĐỒ DOANH THU ====================
     * Load biểu đồ doanh thu 7 ngày gần nhất
     */
    private void loadRevenueChart() {
        if (chartDoanhThu == null) return;

        try {
            Map<String, Double> doanhThuData = thongKeDAO.getDoanhThu7NgayGanNhat();

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Doanh thu (VNĐ)");

            // Tạo dữ liệu cho 7 ngày gần nhất
            // *** CHỈNH SỬA SỐ NGÀY: Thay đổi số 6 thành (số_ngày - 1) ***
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

            // Thêm tooltip cho từng điểm dữ liệu
            for (XYChart.Data<String, Number> data : series.getData()) {
                javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(
                    "Ngày: " + data.getXValue() + "\n" +
                    "Doanh thu: " + formatter.format(data.getYValue().doubleValue()) + " VNĐ"
                );

                // *** CHỈNH MÀU TOOLTIP: Thay đổi màu nền và màu chữ ***
                tooltip.setShowDelay(javafx.util.Duration.millis(100));
                tooltip.setStyle(
                    "-fx-font-size: 12px; " +
                    "-fx-background-color: #1e3a8a; " +    // Màu nền tooltip (xanh dương đậm)
                    "-fx-text-fill: white;"                 // Màu chữ tooltip (trắng)
                );

                // Gắn tooltip vào node của data point
                javafx.scene.Node node = data.getNode();
                if (node != null) {
                    javafx.scene.control.Tooltip.install(node, tooltip);

                    // *** CHỈNH HIỆU ỨNG HOVER: Thay đổi kích thước khi rê chuột ***
                    node.setOnMouseEntered(e -> {
                        node.setStyle(
                            "-fx-cursor: hand; " +
                            "-fx-scale-x: 1.5; " +          // Phóng to 1.5 lần theo chiều ngang
                            "-fx-scale-y: 1.5;"             // Phóng to 1.5 lần theo chiều dọc
                        );
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
     * ==================== BIỂU ĐỒ TOP SẢN PHẨM ====================
     * Load biểu đồ top sản phẩm bán chạy
     */
    private void loadTopProductsChart() {
        if (chartTopSanPham == null) return;

        try {
            // *** CHỈNH SỐ SẢN PHẨM: Thay đổi số 5 để hiển thị nhiều/ít sản phẩm hơn ***
            Map<String, Integer> topProducts = thongKeDAO.getTopSanPhamBanChay(5);

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

    /**
     * ==================== DANH SÁCH THUỐC SẮP HẾT HẠN ====================
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
                // *** CHỈNH MÀU CHỮ: Thay đổi màu khi không có dữ liệu ***
                Label noDataLabel = new Label("Không có thuốc nào sắp hết hạn");
                noDataLabel.setStyle(
                    "-fx-text-fill: #64748b; " +            // Màu chữ (xám)
                    "-fx-font-style: italic;"               // Kiểu chữ nghiêng
                );
                vboxThuocSapHetHan.getChildren().add(noDataLabel);
            } else {
                // *** CHỈNH SỐ THUỐC: Thay đổi số 5 để hiển thị nhiều/ít thuốc hơn ***
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
     * ==================== DANH SÁCH THUỐC TỒN KHO THẤP ====================
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
                // *** CHỈNH MÀU CHỮ: Thay đổi màu khi không có dữ liệu ***
                Label noDataLabel = new Label("Tất cả thuốc đều có tồn kho đủ");
                noDataLabel.setStyle(
                    "-fx-text-fill: #64748b; " +            // Màu chữ (xám)
                    "-fx-font-style: italic;"               // Kiểu chữ nghiêng
                );
                vboxThuocTonKhoThap.getChildren().add(noDataLabel);
            } else {
                // *** CHỈNH SỐ THUỐC: Thay đổi số 5 để hiển thị nhiều/ít thuốc hơn ***
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
     * ==================== TẠO ITEM THUỐC SẮP HẾT HẠN ====================
     * Tạo item hiển thị thuốc sắp hết hạn
     */
    private void createExpiredMedicineItem(Map<String, Object> thuoc) {
        try {
            HBox container = new HBox();
            // *** CHỈNH KHOẢNG CÁCH: Thay đổi số trong setSpacing() ***
            container.setSpacing(10);                       // Khoảng cách giữa các phần tử (pixels)
            container.setPadding(new Insets(5, 0, 5, 0));  // Padding: top, right, bottom, left

            // *** CHỈNH MÀU VIỀN: Thay đổi màu và độ dày viền ***
            container.setStyle(
                "-fx-border-color: #e2e8f0; " +             // Màu viền (xám nhạt)
                "-fx-border-width: 0 0 1 0;"                // Độ dày viền: top right bottom left
            );

            VBox leftContent = new VBox();
            leftContent.setSpacing(2);

            // *** CHỈNH MÀU VÀ CỠ CHỮ TÊN THUỐC ***
            Text tenThuoc = new Text((String) thuoc.get("tenThuoc"));
            tenThuoc.setFont(Font.font("System", 14));      // Cỡ chữ: 14px
            tenThuoc.setFill(Color.web("#1e3a8a"));        // Màu chữ: xanh dương đậm

            // *** CHỈNH MÀU VÀ CỠ CHỮ NGÀY HẾT HẠN ***
            Date hanSuDung = (Date) thuoc.get("hanSuDung");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Text ngayHetHan = new Text("Hết hạn: " + sdf.format(hanSuDung));
            ngayHetHan.setFont(Font.font("System", 12));    // Cỡ chữ: 12px
            ngayHetHan.setFill(Color.web("#dc2626"));      // Màu chữ: đỏ (cảnh báo)

            leftContent.getChildren().addAll(tenThuoc, ngayHetHan);

            // *** CHỈNH MÀU VÀ CỠ CHỮ SỐ LƯỢNG ***
            Text soLuong = new Text(formatter.format((Integer) thuoc.get("soLuongTon")) + " đơn vị");
            soLuong.setFont(Font.font("System", 12));       // Cỡ chữ: 12px
            soLuong.setFill(Color.web("#64748b"));         // Màu chữ: xám

            container.getChildren().addAll(leftContent, soLuong);
            vboxThuocSapHetHan.getChildren().add(container);

        } catch (Exception e) {
            System.err.println("Error creating expired medicine item: " + e.getMessage());
        }
    }

    /**
     * ==================== TẠO ITEM THUỐC TỒN KHO THẤP ====================
     * Tạo item hiển thị thuốc tồn kho thấp
     */
    private void createLowStockMedicineItem(Map<String, Object> thuoc) {
        try {
            HBox container = new HBox();
            // *** CHỈNH KHOẢNG CÁCH: Thay đổi số trong setSpacing() ***
            container.setSpacing(10);                       // Khoảng cách giữa các phần tử (pixels)
            container.setPadding(new Insets(5, 0, 5, 0));  // Padding: top, right, bottom, left

            // *** CHỈNH MÀU VIỀN: Thay đổi màu và độ dày viền ***
            container.setStyle(
                "-fx-border-color: #e2e8f0; " +             // Màu viền (xám nhạt)
                "-fx-border-width: 0 0 1 0;"                // Độ dày viền: top right bottom left
            );

            VBox leftContent = new VBox();
            leftContent.setSpacing(2);

            // *** CHỈNH MÀU VÀ CỠ CHỮ TÊN THUỐC ***
            Text tenThuoc = new Text((String) thuoc.get("tenThuoc"));
            tenThuoc.setFont(Font.font("System", 14));      // Cỡ chữ: 14px
            tenThuoc.setFill(Color.web("#1e3a8a"));        // Màu chữ: xanh dương đậm

            // *** CHỈNH MÀU VÀ CỠ CHỮ MÃ THUỐC ***
            Text maThuoc = new Text("Mã: " + (String) thuoc.get("maThuoc"));
            maThuoc.setFont(Font.font("System", 12));       // Cỡ chữ: 12px
            maThuoc.setFill(Color.web("#64748b"));         // Màu chữ: xám

            leftContent.getChildren().addAll(tenThuoc, maThuoc);

            // *** CHỈNH MÀU VÀ CỠ CHỮ SỐ LƯỢNG (MÀU ĐỎ CẢNH BÁO) ***
            Text soLuong = new Text(formatter.format((Integer) thuoc.get("soLuongTon")) + " đơn vị");
            soLuong.setFont(Font.font("System", 12));       // Cỡ chữ: 12px
            soLuong.setFill(Color.web("#dc2626"));         // Màu chữ: đỏ (cảnh báo tồn kho thấp)

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
