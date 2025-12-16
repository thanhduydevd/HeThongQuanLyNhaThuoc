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
    public static void goiYKhach(TextField txtTen, TextField txtSDT,
                                 ObservableList<KhachHang> dsKhach) {
        taoGoiY(txtTen, dsKhach, true, txtSDT);
        taoGoiY(txtSDT, dsKhach, false, txtTen);
    }
    /**
     * Tự động gợi ý khách hàng khi nhập tên hoặc số điện thoại.
     * @param textField - TextField mà phương thức áp dụng gợi ý
     * @param dsKhach - Danh sách khách hàng để gợi ý
     */
    private static boolean dangChonGoiY = false;

    private static void taoGoiY(TextField textField,
                                ObservableList<KhachHang> dsKhach,
                                boolean timTheoTen,
                                TextField textFieldConLai) {

        ContextMenu menuGoiY = new ContextMenu();

        textField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (dangChonGoiY || newVal == null || newVal.isEmpty()) {
                menuGoiY.hide();
                return;
            }

            FilteredList<KhachHang> listLoc = dsKhach.filtered(kh ->
                    timTheoTen
                            ? kh.getTenKH().toLowerCase().contains(newVal.toLowerCase())
                            : kh.getSoDienThoai().contains(newVal)
            );

            if (listLoc.isEmpty()) {
                menuGoiY.hide();
                return;
            }

            menuGoiY.getItems().clear();

            for (KhachHang kh : listLoc) {
                String label = kh.getTenKH() + " — " + kh.getSoDienThoai();
                MenuItem item = new MenuItem(label);

                item.setOnAction(e -> {
                    chonKhach(kh, textField, textFieldConLai, timTheoTen, menuGoiY);
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

        // ENTER = chọn item đầu tiên & đóng menu
        textField.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER:
                    if (menuGoiY.isShowing() && !menuGoiY.getItems().isEmpty()) {
                        menuGoiY.getItems().get(0).fire();
                    }
                    break;
                case TAB:
                case ESCAPE:
                    menuGoiY.hide();
                    break;
            }
        });

        // Mất focus là đóng menu
        textField.focusedProperty().addListener((obs, old, isFocused) -> {
            if (!isFocused) {
                menuGoiY.hide();
            }
        });
    }

    private static void chonKhach(KhachHang kh,
                                  TextField textField,
                                  TextField textFieldConLai,
                                  boolean timTheoTen,
                                  ContextMenu menuGoiY) {

        dangChonGoiY = true;

        if (timTheoTen) {
            textField.setText(kh.getTenKH());
            textFieldConLai.setText(kh.getSoDienThoai());
        } else {
            textField.setText(kh.getSoDienThoai());
            textFieldConLai.setText(kh.getTenKH());
        }

        menuGoiY.hide();
        dangChonGoiY = false;
    }

    /**
     * Tự động gợi ý tên khách hàng khi nhập liệu. Đây là bản cũ nhầm mục đích thử nghiệm.
     * @param txtTenKhach
     * @param dataList
     */
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