package com.shopman.dao;

import com.shopman.model.Product8;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAO8 extends BaseDAO8 {
    public ProductDAO8() { super(); }
    public ProductDAO8(DataSource ds) { super(ds); }

    public List<Product8> findAll() throws SQLException {
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM tblProduct8 ORDER BY id DESC"); ResultSet rs = ps.executeQuery()) {
            return readList(rs);
        }
    }

    public List<Product8> searchByName(String name) throws SQLException {
        String sql = "SELECT * FROM tblProduct8 WHERE LOWER(name) LIKE LOWER(?) ORDER BY name";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + (name == null ? "" : name.trim()) + "%");
            try (ResultSet rs = ps.executeQuery()) { return readList(rs); }
        }
    }

    public Optional<Product8> findById(long id) throws SQLException {
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM tblProduct8 WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? Optional.of(map(rs)) : Optional.empty(); }
        }
    }

    public boolean existsByCodeOrName(String code, String name) throws SQLException {
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(
                "SELECT 1 FROM tblProduct8 WHERE productCode=? OR LOWER(name)=LOWER(?)")) {
            ps.setString(1, code); ps.setString(2, name);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    public Product8 insert(Product8 p) throws SQLException {
        String sql = """
            INSERT INTO tblProduct8(productCode,name,productType,unit,price,stockQuantity,description,image,createdByManagerId)
            VALUES(?,?,?,?,?,?,?,?,?)
            """;
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1,p.getProductCode()); ps.setString(2,p.getName()); ps.setString(3,p.getProductType());
            ps.setString(4,p.getUnit()); ps.setBigDecimal(5,p.getPrice()); ps.setInt(6,p.getStockQuantity());
            ps.setString(7,p.getDescription()); ps.setString(8,p.getImage()); ps.setLong(9,p.getCreatedByManagerId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) p.setId(keys.getLong(1)); }
            return p;
        }
    }

    public Product8 findByIdForUpdate(Connection con, long id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM tblProduct8 WHERE id=? FOR UPDATE")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new SQLException("Mặt hàng không còn tồn tại: " + id);
                return map(rs);
            }
        }
    }

    public void decreaseStock(Connection con, long id, int quantity) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "UPDATE tblProduct8 SET stockQuantity=stockQuantity-? WHERE id=? AND stockQuantity>=?")) {
            ps.setInt(1, quantity); ps.setLong(2, id); ps.setInt(3, quantity);
            if (ps.executeUpdate() != 1) throw new SQLException("Không đủ tồn kho cho mặt hàng " + id);
        }
    }

    private List<Product8> readList(ResultSet rs) throws SQLException {
        List<Product8> result = new ArrayList<>();
        while (rs.next()) result.add(map(rs));
        return result;
    }

    public static Product8 map(ResultSet rs) throws SQLException {
        Product8 p = new Product8();
        p.setId(rs.getLong("id")); p.setProductCode(rs.getString("productCode")); p.setName(rs.getString("name"));
        p.setProductType(rs.getString("productType")); p.setUnit(rs.getString("unit")); p.setPrice(rs.getBigDecimal("price"));
        p.setStockQuantity(rs.getInt("stockQuantity")); p.setDescription(rs.getString("description")); p.setImage(rs.getString("image"));
        p.setCreatedByManagerId(rs.getLong("createdByManagerId"));
        return p;
    }
}
