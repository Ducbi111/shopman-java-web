package com.shopman.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseDAO8 {
    protected final DataSource dataSource;

    protected BaseDAO8() { this(Database.getDataSource()); }
    protected BaseDAO8(DataSource dataSource) { this.dataSource = dataSource; }
    protected Connection getConnection() throws SQLException { return dataSource.getConnection(); }
}
