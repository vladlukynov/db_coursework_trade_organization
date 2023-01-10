package com.coursework.app.view.admin;

import com.coursework.app.TradeOrganizationApp;
import com.coursework.app.entity.*;
import com.coursework.app.entity.queries.*;
import com.coursework.app.exception.GetDBInformationException;
import com.coursework.app.exception.NoUserByLoginException;
import com.coursework.app.service.*;
import com.coursework.app.utils.StringConverterUtils;
import com.coursework.app.utils.ViewUtils;
import com.coursework.app.view.ViewControllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

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
    private final SalePointTypeService salePointTypeService = new SalePointTypeService();

    private final ConsumerService consumerService = new ConsumerService();

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

        /* *********** ПОКУПАТЕЛИ *********** */
        productComboBox.setConverter(StringConverterUtils.productNameStringConverter);
        productBox1.setConverter(StringConverterUtils.productNameStringConverter);
        salePointTypeBox1.setConverter(StringConverterUtils.salePointTypeStringConverter);
        salePointBox3.setConverter(StringConverterUtils.salePointNameStringConverter);
        /* *********** ТОРГОВЫЕ ТОЧКИ *********** */
        salePointBox4.setConverter(StringConverterUtils.salePointNameStringConverter);
        salePointComboBox.setConverter(StringConverterUtils.salePointNameStringConverter);
        salePointTypeComboBox.setConverter(StringConverterUtils.salePointTypeStringConverter);
        salePointComboBox3.setConverter(StringConverterUtils.salePointNameStringConverter);
        productComboBox2.setConverter(StringConverterUtils.productNameStringConverter);
        productComboBox3.setConverter(StringConverterUtils.productNameStringConverter);
        salePointTypeBox4.setConverter(StringConverterUtils.salePointTypeStringConverter);
        salePointBox.setConverter(StringConverterUtils.salePointNameStringConverter);
        productBox.setConverter(StringConverterUtils.productNameStringConverter);
        supplierBox2.setConverter(StringConverterUtils.supplierNameStringConverter);
        salePointBox1.setConverter(StringConverterUtils.salePointNameStringConverter);
        /* *********** ПРОДАВЦЫ *********** */
        salePointTypeBox.setConverter(StringConverterUtils.salePointTypeStringConverter);
        salePointTypeBox2.setConverter(StringConverterUtils.salePointTypeStringConverter);
        sellersBox.setConverter(StringConverterUtils.sellerNameStringConverter);
        salePointTypeBox3.setConverter(StringConverterUtils.salePointTypeStringConverter);
        salePointBox2.setConverter(StringConverterUtils.salePointNameStringConverter);
        /* *********** ПОСТАВЩИКИ *********** */
        productsForSuppliersBox.setConverter(StringConverterUtils.productNameStringConverter);

        ViewControllers.setAdminController(this);
        isInitialized = true;
    }


    // Окно сотрудники
    @FXML
    protected void informationButtonClick() {
        User user = employeeTable.getSelectionModel().getSelectedItem();

        if (user == null) {
            return;
        }

        try {
            if (userService.isSeller(user.getUserLogin())) {
                Seller seller = userService.getSellerByLogin(user.getUserLogin());

                FXMLLoader fxmlLoader = new FXMLLoader(TradeOrganizationApp.class.getResource("admin/employee-information-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                EmployeeInformationController controller = fxmlLoader.getController();
                controller.seller(seller);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Информация о продавце");
                stage.setResizable(false);
                stage.show();

                return;
            }

            if (userService.isSuperVisor(user.getUserLogin())) {
                SuperVisor superVisor = userService.getSuperVisorByLogin(user.getUserLogin());

                FXMLLoader fxmlLoader = new FXMLLoader(TradeOrganizationApp.class.getResource("admin/employee-information-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                EmployeeInformationController controller = fxmlLoader.getController();
                controller.superVisor(superVisor);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Информация о руководителе");
                stage.setResizable(false);
                stage.show();
            }
        } catch (SQLException | IOException | NoUserByLoginException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void employeeDataChangeButton() {
        try {
            ViewUtils.openWindow("admin/change-employee-data-view.fxml", "Изменение информации о сотруднике",
                    ViewUtils.getStage(loginLabel), false);
        } catch (IOException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void onDismissEmployeeButtonClick() {
        try {
            User user = employeeTable.getSelectionModel().getSelectedItem();
            if (user != null) {
                if (user.getUserLogin().equals(TradeOrganizationApp.getUser().getUserLogin())) {
                    new Alert(Alert.AlertType.INFORMATION, "Удалять самого себя плохая идея", ButtonType.OK).showAndWait();
                    return;
                }

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
            if (employeeStatusBox.getSelectionModel().getSelectedItem() == null) {
                return;
            }
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

    protected User getSelectedEmployee() {
        return employeeTable.getSelectionModel().getSelectedItem();
    }

    /* *************** ПОКУПАТЕЛИ *************** */
    @FXML
    private Tab consumersWorkTab;
    @FXML
    private ComboBox<Product> productBox1;
    @FXML
    private ComboBox<Product> productComboBox;
    @FXML
    private ComboBox<SalePointType> salePointTypeBox1;
    @FXML
    private ComboBox<SalePoint> salePointBox3;
    @FXML
    private VBox consumersTableLayout;

    @FXML
    protected void getConsumersByProduct() {
        if (productComboBox.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите товар", ButtonType.OK).showAndWait();
            return;
        }
        consumersTableLayout.getChildren().clear();
        TableView<ConsumerByProductName> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ConsumerByProductName, String> firstName = new TableColumn<>();
        TableColumn<ConsumerByProductName, String> lastName = new TableColumn<>();
        TableColumn<ConsumerByProductName, String> middleName = new TableColumn<>();

        firstName.setText("Имя");
        lastName.setText("Фамилиия");
        middleName.setText("Отчество");

        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        middleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));

        table.getColumns().addAll(List.of(lastName, firstName, middleName));

        Label label = new Label();
        try {
            List<ConsumerByProductName> list = consumerService.getConsumersByProductName(productComboBox.getSelectionModel().getSelectedItem().getProductName());
            table.getItems().addAll(list);
            label.setText("Общее число покупателей: " + list.size());
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        }
        consumersTableLayout.getChildren().add(table);
        consumersTableLayout.getChildren().add(label);
    }

    @FXML
    protected void getConsumerProductInfo() {
        if (productBox1.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите товар", ButtonType.OK).showAndWait();
            return;
        }
        consumersTableLayout.getChildren().clear();
        TableView<ConsumerProductInfo> table = initializeConsumerProductInfoTable();

        try {
            table.getItems().addAll(consumerService.getConsumerProductInfo(productBox1.getSelectionModel().getSelectedItem().getProductName()));
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        }

        consumersTableLayout.getChildren().add(table);
    }

    @FXML
    protected void getConsumerProductInfoBySalePointTypeName() {
        if (productBox1.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите товар", ButtonType.OK).showAndWait();
            return;
        }
        if (salePointTypeBox1.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите тип торговой точки", ButtonType.OK).showAndWait();
            return;
        }
        consumersTableLayout.getChildren().clear();
        TableView<ConsumerProductInfo> table = initializeConsumerProductInfoTable();

        try {
            table.getItems().addAll(consumerService.getConsumerProductInfoBySalePointTypeName(
                    productBox1.getSelectionModel().getSelectedItem().getProductName(),
                    salePointTypeBox1.getSelectionModel().getSelectedItem().getTypeName()));
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        }

        consumersTableLayout.getChildren().add(table);
    }

    @FXML
    protected void getConsumerProductInfoBySalePointId() {
        if (productBox1.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите товар", ButtonType.OK).showAndWait();
            return;
        }
        if (salePointBox3.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите торговую точку", ButtonType.OK).showAndWait();
            return;
        }
        consumersTableLayout.getChildren().clear();
        TableView<ConsumerProductInfo> table = initializeConsumerProductInfoTable();

        try {
            table.getItems().addAll(consumerService.getConsumerProductInfoBySalePointId(
                    productBox1.getSelectionModel().getSelectedItem().getProductName(),
                    salePointBox3.getSelectionModel().getSelectedItem().getSalePointId()));
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        }

        consumersTableLayout.getChildren().add(table);
    }

    private TableView<ConsumerProductInfo> initializeConsumerProductInfoTable() {
        TableView<ConsumerProductInfo> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ConsumerProductInfo, String> salePointName = new TableColumn<>();
        TableColumn<ConsumerProductInfo, String> typeName = new TableColumn<>();
        TableColumn<ConsumerProductInfo, String> firstName = new TableColumn<>();
        TableColumn<ConsumerProductInfo, String> lastName = new TableColumn<>();
        TableColumn<ConsumerProductInfo, String> middleName = new TableColumn<>();
        TableColumn<ConsumerProductInfo, LocalDate> transactionDate = new TableColumn<>();

        salePointName.setCellValueFactory(new PropertyValueFactory<>("salePointName"));
        typeName.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        middleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        transactionDate.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

        transactionDate.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.transactionDateConverter));

        salePointName.setText("Торговая точка");
        typeName.setText("Тип торговой точки");
        firstName.setText("Имя");
        lastName.setText("Фамилия");
        middleName.setText("Отчество");
        transactionDate.setText("Дата транзакции");

        table.getColumns().addAll(List.of(salePointName, typeName, lastName, firstName, middleName, transactionDate));

        return table;
    }

    @FXML
    protected void getActiveConsumers() {
        TableView<ActiveConsumer> table = new TableView<>();

        TableColumn<ActiveConsumer, Integer> salePointId = new TableColumn<>();
        TableColumn<ActiveConsumer, String> firstName = new TableColumn<>();
        TableColumn<ActiveConsumer, String> lastName = new TableColumn<>();
        TableColumn<ActiveConsumer, String> middleName = new TableColumn<>();
        TableColumn<ActiveConsumer, Integer> consumersCount = new TableColumn<>();

        salePointId.setCellValueFactory(new PropertyValueFactory<>("salePointId"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        middleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        consumersCount.setCellValueFactory(new PropertyValueFactory<>("consumersCount"));

        salePointId.setText("ID торговой точки");
        firstName.setText("Имя");
        lastName.setText("Фамилия");
        middleName.setText("Отчество");
        consumersCount.setText("Количество");

        table.getColumns().addAll(List.of(salePointId, lastName, firstName, middleName, consumersCount));

        try {
            table.getItems().addAll(consumerService.getActiveConsumers());
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        }

        consumersTableLayout.getChildren().add(table);
    }

    @FXML
    protected void consumersWorkTabSelected() {
        if (consumersWorkTab.isSelected()) {
            consumersTableLayout.getChildren().clear();
            productComboBox.getItems().clear();
            productBox1.getItems().clear();
            salePointBox3.getItems().clear();
            salePointTypeBox1.getItems().clear();

            try {
                productComboBox.getItems().addAll(productService.getProducts());
                productBox1.getItems().addAll(productService.getProducts());
                salePointBox3.getItems().addAll(salePointService.getSalePoints());
                salePointTypeBox1.getItems().addAll(salePointTypeService.getSalePointTypes());
            } catch (SQLException exception) {
                new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            }
        }
    }

    /* ***************** ТОРГОВЫЕ ТОЧКИ ***************** */
    @FXML
    private Tab salePointsWorkTab;
    @FXML
    private ComboBox<SalePoint> salePointComboBox;
    @FXML
    private ComboBox<SalePoint> salePointComboBox3;
    @FXML
    private VBox salePointsTableLayout;
    @FXML
    private ComboBox<SalePoint> salePointBox4;
    @FXML
    private ComboBox<SalePoint> salePointBox;
    @FXML
    private ComboBox<SalePointType> salePointTypeComboBox;
    @FXML
    private ComboBox<Product> productComboBox2;
    @FXML
    private ComboBox<SalePointType> salePointTypeBox4;
    @FXML
    private ComboBox<Product> productComboBox3;
    @FXML
    private ComboBox<Product> productBox;
    @FXML
    private ComboBox<Supplier> supplierBox2;
    @FXML
    private ComboBox<SalePoint> salePointBox1;

    @FXML
    protected void getNomenclature() {
        if (salePointComboBox.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите торговую точку", ButtonType.OK).showAndWait();
            return;
        }
        salePointsTableLayout.getChildren().clear();
        TableView<Nomenclature> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Nomenclature, String> productName = new TableColumn<>();
        TableColumn<Nomenclature, Integer> quantity = new TableColumn<>();
        TableColumn<Nomenclature, Double> price = new TableColumn<>();
        TableColumn<Nomenclature, Double> discount = new TableColumn<>();

        productName.setText("Наименование товара");
        quantity.setText("Количество");
        price.setText("Стоимость");
        discount.setText("Скидка");

        productName.setCellValueFactory(new PropertyValueFactory<>("ProductName"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        price.setCellValueFactory(new PropertyValueFactory<>("Price"));
        discount.setCellValueFactory(new PropertyValueFactory<>("Discount"));

        price.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.moneyStringConverter));
        discount.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.productDiscountConverter));

        table.getColumns().addAll(List.of(productName, quantity, price, discount));

        try {
            table.getItems().addAll(salePointService.getNomenclature(salePointComboBox.getSelectionModel().getSelectedItem().getSalePointId()));
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        }
        salePointsTableLayout.getChildren().add(table);
    }

    @FXML
    protected void getSalePointsByProductName() {
        if (productComboBox2.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите товар", ButtonType.OK).showAndWait();
            return;
        }
        TableView<SalePointByProduct> table = initializeSalePointByProductTable();
        salePointsTableLayout.getChildren().clear();

        try {
            table.getItems().addAll(salePointService.getSalePointsByProductName(productComboBox2.getSelectionModel().getSelectedItem().getProductName()));
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        }

        salePointsTableLayout.getChildren().add(table);
    }

    @FXML
    protected void getSalePointsByProductNameAndTypeName() {
        if (productComboBox2.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите товар", ButtonType.OK).showAndWait();
            return;
        }
        if (salePointTypeComboBox.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите тип торговой точки", ButtonType.OK).showAndWait();
            return;
        }
        salePointsTableLayout.getChildren().clear();
        TableView<SalePointByProduct> table = initializeSalePointByProductTable();

        try {
            table.getItems().addAll(salePointService.getSalePointsByProductNameAndTypeName(
                    productComboBox2.getSelectionModel().getSelectedItem().getProductName(),
                    salePointTypeComboBox.getSelectionModel().getSelectedItem().getTypeName()
            ));
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        }

        salePointsTableLayout.getChildren().add(table);
    }

    @FXML
    protected void getSalePointsByProductNameAndSalePointId() {
        if (productComboBox2.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите товар", ButtonType.OK).showAndWait();
            return;
        }
        if (salePointComboBox3.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите торговую точку", ButtonType.OK).showAndWait();
            return;
        }
        salePointsTableLayout.getChildren().clear();
        TableView<SalePointByProduct> table = initializeSalePointByProductTable();

        try {
            table.getItems().addAll(salePointService.getSalePointsByProductNameAndSalePointId(
                    productComboBox2.getSelectionModel().getSelectedItem().getProductName(),
                    salePointComboBox3.getSelectionModel().getSelectedItem().getSalePointId()));
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        }

        salePointsTableLayout.getChildren().add(table);
    }

    private TableView<SalePointByProduct> initializeSalePointByProductTable() {
        TableView<SalePointByProduct> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<SalePointByProduct, String> salePointName = new TableColumn<>();
        TableColumn<SalePointByProduct, String> typeName = new TableColumn<>();
        TableColumn<SalePointByProduct, String> productName = new TableColumn<>();
        TableColumn<SalePointByProduct, Integer> quantity = new TableColumn<>();
        TableColumn<SalePointByProduct, Double> price = new TableColumn<>();
        TableColumn<SalePointByProduct, Double> discount = new TableColumn<>();

        salePointName.setCellValueFactory(new PropertyValueFactory<>("salePointName"));
        typeName.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        discount.setCellValueFactory(new PropertyValueFactory<>("discount"));

        salePointName.setText("Торговая точка");
        typeName.setText("Тип торговой точки");
        productName.setText("Наименование товара");
        quantity.setText("Количество");
        price.setText("Стоимость");
        discount.setText("Скидка");

        price.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.moneyStringConverter));
        discount.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.productDiscountConverter));

        table.getColumns().addAll(List.of(salePointName, typeName, productName, quantity, price, discount));

        return table;
    }

    @FXML
    protected void getSalesVolumeByProductId() {
        if (productComboBox3.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите товар", ButtonType.OK).showAndWait();
            return;
        }
        salePointsTableLayout.getChildren().clear();
        TableView<SalesVolume> table = initializeSalesVolumeTable();

        try {
            table.getItems().addAll(salePointService.getSalesVolumeByProductId(productComboBox3.getSelectionModel().getSelectedItem().getProductId()));
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        }

        salePointsTableLayout.getChildren().add(table);
    }

    @FXML
    protected void getSalesVolumeByProductIdAndSalePointTypeName() {
        if (productComboBox3.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите товар", ButtonType.OK).showAndWait();
            return;
        }
        if (salePointTypeBox4.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите тип торговой точки", ButtonType.OK).showAndWait();
            return;
        }
        salePointsTableLayout.getChildren().clear();
        TableView<SalesVolume> table = initializeSalesVolumeTable();

        try {
            table.getItems().addAll(salePointService.getSalesVolumeByProductIdAndSalePointTypeName(
                    productComboBox3.getSelectionModel().getSelectedItem().getProductId(),
                    salePointTypeBox4.getSelectionModel().getSelectedItem().getTypeName()));
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        }

        salePointsTableLayout.getChildren().add(table);
    }

    @FXML
    protected void getSalesVolumeByProductIdAndSalePointId() {
        if (productComboBox3.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите товар", ButtonType.OK).showAndWait();
            return;
        }
        if (salePointBox.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите торговую точку", ButtonType.OK).showAndWait();
            return;
        }
        salePointsTableLayout.getChildren().clear();
        TableView<SalesVolume> table = initializeSalesVolumeTable();

        try {
            table.getItems().addAll(salePointService.getSalesVolumeByProductIdAndSalePointId(
                    productComboBox3.getSelectionModel().getSelectedItem().getProductId(),
                    salePointBox.getSelectionModel().getSelectedItem().getSalePointId()));
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        }

        salePointsTableLayout.getChildren().add(table);
    }

    @FXML
    protected void getDeliveriesByProductName() {
        if (productBox.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите товар", ButtonType.OK).showAndWait();
            return;
        }
        if (supplierBox2.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите поставщика", ButtonType.OK).showAndWait();
            return;
        }
        salePointsTableLayout.getChildren().clear();
        TableView<Deliveries> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Deliveries, Integer> requestId = new TableColumn<>();
        TableColumn<Deliveries, String> salePointName = new TableColumn<>();
        TableColumn<Deliveries, String> typeName = new TableColumn<>();
        TableColumn<Deliveries, String> productName = new TableColumn<>();
        TableColumn<Deliveries, Integer> quantity = new TableColumn<>();
        TableColumn<Deliveries, Double> price = new TableColumn<>();

        requestId.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        salePointName.setCellValueFactory(new PropertyValueFactory<>("salePointName"));
        typeName.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        price.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.moneyStringConverter));

        requestId.setText("Номер заявки");
        salePointName.setText("Торговая точка");
        typeName.setText("Тип торговой точки");
        productName.setText("Наименование товара");
        quantity.setText("Количество");
        price.setText("Стоимость");

        table.getColumns().addAll(List.of(requestId, salePointName, typeName, productName, quantity, price));

        try {
            table.getItems().addAll(consumerService.getDeliveriesByProductName(
                    productBox.getSelectionModel().getSelectedItem().getProductName(),
                    supplierBox2.getSelectionModel().getSelectedItem().getSupplierId()));
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        }
        salePointsTableLayout.getChildren().add(table);
    }

    @FXML
    protected void getProfitability() {
        if (salePointBox1.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите торговую точку", ButtonType.OK).showAndWait();
            return;
        }
        salePointsTableLayout.getChildren().clear();
        Label label = new Label();

        try {
            DecimalFormat decimalFormat = new DecimalFormat("#.###");
            label.setText("Рентабельность: " + decimalFormat.format(salePointService.getProfitability(
                    salePointBox1.getSelectionModel().getSelectedItem().getSalePointId())));
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        }

        salePointsTableLayout.getChildren().add(label);
    }

    private TableView<SalesVolume> initializeSalesVolumeTable() {
        TableView<SalesVolume> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<SalesVolume, String> salePointName = new TableColumn<>();
        TableColumn<SalesVolume, Integer> quantity = new TableColumn<>();

        salePointName.setCellValueFactory(new PropertyValueFactory<>("salePointName"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        salePointName.setText("Торговая точка");
        quantity.setText("Количество");

        table.getColumns().addAll(List.of(salePointName, quantity));

        return table;
    }

    @FXML
    protected void getTradeTurnover() {
        if (salePointBox4.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите торговую точку", ButtonType.OK).showAndWait();
            return;
        }
        salePointsTableLayout.getChildren().clear();
        TableView<TradeTurnover> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<TradeTurnover, String> salePointName = new TableColumn<>();
        TableColumn<TradeTurnover, String> typeName = new TableColumn<>();
        TableColumn<TradeTurnover, String> productName = new TableColumn<>();
        TableColumn<TradeTurnover, Integer> quantity = new TableColumn<>();
        TableColumn<TradeTurnover, Double> price = new TableColumn<>();
        TableColumn<TradeTurnover, Double> discount = new TableColumn<>();
        TableColumn<TradeTurnover, Double> total = new TableColumn<>();

        salePointName.setText("Торговая точка");
        typeName.setText("Тип тороговой точки");
        productName.setText("Наименование продукта");
        quantity.setText("Колиечство");
        price.setText("Стоимость");
        discount.setText("Скидка");
        total.setText("Итого");

        salePointName.setCellValueFactory(new PropertyValueFactory<>("salePointName"));
        typeName.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        discount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        total.setCellValueFactory(new PropertyValueFactory<>("total"));

        price.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.moneyStringConverter));
        discount.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.productDiscountConverter));
        total.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.moneyStringConverter));

        table.getColumns().addAll(List.of(salePointName, typeName, productName, quantity, price, discount, total));

        Label label = new Label();
        try {
            List<TradeTurnover> list = salePointService.getTradeTurnover(salePointBox4.getSelectionModel().getSelectedItem().getSalePointId());
            table.getItems().addAll(list);
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            label.setText(list.size() == 0 ? "Полная сумма: 0,00₽" : "Полная сумма: " + decimalFormat.format(list.get(0).getSumma()) + "₽");
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        }

        salePointsTableLayout.getChildren().add(table);
        salePointsTableLayout.getChildren().add(label);
    }

    @FXML
    protected void salePointsWorkTabSelected() {
        if (salePointsWorkTab.isSelected()) {
            salePointsTableLayout.getChildren().clear();
            salePointComboBox.getItems().clear();
            salePointComboBox3.getItems().clear();
            productComboBox3.getItems().clear();
            salePointTypeBox4.getItems().clear();
            salePointTypeComboBox.getItems().clear();
            productComboBox2.getItems().clear();
            salePointBox.getItems().clear();
            salePointBox4.getItems().clear();
            salePointBox1.getItems().clear();
            productBox.getItems().clear();
            supplierBox2.getItems().clear();
            try {
                salePointComboBox.getItems().addAll(salePointService.getSalePoints());
                salePointComboBox3.getItems().addAll(salePointService.getSalePoints());
                salePointTypeComboBox.getItems().addAll(salePointTypeService.getSalePointTypes());
                productComboBox2.getItems().addAll(productService.getProducts());
                salePointBox.getItems().addAll(salePointService.getSalePoints());
                salePointTypeBox4.getItems().addAll(salePointTypeService.getSalePointTypes());
                productComboBox3.getItems().addAll(productService.getProducts());
                productBox.getItems().addAll(productService.getProducts());
                salePointBox4.getItems().addAll(salePointService.getSalePoints());
                salePointBox1.getItems().addAll(salePointService.getSalePoints());
                supplierBox2.getItems().addAll(supplierService.getSuppliers());
            } catch (SQLException exception) {
                new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            }
        }
    }

    /* ***************** ПРОДАВЦЫ ***************** */
    @FXML
    private Tab sellersWorkTab;
    @FXML
    private VBox sellersWorkTableLayout;
    @FXML
    private ComboBox<SalePointType> salePointTypeBox;
    @FXML
    private ComboBox<Seller> sellersBox;
    @FXML
    private ComboBox<SalePointType> salePointTypeBox2;
    @FXML
    private ComboBox<SalePointType> salePointTypeBox3;
    @FXML
    private ComboBox<SalePoint> salePointBox2;

    @FXML
    protected void getAllSalePointsSellersButtonClick() {
        sellersWorkTableLayout.getChildren().clear();
        TableView<SalePointsSellers> table = initializeSellersTable();

        try {
            table.getItems().addAll(userService.getAllSalePointsSellers());
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        }

        sellersWorkTableLayout.getChildren().add(table);
    }

    @FXML
    protected void getSalePointSellersButtonClick() {
        if (salePointTypeBox.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите тип торговой точки", ButtonType.OK).showAndWait();
            return;
        }
        sellersWorkTableLayout.getChildren().clear();
        TableView<SalePointsSellers> table = initializeSellersTable();

        try {
            table.getItems().addAll(userService.getSalePointSellers(salePointTypeBox.getSelectionModel().getSelectedItem().getTypeName()));
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        }

        sellersWorkTableLayout.getChildren().add(table);
    }

    @FXML
    protected void getSalePointSellerButtonClick() {
        if (sellersBox.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Не выбран продавец", ButtonType.OK).showAndWait();
            return;
        }
        sellersWorkTableLayout.getChildren().clear();

        TableView<SalePointsSellers> table = initializeSellersTable();

        try {
            table.getItems().add(userService.getSalePointSeller(sellersBox.getSelectionModel().getSelectedItem().getUserLogin()));
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        } catch (GetDBInformationException exception) {
            new Alert(Alert.AlertType.INFORMATION, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        }
        sellersWorkTableLayout.getChildren().add(table);
    }

    @FXML
    protected void getRelationButtonClick() {
        if (salePointTypeBox2.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите тип торговой точки", ButtonType.OK).showAndWait();
            return;
        }
        sellersWorkTableLayout.getChildren().clear();
        Label label = new Label();
        try {
            DecimalFormat decimalFormat = new DecimalFormat("#.###");
            label.setText("Отношение объема продаж к объему торговых площадей: " +
                    decimalFormat.format(userService.getRelation(salePointTypeBox2.getSelectionModel().getSelectedItem().getTypeName())));
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        }
        sellersWorkTableLayout.getChildren().add(label);
    }

    @FXML
    protected void sellersWorkTabSelected() {
        if (sellersWorkTab.isSelected()) {
            sellersWorkTableLayout.getChildren().clear();
            salePointTypeBox.getItems().clear();
            salePointTypeBox2.getItems().clear();
            sellersBox.getItems().clear();
            salePointTypeBox3.getItems().clear();
            salePointBox2.getItems().clear();
            try {
                List<SalePointType> types = salePointTypeService.getSalePointTypes();
                salePointTypeBox.getItems().addAll(types);
                salePointTypeBox2.getItems().addAll(types);
                salePointTypeBox3.getItems().addAll(types);
                salePointBox2.getItems().addAll(salePointService.getSalePoints());
                List<User> users = userService.getUsers();

                for (User user : users) {
                    if (userService.isSeller(user.getUserLogin())) {
                        sellersBox.getItems().add(userService.getSellerByLogin(user.getUserLogin()));
                    }
                }
            } catch (SQLException | NoUserByLoginException exception) {
                new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            }
        }
    }

    @FXML
    protected void getSellersSalary() {
        sellersWorkTableLayout.getChildren().clear();
        TableView<SellerSalary> table = initializeSellerSalaryTable();

        try {
            table.getItems().addAll(userService.getSellersSalary());
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        }

        sellersWorkTableLayout.getChildren().add(table);
    }

    @FXML
    protected void getSellersSalaryBySalePointType() {
        if (salePointTypeBox3.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите тип торговой точки", ButtonType.OK).showAndWait();
            return;
        }
        sellersWorkTableLayout.getChildren().clear();
        TableView<SellerSalary> table = initializeSellerSalaryTable();


        try {
            table.getItems().addAll(userService.getSellersSalaryBySalePointType(salePointTypeBox3.getSelectionModel().getSelectedItem().getTypeName()));
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        }

        sellersWorkTableLayout.getChildren().add(table);
    }

    @FXML
    protected void getSellersSalaryBySalePointId() {
        if (salePointBox2.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Выберите торговую точку", ButtonType.OK).showAndWait();
            return;
        }
        sellersWorkTableLayout.getChildren().clear();
        TableView<SellerSalary> table = initializeSellerSalaryTable();

        try {
            table.getItems().addAll(userService.getSellersSalaryBySalePointId(salePointBox2.getSelectionModel().getSelectedItem().getSalePointId()));
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        }

        sellersWorkTableLayout.getChildren().add(table);
    }

    private TableView<SellerSalary> initializeSellerSalaryTable() {
        TableView<SellerSalary> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<SellerSalary, String> salePointName = new TableColumn<>();
        TableColumn<SellerSalary, String> typeName = new TableColumn<>();
        TableColumn<SellerSalary, String> firstName = new TableColumn<>();
        TableColumn<SellerSalary, String> lastName = new TableColumn<>();
        TableColumn<SellerSalary, String> middleName = new TableColumn<>();
        TableColumn<SellerSalary, Double> salary = new TableColumn<>();

        salePointName.setCellValueFactory(new PropertyValueFactory<>("salePointName"));
        typeName.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        middleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        salary.setCellValueFactory(new PropertyValueFactory<>("salary"));

        salary.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.moneyStringConverter));

        salePointName.setText("Торговая точка");
        typeName.setText("Тип торговой точки");
        firstName.setText("Имя");
        lastName.setText("Фамилия");
        middleName.setText("Отчество");
        salary.setText("Зарплата");

        table.getColumns().addAll(List.of(salePointName, typeName, lastName, firstName, middleName, salary));
        return table;
    }

    private TableView<SalePointsSellers> initializeSellersTable() {
        TableView<SalePointsSellers> table = new TableView<>();

        TableColumn<SalePointsSellers, String> sellerLogin = new TableColumn<>();
        TableColumn<SalePointsSellers, String> firstName = new TableColumn<>();
        TableColumn<SalePointsSellers, String> lastName = new TableColumn<>();
        TableColumn<SalePointsSellers, String> middleName = new TableColumn<>();
        TableColumn<SalePointsSellers, String> salePointName = new TableColumn<>();
        TableColumn<SalePointsSellers, String> typeName = new TableColumn<>();
        TableColumn<SalePointsSellers, Double> total = new TableColumn<>();

        sellerLogin.setText("Логин");
        firstName.setText("Имя");
        lastName.setText("Фамилия");
        middleName.setText("Отчество");
        salePointName.setText("Торговая точка");
        typeName.setText("Тип торговой точки");
        total.setText("Итого");

        sellerLogin.setCellValueFactory(new PropertyValueFactory<>("SellerLogin"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        middleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        salePointName.setCellValueFactory(new PropertyValueFactory<>("salePointName"));
        typeName.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        total.setCellValueFactory(new PropertyValueFactory<>("total"));

        total.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.moneyStringConverter));

        table.getColumns().addAll(List.of(sellerLogin, lastName, firstName, middleName, salePointName,
                typeName, total));

        return table;
    }

    /* ********************** ПОСТАВЩИКИ ********************** */
    @FXML
    private Tab suppliersWorkTab;
    @FXML
    private TextField orderIdTextField;
    @FXML
    private ComboBox<Product> productsForSuppliersBox;
    @FXML
    private VBox suppliersWorkTableLayout;

    @FXML
    protected void getSuppliersByProductButtonClick() {
        if (productsForSuppliersBox.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Товар не выбран", ButtonType.OK).showAndWait();
            return;
        }
        suppliersWorkTableLayout.getChildren().clear();

        TableView<SuppliersByProduct> table = new TableView<>();
        Label label = new Label();

        TableColumn<SuppliersByProduct, String> nameColumn = new TableColumn<>();
        nameColumn.setText("Наименование поставщика");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("SupplierName"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().add(nameColumn);

        try {
            List<SuppliersByProduct> list = supplierService.getSuppliersByProduct(productsForSuppliersBox.getSelectionModel().getSelectedItem());
            table.getItems().addAll(list);

            label.setText("Количество поставщиков: " + list.size());
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        }

        suppliersWorkTableLayout.getChildren().add(table);
        suppliersWorkTableLayout.getChildren().add(label);
    }

    @FXML
    protected void getSuppliersByOrderButtonClick() {
        int orderId;
        try {
            orderId = Integer.parseInt(orderIdTextField.getText().trim());
        } catch (NumberFormatException exception) {
            new Alert(Alert.AlertType.INFORMATION, "Номер заказа должен быть целым числом", ButtonType.OK).showAndWait();
            return;
        }
        if (orderId <= 0) {
            new Alert(Alert.AlertType.INFORMATION, "Номер заказа должен быть больше 0", ButtonType.OK).showAndWait();
            return;
        }
        suppliersWorkTableLayout.getChildren().clear();

        TableView<SuppliersByOrder> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<SuppliersByOrder, String> salePointName = new TableColumn<>();
        TableColumn<SuppliersByOrder, String> typeName = new TableColumn<>();
        TableColumn<SuppliersByOrder, String> productName = new TableColumn<>();
        TableColumn<SuppliersByOrder, Integer> quantity = new TableColumn<>();
        TableColumn<SuppliersByOrder, Double> price = new TableColumn<>();

        salePointName.setText("Торговая точка");
        typeName.setText("Тип торговой точки");
        productName.setText("Наименование товара");
        quantity.setText("Количество");
        price.setText("Стоимость");

        salePointName.setCellValueFactory(new PropertyValueFactory<>("salePointName"));
        typeName.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        price.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.moneyStringConverter));

        table.getColumns().addAll(List.of(salePointName, typeName, productName, quantity, price));
        suppliersWorkTableLayout.getChildren().add(table);

        try {
            table.getItems().addAll(supplierService.getSuppliersByOrder(orderId));
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void suppliersWorkTabSelected() {
        if (suppliersWorkTab.isSelected()) {
            suppliersWorkTableLayout.getChildren().clear();
            productsForSuppliersBox.getItems().clear();
            try {
                productsForSuppliersBox.getItems().addAll(productService.getProducts());
            } catch (SQLException exception) {
                new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            }
        }
    }
}
