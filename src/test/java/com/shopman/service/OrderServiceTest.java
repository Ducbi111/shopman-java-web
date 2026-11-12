package com.shopman.service;

import com.shopman.TestDatabase;
import com.shopman.dao.ProductDAO8;
import com.shopman.model.Product8;
import org.junit.jupiter.api.Test;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {
    @Test void checkoutCreatesInvoiceDecreasesStockAndClearsCart() throws Exception {
        DataSource ds=TestDatabase.create("order");ProductDAO8 productDAO=new ProductDAO8(ds);
        Product8 p=new Product8();p.setProductCode("MH01");p.setName("Sữa tươi");p.setProductType("Sữa");p.setUnit("Hộp");p.setPrice(new BigDecimal("35000"));p.setStockQuantity(10);p.setCreatedByManagerId(1L);productDAO.insert(p);
        try(Connection c=ds.getConnection();Statement s=c.createStatement()){
            s.executeUpdate("INSERT INTO tblCart8(customerId) VALUES(2)");
            s.executeUpdate("INSERT INTO tblCartItem8(cartId,productId,quantity,unitPrice) VALUES(1,"+p.getId()+",2,35000)");
        }
        String code=new OrderService(ds).createPendingInvoice(2,"Số 10 Nguyễn Trãi");
        assertTrue(code.startsWith("HD"));
        try(Connection c=ds.getConnection();Statement s=c.createStatement()){
            try(ResultSet r=s.executeQuery("SELECT totalAmount,status FROM tblInvoice8")){assertTrue(r.next());assertEquals(new BigDecimal("70000.00"),r.getBigDecimal(1));assertEquals("PENDING",r.getString(2));}
            try(ResultSet r=s.executeQuery("SELECT stockQuantity FROM tblProduct8")){r.next();assertEquals(8,r.getInt(1));}
            try(ResultSet r=s.executeQuery("SELECT COUNT(*) FROM tblCartItem8")){r.next();assertEquals(0,r.getInt(1));}
        }
    }

    @Test void checkoutRollsBackWhenStockIsInsufficient() throws Exception {
        DataSource ds=TestDatabase.create("rollback");ProductDAO8 dao=new ProductDAO8(ds);
        Product8 p=new Product8();p.setProductCode("MH02");p.setName("Bánh mì");p.setProductType("Bánh");p.setUnit("Gói");p.setPrice(BigDecimal.TEN);p.setStockQuantity(1);p.setCreatedByManagerId(1L);dao.insert(p);
        try(Connection c=ds.getConnection();Statement s=c.createStatement()){s.executeUpdate("INSERT INTO tblCart8(customerId) VALUES(2)");s.executeUpdate("INSERT INTO tblCartItem8(cartId,productId,quantity,unitPrice) VALUES(1,"+p.getId()+",2,10)");}
        assertThrows(IllegalArgumentException.class,()->new OrderService(ds).createPendingInvoice(2,"Hà Nội"));
        try(Connection c=ds.getConnection();Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT COUNT(*) FROM tblInvoice8")){r.next();assertEquals(0,r.getInt(1));}
    }
}
