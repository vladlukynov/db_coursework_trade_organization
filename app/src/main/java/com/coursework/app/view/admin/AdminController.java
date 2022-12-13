package com.coursework.app.view.admin;

import com.coursework.app.TradeOrganizationApp;
import com.coursework.app.entity.Product;
import com.coursework.app.entity.Role;
import com.coursework.app.entity.User;
import com.coursework.app.service.ProductService;
import com.coursework.app.service.UserService;
import com.coursework.app.utils.StringConverterUtils;
import com.coursework.app.utils.ViewUtils;
import com.coursework.app.view.ViewControllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.IOException;
import java.sql.SQLException;

public class AdminController {
    @FXML
    private TableView<User> employeeTable;
    @FXML
    private TableColumn<User, String> loginColumn;
    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TableColumn<User, String> middleNameColumn;
    @FXML
    private TableColumn<User, Role> roleColumn;
    @FXML
    private TableColumn<User, Boolean> isActiveColumn;
    @FXML
    private TableView<Product> productsTable;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private ComboBox<String> employeeStatusBox;
    @FXML
    private Label loginLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label roleLabel;

    private final ObservableList<User> users = FXCollections.observableArrayList();
    private final ObservableList<Product> products = FXCollections.observableArrayList();
    private final UserService userService = new UserService();
    private final ProductService productService = new ProductService();

    @FXML
    protected void initialize() {
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("userLogin"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        middleNameColumn.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleColumn.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.roleStringConverter));
        isActiveColumn.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        isActiveColumn.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.accountActiveStringConverter));
        employeeTable.setItems(users);

        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productsTable.setItems(products);

        employeeStatusBox.getItems().addAll("Не уволенные", "Уволенные");
        employeeStatusBox.getSelectionModel().selectFirst();
        employeeStatusBox.setOnAction(event -> updateEmployeesPage());

        ViewControllers.setAdminController(this);

        fullUpdate();
    }

    @FXML
    protected void onDismissEmployeeButtonClick() {
        User user = employeeTable.getSelectionModel().getSelectedItem();
        if (user != null) {
            dismissUser(user.getUserLogin());
            updateEmployeesPage();
        }
    }

    @FXML
    protected void onRegisterEmployeeButtonClick() {
        try {
            ViewUtils.openWindow("admin/add-employee-view.fxml", "Регистрация сотрудника",
                    ViewUtils.getStage(employeeTable), false);
        } catch (IOException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void onExitButtonClick() {
        try {
            TradeOrganizationApp.setUser(null);
            ViewUtils.openWindow("auth/auth-view.fxml", "Авторизация",
                    ViewUtils.getStage(loginLabel), true);
        } catch (IOException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void onAddProductButtonClick() {
        try {
            ViewUtils.openWindow("admin/add-product-view.fxml", "Добавление наименования продукта",
                    ViewUtils.getStage(loginLabel), false);
        } catch (IOException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void onDeleteProductButtonClick() {
        try {
            Product product = productsTable.getSelectionModel().getSelectedItem();
            if (product != null) {
                productService.changeProductStatus(product.getProductId(), false);
                updateProductsPage();
            }
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    private void dismissUser(String login) {
        try {
            userService.setActiveStatus(login, 0);
            updateEmployeesPage();
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    private void updateEmployeesPage() {
        try {
            users.clear();
            if (employeeStatusBox.getSelectionModel().isSelected(0)) {
                users.addAll(userService.getUsers().stream().filter(User::getIsActive).toList());
            } else {
                users.addAll(userService.getUsers().stream().filter(user -> !user.getIsActive()).toList());
            }
            users.sort((o1, o2) -> o1.getLastName().compareToIgnoreCase(o2.getLastName()));
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    private void updateAccountPage() {
        loginLabel.setText("Логин: " + TradeOrganizationApp.getUser().getUserLogin());
        nameLabel.setText("ФИО: " + TradeOrganizationApp.getUser().getLastName() + " " +
                TradeOrganizationApp.getUser().getFirstName() + " " +
                TradeOrganizationApp.getUser().getMiddleName());
        roleLabel.setText("Роль: " + TradeOrganizationApp.getUser().getRole().getRoleName());
    }

    private void updateProductsPage() {
        try {
            products.clear();
            productService.getProducts().forEach(product -> {
                if (product.getIsActive()) {
                    products.add(product);
                }
            });
            products.sort((o1, o2) -> o1.getProductName().compareToIgnoreCase(o2.getProductName()));
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    public void fullUpdate() {
        updateAccountPage();
        updateEmployeesPage();
        updateProductsPage();
    }
}
