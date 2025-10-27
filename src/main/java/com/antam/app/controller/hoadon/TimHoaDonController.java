//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.hoadon;

import com.antam.app.dao.HoaDon_DAO;
import com.antam.app.dao.NhanVien_DAO;
import com.antam.app.entity.HoaDon;
import com.antam.app.entity.NhanVien;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
public class TimHoaDonController {
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
    private Button btnSearchInvoice; // Nút tìm kiếm hóa đơn theo mã
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

    public TimHoaDonController() {
    }

    /**
     * Khởi tạo controller, gán sự kiện cho các nút và TableView.
     * - Thiết lập cell value factory cho các cột TableView.
     * - Load dữ liệu hóa đơn từ DB lên bảng.
     * - Gán sự kiện double-click vào dòng để xem chi tiết hóa đơn.
     */
    public void initialize() {
        // Thiết lập cách lấy dữ liệu cho từng cột TableView
        colMaHD.setCellValueFactory(new PropertyValueFactory<>("MaHD"));
        colNgayTao.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNgayTao().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
        colKhachHang.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaKH().getTenKH()));
        colNhanVien.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaNV().getHoTen()));
        colKhuyenMai.setCellValueFactory(cellData -> {
            HoaDon hoaDon = cellData.getValue();
            String tenKM = (hoaDon.getMaKM() != null) ? hoaDon.getMaKM().getTenKM() : "Không có khuyến mãi";
            return new SimpleStringProperty(tenKM);
        });
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
        // Hiển thị tên nhân viên trong ComboBox thay vì mã
        cbEmployee.setCellFactory(lv -> new javafx.scene.control.ListCell<NhanVien>() {
            @Override
            protected void updateItem(NhanVien item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Hiển thị "Tất cả" cho lựa chọn đặc biệt, hoặc tên nhân viên
                    setText("Tất cả".equals(item.getMaNV()) ? "Tất cả" : item.getHoTen());
                }
            }
        });
        cbEmployee.setButtonCell(new javafx.scene.control.ListCell<NhanVien>() {
            @Override
            protected void updateItem(NhanVien item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Hiển thị "Tất cả" cho lựa chọn đặc biệt, hoặc tên nhân viên
                    setText("Tất cả".equals(item.getMaNV()) ? "Tất cả" : item.getHoTen());
                }
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
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/antam/app/views/hoadon/dialog/xemChiTietHoaDonForm.fxml"));
                    Parent root = loader.load();
                    Object controllerObj = loader.getController();
                    if (controllerObj == null) {
                        System.err.println("[DEBUG] Controller is null after loading FXML! Check fx:controller and FXML path.");
                    } else {
                        // Truyền hóa đơn được chọn sang dialog chi tiết
                        XemChiTietHoaDonFormController controller = (XemChiTietHoaDonFormController) controllerObj;
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
        // Chỉ gán duy nhất một handler cho sự kiện click
        this.table_invoice.setOnMouseClicked(event -> {
            // Nếu double click và có dòng được chọn thì mở dialog chi tiết hóa đơn
            if (event.getClickCount() == 2 && table_invoice.getSelectionModel().getSelectedItem() != null) {
                HoaDon selectedHoaDon = table_invoice.getSelectionModel().getSelectedItem();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/antam/app/views/hoadon/dialog/xemChiTietHoaDonForm.fxml"));
                    Parent root = loader.load();
                    Object controllerObj = loader.getController();
                    if (controllerObj == null) {
                        System.err.println("[DEBUG] Controller is null after loading FXML! Check fx:controller and FXML path.");
                    } else {
                        // Truyền hóa đơn được chọn sang dialog chi tiết
                        XemChiTietHoaDonFormController controller = (XemChiTietHoaDonFormController) controllerObj;
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
