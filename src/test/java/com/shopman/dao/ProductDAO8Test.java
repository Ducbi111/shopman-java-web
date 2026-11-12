package com.shopman.dao;

import com.shopman.TestDatabase;
import com.shopman.model.Product8;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ProductDAO8Test {
    @Test void insertAndSearchProduct() throws Exception {
        ProductDAO8 dao=new ProductDAO8(TestDatabase.create("product"));
        Product8 p=new Product8();p.setProductCode("MH008");p.setName("Sữa chua Ba Vì có đường");p.setProductType("Sữa");
        p.setUnit("Lốc");p.setPrice(new BigDecimal("28000"));p.setStockQuantity(100);p.setDescription("Lốc 4 hộp");p.setImage("suachua.png");p.setCreatedByManagerId(1L);
        dao.insert(p);
        assertNotNull(p.getId());
        assertTrue(dao.existsByCodeOrName("MH008","khác"));
        assertEquals("Sữa chua Ba Vì có đường",dao.searchByName("ba vì").get(0).getName());
    }
}
