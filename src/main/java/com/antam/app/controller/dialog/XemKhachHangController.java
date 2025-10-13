package com.antam.app.controller.dialog;

import com.antam.app.dao.ChiTietHoaDon_DAO;
import com.antam.app.dao.HoaDon_DAO;
import com.antam.app.entity.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Insets;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class XemKhachHangController implements Initializable {

    @FXML
    private DialogPane dialogPane;

    @FXML
    private Text txtTenKhachHang;

    @FXML
    private Text txtMaKhachHang;

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
    private TableColumn<HoaDon, String> colSoThuoc;

    @FXML
    private TableColumn<HoaDon, String> colTongTien;

    @FXML
    private TableColumn<HoaDon, String> colNhanVien;

    @FXML
    private FlowPane flowPaneThuocDaMua;

    private KhachHang khachHang;
    private final DecimalFormat formatter = new DecimalFormat("#,###");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
        loadKhachHangInfo();
        loadLichSuMuaHang();
        loadThuocDaMua();
    }

    private void setupTableColumns() {
        colMaHoaDon.setCellValueFactory(new PropertyValueFactory<>("maHD"));

        colNgayMua.setCellValueFactory(cellData -> {
            HoaDon hd = cellData.getValue();
            if (hd.getNgayTao() != null) {
                return new SimpleStringProperty(hd.getNgayTao().format(dateFormatter));
            }
            return new SimpleStringProperty("");
        });

        colSoThuoc.setCellValueFactory(cellData -> {
            List<ChiTietHoaDon> chiTietList = ChiTietHoaDon_DAO.getChiTietByMaHD(cellData.getValue().getMaHD());
            return new SimpleStringProperty(String.valueOf(chiTietList.size()));
        });

        colTongTien.setCellValueFactory(cellData ->
            new SimpleStringProperty(formatter.format(cellData.getValue().getTongTien()) + "đ"));

        colNhanVien.setCellValueFactory(cellData -> {
            HoaDon hd = cellData.getValue();
            if (hd.getMaNV() != null && hd.getMaNV().getHoTen() != null) {
                return new SimpleStringProperty(hd.getMaNV().getHoTen());
            }
            return new SimpleStringProperty("Không xác định");
        });
    }

    private void loadKhachHangInfo() {
        if (khachHang != null) {
            txtMaKhachHang.setText("Mã KH: " + khachHang.getMaKH());
            txtTenKhachHang.setText(khachHang.getTenKH());
            txtSoDienThoai.setText("SĐT: " + khachHang.getSoDienThoai());
            txtTongDonHang.setText("Tổng đơn hàng: " + khachHang.getSoDonHang());
            txtTongChiTieu.setText("Tổng chi tiêu: " + formatter.format(khachHang.getTongChiTieu()) + " ₫");

            if (khachHang.getNgayMuaGanNhat() != null) {
                txtDonHangGanNhat.setText("Đơn hàng gần nhất: " + khachHang.getNgayMuaGanNhat().format(dateFormatter));
            } else {
                txtDonHangGanNhat.setText("Đơn hàng gần nhất: Chưa có");
            }
        }
    }

    private void loadLichSuMuaHang() {
        if (khachHang != null) {
            List<HoaDon> danhSachHoaDon = HoaDon_DAO.getHoaDonByMaKH(khachHang.getMaKH());
            ObservableList<HoaDon> hoaDonList = FXCollections.observableArrayList(danhSachHoaDon);
            tableLichSuMuaHang.setItems(hoaDonList);
        }
    }

    private void loadThuocDaMua() {
        if (khachHang != null) {
            List<HoaDon> danhSachHoaDon = HoaDon_DAO.getHoaDonByMaKH(khachHang.getMaKH());
            flowPaneThuocDaMua.getChildren().clear();

            if (danhSachHoaDon.isEmpty()) {
                Text noDataText = new Text("Khách hàng chưa có đơn hàng nào");
                noDataText.setStyle("-fx-fill: #666666; -fx-font-style: italic;");
                VBox noDataBox = new VBox(noDataText);
                noDataBox.setPadding(new Insets(20));
                noDataBox.setStyle("-fx-alignment: center;");
                flowPaneThuocDaMua.getChildren().add(noDataBox);
                return;
            }

            try {
                for (HoaDon hd : danhSachHoaDon) {
                    try {
                        List<ChiTietHoaDon> chiTietList = ChiTietHoaDon_DAO.getChiTietByMaHD(hd.getMaHD());
                        for (ChiTietHoaDon ct : chiTietList) {
                            if (ct != null && ct.getMaCTT() != null && ct.getMaCTT().getMaThuoc() != null) {
                                VBox thuocBox = createThuocCard(ct, hd);
                                flowPaneThuocDaMua.getChildren().add(thuocBox);
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Lỗi khi lấy chi tiết hóa đơn " + hd.getMaHD() + ": " + e.getMessage());
                    }
                }

                if (flowPaneThuocDaMua.getChildren().isEmpty()) {
                    Text noMedicineText = new Text("Không có thông tin thuốc");
                    noMedicineText.setStyle("-fx-fill: #666666; -fx-font-style: italic;");
                    VBox noMedicineBox = new VBox(noMedicineText);
                    noMedicineBox.setPadding(new Insets(20));
                    noMedicineBox.setStyle("-fx-alignment: center;");
                    flowPaneThuocDaMua.getChildren().add(noMedicineBox);
                }

            } catch (Exception e) {
                System.err.println("Lỗi khi load thuốc đã mua: " + e.getMessage());
                Text errorText = new Text("Có lỗi khi tải thông tin thuốc");
                errorText.setStyle("-fx-fill: #dc2626; -fx-font-style: italic;");
                VBox errorBox = new VBox(errorText);
                errorBox.setPadding(new Insets(20));
                errorBox.setStyle("-fx-alignment: center;");
                flowPaneThuocDaMua.getChildren().add(errorBox);
            }
        }
    }

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

        Text txtThongTinMua = new Text("SL: " + chiTiet.getSoLuong() + " " + donViTinh + " | Thành tiền: " + formatter.format(chiTiet.getThanhTien()) + " ₫");
        txtThongTinMua.setStyle("-fx-fill: #059669; -fx-font-weight: bold; -fx-font-size: 12px;");

        String ngayMua = hoaDon.getNgayTao() != null ? hoaDon.getNgayTao().format(dateFormatter) : "Không xác định";
        Text txtThongTinHoaDon = new Text("HĐ: " + hoaDon.getMaHD() + " | Ngày: " + ngayMua);
        txtThongTinHoaDon.setStyle("-fx-fill: #6b7280; -fx-font-size: 11px;");

        vbox.getChildren().addAll(txtTenThuoc, txtLoaiThuoc, txtThongTinMua, txtThongTinHoaDon);
        return vbox;
    }
}
