# ShopMan

Ứng dụng Java Web minh họa hệ thống quản lý bán hàng bách hóa trực tuyến, tập trung vào hai module:

1. Nhân viên quản lý thêm thông tin mặt hàng.
2. Khách hàng tìm hàng, thêm vào giỏ và đặt hàng trực tuyến.

## Công nghệ

- Java 17, Jakarta Servlet 6, JSP/JSTL 3
- JDBC, MySQL 8
- Maven WAR, Apache Tomcat 10.1+
- JUnit 5 và H2 cho kiểm thử DAO/nghiệp vụ

## Chạy ứng dụng

1. Chạy `database/shopman.sql` trong MySQL.
2. Sửa tài khoản MySQL trong `src/main/resources/database.properties` (hoặc đặt biến môi trường `SHOPMAN_DB_URL`, `SHOPMAN_DB_USER`, `SHOPMAN_DB_PASSWORD`).
3. Chạy `mvn clean test package`.
4. Chép `target/shopman.war` vào thư mục `webapps` của Tomcat 10.1+.
5. Truy cập `http://localhost:8080/shopman/`.

Tài khoản mẫu (mật khẩu chỉ dùng cho đồ án):

- Quản lý: `manager` / `123456`
- Khách hàng: `customer` / `123456`
- Bán hàng: `sales` / `123456`

## Package chính

- `com.shopman.model`: toàn bộ lớp thực thể.
- `com.shopman.dao`: truy cập dữ liệu JDBC.
- `com.shopman.service`: transaction nghiệp vụ đặt hàng.
- `com.shopman.view.manager`: giao diện/chức năng quản lý mặt hàng.
- `com.shopman.view.customer`: tìm hàng, giỏ hàng, đặt hàng.
- `com.shopman.view.salesstaff`: danh sách và duyệt hóa đơn.
- `com.shopman.view.auth`, `com.shopman.filter`: đăng nhập và bảo vệ URL.

Giá trị tiền dùng `DECIMAL/BigDecimal`. Khi submit đơn, hệ thống khóa các dòng sản phẩm (`SELECT ... FOR UPDATE`), kiểm tra tồn, tạo hóa đơn + chi tiết, trừ tồn và xóa giỏ trong cùng một transaction.
