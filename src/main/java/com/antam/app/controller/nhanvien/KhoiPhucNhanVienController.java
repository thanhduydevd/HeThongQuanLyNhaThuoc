package com.antam.app.controller.nhanvien;

import com.antam.app.dao.NhanVien_DAO;
import com.antam.app.entity.NhanVien;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class KhoiPhucNhanVienController extends ScrollPane{

    private TableView<NhanVien> tbNhanVien;
    private Button btnFindNV,btnXoaTrang, btnKhoiPhuc;
    private TextField txtFindNV;
    private TableColumn<NhanVien, String> colMaNV, colHoTen, colChucVu, colSDT, colDiaChi, colEmail;
    private TableColumn<NhanVien, String> colLuong;
    private ComboBox<String> cbChucVu, cbLuongCB;
    ArrayList<NhanVien> listNV = NhanVien_DAO.getDsNhanVienformDBS();

    private ObservableList<NhanVien> TVNhanVien;
    private final ObservableList<NhanVien> filteredList = FXCollections.observableArrayList();
    public  static NhanVien nhanVienSelected;

    public KhoiPhucNhanVienController(){
        /** Giao diện **/
        this.setFitToHeight(true);
        this.setFitToWidth(true);
        this.setPrefSize(900, 730);
        AnchorPane.setTopAnchor(this, 0.0);
        AnchorPane.setBottomAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, 0.0);
        AnchorPane.setRightAnchor(this, 0.0);

        VBox root = new VBox(30);
        root.setStyle("-fx-background-color: #f8fafc;");
        root.setPadding(new Insets(20));

        HBox titleBox = new HBox(5);
        titleBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Text title = new Text("Khôi phục nhân viên");
        title.setFont(Font.font("System Bold", 30));
        title.setFill(Color.web("#1e3a8a"));
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        FontAwesomeIcon iconRestore = new FontAwesomeIcon();
        iconRestore.setIcon(FontAwesomeIcons.BACKWARD);
        iconRestore.setFill(Color.WHITE);
        btnKhoiPhuc = new Button("Khôi phục");
        btnKhoiPhuc.getStyleClass().add("btn-khoiphuc");
        btnKhoiPhuc.setGraphic(iconRestore);
        titleBox.getChildren().addAll(title, spacer, btnKhoiPhuc);

        // Filters FlowPane
        FlowPane filterPane = new FlowPane(5, 5);
        filterPane.setPadding(new Insets(10));
        filterPane.setStyle("-fx-background-color: white; -fx-background-radius: 8px; -fx-border-radius: 5px;");
        DropShadow ds = new DropShadow();
        ds.setColor(Color.rgb(211,211,211));
        ds.setOffsetX(3);
        ds.setOffsetY(2);
        ds.setRadius(19.5);
        filterPane.setEffect(ds);

        // Chức vụ
        VBox vbChucVu = new VBox(5);
        Text txtChucVu = new Text("Chức vụ");
        txtChucVu.setFont(Font.font(13));
        txtChucVu.setFill(Color.web("#374151"));
        cbChucVu = new ComboBox<>();
        cbChucVu.setPrefSize(200, 40);
        cbChucVu.setPromptText("Chọn chức vụ");
        vbChucVu.getChildren().addAll(txtChucVu, cbChucVu);

        // Lương cơ bản
        VBox vbLuong = new VBox(5);
        Text txtLuong = new Text("Lương cơ bản");
        txtLuong.setFont(Font.font(13));
        txtLuong.setFill(Color.web("#374151"));
        cbLuongCB = new ComboBox<>();
        cbLuongCB.setPrefSize(200, 40);
        cbLuongCB.setPromptText("Chọn lương cơ bản");
        vbLuong.getChildren().addAll(txtLuong, cbLuongCB);

        // Xóa trắng
        VBox vbXoaTrang = new VBox(5);
        Text txtEmpty = new Text("");
        btnXoaTrang = new Button("Xóa trắng");
        btnXoaTrang.setPrefSize(70, 34);
        btnXoaTrang.getStyleClass().add("btn-xoarong");
        vbXoaTrang.getChildren().addAll(txtEmpty, btnXoaTrang);

        filterPane.getChildren().addAll(vbChucVu, vbLuong, vbXoaTrang);

        // Search box HBox
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        txtFindNV = new TextField();
        txtFindNV.setPromptText("Tìm kiếm nhân viên...");
        txtFindNV.setPrefSize(300, 40);
        txtFindNV.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-radius: 8px;");

        btnFindNV = new Button();
        btnFindNV.setPrefSize(50, 40);
        btnFindNV.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");
        btnFindNV.setTextFill(Color.WHITE);
        FontAwesomeIcon searchIcon = new FontAwesomeIcon();
        searchIcon.setIcon(FontAwesomeIcons.SEARCH);
        searchIcon.setFill(Color.WHITE);
        btnFindNV.setGraphic(searchIcon);

        searchBox.getChildren().addAll(txtFindNV, btnFindNV);

        // TableView
        tbNhanVien = new TableView<>();
        tbNhanVien.setPrefSize(858, 480);

        colMaNV = new TableColumn<>("Mã nhân viên");
        colHoTen = new TableColumn<>("Họ tên");
        colChucVu = new TableColumn<>("Chức vụ");
        colSDT = new TableColumn<>("Số điện thoại");
        colDiaChi = new TableColumn<>("Địa chỉ");
        colEmail = new TableColumn<>("Email");
        colLuong = new TableColumn<>("Lương cơ bản");

        tbNhanVien.getColumns().addAll(colMaNV, colHoTen, colChucVu, colSDT, colDiaChi, colEmail, colLuong);
        tbNhanVien.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        root.getChildren().addAll(titleBox, filterPane, searchBox, tbNhanVien);

        this.setContent(root);
        /** Sự kiện **/
    }
}
