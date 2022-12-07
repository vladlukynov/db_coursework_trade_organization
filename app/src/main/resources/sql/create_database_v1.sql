CREATE TABLE Posts
(
    post_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name    NVARCHAR(50)
);

CREATE TABLE Specializations
(
    functions    NVARCHAR(128),
    productTypes NVARCHAR(128),
    post_id      INTEGER,
    FOREIGN KEY (post_id) REFERENCES Posts (post_id)
);

CREATE TABLE Departments
(
    DepartId INTEGER PRIMARY KEY AUTOINCREMENT,
    Name     NVARCHAR(50)
);

CREATE TABLE Customer
(
    CustomerId  INTEGER PRIMARY KEY AUTOINCREMENT,
    FirstName   NVARCHAR(50),
    LastName    NVARCHAR(50),
    PhoneNumber VARCHAR(15) CHECK (PhoneNumber LIKE '+7[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]')
);

CREATE TABLE Employees
(
    EmployeeId    INTEGER PRIMARY KEY AUTOINCREMENT,
    ServiceNumber INTEGER,
    FirstName     NVARCHAR(50),
    LastName      NVARCHAR(50),
    PostID        INTEGER,
    DepartId      INTEGER,
    FOREIGN KEY (DepartId) REFERENCES Departments (DepartId),
    FOREIGN KEY (PostID) REFERENCES Posts (post_id)
);

CREATE TABLE Products
(
    ProductId   INTEGER PRIMARY KEY AUTOINCREMENT,
    CustomerId  INTEGER,
    Name        NVARCHAR(50),
    Article     INTEGER,
    Price       INTEGER CHECK (Price >= 0),
    isAvailable BOOLEAN,
    FOREIGN KEY (CustomerId) REFERENCES Customer (CustomerId)
);

CREATE TABLE Storage
(
    ProductId   INTEGER,
    ProductType NVARCHAR(50),
    Location    NVARCHAR(50),
    FOREIGN KEY (ProductId) REFERENCES Products (ProductId)
);
