//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.phieudat;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.*;
import com.antam.app.entity.*;
import com.antam.app.helper.TuDongGoiY;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;

public class ThemPhieuDatFormController extends DialogPane{
    private TextField txtMa;
    private TextField txtTenKhach;
    private TextField txtSoDienThoai;
    private VBox vbThuoc;
    private ComboBox<Thuoc> cbTenThuoc;
    private ComboBox<DonViTinh> cbDonVi;
    private Spinner<Integer> spSoLuong;
    private TextField txtDonGia;
    private Button btnThem;
    private ComboBox<KhuyenMai> cbKhuyenMai;
    private TableView<ChiTietPhieuDatThuoc> tbChonThuoc;
    private TableColumn<ChiTietPhieuDatThuoc,String> colTenThuoc;
    private TableColumn<ChiTietPhieuDatThuoc,String> colDonVi;
    private TableColumn<ChiTietPhieuDatThuoc,String> colSoLuong;
    private TableColumn<ChiTietPhieuDatThuoc,String> colDonGia;
    private TableColumn<ChiTietPhieuDatThuoc,String> colThanhTien;
    private Text txtTotal;
    private Text txtCanhBaoSDT = new Text();
    private Text txtCanhBaoKM = new Text();

    private Thuoc_DAO thuoc_dao = new Thuoc_DAO();
    private  DonViTinh_DAO donViTinh_dao = new DonViTinh_DAO();
    private ArrayList<Thuoc> dsThuoc;
    private ArrayList<DonViTinh> dsDonViTinh;
    private DecimalFormat decimalFormat = new DecimalFormat("#,### đ");
    private ArrayList<ChiTietPhieuDatThuoc> list = new ArrayList<>();
    private ObservableList<ChiTietPhieuDatThuoc> obsThuoc = FXCollections.observableArrayList();


    KhachHang_DAO khachHangDAO = new KhachHang_DAO();
    ArrayList<KhachHang> dsKhach = khachHangDAO.getAllKhachHang();
    KhuyenMai_DAO KhuyenMai_DAO = new KhuyenMai_DAO();
    ArrayList<KhuyenMai> dsKhuyenMai = (ArrayList<KhuyenMai>) KhuyenMai_DAO.getAllKhuyenMaiConHieuLuc();
    DonViTinh_DAO dvtDAO = new DonViTinh_DAO();
    ChiTietThuoc_DAO chiTietThuoc_dao = new ChiTietThuoc_DAO();
    HoaDon_DAO hoaDon_DAO = new HoaDon_DAO();
    ChiTietThuoc_DAO chiTietThuoc_DAO = new ChiTietThuoc_DAO();
    private ObservableList<KhachHang> autoKhach = FXCollections.observableArrayList(dsKhach);

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

        HBox hbSDT = new HBox();
        Text lblSoDienThoai = new Text("Số điện thoại:");
        lblSoDienThoai.setFill(Color.web("#374151"));
        // Cảnh báo
        txtCanhBaoSDT.setFill(Color.RED);

        // ngăn chặn nhảy lệch
        TextFlow warnFlow = new TextFlow(txtCanhBaoSDT);
        warnFlow.setMinWidth(150);
        warnFlow.setMaxWidth(150);

        // Tạo nhóm label + cảnh báo
        hbSDT.getChildren().add(lblSoDienThoai);
        hbSDT.getChildren().add(warnFlow);

        // Đặt vào GridPane
        GridPane.setColumnIndex(hbSDT, 1);
        GridPane.setRowIndex(hbSDT, 2);
        // TextField nhập SDT
        txtSoDienThoai = new TextField();
        txtSoDienThoai.getStyleClass().add("text-field");
        // Đặt TextField vào GridPane
        GridPane.setColumnIndex(txtSoDienThoai, 1);
        GridPane.setRowIndex(txtSoDienThoai, 3);


        Text lblThuocDat = new Text("Thuốc đặt:");
        lblThuocDat.setFill(javafx.scene.paint.Color.web("#374151"));
        GridPane.setRowIndex(lblThuocDat, 4);

        gridTop.getChildren().addAll(lblMa, txtMa, lblTenKhach, txtTenKhach, hbSDT, txtSoDienThoai, lblThuocDat);

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

        HBox hbCanhBaoKM = new HBox();
        txtCanhBaoKM.setFill(Color.RED);
        Text lblKM = new Text("Áp dụng khuyến mãi:");

        hbCanhBaoKM.getChildren().addAll(lblKM, txtCanhBaoKM);
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
                hbCanhBaoKM,
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

        //tạo cổng kết nối
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //setup phụ
        txtDonGia.setEditable(false);
        cbDonVi.setDisable(true);

        //load ds đơn vị tính.
        dsDonViTinh = donViTinh_dao.getTatCaDonViTinh();
        dsThuoc = thuoc_dao.getAllThuoc();
        cbDonVi.getItems().addAll(FXCollections.observableArrayList(dsDonViTinh));

        // load comboBox thuốc
        cbTenThuoc.getItems().addAll(FXCollections.observableArrayList(dsThuoc));

        // setup mã phiếu
        txtMa.setText(getHashPD());
        txtMa.setEditable(false);


        // load ComboBox Khuyến mãi.
        KhuyenMai nothing = new KhuyenMai("None","Không áp dụng");
        cbKhuyenMai.getItems().add(nothing);
        cbKhuyenMai.getItems().addAll(FXCollections.observableArrayList(dsKhuyenMai));

        //gọi tính tổng tiền khi chạy giao diện
        loadTongTien();

        //setup table
        setupTable();
        tbChonThuoc.setItems(obsThuoc);
        loadTable();

        //
        Button btnApply = (Button)this.lookupButton(applyButton);
        Button btnCancel = (Button)this.lookupButton(cancelButton);

        //sự kiện thêm phiếu đặt
        btnApply.addEventFilter(ActionEvent.ACTION, event -> {
            if (checkDuLieu()) {
                themPhieuDat();
            } else {
                event.consume();
            }
        });


        //thêm thuốc vào table
        btnThem.setOnAction(  e ->   {
            if (approveThuoc()){
                addThuocVaoTable();
                loadTongTien();
            }
        });

        //sự kiện khi thay đổi comboBox khuyến mãi
        cbKhuyenMai.setOnAction(e -> {
            KhuyenMai km = cbKhuyenMai.getSelectionModel().getSelectedItem();
            // Không áp dụng hoặc chọn null
            if (km == null || km.getTenKM().equals("Không áp dụng")) {
                txtCanhBaoKM.setText("");
                loadTongTien();
                return;
            }
            LocalDate today = LocalDate.now();
            // Chưa đến ngày bắt đầu
            if (km.getNgayBatDau().isAfter(today)) {
                txtCanhBaoKM.setText("Khuyến mãi chưa bắt đầu");
            }
            // Đã hết hạn
            else if (km.getNgayKetThuc().isBefore(today)) {
                txtCanhBaoKM.setText("Khuyến mãi đã hết hạn");
            }
            // Hợp lệ
            else {
                txtCanhBaoKM.setText("");
            }
            loadTongTien();
        });

        //setup spinner số lượng
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1);
        spSoLuong.setValueFactory(valueFactory);
        spSoLuong.setEditable(true);

        //sự kiện khi thay đổi comboBox thuốc
        cbTenThuoc.setOnAction(e -> {

            if (cbTenThuoc.getSelectionModel().getSelectedItem() == null) {
                return;
            }

            for (DonViTinh dvt : cbDonVi.getItems()) {
                if (dvt.getMaDVT() == cbTenThuoc.getSelectionModel().getSelectedItem().getMaDVTCoSo().getMaDVT() ) {
                    cbDonVi.getSelectionModel().select(dvt);
                    break;
                }
            }

            txtDonGia.setText(dinhDangTien(cbTenThuoc.getSelectionModel().getSelectedItem().getGiaBan()));
        });

        //sự kiện thay đổi số điện thoại
        txtSoDienThoai.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.isBlank()) {
                txtCanhBaoSDT.setText("");
                return;
            }

            if (!newValue.matches("^0[35679]\\d{8}$")) {
                txtCanhBaoSDT.setText("Số điện thoại không hợp lệ");
            } else {
                txtCanhBaoSDT.setText("");
            }
        });


        //sự kiện autocomplete khách hàng
        TuDongGoiY.goiYKhach(txtTenKhach, txtSoDienThoai, autoKhach);

        //sự kiện xóa thuốc khỏi bảng
        tbChonThuoc.setRowFactory( tv -> {
            TableRow<ChiTietPhieuDatThuoc> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteItem = new MenuItem("Xóa thuốc khỏi bảng");
            deleteItem.setOnAction( event -> {
                ChiTietPhieuDatThuoc selectedItem = row.getItem();
                list.remove(selectedItem);
                loadTable();
                loadTongTien();
            });
            contextMenu.getItems().add(deleteItem);
            // Chỉ hiển thị menu khi có dữ liệu
            row.contextMenuProperty().bind(
                    javafx.beans.binding.Bindings.when(row.emptyProperty())
                            .then((ContextMenu)null)
                            .otherwise(contextMenu)
            );
            return row;
        });
    }


    /**
     * Duyệt thông tin thuốc trước khi thêm vào bảng
     * @return true - hợp lệ, false - không hợp lệ
     */
    private boolean approveThuoc() {

        Thuoc selectedThuoc = cbTenThuoc.getSelectionModel().getSelectedItem();
        //gọi integer để có thể sử dụng null trong điều kiện
        Integer soLuongNhap = spSoLuong.getValue();

        // Nếu người dùng chưa chọn thuốc hoặc chọn rỗng
        if (selectedThuoc == null) {
            showMess("Thiếu thông tin", "Vui lòng chọn thuốc.");
            return false;
        }
        // Số lượng không hợp lệ
        if (soLuongNhap == null || soLuongNhap <= 0) {
            showMess("Số lượng sai", "Số lượng thuốc phải lớn hơn 0.");
            return false;
        }

        // Lấy chi tiết thuốc trong kho
        ArrayList<ChiTietThuoc> dsChiTiet =
                chiTietThuoc_dao.getAllCHiTietThuocTheoMaThuoc(selectedThuoc.getMaThuoc());
        if (dsChiTiet.isEmpty()) {
            showMess("Hết hàng", "Thuốc \"" + selectedThuoc.getTenThuoc() + "\" hiện không có trong kho.");
            return false;
        }

        int tongSoLuongTrongKho = dsChiTiet.stream()
                .mapToInt(ChiTietThuoc::getSoLuong)
                .sum();
        // So sánh tồn kho với số lượng nhập

        int soLuongdaChon = 0;
        for (ChiTietPhieuDatThuoc ct : tbChonThuoc.getItems()) {
            if (ct.getSoDangKy().getMaThuoc().equals(selectedThuoc.getMaThuoc())) {
                soLuongdaChon += ct.getSoLuong();
            }
        }

        if ( (soLuongNhap + soLuongdaChon) > tongSoLuongTrongKho) {
            showMess("Không đủ số lượng",
                    "đơn vị của thuốc " + selectedThuoc.getTenThuoc() +
                            " Kho chỉ còn \"" + (tongSoLuongTrongKho -soLuongdaChon)+ "\".");
            return false;
        }
        return true;
    }

    /**
     * Load lại bảng thuốc
     */
    private void loadTable() {
        obsThuoc.setAll(list);
    }

    /**
     * Thêm thuốc vào bảng nếu đã tồn tại thì cộng dồn số lượng
     */
    private void addThuocVaoTable() {
        ChiTietPhieuDatThuoc ctPDT = new ChiTietPhieuDatThuoc(cbTenThuoc.getSelectionModel().getSelectedItem(),
                spSoLuong.getValue(),
                cbDonVi.getSelectionModel().getSelectedItem());
        txtDonGia.setText(dinhDangTien(ctPDT.getSoDangKy().getGiaBan()));
        if (list.contains(ctPDT)){
            int index = list.indexOf(ctPDT);
            ChiTietPhieuDatThuoc existingCTPDT = list.get(index);
            existingCTPDT.setSoLuong(existingCTPDT.getSoLuong() + ctPDT.getSoLuong());
            list.set(index, existingCTPDT);
            loadTable();
            return;
        }
        list.add(ctPDT);
        loadTable();
        loadTongTien();
    }

    private void themPhieuDat() {
        // Kiểm tra nhân viên
        String hashPD = getHashPD();
        NhanVien nguoiDat = PhienNguoiDung.getMaNV();

        if (nguoiDat == null) {
            showMess("Lỗi người dùng", "Không tìm thấy thông tin người đặt.");
            return;
        }

        // Lấy thông tin khách nhập vào
        String ten = txtTenKhach.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();
        if (ten.isEmpty() || sdt.isEmpty()) {
            showMess("Lỗi khách hàng", "Vui lòng nhập đầy đủ tên và số điện thoại khách hàng.");
            return;
        }

        // ===== XÁC ĐỊNH KHÁCH HÀNG =====

        KhachHang khach;

        if (isKhachHangMoi()) {
            //khách mới
            khach = new KhachHang(getMaKhachMoi());
            khach.setTenKH(ten);
            khach.setSoDienThoai(sdt);

            khachHangDAO.insertKhachHang(khach);

        } else {
            //khách cũ: tìm theo SĐT
            khach = dsKhach.stream()
                    .filter(k -> k.getSoDienThoai().equals(sdt))
                    .findFirst()
                    .orElse(null);

            if (khach == null) {
                showMess("Lỗi khách hàng", "Không tìm thấy thông tin khách hàng cũ.");
                return;
            }
        }

        // ===== KIỂM TRA BẢNG THUỐC =====
        if (tbChonThuoc.getItems().isEmpty()) {
            showMess("Lỗi dữ liệu", "Vui lòng chọn ít nhất một loại thuốc.");
            return;
        }

        // ===== XỬ LÝ KHUYẾN MÃI =====
        KhuyenMai km = cbKhuyenMai.getSelectionModel().getSelectedItem();
        if (km != null && km.getTenKM().equals("Không áp dụng")) {
            km = null;
        }

        // ===== TÍNH TỔNG TIỀN =====
        double tongTien = tinhTongTien();

        // ===== TẠO PHIẾU ĐẶT =====
        PhieuDatThuoc phieu = new PhieuDatThuoc(
                hashPD,
                LocalDate.now(),
                false,
                nguoiDat,
                khach,
                km,
                tongTien
        );

        PhieuDat_DAO.themPhieuDatThuocVaoDBS(phieu);

        // ===== TẠO CHI TIẾT =====
        for (ChiTietPhieuDatThuoc ct : tbChonThuoc.getItems()) {
            ChiTietPhieuDat_DAO.themChiTietPhieuDatVaoDBS(
                    new ChiTietPhieuDatThuoc(phieu, ct.getSoDangKy(), ct.getSoLuong(), ct.getDonViTinh())
            );
        }
    }

    /**
     * Tạo mã khách hàng mới với đinh dạng KHxxxxxxxxx (x là số bất kì, có 9 số)
     * @return
     */
    private String getMaKhachMoi() {
        int newNum = khachHangDAO. getMaxHash() +1;
        return String.format("KH%09d", newNum);
    }


    /**
     * Kiểm tra khách hàng đã tồn tại chưa
     * @return true - khách mới. false - khách đã tồn tại.
     */
    private boolean isKhachHangMoi() {
        String sdt = txtSoDienThoai.getText().trim();
        if (sdt == null || sdt.isEmpty()) return true;

        for (KhachHang kh : dsKhach) {
            if (kh.getSoDienThoai().equals(sdt)) {
                return false;
            }
        }
        return true;
    }


    /**
     * Tạo mã phiếu đặt mới chưa tồn tại trong dbs
     * @return String - mã phiếu đặt mới
     */
    private String getHashPD() {
        String hash = PhieuDat_DAO.getMaxHash();
        if (hash == null){
            return "";
        }else{
            int soThuTu = Integer.parseInt(hash) + 1;
            return String.format("PDT%03d", soThuTu);
        }
    }

    /**
     * Tính tổng tiền của phiếu đặt áp dụng khuyến mãi (nếu có)
     * @return double - tổng tiền sau khi áp dụng khuyến mãi (nếu có)
     */
    public double tinhTongTien(){
        double tongTien = 0.0;
        for (ChiTietPhieuDatThuoc e : tbChonThuoc.getItems()){
            tongTien += e.getSoLuong() * e.getSoDangKy().getGiaBan();
        }
        // Áp dụng khuyến mãi nếu có
        if (cbKhuyenMai.getSelectionModel().getSelectedItem() != null &&
                !cbKhuyenMai.getSelectionModel().getSelectedItem().getTenKM().equals("Không áp dụng")) {
            if (!txtCanhBaoKM.getText().isEmpty()) {
                return tongTien;
            }
            KhuyenMai khuyenMai = cbKhuyenMai.getSelectionModel().getSelectedItem();
            LoaiKhuyenMai loaiKM = khuyenMai.getLoaiKhuyenMai();
            int soDaSuDung = hoaDon_DAO.soHoaDonDaCoKhuyenMaiVoiMa(khuyenMai.getMaKM());
//            System.out.println("Số hóa đơn: "+soDaSuDung);
            if (soDaSuDung >= khuyenMai.getSoLuongToiDa()) {
                return tongTien > 0? tongTien : 0;
            }else{
                if(loaiKM.getMaLKM() == 1){
                    tongTien = tongTien * (1 - khuyenMai.getSo()/100);
                }else {
                    tongTien = tongTien - khuyenMai.getSo();
                }
            }
        }

        return tongTien > 0 ? tongTien : 0;
    }

    public void showMess(String tieuDe, String vanBan){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tieuDe);
        alert.setContentText(vanBan);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    /**
     * Cập nhật tổng tiền hiển thị trên giao diện
     */
    private void loadTongTien(){
        double tongTien = tinhTongTien();
        txtTotal.setText("Tổng tiền: " + decimalFormat.format(tongTien));
    }
    private boolean checkDuLieu() {
        if (txtTenKhach.getText().trim().isEmpty()) {
            showMess("Thiếu thông tin", "Vui lòng nhập tên khách hàng.");
            txtTenKhach.requestFocus();
            return false;
        }
        if (txtSoDienThoai.getText().trim().isEmpty()){
            showMess("Thiếu thông tin số điện thọai khách hàng", "Vui lòng nhập số điện thoại khách hàng.");
            txtSoDienThoai.requestFocus();;
            return false;
        }
        if (!txtSoDienThoai.getText().matches("^0[35679]\\d{8}$")){
            showMess("Thông tin số điện thọai khách hàng không hợp lệ", "Số điện thoại khách hàng phải có 10 số và bắt đầu với 03, 05, 06, 07, 09.");
            txtSoDienThoai.requestFocus();;
            return false;
        }
        if (tbChonThuoc.getItems().isEmpty()) {
            showMess("Thiếu thông tin", "Vui lòng thêm thuốc vào phiếu đặt.");
            cbTenThuoc.requestFocus();
            return false;
        }
        if (txtTotal.getText().equals("Tổng tiền: 0 đ")) {
            showMess("Tổng tiền bằng 0", "Giá trị tổng tiền không hợp lệ.");
            return false;
        }
        return true;
    }

    /**
     * hỗ trợ định dạng tiền
     * @param tien
     * @return String - tiền đã được định dạng
     */
    public String dinhDangTien(double tien){
        DecimalFormat df = new DecimalFormat("#,###đ");
        return df.format(tien);
    }

    /**
     * Cài đặt các cột trong bảng
     */
    private void setupTable(){
        colTenThuoc.setCellValueFactory( cellData -> new SimpleStringProperty(cellData.getValue().getSoDangKy().getTenThuoc()));
        colDonVi.setCellValueFactory( cellData -> new SimpleStringProperty(cellData.getValue().getSoDangKy().getMaDVTCoSo().getTenDVT()));
        colSoLuong.setCellValueFactory( cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getSoLuong())));
        colDonGia.setCellValueFactory( cellData -> new SimpleStringProperty(dinhDangTien(cellData.getValue().getSoDangKy().getGiaBan())));
        colThanhTien.setCellValueFactory( cellData -> {
            double thanhTien = cellData.getValue().getSoLuong() * cellData.getValue().getSoDangKy().getGiaBan();
            return new SimpleStringProperty(dinhDangTien(thanhTien));
        } );
    }
}
