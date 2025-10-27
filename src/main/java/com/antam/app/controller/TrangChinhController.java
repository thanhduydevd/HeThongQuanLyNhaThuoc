//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class TrangChinhController {
    @FXML
    private Button btnTrangChinh, btnHoaDon, btnPhieuDat, btnThuoc, btnKeThuoc, btnDangDieuChe, btnDonViTinh, btnKhuyenMai, btnPhieuNhap, btnKhachHang, btnNhanVien, btnCaiDat;

    @FXML
    private VBox subMenuHoaDon, subMenuPhieuDat, subMenuThuoc, subMenuKeThuoc, subMenuDangDieuChe, subMenuDonViTinh, subMenuKhuyenMai, subMenuPhieuNhap, subMenuKhachHang, subMenuNhanVien, subMenuCaiDat;

    @FXML
    private Button btnXemHoaDon, btnThemHoaDon, btnCapNhatHoaDon, btnThongKeDoanhThu, btnTimPhieuDat, btnThemPhieuDat, btnCapNhatPhieuDat, btnXemThuoc, btnThemThuoc, btnCapNhatThuoc, btnXemKeThuoc, btnThemKeThuoc, btnCapNhatKeThuoc, btnThemDangDieuChe, btnCapNhatDangDieuChe, btnThemDonViTinh, btnCapNhatDonViTinh, btnXemKhuyenMai, btnThemKhuyenMai, btnCapNhatKhuyenMai, btnXemPhieuNhap, btnThemPhieuNhap, btnXemKhachHang, btnThemKhachHang, btnCapNhatKhachHang, btnXemNhanVien, btnThemNhanVien, btnCapNhatNhanVien, btnCaiDatTaiKhoan;

    @FXML
    private AnchorPane paneContent;



    public TrangChinhController() {
    }

    private void setActiveButton(Button activeButton) {
        Button[] buttons = {
                btnTrangChinh, btnXemHoaDon, btnThemHoaDon, btnCapNhatHoaDon, btnThongKeDoanhThu, btnXemThuoc, btnThemThuoc, btnCapNhatThuoc, btnXemKeThuoc, btnThemKeThuoc, btnCapNhatKeThuoc, btnThemDangDieuChe, btnCapNhatDangDieuChe, btnThemDonViTinh, btnCapNhatDonViTinh, btnXemKhuyenMai, btnThemKhuyenMai, btnCapNhatKhuyenMai, btnXemPhieuNhap, btnThemPhieuNhap,
                btnXemKhachHang, btnThemKhachHang, btnCapNhatKhachHang, btnXemNhanVien, btnThemNhanVien, btnCapNhatNhanVien, btnCaiDatTaiKhoan, btnTimPhieuDat, btnThemPhieuDat, btnCapNhatPhieuDat
        };
        for (Button btn : buttons) {
            btn.getStyleClass().remove("btn-active");
        }
        activeButton.getStyleClass().add("btn-active");
    }
    public void initialize() {
        this.loadPage("/com/antam/app/views/trangchinh/trangChinh.fxml");
        this.btnTrangChinh.setOnAction((e) -> {

            setActiveButton(btnTrangChinh);
        });
        this.btnHoaDon.setOnAction((e) -> {
            this.toggleSubMenu(this.subMenuHoaDon);
        });
        this.btnPhieuDat.setOnAction((e) -> {
            this.toggleSubMenu(this.subMenuPhieuDat);
        });
        this.btnThuoc.setOnAction((e) -> {
            this.toggleSubMenu(this.subMenuThuoc);
        });
        this.btnKeThuoc.setOnAction((e) -> {
            this.toggleSubMenu(this.subMenuKeThuoc);
        });
        this.btnDangDieuChe.setOnAction((e) -> {
            this.toggleSubMenu(this.subMenuDangDieuChe);
        });
        this.btnDonViTinh.setOnAction((e) -> {
            this.toggleSubMenu(this.subMenuDonViTinh);
        });
        this.btnKhuyenMai.setOnAction((e) -> {
            this.toggleSubMenu(this.subMenuKhuyenMai);
        });
        this.btnPhieuNhap.setOnAction((e) -> {
            this.toggleSubMenu(this.subMenuPhieuNhap);
        });
        this.btnKhachHang.setOnAction((e) -> {
            this.toggleSubMenu(this.subMenuKhachHang);
        });
        this.btnNhanVien.setOnAction((e) -> {
            this.toggleSubMenu(this.subMenuNhanVien);
        });
        this.btnCaiDat.setOnAction((e) -> {
            this.toggleSubMenu(this.subMenuCaiDat);
        });


        this.btnTrangChinh.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/trangchinh/trangChinh.fxml");
            setActiveButton(btnTrangChinh);
        });
        //Hoá đơn
        this.btnXemHoaDon.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/hoadon/timHoaDon.fxml");
            setActiveButton(btnXemHoaDon);
        });
        this.btnThemHoaDon.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/hoadon/themHoaDon.fxml");
            setActiveButton(btnThemHoaDon);
        });
        this.btnCapNhatHoaDon.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/hoadon/capNhatHoaDon.fxml");
            setActiveButton(btnCapNhatHoaDon);
        });
        this.btnThongKeDoanhThu.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/hoadon/thongKeDoanhThu.fxml");
            setActiveButton(btnThongKeDoanhThu);
        });
        //Phieudat
        this.btnTimPhieuDat.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/phieudat/timPhieuDat.fxml");
            setActiveButton(btnTimPhieuDat);
        });
        this.btnThemPhieuDat.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/phieudat/themPhieuDat.fxml");
            setActiveButton(btnThemPhieuDat);
        });
        this.btnCapNhatPhieuDat.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/phieudat/capNhatPhieuDat.fxml");
            setActiveButton(btnCapNhatPhieuDat);
        });
        //Thuốc
        this.btnXemThuoc.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/thuoc/timThuoc.fxml");
            setActiveButton(btnXemThuoc);
        });
        this.btnThemThuoc.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/thuoc/themThuoc.fxml");
            setActiveButton(btnThemThuoc);
        });
        this.btnCapNhatThuoc.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/thuoc/capNhatThuoc.fxml");
            setActiveButton(btnCapNhatThuoc);
        });
        //Kệ thuốc
        this.btnXemKeThuoc.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/kethuoc/timKeThuoc.fxml");
            setActiveButton(btnXemKeThuoc);
        });
        this.btnThemKeThuoc.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/kethuoc/themKeThuoc.fxml");
            setActiveButton(btnThemKeThuoc);
        });
        this.btnCapNhatKeThuoc.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/kethuoc/capNhatKeThuoc.fxml");
            setActiveButton(btnCapNhatKeThuoc);
        });
        //Dạng điều chế
        this.btnThemDangDieuChe.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/dangdieuche/themDangDieuChe.fxml");
            setActiveButton(btnThemDangDieuChe);
        });
        this.btnCapNhatDangDieuChe.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/dangdieuche/capNhatDangDieuChe.fxml");
            setActiveButton(btnCapNhatDangDieuChe);
        });
        //Don vị tính
        this.btnThemDonViTinh.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/donvitinh/themDonViTinh.fxml");
            setActiveButton(btnThemDonViTinh);
        });
        this.btnCapNhatDonViTinh.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/donvitinh/capNhatDonViTinh.fxml");
            setActiveButton(btnCapNhatDonViTinh);
        });
        //Khuyen mai
        this.btnXemKhuyenMai.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/khuyenmai/timKhuyenMai.fxml");
            setActiveButton(btnXemKhuyenMai);
        });
        this.btnThemKhuyenMai.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/khuyenmai/themKhuyenMai.fxml");
            setActiveButton(btnThemKhuyenMai);
        });
        this.btnCapNhatKhuyenMai.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/khuyenmai/capNhatKhuyenMai.fxml");
            setActiveButton(btnCapNhatKhuyenMai);
        });
        //Phieu nhap
        this.btnXemPhieuNhap.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/phieunhap/timPhieuNhap.fxml");
            setActiveButton(btnXemPhieuNhap);
        });
        this.btnThemPhieuNhap.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/phieunhap/themPhieuNhap.fxml");
            setActiveButton(btnThemPhieuNhap);
        });
        //Kh hang
        this.btnXemKhachHang.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/khachhang/xemKhachHang.fxml");
            setActiveButton(btnXemKhachHang);
        });
        this.btnThemKhachHang.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/khachhang/themKhachHang.fxml");
            setActiveButton(btnThemKhachHang);
        });
        this.btnCapNhatKhachHang.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/khachhang/capNhatKhachHang.fxml");
            setActiveButton(btnCapNhatKhachHang);
        });
        //Nv
        this.btnXemNhanVien.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/nhanvien/xemNhanVien.fxml");
            setActiveButton(btnXemNhanVien);
        });
        this.btnThemNhanVien.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/nhanvien/themNhanVien.fxml");
            setActiveButton(btnThemNhanVien);
        });
        this.btnCapNhatNhanVien.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/nhanvien/capNhatNhanVien.fxml");
            setActiveButton(btnCapNhatNhanVien);
        });
        //cai dat
        this.btnCaiDatTaiKhoan.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/caidat/caiDatTaiKhoan.fxml");
            setActiveButton(btnCaiDatTaiKhoan);
        });

        this.subMenuHoaDon.setVisible(false);
        this.subMenuHoaDon.setManaged(false);

        this.subMenuPhieuDat.setVisible(false);
        this.subMenuPhieuDat.setManaged(false);

        this.subMenuThuoc.setVisible(false);
        this.subMenuThuoc.setManaged(false);

        this.subMenuKeThuoc.setVisible(false);
        this.subMenuKeThuoc.setManaged(false);

        this.subMenuDangDieuChe.setVisible(false);
        this.subMenuDangDieuChe.setManaged(false);

        this.subMenuDonViTinh.setVisible(false);
        this.subMenuDonViTinh.setManaged(false);

        this.subMenuKhuyenMai.setVisible(false);
        this.subMenuKhuyenMai.setManaged(false);

        this.subMenuPhieuNhap.setVisible(false);
        this.subMenuPhieuNhap.setManaged(false);

        this.subMenuKhachHang.setVisible(false);
        this.subMenuKhachHang.setManaged(false);

        this.subMenuNhanVien.setVisible(false);
        this.subMenuNhanVien.setManaged(false);

        this.subMenuCaiDat.setVisible(false);
        this.subMenuCaiDat.setManaged(false);
    }

    @FXML
    protected void toggleSubMenu(VBox subMenu) {
        boolean isVisible = subMenu.isVisible();
        subMenu.setVisible(!isVisible);
        subMenu.setManaged(!isVisible);
    }

    private void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(fxmlFile));
            Node node = (Node)loader.load();
            AnchorPane.setTopAnchor(node, 0.0);
            AnchorPane.setBottomAnchor(node, 0.0);
            AnchorPane.setLeftAnchor(node, 0.0);
            AnchorPane.setRightAnchor(node, 0.0);
            this.paneContent.getChildren().setAll(new Node[]{node});
        } catch (IOException var4) {
            IOException e = var4;
            e.printStackTrace();
        }

    }
}
