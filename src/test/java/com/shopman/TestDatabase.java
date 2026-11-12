package com.shopman;

import org.h2.jdbcx.JdbcDataSource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class TestDatabase {
    private TestDatabase() {}
    public static DataSource create(String name) throws SQLException {
        JdbcDataSource ds=new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:"+name+";MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1");ds.setUser("sa");ds.setPassword("");
        try(Connection c=ds.getConnection();Statement s=c.createStatement()){
            s.execute("CREATE TABLE tblProduct8(id BIGINT AUTO_INCREMENT PRIMARY KEY,productCode VARCHAR(30) UNIQUE,name VARCHAR(200),productType VARCHAR(100),unit VARCHAR(50),price DECIMAL(15,2),stockQuantity INT,description TEXT,image VARCHAR(255),createdByManagerId BIGINT)");
            s.execute("CREATE TABLE tblCart8(id BIGINT AUTO_INCREMENT PRIMARY KEY,customerId BIGINT UNIQUE)");
            s.execute("CREATE TABLE tblCartItem8(id BIGINT AUTO_INCREMENT PRIMARY KEY,cartId BIGINT,productId BIGINT,quantity INT,unitPrice DECIMAL(15,2))");
            s.execute("CREATE TABLE tblInvoice8(id BIGINT AUTO_INCREMENT PRIMARY KEY,invoiceCode VARCHAR(30) UNIQUE,orderDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,shippingAddress VARCHAR(255),status VARCHAR(30),totalAmount DECIMAL(15,2),customerId BIGINT,approvedBySalesStaffId BIGINT,deliveryStaffId BIGINT)");
            s.execute("CREATE TABLE tblInvoiceDetail8(id BIGINT AUTO_INCREMENT PRIMARY KEY,invoiceId BIGINT,productId BIGINT,quantity INT,unitPrice DECIMAL(15,2))");
        }
        return ds;
    }
}
