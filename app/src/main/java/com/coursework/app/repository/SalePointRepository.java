package com.coursework.app.repository;

import com.coursework.app.entity.SalePoint;
import com.coursework.app.entity.SalePointProduct;
import com.coursework.app.entity.queries.Nomenclature;
import com.coursework.app.entity.queries.SalePointByProduct;
import com.coursework.app.entity.queries.SalesVolume;
import com.coursework.app.entity.queries.TradeTurnover;
import com.coursework.app.utils.DBProperties;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalePointRepository {
    private final SalePointTypeRepository salePointTypeRepository = new SalePointTypeRepository();
    private final ProductRepository productRepository = new ProductRepository();

    public List<SalePoint> getSalePoints() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM SalePoints");
            ResultSet resultSet = statement.executeQuery();
            List<SalePoint> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new SalePoint(resultSet.getInt("SalePointId"),
                        salePointTypeRepository.getSalePointTypeById(resultSet.getInt("TypeId")),
                        resultSet.getDouble("PointSize"),
                        resultSet.getDouble("RentalPrice"),
                        resultSet.getDouble("CommunalService"),
                        resultSet.getInt("CountersNumber"),
                        resultSet.getBoolean("IsActive"),
                        resultSet.getString("SalePointName")));
            }
            return list;
        }
    }

    public SalePoint getSalePointById(int id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM SalePoints WHERE SalePointId=?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new SalePoint(resultSet.getInt("SalePointId"),
                        salePointTypeRepository.getSalePointTypeById(resultSet.getInt("TypeId")),
                        resultSet.getDouble("PointSize"),
                        resultSet.getDouble("RentalPrice"),
                        resultSet.getDouble("CommunalService"),
                        resultSet.getInt("CountersNumber"),
                        resultSet.getBoolean("IsActive"),
                        resultSet.getString("SalePointName"));
            }
            return null;
        }
    }

    public SalePoint addSalePoint(SalePoint salePoint) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO SalePoints(TypeId, PointSize, RentalPrice, CommunalService, CountersNumber, IsActive, SalePointName)
                        VALUES (?,?,?,?,?,?,?)""", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, salePoint.getType().getTypeId());
            statement.setDouble(2, salePoint.getPointSize());
            statement.setDouble(3, salePoint.getRentalPrice());
            statement.setDouble(4, salePoint.getCommunalService());
            statement.setInt(5, salePoint.getCountersNumber());
            statement.setBoolean(6, salePoint.getIsActive());
            statement.setString(7, salePoint.getSalePointName());
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                salePoint.setSalePointId(resultSet.getInt(1));
                return salePoint;
            }
            return null;
        }
    }

    public void changeActiveStatusById(int id, boolean status) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    UPDATE SalePoints SET IsActive=?
                        WHERE SalePointId=?""");
            statement.setBoolean(1, status);
            statement.setInt(2, id);
            statement.execute();
        }
    }

    public List<SalePointProduct> getSalePointProducts(int salePointId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT * FROM ProductsSalePoints WHERE SalePointId=?""");
            statement.setInt(1, salePointId);
            ResultSet resultSet = statement.executeQuery();
            List<SalePointProduct> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new SalePointProduct(productRepository.getProductById(resultSet.getInt("ProductId")),
                        getSalePointById(resultSet.getInt("SalePointId")),
                        resultSet.getInt("Quantity"),
                        resultSet.getDouble("Price"),
                        resultSet.getDouble("Discount")));
            }
            return list;
        }
    }

    public SalePointProduct addSalePointProduct(SalePointProduct product) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement;
            SalePointProduct product_;
            if (isExistsProduct(product.getSalePoint().getSalePointId(), product.getProduct().getProductId())) {
                statement = connection.prepareStatement("""
                        UPDATE ProductsSalePoints SET Quantity=? WHERE SalePointId=? AND ProductId=?""");
                int newQuantity = getSalePointProductQuantity(product.getSalePoint().getSalePointId(),
                        product.getProduct().getProductId()) + product.getQuantity();
                statement.setInt(1, newQuantity);
                statement.setInt(2, product.getSalePoint().getSalePointId());
                statement.setInt(3, product.getProduct().getProductId());
                product_ = new SalePointProduct(product.getProduct(), product.getSalePoint(),
                        newQuantity, product.getPrice(), product.getDiscount());
            } else {
                statement = connection.prepareStatement("""
                        INSERT INTO ProductsSalePoints(ProductId, SalePointId, Quantity, Price, Discount)  VALUES (?,?,?,?,?)""");
                statement.setInt(1, product.getProduct().getProductId());
                statement.setInt(2, product.getSalePoint().getSalePointId());
                statement.setInt(3, product.getQuantity());
                statement.setDouble(4, product.getPrice());
                statement.setDouble(5, product.getDiscount());
                product_ = product;
            }
            statement.execute();

            return product_;
        }
    }

    public void changeSalePointProductQuantity(int productId, int salePointId, int quantity) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    UPDATE ProductsSalePoints SET Quantity=? WHERE ProductId=? AND SalePointId=?""");
            statement.setInt(1, quantity);
            statement.setInt(2, productId);
            statement.setInt(3, salePointId);
            statement.execute();
        }
    }

    public SalePoint getSalePointBySellerLogin(String sellerLogin) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT Halls.SalePointId FROM Sellers
                        JOIN Halls ON Sellers.HallId = Halls.HallId
                    WHERE Sellers.UserLogin=?""");
            statement.setString(1, sellerLogin);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getSalePointById(resultSet.getInt("SalePointId"));
            }
            return null;
        }
    }

    public SalePoint getSalePointBySuperVisorLogin(String login) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT SalePointId FROM SuperVisors
                        JOIN Sections ON SuperVisors.SectionId = Sections.SectionId
                            JOIN Halls ON Sections.HallId = Halls.HallId
                    WHERE UserLogin=?""");
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getSalePointById(resultSet.getInt("SalePointId"));
            }
            return null;
        }
    }

    public int getSalePointProductQuantity(int salePointId, int productId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT Quantity FROM ProductsSalePoints WHERE SalePointId = ? AND ProductId = ?""");
            statement.setInt(1, salePointId);
            statement.setInt(2, productId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        }
    }

    private boolean isExistsProduct(int salePointId, int productId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT EXISTS(SELECT * FROM ProductsSalePoints WHERE SalePointId = ? AND ProductId = ?)""");
            statement.setInt(1, salePointId);
            statement.setInt(2, productId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBoolean(1);
            }

            return false;
        }
    }

    /* ***************** Запросы ***************** */

    // Получить номенклатуру и объем товаров в указанной торговой точке
    public List<Nomenclature> getNomenclature(int salePointId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT ProductName, Quantity, Price, Discount
                    FROM ProductsSalePoints
                             JOIN Products ON ProductsSalePoints.ProductId = Products.ProductId
                    WHERE SalePointId = ?""");
            statement.setInt(1, salePointId);
            ResultSet resultSet = statement.executeQuery();
            List<Nomenclature> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Nomenclature(resultSet.getString("ProductName"),
                        resultSet.getInt("Quantity"),
                        resultSet.getDouble("Price"),
                        resultSet.getDouble("Discount")));
            }
            return list;
        }
    }

    // Получить данные о рентабельности торговой точки: соотношение объема продаж к накладным расходам
    // (суммарная заработная плата продавцов + платежи за аренду, коммунальные услуги) за указанный период.
    public double getProfitability(int salePointId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT (Table1.ProductsSum / Table2.SalePointPrice) as Profitability
                    FROM (SELECT SUM(TransactionsProducts.Quantity * Price * (1 - Discount / 100)) as ProductsSum
                          FROM (SELECT TransactionId
                                FROM SalePoints
                                         JOIN Halls ON SalePoints.SalePointId = Halls.SalePointId
                                         JOIN Sellers ON Halls.HallId = Sellers.HallId
                                         JOIN Transactions ON Sellers.UserLogin = Transactions.SellerLogin
                                WHERE SalePoints.SalePointId = ?) as Transactions
                                   JOIN TransactionsProducts ON Transactions.TransactionId = TransactionsProducts.TransactionId
                                   JOIN ProductsSalePoints ON ProductsSalePoints.ProductId = TransactionsProducts.ProductId
                                   JOIN Products ON TransactionsProducts.ProductId = Products.ProductId) as Table1,
                         (SELECT (RentalPrice + SalePoints.CommunalService + SalaryTable.Salary) as SalePointPrice
                          FROM SalePoints,
                               (SELECT Sum(Salary) as Salary
                                FROM Sellers
                                         JOIN Halls ON Sellers.HallId = Halls.HallId
                                WHERE SalePointId = ?) as SalaryTable
                          WHERE SalePointId = ?) as Table2""");
            statement.setInt(1, salePointId);
            statement.setInt(2, salePointId);
            statement.setInt(3, salePointId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getDouble("Profitability") : 0.0;
        }
    }

    // Получить сведения об объеме и ценах на указанный товар по всем торговым точкам
    public List<SalePointByProduct> getSalePointsByProductName(String productName) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT SalePointName, TypeName, ProductName, Quantity, Price, Discount
                    FROM ProductsSalePoints
                             JOIN Products ON ProductsSalePoints.ProductId = Products.ProductId
                             JOIN SalePoints ON ProductsSalePoints.SalePointId = SalePoints.SalePointId
                             JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
                    WHERE ProductName = ?""");
            statement.setString(1, productName);
            ResultSet resultSet = statement.executeQuery();
            List<SalePointByProduct> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new SalePointByProduct(resultSet.getString("SalePointName"),
                        resultSet.getString("TypeName"),
                        resultSet.getString("ProductName"),
                        resultSet.getInt("Quantity"),
                        resultSet.getDouble("Price"),
                        resultSet.getDouble("Discount")));
            }
            return list;
        }
    }

    // Получить сведения об объеме и ценах на указанный товар по торговым точкам заданного типа
    public List<SalePointByProduct> getSalePointsByProductNameAndTypeName(String productName, String typeName) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT SalePointName, TypeName, ProductName, Quantity, Price, Discount
                    FROM ProductsSalePoints
                             JOIN Products ON ProductsSalePoints.ProductId = Products.ProductId
                             JOIN SalePoints ON ProductsSalePoints.SalePointId = SalePoints.SalePointId
                             JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
                    WHERE ProductName = ?
                      AND TypeName = ?""");
            statement.setString(1, productName);
            statement.setString(2, typeName);
            ResultSet resultSet = statement.executeQuery();
            List<SalePointByProduct> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new SalePointByProduct(resultSet.getString("SalePointName"),
                        resultSet.getString("TypeName"),
                        resultSet.getString("ProductName"),
                        resultSet.getInt("Quantity"),
                        resultSet.getDouble("Price"),
                        resultSet.getDouble("Discount")));
            }
            return list;
        }
    }

    // Получить сведения об объеме и ценах на указанный товар по конкретной торговой точке.
    public List<SalePointByProduct> getSalePointsByProductNameAndSalePointId(String productName, int salePointId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT SalePointName, TypeName, ProductName, Quantity, Price, Discount
                    FROM ProductsSalePoints
                             JOIN Products ON ProductsSalePoints.ProductId = Products.ProductId
                             JOIN SalePoints ON ProductsSalePoints.SalePointId = SalePoints.SalePointId
                             JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
                    WHERE ProductName = ?
                      AND ProductsSalePoints.SalePointId = ?""");
            statement.setString(1, productName);
            statement.setInt(2, salePointId);
            ResultSet resultSet = statement.executeQuery();
            List<SalePointByProduct> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new SalePointByProduct(resultSet.getString("SalePointName"),
                        resultSet.getString("TypeName"),
                        resultSet.getString("ProductName"),
                        resultSet.getInt("Quantity"),
                        resultSet.getDouble("Price"),
                        resultSet.getDouble("Discount")));
            }
            return list;
        }
    }

    // Получить данные о товарообороте торговой точки, либо всех торговых определенной группы за указанный период.
    public List<TradeTurnover> getTradeTurnover(int salePointId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT SalePointName,
                           TypeName,
                           ProductName,
                           TransactionsProducts.Quantity,
                           Price,
                           Discount,
                           (TransactionsProducts.Quantity * Price * (1 - Discount / 100)) as Total,
                           SumTable.summa
                    FROM (SELECT SUM(TransactionsProducts.Quantity * Price * (1 - Discount / 100)) as summa
                          FROM TransactionsProducts
                                   JOIN Products ON TransactionsProducts.ProductId = Products.ProductId
                                   JOIN ProductsSalePoints ON Products.ProductId = ProductsSalePoints.ProductId
                                   JOIN SalePoints ON ProductsSalePoints.SalePointId = SalePoints.SalePointId
                                   JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
                          WHERE SalePoints.SalePointId = ?) as SumTable,
                         TransactionsProducts
                             JOIN Products ON TransactionsProducts.ProductId = Products.ProductId
                             JOIN ProductsSalePoints ON Products.ProductId = ProductsSalePoints.ProductId
                             JOIN SalePoints ON ProductsSalePoints.SalePointId = SalePoints.SalePointId
                             JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
                    WHERE SalePoints.SalePointId = ?""");
            statement.setInt(1, salePointId);
            statement.setInt(2, salePointId);
            ResultSet resultSet = statement.executeQuery();

            List<TradeTurnover> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new TradeTurnover(resultSet.getString("SalePointName"),
                        resultSet.getString("TypeName"),
                        resultSet.getString("ProductName"),
                        resultSet.getInt("Quantity"),
                        resultSet.getDouble("Price"),
                        resultSet.getDouble("Discount"),
                        resultSet.getDouble("Total"),
                        resultSet.getDouble("summa")));
            }
            return list;
        }
    }

    // Получить данные об объеме продаж указанного товара за некоторый период по всем торговым точкам
    public List<SalesVolume> getSalesVolumeByProductId(int productId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT SalePointName, Quantity
                    FROM (SELECT ProductsSalePoints.SalePointId, SUM(TransactionsProducts.Quantity) as Quantity
                          FROM Transactions
                                   JOIN TransactionsProducts ON Transactions.TransactionId = TransactionsProducts.TransactionId
                                   JOIN ProductsSalePoints ON TransactionsProducts.ProductId = ProductsSalePoints.ProductId
                                   JOIN SalePoints ON ProductsSalePoints.SalePointId = SalePoints.SalePointId
                          WHERE TransactionsProducts.ProductId = ?
                          GROUP BY ProductsSalePoints.SalePointId) as QuantityTable
                             JOIN SalePoints ON QuantityTable.SalePointId = SalePoints.SalePointId""");
            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();

            List<SalesVolume> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new SalesVolume(resultSet.getString("SalePointName"),
                        resultSet.getInt("Quantity")));
            }
            return list;
        }
    }

    // Получить данные об объеме продаж указанного товара за некоторый период по торговым точкам заданного типа
    public List<SalesVolume> getSalesVolumeByProductIdAndSalePointTypeName(int productId, String typeName) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT SalePointName, Quantity
                    FROM (SELECT ProductsSalePoints.SalePointId, SUM(TransactionsProducts.Quantity) as Quantity
                          FROM Transactions
                                   JOIN TransactionsProducts ON Transactions.TransactionId = TransactionsProducts.TransactionId
                                   JOIN ProductsSalePoints ON TransactionsProducts.ProductId = ProductsSalePoints.ProductId
                                   JOIN SalePoints ON ProductsSalePoints.SalePointId = SalePoints.SalePointId
                          WHERE TransactionsProducts.ProductId = ?
                          GROUP BY ProductsSalePoints.SalePointId) as QuantityTable
                             JOIN SalePoints ON QuantityTable.SalePointId = SalePoints.SalePointId
                             JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
                    WHERE TypeName = ?""");
            statement.setInt(1, productId);
            statement.setString(2, typeName);
            ResultSet resultSet = statement.executeQuery();

            List<SalesVolume> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new SalesVolume(resultSet.getString("SalePointName"),
                        resultSet.getInt("Quantity")));
            }
            return list;
        }
    }


    // Получить данные об объеме продаж указанного товара за некоторый период по конкретной торговой точке
    public List<SalesVolume> getSalesVolumeByProductIdAndSalePointId(int productId, int salePointId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT SalePointName, Quantity
                    FROM (SELECT ProductsSalePoints.SalePointId, SUM(TransactionsProducts.Quantity) as Quantity
                          FROM Transactions
                                   JOIN TransactionsProducts ON Transactions.TransactionId = TransactionsProducts.TransactionId
                                   JOIN ProductsSalePoints ON TransactionsProducts.ProductId = ProductsSalePoints.ProductId
                                   JOIN SalePoints ON ProductsSalePoints.SalePointId = SalePoints.SalePointId
                          WHERE TransactionsProducts.ProductId = ?
                          GROUP BY ProductsSalePoints.SalePointId) as QuantityTable
                             JOIN SalePoints ON QuantityTable.SalePointId = SalePoints.SalePointId
                    WHERE QuantityTable.SalePointId = ?""");
            statement.setInt(1, productId);
            statement.setInt(2, salePointId);
            ResultSet resultSet = statement.executeQuery();

            List<SalesVolume> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new SalesVolume(resultSet.getString("SalePointName"),
                        resultSet.getInt("Quantity")));
            }
            return list;
        }
    }
}
