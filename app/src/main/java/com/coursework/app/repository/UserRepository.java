package com.coursework.app.repository;

import com.coursework.app.entity.Seller;
import com.coursework.app.entity.SuperVisor;
import com.coursework.app.entity.User;
import com.coursework.app.entity.queries.SalePointsSellers;
import com.coursework.app.utils.DBProperties;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final RoleRepository roleRepository = new RoleRepository();
    private final SectionRepository sectionRepository = new SectionRepository();
    private final HallRepository hallRepository = new HallRepository();

    public List<User> getUsers() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users");
            ResultSet resultSet = statement.executeQuery();
            List<User> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new User(resultSet.getString("UserLogin"),
                        resultSet.getString("Password"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        roleRepository.getRoleById(resultSet.getInt("RoleId")),
                        resultSet.getBoolean("IsActive")));
            }
            return list;
        }
    }

    public List<Seller> getSellers() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT Users.UserLogin, Users.Password, Users.FirstName, Users.LastName, Users.MiddleName, Users.RoleId, Users.IsActive, Sellers.HallId, Sellers.Salary FROM Sellers
                        JOIN Users ON Sellers.UserLogin = Users.UserLogin""");
            ResultSet resultSet = statement.executeQuery();
            List<Seller> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Seller(resultSet.getString("UserLogin"),
                        resultSet.getString("Password"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        roleRepository.getRoleById(resultSet.getInt("RoleId")),
                        resultSet.getBoolean("IsActive"),
                        hallRepository.getHallById(resultSet.getInt("HallId")),
                        resultSet.getDouble("Salary")));
            }
            return list;
        }
    }

    public List<SuperVisor> getSuperVisors() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT Users.UserLogin, Users.Password, Users.FirstName, Users.LastName, Users.MiddleName, Users.RoleId, Users.IsActive, SuperVisors.SectionId FROM SuperVisors
                        JOIN Users ON SuperVisors.UserLogin = Users.UserLogin""");
            ResultSet resultSet = statement.executeQuery();
            List<SuperVisor> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new SuperVisor(resultSet.getString("UserLogin"),
                        resultSet.getString("Password"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        roleRepository.getRoleById(resultSet.getInt("RoleId")),
                        resultSet.getBoolean("IsActive"),
                        sectionRepository.getSectionById(resultSet.getInt("SectionId"))));
            }
            return list;
        }
    }

    public User getUserByLogin(String loginUser) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users WHERE UserLogin = ?");
            statement.setString(1, loginUser);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getString("UserLogin"),
                        resultSet.getString("Password"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        roleRepository.getRoleById(resultSet.getInt("RoleId")),
                        resultSet.getBoolean("IsActive"));
            }
            return null;
        }
    }

    public Seller getSellerByLogin(String login) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT Users.UserLogin, Users.Password, Users.FirstName, Users.LastName, Users.MiddleName, Users.RoleId, Users.IsActive, Sellers.HallId, Sellers.Salary FROM Sellers
                        JOIN Users ON Sellers.UserLogin = Users.UserLogin
                    WHERE Sellers.UserLogin = ?""");
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Seller(resultSet.getString("UserLogin"),
                        resultSet.getString("Password"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        roleRepository.getRoleById(resultSet.getInt("RoleId")),
                        resultSet.getBoolean("IsActive"),
                        hallRepository.getHallById(resultSet.getInt("HallId")),
                        resultSet.getDouble("Salary"));
            }
            return null;
        }
    }

    public SuperVisor getSuperVisorByLogin(String login) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT Users.UserLogin, Users.Password, Users.FirstName, Users.LastName, Users.MiddleName, Users.RoleId, Users.IsActive, SuperVisors.SectionId FROM SuperVisors
                        JOIN Users ON SuperVisors.UserLogin = Users.UserLogin
                    WHERE SuperVisors.UserLogin = ?""");
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new SuperVisor(resultSet.getString("UserLogin"),
                        resultSet.getString("Password"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        roleRepository.getRoleById(resultSet.getInt("RoleId")),
                        resultSet.getBoolean("IsActive"),
                        sectionRepository.getSectionById(resultSet.getInt("SectionId")));
            }
            return null;
        }
    }

    public User addUser(User user) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO Users(UserLogin, Password, FirstName, LastName, MiddleName, RoleId, IsActive) VALUES (?,?,?,?,?,?,?)""");
            statement.setString(1, user.getUserLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setString(5, user.getMiddleName());
            statement.setInt(6, user.getRole().getRoleId());
            statement.setBoolean(7, user.getIsActive());
            statement.execute();

            return user;
        }
    }

    public void updateUser(User user, String oldLogin) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    UPDATE Users SET UserLogin=?, Password=?, FirstName=?, LastName=?, MiddleName=?, RoleId=?, IsActive=? WHERE UserLogin=?""");
            statement.setString(1, user.getUserLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setString(5, user.getMiddleName());
            statement.setInt(6, user.getRole().getRoleId());
            statement.setBoolean(7, user.getIsActive());
            statement.setString(8, oldLogin);
            statement.execute();
        }
    }

    public Seller addSeller(Seller seller) throws SQLException {
        addUser(seller);
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO Sellers(UserLogin, HallId, Salary) VALUES (?,?,?)""");
            statement.setString(1, seller.getUserLogin());
            statement.setInt(2, seller.getHall().getHallId());
            statement.setDouble(3, seller.getSalary());
            statement.execute();

            return seller;
        }
    }

    public void updateSeller(Seller seller, String oldLogin) throws SQLException {
        updateUser(seller, oldLogin);
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    UPDATE Sellers SET UserLogin=?, HallId=? WHERE UserLogin=?""");
            statement.setString(1, seller.getUserLogin());
            statement.setInt(2, seller.getHall().getHallId());
            statement.setString(3, oldLogin);
            statement.execute();
        }
    }

    public SuperVisor addSuperVisor(SuperVisor superVisor) throws SQLException {
        addUser(superVisor);
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO SuperVisors(UserLogin, SectionId) VALUES (?,?)""");
            statement.setString(1, superVisor.getUserLogin());
            statement.setInt(2, superVisor.getSection().getSectionId());
            statement.execute();

            return superVisor;
        }
    }

    public void updateSuperVisor(SuperVisor supervisor, String oldLogin) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            updateUser(supervisor, oldLogin);
            PreparedStatement statement = connection.prepareStatement("""
                    UPDATE SuperVisors SET UserLogin=?, SectionId=? WHERE UserLogin=?""");
            statement.setString(1, supervisor.getUserLogin());
            statement.setInt(2, supervisor.getSection().getSectionId());
            statement.setString(3, oldLogin);
            statement.execute();
        }
    }

    public void changeActiveStatus(String login, boolean status) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    UPDATE Users SET IsActive = ? WHERE UserLogin = ?""");
            statement.setBoolean(1, status);
            statement.setString(2, login);
            statement.execute();
        }
    }

    public boolean isSeller(String login) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    IF EXISTS(SELECT *
                              FROM Sellers
                              WHERE UserLogin = ?)
                        BEGIN
                            SELECT 1
                        END
                    ELSE
                        BEGIN
                            SELECT 0
                        END""");
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean(1);
            }
            return false;
        }
    }

    public boolean isSuperVisor(String login) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    IF EXISTS(SELECT *
                              FROM SuperVisors
                              WHERE UserLogin = ?)
                        BEGIN
                            SELECT 1
                        END
                    ELSE
                        BEGIN
                            SELECT 0
                        END""");
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean(1);
            }
            return false;
        }
    }

    /* ******************* Продавцы ******************* */

    // Получить данные о выработке на одного продавца за указанный период по всем торговым точкам
    public List<SalePointsSellers> getAllSalePointsSellers() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT SellerLogin, FirstName, LastName, MiddleName, SalePointName, TypeName, Total
                    FROM (SELECT SellerLogin, SUM(TransactionsProducts.Quantity * Price * (1 - Discount / 100)) as Total
                          FROM Transactions
                                   JOIN TransactionsProducts ON Transactions.TransactionId = TransactionsProducts.TransactionId
                                   JOIN ProductsSalePoints ON TransactionsProducts.ProductId = ProductsSalePoints.ProductId
                                   JOIN SalePoints ON ProductsSalePoints.SalePointId = SalePoints.SalePointId
                                   JOIN Users ON UserLogin = SellerLogin
                          GROUP BY SellerLogin) as totalTable
                             JOIN Users ON SellerLogin = UserLogin
                             JOIN Sellers ON Users.UserLogin = Sellers.UserLogin
                             JOIN Halls ON Sellers.HallId = Halls.HallId
                             JOIN SalePoints ON Halls.SalePointId = SalePoints.SalePointId
                             JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId""");
            ResultSet resultSet = statement.executeQuery();
            List<SalePointsSellers> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new SalePointsSellers(resultSet.getString("SellerLogin"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        resultSet.getString("SalePointName"),
                        resultSet.getString("TypeName"),
                        resultSet.getDouble("Total")));
            }
            return list;
        }
    }

    // Получить данные о выработке на одного продавца за указанный период по торговым точкам
    // заданного типа
    public List<SalePointsSellers> getSalePointSellers(String salePointTypeName) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT SellerLogin, FirstName, LastName, MiddleName, SalePointName, TypeName, Total
                    FROM (SELECT SellerLogin, SUM(TransactionsProducts.Quantity * Price * (1 - Discount / 100)) as Total
                          FROM Transactions
                                   JOIN TransactionsProducts ON Transactions.TransactionId = TransactionsProducts.TransactionId
                                   JOIN ProductsSalePoints ON TransactionsProducts.ProductId = ProductsSalePoints.ProductId
                                   JOIN SalePoints ON ProductsSalePoints.SalePointId = SalePoints.SalePointId
                                   JOIN Users ON UserLogin = SellerLogin
                          GROUP BY SellerLogin) as totalTable
                             JOIN Users ON SellerLogin = UserLogin
                             JOIN Sellers ON Users.UserLogin = Sellers.UserLogin
                             JOIN Halls ON Sellers.HallId = Halls.HallId
                             JOIN SalePoints ON Halls.SalePointId = SalePoints.SalePointId
                             JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
                    WHERE TypeName = ?""");
            statement.setString(1, salePointTypeName);
            ResultSet resultSet = statement.executeQuery();

            List<SalePointsSellers> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new SalePointsSellers(resultSet.getString("SellerLogin"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        resultSet.getString("SalePointName"),
                        resultSet.getString("TypeName"),
                        resultSet.getDouble("Total")));
            }
            return list;
        }
    }

    // Получить данные о выработке отдельно взятого продавца отдельно взятой торговой точки за указанный период.
    public SalePointsSellers getSalePointSeller(String login) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT SellerLogin, FirstName, LastName, MiddleName, SalePointName, TypeName, Total
                    FROM (SELECT SellerLogin, SUM(TransactionsProducts.Quantity * Price * (1 - Discount / 100)) as Total
                          FROM Transactions
                                   JOIN TransactionsProducts ON Transactions.TransactionId = TransactionsProducts.TransactionId
                                   JOIN ProductsSalePoints ON TransactionsProducts.ProductId = ProductsSalePoints.ProductId
                                   JOIN SalePoints ON ProductsSalePoints.SalePointId = SalePoints.SalePointId
                                   JOIN Users ON UserLogin = SellerLogin
                          GROUP BY SellerLogin) as totalTable
                             JOIN Users ON SellerLogin = UserLogin
                             JOIN Sellers ON Users.UserLogin = Sellers.UserLogin
                             JOIN Halls ON Sellers.HallId = Halls.HallId
                             JOIN SalePoints ON Halls.SalePointId = SalePoints.SalePointId
                             JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
                    WHERE SellerLogin = ?""");
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? new SalePointsSellers(resultSet.getString("SellerLogin"),
                    resultSet.getString("FirstName"),
                    resultSet.getString("LastName"),
                    resultSet.getString("MiddleName"),
                    resultSet.getString("SalePointName"),
                    resultSet.getString("TypeName"),
                    resultSet.getDouble("Total")) : null;
        }
    }

    // Получить данные об отношении объема продаж к объему торговых площадей по торговым точкам указанного типа
    public double getRelation(String typeName) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT (Table1.ProductsSum / Table2.PointSize) AS Relation
                    FROM (SELECT SUM(TransactionsProducts.Quantity * Price * (1 - Discount / 100)) as ProductsSum
                          FROM (SELECT TransactionId, TypeName
                                FROM SalePoints
                                         JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
                                         JOIN Halls ON SalePoints.SalePointId = Halls.SalePointId
                                         JOIN Sellers ON Halls.HallId = Sellers.HallId
                                         JOIN Transactions ON Sellers.UserLogin = Transactions.SellerLogin) as Transactions
                                   JOIN TransactionsProducts ON Transactions.TransactionId = TransactionsProducts.TransactionId
                                   JOIN ProductsSalePoints ON ProductsSalePoints.ProductId = TransactionsProducts.ProductId
                                   JOIN Products ON TransactionsProducts.ProductId = Products.ProductId
                          WHERE TypeName = ?
                          GROUP BY TypeName) as Table1,
                         (SELECT SUM(PointSize) as PointSize
                          FROM SalePoints
                                   JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
                          WHERE TypeName = ?) as Table2""");
            statement.setString(1, typeName);
            statement.setString(2, typeName);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getDouble("Relation") : 0.0;
        }
    }
}
