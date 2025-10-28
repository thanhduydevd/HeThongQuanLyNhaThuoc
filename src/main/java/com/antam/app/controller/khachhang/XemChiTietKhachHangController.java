/*
 * @ (#) XemKhachHangController.java   1.0 28/10/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.antam.app.controller.khachhang;

import com.antam.app.dao.ChiTietHoaDon_DAO;
import com.antam.app.dao.HoaDon_DAO;
import com.antam.app.entity.ChiTietHoaDon;
import com.antam.app.entity.ChiTietThuoc;
import com.antam.app.entity.DangDieuChe;
import com.antam.app.entity.DonViTinh;
import com.antam.app.entity.HoaDon;
import com.antam.app.entity.KhachHang;
import com.antam.app.entity.Thuoc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/*
 * @description Controller for viewing customer details
 * @author: Tran Tuan Hung
 * @date: 28/10/25
 * @version: 1.0
 */
public class XemChiTietKhachHangController implements Initializable {

    @FXML
    private Text txtMaKhachHang;

    @FXML
    private Text txtTenKhachHang;

    @FXML
    private Text txtSoDienThoai;

    @FXML
    private Text txtTongDonHang;

    @FXML
    private Text txtTongChiTieu;

    @FXML
    private Text txtDonHangGanNhat;

    @FXML
    private TableView<HoaDon> tableLichSuMuaHang;

    @FXML
    private TableColumn<HoaDon, String> colMaHoaDon;

    @FXML
    private TableColumn<HoaDon, String> colNgayMua;

    @FXML
    private TableColumn<HoaDon, Integer> colSoThuoc;

    @FXML
    private TableColumn<HoaDon, Double> colTongTien;

    @FXML
    private TableColumn<HoaDon, String> colNhanVien;

    @FXML
    private Button btnDong;

    @FXML
    private javafx.scene.layout.FlowPane flowPaneThuocDaMua;

    private KhachHang khachHang;
    private HoaDon_DAO hoaDonDAO;
    private ChiTietHoaDon_DAO chiTietHoaDonDAO;
    private DateTimeFormatter formatter;
    private DateTimeFormatter dateFormatter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Khởi tạo các DAO
        hoaDonDAO = new HoaDon_DAO();
        chiTietHoaDonDAO = new ChiTietHoaDon_DAO();
        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Thiết lập các cột bảng
        setupTableColumns();
    }

    /**
     * Thiết lập dữ liệu cho controller (gọi từ TimKhachHangController)
     */
    public void setKhachHang(KhachHang kh) {
        this.khachHang = kh;
        loadKhachHangInfo();
        loadLichSuMuaHang();
        loadThuocDaMua();
    }

    /**
     * Thiết lập các cột của bảng
     */
    private void setupTableColumns() {
        colMaHoaDon.setCellValueFactory(new PropertyValueFactory<>("maHD"));
        colNgayMua.setCellValueFactory(cellData -> {
            HoaDon hd = cellData.getValue();
            String dateStr = hd.getNgayTao() != null
                ? hd.getNgayTao().format(formatter)
                : "N/A";
            return new javafx.beans.property.SimpleStringProperty(dateStr);
        });
        colSoThuoc.setCellValueFactory(cellData -> {
            HoaDon hd = cellData.getValue();
            // Lấy số lượng chi tiết hóa đơn
            ArrayList<ChiTietHoaDon> chiTietList = chiTietHoaDonDAO.getAllChiTietHoaDonTheoMaHD(hd.getMaHD());
            return new javafx.beans.property.SimpleObjectProperty<>(chiTietList.size());
        });
        colTongTien.setCellValueFactory(new PropertyValueFactory<>("tongTien"));
        colTongTien.setCellFactory(column -> new javafx.scene.control.TableCell<HoaDon, Double>() {
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
        colNhanVien.setCellValueFactory(cellData -> {
            HoaDon hd = cellData.getValue();
            String tenNV = hd.getMaNV().getHoTen() != null ? hd.getMaNV().getHoTen() : "N/A";
            return new javafx.beans.property.SimpleStringProperty(tenNV);
        });
    }

    /**
     * Tải thông tin khách hàng
     */
    private void loadKhachHangInfo() {
        if (khachHang == null) return;

        txtMaKhachHang.setText("Mã KH: " + khachHang.getMaKH());
        txtTenKhachHang.setText(khachHang.getTenKH());
        txtSoDienThoai.setText("SĐT: " + khachHang.getSoDienThoai());
        txtTongDonHang.setText("Tổng đơn hàng: " + khachHang.getSoDonHang());
        txtTongChiTieu.setText("Tổng chi tiêu: " + String.format("%,.0f VNĐ", khachHang.getTongChiTieu()));

        String ngayGanNhat = khachHang.getNgayMuaGanNhat() != null
            ? khachHang.getNgayMuaGanNhat().format(formatter)
            : "N/A";
        txtDonHangGanNhat.setText("Đơn hàng gần nhất: " + ngayGanNhat);
    }

    /**
     * Tải lịch sử mua hàng của khách hàng
     */
    private void loadLichSuMuaHang() {
        if (khachHang == null) return;

        try {
            // Lấy tất cả hóa đơn từ database
            ArrayList<HoaDon> allHoaDon = hoaDonDAO.getAllHoaDon();

            // Lọc các hóa đơn của khách hàng này
            List<HoaDon> hoaDonCuaKH = allHoaDon.stream()
                .filter(hd -> hd.getMaKH().getMaKH().equals(khachHang.getMaKH()))
                .collect(Collectors.toList());

            // Chuyển đổi sang ObservableList
            ObservableList<HoaDon> dsHoaDon = FXCollections.observableArrayList(hoaDonCuaKH);

            // Gán dữ liệu cho bảng
            tableLichSuMuaHang.setItems(dsHoaDon);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Xử lý sự kiện click nút Đóng
     */
    @FXML
    private void onBtnDongClick() {
        // Đóng dialog bằng cách tìm window và đóng nó
        btnDong.getScene().getWindow().hide();
    }

    /**
     * Tải và hiển thị các card thuốc đã mua
     */
    private void loadThuocDaMua() {
        if (khachHang == null || flowPaneThuocDaMua == null) return;

        try {
            // Xóa các card cũ
            flowPaneThuocDaMua.getChildren().clear();

            // Lấy tất cả hóa đơn của khách hàng
            ArrayList<HoaDon> allHoaDon = hoaDonDAO.getAllHoaDon();
            List<HoaDon> hoaDonCuaKH = allHoaDon.stream()
                .filter(hd -> hd.getMaKH().getMaKH().equals(khachHang.getMaKH()))
                .collect(Collectors.toList());

            // Duyệt qua từng hóa đơn và lấy chi tiết thuốc
            for (HoaDon hoaDon : hoaDonCuaKH) {
                ArrayList<ChiTietHoaDon> chiTietList = chiTietHoaDonDAO.getAllChiTietHoaDonTheoMaHD(hoaDon.getMaHD());

                // Tạo card cho mỗi chi tiết thuốc
                for (ChiTietHoaDon chiTiet : chiTietList) {
                    VBox thuocCard = createThuocCard(chiTiet, hoaDon);
                    flowPaneThuocDaMua.getChildren().add(thuocCard);
                }
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi tải thuốc đã mua: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Tạo card hiển thị thông tin thuốc đã mua
     */
    private VBox createThuocCard(ChiTietHoaDon chiTiet, HoaDon hoaDon) {
        VBox vbox = new VBox(5);
        vbox.setPrefWidth(280);
        vbox.setStyle("-fx-background-color: white; -fx-border-radius: 6px; -fx-border-color: #10b981; -fx-border-width: 0px 0px 0px 4px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        vbox.setPadding(new Insets(12));

        // Khởi tạo thông tin mặc định
        String tenThuoc = "Thuốc không xác định";
        String dangDieuChe = "Không xác định";
        String donViTinh = "Không xác định";
        String hamLuong = "";

        try {
            // Flow: ChiTietHoaDon -> ChiTietThuoc -> Thuoc (để lấy tên thuốc và dạng điều chế)
            ChiTietThuoc chiTietThuoc = chiTiet.getMaCTT();
            System.out.println(chiTietThuoc);
            if (chiTietThuoc != null) {
                Thuoc thuoc = chiTietThuoc.getMaThuoc();
                if (thuoc != null) {
                    // Lấy tên thuốc
                    String ten = thuoc.getTenThuoc();
                    if (ten != null && !ten.trim().isEmpty()) {
                        tenThuoc = ten;
                    }

                    // Lấy hàm lượng
                    String hl = thuoc.getHamLuong();
                    if (hl != null && !hl.trim().isEmpty()) {
                        hamLuong = " (" + hl + ")";
                    }

                    // Lấy dạng điều chế
                    if (thuoc.getDangDieuChe() != null) {
                        String ddc = thuoc.getDangDieuChe().getTenDDC();
                        if (ddc != null && !ddc.trim().isEmpty()) {
                            dangDieuChe = ddc;
                        }
                    }
                }
            }

            // Flow: ChiTietHoaDon -> DonViTinh (để lấy tên đơn vị tính)
            DonViTinh dvt = chiTiet.getMaDVT();
            if (dvt != null) {
                String tenDVT = dvt.getTenDVT();
                if (tenDVT != null && !tenDVT.trim().isEmpty()) {
                    donViTinh = tenDVT;
                }
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi lấy thông tin thuốc: " + e.getMessage());
            e.printStackTrace();
        }

        // Tạo các Text elements với thông tin đầy đủ
        Text txtTenThuoc = new Text(tenThuoc + hamLuong);
        txtTenThuoc.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-fill: black;");

        Text txtLoaiThuoc = new Text("Dạng: " + dangDieuChe);
        txtLoaiThuoc.setStyle("-fx-fill: #6b6b6b; -fx-font-size: 12px;");

        Text txtThongTinMua = new Text("SL: " + chiTiet.getSoLuong() + " " + donViTinh + " | Thành tiền: " + String.format("%,.0f", chiTiet.getThanhTien()) + " ₫");
        txtThongTinMua.setStyle("-fx-fill: #059669; -fx-font-weight: bold; -fx-font-size: 12px;");

        String ngayMua = hoaDon.getNgayTao() != null ? hoaDon.getNgayTao().format(dateFormatter) : "Không xác định";
        Text txtThongTinHoaDon = new Text("HĐ: " + hoaDon.getMaHD() + " | Ngày: " + ngayMua);
        txtThongTinHoaDon.setStyle("-fx-fill: #6b7280; -fx-font-size: 11px;");

        vbox.getChildren().addAll(txtTenThuoc, txtLoaiThuoc, txtThongTinMua, txtThongTinHoaDon);
        return vbox;
    }
}
