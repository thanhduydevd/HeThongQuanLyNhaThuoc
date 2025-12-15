//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.phieudat;

import com.antam.app.dao.NhanVien_DAO;
import com.antam.app.dao.PhieuDat_DAO;
import com.antam.app.entity.NhanVien;
import com.antam.app.entity.PhieuDatThuoc;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class KhoiPhucPhieuDatController extends ScrollPane{

    private Button btnKhoiPhuc;

    private ComboBox<NhanVien> cbNhanVien = new ComboBox<>();

    private ComboBox<String> cbTrangThai = new ComboBox<>();
    private ComboBox<String> cbGia = new ComboBox<>();

    private DatePicker dpstart = new DatePicker();
    private DatePicker dpend = new DatePicker();

    private TextField txtFind = new TextField();

    private Button btnFind = new Button();
    private Button btnXoaRong = new Button();

    private TableView<PhieuDatThuoc> tvPhieuDat = new TableView<>();

    private TableColumn<PhieuDatThuoc,String> colMaPhieu = new TableColumn<>("Mã phiếu");
    private TableColumn<PhieuDatThuoc,String> colNgay = new TableColumn<>("Ngày tạo");
    private TableColumn<PhieuDatThuoc,String> colKhach = new TableColumn<>("Khách hàng");
    private TableColumn<PhieuDatThuoc,String> colSDT = new TableColumn<>("SĐT");
    private TableColumn<PhieuDatThuoc,String> colNhanVien = new TableColumn<>("Nhân viên");
    private TableColumn<PhieuDatThuoc,String> colStatus = new TableColumn<>("Trạng thái");
    private TableColumn<PhieuDatThuoc,String> colTotal = new TableColumn<>("Tổng tiền");

    public static PhieuDatThuoc selectedPDT;

    ArrayList<PhieuDatThuoc> listPDT = PhieuDat_DAO.getAllPhieuDatThuocFromDBS();
    ArrayList<NhanVien> listNV = NhanVien_DAO.getDsNhanVienformDBS();
    ObservableList<PhieuDatThuoc> origin;
    ObservableList<PhieuDatThuoc> filter= FXCollections.observableArrayList();

    public KhoiPhucPhieuDatController() {
        this.setFitToHeight(true);
        this.setFitToWidth(true);
        this.setPrefSize(900, 730);

        AnchorPane.setTopAnchor(this, 0.0);
        AnchorPane.setBottomAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, 0.0);
        AnchorPane.setRightAnchor(this, 0.0);

        VBox mainVBox = new VBox(30);
        mainVBox.setStyle("-fx-background-color: #f8fafc;");
        mainVBox.setPadding(new Insets(20));

        // Header
        HBox header = new HBox(5);
        header.setAlignment(Pos.CENTER_LEFT);

        Text title = new Text("Khôi phục phiếu đặt");
        title.setFill(Color.web("#1e3a8a"));
        title.setFont(Font.font("System",30));

        Pane spacer = new Pane();

        FontAwesomeIcon iconAdd = new FontAwesomeIcon();
        iconAdd.setFill(Color.WHITE);
        iconAdd.setIcon(FontAwesomeIcons.BACKWARD);
        btnKhoiPhuc = new Button("Khôi phục");
        btnKhoiPhuc.setGraphic(iconAdd);
        btnKhoiPhuc.getStyleClass().add("btn-khoiphuc");

        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(title, spacer, btnKhoiPhuc);

        // Filters
        FlowPane filters = new FlowPane(5, 5);
        filters.setStyle("-fx-background-color: white;");
        filters.getStyleClass().add("box-pane");
        filters.setPadding(new Insets(10));

        DropShadow ds = new DropShadow();
        ds.setBlurType(BlurType.GAUSSIAN);
        ds.setHeight(44.45);
        ds.setWidth(35.66);
        ds.setOffsetX(3);
        ds.setOffsetY(2);
        ds.setRadius(19.53);
        ds.setColor(Color.rgb(211, 211, 211));
        filters.setEffect(ds);

        filters.getChildren().add(createFilterVBox("Nhân viên:", cbNhanVien));
        filters.getChildren().add(createFilterVBox("Trạng thái:", cbTrangThai));
        filters.getChildren().add(createFilterVBox("Từ ngày:", dpstart));
        filters.getChildren().add(createFilterVBox("Đến ngày:", dpend));
        filters.getChildren().add(createFilterVBox("Khoảng giá:", cbGia));

        // --- Button Xoá rỗng ---
        VBox vbReset = new VBox(5);
        Text emptyText = new Text("");
        emptyText.setFont(Font.font(13));

        btnXoaRong.setText("Xoá rỗng");
        btnXoaRong.setPrefSize(93, 40);
        btnXoaRong.setTextFill(Color.WHITE);
        btnXoaRong.getStyleClass().add("btn-xoarong");
        FontAwesomeIcon refreshIcon = new FontAwesomeIcon();
        refreshIcon.setIcon(FontAwesomeIcons.REFRESH);
        refreshIcon.setFill(Color.WHITE);
        btnXoaRong.setGraphic(refreshIcon);

        vbReset.getChildren().addAll(emptyText, btnXoaRong);
        filters.getChildren().add(vbReset);

        // --- Search box ---
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        txtFind.setPromptText("Tìm kiếm mã phiếu đặt");
        txtFind.setPrefSize(300, 40);
        txtFind.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-radius: 8px;");

        btnFind.setPrefSize(50, 40);
        btnFind.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 5px;");
        btnFind.setTextFill(Color.WHITE);
        FontAwesomeIcon searchIcon = new FontAwesomeIcon(); searchIcon.setGlyphName("SEARCH");
        searchIcon.setFill(Color.WHITE);
        btnFind.setGraphic(searchIcon);

        searchBox.getChildren().addAll(txtFind, btnFind);

        // --- Table ---
        VBox tableWrapper = new VBox(5);
        tvPhieuDat.setPrefSize(200, 500);

        tvPhieuDat.getColumns().addAll(
                colMaPhieu, colNgay, colKhach, colSDT,
                colNhanVien, colStatus, colTotal
        );

        tvPhieuDat.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button hint = new Button("Nhấn 2 lần chuột trái vào bảng để xem chi tiết");
        hint.setMaxWidth(Double.MAX_VALUE);
        hint.getStyleClass().add("pane-huongdan");
        hint.setTextFill(Color.web("#2563eb"));
        hint.setPadding(new Insets(10));
        FontAwesomeIcon infoIcon = new FontAwesomeIcon(); infoIcon.setGlyphName("INFO");
        infoIcon.setFill(Color.web("#2563eb"));
        hint.setGraphic(infoIcon);

        tableWrapper.getChildren().addAll(tvPhieuDat, hint);

        // Add tất cả vào giao diện
        mainVBox.getChildren().addAll(header, filters, searchBox, tableWrapper);

        this.getStylesheets().add(getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm());
        this.setContent(mainVBox);
        /** Sự kiện **/
    }

    private VBox createFilterVBox(String label, Control control) {
        VBox vb = new VBox(5);

        Text txt = new Text(label);
        txt.setFill(Color.web("#374151"));
        txt.setFont(Font.font(13));

        control.setPrefSize(200, 40);
        control.getStyleClass().add("combo-box");

        vb.getChildren().addAll(txt, control);
        return vb;
    }
}
