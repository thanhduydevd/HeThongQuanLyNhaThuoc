//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller;

import com.antam.app.controller.dialog.DoiThuocController;
import com.antam.app.controller.dialog.TraThuocController;
import com.antam.app.controller.dialog.XoaSuaThuocController;
import com.antam.app.dao.HoaDon_DAO;
import com.antam.app.entity.HoaDon;
import com.antam.app.gui.GiaoDienCuaSo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.antam.app.controller.dialog.XemChiTietHoaDonController;
import com.antam.app.dao.NhanVien_DAO;
import com.antam.app.entity.NhanVien;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Controller cho giao diện quản lý hóa đơn (invoice_view.fxml).
 * - Hiển thị danh sách hóa đơn ra TableView.
 * - Cho phép tạo, đổi, trả hóa đơn qua các nút chức năng.
 * - Khi double-click vào 1 dòng hóa đơn sẽ mở dialog chi tiết hóa đơn.
 */
public class HoaDonController {
    // Các nút chức năng
    @FXML
    private Button btnAddInvoice; // Nút tạo hóa đơn mới
    @FXML
    private Button btnReturnMedicine; // Nút doi thuốc
    @FXML
    private Button btnExchangeMedicine; // Nút trả thuốc

    // TableView và các cột hiển thị danh sách hóa đơn
    @FXML
    private TableView<HoaDon> table_invoice; // Bảng danh sách hóa đơn
    @FXML
    private TableColumn<HoaDon, String> colMaHD; // Cột mã hóa đơn
    @FXML
    private TableColumn<HoaDon, String> colNgayTao; // Cột ngày tạo
    @FXML
    private TableColumn<HoaDon, String> colKhachHang; // Cột mã khách hàng
    @FXML
    private TableColumn<HoaDon, String> colNhanVien; // Cột mã nhân viên
    @FXML
    private TableColumn<HoaDon, String> colKhuyenMai; // Cột mã khuyến mãi
    @FXML
    private TableColumn<HoaDon, Double> colTongTien; // Cột tổng tiền
    @FXML
    private TableColumn<HoaDon, String> colTrangThai; // Cột trạng thái (đã huỷ/hoạt động)
    @FXML
    private javafx.scene.control.TextField txtSearchInvoice; // Ô nhập mã hóa đơn cần tìm
    @FXML
    private javafx.scene.control.Button btnSearchInvoice; // Nút tìm kiếm hóa đơn theo mã
    @FXML
    private ComboBox<NhanVien> cbEmployee; // ComboBox chọn nhân viên
    @FXML
    private ComboBox<String> cbStatus; // ComboBox chọn trạng thái
    @FXML
    private ComboBox<String> cbPrice; // ComboBox chọn khoảng giá
    @FXML
    private DatePicker cbFirstDate; // DatePicker chọn ngày bắt đầu
    @FXML
    private DatePicker cbEndDate;   // DatePicker chọn ngày kết thúc

    // Định dạng tiền tệ kiểu Việt Nam: 1.000đ, 10.000đ
    private static final DecimalFormat VND_FORMAT;
    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        VND_FORMAT = new DecimalFormat("#,##0", symbols);
    }

    public HoaDonController() {
    }

    /**
     * Khởi tạo controller, gán sự kiện cho các nút và TableView.
     * - Thiết lập cell value factory cho các cột TableView.
     * - Load dữ liệu hóa đơn từ DB lên bảng.
     * - Gán sự kiện double-click vào dòng để xem chi tiết hóa đơn.
     */
    public void initialize() {
        // Gán sự kiện cho các nút chức năng
        this.btnAddInvoice.setOnAction((e) -> {
            GiaoDienCuaSo dialog = new GiaoDienCuaSo("themhoadon");
            dialog.showAndWait();
            // Sau khi dialog đóng, cập nhật lại bảng và chọn/tô màu dòng mới
            HoaDon_DAO hoaDonDAO = new HoaDon_DAO();
            ObservableList<HoaDon> hoaDonList = FXCollections.observableArrayList(hoaDonDAO.getAllHoaDon());
            table_invoice.setItems(hoaDonList);
            // Tìm hoá đơn mới nhất (theo mã lớn nhất)
            HoaDon newest = null;
            int maxNum = -1;
            for (HoaDon hd : hoaDonList) {
                String ma = hd.getMaHD();
                if (ma != null && ma.matches("HD\\d+")) {
                    int num = Integer.parseInt(ma.substring(2));
                    if (num > maxNum) {
                        maxNum = num;
                        newest = hd;
                    }
                }
            }
            if (newest != null) {
                table_invoice.getSelectionModel().select(newest);
                table_invoice.scrollTo(newest);
            }
        });
        // duong
        this.btnExchangeMedicine.setOnAction((e) -> {
            HoaDon selectedHoaDon = table_invoice.getSelectionModel().getSelectedItem();
            if (selectedHoaDon == null) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Chưa chọn hóa đơn");
                alert.setContentText("Vui lòng chọn ít nhất một hóa đơn.");
                alert.showAndWait();
                return;
            }else{
                GiaoDienCuaSo dialog = new GiaoDienCuaSo("doithuoc");
                // Lấy controller và set Thuoc vào
                DoiThuocController controller = dialog.getController();
                controller.setHoaDon(selectedHoaDon);
                controller.showData(selectedHoaDon);
                dialog.showAndWait();
                HoaDon_DAO hoaDonDAO = new HoaDon_DAO();
                ObservableList<HoaDon> hoaDonList = FXCollections.observableArrayList(hoaDonDAO.getAllHoaDon());
                table_invoice.refresh();
                table_invoice.setItems(hoaDonList);
            }
        });
        // duong
        this.btnReturnMedicine.setOnAction((e) -> {
            HoaDon selectedHoaDon = table_invoice.getSelectionModel().getSelectedItem();
            if (selectedHoaDon == null) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Chưa chọn hóa đơn");
                alert.setContentText("Vui lòng chọn ít nhất một hóa đơn.");
                alert.showAndWait();
                return;
            }else{

                GiaoDienCuaSo dialog = new GiaoDienCuaSo("trathuoc");
                // Lấy controller và set Thuoc vào
                TraThuocController controller = dialog.getController();
                controller.setHoaDon(selectedHoaDon);
                controller.showData(selectedHoaDon);
                dialog.showAndWait();
                HoaDon_DAO hoaDonDAO = new HoaDon_DAO();
                ObservableList<HoaDon> hoaDonList = FXCollections.observableArrayList(hoaDonDAO.getAllHoaDon());
                table_invoice.refresh();
                table_invoice.setItems(hoaDonList);
            }
        });

        // Thiết lập cách lấy dữ liệu cho từng cột TableView

        colMaHD.setCellValueFactory(new PropertyValueFactory<>("MaHD"));
        colNgayTao.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNgayTao().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        colKhachHang.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaKH().getMaKH()));
        colNhanVien.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaNV().getMaNV()));
        colKhuyenMai.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaKM() != null ? cellData.getValue().getMaKM().getMaKM() : ""));
        colTongTien.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTongTien()).asObject());
        colTongTien.setCellFactory(column -> new javafx.scene.control.TableCell<HoaDon, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(VND_FORMAT.format(item) + "đ");
                }
            }
        });
        colTrangThai.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isDeleteAt() ? "Đã huỷ" : "Hoạt động"));

        // Load dữ liệu hóa đơn từ DB lên bảng
        HoaDon_DAO hoaDonDAO = new HoaDon_DAO();
        ObservableList<HoaDon> hoaDonList = FXCollections.observableArrayList(hoaDonDAO.getAllHoaDon());
        table_invoice.setItems(hoaDonList);

        // --- Khởi tạo ComboBox nhân viên ---
        NhanVien_DAO nhanVienDAO = new NhanVien_DAO();
        ObservableList<NhanVien> dsNhanVien = FXCollections.observableArrayList(NhanVien_DAO.getDsNhanVienformDBS());
        // Thêm lựa chọn "Tất cả" vào đầu danh sách
        NhanVien tatCaNV = new NhanVien("Tất cả");
        dsNhanVien.add(0, tatCaNV);
        cbEmployee.setItems(dsNhanVien);
        cbEmployee.setPromptText("Chọn nhân viên");
        // Hiển thị chỉ mã nhân viên trong ComboBox, "Tất cả" nếu là lựa chọn đặc biệt
        cbEmployee.setCellFactory(lv -> new javafx.scene.control.ListCell<NhanVien>() {
            @Override
            protected void updateItem(NhanVien item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getMaNV());
            }
        });
        cbEmployee.setButtonCell(new javafx.scene.control.ListCell<NhanVien>() {
            @Override
            protected void updateItem(NhanVien item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getMaNV());
            }
        });
        // --- Khởi tạo ComboBox trạng thái ---
        ObservableList<String> dsTrangThai = FXCollections.observableArrayList("Tất cả", "Hoạt động", "Đã huỷ");
        cbStatus.setItems(dsTrangThai);
        cbStatus.setValue("Tất cả");
        // --- Khởi tạo ComboBox khoảng giá ---
        ObservableList<String> dsKhoangGia = FXCollections.observableArrayList(
            "Tất cả",
            "Dưới 500.000",
            "500.000 - 1.000.000",
            "1.000.000 - 2.000.000",
            "Trên 2.000.000"
        );
        cbPrice.setItems(dsKhoangGia);
        cbPrice.setValue("Tất cả");

        // --- Hàm lọc hóa đơn theo nhân viên, trạng thái, khoảng giá, ngày ---
        Runnable filterInvoices = () -> {
            HoaDon_DAO hoaDonDAO1 = new HoaDon_DAO();
            NhanVien selectedNV = cbEmployee.getValue();
            String selectedStatus = cbStatus.getValue();
            String selectedPrice = cbPrice.getValue();
            LocalDate fromDate = cbFirstDate != null ? cbFirstDate.getValue() : null;
            LocalDate toDate = cbEndDate != null ? cbEndDate.getValue() : null;
            boolean allNV = (selectedNV == null || "Tất cả".equals(selectedNV.getMaNV()));
            boolean allStatus = (selectedStatus == null || "Tất cả".equals(selectedStatus));
            boolean allPrice = (selectedPrice == null || "Tất cả".equals(selectedPrice));
            ObservableList<HoaDon> filtered;
            // Lọc theo nhân viên và trạng thái trước
            ArrayList<HoaDon> baseList;
            if (allNV && allStatus) {
                baseList = new ArrayList<>(hoaDonDAO1.getAllHoaDon());
            } else if (!allNV && allStatus) {
                baseList = new ArrayList<>(hoaDonDAO1.searchHoaDonByMaNV(selectedNV.getMaNV()));
            } else if (allNV && !allStatus) {
                baseList = new ArrayList<>(hoaDonDAO1.searchHoaDonByStatus(selectedStatus));
            } else {
                ArrayList<HoaDon> byStatus = hoaDonDAO1.searchHoaDonByStatus(selectedStatus);
                baseList = new ArrayList<>();
                for (HoaDon hd : byStatus) {
                    if (hd.getMaNV() != null && selectedNV.getMaNV().equals(hd.getMaNV().getMaNV())) {
                        baseList.add(hd);
                    }
                }
            }
            // Lọc tiếp theo khoảng giá
            if (!allPrice) {
                double min = 0, max = Double.MAX_VALUE;
                switch (selectedPrice) {
                    case "Dưới 500.000":
                        max = 500000;
                        break;
                    case "500.000 - 1.000.000":
                        min = 500000;
                        max = 1000000;
                        break;
                    case "1.000.000 - 2.000.000":
                        min = 1000000;
                        max = 2000000;
                        break;
                    case "Trên 2.000.000":
                        min = 2000000;
                        max = Double.MAX_VALUE;
                        break;
                }
                ArrayList<HoaDon> priceFiltered = new ArrayList<>();
                for (HoaDon hd : baseList) {
                    double tongTien = hd.getTongTien();
                    if (tongTien >= min && tongTien < max) {
                        priceFiltered.add(hd);
                    }
                }
                baseList = priceFiltered;
            }
            // Lọc tiếp theo ngày (lấy hóa đơn có ngày tạo nằm trong khoảng [fromDate, toDate])
            if (fromDate != null || toDate != null) {
                ArrayList<HoaDon> dateFiltered = new ArrayList<>();
                for (HoaDon hd : baseList) {
                    LocalDate ngayTao = hd.getNgayTao();
                    boolean afterFrom = (fromDate == null) || !ngayTao.isBefore(fromDate);
                    boolean beforeTo = (toDate == null) || !ngayTao.isAfter(toDate);
                    if (afterFrom && beforeTo) {
                        dateFiltered.add(hd);
                    }
                }
                baseList = dateFiltered;
            }
            filtered = FXCollections.observableArrayList(baseList);
            table_invoice.setItems(filtered);
        };
        // Lắng nghe thay đổi trên cbEmployee, cbStatus, cbPrice, cbFirstDate, cbEndDate
        cbEmployee.valueProperty().addListener((obs, oldNV, newNV) -> filterInvoices.run());
        cbStatus.valueProperty().addListener((obs, oldSt, newSt) -> filterInvoices.run());
        cbPrice.valueProperty().addListener((obs, oldPr, newPr) -> filterInvoices.run());
        if (cbFirstDate != null) cbFirstDate.valueProperty().addListener((obs, oldDate, newDate) -> filterInvoices.run());
        if (cbEndDate != null) cbEndDate.valueProperty().addListener((obs, oldDate, newDate) -> filterInvoices.run());

        // Gán sự kiện double-click vào dòng để mở dialog xem chi tiết hóa đơn
        table_invoice.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && table_invoice.getSelectionModel().getSelectedItem() != null) {
                HoaDon selectedHoaDon = table_invoice.getSelectionModel().getSelectedItem();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/antam/app/views/sales/dialog/view_invoice.fxml"));
                    Parent root = loader.load();
                    Object controllerObj = loader.getController();
                    if (controllerObj == null) {
                        System.err.println("[DEBUG] Controller is null after loading FXML! Check fx:controller and FXML path.");
                    } else {
                        // Truyền hóa đơn được chọn sang dialog chi tiết
                        XemChiTietHoaDonController controller = (XemChiTietHoaDonController) controllerObj;
                        controller.setInvoice(selectedHoaDon);
                    }
                    // Hiển thị dialog chi tiết hóa đơn
                    Stage dialogStage = new Stage();
                    dialogStage.initModality(Modality.APPLICATION_MODAL);
                    dialogStage.setTitle("Chi tiết hóa đơn");
                    dialogStage.setScene(new Scene(root));
                    dialogStage.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // Lắng nghe thay đổi nội dung ô tìm kiếm để search realtime
        txtSearchInvoice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                // Nếu ô tìm kiếm rỗng, load lại toàn bộ hóa đơn
                ObservableList<HoaDon> allHoaDon = FXCollections.observableArrayList(hoaDonDAO.getAllHoaDon());
                table_invoice.setItems(allHoaDon);
            } else {
                // Nếu có nội dung, search theo mã
                ObservableList<HoaDon> searchResult = FXCollections.observableArrayList(hoaDonDAO.searchHoaDonByMaHd(newValue));
                table_invoice.setItems(searchResult);
            }
        });
        // Sự kiện cho nút tìm kiếm vẫn giữ lại để người dùng có thể bấm nút
        btnSearchInvoice.setOnAction(e -> {
            String maHd = txtSearchInvoice.getText();
            if (maHd == null || maHd.trim().isEmpty()) {
                ObservableList<HoaDon> allHoaDon = FXCollections.observableArrayList(hoaDonDAO.getAllHoaDon());
                table_invoice.setItems(allHoaDon);
            } else {
                ObservableList<HoaDon> searchResult = FXCollections.observableArrayList(hoaDonDAO.searchHoaDonByMaHd(maHd));
                table_invoice.setItems(searchResult);
            }
        });
        // Đặt row factory để tô màu dòng được chọn
        this.table_invoice.setRowFactory(tv -> new javafx.scene.control.TableRow<>() {
            @Override
            protected void updateItem(HoaDon item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null && isSelected()) {
                    setStyle("-fx-background-color: #d1fae5;"); // Màu xanh nhạt
                } else {
                    setStyle("");
                }
            }
        });
        // Bỏ chọn khi click 1 lần vào dòng đã chọn (chỉ double click mới bỏ chọn)
        this.table_invoice.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                int selectedIndex = table_invoice.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0 && table_invoice.getFocusModel().getFocusedIndex() == selectedIndex) {
                    table_invoice.getSelectionModel().clearSelection();
                }
            }
            // Sự kiện double click mở chi tiết hóa đơn
            if (event.getClickCount() == 2 && table_invoice.getSelectionModel().getSelectedItem() != null) {
                HoaDon selectedHoaDon = table_invoice.getSelectionModel().getSelectedItem();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/antam/app/views/sales/dialog/view_invoice.fxml"));
                    Parent root = loader.load();
                    Object controllerObj = loader.getController();
                    if (controllerObj == null) {
                        System.err.println("[DEBUG] Controller is null after loading FXML! Check fx:controller and FXML path.");
                    } else {
                        // Truyền hóa đơn được chọn sang dialog chi tiết
                        XemChiTietHoaDonController controller = (XemChiTietHoaDonController) controllerObj;
                        controller.setInvoice(selectedHoaDon);
                    }
                    // Hiển thị dialog chi tiết hóa đơn
                    Stage dialogStage = new Stage();
                    dialogStage.initModality(Modality.APPLICATION_MODAL);
                    dialogStage.setTitle("Chi tiết hóa đơn");
                    dialogStage.setScene(new Scene(root));
                    dialogStage.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
