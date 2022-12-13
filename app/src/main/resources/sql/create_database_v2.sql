-- Типы торговых точек
CREATE TABLE SalePointTypes
(
    TypeId   INTEGER PRIMARY KEY AUTOINCREMENT,
    TypeName TEXT NOT NULL UNIQUE
);

-- Торговые точки
CREATE TABLE SalePoints
(
    SalePointId     INTEGER PRIMARY KEY AUTOINCREMENT,
    TypeId          INTEGER NOT NULL,
    -- Размеры торговой точки
    PointSize       REAL    NOT NULL CHECK ( PointSize >= 0 ),
    -- Стоимость аренды
    RentalPrice     REAL    NOT NULL CHECK ( RentalPrice >= 0 ),
    -- Коммунальные услуги
    СommunalService REAL    NOT NULL CHECK (СommunalService >= 0),
    -- Количество прилавков
    CountersNumber  INTEGER NOT NULL CHECK ( CountersNumber >= 0 ),
    FOREIGN KEY (TypeId) REFERENCES SalePointTypes (TypeId)
);

-- Поставщики
CREATE TABLE Suppliers
(
    SupplierId   INTEGER PRIMARY KEY AUTOINCREMENT,
    SupplierName TEXT    NOT NULL UNIQUE,
    IsActive     INTEGER NOT NULL CHECK ( IsActive = 0 OR IsActive = 1 )
);

-- Роли пользователей
CREATE TABLE Roles
(
    RoleId   INTEGER PRIMARY KEY AUTOINCREMENT,
    RoleName TEXT NOT NULL UNIQUE
);

-- Пользователи
CREATE TABLE Users
(
    UserLogin  TEXT PRIMARY KEY,
    Password   TEXT    NOT NULL,
    FirstName  TEXT    NOT NULL,
    LastName   TEXT    NOT NULL,
    MiddleName TEXT    NOT NULL,
    RoleId     INTEGER NOT NULL,
    IsActive   INTEGER NOT NULL CHECK ( IsActive = 0 OR IsActive = 1 ),
    FOREIGN KEY (RoleId) REFERENCES Roles (RoleId)
);

-- Залы
CREATE TABLE Halls
(
    HallId      INTEGER PRIMARY KEY AUTOINCREMENT,
    HallName    TEXT    NOT NULL,
    SalePointId INTEGER NOT NULL,
    FOREIGN KEY (SalePointId) REFERENCES SalePoints (SalePointId)
);

-- Секции
CREATE TABLE Sections
(
    SectionId   INTEGER PRIMARY KEY AUTOINCREMENT,
    SectionName TEXT    NOT NULL,
    HallId      INTEGER NOT NULL,
    FOREIGN KEY (HallId) REFERENCES Halls (HallId)
);

-- Управляющие (или руководители)
CREATE TABLE SuperVisors
(
    UserLogin TEXT    NOT NULL,
    SectionId INTEGER NOT NULL,
    FOREIGN KEY (UserLogin) REFERENCES Users (UserLogin),
    FOREIGN KEY (SectionId) REFERENCES Sections (SectionId)
);

-- Продавцы
CREATE TABLE Sellers
(
    UserLogin TEXT    NOT NULL,
    HallId    INTEGER NOT NULL,
    FOREIGN KEY (UserLogin) REFERENCES Users (UserLogin),
    FOREIGN KEY (HallId) REFERENCES Halls (HallId)
);

-- Таблица продаж
CREATE TABLE Transactions
(
    TransactionId   INTEGER PRIMARY KEY AUTOINCREMENT,
    SellerLogin     TEXT NOT NULL,
    TransactionDate TEXT NOT NULL,
    FOREIGN KEY (SellerLogin) REFERENCES Sellers (UserLogin)
);

-- Покупатели
CREATE TABLE Consumers
(
    ConsumerId    INTEGER PRIMARY KEY AUTOINCREMENT,
    FirstName     TEXT    NOT NULL,
    LastName      TEXT    NOT NULL,
    MiddleName    TEXT    NOT NULL,
    TransactionId INTEGER NOT NULL,
    FOREIGN KEY (TransactionId) REFERENCES Transactions (TransactionId)
);

-- Товары
CREATE TABLE Products
(
    ProductId   INTEGER PRIMARY KEY AUTOINCREMENT,
    ProductName TEXT    NOT NULL,
    IsActive    INTEGER NOT NULL CHECK ( IsActive = 0 OR IsActive = 1 )
);

-- Заявки
CREATE TABLE Requests
(
    RequestId   INTEGER PRIMARY KEY AUTOINCREMENT,
    SalePointId INTEGER NOT NULL,
    -- Обработана заявка менеджером или нет
    IsProcessed INTEGER NOT NULL CHECK ( IsProcessed = 0 OR IsProcessed = 1 ),
    FOREIGN KEY (SalePointId) REFERENCES SalePoints (SalePointId)
);

-- Таблица количества проданных товаров в транзакции
CREATE TABLE TransactionsProducts
(
    TransactionId INTEGER NOT NULL,
    ProductId     INTEGER NOT NULL,
    Quantity      INTEGER NOT NULL CHECK ( Quantity >= 0 ),
    FOREIGN KEY (TransactionId) REFERENCES Transactions (TransactionId),
    FOREIGN KEY (ProductId) REFERENCES Products (ProductId)
);

-- Таблица количества запрашиваемых товаров в заявке
CREATE TABLE RequestsProducts
(
    RequestId INTEGER NOT NULL,
    ProductId INTEGER NOT NULL,
    Quantity  INTEGER NOT NULL CHECK ( Quantity >= 0 ),
    FOREIGN KEY (RequestId) REFERENCES Requests (RequestId),
    FOREIGN KEY (ProductId) REFERENCES Products (ProductId)
);

-- Таблица товаров, которые может поставлять поставщик и по какой цене
CREATE TABLE SuppliersProducts
(
    SupplierId INTEGER NOT NULL,
    ProductId  INTEGER NOT NULL,
    Price      REAL    NOT NULL CHECK ( Price >= 0 ),
    FOREIGN KEY (SupplierId) REFERENCES Suppliers (SupplierId),
    FOREIGN KEY (ProductId) REFERENCES Products (ProductId)
);

-- Таблица товаров, кулпенных по заявке, с указанием поставщика и количества
CREATE TABLE SuppliersRequests
(
    RequestId  INTEGER NOT NULL,
    SupplierId INTEGER NOT NULL,
    ProductId  INTEGER NOT NULL,
    Quantity   INTEGER NOT NULL CHECK ( Quantity >= 0 ),
    FOREIGN KEY (RequestId) REFERENCES Requests (RequestId),
    FOREIGN KEY (SupplierId) REFERENCES Suppliers (SupplierId),
    FOREIGN KEY (ProductId) REFERENCES Products (ProductId)
);

-- Наличие товаров у торговой точки (в каком количестве)
CREATE TABLE ProductsSalePoints
(
    ProductId   INTEGER NOT NULL,
    SalePointId INTEGER NOT NULL,
    Quantity    INTEGER NOT NULL CHECK ( Quantity >= 0 ),
    Price       REAL    NOT NULL CHECK ( Price >= 0 ),
    FOREIGN KEY (ProductId) REFERENCES Products (ProductId),
    FOREIGN KEY (SalePointId) REFERENCES SalePoints (SalePointId)
);
