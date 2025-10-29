//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.donvitinh;

import com.antam.app.dao.DonViTinh_DAO;
import com.antam.app.entity.DonViTinh;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class CapNhatDonViTinhController {
    @FXML
    private TextField txtMa,txtTen;
    @FXML
    private TableView<DonViTinh> tableThuoc;
    @FXML
    private TableColumn<DonViTinh, String> colMaThuoc, colTenThuoc;
    @FXML
    private Button btnXoa,btnCapNhat;

    DonViTinh_DAO donViTinh_dao = new DonViTinh_DAO();
    ArrayList<DonViTinh> listDVT ;

    public CapNhatDonViTinhController() {
    }

    public void initialize() {
        btnCapNhat.setOnAction( e-> {
            try {
                String ma = txtMa.getText();
                int maInt = Integer.parseInt(ma);
                String ten = txtTen.getText();
                DonViTinh donViTinh = new DonViTinh(maInt, ten, false);
                donViTinh_dao.updateDonViTinh(donViTinh);
                loadTable();
            }catch (Exception ex) {
                // Hiển thị thông báo lỗi nếu có
            }
        });

        btnXoa.setOnAction( e-> {
            try {
                String ma = txtMa.getText();
                int maInt = Integer.parseInt(ma);
                String ten = txtTen.getText();
                DonViTinh donViTinh = new DonViTinh(maInt, ten,false);
                donViTinh_dao.xoaDonViTinh(donViTinh);
                loadTable();
            }catch (Exception ex) {
                // Hiển thị thông báo lỗi nếu có
            }
        });
        txtMa.setEditable(false);
        try {
            int maxMa = Integer.parseInt(DonViTinh_DAO.getHashDVT());
            txtMa.setText(String.valueOf(++maxMa));
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadTable();
        setupTable();
        tableThuoc.setOnMouseClicked( e-> {
            DonViTinh selected = tableThuoc.getSelectionModel().getSelectedItem();
            if (selected != null) {
                txtMa.setText(String.valueOf(selected.getMaDVT()));
                txtTen.setText(selected.getTenDVT());
            }
        });
    }

    private void setupTable() {
        colMaThuoc.setCellValueFactory( e -> new SimpleStringProperty(String.valueOf(e.getValue().getMaDVT())));
        colTenThuoc.setCellValueFactory( e -> new SimpleStringProperty(e.getValue().getTenDVT()));
    }

    private void loadTable() {
        listDVT = donViTinh_dao.getTatCaDonViTinh();

        // Loại bỏ các đơn vị đã xóa
        listDVT.removeIf(DonViTinh::isDelete);

        // Cập nhật dữ liệu cho TableView
        tableThuoc.getItems().setAll(listDVT);

    }
}
