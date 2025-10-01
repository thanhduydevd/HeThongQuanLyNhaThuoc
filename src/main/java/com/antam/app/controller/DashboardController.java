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

public class DashboardController {
    @FXML
    private VBox subMenuOverview;
    @FXML
    private VBox subMenuProduct;
    @FXML
    private VBox subMenuSales;
    @FXML
    private VBox subMenuUser;
    @FXML
    private VBox subMenuSystem;
    @FXML
    private AnchorPane paneContent;
    @FXML
    private Button btnOverview;
    @FXML
    private Button btnProduct;
    @FXML
    private Button btnSales;
    @FXML
    private Button btnUser;
    @FXML
    private Button btnSystem;
    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnStatistical;
    @FXML
    private Button btnMedicine;
    @FXML
    private Button btnShelf;
    @FXML
    private Button btnWarehouse;
    @FXML
    private Button btnInvoice;
    @FXML
    private Button btnPurchaseOrder;
    @FXML
    private Button btnPromotion;
    @FXML
    private Button btnCustomer;
    @FXML
    private Button btnEmployee;
    @FXML
    private Button btnSetting;

    public DashboardController() {
    }

    public void initialize() {
        this.loadPage("/com/antam/app/views/overview/dashboard_view.fxml");
        this.btnOverview.setOnAction((e) -> {
            this.toggleSubMenu(this.subMenuOverview);
        });
        this.btnProduct.setOnAction((e) -> {
            this.toggleSubMenu(this.subMenuProduct);
        });
        this.btnSales.setOnAction((e) -> {
            this.toggleSubMenu(this.subMenuSales);
        });
        this.btnUser.setOnAction((e) -> {
            this.toggleSubMenu(this.subMenuUser);
        });
        this.btnSystem.setOnAction((e) -> {
            this.toggleSubMenu(this.subMenuSystem);
        });
        this.btnDashboard.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/overview/dashboard_view.fxml");
        });
        this.btnStatistical.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/overview/statistical_view.fxml");
        });
        this.btnMedicine.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/product/medicine_view.fxml");
        });
        this.btnShelf.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/product/shelf_view.fxml");
        });
        this.btnWarehouse.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/product/warehouse_view.fxml");
        });
        this.btnInvoice.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/sales/invoice_view.fxml");
        });
        this.btnPurchaseOrder.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/sales/purchase_order_view.fxml");
        });
        this.btnPromotion.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/sales/promotion_view.fxml");
        });
        this.btnCustomer.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/user/customer_view.fxml");
        });
        this.btnEmployee.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/user/employee_view.fxml");
        });
        this.btnSetting.setOnAction((e) -> {
            this.loadPage("/com/antam/app/views/system/setting_view.fxml");
        });
        this.subMenuOverview.setVisible(false);
        this.subMenuOverview.setManaged(false);
        this.subMenuProduct.setVisible(false);
        this.subMenuProduct.setManaged(false);
        this.subMenuSales.setVisible(false);
        this.subMenuSales.setManaged(false);
        this.subMenuUser.setVisible(false);
        this.subMenuUser.setManaged(false);
        this.subMenuSystem.setVisible(false);
        this.subMenuSystem.setManaged(false);
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
