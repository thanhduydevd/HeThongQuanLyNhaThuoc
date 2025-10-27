//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.hoadon;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.*;
import com.antam.app.entity.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static javafx.collections.FXCollections.observableArrayList;

public class DoiThuocFormController {
    @FXML
    private DialogPane dialogPane;

    @FXML
    private VBox vhDSCTHD, vhDSCTHDM;
    @FXML
    private TextField txtMaHoaDonDoi, txtKhachHangDoi;
    @FXML
    private Text txtTongTienDoi, txtTongTienTra, txtTongTienMua, txtThongBaoDoi;
    @FXML
    private Button btnThemMoiThuoc;
    @FXML
    private ComboBox<String> cbLyDoDoi;

    private Thuoc_DAO thuoc_dao = new Thuoc_DAO();
    private HoaDon_DAO hoaDon_dao = new HoaDon_DAO();
    private KhachHang_DAO khachHang_dao = new KhachHang_DAO();
    private ChiTietHoaDon_DAO chiTietHoaDon_dao = new ChiTietHoaDon_DAO();
    private ChiTietThuoc_DAO chiTietThuoc_dao = new ChiTietThuoc_DAO();
    private DonViTinh_DAO donViTinh_dao = new DonViTinh_DAO();
    private KhuyenMai_DAO khuyenMai_dao = new KhuyenMai_DAO();
    private HoaDon hoaDon;
    private ArrayList<ChiTietHoaDon> selectedItems = new ArrayList<>();
    private ArrayList<ChiTietHoaDon> chiTietHoaDons;
    private int soLuongThuoc = 0;
    private int soLuongThuocDoi = 0;

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void showData(HoaDon hoaDon) {
        // Kết nối DB
        try { Connection con = ConnectDB.getInstance().connect(); }
        catch (SQLException e) { throw new RuntimeException(e); }
        HoaDon hd = hoaDon_dao.getHoaDonTheoMa(hoaDon.getMaHD());
        chiTietHoaDons = chiTietHoaDon_dao.getAllChiTietHoaDonTheoMaHD(hoaDon.getMaHD());
        txtMaHoaDonDoi.setText(hd.getMaHD());
        txtKhachHangDoi.setText(khachHang_dao.getKhachHangTheoMa(hd.getMaKH().getMaKH()).getTenKH());

        for (ChiTietHoaDon ct : chiTietHoaDons) {
            HBox hBox = renderChiTietHoaDon(ct);
            vhDSCTHD.getChildren().add(hBox);
            soLuongThuoc += 1;
        }

        txtTongTienTra.setText("0 đ");
        txtTongTienMua.setText("0 đ");
        txtTongTienDoi.setText("0 đ");
    }

    public void initialize() {
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Xác nhận đổi thuốc", ButtonData.APPLY);
        this.dialogPane.getButtonTypes().add(cancelButton);
        this.dialogPane.getButtonTypes().add(applyButton);

        // Kết nối DB
        try { Connection con = ConnectDB.getInstance().connect(); }
        catch (SQLException e) { throw new RuntimeException(e); }

        Button btnApply = (Button) this.dialogPane.lookupButton(applyButton);
        btnApply.addEventFilter(ActionEvent.ACTION, event -> {
            String lyDo = cbLyDoDoi.getValue();
            if (lyDo == null || lyDo.trim().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Chưa chọn lý do trả thuốc");
                alert.setContentText("Vui lòng chọn lý do trả trước khi xác nhận.");
                alert.showAndWait();
                event.consume();
                return;
            }
            if (selectedItems.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Chưa chọn thuốc để đổi");
                alert.setContentText("Vui lòng chọn ít nhất một thuốc để đổi.");
                alert.showAndWait();
                event.consume();
            }
            if (vhDSCTHDM.getChildren().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Chưa thêm thuốc mới để đổi");
                alert.setContentText("Vui lòng thêm ít nhất một thuốc mới để đổi.");
                alert.showAndWait();
                event.consume();
            }
            if (!checkThongTinThuoc()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Thông tin thuốc mới không hợp lệ");
                alert.setContentText("Vui lòng kiểm tra lại thông tin thuốc mới.");
                alert.showAndWait();
                event.consume();
            }
            else {
                for (ChiTietHoaDon ct : selectedItems) {
                    chiTietHoaDon_dao.xoaMemChiTietHoaDon(ct.getMaHD().getMaHD(), ct.getMaCTT().getMaCTT(), "Trả Khi Đổi");
                    switch (lyDo) {
                        // Các lý do KHÔNG cộng lại vào kho
                        case "Hết hạn sử dụng":
                        case "Bao bì bị hư hỏng":
                        case "Thuốc lỗi / hư hỏng":
                        case "Thuốc bị thu hồi":
                            // Không làm gì cả
                            break;

                        // Các lý do CÓ cộng lại vào kho
                        case "Khách hàng đổi ý":
                        case "Nhập nhầm lô / dư":
                        case "Sai thông tin đơn / bảo hiểm":
                            Thuoc t = thuoc_dao.getThuocTheoMa(
                                    chiTietThuoc_dao
                                            .getChiTietThuoc(ct.getMaCTT().getMaCTT())
                                            .getMaThuoc()
                                            .getMaThuoc()
                            );
                            chiTietThuoc_dao.CapNhatSoLuongChiTietThuoc(
                                    ct.getMaCTT().getMaCTT(),
                                    ct.getSoLuong()
                            );
                            break;

                        default:
                            // Nếu có giá trị không nằm trong danh sách trên
                            System.out.println("Lý do trả không hợp lệ: " + lyDo);
                            break;
                    }
                }
                for (Node node : vhDSCTHDM.getChildren()) {
                    if (node instanceof HBox hBox) {
                        VBox vbThuoc = (VBox) hBox.getChildren().get(0);
                        VBox vbDVT = (VBox) hBox.getChildren().get(1);
                        VBox vbSoLuong = (VBox) hBox.getChildren().get(2);

                        ComboBox<Thuoc> comboThuoc = (ComboBox<Thuoc>) vbThuoc.getChildren().get(1);
                        ComboBox<DonViTinh> comboDonVi = (ComboBox<DonViTinh>) vbDVT.getChildren().get(1);
                        Spinner<Integer> spinnerSoLuong = (Spinner<Integer>) vbSoLuong.getChildren().get(1);
                        if (comboThuoc.getValue() != null && comboDonVi.getValue() != null && spinnerSoLuong.getValue() != null) {
                            Thuoc t = comboThuoc.getValue();
                            int soLuong = spinnerSoLuong.getValue();
                            ArrayList<ChiTietThuoc> listCTT = chiTietThuoc_dao.getChiTietThuocHanSuDungGiamDan(t.getMaThuoc());
                            int tongSoLuong = 0;
                            double tongTienMua = 0;
                            for (ChiTietThuoc cts : listCTT) {
                                tongSoLuong += cts.getSoLuong();
                            }
                            if (tongSoLuong < soLuong) {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Cảnh báo");
                                alert.setHeaderText("Số lượng thuốc trong kho không đủ");
                                alert.setContentText("Vui lòng kiểm tra lại số lượng thuốc mới.");
                                alert.showAndWait();
                                event.consume();
                                return;
                            }else{
                                for (ChiTietThuoc ctt : listCTT) {
                                    if (ctt.getSoLuong() >= soLuong) {
                                        ChiTietHoaDon newCTHD = new ChiTietHoaDon(
                                                hoaDon,
                                                ctt,
                                                soLuong,
                                                comboDonVi.getValue(),
                                                "Thuốc Mới Khi Đổi",
                                                Math.round(t.getGiaBan() * soLuong * (1 + t.getThue()) * 100.0) / 100.0
                                        );
                                        chiTietHoaDon_dao.themChiTietHoaDon(newCTHD);
                                        chiTietThuoc_dao.CapNhatSoLuongChiTietThuoc(ctt.getMaCTT(), -soLuong);
                                        tongTienMua += newCTHD.getThanhTien();
                                        break;
                                    }else{
                                        soLuong -= ctt.getSoLuong();
                                        ChiTietHoaDon newCTHD = new ChiTietHoaDon(
                                                hoaDon,
                                                ctt,
                                                ctt.getSoLuong(),
                                                comboDonVi.getValue(),
                                                "Thuốc Mới Khi Đổi",
                                                Math.round(t.getGiaBan() * ctt.getSoLuong() * (1 + t.getThue()) * 100.0) / 100.0
                                        );
                                        chiTietHoaDon_dao.themChiTietHoaDon(newCTHD);
                                        chiTietThuoc_dao.CapNhatSoLuongChiTietThuoc(ctt.getMaCTT(), -ctt.getSoLuong());
                                        tongTienMua += newCTHD.getThanhTien();
                                    }
                                }
                            }
                            double tongTienCu = hoaDon.getTongTien();
                            double tongTienTra = 0;
                            double tongTienCoKM = 0;
                            for (ChiTietHoaDon ct : selectedItems) {
                                if (!ct.getTinhTrang().equals("Thuốc Mới Khi Đổi")){
                                    tongTienCoKM += ct.getThanhTien();
                                }else{
                                    tongTienTra += ct.getThanhTien();
                                }
                            }
                            if (khuyenMai_dao.getKhuyenMaiTheoMa(hoaDon.getMaKM().getMaKM()) != null) {
                                tongTienCoKM = TinhTienKhuyenMai(tongTienCoKM, khuyenMai_dao.getKhuyenMaiTheoMa(hoaDon.getMaKM().getMaKM()).getSo());
                            }
                            hoaDon_dao.CapNhatTongTienHoaDon(hoaDon.getMaHD(), tongTienCu - tongTienCoKM - tongTienTra + tongTienMua);
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Cảnh báo");
                            alert.setHeaderText("Thông tin thuốc không hợp lệ");
                            alert.setContentText("Vui lòng kiểm tra lại thông tin thuốc mới.");
                            alert.showAndWait();
                            event.consume();
                            return;
                        }

                    }
                }

            }
        });
        // Them gia tri cho combobox ly do
        addValueCombobox();
        // Nút thêm mới thuốc
        btnThemMoiThuoc.setOnAction(e -> {
            renderChiTietHoaDonDoi(vhDSCTHDM);
        });
    }

    public void addValueCombobox(){
        ObservableList<String> lyDoList = observableArrayList(
                "Hết hạn sử dụng",
                "Bao bì bị hư hỏng",
                "Khách hàng đổi ý",
                "Thuốc lỗi / hư hỏng",
                "Nhập nhầm lô / dư",
                "Thuốc bị thu hồi",
                "Sai thông tin đơn / bảo hiểm"
        );
        cbLyDoDoi.setItems(lyDoList);
    }

    public HBox renderChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
        if (chiTietHoaDon == null) {
            HBox h = new HBox();
            h.setVisible(false);
            h.setManaged(false);
            return h;
        }
        if (chiTietHoaDon.getTinhTrang().equals("Trả") || chiTietHoaDon.getTinhTrang().equals("Trả Khi Đổi")) {
            HBox h = new HBox();
            h.setVisible(false);
            h.setManaged(false);
            return h;
        }
        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setStyle(
                "-fx-background-color: #f8fafc;" +
                "-fx-padding: 10;"
        );

        CheckBox checkBox = new CheckBox();
        checkBox.setOnAction(event -> {
            if (chiTietHoaDon.getTinhTrang().equals("Mua")) {
                soLuongThuocDoi += checkBox.isSelected() ? 1 : -1;
            }
            if (checkBox.isSelected()) {
                selectedItems.add(chiTietHoaDon);
            } else {
                selectedItems.remove(chiTietHoaDon);
            }
            tinhTongTien();
        });

        // Kết nối DB
        try { Connection con = ConnectDB.getInstance().connect(); }
        catch (SQLException e) { throw new RuntimeException(e); }
        ChiTietThuoc ctt = chiTietThuoc_dao.getChiTietThuoc(chiTietHoaDon.getMaCTT().getMaCTT());
        Thuoc t = thuoc_dao.getThuocTheoMa(ctt.getMaThuoc().getMaThuoc());
        Text txtMaThuoc = new Text(t.getTenThuoc());
        txtMaThuoc.setStyle("-fx-font-size: 15px;");
        Text txtSoLuong = new Text("SL " + chiTietHoaDon.getSoLuong());
        txtSoLuong.setStyle("-fx-font-size: 15px;");
        DecimalFormat df = new DecimalFormat("#,### đ");
        Text txtDonGia = new Text(df.format(chiTietHoaDon.getThanhTien()));
        txtDonGia.setStyle("-fx-font-size: 15px;");
        String valueBtn = "Bình thường";
        if (chiTietHoaDon.getTinhTrang().equals("Trả")) {
            valueBtn = "Đã trả";
        } else if (chiTietHoaDon.getTinhTrang().equals("Trả Khi Đổi")) {
            valueBtn = "Đã đổi";
        } else if (chiTietHoaDon.getTinhTrang().equals("Thuốc Mới Khi Đổi")){
            valueBtn = "Thuốc đổi";
        }
        Button btn = new Button(valueBtn);
        btn.setStyle(
                "-fx-background-color: #e0f2fe;" +
                "-fx-text-fill: #0369a1;" +
                "-fx-background-radius: 10;" +
                "-fx-font-size: 12px;"
        );

        hBox.getChildren().addAll(checkBox, txtMaThuoc, txtSoLuong, txtDonGia, btn);
        return hBox;
    }


    public void renderChiTietHoaDonDoi(VBox vbox) {
        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setStyle(
                "-fx-background-color: #f8fafc;" +
                "-fx-padding: 10;"
        );
        hBox.setAlignment(Pos.CENTER);
        VBox vbThuoc = new VBox();
        ComboBox<Thuoc> comboBoxThuoc = new ComboBox<>();
        comboBoxThuoc.getStylesheets().add(
                getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm()
        );
        comboBoxThuoc.setPromptText("Chọn thuốc");
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ArrayList<Thuoc> thuocs = thuoc_dao.getAllThuoc();
        for (Thuoc t : thuocs) {
            comboBoxThuoc.getItems().add(t);
        }
        VBox vbDVT = new VBox();
        ComboBox<DonViTinh> comboBoxDVT = new ComboBox<>();
        comboBoxDVT.getStylesheets().add(
                getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm()
        );
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        comboBoxThuoc.setOnAction(event -> {
            comboBoxDVT.getItems().clear();
            comboBoxDVT.getItems().add(donViTinh_dao.getDVTTheoMa(comboBoxThuoc.getValue().getMaDVTCoSo().getMaDVT()));
            comboBoxDVT.getSelectionModel().selectFirst();
            tinhTongTien();
        });
        comboBoxDVT.setOnAction(event -> {
            tinhTongTien();
        });
        comboBoxDVT.setPromptText("Chọn đơn vị tính");
        VBox vbSoLuong = new VBox();
        Spinner<Integer> spinnerSoLuong = new Spinner<>();
        spinnerSoLuong.getStylesheets().add(
                getClass().getResource("/com/antam/app/styles/dashboard_style.css").toExternalForm()
        );
        spinnerSoLuong.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 0, 1));

        spinnerSoLuong.valueProperty().addListener((obs, oldValue, newValue) -> {
            tinhTongTien();
        });
        spinnerSoLuong.setPromptText("Nhập số lượng");
        Button btn = new Button("X");
        btn.setStyle(
                "-fx-padding: 10 15 10 15;" +
                "-fx-background-color: #ef4444;" +
                "-fx-background-radius: 50%;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: BOLD;"
        );

        btn.setOnAction(event -> {
            vbox.getChildren().remove(hBox);
            tinhTongTien();
        });
        vbThuoc.getChildren().addAll(new Text("Thuốc Bán: "),comboBoxThuoc);
        vbDVT.getChildren().addAll(new Text("Đơn Vị Tính: "),comboBoxDVT);
        vbSoLuong.getChildren().addAll(new Text("Số Lượng: "),spinnerSoLuong);
        hBox.getChildren().addAll(vbThuoc, vbDVT, vbSoLuong , btn);
        vbox.getChildren().add(hBox);
    }

    public void tinhTongTien() {
        double tongTienTraCoKM = 0;
        double tongTienKhiTra = 0;
        for (ChiTietHoaDon ct : selectedItems) {
            if (ct.getTinhTrang().equals("Thuốc Mới Khi Đổi")){
                tongTienKhiTra += ct.getThanhTien();
            } else {
                tongTienTraCoKM += ct.getThanhTien();
            }
        }

        double tongTienMua = 0;
        for (Node node : vhDSCTHDM.getChildren()) {
            if (node instanceof HBox hBox) {
                VBox vbThuoc = (VBox) hBox.getChildren().get(0);
                VBox vbDVT = (VBox) hBox.getChildren().get(1);
                VBox vbSoLuong = (VBox) hBox.getChildren().get(2);

                ComboBox<Thuoc> comboThuoc = (ComboBox<Thuoc>) vbThuoc.getChildren().get(1);
                ComboBox<DonViTinh> comboDonVi = (ComboBox<DonViTinh>) vbDVT.getChildren().get(1);
                Spinner<Integer> spinnerSoLuong = (Spinner<Integer>) vbSoLuong.getChildren().get(1);
                if (comboThuoc.getValue() != null && comboDonVi.getValue() != null && spinnerSoLuong.getValue() != null) {
                    Thuoc t = comboThuoc.getValue();
                    int soLuong = spinnerSoLuong.getValue();
                    tongTienMua += t.getGiaBan() * soLuong * (1 + t.getThue());
                }
            }
        }

        DecimalFormat df = new DecimalFormat("#,### đ");
        if (khuyenMai_dao.getKhuyenMaiTheoMa(hoaDon.getMaKM().getMaKM()) != null && tongTienTraCoKM > 0) {
            tongTienTraCoKM = TinhTienKhuyenMai(tongTienTraCoKM, khuyenMai_dao.getKhuyenMaiTheoMa(hoaDon.getMaKM().getMaKM()).getSo());
            txtTongTienTra.setText(df.format(tongTienTraCoKM + tongTienKhiTra) + " (KM chỉ áp dụng cho thuốc mua)");
        }else{
            txtTongTienTra.setText(df.format(tongTienTraCoKM));
        }
        txtTongTienMua.setText(df.format(tongTienMua));

        double tienDoi = tongTienMua - (tongTienTraCoKM + tongTienKhiTra);
        if (tienDoi >= 0) {
            txtTongTienDoi.setText("Tổng kết: " + df.format(tienDoi));
            txtThongBaoDoi.setText("Khách hàng cần trả thêm");
        } else {
            txtTongTienDoi.setText("Tổng kết: " + df.format(-tienDoi));
            txtThongBaoDoi.setText("Tiền thừa trả khách");
        }
    }
    public double TinhTienKhuyenMai(double tongTien, double giaSo){
        double giam = 0;
        if (giaSo < 100) {
            giam = tongTien * giaSo / 100.0;
        } else if (giaSo >= 1000) {
            giam = giaSo * (soLuongThuocDoi / soLuongThuoc);
        }
        if (giam > tongTien) giam = tongTien;
        tongTien -= giam;
        return tongTien;
    }

    public boolean checkThongTinThuoc() {
        for (Node node : vhDSCTHDM.getChildren()) {
            if (node instanceof HBox hBox) {
                VBox vbThuoc = (VBox) hBox.getChildren().get(0);
                VBox vbDVT = (VBox) hBox.getChildren().get(1);
                VBox vbSoLuong = (VBox) hBox.getChildren().get(2);

                ComboBox<Thuoc> comboThuoc = (ComboBox<Thuoc>) vbThuoc.getChildren().get(1);
                ComboBox<DonViTinh> comboDonVi = (ComboBox<DonViTinh>) vbDVT.getChildren().get(1);
                Spinner<Integer> spinnerSoLuong = (Spinner<Integer>) vbSoLuong.getChildren().get(1);
                if (comboThuoc.getValue() == null || comboDonVi.getValue() == null || spinnerSoLuong.getValue() == null || spinnerSoLuong.getValue() <= 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
