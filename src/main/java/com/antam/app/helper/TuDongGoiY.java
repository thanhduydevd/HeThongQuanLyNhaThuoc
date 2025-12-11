package com.antam.app.helper;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 08/12/2025
 * @version: 1.0
 */
import com.antam.app.entity.KhachHang;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

public class TuDongGoiY {

    /**
     * Tự động gợi ý khách hàng khi nhập tên hoặc số điện thoại
     * @param txtTen - TextField tên khách hàng
     * @param txtSDT - TextField số điện thoại khách hàng
     * @param dsKhach - Danh sách khách hàng để gợi ý
     */
    public static void goiYKhach(TextField txtTen, TextField txtSDT, ObservableList<KhachHang> dsKhach) {
        taoGoiY(txtTen, dsKhach, true, txtSDT);
        taoGoiY(txtSDT, dsKhach, false, txtTen);
    }

    /**
     * Tự động gợi ý khách hàng khi nhập tên hoặc số điện thoại.
     * @param textField - TextField mà phương thức áp dụng gợi ý
     * @param dsKhach - Danh sách khách hàng để gợi ý
     */
    // Tạo gợi ý cho từng TextField
    private static void taoGoiY(TextField textField,
                                ObservableList<KhachHang> dsKhach,
                                boolean timTheoTen,
                                TextField textFieldConLai) {

        ContextMenu menuGoiY = new ContextMenu();

        textField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty()) {
                menuGoiY.hide();
                return;
            }

            // Lọc theo tên hoặc theo số điện thoại
            FilteredList<KhachHang> listLoc = dsKhach.filtered(kh -> {
                if (timTheoTen) {
                    return kh.getTenKH().toLowerCase().contains(newVal.toLowerCase());
                } else {
                    return kh.getSoDienThoai().contains(newVal);
                }
            });

            if (listLoc.isEmpty()) {
                menuGoiY.hide();
                return;
            }

            // Xóa gợi ý cũ
            menuGoiY.getItems().clear();

            for (KhachHang kh : listLoc) {
                String label = kh.getTenKH() + " — " + kh.getSoDienThoai();

                MenuItem item = new MenuItem(label);
                item.setOnAction(e -> {
                    textField.setText(timTheoTen ? kh.getTenKH() : kh.getSoDienThoai());

                    // Điền luôn TextField còn lại
                    if (timTheoTen) {
                        textFieldConLai.setText(kh.getSoDienThoai());
                    } else {
                        textFieldConLai.setText(kh.getTenKH());
                    }

                    menuGoiY.hide();
                });

                menuGoiY.getItems().add(item);
            }

            if (!menuGoiY.isShowing()) {
                menuGoiY.show(textField,
                        textField.localToScreen(textField.getBoundsInLocal()).getMinX(),
                        textField.localToScreen(textField.getBoundsInLocal()).getMaxY()
                );
            }
        });
    }
    public static void tuDongGoiYTenKhach(TextField txtTenKhach,
                                          ObservableList<KhachHang> dataList) {

        ContextMenu suggestionMenu = new ContextMenu();

        txtTenKhach.textProperty().addListener((obs, oldText, newText) -> {

            if (newText == null || newText.trim().isEmpty()) {
                suggestionMenu.hide();
                return;
            }

            // Lọc theo tên hoặc sđt
            FilteredList<KhachHang> filtered = dataList.filtered(kh ->
                    kh.getTenKH().toLowerCase().contains(newText.toLowerCase()) ||
                            kh.getSoDienThoai().toLowerCase().contains(newText.toLowerCase())
            );

            if (filtered.isEmpty()) {
                suggestionMenu.hide();
            } else {
                suggestionMenu.getItems().clear();

                for (KhachHang kh : filtered) {
                    // Hiển thị dạng: "Tên — SĐT"
                    String display = kh.getTenKH() + " — " + kh.getSoDienThoai();

                    MenuItem item = new MenuItem(display);

                    item.setOnAction(e -> {
                        txtTenKhach.setText(kh.getTenKH());
                        txtTenKhach.positionCaret(txtTenKhach.getText().length());
                        suggestionMenu.hide();
                    });

                    suggestionMenu.getItems().add(item);
                }

                if (!suggestionMenu.isShowing()) {
                    suggestionMenu.show(
                            txtTenKhach,
                            txtTenKhach.localToScreen(0, txtTenKhach.getHeight()).getX(),
                            txtTenKhach.localToScreen(0, txtTenKhach.getHeight()).getY()
                    );
                }
            }
        });

        // Mất focus thì ẩn gợi ý
        txtTenKhach.focusedProperty().addListener((obs, oldV, newV) -> {
            if (!newV) suggestionMenu.hide();
        });
    }
}