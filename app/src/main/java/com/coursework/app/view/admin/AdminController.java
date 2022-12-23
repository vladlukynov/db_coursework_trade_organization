package com.coursework.app.view.admin;

import com.coursework.app.TradeOrganizationApp;
import com.coursework.app.entity.*;
import com.coursework.app.service.*;
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
    private boolean isInitialized = false;

    // Окно сотрудники
    @FXML
    private Tab employeeTab;
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
    private final ObservableList<User> users = FXCollections.observableArrayList();
    private final ObservableList<String> employeeStatusBoxItems = FXCollections.observableArrayList("Не уволенные", "Уволенные");

    // Окно товары
    @FXML
    private Tab productTab;
    @FXML
    private TableView<Product> productsTable;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    private final ObservableList<Product> products = FXCollections.observableArrayList();

    // Окно поставщики
    @FXML
    private Tab supplierTab;
    @FXML
    private ComboBox<Supplier> supplierBox;
    @FXML
    private TableView<SupplierProduct> supplierProductsTable;
    @FXML
    private TableColumn<SupplierProduct, Product> supplierProductNameColumn;
    @FXML
    private TableColumn<SupplierProduct, Double> supplierProductPriceColumn;
    private final ObservableList<SupplierProduct> supplierProducts = FXCollections.observableArrayList();
    private final ObservableList<Supplier> supplierBoxItems = FXCollections.observableArrayList();

    // Окно аккаунт
    @FXML
    private Tab accountTab;
    @FXML
    private ComboBox<String> employeeStatusBox;
    @FXML
    private Label loginLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label roleLabel;

    // Окно торговые точки
    @FXML
    private Tab salePointTab;
    @FXML
    private TableView<SalePoint> salePointsTable;
    @FXML
    private TableColumn<SalePoint, String> salePointNameColumn;
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
    private final ObservableList<SalePoint> salePoints = FXCollections.observableArrayList();

    // Окно залы
    @FXML
    private Tab hallTab;
    @FXML
    private TableView<Hall> hallsTable;
    @FXML
    private TableColumn<Hall, String> hallNameColumn;
    @FXML
    private TableColumn<Hall, SalePoint> hallSalePointNameColumn;
    @FXML
    private ComboBox<SalePoint> hallSalePointBox;
    private final ObservableList<Hall> halls = FXCollections.observableArrayList();
    private final ObservableList<SalePoint> hallSalePointBoxItems = FXCollections.observableArrayList();

    // Окно секции
    @FXML
    private Tab sectionTab;
    @FXML
    private TableView<Section> sectionsTable;
    @FXML
    private TableColumn<Section, String> sectionNameColumn;
    @FXML
    private ComboBox<Hall> sectionHallBox;
    @FXML
    private ComboBox<SalePoint> sectionSalePointBox;
    private final ObservableList<Section> sections = FXCollections.observableArrayList();
    private final ObservableList<Hall> sectionHallBoxItems = FXCollections.observableArrayList();
    private final ObservableList<SalePoint> sectionSalePointBoxItems = FXCollections.observableArrayList();

    // Сервисы для обращения к БД
    private final UserService userService = new UserService();
    private final ProductService productService = new ProductService();
    private final SupplierService supplierService = new SupplierService();
    private final SalePointService salePointService = new SalePointService();
    private final HallService hallService = new HallService();
    private final SectionService sectionService = new SectionService();

    @FXML
    protected void initialize() {
        // Инициализируем все TableView
        // Окно сотрудники
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("userLogin"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        middleNameColumn.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleColumn.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.roleStringConverter));
        isActiveColumn.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        isActiveColumn.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.accountActiveStringConverter));
        employeeTable.setItems(users);

        // Окно товары
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productsTable.setItems(products);

        // Окно поставщики
        supplierProductNameColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
        supplierProductNameColumn.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.productNameStringConverter));
        supplierProductPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        supplierProductsTable.setItems(supplierProducts);

        // Окно торговые точки
        salePointNameColumn.setCellValueFactory(new PropertyValueFactory<>("salePointName"));
        salePointTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        salePointTypeColumn.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.salePointTypeStringConverter));
        salePointSizeColumn.setCellValueFactory(new PropertyValueFactory<>("pointSize"));
        salePointRentalColumn.setCellValueFactory(new PropertyValueFactory<>("rentalPrice"));
        comServColumn.setCellValueFactory(new PropertyValueFactory<>("communalService"));
        countersColumn.setCellValueFactory(new PropertyValueFactory<>("countersNumber"));
        salePointsTable.setItems(salePoints);

        // Окно залы
        hallNameColumn.setCellValueFactory(new PropertyValueFactory<>("hallName"));
        hallSalePointNameColumn.setCellValueFactory(new PropertyValueFactory<>("salePoint"));
        hallSalePointNameColumn.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.salePointNameStringConverter));
        hallsTable.setItems(halls);

        // Окно секции
        sectionNameColumn.setCellValueFactory(new PropertyValueFactory<>("sectionName"));
        sectionsTable.setItems(sections);

        // Инициализируем все Combobox
        // Сотрудники
        employeeStatusBox.setItems(employeeStatusBoxItems);
        employeeStatusBox.setOnAction(event -> updateEmployeesTable());

        // Поставщики
        supplierBox.setItems(supplierBoxItems);
        supplierBox.setConverter(StringConverterUtils.supplierNameStringConverter);
        supplierBox.setOnAction(event -> updateSupplierProductsTable());

        // Залы
        hallSalePointBox.setItems(hallSalePointBoxItems);
        hallSalePointBox.setConverter(StringConverterUtils.salePointNameStringConverter);
        hallSalePointBox.setOnAction(event -> updateHallsPage());

        // Секции
        sectionHallBox.setItems(sectionHallBoxItems);
        sectionHallBox.setConverter(StringConverterUtils.hallNameStringConverter);
        sectionHallBox.setOnAction(event -> updateSectionTable());
        sectionSalePointBox.setItems(sectionSalePointBoxItems);
        sectionSalePointBox.setConverter(StringConverterUtils.salePointNameStringConverter);
        sectionSalePointBox.setOnAction(event -> updateSectionHallBox());

        ViewControllers.setAdminController(this);
        isInitialized = true;
    }

    @FXML
    protected void onDismissEmployeeButtonClick() {
        try {
            User user = employeeTable.getSelectionModel().getSelectedItem();
            if (user != null) {
                userService.deactivateUser(user.getUserLogin());
                updateEmployeesTable();
            }
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
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
                productService.deactivateProduct(product.getProductId());
                updateProductTable();
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
                supplierService.deactivateSupplier(supplier.getSupplierId());
                updateSupplierProductsTable();
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
        try {
            SupplierProduct product = supplierProductsTable.getSelectionModel().getSelectedItem();
            Supplier supplier = supplierBox.getSelectionModel().getSelectedItem();
            if (supplier != null && product != null) {
                supplierService.deactivateSupplierProduct(supplier.getSupplierId(), product.getProduct().getProductId());
                updateSupplierProductsTable();
            }
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
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
                salePointService.deactivateById(salePoint.getSalePointId());
                updateSalePointsTable();
            }
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    // Окно залы
    @FXML
    protected void onHallDeleteButtonClick() {
        try {
            Hall hall = hallsTable.getSelectionModel().getSelectedItem();

            if (hall != null) {
                hallService.deactivateById(hall.getHallId());
                updateHallsPage();
            }
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void onHallAddButtonClick() {
        try {
            ViewUtils.openWindow("admin/add-hall-view.fxml", "Добавить зал",
                    ViewUtils.getStage(loginLabel), false);
        } catch (IOException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    // Окно секции
    @FXML
    private void addSectionButton() {
        try {
            ViewUtils.openWindow("admin/add-section-view.fxml", "Добавить секцию",
                    ViewUtils.getStage(loginLabel), false);
        } catch (IOException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    private void deleteSectionButton() {
        Section section = sectionsTable.getSelectionModel().getSelectedItem();
        if (section != null) {
            try {
                sectionService.deactivateSection(section.getSectionId());
                updateSectionTable();
            } catch (SQLException exception) {
                new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            }
        }
    }

    /* ДЕЙСТВИЯ В ОКНЕ СОТРУДНИКИ */
    @FXML
    protected void employeeTabSelected() {
        if (employeeTab.isSelected() && isInitialized) {
            employeeStatusBox.getSelectionModel().selectFirst();
            updateEmployeesTable();
        }
    }

    public void updateEmployeesTable() {
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
    /* КОНЕЦ ДЕЙСТВИЙ В ОКНЕ СОТРУДНИКИ */

    /* ДЕЙСТВИЯ В ОКНЕ ТОВАРЫ */
    @FXML
    private void productTabSelected() {
        if (productTab.isSelected()) {
            updateProductTable();
        }
    }

    public void updateProductTable() {
        try {
            products.clear();
            products.addAll(productService.getProducts().stream().filter(Product::getIsActive).toList());
            products.sort((o1, o2) -> o1.getProductName().compareToIgnoreCase(o2.getProductName()));
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }
    /* КОНЕЦ ДЕЙСТВИЙ В ОКНЕ ТОВАРЫ */

    /* ДЕЙСТВИЯ В ОКНЕ ПОСТАВЩИКИ */
    @FXML
    protected void supplierTabSelected() {
        if (supplierTab.isSelected()) {
            updateSupplierBox();
            updateSupplierProductsTable();
        }
    }

    public void updateSupplierBox() {
        try {
            supplierBoxItems.clear();
            supplierBoxItems.addAll(supplierService.getSuppliers().stream().filter(Supplier::getIsActive).toList());
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    public void updateSupplierProductsTable() {
        try {
            supplierProducts.clear();
            Supplier supplier = supplierBox.getSelectionModel().getSelectedItem();
            if (supplier != null) {
                supplierProducts.addAll(supplierService.getSupplierProducts(supplier.getSupplierId()).stream()
                        .filter(supplierProduct -> supplierProduct.getIsActive() && supplierProduct.getProduct().getIsActive()).toList());
            }
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }
    /* КОНЕЦ ДЕЙСТВИЙ В ОКНЕ ПОСТАВЩИКИ */

    /* ДЕЙСТВИЯ В ОКНЕ АККАУНТ */
    @FXML
    private void accountTabSelected() {
        if (accountTab.isSelected()) {
            updateAccountPage();
        }
    }

    public void updateAccountPage() {
        loginLabel.setText("Логин: " + TradeOrganizationApp.getUser().getUserLogin());
        nameLabel.setText("ФИО: " + TradeOrganizationApp.getUser().getLastName() + " " +
                TradeOrganizationApp.getUser().getFirstName() + " " +
                TradeOrganizationApp.getUser().getMiddleName());
        roleLabel.setText("Роль: " + TradeOrganizationApp.getUser().getRole().getRoleName());
    }
    /* КОНЕЦ ДЕЙСТВИЙ В ОКНЕ АККАУНТ */

    /* ДЕЙСТВИЯ В ОКНЕ ТОРГОВЫЕ ТОЧКИ */
    @FXML
    private void salePointTabSelected() {
        if (salePointTab.isSelected()) {
            updateSalePointsTable();
        }
    }

    public void updateSalePointsTable() {
        try {
            salePoints.clear();
            salePoints.addAll(salePointService.getSalePoints().stream().filter(SalePoint::getIsActive).toList());
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }
    /* КОНЕЦ ДЕЙСТВИЙ В ОКНЕ ТОРГОВЫЕ ТОЧКИ */

    /* ДЕЙСТВИЯ В ОКНЕ ЗАЛЫ */
    @FXML
    private void hallTabSelected() {
        if (hallTab.isSelected()) {
            updateHallSalePointBox();
            updateHallsPage();
        }
    }

    public void updateHallSalePointBox() {
        try {
            hallSalePointBoxItems.clear();
            hallSalePointBoxItems.addAll(salePointService.getSalePoints().stream().filter(SalePoint::getIsActive).toList());
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    public void updateHallsPage() {
        try {
            halls.clear();
            SalePoint point = hallSalePointBox.getSelectionModel().getSelectedItem();
            if (point != null) {
                halls.addAll(hallService.getHalls().stream().filter(hall -> hall.getIsActive() &&
                        hall.getSalePoint().getSalePointId() == point.getSalePointId()).toList());
            }
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }
    /* КОНЕЦ ДЕЙСТВИЙ В ОКНЕ ЗАЛЫ */

    /* ДЕЙСТВИЯ В ОКНЕ СЕКЦИИ */
    @FXML
    protected void sectionTabSelected() {
        if (sectionTab.isSelected()) {
            updateSectionSalePointBox();
        } else {
            sectionHallBox.setDisable(true);
            sectionHallBoxItems.clear();
        }
    }

    protected void updateSectionSalePointBox() {
        try {
            sectionSalePointBoxItems.clear();
            sectionSalePointBoxItems.addAll(salePointService.getSalePoints().stream().filter(SalePoint::getIsActive).toList());
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    protected void updateSectionHallBox() {
        SalePoint salePoint = sectionSalePointBox.getSelectionModel().getSelectedItem();
        if (salePoint != null) {
            sectionHallBoxItems.clear();
            sectionHallBox.setDisable(false);
            try {
                sectionHallBoxItems.addAll(hallService.getHallsBySalePointId(salePoint.getSalePointId()).stream()
                        .filter(Hall::getIsActive).toList());
            } catch (SQLException exception) {
                new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            }
        }
    }

    protected void updateSectionTable() {
        SalePoint salePoint = sectionSalePointBox.getSelectionModel().getSelectedItem();
        Hall hall = sectionHallBox.getSelectionModel().getSelectedItem();
        if (salePoint != null && hall != null) {
            sections.clear();
            try {
                sections.addAll(sectionService.getSectionsByHallId(hall.getHallId())
                        .stream().filter(Section::getIsActive).toList());
            } catch (SQLException exception) {
                new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            }
        }
    }
    /* КОНЕЦ ДЕЙСТВИЙ В ОКНЕ СЕКЦИИ */

    // Геттеры
    protected Supplier getSelectedSupplier() {
        return supplierBox.getSelectionModel().getSelectedItem();
    }

    protected SalePoint getSelectedHallSalePoint() {
        return hallSalePointBox.getSelectionModel().getSelectedItem();
    }

    protected Hall getSelectedSectionHall() {
        return sectionHallBox.getSelectionModel().getSelectedItem();
    }
}
