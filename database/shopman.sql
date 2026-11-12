DROP DATABASE IF EXISTS shopman;
CREATE DATABASE shopman CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE shopman;

CREATE TABLE tblMember8 (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    fullName VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phoneNumber VARCHAR(20)
);

CREATE TABLE tblCustomer8 (
    memberId BIGINT PRIMARY KEY,
    customerCode VARCHAR(30) NOT NULL UNIQUE,
    shippingAddress VARCHAR(255),
    CONSTRAINT fk_customer_member FOREIGN KEY (memberId) REFERENCES tblMember8(id)
);

CREATE TABLE tblStaff8 (
    memberId BIGINT PRIMARY KEY,
    staffCode VARCHAR(30) NOT NULL UNIQUE,
    position VARCHAR(100) NOT NULL,
    CONSTRAINT fk_staff_member FOREIGN KEY (memberId) REFERENCES tblMember8(id)
);

CREATE TABLE tblManager8 (
    memberId BIGINT PRIMARY KEY,
    managementLevel VARCHAR(50) NOT NULL,
    CONSTRAINT fk_manager_staff FOREIGN KEY (memberId) REFERENCES tblStaff8(memberId)
);

CREATE TABLE tblSalesStaff8 (
    memberId BIGINT PRIMARY KEY,
    counterNumber VARCHAR(30),
    CONSTRAINT fk_sales_staff FOREIGN KEY (memberId) REFERENCES tblStaff8(memberId)
);

CREATE TABLE tblDeliverStaff8 (
    memberId BIGINT PRIMARY KEY,
    vehiclePlateNumber VARCHAR(30),
    CONSTRAINT fk_deliver_staff FOREIGN KEY (memberId) REFERENCES tblStaff8(memberId)
);

CREATE TABLE tblSupplier8 (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    supplierCode VARCHAR(30) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    address VARCHAR(255),
    phoneNumber VARCHAR(20),
    email VARCHAR(100)
);

CREATE TABLE tblProduct8 (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    productCode VARCHAR(30) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    productType VARCHAR(100) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    price DECIMAL(15,2) NOT NULL CHECK (price >= 0),
    stockQuantity INT NOT NULL DEFAULT 0 CHECK (stockQuantity >= 0),
    description TEXT,
    image VARCHAR(255),
    createdByManagerId BIGINT NOT NULL,
    createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_manager FOREIGN KEY (createdByManagerId) REFERENCES tblManager8(memberId),
    INDEX idx_product_name (name)
);

CREATE TABLE tblCart8 (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customerId BIGINT NOT NULL UNIQUE,
    updatedAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_cart_customer FOREIGN KEY (customerId) REFERENCES tblCustomer8(memberId)
);

CREATE TABLE tblCartItem8 (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    cartId BIGINT NOT NULL,
    productId BIGINT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    unitPrice DECIMAL(15,2) NOT NULL CHECK (unitPrice >= 0),
    CONSTRAINT fk_cartitem_cart FOREIGN KEY (cartId) REFERENCES tblCart8(id) ON DELETE CASCADE,
    CONSTRAINT fk_cartitem_product FOREIGN KEY (productId) REFERENCES tblProduct8(id),
    CONSTRAINT uq_cart_product UNIQUE (cartId, productId)
);

CREATE TABLE tblInvoice8 (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    invoiceCode VARCHAR(30) NOT NULL UNIQUE,
    orderDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    shippingAddress VARCHAR(255) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    totalAmount DECIMAL(15,2) NOT NULL DEFAULT 0,
    customerId BIGINT NOT NULL,
    approvedBySalesStaffId BIGINT NULL,
    deliveryStaffId BIGINT NULL,
    CONSTRAINT fk_invoice_customer FOREIGN KEY (customerId) REFERENCES tblCustomer8(memberId),
    CONSTRAINT fk_invoice_sales FOREIGN KEY (approvedBySalesStaffId) REFERENCES tblSalesStaff8(memberId),
    CONSTRAINT fk_invoice_delivery FOREIGN KEY (deliveryStaffId) REFERENCES tblDeliverStaff8(memberId),
    INDEX idx_invoice_customer (customerId),
    INDEX idx_invoice_status (status)
);

CREATE TABLE tblInvoiceDetail8 (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    invoiceId BIGINT NOT NULL,
    productId BIGINT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    unitPrice DECIMAL(15,2) NOT NULL CHECK (unitPrice >= 0),
    CONSTRAINT fk_detail_invoice FOREIGN KEY (invoiceId) REFERENCES tblInvoice8(id) ON DELETE CASCADE,
    CONSTRAINT fk_detail_product FOREIGN KEY (productId) REFERENCES tblProduct8(id),
    CONSTRAINT uq_invoice_product UNIQUE (invoiceId, productId)
);

CREATE TABLE tblImportInvoice8 (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    importCode VARCHAR(30) NOT NULL UNIQUE,
    importDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    totalAmount DECIMAL(15,2) NOT NULL DEFAULT 0,
    supplierId BIGINT NOT NULL,
    createdByManagerId BIGINT NOT NULL,
    CONSTRAINT fk_import_supplier FOREIGN KEY (supplierId) REFERENCES tblSupplier8(id),
    CONSTRAINT fk_import_manager FOREIGN KEY (createdByManagerId) REFERENCES tblManager8(memberId)
);

CREATE TABLE tblImportInvoiceDetail8 (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    importInvoiceId BIGINT NOT NULL,
    productId BIGINT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    importPrice DECIMAL(15,2) NOT NULL CHECK (importPrice >= 0),
    CONSTRAINT fk_import_detail_invoice FOREIGN KEY (importInvoiceId) REFERENCES tblImportInvoice8(id) ON DELETE CASCADE,
    CONSTRAINT fk_import_detail_product FOREIGN KEY (productId) REFERENCES tblProduct8(id),
    CONSTRAINT uq_import_product UNIQUE (importInvoiceId, productId)
);

-- Mật khẩu dạng rõ chỉ nhằm phục vụ demo đồ án; hệ thống thật phải dùng BCrypt/Argon2.
INSERT INTO tblMember8(id, username, password, fullName, email, phoneNumber) VALUES
(1, 'manager', '123456', 'Nguyễn Văn Quản Lý', 'manager@shopman.vn', '0900000001'),
(2, 'customer', '123456', 'Trần Minh Khách', 'customer@example.com', '0900000002'),
(3, 'sales', '123456', 'Lê Thị Bán Hàng', 'sales@shopman.vn', '0900000003'),
(4, 'delivery', '123456', 'Phạm Văn Giao Hàng', 'delivery@shopman.vn', '0900000004');

INSERT INTO tblStaff8(memberId, staffCode, position) VALUES
(1, 'NVQL001', 'MANAGER'), (3, 'NVBH001', 'SALES'), (4, 'NVGH001', 'DELIVERY');
INSERT INTO tblManager8(memberId, managementLevel) VALUES (1, 'SENIOR');
INSERT INTO tblSalesStaff8(memberId, counterNumber) VALUES (3, 'Q01');
INSERT INTO tblDeliverStaff8(memberId, vehiclePlateNumber) VALUES (4, '29A-123.45');
INSERT INTO tblCustomer8(memberId, customerCode, shippingAddress)
VALUES (2, 'KH001', 'Số 10 Nguyễn Trãi, Hà Đông, Hà Nội');

INSERT INTO tblSupplier8(supplierCode, name, address, phoneNumber, email) VALUES
('NCC001', 'Công ty Vinamilk', 'TP. Hồ Chí Minh', '02854155555', 'contact@vinamilk.com.vn'),
('NCC002', 'Công ty Thực phẩm Việt', 'Hà Nội', '02435556666', 'contact@thucphamviet.vn');

INSERT INTO tblProduct8(productCode, name, productType, unit, price, stockQuantity, description, image, createdByManagerId) VALUES
('MH01', 'Sữa tươi tiệt trùng Vinamilk 1L', 'Sữa & sản phẩm từ sữa', 'Hộp', 35000, 50, 'Sữa tươi tiệt trùng không đường', 'sua-vinamilk.png', 1),
('MH02', 'Sữa tươi nguyên chất TH True Milk 1L', 'Sữa & sản phẩm từ sữa', 'Hộp', 38000, 30, 'Sữa tươi nguyên chất', 'sua-th.png', 1),
('MH03', 'Bánh mì gối Sandwich', 'Bánh kẹo', 'Gói', 20000, 40, 'Bánh mì mềm dùng cho bữa sáng', 'banh-mi.png', 1);

INSERT INTO tblCart8(customerId) VALUES (2);
