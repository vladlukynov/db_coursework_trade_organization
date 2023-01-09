-- 1. Получить перечень и общее число поставщиков, поставляющих указанный вид товара,
-- либо некоторый товар в объеме, не менее заданного за весь период сотрудничества,
-- либо за указанный период.

-- Получить перечень и общее число поставщиков, поставляющих указанный вид товара за весь период сотрудничества
SELECT SupplierName, COUNT(*) as SuppliersCount
FROM SuppliersProducts
         JOIN Suppliers ON SuppliersProducts.SupplierId = Suppliers.SupplierId
WHERE ProductId = 4
GROUP BY SupplierName;

-- 2. Получить перечень и общее число покупателей, купивших указанный вид товара за
-- некоторый период, либо сделавших покупку товара в объеме, не менее заданного.

-- Получить перечень и общее число покупателей, купивших указанный вид товара за некоторый период
SELECT FirstName, LastName, MiddleName, ConsumersCount
FROM Consumers
         JOIN Transactions ON Consumers.TransactionId = Transactions.TransactionId
         JOIN TransactionsProducts ON Transactions.TransactionId = TransactionsProducts.TransactionId
         JOIN Products ON TransactionsProducts.ProductId = Products.ProductId,
     (SELECT COUNT(*) as ConsumersCount
      FROM Consumers
               JOIN Transactions ON Consumers.TransactionId = Transactions.TransactionId
               JOIN TransactionsProducts ON Transactions.TransactionId = TransactionsProducts.TransactionId
               JOIN Products ON TransactionsProducts.ProductId = Products.ProductId
      WHERE ProductName = N'Чипсы') as ConsumersTable
WHERE ProductName = N'Чипсы';

-- 3. Получить номенклатуру и объем товаров в указанной торговой точке.
SELECT ProductName, Quantity, Price, Discount
FROM ProductsSalePoints
         JOIN Products ON ProductsSalePoints.ProductId = Products.ProductId
WHERE SalePointId = 3;

-- 4. Получить сведения об объеме и ценах на указанный товар по всем торговым точкам,
-- по торговым точкам заданного типа, по конкретной торговой точке.

-- Получить сведения об объеме и ценах на указанный товар по всем торговым точкам
SELECT SalePointName, TypeName, ProductName, Quantity, Price, Discount
FROM ProductsSalePoints
         JOIN Products ON ProductsSalePoints.ProductId = Products.ProductId
         JOIN SalePoints ON ProductsSalePoints.SalePointId = SalePoints.SalePointId
         JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
WHERE ProductName = N'Молоко';

-- Получить сведения об объеме и ценах на указанный товар по торговым точкам заданного типа
SELECT SalePointName, TypeName, ProductName, Quantity, Price, Discount
FROM ProductsSalePoints
         JOIN Products ON ProductsSalePoints.ProductId = Products.ProductId
         JOIN SalePoints ON ProductsSalePoints.SalePointId = SalePoints.SalePointId
         JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
WHERE ProductName = N'Молоко'
  AND TypeName = N'Универмаг';

-- Получить сведения об объеме и ценах на указанный товар по конкретной торговой точке.
SELECT SalePointName, TypeName, ProductName, Quantity, Price, Discount
FROM ProductsSalePoints
         JOIN Products ON ProductsSalePoints.ProductId = Products.ProductId
         JOIN SalePoints ON ProductsSalePoints.SalePointId = SalePoints.SalePointId
         JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
WHERE ProductName = N'Кефир'
  AND ProductsSalePoints.SalePointId = 3;

-- 5. Получить данные о выработке на одного продавца за указанный период по всем
-- торговым точкам, по торговым точкам заданного типа.

-- Получить данные о выработке на одного продавца за указанный период по всем
-- торговым точкам
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
         JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId;

-- Получить данные о выработке на одного продавца за указанный период по
-- торговым точкам заданного типа
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
WHERE TypeName = N'Киоск';

-- 6. Получить данные о выработке отдельно взятого продавца отдельно взятой торговой
-- точки за указанный период.
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
WHERE SellerLogin = 'seller2';

-- 7. Получить данные об объеме продаж указанного товара за некоторый период по
-- всем торговым точкам, по торговым точкам заданного типа, по конкретной торговой точке.

-- Получить данные об объеме продаж указанного товара за некоторый период по всем торговым точкам
SELECT SalePointName, Quantity
FROM (SELECT ProductsSalePoints.SalePointId, SUM(TransactionsProducts.Quantity) as Quantity
      FROM Transactions
               JOIN TransactionsProducts ON Transactions.TransactionId = TransactionsProducts.TransactionId
               JOIN ProductsSalePoints ON TransactionsProducts.ProductId = ProductsSalePoints.ProductId
               JOIN SalePoints ON ProductsSalePoints.SalePointId = SalePoints.SalePointId
      WHERE TransactionsProducts.ProductId = 4
      GROUP BY ProductsSalePoints.SalePointId) as QuantityTable
         JOIN SalePoints ON QuantityTable.SalePointId = SalePoints.SalePointId;

-- Получить данные об объеме продаж указанного товара за некоторый период по торговым точкам
-- заданного типа
SELECT SalePointName, Quantity
FROM (SELECT ProductsSalePoints.SalePointId, SUM(TransactionsProducts.Quantity) as Quantity
      FROM Transactions
               JOIN TransactionsProducts ON Transactions.TransactionId = TransactionsProducts.TransactionId
               JOIN ProductsSalePoints ON TransactionsProducts.ProductId = ProductsSalePoints.ProductId
               JOIN SalePoints ON ProductsSalePoints.SalePointId = SalePoints.SalePointId
      WHERE TransactionsProducts.ProductId = 6
      GROUP BY ProductsSalePoints.SalePointId) as QuantityTable
         JOIN SalePoints ON QuantityTable.SalePointId = SalePoints.SalePointId
         JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
WHERE TypeName = N'Киоск';

-- Получить данные об объеме продаж указанного товара за некоторый период по конкретной торговой точке
SELECT SalePointName, Quantity
FROM (SELECT ProductsSalePoints.SalePointId, SUM(TransactionsProducts.Quantity) as Quantity
      FROM Transactions
               JOIN TransactionsProducts ON Transactions.TransactionId = TransactionsProducts.TransactionId
               JOIN ProductsSalePoints ON TransactionsProducts.ProductId = ProductsSalePoints.ProductId
               JOIN SalePoints ON ProductsSalePoints.SalePointId = SalePoints.SalePointId
      WHERE TransactionsProducts.ProductId = 4
      GROUP BY ProductsSalePoints.SalePointId) as QuantityTable
         JOIN SalePoints ON QuantityTable.SalePointId = SalePoints.SalePointId
WHERE QuantityTable.SalePointId = 3;

-- 8. Получить данные о заработной плате продавцов по всем торговым точкам, по
-- торговым точкам заданного типа, по конкретной торговой точке.

-- Получить данные о заработной плате продавцов по всем торговым точкам
SELECT SalePointName, TypeName, FirstName, LastName, MiddleName, Salary
FROM Sellers
         JOIN Halls ON Sellers.HallId = Halls.HallId
         JOIN SalePoints ON Halls.SalePointId = SalePoints.SalePointId
         JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
         JOIN Users ON Sellers.UserLogin = Users.UserLogin;

-- Получить данные о заработной плате продавцов по торговым точкам заданного типа
SELECT SalePointName, TypeName, FirstName, LastName, MiddleName, Salary
FROM Sellers
         JOIN Halls ON Sellers.HallId = Halls.HallId
         JOIN SalePoints ON Halls.SalePointId = SalePoints.SalePointId
         JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
         JOIN Users ON Sellers.UserLogin = Users.UserLogin
WHERE TypeName = N'Киоск';

-- Получить данные о заработной плате продавцов по конкретной торговой точке.
SELECT SalePointName, TypeName, FirstName, LastName, MiddleName, Salary
FROM Sellers
         JOIN Halls ON Sellers.HallId = Halls.HallId
         JOIN SalePoints ON Halls.SalePointId = SalePoints.SalePointId
         JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
         JOIN Users ON Sellers.UserLogin = Users.UserLogin
WHERE SalePoints.SalePointId = 3;

-- 9. Получить сведения о поставках определенного товара указанным поставщиком за все время поставок, либо за некоторый период.

-- Получить сведения о поставках определенного товара указанным поставщиком за все время поставок
SELECT Requests.RequestId, SalePointName, TypeName, ProductName, Quantity, Price
FROM SuppliersRequests
         JOIN Suppliers ON SuppliersRequests.SupplierId = Suppliers.SupplierId
         JOIN Products ON SuppliersRequests.ProductId = Products.ProductId
         JOIN Requests ON SuppliersRequests.RequestId = Requests.RequestId
         JOIN SuppliersProducts ON Products.ProductId = SuppliersProducts.ProductId
         JOIN SalePoints ON Requests.SalePointId = SalePoints.SalePointId
         JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
WHERE ProductName = N'Кефир';

-- 10. Получить данные об отношении объема продаж к объему торговых площадей, либо к числу торговых залов,
-- либо к числу прилавков по торговым точкам указанного типа, о выработке отдельно взятого продавца торговой точки,
-- по заданной торговой точке.

-- Получить данные об отношении объема продаж к объему торговых площадей по торговым точкам указанного типа
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
      WHERE TypeName = N'Универмаг'
      GROUP BY TypeName) as Table1,
     (SELECT SUM(PointSize) as PointSize
      FROM SalePoints
               JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
      WHERE TypeName = N'Универмаг') as Table2;

-- 11. Получить данные о рентабельности торговой точки: соотношение объема продаж к накладным расходам
-- (суммарная заработная плата продавцов + платежи за аренду, коммунальные услуги) за указанный период.
SELECT (Table1.ProductsSum / Table2.SalePointPrice) as Profitability
FROM (SELECT SUM(TransactionsProducts.Quantity * Price * (1 - Discount / 100)) as ProductsSum
      FROM (SELECT TransactionId
            FROM SalePoints
                     JOIN Halls ON SalePoints.SalePointId = Halls.SalePointId
                     JOIN Sellers ON Halls.HallId = Sellers.HallId
                     JOIN Transactions ON Sellers.UserLogin = Transactions.SellerLogin
            WHERE SalePoints.SalePointId = 3) as Transactions
               JOIN TransactionsProducts ON Transactions.TransactionId = TransactionsProducts.TransactionId
               JOIN ProductsSalePoints ON ProductsSalePoints.ProductId = TransactionsProducts.ProductId
               JOIN Products ON TransactionsProducts.ProductId = Products.ProductId) as Table1,
     (SELECT (RentalPrice + SalePoints.CommunalService + SalaryTable.Salary) as SalePointPrice
      FROM SalePoints,
           (SELECT Sum(Salary) as Salary
            FROM Sellers
                     JOIN Halls ON Sellers.HallId = Halls.HallId
            WHERE SalePointId = 3) as SalaryTable
      WHERE SalePointId = 3) as Table2;

-- 12. Получить сведения о поставках товаров по указанному номеру заказа.
SELECT SalePointName, TypeName, ProductName, Quantity, Price
FROM SuppliersRequests
         JOIN Suppliers ON SuppliersRequests.SupplierId = Suppliers.SupplierId
         JOIN Products ON SuppliersRequests.ProductId = Products.ProductId
         JOIN Requests ON SuppliersRequests.RequestId = Requests.RequestId
         JOIN SuppliersProducts ON Products.ProductId = SuppliersProducts.ProductId
         JOIN SalePoints ON Requests.SalePointId = SalePoints.SalePointId
         JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
WHERE SuppliersRequests.RequestId = 1;

-- 13. Получить сведения о покупателях указанного товара за обозначенный, либо за весь период,
-- по всем торговым точкам, по торговым точкам указанного типа, по данной торговой точке.

-- Получить сведения о покупателях указанного товара за весь период по всем торговым точкам, по торговым точкам указанного типа, по данной торговой точке.
SELECT SalePointName, TypeName, FirstName, LastName, MiddleName, TransactionDate
FROM Consumers
         JOIN Transactions ON Consumers.TransactionId = Transactions.TransactionId
         JOIN Sellers ON Transactions.SellerLogin = Sellers.UserLogin
         JOIN TransactionsProducts ON Transactions.TransactionId = TransactionsProducts.TransactionId
         JOIN Products ON TransactionsProducts.ProductId = Products.ProductId
         JOIN Halls ON Sellers.HallId = Halls.HallId
         JOIN SalePoints ON Halls.SalePointId = SalePoints.SalePointId
         JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
WHERE ProductName = N'Молоко';

-- Получить сведения о покупателях указанного товара за весь период по торговым точкам указанного типа.
SELECT SalePointName, TypeName, FirstName, LastName, MiddleName, TransactionDate
FROM Consumers
         JOIN Transactions ON Consumers.TransactionId = Transactions.TransactionId
         JOIN Sellers ON Transactions.SellerLogin = Sellers.UserLogin
         JOIN TransactionsProducts ON Transactions.TransactionId = TransactionsProducts.TransactionId
         JOIN Products ON TransactionsProducts.ProductId = Products.ProductId
         JOIN Halls ON Sellers.HallId = Halls.HallId
         JOIN SalePoints ON Halls.SalePointId = SalePoints.SalePointId
         JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
WHERE ProductName = N'Молоко'
  AND TypeName = N'Универмаг';

-- Получить сведения о покупателях указанного товара за весь период по данной торговой точке.
SELECT SalePointName, TypeName, FirstName, LastName, MiddleName, TransactionDate
FROM Consumers
         JOIN Transactions ON Consumers.TransactionId = Transactions.TransactionId
         JOIN Sellers ON Transactions.SellerLogin = Sellers.UserLogin
         JOIN TransactionsProducts ON Transactions.TransactionId = TransactionsProducts.TransactionId
         JOIN Products ON TransactionsProducts.ProductId = Products.ProductId
         JOIN Halls ON Sellers.HallId = Halls.HallId
         JOIN SalePoints ON Halls.SalePointId = SalePoints.SalePointId
         JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
WHERE ProductName = N'Молоко'
  AND SalePoints.SalePointId = 3;

-- 14. Получить сведения о наиболее активных покупателях по всем торговым точкам, по торговым точкам указанного типа,
-- по данной торговой точке.
SELECT Halls.SalePointId, FirstName, LastName, MiddleName, COUNT(*) AS ConsumersCount
FROM Consumers
         JOIN Transactions ON Consumers.TransactionId = Transactions.TransactionId
         JOIN Sellers ON Transactions.SellerLogin = Sellers.UserLogin
         JOIN Halls ON Sellers.HallId = Halls.HallId
         JOIN SalePoints ON Halls.SalePointId = SalePoints.SalePointId
GROUP BY Halls.SalePointId, FirstName, LastName, MiddleName;

-- 15. Получить данные о товарообороте торговой точки, либо всех торговых определенной группы за указанный период.
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
      WHERE SalePoints.SalePointId = 3) as SumTable,
     TransactionsProducts
         JOIN Products ON TransactionsProducts.ProductId = Products.ProductId
         JOIN ProductsSalePoints ON Products.ProductId = ProductsSalePoints.ProductId
         JOIN SalePoints ON ProductsSalePoints.SalePointId = SalePoints.SalePointId
         JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
WHERE SalePoints.SalePointId = 3;
