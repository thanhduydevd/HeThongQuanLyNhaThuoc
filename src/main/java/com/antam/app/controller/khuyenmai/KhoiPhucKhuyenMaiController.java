//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.khuyenmai;

import com.antam.app.dao.KhuyenMai_DAO;
import com.antam.app.entity.KhuyenMai;
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

public class KhoiPhucKhuyenMaiController extends ScrollPane{

    private Button btnKhoiPhuc;
    private ComboBox<String> cbLoaiKhuyenMai, cbTrangThai;
    private DatePicker dpTuNgay, dpDenNgay;
    private TextField txtTiemKiemKhuyenMai;
    private Button btnTimKiem;
    private TableView<KhuyenMai> tableKhuyenMai;

    private TableColumn<KhuyenMai, String> colMaKhuyenMai, colTenKhuyenMai, colLoaiKhuyenMai, colSo, colSoLuongToiDa, colTinhTrang;
    private ObservableList<KhuyenMai> khuyenMaiList = FXCollections.observableArrayList();
    private ArrayList<KhuyenMai> arrayKhuyenMai = new ArrayList<>();
    private KhuyenMai_DAO khuyenMai_dao = new KhuyenMai_DAO();
    public KhoiPhucKhuyenMaiController() {
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

        // ========================= TIÊU ĐỀ =========================
        HBox titleBox = new HBox(5);
        titleBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Text title = new Text("Khôi phục khuyến mãi");
        title.setFont(Font.font("System Bold", 30));
        title.setFill(Color.web("#1e3a8a"));

        Pane space = new Pane();
        HBox.setHgrow(space, Priority.ALWAYS);

        btnKhoiPhuc = new Button("Khôi phục");
        btnKhoiPhuc.getStyleClass().add("btn-khoiphuc");
        btnKhoiPhuc.setTextFill(Color.WHITE);
        FontAwesomeIcon iconRestore = new FontAwesomeIcon();
        iconRestore.setIcon(FontAwesomeIcons.BACKWARD);
        iconRestore.setFill(Color.WHITE);
        btnKhoiPhuc.setGraphic(iconRestore);

        titleBox.getChildren().addAll(title, space, btnKhoiPhuc);

        // ========================= HỘP FILTER =========================
        FlowPane filterPane = new FlowPane(5, 5);
        filterPane.getStyleClass().add("box-pane");
        filterPane.setPadding(new Insets(10));
        filterPane.setStyle("-fx-background-color: white;");

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#d3d3d3"));
        shadow.setOffsetX(3);
        shadow.setOffsetY(2);
        filterPane.setEffect(shadow);

        // 4 combobox theo cột
        cbLoaiKhuyenMai = new ComboBox<>();
        cbTrangThai = new ComboBox<>();
        dpTuNgay = new DatePicker();
        dpDenNgay = new DatePicker();

        filterPane.getChildren().addAll(
                createBox("Loại khuyến mãi:", cbLoaiKhuyenMai),
                createBox("Trạng thái:", cbTrangThai),
                createBox("Từ ngày:", dpTuNgay),
                createBox("Đến ngày:", dpDenNgay)
        );

        // ========================= TÌM KIẾM =========================
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        txtTiemKiemKhuyenMai = new TextField();
        txtTiemKiemKhuyenMai.setPromptText("Tìm kiếm khuyến mãi...");
        txtTiemKiemKhuyenMai.setPrefSize(300, 40);
        txtTiemKiemKhuyenMai.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-radius: 8px;");

        btnTimKiem = new Button();
        btnTimKiem.setPrefSize(50, 40);
        btnTimKiem.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");
        btnTimKiem.setTextFill(Color.WHITE);

        FontAwesomeIcon iconSearch = new FontAwesomeIcon();
        iconSearch.setIcon(FontAwesomeIcons.SEARCH);
        iconSearch.setFill(Color.WHITE);
        btnTimKiem.setGraphic(iconSearch);

        searchBox.getChildren().addAll(txtTiemKiemKhuyenMai, btnTimKiem);

        // ========================= TABLE =========================
        tableKhuyenMai = new TableView();
        tableKhuyenMai.setPrefHeight(800);

        colMaKhuyenMai = new TableColumn("Mã khuyến mãi");
        colTenKhuyenMai = new TableColumn("Tên khuyến mãi");
        colLoaiKhuyenMai = new TableColumn("Loại");
        colSo = new TableColumn("Số (Giá trị)");
        colSoLuongToiDa = new TableColumn("Số lượng tối đa");
        colTinhTrang = new TableColumn("Trạng thái");

        tableKhuyenMai.getColumns().addAll(
                colMaKhuyenMai, colTenKhuyenMai, colLoaiKhuyenMai,
                colSo, colSoLuongToiDa, colTinhTrang
        );

        tableKhuyenMai.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ========================= ADD TO ROOT =========================
        root.getChildren().addAll(titleBox, filterPane, searchBox, tableKhuyenMai);

        this.setContent(root);
        /** Sự kiện **/
    }

    private VBox createBox(String title, Control field) {
        VBox box = new VBox(5);

        Text label = new Text(title);
        label.setFill(Color.web("#374151"));
        label.setFont(Font.font(13));

        field.setPrefSize(200, 40);

        box.getChildren().addAll(label, field);
        return box;
    }
}
