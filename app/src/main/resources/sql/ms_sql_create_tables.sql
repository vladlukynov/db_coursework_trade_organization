-- Типы торговых точек
CREATE TABLE SalePointTypes
(
    TypeId   INTEGER IDENTITY (1, 1) PRIMARY KEY,
    TypeName NVARCHAR(32) NOT NULL UNIQUE
);

-- Торговые точки
CREATE TABLE SalePoints
(
    SalePointId     INTEGER IDENTITY (1, 1) PRIMARY KEY,
    TypeId          INTEGER      NOT NULL FOREIGN KEY REFERENCES SalePointTypes (TypeId),
    -- Размеры торговой точки
    PointSize       REAL         NOT NULL CHECK ( PointSize >= 0 ),
    -- Стоимость аренды
    RentalPrice     MONEY        NOT NULL CHECK ( RentalPrice >= 0 ),
    -- Коммунальные услуги
    CommunalService MONEY        NOT NULL CHECK (CommunalService >= 0),
    -- Количество прилавков
    CountersNumber  INTEGER      NOT NULL CHECK ( CountersNumber >= 0 ),
    IsActive        BIT          NOT NULL,
    SalePointName   NVARCHAR(50) NOT NULL
);

-- Поставщики
CREATE TABLE Suppliers
(
    SupplierId   INTEGER IDENTITY (1, 1) PRIMARY KEY,
    SupplierName NVARCHAR(50) NOT NULL UNIQUE,
    IsActive     BIT          NOT NULL
);

-- Роли пользователей
CREATE TABLE Roles
(
    RoleId   INTEGER IDENTITY (1, 1) PRIMARY KEY,
    RoleName NVARCHAR(50) NOT NULL UNIQUE
);

-- Пользователи
CREATE TABLE Users
(
    UserLogin  VARCHAR(64) PRIMARY KEY,
    Password   NVARCHAR(256) NOT NULL,
    FirstName  NVARCHAR(64)  NOT NULL,
    LastName   NVARCHAR(64)  NOT NULL,
    MiddleName NVARCHAR(64)  NOT NULL,
    RoleId     INTEGER       NOT NULL FOREIGN KEY REFERENCES Roles (RoleId),
    IsActive   BIT           NOT NULL
);

-- Залы
CREATE TABLE Halls
(
    HallId      INTEGER IDENTITY (1, 1) PRIMARY KEY,
    HallName    NVARCHAR(50) NOT NULL,
    SalePointId INTEGER      NOT NULL FOREIGN KEY REFERENCES SalePoints (SalePointId),
    IsActive    BIT          NOT NULL
);

-- Секции
CREATE TABLE Sections
(
    SectionId   INTEGER IDENTITY (1, 1) PRIMARY KEY,
    SectionName NVARCHAR(50) NOT NULL,
    HallId      INTEGER      NOT NULL FOREIGN KEY REFERENCES Halls (HallId)
);

-- Управляющие (или руководители)
CREATE TABLE SuperVisors
(
    UserLogin VARCHAR(64) NOT NULL FOREIGN KEY REFERENCES Users (UserLogin),
    SectionId INTEGER     NOT NULL FOREIGN KEY REFERENCES Sections (SectionId)
);

-- Продавцы
CREATE TABLE Sellers
(
    UserLogin VARCHAR(64) NOT NULL UNIQUE FOREIGN KEY REFERENCES Users (UserLogin),
    HallId    INTEGER     NOT NULL FOREIGN KEY REFERENCES Halls (HallId)
);

-- Таблица продаж
CREATE TABLE Transactions
(
    TransactionId   INTEGER IDENTITY (1, 1) PRIMARY KEY,
    SellerLogin     VARCHAR(64) NOT NULL FOREIGN KEY REFERENCES Sellers (UserLogin),
    TransactionDate DATE        NOT NULL
);

-- Покупатели
CREATE TABLE Consumers
(
    ConsumerId    INTEGER IDENTITY (1 ,1) PRIMARY KEY,
    FirstName     NVARCHAR(64) NOT NULL,
    LastName      NVARCHAR(64) NOT NULL,
    MiddleName    NVARCHAR(64) NOT NULL,
    TransactionId INTEGER      NOT NULL FOREIGN KEY REFERENCES Transactions (TransactionId)
);

-- Товары
CREATE TABLE Products
(
    ProductId   INTEGER IDENTITY (1, 1) PRIMARY KEY,
    ProductName NVARCHAR(50) NOT NULL,
    IsActive    BIT          NOT NULL
);

-- Заявки
CREATE TABLE Requests
(
    RequestId    INTEGER IDENTITY (1, 1) PRIMARY KEY,
    SalePointId  INTEGER NOT NULL FOREIGN KEY REFERENCES SalePoints (SalePointId),
    -- Обработана заявка менеджером или нет
    IsProcessed  BIT     NOT NULL,
    CreationDate DATE    NOT NULL,
    CompleteDate DATE
);

-- Таблица количества проданных товаров в транзакции
CREATE TABLE TransactionsProducts
(
    TransactionId INTEGER NOT NULL FOREIGN KEY REFERENCES Transactions (TransactionId),
    ProductId     INTEGER NOT NULL FOREIGN KEY REFERENCES Products (ProductId),
    Quantity      INTEGER NOT NULL CHECK ( Quantity >= 0 )
);

-- Таблица количества запрашиваемых товаров в заявке
CREATE TABLE RequestsProducts
(
    RequestId INTEGER NOT NULL FOREIGN KEY REFERENCES Requests (RequestId),
    ProductId INTEGER NOT NULL FOREIGN KEY REFERENCES Products (ProductId),
    Quantity  INTEGER NOT NULL CHECK ( Quantity >= 0 )
);

-- Таблица товаров, которые может поставлять поставщик и по какой цене
CREATE TABLE SuppliersProducts
(
    SupplierId INTEGER NOT NULL FOREIGN KEY REFERENCES Suppliers (SupplierId),
    ProductId  INTEGER NOT NULL FOREIGN KEY REFERENCES Products (ProductId),
    Price      MONEY   NOT NULL CHECK ( Price >= 0 ),
    IsActive   BIT     NOT NULL
);

-- Таблица товаров, кулпенных по заявке, с указанием поставщика и количества
CREATE TABLE SuppliersRequests
(
    RequestId  INTEGER NOT NULL FOREIGN KEY REFERENCES Requests (RequestId),
    SupplierId INTEGER NOT NULL FOREIGN KEY REFERENCES Suppliers (SupplierId),
    ProductId  INTEGER NOT NULL FOREIGN KEY REFERENCES Products (ProductId),
    Quantity   INTEGER NOT NULL CHECK ( Quantity >= 0 )
);

-- Наличие товаров у торговой точки (в каком количестве)
CREATE TABLE ProductsSalePoints
(
    ProductId   INTEGER NOT NULL FOREIGN KEY REFERENCES Products (ProductId),
    SalePointId INTEGER NOT NULL FOREIGN KEY REFERENCES SalePoints (SalePointId),
    Quantity    INTEGER NOT NULL CHECK ( Quantity >= 0 ),
    Price       MONEY   NOT NULL CHECK ( Price >= 0 ),
    Discount    REAL    NOT NULL CHECK (Discount >= 0)
);
