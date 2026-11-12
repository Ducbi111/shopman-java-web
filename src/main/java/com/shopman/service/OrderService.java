package com.shopman.service;

import com.shopman.dao.Database;
import com.shopman.dao.ProductDAO8;
import com.shopman.model.Product8;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class OrderService {
    private final DataSource dataSource;
    private final ProductDAO8 productDAO;

    public OrderService() { this(Database.getDataSource()); }
    public OrderService(DataSource dataSource) { this.dataSource=dataSource; this.productDAO=new ProductDAO8(dataSource); }

    public String createPendingInvoice(long customerId, String address) throws SQLException {
        if (address == null || address.isBlank()) throw new IllegalArgumentException("Địa chỉ nhận hàng không được để trống");
        try (Connection con=dataSource.getConnection()) {
            con.setAutoCommit(false);
            try {
                Long cartId = null;
                try (PreparedStatement ps=con.prepareStatement("SELECT id FROM tblCart8 WHERE customerId=? FOR UPDATE")) {
                    ps.setLong(1,customerId); try(ResultSet rs=ps.executeQuery()){ if(rs.next()) cartId=rs.getLong(1); }
                }
                if(cartId==null) throw new IllegalArgumentException("Giỏ hàng không tồn tại");
                List<OrderLine> lines=new ArrayList<>();
                try(PreparedStatement ps=con.prepareStatement("SELECT productId,quantity FROM tblCartItem8 WHERE cartId=? ORDER BY productId")) {
                    ps.setLong(1,cartId); try(ResultSet rs=ps.executeQuery()){ while(rs.next()) lines.add(new OrderLine(rs.getLong(1),rs.getInt(2))); }
                }
                if(lines.isEmpty()) throw new IllegalArgumentException("Giỏ hàng đang trống");

                BigDecimal total=BigDecimal.ZERO;
                for(OrderLine line:lines){
                    Product8 p=productDAO.findByIdForUpdate(con,line.productId());
                    if(line.quantity()>p.getStockQuantity()) throw new IllegalArgumentException("Mặt hàng '"+p.getName()+"' chỉ còn "+p.getStockQuantity());
                    line.product=p; total=total.add(p.getPrice().multiply(BigDecimal.valueOf(line.quantity())));
                }
                String code="HD"+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                        +ThreadLocalRandom.current().nextInt(100,1000);
                long invoiceId;
                try(PreparedStatement ps=con.prepareStatement(
                        "INSERT INTO tblInvoice8(invoiceCode,shippingAddress,status,totalAmount,customerId) VALUES(?,?,'PENDING',?,?)",Statement.RETURN_GENERATED_KEYS)){
                    ps.setString(1,code); ps.setString(2,address.trim()); ps.setBigDecimal(3,total); ps.setLong(4,customerId); ps.executeUpdate();
                    try(ResultSet rs=ps.getGeneratedKeys()){rs.next();invoiceId=rs.getLong(1);}
                }
                try(PreparedStatement detail=con.prepareStatement("INSERT INTO tblInvoiceDetail8(invoiceId,productId,quantity,unitPrice) VALUES(?,?,?,?)")){
                    for(OrderLine line:lines){
                        detail.setLong(1,invoiceId);detail.setLong(2,line.productId());detail.setInt(3,line.quantity());detail.setBigDecimal(4,line.product.getPrice());detail.addBatch();
                        productDAO.decreaseStock(con,line.productId(),line.quantity());
                    }
                    detail.executeBatch();
                }
                try(PreparedStatement ps=con.prepareStatement("DELETE FROM tblCartItem8 WHERE cartId=?")){ps.setLong(1,cartId);ps.executeUpdate();}
                con.commit(); return code;
            } catch(SQLException|RuntimeException e){con.rollback();throw e;}
            finally { con.setAutoCommit(true); }
        }
    }

    private static final class OrderLine {
        private final long productId; private final int quantity; private Product8 product;
        private OrderLine(long productId,int quantity){this.productId=productId;this.quantity=quantity;}
        long productId(){return productId;} int quantity(){return quantity;}
    }
}
