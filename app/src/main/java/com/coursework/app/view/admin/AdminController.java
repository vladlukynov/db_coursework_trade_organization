package com.coursework.app.view.admin;

import com.coursework.app.TradeOrganizationApp;
import com.coursework.app.entity.*;
import com.coursework.app.service.ProductService;
import com.coursework.app.service.SalePointService;
import com.coursework.app.service.SupplierService;
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
    // Окно сотрудники
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
    // Окно товары
    @FXML
    private TableView<Product> productsTable;
    @FXML
    private TableColumn<Product, String> productNameColumn;

    // Окно поставщики
    @FXML
    private ComboBox<Supplier> supplierBox;
    @FXML
    private TableView<SupplierProduct> supplierProductsTable;
    @FXML
    private TableColumn<SupplierProduct, Product> supplierProductNameColumn;
    @FXML
    private TableColumn<SupplierProduct, Double> supplierProductPriceColumn;

    // Окно торговые точки
    @FXML
    private TableView<SalePoint> salePointsTable;
    @FXML
    private TableColumn<SalePoint, SalePointType> salePointTypeColumn;
    @FXML
    private TableColumn<SalePoint, Double> salePointSizeColumn;
    @FXML
    private TableColumn<SalePoint, Double> salePointRentalColumn;
    @FXML
    private TableColumn<SalePoint, Double> comServColumn;
    @FXML
    private TableColumn<SalePoint, Integer> countersColumn;

    // Окно аккаунт
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
    private final ObservableList<SupplierProduct> supplierProducts = FXCollections.observableArrayList();
    private final ObservableList<SalePoint> salePoints = FXCollections.observableArrayList();
    private final ObservableList<Supplier> supplierBoxItems = FXCollections.observableArrayList();
    private final UserService userService = new UserService();
    private final ProductService productService = new ProductService();
    private final SupplierService supplierService = new SupplierService();
    private final SalePointService salePointSerivce = new SalePointService();

    @FXML
    protected void initialize() {
        // Сотрудники
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("userLogin"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        middleNameColumn.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleColumn.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.roleStringConverter));
        isActiveColumn.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        isActiveColumn.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.accountActiveStringConverter));
        employeeTable.setItems(users);

        // Товары
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productsTable.setItems(products);

        // Поставщики
        supplierProductNameColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
        supplierProductNameColumn.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.productNameStringConverter));
        supplierProductPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        supplierProductsTable.setItems(supplierProducts);

        // Торговые точки
        salePointTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        salePointTypeColumn.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.salePointTypeStringConverter));
        salePointSizeColumn.setCellValueFactory(new PropertyValueFactory<>("pointSize"));
        salePointRentalColumn.setCellValueFactory(new PropertyValueFactory<>("rentalPrice"));
        comServColumn.setCellValueFactory(new PropertyValueFactory<>("comServ"));
        countersColumn.setCellValueFactory(new PropertyValueFactory<>("counters"));
        salePointsTable.setItems(salePoints);

        // Инициализируем все Combobox
        employeeStatusBox.getItems().addAll("Не уволенные", "Уволенные");
        employeeStatusBox.getSelectionModel().selectFirst();
        employeeStatusBox.setOnAction(event -> updateEmployeesPage());

        supplierBox.setItems(supplierBoxItems);
        supplierBox.setConverter(StringConverterUtils.supplierNameStringConverter);
        supplierBox.setOnAction(event -> updateSuppliersPage());

        ViewControllers.setAdminController(this);

        initializeSupplierBox();
        updateEmployeesPage();
        updateProductsPage();
        updateSuppliersPage();
        updateAccountPage();
        updateSalePointPage();
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

    // Окно товары
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

    // Окно поставщики
    @FXML
    protected void onAddSupplierClick() {
        try {
            ViewUtils.openWindow("admin/add-supplier-view.fxml", "Добавить поставщика",
                    ViewUtils.getStage(loginLabel), false);
        } catch (IOException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void onDeleteSupplierClick() {
        try {
            Supplier supplier = supplierBox.getSelectionModel().getSelectedItem();
            if (supplier != null) {
                supplierService.changeSupplierStatus(supplier.getSupplierId(), false);
                updateSuppliersPage();
            }
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void onSupplierProductAddClick() {
        try {
            ViewUtils.openWindow("admin/add-supplier-product-view.fxml", "Добавить товар",
                    ViewUtils.getStage(loginLabel), false);
        } catch (IOException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void onSupplierProductDeleteClick() {

    }

    // Окно аккаунт
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

    // Окно торговые точки
    @FXML
    protected void salePointAddClick() {
        try {
            ViewUtils.openWindow("admin/add-sale-point-view.fxml", "Добавить торговую точку",
                    ViewUtils.getStage(loginLabel), false);
        } catch (IOException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void salePointDeleteClick() {
        try {
            SalePoint salePoint = salePointsTable.getSelectionModel().getSelectedItem();
            if (salePoint != null) {
                salePointSerivce.changeStatus(salePoint.getSalePointId(), false);
                updateSalePointPage();
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

    public void updateEmployeesPage() {
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

    public void updateProductsPage() {
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

    public void updateAccountPage() {
        loginLabel.setText("Логин: " + TradeOrganizationApp.getUser().getUserLogin());
        nameLabel.setText("ФИО: " + TradeOrganizationApp.getUser().getLastName() + " " +
                TradeOrganizationApp.getUser().getFirstName() + " " +
                TradeOrganizationApp.getUser().getMiddleName());
        roleLabel.setText("Роль: " + TradeOrganizationApp.getUser().getRole().getRoleName());
    }

    public void updateSuppliersPage() {
        try {
            supplierProducts.clear();
            Supplier supplier = supplierBox.getSelectionModel().getSelectedItem();
            if (supplier != null) {
                supplierService.getSupplierProducts(supplier.getSupplierId())
                        .forEach(supplierProduct -> {
                            if (supplierProduct.getProduct().getIsActive()) {
                                supplierProducts.add(supplierProduct);
                            }
                        });
            }
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    public void updateSalePointPage() {
        try {
            salePoints.clear();
            salePointSerivce.getSalePoints().forEach(point -> {
                if (point.getIsActive()) {
                    salePoints.add(point);
                }
            });
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    public void initializeSupplierBox() {
        try {
            supplierBoxItems.clear();

            supplierService.getSuppliers().forEach(supplier -> {
                if (supplier.getIsActive()) {
                    supplierBoxItems.add(supplier);
                }
            });
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    public Supplier getSelectedSupplier() {
        return supplierBox.getSelectionModel().getSelectedItem();
    }
}
