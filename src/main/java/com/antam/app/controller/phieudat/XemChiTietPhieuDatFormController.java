//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.phieudat;

import com.antam.app.dao.PhieuDat_DAO;
import com.antam.app.entity.ChiTietPhieuDatThuoc;
import com.antam.app.entity.PhieuDatThuoc;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.antam.app.controller.phieudat.TimPhieuDatController.selectedPhieuDatThuoc;

public class XemChiTietPhieuDatFormController {
    @FXML
    DialogPane dialogPane;
    @FXML
    private Text txtMa,txtNgay,txtSDT,txtStatus,txtTongTien,txtKM;
    @FXML
    private TableColumn<ChiTietPhieuDatThuoc,Integer> colSTT;
    @FXML
    private TableColumn<ChiTietPhieuDatThuoc,String> colTenThuoc,colThanhTien,colDonGia;
    @FXML
    private TableColumn<ChiTietPhieuDatThuoc,Integer> colSoLuong;
    @FXML
    private TableView<ChiTietPhieuDatThuoc> tbThuoc;

    private PhieuDatThuoc select = selectedPhieuDatThuoc;
    private ArrayList<ChiTietPhieuDatThuoc> listChiTiet = PhieuDat_DAO.getChiTietTheoPhieu(select.getMaPhieu());
    public XemChiTietPhieuDatFormController() {
    }

    public void initialize() {
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        this.dialogPane.getButtonTypes().add(cancelButton);

        //gọi các phương thức xử lí
        setupTable();
        loadBangChiTiet();
        loadContent();
    }

    private void loadContent() {
        txtMa.setText("Mã phiếu đặt: " + select.getMaPhieu());
        txtNgay.setText("Ngày đặt: "+ select.getNgayTao().toString());
        txtSDT.setText("Tên khách hàng: "+select.getKhachHang().getTenKH());
        String trangThai;
        if (select.isThanhToan()) {
            trangThai = "Đã thanh toán";
        } else  {
            trangThai = "Chưa thanh toán";
        }
        txtStatus.setText("Trạng thái: "+ trangThai);
        if (select.getKhuyenMai() != null) {
            txtKM.setText(select.getKhuyenMai().getTenKM());
        } else {
            txtKM.setText("Không áp dụng");
        }
        txtTongTien.setText(dinhDangTien(select.getTongTien()));
    }

    private void setupTable() {
        colSTT.setCellValueFactory(cellData -> new SimpleIntegerProperty(listChiTiet.indexOf(cellData.getValue()) + 1).asObject());
        colTenThuoc.setCellValueFactory(cellData ->new SimpleStringProperty(cellData.getValue().getSoDangKy().getTenThuoc()));
        colSoLuong.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSoLuong()).asObject());
        colDonGia.setCellValueFactory(cellData -> new SimpleStringProperty(dinhDangTien(cellData.getValue().getSoDangKy().getGiaBan())));
        colThanhTien.setCellValueFactory(cellData -> new SimpleStringProperty(dinhDangTien(cellData.getValue().getThanhTien())));
    }

    private void loadBangChiTiet() {
        ObservableList<ChiTietPhieuDatThuoc> load = FXCollections.observableArrayList(listChiTiet);
        tbThuoc.setItems(load);
    }

    private String dinhDangTien(double tien){
        DecimalFormat df = new DecimalFormat("#,### đ");
        return df.format(tien);
    }
}
