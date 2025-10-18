//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller;

import com.antam.app.controller.dialog.SuaKhachHangController;
import com.antam.app.controller.dialog.XemKhachHangController;
import com.antam.app.dao.KhachHang_DAO;
import com.antam.app.entity.KhachHang;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class KhachHangController implements Initializable {

    @FXML
    private Text textCustomerQuantity;

    @FXML
    private Text textVipCustomer;

    @FXML
    private Text textOrderQuantity;

    @FXML
    private Text textRevenueQuantity;

    @FXML
    private TextField txtSearchEmployee;

    @FXML
    private Button btnSearchEmployee;

    @FXML
    private Button btnEditCustomer;

    @FXML
    private TableView<KhachHang> tableViewKhachHang;

    @FXML
    private TableColumn<KhachHang, String> colMaKH;

    @FXML
    private TableColumn<KhachHang, String> colTenKH;

    @FXML
    private TableColumn<KhachHang, String> colSoDienThoai;

    @FXML
    private TableColumn<KhachHang, String> colSoDonHang;

    @FXML
    private TableColumn<KhachHang, String> colTongChiTieu;

    @FXML
    private TableColumn<KhachHang, String> colDonHangGanNhat;

    @FXML
    private TableColumn<KhachHang, String> colLoaiKhachHang;

    private ObservableList<KhachHang> dsKhachHang;
    private DecimalFormat formatter = new DecimalFormat("#,###");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadStatistics();
        loadKhachHangData();
        setupSearchFunction();
        setupTableRowSelection();
        setupEditButton();
    }

    private void setupTableColumns() {
        // Thiết lập các cột của bảng
        colMaKH.setCellValueFactory(new PropertyValueFactory<>("maKH"));
        colTenKH.setCellValueFactory(new PropertyValueFactory<>("tenKH"));
        colSoDienThoai.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));

        // Cột số đơn hàng
        colSoDonHang.setCellValueFactory(cellData ->
            new SimpleStringProperty(String.valueOf(cellData.getValue().getSoDonHang())));

        // Cột tổng chi tiêu với định dạng tiền tệ
        colTongChiTieu.setCellValueFactory(cellData ->
            new SimpleStringProperty(formatter.format(cellData.getValue().getTongChiTieu()) + "đ"));

        // Cột đơn hàng gần nhất
        colDonHangGanNhat.setCellValueFactory(cellData -> {
            KhachHang kh = cellData.getValue();
            if (kh.getNgayMuaGanNhat() != null) {
                return new SimpleStringProperty(kh.getNgayMuaGanNhat().format(dateFormatter));
            } else {
                return new SimpleStringProperty("Chưa có");
            }
        });

        // Cột loại khách hàng
        colLoaiKhachHang.setCellValueFactory(cellData -> {
            double tongChiTieu = cellData.getValue().getTongChiTieu();
            String loai = tongChiTieu >= 1000000 ? "VIP" : "Thường";
            return new SimpleStringProperty(loai);
        });
    }

    private void loadStatistics() {
        // Load thống kê
        int tongKhachHang = KhachHang_DAO.getTongKhachHang();
        int khachHangVIP = KhachHang_DAO.getTongKhachHangVIP();
        int tongDonHang = KhachHang_DAO.getTongDonHang();
        double tongDoanhThu = KhachHang_DAO.getTongDoanhThu();

        textCustomerQuantity.setText(String.valueOf(tongKhachHang));
        textVipCustomer.setText(String.valueOf(khachHangVIP));
        textOrderQuantity.setText(String.valueOf(tongDonHang));
        textRevenueQuantity.setText(formatter.format(tongDoanhThu) + "đ");
    }

    private void loadKhachHangData() {
        // Load dữ liệu khách hàng vào bảng
        List<KhachHang> listKH = KhachHang_DAO.loadKhachHangFromDB();
        dsKhachHang = FXCollections.observableArrayList(listKH);
        tableViewKhachHang.setItems(dsKhachHang);
    }

    private void setupSearchFunction() {
        // Thiết lập chức năng tìm kiếm theo thời gian thực
        txtSearchEmployee.textProperty().addListener((observable, oldValue, newValue) -> {
            String keyword = newValue.trim();
            if (keyword.isEmpty()) {
                loadKhachHangData(); // Load lại tất cả dữ liệu khi trống
            } else {
                searchKhachHang(keyword); // Tìm kiếm ngay khi gõ
            }
        });

        // Thiết lập chức năng tìm kiếm khi click nút
        btnSearchEmployee.setOnAction(event -> {
            String keyword = txtSearchEmployee.getText().trim();
            if (keyword.isEmpty()) {
                loadKhachHangData(); // Load lại tất cả dữ liệu
            } else {
                searchKhachHang(keyword);
            }
        });

        // Tìm kiếm khi nhấn Enter (giữ lại để người dùng quen thuộc)
        txtSearchEmployee.setOnAction(event -> {
            String keyword = txtSearchEmployee.getText().trim();
            if (keyword.isEmpty()) {
                loadKhachHangData();
            } else {
                searchKhachHang(keyword);
            }
        });
    }

    private void searchKhachHang(String keyword) {
        List<KhachHang> ketQuaTimKiem = KhachHang_DAO.searchKhachHangByName(keyword);
        dsKhachHang = FXCollections.observableArrayList(ketQuaTimKiem);
        tableViewKhachHang.setItems(dsKhachHang);
    }

    private void setupTableRowSelection() {
        // Thiết lập sự kiện click vào row để hiển thị chi tiết
        tableViewKhachHang.setRowFactory(tv -> {
            TableRow<KhachHang> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    KhachHang selectedKH = row.getItem();
                    showKhachHangDetail(selectedKH);
                }
            });
            return row;
        });

        // Hoặc sử dụng selection listener để hiển thị thông tin khi click 1 lần
        tableViewKhachHang.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    showKhachHangInfo(newSelection);
                }
            });
    }

    private void setupEditButton() {
        btnEditCustomer.setOnAction(event -> {
            KhachHang selectedKH = tableViewKhachHang.getSelectionModel().getSelectedItem();
            if (selectedKH == null) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn khách hàng cần sửa!");
                return;
            }
            showEditCustomerDialog(selectedKH);
        });
    }

    private void showEditCustomerDialog(KhachHang khachHang) {
        try {
            // Load FXML dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/antam/app/views/user/dialog/edit_customer.fxml"));
            DialogPane dialogPane = loader.load();

            // Lấy controller và set dữ liệu khách hàng
            SuaKhachHangController controller = loader.getController();
            controller.setKhachHang(khachHang);

            // Tạo và hiển thị dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setResizable(false);

            // Hiển thị dialog và đợi kết quả
            dialog.showAndWait();

            // Nếu cập nhật thành công, reload dữ liệu
            if (controller.isUpdated()) {
                // Refresh toàn bộ bảng từ database
                loadKhachHangData();
                loadStatistics();

                // Force refresh TableView
                tableViewKhachHang.refresh();
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở dialog sửa thông tin khách hàng: " + e.getMessage());
        }
    }

    private void showKhachHangDetail(KhachHang khachHang) {
        try {
            // Load FXML dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/antam/app/views/user/dialog/view_customer.fxml"));
            DialogPane dialogPane = loader.load();

            // Lấy controller và set dữ liệu khách hàng
            XemKhachHangController controller = loader.getController();
            controller.setKhachHang(khachHang);

            // Tạo và hiển thị dialog
            Dialog<Void> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setResizable(true);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

            // Hiển thị dialog
            dialog.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            // Hiển thị lỗi nếu cần
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Không thể mở chi tiết khách hàng");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void showKhachHangInfo(KhachHang khachHang) {
        // Có thể hiển thị thông tin khách hàng ở panel bên cạnh hoặc dưới bảng
        // Hiện tại chỉ in ra console để test
        System.out.println("Đã chọn khách hàng: " + khachHang.getTenKH() +
                          " - Tổng chi tiêu: " + formatter.format(khachHang.getTongChiTieu()) + "đ");
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
