package com.shopman.dao;

import com.shopman.model.UserSession;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MemberDAO8 extends BaseDAO8 {
    public MemberDAO8() { super(); }
    public MemberDAO8(DataSource ds) { super(ds); }

    public Optional<UserSession> authenticate(String username, String password) throws SQLException {
        String sql = """
            SELECT m.id, m.username, m.fullName, c.shippingAddress,
              CASE WHEN mg.memberId IS NOT NULL THEN 'MANAGER'
                   WHEN ss.memberId IS NOT NULL THEN 'SALES'
                   WHEN ds.memberId IS NOT NULL THEN 'DELIVERY'
                   WHEN c.memberId IS NOT NULL THEN 'CUSTOMER' END AS role
            FROM tblMember8 m
            LEFT JOIN tblCustomer8 c ON c.memberId=m.id
            LEFT JOIN tblManager8 mg ON mg.memberId=m.id
            LEFT JOIN tblSalesStaff8 ss ON ss.memberId=m.id
            LEFT JOIN tblDeliverStaff8 ds ON ds.memberId=m.id
            WHERE m.username=? AND m.password=?
            """;
        try (var con = getConnection(); var ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next() || rs.getString("role") == null) return Optional.empty();
                return Optional.of(new UserSession(rs.getLong("id"), rs.getString("username"),
                        rs.getString("fullName"), rs.getString("role"), rs.getString("shippingAddress")));
            }
        }
    }
}
