package com.shopman.dao;

import com.shopman.model.Invoice8;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO8 extends BaseDAO8 {
    public InvoiceDAO8() { super(); }
    public InvoiceDAO8(DataSource ds) { super(ds); }

    public List<Invoice8> findByCustomer(long customerId) throws SQLException {
        return find("WHERE customerId=? ORDER BY orderDate DESC", customerId);
    }

    public List<Invoice8> findPending() throws SQLException {
        return find("WHERE status='PENDING' ORDER BY orderDate", null);
    }

    private List<Invoice8> find(String suffix, Long value) throws SQLException {
        List<Invoice8> result = new ArrayList<>();
        try (Connection con=getConnection(); PreparedStatement ps=con.prepareStatement("SELECT * FROM tblInvoice8 " + suffix)) {
            if (value != null) ps.setLong(1,value);
            try (ResultSet rs=ps.executeQuery()) {
                while(rs.next()) {
                    Invoice8 i=new Invoice8(); i.setId(rs.getLong("id")); i.setInvoiceCode(rs.getString("invoiceCode"));
                    i.setOrderDate(rs.getTimestamp("orderDate").toLocalDateTime()); i.setShippingAddress(rs.getString("shippingAddress"));
                    i.setStatus(rs.getString("status")); i.setTotalAmount(rs.getBigDecimal("totalAmount")); i.setCustomerId(rs.getLong("customerId"));
                    result.add(i);
                }
            }
        }
        return result;
    }

    public void approve(long invoiceId, long salesStaffId) throws SQLException {
        try (Connection con=getConnection(); PreparedStatement ps=con.prepareStatement(
                "UPDATE tblInvoice8 SET status='APPROVED', approvedBySalesStaffId=? WHERE id=? AND status='PENDING'")) {
            ps.setLong(1,salesStaffId); ps.setLong(2,invoiceId);
            if(ps.executeUpdate()!=1) throw new IllegalArgumentException("Hóa đơn không tồn tại hoặc đã được xử lý");
        }
    }
}
