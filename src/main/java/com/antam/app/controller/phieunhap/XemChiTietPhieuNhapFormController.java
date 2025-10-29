//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.phieunhap;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.ChiTietPhieuNhap_DAO;
import com.antam.app.dao.PhieuNhap_DAO;
import com.antam.app.entity.ChiTietPhieuNhap;
import com.antam.app.entity.PhieuNhap;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;


public class XemChiTietPhieuNhapFormController {
    @FXML
    private DialogPane dialogPane;

    @FXML
    private Text txtMaPhieuNhap, txtDiaChi, txtLyDo, txtNgayNhap, txtTongTien, txtNhanVien, txtNhaCungCap;

    @FXML
    private TableView<ChiTietPhieuNhap> tbChiTietPhieuNhap;

    private PhieuNhap_DAO phieuNhap_DAO = new PhieuNhap_DAO();
    private ChiTietPhieuNhap_DAO chiTietPhieuNhap_DAO = new ChiTietPhieuNhap_DAO();

    public XemChiTietPhieuNhapFormController() {

    }

    public void initialize() {
        //Kết nối
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        ButtonType applyButton = new ButtonType("Đóng", ButtonData.CANCEL_CLOSE);
        this.dialogPane.getButtonTypes().add(applyButton);
    }

    public void showChiTietPhieuNhap(PhieuNhap phieuNhap) {
        txtMaPhieuNhap.setText("Mã phiếu nhập: "+ phieuNhap.getMaPhieuNhap());
        txtDiaChi.setText("Địa chỉ: " + phieuNhap.getDiaChi());
        txtLyDo.setText("Lý do nhập: " + phieuNhap.getLyDo());
        txtNgayNhap.setText("Ngày nhập: " + String.valueOf(phieuNhap.getNgayNhap()));
        DecimalFormat decimal = new DecimalFormat("#,### đ");
        txtTongTien.setText(decimal.format(phieuNhap.getTongTien()));
        txtNhanVien.setText("Nhân viên nhập: " + phieuNhap.getMaNV().getHoTen());
        txtNhaCungCap.setText("Nhà cung cấp: " + phieuNhap.getNhaCungCap());

        //Load chi tiết phiếu nhập
        ArrayList<ChiTietPhieuNhap> dsChiTietPhieuNhap = chiTietPhieuNhap_DAO.getDanhSachChiTietPhieuNhapTheoMaPN(phieuNhap.getMaPhieuNhap());
        ObservableList<ChiTietPhieuNhap> data = FXCollections.observableArrayList();
        data.setAll(dsChiTietPhieuNhap);
        tbChiTietPhieuNhap.setItems(data);
        TableColumn<ChiTietPhieuNhap, Number> colSoThuTu = new TableColumn<>("STT");
        colSoThuTu.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(tbChiTietPhieuNhap.getItems().indexOf(cellData.getValue()) + 1)
        );

        TableColumn<ChiTietPhieuNhap, String> colMaThuoc = new TableColumn<>("Mã thuốc");
        colMaThuoc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSoDangKy().getMaThuoc()));

        TableColumn<ChiTietPhieuNhap, String> colDonViTinh = new TableColumn<>("Đơn Vị Tính");
        colDonViTinh.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaDVT().getTenDVT()));

        TableColumn<ChiTietPhieuNhap, Number> colSoLuongNhap = new TableColumn<>("Số Lượng Nhập");
        colSoLuongNhap.setCellValueFactory(new PropertyValueFactory<>("soLuong"));

        TableColumn<ChiTietPhieuNhap, Double> colGiaNhap = new TableColumn<>("Giá Nhập");
        colGiaNhap.setCellValueFactory(new PropertyValueFactory<>("giaNhap"));
        colGiaNhap.setCellFactory(tc -> new TableCell<ChiTietPhieuNhap, Double>() {
            @Override
            protected void updateItem(Double giaNhap, boolean empty) {
                super.updateItem(giaNhap, empty);
                if (empty) {
                    setText(null);
                } else {
                    DecimalFormat df = new DecimalFormat("#,###");
                    setText(df.format(giaNhap) + " đ");
                }
            }
        });
        tbChiTietPhieuNhap.getColumns().addAll(colSoThuTu, colMaThuoc, colDonViTinh, colSoLuongNhap, colGiaNhap);
    }

}