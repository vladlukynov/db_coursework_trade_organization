package com.coursework.app.view;

import com.coursework.app.view.admin.AdminController;

public class ViewControllers {
    private static AdminController adminController = null;

    public static AdminController getAdminController() {
        return adminController;
    }

    public static void setAdminController(AdminController controller) {
        adminController = controller;
    }
}
