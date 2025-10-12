package com.antam.app.controller.dialog;

import com.antam.app.entity.HoaDon;
import javafx.fxml.FXML;
import javafx.scene.control.DialogPane;
import javafx.scene.text.Text;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import com.antam.app.entity.ChiTietHoaDon;
import com.antam.app.dao.ChiTietHoaDon_DAO;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class XemChiTietHoaDonController {
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
        thanhTienCol.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));
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
        txtCustomer.setText("Khách hàng: " + hoaDon.getMaKH().getMaKH());
        txtEmployee.setText("Nhân viên: " + hoaDon.getMaNV().getMaNV());
        // Tính toán các giá trị tiền
        double tongTien = hoaDon.getTongTien();
        double vat = tongTien * 0.1; // Giả sử VAT 10%
        double subTotal = tongTien - vat;
        txtTotal.setText(VND_FORMAT.format(tongTien) + "đ");
        txtVAT.setText(VND_FORMAT.format(vat) + "đ");
        txtSubTotal.setText(VND_FORMAT.format(subTotal) + "đ");
        txtPromotion.setText(hoaDon.getMaKM() != null ? hoaDon.getMaKM().getMaKM() : "Không có");

        // Lấy danh sách chi tiết hóa đơn từ DAO
        ChiTietHoaDon_DAO cthdDAO = new ChiTietHoaDon_DAO();
        ObservableList<ChiTietHoaDon> data = FXCollections.observableArrayList(
            cthdDAO.getAllChiTietHoaDonTheoMaHD(hoaDon.getMaHD())
        );
        tableListsThuoc.setItems(data);
    }
}
