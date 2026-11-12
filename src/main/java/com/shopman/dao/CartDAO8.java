package com.shopman.dao;

import com.shopman.model.Cart8;
import com.shopman.model.CartItem8;
import com.shopman.model.Product8;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO8 extends BaseDAO8 {
    public CartDAO8() { super(); }
    public CartDAO8(DataSource ds) { super(ds); }

    public Cart8 findOrCreate(long customerId) throws SQLException {
        try (Connection con = getConnection()) {
            con.setAutoCommit(false);
            try {
                Long cartId = findCartId(con, customerId);
                if (cartId == null) {
                    try (PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO tblCart8(customerId) VALUES(?)", Statement.RETURN_GENERATED_KEYS)) {
                        ps.setLong(1, customerId); ps.executeUpdate();
                        try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); cartId = rs.getLong(1); }
                    }
                }
                Cart8 cart = loadCart(con, cartId, customerId);
                con.commit();
                return cart;
            } catch (SQLException e) { con.rollback(); throw e; }
        }
    }

    public void addOrUpdateItem(long customerId, long productId, int quantity) throws SQLException {
        if (quantity <= 0) throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        try (Connection con = getConnection()) {
            con.setAutoCommit(false);
            try {
                Long cartId = findCartId(con, customerId);
                if (cartId == null) {
                    try (PreparedStatement ps = con.prepareStatement("INSERT INTO tblCart8(customerId) VALUES(?)", Statement.RETURN_GENERATED_KEYS)) {
                        ps.setLong(1, customerId); ps.executeUpdate();
                        try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); cartId = rs.getLong(1); }
                    }
                }
                Product8 product;
                try (PreparedStatement ps = con.prepareStatement("SELECT * FROM tblProduct8 WHERE id=?")) {
                    ps.setLong(1, productId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new IllegalArgumentException("Mặt hàng không tồn tại");
                        product = ProductDAO8.map(rs);
                    }
                }
                int current = 0;
                try (PreparedStatement ps = con.prepareStatement("SELECT quantity FROM tblCartItem8 WHERE cartId=? AND productId=?")) {
                    ps.setLong(1, cartId); ps.setLong(2, productId);
                    try (ResultSet rs = ps.executeQuery()) { if (rs.next()) current = rs.getInt(1); }
                }
                if (current + quantity > product.getStockQuantity())
                    throw new IllegalArgumentException("Số lượng vượt quá tồn kho (còn " + product.getStockQuantity() + ")");
                if (current == 0) {
                    try (PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO tblCartItem8(cartId,productId,quantity,unitPrice) VALUES(?,?,?,?)")) {
                        ps.setLong(1,cartId); ps.setLong(2,productId); ps.setInt(3,quantity); ps.setBigDecimal(4,product.getPrice()); ps.executeUpdate();
                    }
                } else {
                    try (PreparedStatement ps = con.prepareStatement(
                            "UPDATE tblCartItem8 SET quantity=quantity+?, unitPrice=? WHERE cartId=? AND productId=?")) {
                        ps.setInt(1,quantity); ps.setBigDecimal(2,product.getPrice()); ps.setLong(3,cartId); ps.setLong(4,productId); ps.executeUpdate();
                    }
                }
                con.commit();
            } catch (SQLException | RuntimeException e) { con.rollback(); throw e; }
        }
    }

    public void updateQuantity(long customerId, long itemId, int quantity) throws SQLException {
        try (Connection con = getConnection()) {
            if (quantity <= 0) {
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE ci FROM tblCartItem8 ci JOIN tblCart8 c ON c.id=ci.cartId WHERE ci.id=? AND c.customerId=?")) {
                    ps.setLong(1,itemId); ps.setLong(2,customerId); ps.executeUpdate();
                }
            } else {
                String sql = """
                    UPDATE tblCartItem8 ci JOIN tblCart8 c ON c.id=ci.cartId JOIN tblProduct8 p ON p.id=ci.productId
                    SET ci.quantity=? WHERE ci.id=? AND c.customerId=? AND ?<=p.stockQuantity
                    """;
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1,quantity); ps.setLong(2,itemId); ps.setLong(3,customerId); ps.setInt(4,quantity);
                    if (ps.executeUpdate()!=1) throw new IllegalArgumentException("Số lượng không hợp lệ hoặc vượt tồn kho");
                }
            }
        }
    }

    private Long findCartId(Connection con, long customerId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT id FROM tblCart8 WHERE customerId=?")) {
            ps.setLong(1, customerId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getLong(1) : null; }
        }
    }

    private Cart8 loadCart(Connection con, long cartId, long customerId) throws SQLException {
        Cart8 cart = new Cart8(); cart.setId(cartId); cart.setCustomerId(customerId);
        String sql = """
            SELECT ci.id itemId, ci.cartId, ci.quantity, ci.unitPrice, p.*
            FROM tblCartItem8 ci JOIN tblProduct8 p ON p.id=ci.productId
            WHERE ci.cartId=? ORDER BY ci.id
            """;
        List<CartItem8> items = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1,cartId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem8 item = new CartItem8(); item.setId(rs.getLong("itemId")); item.setCartId(cartId);
                    item.setQuantity(rs.getInt("quantity")); item.setUnitPrice(rs.getBigDecimal("unitPrice"));
                    item.setProduct(ProductDAO8.map(rs)); items.add(item);
                }
            }
        }
        cart.setItems(items); return cart;
    }
}
