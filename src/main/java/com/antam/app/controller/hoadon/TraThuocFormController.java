//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.hoadon;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.*;
import com.antam.app.entity.ChiTietHoaDon;
import com.antam.app.entity.ChiTietThuoc;
import com.antam.app.entity.HoaDon;
import com.antam.app.entity.Thuoc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class TraThuocFormController {
    @FXML
    private DialogPane dialogPane;
    @FXML
    private VBox vbListChiTietHoaDon;
    @FXML
    private TextField txtMaHoaDonTra, txtKhachHangTra;
    @FXML
    private Text txtTongTienTra;
    @FXML
    private ComboBox<String> cbLyDoTra;


    private Thuoc_DAO thuoc_dao = new Thuoc_DAO();
    private HoaDon_DAO hoaDon_dao = new HoaDon_DAO();
    private KhachHang_DAO khachHang_dao = new KhachHang_DAO();
    private ChiTietHoaDon_DAO chiTietHoaDon_dao = new ChiTietHoaDon_DAO();
    private ChiTietThuoc_DAO chiTietThuoc_dao = new ChiTietThuoc_DAO();
    private KhuyenMai_DAO khuyenMai_dao = new KhuyenMai_DAO();
    private HoaDon hoaDon;
    private ArrayList<ChiTietHoaDon> selectedItems = new ArrayList<>();

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public HoaDon getHoaDOn() {
        return hoaDon;
    }

    // Hiển thị thông tin hóa đơn và chi tiết hóa đơn
    public void showData(HoaDon hoaDon) {
        HoaDon hd = hoaDon_dao.getHoaDonTheoMa(hoaDon.getMaHD());
        ArrayList<ChiTietHoaDon> chiTietHoaDons = chiTietHoaDon_dao.getAllChiTietHoaDonTheoMaHD(hoaDon.getMaHD());
        txtMaHoaDonTra.setText(hoaDon.getMaHD());
        txtKhachHangTra.setText(khachHang_dao.getKhachHangTheoMa(hd.getMaKH().getMaKH()).getTenKH());
        for (ChiTietHoaDon ct : chiTietHoaDons) {
            HBox hBox = renderChiTietHoaDon(ct);
            vbListChiTietHoaDon.getChildren().add(hBox);
        }
        txtTongTienTra.setText("0.0 đ");
    }

    public void initialize() {
        // Thêm nút Huỷ và Xác nhận trả thuốc
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Xác nhận trả thuốc", ButtonData.APPLY);
        this.dialogPane.getButtonTypes().add(cancelButton);
        this.dialogPane.getButtonTypes().add(applyButton);

        // Kết nối DB
        try { Connection con = ConnectDB.getInstance().connect(); }
        catch (SQLException e) { throw new RuntimeException(e); }
        // Xử lý sự kiện khi nhấn nút Xác nhận trả thuốc
        Button applyBtn = (Button) dialogPane.lookupButton(applyButton);
        applyBtn.addEventFilter(ActionEvent.ACTION, event -> {
            if (selectedItems.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Chưa chọn thuốc để trả");
                alert.setContentText("Vui lòng chọn ít nhất một thuốc để trả.");
                alert.showAndWait();
                event.consume();
            } else {
                try {
                    Connection con = ConnectDB.getInstance().connect();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                String lyDoTra = cbLyDoTra.getValue();
                if (lyDoTra == null || lyDoTra.trim().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Cảnh báo");
                    alert.setHeaderText("Chưa chọn lý do trả thuốc");
                    alert.setContentText("Vui lòng chọn lý do trả trước khi xác nhận.");
                    alert.showAndWait();
                    event.consume();
                    return;
                }

                for (ChiTietHoaDon ct : selectedItems) {
                    chiTietHoaDon_dao.xoaMemChiTietHoaDon(ct.getMaHD().getMaHD(), ct.getMaCTT().getMaCTT(), "Trả");

                    switch (lyDoTra) {
                        // Các lý do KHÔNG cộng lại vào kho
                        case "Hết hạn sử dụng":
                        case "Bao bì bị hư hỏng":
                        case "Thuốc lỗi / hư hỏng":
                        case "Thuốc bị thu hồi":
                            // Không làm gì cả
                            break;

                        // Các lý do CÓ cộng lại vào kho
                        case "Khách hàng đổi ý":
                        case "Nhập nhầm lô / dư":
                        case "Sai thông tin đơn / bảo hiểm":
                            Thuoc t = thuoc_dao.getThuocTheoMa(
                                    chiTietThuoc_dao
                                            .getChiTietThuoc(ct.getMaCTT().getMaCTT())
                                            .getMaThuoc()
                                            .getMaThuoc()
                            );
                            chiTietThuoc_dao.CapNhatSoLuongChiTietThuoc(
                                    ct.getMaCTT().getMaCTT(),
                                     ct.getSoLuong()
                            );
                            break;

                        default:
                            // Nếu có giá trị không nằm trong danh sách trên
                            System.out.println("Lý do trả không hợp lệ: " + lyDoTra);
                            break;
                    }

                }
                if (chiTietHoaDon_dao.getAllChiTietHoaDonTheoMaHDConBan(hoaDon.getMaHD()).isEmpty()) {
                    hoaDon_dao.xoaMemHoaDon(hoaDon.getMaHD());
                    hoaDon_dao.CapNhatTongTienHoaDon(hoaDon.getMaHD(), 0);
                } else {
                    double tongTienCu = hoaDon.getTongTien();
                    double tongTienTra = 0;
                    double tongTienCoKM = 0;
                    for (ChiTietHoaDon ct : selectedItems) {
                        if (ct.getTinhTrang().equals("Thuốc Mới Khi Đổi")){
                            tongTienTra += ct.getThanhTien();
                        } else {
                            tongTienCoKM += ct.getThanhTien();
                        }
                    }
                    if (khuyenMai_dao.getKhuyenMaiTheoMa(hoaDon.getMaKM().getMaKM()) != null) {
                        tongTienCoKM = TinhTienKhuyenMai(tongTienCoKM, khuyenMai_dao.getKhuyenMaiTheoMa(hoaDon.getMaKM().getMaKM()).getSo());
                    }
                    hoaDon_dao.CapNhatTongTienHoaDon(hoaDon.getMaHD(), tongTienCu - tongTienTra - tongTienCoKM);
                }
            }
        });

        // Thêm giá trị vào combobox lý do trả
        addValueCombobox();
    }
    // Tính tổng tiền trả
    public void tinhTongTienTra(){
        double tongTien = 0;
        double tongTienKhiTra = 0;
        for (ChiTietHoaDon ct : selectedItems){
            if (ct.getTinhTrang().equals("Thuốc Mới Khi Đổi")){
                tongTienKhiTra += ct.getThanhTien();
            } else {
                tongTien += ct.getThanhTien();
            }
        }
        DecimalFormat df = new DecimalFormat("#,### đ");
        if (khuyenMai_dao.getKhuyenMaiTheoMa(hoaDon.getMaKM().getMaKM()) != null){
            tongTien = TinhTienKhuyenMai(tongTien, khuyenMai_dao.getKhuyenMaiTheoMa(hoaDon.getMaKM().getMaKM()).getSo());
            txtTongTienTra.setText(df.format(tongTien + tongTienKhiTra) + " (Có áp dụng KM)");
        }else {
            txtTongTienTra.setText(df.format(tongTien + tongTienKhiTra));
        }
    }
    // Thêm giá trị vào combobox lý do trả
    public void addValueCombobox(){
        ObservableList<String> lyDoList = FXCollections.observableArrayList(
                "Hết hạn sử dụng",
                "Bao bì bị hư hỏng",
                "Khách hàng đổi ý",
                "Thuốc lỗi / hư hỏng",
                "Nhập nhầm lô / dư",
                "Thuốc bị thu hồi",
                "Sai thông tin đơn / bảo hiểm"
        );
        cbLyDoTra.setItems(lyDoList);
    }
    // Render chi tiết hóa đơn
    public HBox renderChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
        if (chiTietHoaDon.getTinhTrang() == null) {
            HBox h = new HBox();
            h.setVisible(false);
            h.setManaged(false);
            return h;
        }
        if (chiTietHoaDon.getTinhTrang().equals("Trả") || chiTietHoaDon.getTinhTrang().equals("Trả Khi Đổi")) {
            HBox h = new HBox();
            h.setVisible(false);
            h.setManaged(false);
            return h;
        }
        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setStyle(
                "-fx-background-color: #f8fafc;" +
                "-fx-padding: 10;"
        );

        CheckBox checkBox = new CheckBox();
        checkBox.setOnAction(event -> {
            if (checkBox.isSelected()) {
                if (!selectedItems.contains(chiTietHoaDon)) {
                    selectedItems.add(chiTietHoaDon);
                }
            } else {
                selectedItems.remove(chiTietHoaDon);
            }
            tinhTongTienTra();
        });
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ChiTietThuoc ctt = chiTietThuoc_dao.getChiTietThuoc(chiTietHoaDon.getMaCTT().getMaCTT());
        Thuoc t = thuoc_dao.getThuocTheoMa(ctt.getMaThuoc().getMaThuoc());
        Text txtMaThuoc = new Text(t.getTenThuoc());
        txtMaThuoc.setStyle("-fx-font-size: 15px;");
        Text txtSoLuong = new Text("SL " + chiTietHoaDon.getSoLuong());
        txtSoLuong.setStyle("-fx-font-size: 15px;");
        DecimalFormat df = new DecimalFormat("#,### đ");
        Text txtDonGia = new Text(df.format(chiTietHoaDon.getThanhTien()));
        txtDonGia.setStyle("-fx-font-size: 15px;");
        String valueBtn = "Bình thường";
        if (chiTietHoaDon.getTinhTrang().equals("Thuốc Mới Khi Đổi")){
            valueBtn = "Thuốc đổi";
        }
        Button btn = new Button(valueBtn);
        btn.setStyle(
                "-fx-background-color: #e0f2fe;" +
                "-fx-text-fill: #0369a1;" +
                "-fx-background-radius: 10;" +
                "-fx-font-size: 12px;"
        );

        hBox.getChildren().addAll(checkBox, txtMaThuoc, txtSoLuong, txtDonGia, btn);
        return hBox;
    }

    public double TinhTienKhuyenMai(double tongTien, double giaSo){
        double giam = 0;
        if (giaSo < 100) {
            giam = tongTien * giaSo / 100.0;
        } else if (giaSo >= 1000) {
            giam = giaSo;
        }
        if (giam > tongTien) giam = tongTien;
        tongTien -= giam;
        return tongTien;
    }

}
