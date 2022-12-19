package com.coursework.app.view;

import com.coursework.app.view.admin.AdminController;
import com.coursework.app.view.seller.SellerController;

public class ViewControllers {
    private static AdminController adminController = null;
    private static SellerController sellerController = null;

    public static AdminController getAdminController() {
        return adminController;
    }

    public static void setAdminController(AdminController controller) {
        adminController = controller;
    }

    public static SellerController getSellerController() {
        return sellerController;
    }

    public static void setSellerController(SellerController controller) {
        sellerController = controller;
    }
}
