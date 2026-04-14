# Personal Notebook Management

Dự án Web Quản lý Sổ tay Cá nhân. Xây dựng bằng Java Spring Boot 3.x (thay thế cho 4.x chưa phát hành), Spring Security 6, Spring Data JPA, PostgreSQL, và giao diện Thymeleaf + Bootstrap 5.

## Chức năng
- **Người dùng**: Đăng ký, đăng nhập, Quản lý Sổ tay cá nhân (CRUD trọn vẹn, tìm đúng dữ liệu của mình).
- **Admin**: Đăng nhập trang quản trị riêng, xem toàn bộ Sổ tay, quản trị và xóa người dùng.
- **Bảo mật**: Mã hóa BCrypt, chặn truy cập không hợp lệ.

## Hướng dẫn cài đặt

### 1. Cài đặt PostgreSQL + pgAdmin 4
- Tải và cài đặt PostgreSQL bản 14 hoặc mới hơn. Quá trình cài đặt nhớ mật khẩu của user `postgres` (mặc định PostgreSQL thường cấp user này).
- Mở **pgAdmin 4**, đăng nhập vào server local.

### 2. Tạo Database
1. Trong pgAdmin 4, chuột phải vào **Databases** -> **Create** -> **Database...**
2. Đặt tên database là: `personal_notebook_db`.
3. Lưu lại.

### 3. Cấu hình & Import Database (Tùy chọn)
- Nếu muốn chạy SQL thủ công: Nhấp chuột phải vào `personal_notebook_db` -> **Query Tool**. Copy nội dung file `database.sql` dán vào và chạy (F5).
- *Lưu ý*: Dự án đã được cấu hình `spring.jpa.hibernate.ddl-auto=update` và một **DataSeeder** nội bộ nên database và tài khoản admin mặc định sẽ được **TỰ ĐỘNG KHỞI TẠO** khi bạn chạy thành công lần đầu, do đó bạn có thể bỏ qua bước nạp script bằng tay.

### 4. Chạy dự án
1. Mở file `src/main/resources/application.properties`
2. Đổi giá trị `spring.datasource.password=root` thành mật khẩu PostgreSQL thực tế của máy bạn.
3. Mở Terminal (Command Prompt / Powershell) tại thư mục chứa file `pom.xml`.
4. Chạy lệnh: `mvn spring-boot:run` (Đảm bảo máy đã cài Maven và Java 17+).

### 5. Tài khoản mặc định

* **Admin:**
  * Trang đăng nhập: `http://localhost:8080/admin/login`
  * Username: `admin`
  * Password: `admin123`

* **Người dùng bình thường:** 
  * Truy cập `http://localhost:8080/` nhấn "Đăng ký" để lập tài khoản và dùng thử.
