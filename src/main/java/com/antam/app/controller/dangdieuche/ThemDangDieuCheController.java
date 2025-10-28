//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.dangdieuche;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.DangDieuChe_DAO;
import com.antam.app.entity.DangDieuChe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class ThemDangDieuCheController {

    @FXML
    private TableView<DangDieuChe> tbDangDieuChe;

    @FXML
    private TextField tfMaDangDieuChe, tfTenDangDieuChe;

    @FXML
    private Button btnThem;

    private DangDieuChe_DAO dangDieuChe_DAO = new DangDieuChe_DAO();

    /* Lấy dữ liệu từ DAO */
    private ArrayList<DangDieuChe> dsDangDieuChe = new ArrayList<>();
    private ObservableList<DangDieuChe> data = FXCollections.observableArrayList();

    public ThemDangDieuCheController() {

    }

    public void initialize() {
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        dsDangDieuChe =  dangDieuChe_DAO.getAllDDC();
        data.setAll(dsDangDieuChe);
        tbDangDieuChe.setItems(data);

        loadDanhSachDangDieuChe();

        //Tạo mã kệ tự động khi mở form
        tfMaDangDieuChe.setText(dangDieuChe_DAO.taoMaDDCTuDong());
        tfMaDangDieuChe.setEditable(false);

        //Sự kiện click thêm
        btnThem.setOnAction(e ->{
            if (kiemTraHopLe()){
                dangDieuChe_DAO.themDDC(new DangDieuChe(Integer.parseInt(tfMaDangDieuChe.getText()), tfTenDangDieuChe.getText()));
                showCanhBao("Thêm dạng điều chế","Thêm dạng điều chế thành công!");
                //Cập nhật lại bảng
                dsDangDieuChe =  dangDieuChe_DAO.getAllDDC();
                data.setAll(dsDangDieuChe);
                tbDangDieuChe.setItems(data);
                //Tạo mã kệ tự động mới
                tfMaDangDieuChe.setText(dangDieuChe_DAO.taoMaDDCTuDong());
                //Xoá trắng các trường nhập liệu
                tfTenDangDieuChe.clear();
            }
        });
    }

    public void loadDanhSachDangDieuChe(){

        /* Tên cột */
        TableColumn<DangDieuChe, String> colMaDangDieuChe = new TableColumn<>("Mã Dạng Điều Chế");
        colMaDangDieuChe.setCellValueFactory(new PropertyValueFactory<>("MaDDC"));

        TableColumn<DangDieuChe, String> colTenDangDieuChe = new TableColumn<>("Tên Dạng Điều Chế");
        colTenDangDieuChe.setCellValueFactory(new PropertyValueFactory<>("TenDDC"));

        tbDangDieuChe.getColumns().addAll(colMaDangDieuChe, colTenDangDieuChe);
    }

    public boolean kiemTraHopLe(){
        String tenDDC = tfTenDangDieuChe.getText();

        if (tenDDC.isEmpty()){
            showCanhBao("Lỗi nhập liệu","Vui lòng nhập tên dạng điều chế!");
            tfTenDangDieuChe.requestFocus();
            return false;
        }

        if (dangDieuChe_DAO.getDDCTheoName(tenDDC) != null){
            showCanhBao("Lỗi nhập liệu","Dạng điều chế đã tồn tại trong hệ thống!");
            tfTenDangDieuChe.requestFocus();
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
