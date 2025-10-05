# Backend API cho Ứng dụng Bán Gundam (Gundam Shop API)

Đây là project backend, được xây dựng bằng **Spring Boot**, cung cấp bộ RESTful API hoàn chỉnh cho ứng dụng di động bán các sản phẩm Gundam (model kit, dụng cụ, sơn...). Project được thiết kế theo **Kiến trúc Phân tầng (Layered Architecture)** để đảm bảo tính module, dễ bảo trì và mở rộng.

## Mục lục

- [Tính năng chính](#tính-năng-chính)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Kiến trúc](#kiến-trúc)
- [Thiết kế Cơ sở dữ liệu](#thiết-kế-cơ-sở-dữ-liệu)
- [Cấu trúc API Endpoints](#cấu-trúc-api-endpoints-gợi-ý)
- [Hướng dẫn Cài đặt và Chạy dự án](#hướng-dẫn-cài-đặt-và-chạy-dự-án)

## Tính năng chính

Ứng dụng cung cấp các chức năng cốt lõi của một hệ thống thương mại điện tử:

1.  **Xác thực người dùng (Authentication - 10%)**
    -   **Đăng ký:** Cho phép người dùng tạo tài khoản mới với username, password, email, SĐT, địa chỉ.
    -   **Đăng nhập:** Xác thực người dùng hiện có qua thông tin đăng nhập.
    -   **Bảo mật mật khẩu:** Mật khẩu người dùng được băm (hash) bằng thuật toán `Bcrypt` trước khi lưu vào cơ sở dữ liệu.

2.  **Danh sách sản phẩm (Product List - 15%)**
    -   **Lấy dữ liệu:** Tải danh sách sản phẩm từ database thông qua API.
    -   **Hiển thị:** Cung cấp thông tin cơ bản: tên, hình ảnh, giá, mô tả ngắn.
    -   **Sắp xếp & Lọc:** Hỗ trợ API để sắp xếp theo giá, độ phổ biến và lọc theo danh mục, thương hiệu, khoảng giá.

3.  **Chi tiết sản phẩm (Product Details - 15%)**
    -   **Hiển thị chi tiết:** Cung cấp đầy đủ thông tin: mô tả, thông số kỹ thuật, nhiều hình ảnh.
    -   **Thêm vào giỏ hàng:** API cho phép người dùng thêm sản phẩm vào giỏ với số lượng tùy chọn.

4.  **Giỏ hàng (Product Cart - 15%)**
    -   **Tổng quan:** API để xem tất cả sản phẩm trong giỏ hàng.
    -   **Quản lý giỏ hàng:** Các API để cập nhật số lượng, xóa sản phẩm, hoặc xóa toàn bộ giỏ hàng.
    -   **Tính tổng tiền:** Tự động tính toán lại tổng giá trị giỏ hàng sau mỗi thay đổi.

5.  **Thanh toán (Billing - 10%)**
    -   **Tích hợp cổng thanh toán:** Sẵn sàng tích hợp với các cổng như VNPay, ZaloPay, PayPal.
    -   **Xử lý đơn hàng:** Cho phép người dùng chọn phương thức thanh toán, nhập thông tin giao hàng và tạo đơn hàng.
    -   **Xác nhận đơn hàng:** API trả về thông tin xác nhận đơn hàng sau khi thanh toán thành công.

6.  **Thông báo (Notification - 15%)**
    -   **Badge giỏ hàng:** API để lấy số lượng sản phẩm trong giỏ hàng, phục vụ cho việc hiển thị badge trên icon ứng dụng di động.

7.  **Bản đồ (Map Screen - 10%)**
    -   **Vị trí cửa hàng:** API cung cấp tọa độ (kinh độ, vĩ độ) của cửa hàng để hiển thị trên bản đồ.

8.  **Trò chuyện (Chat Screen - 10%)**
    -   **Chat thời gian thực:** Cung cấp các API để xây dựng chức năng chat giữa người dùng và nhân viên hỗ trợ, có thể tích hợp với WebSocket hoặc Firebase.

## Công nghệ sử dụng

-   **Backend:** Spring Boot, Spring MVC (REST), Spring Data JPA, Spring Security
-   **Ngôn ngữ:** Java 17+
-   **Cơ sở dữ liệu:** MySQL / SQL Server
-   **Xác thực:** JWT (JSON Web Tokens)
-   **Build Tool:** Maven / Gradle
-   **API Documentation:** Swagger (OpenAPI 3)

## Kiến trúc

Dự án được cấu trúc theo **Kiến trúc Phân tầng (Layered Architecture)** để tách biệt các mối quan tâm:

-   `controller`: Tầng Presentation, chịu trách nhiệm xử lý các HTTP request/response và giao tiếp với client.
-   `service`: Tầng Business Logic, chứa toàn bộ logic nghiệp vụ cốt lõi của ứng dụng.
-   `repository`: Tầng Data Access, chịu trách nhiệm truy cập và thao tác với cơ sở dữ liệu.
-   `model` / `entity`: Các đối tượng Java được ánh xạ tới các bảng trong database.
-   `dto`: Các đối tượng truyền dữ liệu, giúp che giấu cấu trúc entity và tùy biến dữ liệu cho API.
-   `mapper`: Chuyển đổi giữa DTO và Entity.
-   `exception`: Xử lý các lỗi và ngoại lệ một cách tập trung (`@ControllerAdvice`).
-   `configuration`: Chứa các file cấu hình (ví dụ: `SecurityConfig`).

## Thiết kế Cơ sở dữ liệu

Sơ đồ quan hệ thực thể (ERD) được thiết kế để hỗ trợ tất cả các tính năng trên, bao gồm các bảng chính như: `Users`, `Products`, `Categories`, `Brands`, `Orders`, `OrderItems`, `CartItems`, `Conversations`, `Messages`...

## Cấu trúc API Endpoints (Gợi ý)
null

## Hướng dẫn Cài đặt và Chạy dự án

### Yêu cầu

-   JDK 17 hoặc cao hơn.
-   Maven 3.6+ hoặc Gradle 7+.
-   MySQL Server 8.0+ hoặc SQL Server.

### Các bước cài đặt

1.  **Clone a repository**
    ```bash
    git clone https://your-repository-url.git
    cd gundam-shop-api
    ```

2.  **Cấu hình Cơ sở dữ liệu**
    -   Tạo một database mới trong MySQL (ví dụ: `gundam_shop_db`).
    -   Mở file `src/main/resources/application.properties`.
    -   Cập nhật lại các thông tin sau để kết nối đến database của bạn:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/gundam_shop_db
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    
    # Hibernate sẽ tự động tạo/cập nhật các bảng dựa trên Entity
    spring.jpa.hibernate.ddl-auto=update
    ```

3.  **Chạy ứng dụng**
    -   **Sử dụng Maven:**
        ```bash
        mvn spring-boot:run
        ```
    -   **Sử dụng Gradle:**
        ```bash
        ./gradlew bootRun
        ```
    Ứng dụng sẽ khởi động và chạy tại `http://localhost:8080`.

4.  **Kiểm tra**
    -   Mở trình duyệt và truy cập vào địa chỉ Swagger UI để xem và thử nghiệm các API:
    `http://localhost:8080/swagger-ui.html`
