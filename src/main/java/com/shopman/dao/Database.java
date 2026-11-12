package com.shopman.dao;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public final class Database {
    private static volatile DataSource dataSource;

    private Database() {}

    public static DataSource getDataSource() {
        if (dataSource == null) {
            synchronized (Database.class) {
                if (dataSource == null) dataSource = loadDataSource();
            }
        }
        return dataSource;
    }

    public static void setDataSource(DataSource source) {
        dataSource = source;
    }

    private static DataSource loadDataSource() {
        Properties p = new Properties();
        try (InputStream input = Database.class.getResourceAsStream("/database.properties")) {
            if (input == null) throw new IllegalStateException("Không tìm thấy database.properties");
            p.load(input);
            String driver = env("SHOPMAN_DB_DRIVER", p.getProperty("db.driver"));
            Class.forName(driver);
            return new DriverManagerDataSource(
                    env("SHOPMAN_DB_URL", p.getProperty("db.url")),
                    env("SHOPMAN_DB_USER", p.getProperty("db.username")),
                    env("SHOPMAN_DB_PASSWORD", p.getProperty("db.password")));
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Không thể khởi tạo kết nối CSDL", e);
        }
    }

    private static String env(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? fallback : value;
    }

    private record DriverManagerDataSource(String url, String username, String password) implements DataSource {
        @Override public Connection getConnection() throws SQLException { return DriverManager.getConnection(url, username, password); }
        @Override public Connection getConnection(String user, String pass) throws SQLException { return DriverManager.getConnection(url, user, pass); }
        @Override public PrintWriter getLogWriter() { return DriverManager.getLogWriter(); }
        @Override public void setLogWriter(PrintWriter out) { DriverManager.setLogWriter(out); }
        @Override public void setLoginTimeout(int seconds) { DriverManager.setLoginTimeout(seconds); }
        @Override public int getLoginTimeout() { return DriverManager.getLoginTimeout(); }
        @Override public Logger getParentLogger() throws SQLFeatureNotSupportedException { return Logger.getGlobal(); }
        @Override public <T> T unwrap(Class<T> iface) throws SQLException { throw new SQLException("Không hỗ trợ unwrap"); }
        @Override public boolean isWrapperFor(Class<?> iface) { return false; }
    }
}
