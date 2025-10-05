package com.antam.app.controller.dialog;

import com.antam.app.entity.HoaDon;
import javafx.fxml.FXML;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

public class ViewInvoiceController {
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
    // Add more @FXML fields for other UI elements as needed

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
        txtTotal.setText(String.format("%.0f₫", tongTien));
        txtVAT.setText(String.format("%.0f₫", vat));
        txtSubTotal.setText(String.format("%.0f₫", subTotal));
        txtPromotion.setText(hoaDon.getMaKM() != null ? hoaDon.getMaKM().getMaKM() : "Không có");
        // Populate other fields as needed
    }
}
