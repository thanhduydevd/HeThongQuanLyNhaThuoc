//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller;

import com.antam.app.dao.HoaDon_DAO;
import com.antam.app.entity.HoaDon;
import com.antam.app.gui.GiaoDienCuaSo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.antam.app.controller.dialog.ViewInvoiceController;

import java.time.format.DateTimeFormatter;

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
    private Button btnReturnMedicine; // Nút trả thuốc
    @FXML
    private Button btnExchangeMedicine; // Nút đổi thuốc

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
            (new GiaoDienCuaSo("themhoadon")).showAndWait();
        });
        this.btnReturnMedicine.setOnAction((e) -> {
            (new GiaoDienCuaSo("trathuoc")).showAndWait();
        });
        this.btnExchangeMedicine.setOnAction((e) -> {
            (new GiaoDienCuaSo("doithuoc")).showAndWait();
        });

        // Thiết lập cách lấy dữ liệu cho từng cột TableView
        colMaHD.setCellValueFactory(new PropertyValueFactory<>("MaHD"));
        colNgayTao.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNgayTao().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        colKhachHang.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaKH().getMaKH()));
        colNhanVien.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaNV().getMaNV()));
        colKhuyenMai.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaKM() != null ? cellData.getValue().getMaKM().getMaKM() : ""));
        colTongTien.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTongTien()).asObject());
        colTrangThai.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isDeleteAt() ? "Đã huỷ" : "Hoạt động"));

        // Load dữ liệu hóa đơn từ DB lên bảng
        HoaDon_DAO hoaDonDAO = new HoaDon_DAO();
        ObservableList<HoaDon> hoaDonList = FXCollections.observableArrayList(hoaDonDAO.getAllHoaDon());
        table_invoice.setItems(hoaDonList);

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
                        ViewInvoiceController controller = (ViewInvoiceController) controllerObj;
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
    }
}
