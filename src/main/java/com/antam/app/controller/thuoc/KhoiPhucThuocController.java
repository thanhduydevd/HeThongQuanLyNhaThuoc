package com.antam.app.controller.thuoc;

import com.antam.app.dao.ChiTietThuoc_DAO;
import com.antam.app.dao.DangDieuChe_DAO;
import com.antam.app.dao.Ke_DAO;
import com.antam.app.dao.Thuoc_DAO;
import com.antam.app.entity.DangDieuChe;
import com.antam.app.entity.Ke;
import com.antam.app.entity.Thuoc;
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
import java.util.HashMap;

public class KhoiPhucThuocController extends ScrollPane{

    private Ke_DAO ke_dao;
    private DangDieuChe_DAO ddc_dao;
    private Thuoc_DAO thuoc_dao;
    private ChiTietThuoc_DAO chiTietThuoc_dao;
    private HashMap<String, Integer> mapTonKho = new HashMap<>();

    private ComboBox<Ke> cbKe;
    private ComboBox<DangDieuChe> cbDangDieuChe;
    private ComboBox<String>cbTonKho;
    private TextField searchNameThuoc;
    private Button btnSearchThuoc, btnKhoiPhuc;
    private TableView<Thuoc> tableThuoc;
    private TableColumn<Thuoc, String> colMaThuoc, colTenThuoc, colHamLuong, colDangDieuChe, colGiaBan, colKe;
    private TableColumn<Thuoc, String> colTonKho;
    private Button btnSearchInvoice1;

    private ObservableList<Thuoc> thuocList = FXCollections.observableArrayList();
    private ArrayList<Thuoc> arrayThuoc = new ArrayList<>();

    public KhoiPhucThuocController(){
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

        // ===================== TIÊU ĐỀ =====================
        HBox titleBox = new HBox();
        titleBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Text title = new Text("Khôi phục thuốc");
        title.setFont(Font.font("System Bold", 30));
        title.setFill(Color.web("#1e3a8a"));

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        FontAwesomeIcon iconAdd = new FontAwesomeIcon();
        iconAdd.setFill(Color.WHITE);
        iconAdd.setIcon(FontAwesomeIcons.BACKWARD);
        btnKhoiPhuc = new Button("Khôi phục");
        btnKhoiPhuc.setGraphic(iconAdd);
        btnKhoiPhuc.getStyleClass().add("btn-khoiphuc");

        titleBox.getChildren().addAll(title, spacer, btnKhoiPhuc);

        // ===================== BỘ LỌC =====================
        FlowPane filterPane = new FlowPane(5, 5);
        filterPane.getStyleClass().add("box-pane");

        filterPane.setPadding(new Insets(10));

        DropShadow ds = new DropShadow();
        ds.setBlurType(javafx.scene.effect.BlurType.GAUSSIAN);
        ds.setOffsetX(3);
        ds.setOffsetY(2);
        ds.setRadius(19.5);
        ds.setColor(Color.rgb(211, 211, 211));
        filterPane.setEffect(ds);

        // --- Kệ thuốc ---
        cbKe = new ComboBox<>();
        cbKe.setPrefSize(200, 40);
        cbKe.setPromptText("Chọn kệ thuốc");
        cbKe.getStyleClass().add("combo-box");

        VBox boxKe = createFilterBox("Kệ thuốc:", cbKe);

        // --- Dạng điều chế ---
        cbDangDieuChe = new ComboBox<>();
        cbDangDieuChe.setPrefSize(200, 40);
        cbDangDieuChe.setPromptText("Chọn dạng điều chế:");
        cbDangDieuChe.getStyleClass().add("combo-box");

        VBox boxDang = createFilterBox("Dạng điều chế:", cbDangDieuChe);

        // --- Tồn kho ---
        cbTonKho = new ComboBox<>();
        cbTonKho.setPrefSize(200, 40);
        cbTonKho.setPromptText("Chọn tình trạng tồn kho:");
        cbTonKho.getStyleClass().add("combo-box");

        VBox boxTonKho = createFilterBox("Tình trạng tồn kho::", cbTonKho);

        // --- Button Xóa rỗng ---
        btnSearchInvoice1 = new Button("Xoá rỗng");
        btnSearchInvoice1.setPrefSize(93, 40);
        btnSearchInvoice1.getStyleClass().add("btn-xoarong");
        btnSearchInvoice1.setTextFill(Color.WHITE);

        FontAwesomeIcon ref = new FontAwesomeIcon();
        ref.setGlyphName("REFRESH");
        ref.setFill(Color.WHITE);
        btnSearchInvoice1.setGraphic(ref);

        VBox boxXoa = new VBox(5);
        boxXoa.getChildren().addAll(new Text(""), btnSearchInvoice1);

        filterPane.getChildren().addAll(boxKe, boxDang, boxTonKho, boxXoa);

        // ===================== Ô TÌM KIẾM =====================
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        searchNameThuoc = new TextField();
        searchNameThuoc.setPrefSize(300, 40);
        searchNameThuoc.setPromptText("Tìm kiếm thuốc...");
        searchNameThuoc.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-radius: 8px;");

        btnSearchThuoc = new Button();
        btnSearchThuoc.setPrefSize(50, 40);
        btnSearchThuoc.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");
        btnSearchThuoc.setTextFill(Color.WHITE);

        FontAwesomeIcon iconSearch = new FontAwesomeIcon();
        iconSearch.setGlyphName("SEARCH");
        iconSearch.setFill(Color.WHITE);
        btnSearchThuoc.setGraphic(iconSearch);

        searchBox.getChildren().addAll(searchNameThuoc, btnSearchThuoc);

        // ===================== TABLE =====================
        tableThuoc = new TableView<>();
        tableThuoc.setPrefHeight(800);

        colMaThuoc = new TableColumn<>("Mã Thuốc");
        colTenThuoc = new TableColumn<>("Tên thuốc");
        colHamLuong = new TableColumn<>("Hàm lượng");
        colDangDieuChe = new TableColumn<>("Dạng điều chế");
        colGiaBan = new TableColumn<>("Giá bán");
        colTonKho = new TableColumn<>("Tồn kho");
        colKe = new TableColumn<>("Kệ thuốc");

        tableThuoc.getColumns().addAll(
                colMaThuoc, colTenThuoc, colHamLuong, colDangDieuChe, colGiaBan, colTonKho, colKe
        );
        tableThuoc.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button guide = new Button("Nhấn 2 lần chuột trái vào bảng để xem chi tiết");
        guide.getStyleClass().add("pane-huongdan");
        guide.setMaxWidth(Double.MAX_VALUE);
        guide.setPadding(new Insets(10));
        FontAwesomeIcon infoIcon = new FontAwesomeIcon(); infoIcon.setGlyphName("INFO"); infoIcon.setFill(Color.web("#2563eb"));
        guide.setGraphic(infoIcon);

        // ===================== ADD CONTAINER =====================
        root.getChildren().addAll(titleBox, filterPane, searchBox, tableThuoc, guide);

        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        this.setContent(root);
        /** Sự kiện **/
    }

    private VBox createFilterBox(String label, ComboBox<?> cb) {
        Text t = new Text(label);
        t.setFont(Font.font(13));
        t.setFill(Color.web("#374151"));

        VBox box = new VBox(5);
        box.getChildren().addAll(t, cb);
        return box;
    }
}
