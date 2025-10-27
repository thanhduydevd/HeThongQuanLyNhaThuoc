package com.antam.app.controller.hoadon;

import com.antam.app.dao.ChiTietHoaDon_DAO;
import com.antam.app.dao.ChiTietThuoc_DAO;
import com.antam.app.dao.DonViTinh_DAO;
import com.antam.app.entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class XemChiTietHoaDonFormController {
    @FXML
    private DialogPane dialogPane;
    @FXML
    private Text txtInvoiceId;
    @FXML
    private Text txtDate;
    @FXML
    private Text txtCustomer;
    @FXML
    private Text txtEmployee;
    @FXML
    private Text txtSubTotal;
    @FXML
    private Text txtVAT;
    @FXML
    private Text txtTotal;
    @FXML
    private Text txtPromotion;
    @FXML
    private Text txtReturnTotal;
    @FXML
    private TableView<ChiTietHoaDon> tableListsThuoc;
    @FXML
    private TableColumn<ChiTietHoaDon, Integer> sttCol;
    @FXML
    private TableColumn<ChiTietHoaDon, String> tenThuocCol;
    @FXML
    private TableColumn<ChiTietHoaDon, Integer> soLuongCol;
    @FXML
    private TableColumn<ChiTietHoaDon, Double> donGiaCol;
    @FXML
    private TableColumn<ChiTietHoaDon, Double> thanhTienCol;
    @FXML
    private TableColumn<ChiTietHoaDon, String> trangThaiCol;
    @FXML
    private TableColumn<ChiTietHoaDon, String> dvtCol;
    @FXML
    private TableColumn<ChiTietHoaDon, Double> thueCol;

    // Định dạng tiền tệ kiểu Việt Nam: 1.000đ, 10.000đ
    private static final DecimalFormat VND_FORMAT;
    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        VND_FORMAT = new DecimalFormat("#,##0", symbols);
    }

    @FXML
    public void initialize() {
        tenThuocCol.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getMaCTT().getMaThuoc().getTenThuoc()
            )
        );
        soLuongCol.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        donGiaCol.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleDoubleProperty(
                cellData.getValue().getMaCTT().getMaThuoc().getGiaBan()
            ).asObject()
        );
        donGiaCol.setCellFactory(col -> new javafx.scene.control.TableCell<ChiTietHoaDon, Double>() {
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
        thanhTienCol.setCellValueFactory(cellData -> {
            double donGia = 0;
            int soLuong = cellData.getValue().getSoLuong();
            if (cellData.getValue().getMaCTT() != null && cellData.getValue().getMaCTT().getMaThuoc() != null) {
                donGia = cellData.getValue().getMaCTT().getMaThuoc().getGiaBan();
            }
            return new javafx.beans.property.SimpleDoubleProperty(donGia * soLuong).asObject();
        });
        thanhTienCol.setCellFactory(col -> new javafx.scene.control.TableCell<ChiTietHoaDon, Double>() {
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
        trangThaiCol.setCellValueFactory(new PropertyValueFactory<>("tinhTrang"));
        dvtCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
            cellData.getValue().getMaDVT().getTenDVT()));
        // Thuế GTGT
        thueCol.setCellValueFactory(cellData -> {
            double thue = 0;
            if (cellData.getValue().getMaCTT() != null && cellData.getValue().getMaCTT().getMaThuoc() != null) {
                thue = cellData.getValue().getMaCTT().getMaThuoc().getThue();
            }
            return new javafx.beans.property.SimpleDoubleProperty(thue).asObject();
        });
        thueCol.setCellFactory(col -> new javafx.scene.control.TableCell<ChiTietHoaDon, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.0f%%", item * 100));
                }
            }
        });
        // STT custom
        sttCol.setCellFactory(col -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1));
                }
            }
        });
    }

    public void setInvoice(HoaDon hoaDon) {
        if (hoaDon == null) return;
        txtInvoiceId.setText("Hóa đơn: " + hoaDon.getMaHD());
        txtDate.setText("Ngày: " + hoaDon.getNgayTao().toString());
        txtCustomer.setText("Khách hàng: " + hoaDon.getMaKH().getTenKH());
        txtEmployee.setText("Nhân viên: " + hoaDon.getMaNV().getHoTen());
        // Lấy danh sách chi tiết hóa đơn từ DAO
        ChiTietHoaDon_DAO cthdDAO = new ChiTietHoaDon_DAO();
        ChiTietThuoc_DAO chiTietThuoc_dao = new ChiTietThuoc_DAO();
        com.antam.app.dao.Thuoc_DAO thuoc_dao = new com.antam.app.dao.Thuoc_DAO();
        DonViTinh_DAO donViTinh_dao = new DonViTinh_DAO();
        java.util.List<ChiTietHoaDon> list = cthdDAO.getAllChiTietHoaDonTheoMaHD(hoaDon.getMaHD());
        double subTotal = 0;
        double returnTotal = 0;
        double soldItemsTotal = 0; // Tổng tiền của hàng bán
        double vatTotal = 0; // Tổng thuế VAT của hàng bán
        for (ChiTietHoaDon chiTietHoaDon : list) {
            ChiTietThuoc ctt = chiTietThuoc_dao.getChiTietThuoc(chiTietHoaDon.getMaCTT().getMaCTT());
            if (ctt != null) {
                Thuoc t = thuoc_dao.getThuocTheoMa(ctt.getMaThuoc().getMaThuoc());
                if (t != null) {
                    ctt.setMaThuoc(t);
                }
                chiTietHoaDon.setMaCTT(ctt);
            }
            // Lấy lại thông tin đơn vị tính
            DonViTinh dvt = donViTinh_dao.getDVTTheoMa(chiTietHoaDon.getMaDVT().getMaDVT());
            if (dvt != null) {
                chiTietHoaDon.setMaDVT(dvt);
            }
            subTotal += chiTietHoaDon.getThanhTien();

            // Tính tiền hàng trả
            if ("Trả".equalsIgnoreCase(chiTietHoaDon.getTinhTrang()) || "Đổi".equalsIgnoreCase(chiTietHoaDon.getTinhTrang())) {
                returnTotal += chiTietHoaDon.getThanhTien();
            } else {
                // Chỉ tính thuế VAT cho hàng bán (không phải trả hoặc đổi)
                soldItemsTotal += chiTietHoaDon.getThanhTien();
                if (chiTietHoaDon.getMaCTT() != null && chiTietHoaDon.getMaCTT().getMaThuoc() != null) {
                    double thue = chiTietHoaDon.getMaCTT().getMaThuoc().getThue(); // 0.1 = 10%
                    double thanhTien = chiTietHoaDon.getThanhTien();
                    // Tính thuế VAT: nếu giá chưa bao gồm thuế thì nhân trực tiếp
                    vatTotal += thanhTien * thue;
                }
            }
        }
        ObservableList<ChiTietHoaDon> data = FXCollections.observableArrayList(list);
        tableListsThuoc.setItems(data);
        // Tính toán các giá trị tiền
        double tongTien = hoaDon.getTongTien();
        txtTotal.setText(VND_FORMAT.format(tongTien) + "đ");
        txtVAT.setText(VND_FORMAT.format(vatTotal) + "đ");
        txtSubTotal.setText(VND_FORMAT.format(subTotal) + "đ");
        txtReturnTotal.setText(VND_FORMAT.format(returnTotal) + "đ");
        txtPromotion.setText(hoaDon.getMaKM() != null ? hoaDon.getMaKM().getTenKM() : "Không có khuyến mãi");
    }
}
