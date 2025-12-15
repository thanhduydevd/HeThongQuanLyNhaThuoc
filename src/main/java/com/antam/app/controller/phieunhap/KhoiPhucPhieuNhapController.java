//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.phieunhap;

import com.antam.app.dao.NhanVien_DAO;
import com.antam.app.dao.PhieuNhap_DAO;
import com.antam.app.entity.NhanVien;
import com.antam.app.entity.PhieuNhap;
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


public class KhoiPhucPhieuNhapController extends ScrollPane{

    private Button btnKhoiPhuc, btnXoaRong;
    private TableView<PhieuNhap> tbPhieuNhap;
    private ComboBox<NhanVien> cbNhanVienNhap;
    private DatePicker dpTuNgay, dpDenNgay;
    private ComboBox<String> cbKhoangGia;
    private TextField tfTimPhieuNhap;

    private PhieuNhap_DAO phieuNhap_DAO = new PhieuNhap_DAO();
    private NhanVien_DAO nhanVien_DAO = new NhanVien_DAO();

    /* Lấy dữ liệu từ DAO */
    private ArrayList<PhieuNhap> dsPhieuNhap = new ArrayList<>();
    private ObservableList<PhieuNhap> data = FXCollections.observableArrayList();

    private PhieuNhap phieuNhapDuocChon;

    public KhoiPhucPhieuNhapController() {
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

        HBox titleBox = new HBox();
        titleBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Text title = new Text("Cập nhật phiếu nhập");
        title.setFill(Color.web("#1e3a8a"));
        title.setFont(Font.font("System Bold", 30));

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        FontAwesomeIcon iconRestore = new FontAwesomeIcon();
        iconRestore.setFill(Color.WHITE);
        iconRestore.setIcon(FontAwesomeIcons.BACKWARD);
        btnKhoiPhuc = new Button("Khôi phục");
        btnKhoiPhuc.setGraphic(iconRestore);
        btnKhoiPhuc.getStyleClass().add("btn-khoiphuc");

        titleBox.getChildren().addAll(title, spacer, btnKhoiPhuc);

        // ====================
        // 2. TabPane
        // =====================
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setPrefWidth(200);


        Tab tabPhieuNhap = new Tab("Phiếu nhập");

        VBox tabContent = new VBox(10);
        tabContent.setPadding(new Insets(10));

        // ====================
        // 3. FlowPane (ô lọc)
        // =====================
        FlowPane filterPane = new FlowPane();
        filterPane.setHgap(5);
        filterPane.setVgap(5);
        filterPane.getStyleClass().add("box-pane");

        filterPane.setPadding(new Insets(10));

        DropShadow ds = new DropShadow();
        ds.setBlurType(javafx.scene.effect.BlurType.GAUSSIAN);
        ds.setOffsetX(3);
        ds.setOffsetY(2);
        ds.setRadius(19);
        ds.setColor(Color.rgb(211, 211, 211));

        filterPane.setEffect(ds);

        // --- Nhân viên nhập
        cbNhanVienNhap = new ComboBox<>();
        cbNhanVienNhap.setPrefSize(200, 40);
        cbNhanVienNhap.setPromptText("Chọn nhân viên nhập");
        cbNhanVienNhap.getStyleClass().add("combo-box");

        VBox v1 = createLabeledBox("Nhân viên nhập:", cbNhanVienNhap);

        // --- Từ ngày
        dpTuNgay = new DatePicker();
        dpTuNgay.setPrefSize(200, 40);
        dpTuNgay.getStyleClass().add("combo-box");

        VBox v2 = createLabeledBox("Từ ngày:", dpTuNgay);

        // --- Đến ngày
        dpDenNgay = new DatePicker();
        dpDenNgay.setPrefSize(200, 40);
        dpDenNgay.getStyleClass().add("combo-box");

        VBox v3 = createLabeledBox("Đến ngày:", dpDenNgay);

        // --- Khoảng giá
        cbKhoangGia = new ComboBox<>();
        cbKhoangGia.setPrefSize(200, 40);
        cbKhoangGia.setPromptText("Chọn khoảng giá");
        cbKhoangGia.getStyleClass().add("combo-box");

        VBox v4 = createLabeledBox("Khoảng giá:", cbKhoangGia);

        // --- Button Xóa rỗng
        btnXoaRong = new Button("Xoá rỗng");
        btnXoaRong.setPrefSize(93, 40);
        btnXoaRong.getStyleClass().add("btn-xoarong");

        FontAwesomeIcon refreshIcon = new FontAwesomeIcon();
        refreshIcon.setIcon(FontAwesomeIcons.REFRESH);
        refreshIcon.setFill(Color.WHITE);
        btnXoaRong.setGraphic(refreshIcon);

        VBox v5 = new VBox(5);
        v5.getChildren().addAll(new Text(""), btnXoaRong);

        filterPane.getChildren().addAll(v1, v2, v3, v4, v5);

        // ====================
        // 4. Thanh tìm kiếm
        // =====================
        HBox searchBox = new HBox(10);

        tfTimPhieuNhap = new TextField();
        tfTimPhieuNhap.setPromptText("Tìm kiếm theo mã phiếu hoặc nhà cung cấp");
        tfTimPhieuNhap.setPrefSize(300, 100);
        tfTimPhieuNhap.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-radius: 8px;");
        tfTimPhieuNhap.setPadding(new Insets(10));

        Button btnSearch = new Button();
        btnSearch.setPrefSize(50, 40);
        btnSearch.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");

        FontAwesomeIcon searchIcon = new FontAwesomeIcon();
        searchIcon.setGlyphName("SEARCH");
        searchIcon.setFill(Color.WHITE);

        btnSearch.setGraphic(searchIcon);

        searchBox.getChildren().addAll(tfTimPhieuNhap, btnSearch);

        // ====================
        // 5. TableView
        // =====================
        tbPhieuNhap = new TableView();
        tbPhieuNhap.setPrefHeight(800);

        tbPhieuNhap.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ====================
        // 6. Hướng dẫn
        // =====================
        Button btnHuongDan = new Button("Nhấn 2 lần chuột trái vào bảng để xem chi tiết");
        btnHuongDan.setMaxWidth(Double.MAX_VALUE);
        btnHuongDan.getStyleClass().add("pane-huongdan");

        FontAwesomeIcon infoIcon = new FontAwesomeIcon();
        infoIcon.setGlyphName("INFO");
        infoIcon.setFill(Color.web("#2563eb"));
        btnHuongDan.setGraphic(infoIcon);
        btnHuongDan.setPadding(new Insets(10));

        // ====================
        // Add tất cả vào tabContent
        // =====================
        tabContent.getChildren().addAll(filterPane, searchBox, tbPhieuNhap, btnHuongDan);

        tabPhieuNhap.setContent(tabContent);
        tabPane.getTabs().add(tabPhieuNhap);

        // ====================
        // Add tất cả vào root
        // =====================
        root.getChildren().addAll(titleBox, tabPane);

        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        this.setContent(root);
        /** Sự kiện **/
    }

    private VBox createLabeledBox(String label, Control control) {
        Text t = new Text(label);
        t.setFill(Color.web("#374151"));
        t.setFont(Font.font(13));

        VBox v = new VBox(5);
        v.getChildren().addAll(t, control);
        return v;
    }
}
