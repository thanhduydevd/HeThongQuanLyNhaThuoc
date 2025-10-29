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


public class CapNhatDangDieuCheController {

    @FXML
    private TableView<DangDieuChe> tbDangDieuChe;

    @FXML
    private TextField tfMaDangDieuChe, tfTenDangDieuChe ;

    @FXML
    private Button btnCapNhat, btnXoa;

    private DangDieuChe_DAO DangDieuChe_DAO = new DangDieuChe_DAO();


    /* Lấy dữ liệu từ DAO */
    private ArrayList<DangDieuChe> dsDangDieuCheThuoc = new ArrayList<>();
    private ObservableList<DangDieuChe> data = FXCollections.observableArrayList();

    public CapNhatDangDieuCheController() {

    }

    public void initialize() {
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        dsDangDieuCheThuoc =  DangDieuChe_DAO.getAllDDC();
        data.setAll(dsDangDieuCheThuoc);
        tbDangDieuChe.setItems(data);

        loadDanhSachDangDieuCheThuoc();

        tfMaDangDieuChe.setEditable(false);

        //Sự kiện khi chọn 1 hàng trong bảng
        tbDangDieuChe.setOnMouseClicked(e -> {
            DangDieuChe DangDieuChe = tbDangDieuChe.getSelectionModel().getSelectedItem();
            tfMaDangDieuChe.setText(String.valueOf(DangDieuChe.getMaDDC()));
            tfTenDangDieuChe.setText(DangDieuChe.getTenDDC());
        });

        //Sự kiện cho nút sửa dạng điều chế
        btnCapNhat.setOnAction(e -> {
            if (kiemTraHopLe()){
                DangDieuChe_DAO.suaDangDieuChe(new DangDieuChe(Integer.parseInt(tfMaDangDieuChe.getText()), tfTenDangDieuChe.getText()));
                showCanhBao("Thông báo","Cập nhật dạng điều chế thành công!");
                //Cập nhật lại bảng
                dsDangDieuCheThuoc =  DangDieuChe_DAO.getAllDDC();
                data.setAll(dsDangDieuCheThuoc);
                tbDangDieuChe.setItems(data);
                //Xoá trắng các trường nhập liệu
                tfMaDangDieuChe.clear();
                tfTenDangDieuChe.clear();
            }
        });

        btnXoa.setOnAction(e -> {
            if(!tfMaDangDieuChe.getText().isEmpty()){
                DangDieuChe_DAO.xoaDangDieuChe(Integer.parseInt(tfMaDangDieuChe.getText()));
                showCanhBao("Thông báo","Xoá dạng điều chế thành công!");
                //Cập nhật lại bảng
                dsDangDieuCheThuoc =  DangDieuChe_DAO.getAllDDC();
                data.setAll(dsDangDieuCheThuoc);
                tbDangDieuChe.setItems(data);
                //Xoá trắng các trường nhập liệu
                tfMaDangDieuChe.clear();
                tfTenDangDieuChe.clear();
            }else{
                showCanhBao("Lỗi","Vui lòng chọn dạng điều chế cần xoá!");
            }
        });
    }

    public void loadDanhSachDangDieuCheThuoc(){

        /* Tên cột */
        TableColumn<DangDieuChe, String> colMaDangDieuChe = new TableColumn<>("Mã Dạng Điều Chế");
        colMaDangDieuChe.setCellValueFactory(new PropertyValueFactory<>("MaDDC"));

        TableColumn<DangDieuChe, String> colTenDangDieuChe = new TableColumn<>("Tên Dạng Điều Chế");
        colTenDangDieuChe.setCellValueFactory(new PropertyValueFactory<>("TenDDC"));

        tbDangDieuChe.getColumns().addAll(colMaDangDieuChe, colTenDangDieuChe);
    }

    public boolean kiemTraHopLe(){
        String maDangDieuChe = tfMaDangDieuChe.getText();
        String tenDangDieuChe = tfTenDangDieuChe.getText();

        if (maDangDieuChe.isEmpty()){
            showCanhBao("Lỗi nhập liệu","Vui lòng nhập mã dạng điều chế");
            tfMaDangDieuChe.requestFocus();
            return false;
        }

        if (tenDangDieuChe.isEmpty()){
            showCanhBao("Lỗi nhập liệu","Vui lòng nhập tên dạng điều chế!");
            tfTenDangDieuChe.requestFocus();
            return false;
        }

        if (DangDieuChe_DAO.getDDCTheoName(tenDangDieuChe) != null){
            showCanhBao("Lỗi nhập liệu","Tên dạng điều chế đã tồn tại!");
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
