//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.phieudat;

import com.antam.app.dao.*;
import com.antam.app.entity.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class ThemPhieuDatFormController extends DialogPane{
    private TextField txtMa;
    private TextField txtTenKhach;
    private TextField txtSoDienThoai;
    private VBox vbThuoc;
    private ComboBox cbTenThuoc;
    private ComboBox cbDonVi;
    private Spinner spSoLuong;
    private TextField txtDonGia;
    private Button btnThem;
    private ComboBox cbKhuyenMai;
    private TableView tbChonThuoc;
    private TableColumn colTenThuoc;
    private TableColumn colDonVi;
    private TableColumn colSoLuong;
    private TableColumn colDonGia;
    private TableColumn colThanhTien;
    private Text txtTotal;

    private Thuoc_DAO thuoc_dao = new Thuoc_DAO();
    private  DonViTinh_DAO donViTinh_dao = new DonViTinh_DAO();
    private ArrayList<Thuoc> dsThuoc;
    private ArrayList<DonViTinh> dsDonViTinh;
    private DecimalFormat decimalFormat = new DecimalFormat("#,### đ");
    KhachHang_DAO khachHangDAO = new KhachHang_DAO();
    ArrayList<KhachHang> dsKhach = khachHangDAO.getAllKhachHang();
    KhuyenMai_DAO KhuyenMai_DAO = new KhuyenMai_DAO();
    ArrayList<KhuyenMai> dsKhuyenMai = (ArrayList<KhuyenMai>) KhuyenMai_DAO.getAllKhuyenMaiConHieuLuc();
    DonViTinh_DAO dvtDAO = new DonViTinh_DAO();

    public ThemPhieuDatFormController() {
        FlowPane header = new FlowPane();
        header.setAlignment(Pos.CENTER);
        header.setPrefSize(800, 35);
        header.setStyle("-fx-background-color: #1e3a8a;");

        Text headerTitle = new Text("Tạo phiếu đặt mới");
        headerTitle.setFill(javafx.scene.paint.Color.WHITE);
        headerTitle.setFont(Font.font("System Bold", 15));
        FlowPane.setMargin(headerTitle, new Insets(10, 0, 10, 0));
        header.getChildren().add(headerTitle);

        AnchorPane content = new AnchorPane();
        content.setPrefSize(800, 557);

        VBox root = new VBox(10);
        AnchorPane.setLeftAnchor(root, 10.0);
        AnchorPane.setTopAnchor(root, 10.0);

        GridPane gridTop = new GridPane();
        gridTop.setHgap(5);
        gridTop.setPrefSize(744, 69);

        ColumnConstraints cc1 = new ColumnConstraints();
        cc1.setHgrow(Priority.SOMETIMES);
        ColumnConstraints cc2 = new ColumnConstraints();
        cc2.setHgrow(Priority.SOMETIMES);
        gridTop.getColumnConstraints().addAll(cc1, cc2);

        gridTop.getRowConstraints().addAll(
                new RowConstraints(30),
                new RowConstraints(40),
                new RowConstraints(30)
        );

        Text lblMa = new Text("Mã phiếu đặt:");
        lblMa.setFill(javafx.scene.paint.Color.web("#374151"));

        txtMa = new TextField();
        txtMa.getStyleClass().add("text-field");

        GridPane.setRowIndex(txtMa, 1);

        Text lblTenKhach = new Text("Tên khách hàng:");
        lblTenKhach.setFill(javafx.scene.paint.Color.web("#374151"));
        GridPane.setColumnIndex(lblTenKhach, 1);

        txtTenKhach = new TextField();
        txtTenKhach.getStyleClass().add("text-field");
        GridPane.setColumnIndex(txtTenKhach, 1);
        GridPane.setRowIndex(txtTenKhach, 1);

        Text lblSoDienThoai = new Text("Số điện thoại:");
        lblTenKhach.setFill(javafx.scene.paint.Color.web("#374151"));
        GridPane.setColumnIndex(lblSoDienThoai, 1);
        GridPane.setRowIndex(lblSoDienThoai, 2);

        txtSoDienThoai = new TextField();
        txtSoDienThoai.getStyleClass().add("text-field");
        GridPane.setColumnIndex(txtSoDienThoai, 1);
        GridPane.setRowIndex(txtSoDienThoai, 3);

        Text lblThuocDat = new Text("Thuốc đặt:");
        lblThuocDat.setFill(javafx.scene.paint.Color.web("#374151"));
        GridPane.setRowIndex(lblThuocDat, 4);

        gridTop.getChildren().addAll(lblMa, txtMa, lblTenKhach, txtTenKhach, lblSoDienThoai, txtSoDienThoai, lblThuocDat);

        vbThuoc = new VBox(5);
        vbThuoc.setMaxWidth(Double.MAX_VALUE);
        vbThuoc.setStyle("-fx-border-color: #e5e7eb; -fx-border-radius: 6px; -fx-border-width: 2px;");
        vbThuoc.setPadding(new Insets(10));

        HBox hbFirst = new HBox();
        hbFirst.setPrefSize(720, 29);

        VBox boxTen = new VBox();
        Text lblTenThuoc = new Text("Tên thuốc:");
        cbTenThuoc = new ComboBox();
        cbTenThuoc.setPrefSize(154, 26);
        boxTen.getChildren().addAll(lblTenThuoc, cbTenThuoc);

        VBox boxDonVi = new VBox();
        Text lblDV = new Text("Đơn vị:");
        cbDonVi = new ComboBox();
        cbDonVi.setPrefSize(161, 26);
        boxDonVi.getChildren().addAll(lblDV, cbDonVi);

        VBox boxSL = new VBox();
        Text lblSL = new Text("Số lượng:");
        spSoLuong = new Spinner();
        spSoLuong.setPrefSize(150, 26);
        boxSL.getChildren().addAll(lblSL, spSoLuong);

        VBox boxGia = new VBox();
        Text lblGia = new Text("Đơn giá:");
        txtDonGia = new TextField();
        boxGia.getChildren().addAll(lblGia, txtDonGia);

        hbFirst.getChildren().addAll(boxTen, boxDonVi, boxSL, boxGia);
        vbThuoc.getChildren().add(hbFirst);

        btnThem = new Button("Thêm thuốc");
        btnThem.getStyleClass().add("btn-gray");
        btnThem.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());

        Text lblKM = new Text("Áp dụng khuyến mãi:");

        cbKhuyenMai = new ComboBox();
        cbKhuyenMai.setPrefSize(778, 44);
        cbKhuyenMai.setPromptText("Chọn khuyến mãi");

        tbChonThuoc = new TableView();
        tbChonThuoc.setPrefSize(785, 200);

        colTenThuoc = new TableColumn("Tên thuốc");
        colTenThuoc.setPrefWidth(192.8);

        colDonVi = new TableColumn("Đơn vị");
        colDonVi.setPrefWidth(168.8);

        colSoLuong = new TableColumn("Số lượng");
        colSoLuong.setPrefWidth(110.4);

        colDonGia = new TableColumn("Đơn giá");
        colDonGia.setPrefWidth(166.4);

        colThanhTien = new TableColumn("Thành tiền");
        colThanhTien.setPrefWidth(143.2);

        tbChonThuoc.getColumns().addAll(colTenThuoc, colDonVi, colSoLuong, colDonGia, colThanhTien);

        GridPane gridTotal = new GridPane();
        gridTotal.setHgap(5);
        gridTotal.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 8px;");
        gridTotal.setPadding(new Insets(10));

        ColumnConstraints tc1 = new ColumnConstraints();
        tc1.setHgrow(Priority.SOMETIMES);
        ColumnConstraints tc2 = new ColumnConstraints();
        tc2.setHgrow(Priority.SOMETIMES);
        gridTotal.getColumnConstraints().addAll(tc1, tc2);

        Text lblTong = new Text("Tổng tiền:");
        lblTong.setFill(javafx.scene.paint.Color.web("#374151"));
        lblTong.setFont(Font.font("System Bold", 18));

        txtTotal = new Text("0 đ");
        txtTotal.setFill(javafx.scene.paint.Color.web("#374151"));
        txtTotal.setFont(Font.font("System Bold", 18));
        GridPane.setColumnIndex(txtTotal, 1);

        gridTotal.getChildren().addAll(lblTong, txtTotal);

        root.getChildren().addAll(
                gridTop,
                vbThuoc,
                btnThem,
                lblKM,
                cbKhuyenMai,
                tbChonThuoc,
                gridTotal
        );

        content.getChildren().add(root);

        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        this.setHeader(header);
        this.setContent(content);
        /** Sự kiện **/
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Lưu", ButtonData.APPLY);
        this.getButtonTypes().add(cancelButton);
        this.getButtonTypes().add(applyButton);
    }

    private boolean checkDuLieu() {
        return true;
    }

    public String dinhDangTien(double tien){
        DecimalFormat df = new DecimalFormat("#,###đ");
        return df.format(tien);
    }
}
