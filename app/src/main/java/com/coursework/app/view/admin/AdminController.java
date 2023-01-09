package com.coursework.app.view.admin;

import com.coursework.app.TradeOrganizationApp;
import com.coursework.app.entity.*;
import com.coursework.app.entity.queries.SalePointsSellers;
import com.coursework.app.entity.queries.SuppliersByOrder;
import com.coursework.app.entity.queries.SuppliersByProduct;
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

        /* *********** ПРОДАВЦЫ *********** */
        salePointTypeBox.setConverter(StringConverterUtils.salePointTypeStringConverter);
        salePointTypeBox2.setConverter(StringConverterUtils.salePointTypeStringConverter);
        sellersBox.setConverter(StringConverterUtils.sellerNameStringConverter);
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

    @FXML
    private Tab consumersWorkTab;
    @FXML
    private Tab salePointsWorkTab;

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
            try {
                List<SalePointType> types = salePointTypeService.getSalePointTypes();
                salePointTypeBox.getItems().addAll(types);
                salePointTypeBox2.getItems().addAll(types);
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
