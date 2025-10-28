//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.kethuoc;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.Ke_DAO;
import com.antam.app.entity.Ke;
import com.antam.app.entity.PhieuNhap;
import com.antam.app.gui.GiaoDienCuaSo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ThemKeThuocController {

    @FXML
    private TableView<Ke> tbKeThuoc;

    @FXML
    private TextField tfMaKe, tfTenKe, tfLoaiKe;

    @FXML
    private Button btnThemKe;

    private Ke_DAO ke_DAO = new Ke_DAO();

    /* Lấy dữ liệu từ DAO */
    private ArrayList<Ke> dsKeThuoc = new ArrayList<>();
    private ObservableList<Ke> data = FXCollections.observableArrayList();

    public ThemKeThuocController() {

    }

    public void initialize() {
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        dsKeThuoc =  ke_DAO.getAllKe();
        data.setAll(dsKeThuoc);
        tbKeThuoc.setItems(data);

        loadDanhSachKeThuoc();

        //Tạo mã kệ tự động khi mở form
        tfMaKe.setText(ke_DAO.taoMaKeTuDong());
        tfMaKe.setEditable(false);

        //Sự kiện click thêm
        btnThemKe.setOnAction(e ->{
            if (kiemTraHopLe()){
                ke_DAO.themKe(new Ke(tfMaKe.getText(), tfTenKe.getText(), tfLoaiKe.getText(), false));
                showCanhBao("Thêm kệ thành công","Bạn đã thêm kệ thuốc thành công!");
                //Cập nhật lại bảng
                dsKeThuoc =  ke_DAO.getAllKe();
                data.setAll(dsKeThuoc);
                tbKeThuoc.setItems(data);
                //Tạo mã kệ tự động mới
                tfMaKe.setText(ke_DAO.taoMaKeTuDong());
                //Xoá trắng các trường nhập liệu
                tfTenKe.clear();
                tfLoaiKe.clear();
            }
        });
    }

    public void loadDanhSachKeThuoc(){

        /* Tên cột */
        TableColumn<Ke, String> colMaKe = new TableColumn<>("Mã Kệ");
        colMaKe.setCellValueFactory(new PropertyValueFactory<>("MaKe"));

        TableColumn<Ke, String> colTenKe = new TableColumn<>("Tên Kệ");
        colTenKe.setCellValueFactory(new PropertyValueFactory<>("tenKe"));

        TableColumn<Ke, Date> colLoaiKe = new TableColumn<>("Loại Kệ");
        colLoaiKe.setCellValueFactory(new PropertyValueFactory<>("LoaiKe"));

        TableColumn<Ke, String> colTrangThai = new TableColumn<>("Trạng Thái");
        colTrangThai.setCellValueFactory(cellData -> {
            Ke ke = cellData.getValue();
            return new SimpleStringProperty(ke.isDeleteAt() ? "Đã xoá" : "Hoạt động");
        });

        tbKeThuoc.getColumns().addAll(colMaKe, colTenKe, colLoaiKe, colTrangThai);
    }

    public boolean kiemTraHopLe(){
        String tenKe = tfTenKe.getText();
        String loaiKe = tfLoaiKe.getText();

        if (tenKe.isEmpty()){
            showCanhBao("Lỗi nhập liệu","Vui lòng nhập tên kệ!");
            tfTenKe.requestFocus();
            return false;
        }

        if (loaiKe.isEmpty()){
            showCanhBao("Lỗi nhập liệu","Vui lòng nhập loại kệ!");
            tfLoaiKe.requestFocus();
            return false;
        }

        if (ke_DAO.getKeTheoName(tenKe) != null){
            showCanhBao("Lỗi nhập liệu","Tên kệ đã tồn tại, vui lòng nhập tên khác!");
            tfTenKe.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Hiển thị thông báo cảnh báo
     * @param tieuDe
     * @param vanBan
     */
    public void showCanhBao(String tieuDe, String vanBan){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tieuDe);
        alert.setContentText(vanBan);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
