# 📚 Danh Sách Công Nghệ Sử Dụng Trong Project LaptopShop

## 🎯 Mục Đích Và Cấu Trúc Project
**LaptopShop** là một ứng dụng web bán laptop được xây dựng bằng Java Spring Boot MVC.
- Phiên bản Spring Boot: 3.5.5
- Phiên bản Java: 17
- Build Tool: Maven

---

## 📋 Danh Sách Các Công Nghệ

### 1️⃣ **Framework & Core (Spring Framework)**

#### **Spring Boot 3.5.5**
- **Mục đích**: Framework chính để xây dựng ứng dụng Java
- **Áp dụng**: 
  - `spring-boot-starter-web` - Xây dựng ứng dụng web
  - `spring-boot-starter` - Core dependencies

#### **Spring MVC (Model-View-Controller)**
- **Mục đích**: Kiến trúc MVC để phân tách logic
- **Áp dụng**:
  ```
  Controller → Service → Repository → Database
  AdminController/ClientController → ProductService → ProductRepository
  ```
- **Các Controller chính**:
  - `ProductDashboardController` - Quản lý sản phẩm
  - `AuthController` - Xác thực người dùng
  - `ItemController` - Quản lý sản phẩm khách hàng
  - `OrderController` - Quản lý đơn hàng
  - `PaymentController` - Xử lý thanh toán

#### **Spring Data JPA**
- **Mục đích**: Object-Relational Mapping (ORM) để tương tác database
- **Dependency**: `spring-boot-starter-data-jpa`
- **Áp dụng**:
  ```java
  @Repository
  public interface ProductRepository extends JpaRepository<Product, Long> {
      Product findByName(String name);
  }
  ```
- **Các Repository**:
  - ProductRepository
  - UserRepository
  - CartRepository
  - OrderRepository
  - PaymentRepository
  - RoleRepository
  - OrderDetailRepository
  - CartDetailRepository

#### **Spring Security**
- **Mục đích**: Bảo mật ứng dụng web
- **Dependency**: `spring-boot-starter-security`
- **Áp dụng**:
  - `SecurityConfiguration.java` - Cấu hình bảo mật
  - `BCryptPasswordEncoder` - Mã hóa mật khẩu
  - `DaoAuthenticationProvider` - Xác thực người dùng
  - Authentication Rules cho phép/cấm truy cập

#### **Spring Session**
- **Mục đích**: Quản lý phiên làm việc người dùng
- **Dependency**: `spring-session-jdbc`
- **Cấu hình**:
  ```properties
  spring.session.store-type=jdbc
  spring.session.timeout=30m
  spring.session.jdbc.initialize-schema=always
  ```
- **Áp dụng**: Lưu session trong database MySQL thay vì RAM

#### **Spring Actuator**
- **Mục đích**: Giám sát và quản lý ứng dụng
- **Dependency**: `spring-boot-starter-actuator`
- **Tính năng**: Cung cấp endpoints cho health check, metrics

---

### 2️⃣ **Persistence Layer**

#### **Hibernate ORM**
- **Mục đích**: ORM framework để map Java objects to database tables
- **Dependency**: `spring-boot-starter-data-jpa` (includes Hibernate)
- **Áp dụng**:
  ```java
  @Entity
  @Table(name = "products")
  public class Product {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      
      @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
      private List<OrderDetail> orderDetails;
  }
  ```

#### **Hibernate Validator**
- **Mục đích**: Xác thực dữ liệu (validation)
- **Dependency**: `hibernate-validator 8.0.1`
- **Áp dụng**:
  ```java
  @NotBlank(message = "Name is required")
  private String name;
  
  @Email(message = "Email should be valid")
  private String email;
  ```

#### **Hibernate JPAModelGen**
- **Mục đích**: Tạo Criteria API models
- **Dependency**: `hibernate-jpamodelgen 6.4.10`
- **Áp dụng**: Dùng cho advanced queries

---

### 3️⃣ **Database Tech Stack**

#### **MySQL 8.0+**
- **Mục đích**: Database chính lưu trữ dữ liệu
- **Dependency**: `mysql-connector-j`
- **Cơ sở dữ liệu**: `laptopshop`
- **Bảng chính**:
  - `products` - Sản phẩm
  - `users` - Người dùng
  - `roles` - Quyền hạn
  - `cart` - Giỏ hàng
  - `cart_detail` - Chi tiết giỏ hàng
  - `orders` - Đơn hàng
  - `order_detail` - Chi tiết đơn hàng
  - `payment` - Thanh toán

#### **JPA (Jakarta Persistence API)**
- **Mục đích**: Java standard API cho ORM
- **Annotations**:
  - `@Entity` - Đánh dấu class là entity
  - `@Table` - Ánh xạ tên bảng
  - `@Id` - Khóa chính
  - `@GeneratedValue` - Tự động sinh ID
  - `@Column` - Cấu hình cột
  - `@OneToMany`, `@ManyToOne` - Quan hệ

---

### 4️⃣ **Web & View Technology**

#### **Jakarta Servlet & JSP (Java Server Pages)**
- **Mục đích**: Render HTML động phía server
- **Dependencies**:
  - `tomcat-embed-jasper` - JSP engine
  - `jakarta.servlet.jsp.jstl-api` - JSTL API
  - `jakarta.servlet.jsp.jstl` - JSTL implementation
- **Áp dụng**: Templates trong `/WEB-INF/view/`
  ```
  /client/auth/register.jsp
  /admin/product/show.jsp
  /client/cart/show.jsp
  ```

#### **JSTL (JavaServer Pages Standard Tag Library)**
- **Mục đích**: Tag library để tạo JSP templates
- **Thẻ sử dụng**:
  - `<c:forEach>` - Vòng lặp
  - `<c:if>` - Điều kiện
  - `<c:out>` - In ra giá trị

#### **HTML/CSS/JavaScript**
- **Mục đích**: Frontend
- **Thư viện CSS**: Bootstrap (trong `/resources/client/css/`)
- **Framework JS**:
  - Bootstrap JavaScript
  - OwlCarousel - Slider
  - Lightbox - Gallery
  - EasingJS - Animation

---

### 5️⃣ **Payment Gateway**

#### **VNPay**
- **Mục đích**: Cổng thanh toán trực tuyến Việt Nam
- **Áp dụng**: Xử lý thanh toán VNPAY
  - `VnPayService.java` - Xử lý logic VNPay
  - `PaymentController.java` - API endpoint thanh toán
  - `PayConfig.java` - Cấu hình VNPay

**Cấu hình trong application.properties**:
```properties
vnpay.tmnCode=31KZJPRD
vnpay.secretKey=WYQTZ763Y7RUMXHL4HYHBC15N5826QP2
vnpay.vnpUrl=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
vnpay.returnUrl=http://localhost:8080/payment/vnpay-return
```

**Class liên quan**: `Payment.java`, `PaymentResponseDto.java`, `PaymentStatus.java`

---

### 6️⃣ **Utility Libraries**

#### **Gson (Google JSON)**
- **Mục đích**: Convert Java objects ↔ JSON
- **Dependency**: `gson 2.10.1`
- **Áp dụng**: Xử lý JSON trong API responses

#### **SLF4J (Simple Logging Facade for Java)**
- **Mục đích**: Logging framework
- **Áp dụng**: Ghi log thao tác
  ```java
  private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
  logger.info("Product saved with id: {}", productId);
  logger.error("Product not found with id: {}", productId);
  ```

#### **JUnit & Spring Boot Test**
- **Mục đích**: Unit testing
- **Dependency**: `spring-boot-starter-test`

#### **Lombok**
- **Mục đích**: Giảm boilerplate code
- **Dependency**: `org.projectlombok:lombok`
- **Annotations**:
  - `@Data` - Tạo getter/setter/toString
  - `@Slf4j` - Tạo logger
  - `@Builder` - Builder pattern

---

### 7️⃣ **Development Tools**

#### **Spring Boot DevTools**
- **Mục đích**: Auto-reload khi thay đổi code
- **Dependency**: `spring-boot-devtools`

#### **Maven**
- **Mục đích**: Build tool & dependency manager
- **File**: `pom.xml`
- **Lệnh chính**:
  ```bash
  mvn clean install
  mvn spring-boot:run
  mvn compile
  ```

---

## 📊 Sơ Đồ Kiến Trúc

```
┌─────────────────────────────────────────────────────────────┐
│                    Frontend (JSP/HTML)                      │
│              /client/auth/  /admin/product/                │
└────────────────────┬────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│              Controller Layer (Spring MVC)                  │
│    AuthController / ProductDashboardController             │
│          ItemController / PaymentController                │
└────────────────────┬────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│              Service Layer (Business Logic)                │
│    ProductService / UserService / OrderService             │
│      CartService / PaymentService (VNPay)                 │
└────────────────────┬────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│         Repository Layer (Spring Data JPA)                 │
│    ProductRepository / UserRepository / OrderRepository    │
│        CartRepository / PaymentRepository                  │
└────────────────────┬────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│           Persistence Layer (Hibernate ORM)                │
│                    JPA Annotations                          │
└────────────────────┬────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│            Database Layer (MySQL Driver)                   │
│                  laptopshop database                        │
└─────────────────────────────────────────────────────────────┘
```

---

## 🔌 Tích Hợp Các Công Nghệ

### **Authentication Flow**
```
User Login Form (JSP)
      ↓
AuthController.postLoginPage()
      ↓
Spring Security (SecurityConfiguration)
      ↓
BCryptPasswordEncoder (Mã hóa mật khẩu)
      ↓
DaoAuthenticationProvider (Xác thực)
      ↓
UserDetailsService (CustomUserDetailsService)
      ↓
Session (Spring Session JDBC)
      ↓
MySQL Database (Lưu session)
```

### **Product Management Flow**
```
ProductDashboardController
      ↓
ProductService (Business Logic)
      ↓
ProductRepository (JPA)
      ↓
Hibernate/JPA (ORM)
      ↓
MySQL JDBC Driver
      ↓
MySQL Database
```

### **Payment Flow**
```
PaymentController
      ↓
VnPayService (VNPay API)
      ↓
GSON (JSON serialization)
      ↓  
VNPay (External Gateway)
      ↓
Payment Validation
      ↓
OrderService (Create Order)
      ↓
MySQL (Store Payment Info)
```

---

## 📁 Cấu Trúc Thư Mục & Công Nghệ

```
LaptopShop-Spring-MVC/
├── pom.xml (Maven dependencies)
│
├── src/main/java/com/basis/anhangda37/
│   ├── LaptopShopApplication.java (Spring Boot entry point)
│   │
│   ├── controller/
│   │   ├── auth/AuthController.java (Spring Security)
│   │   ├── admin/ProductDashboardController.java
│   │   └── client/ItemController.java
│   │
│   ├── service/
│   │   ├── ProductService.java (Business Logic)
│   │   ├── UserService.java
│   │   ├── OrderService.java
│   │   ├── VnPayService.java (VNPay Integration)
│   │   └── CustomUserDetailsService.java (Spring Security)
│   │
│   ├── repository/
│   │   ├── ProductRepository.java (JPA Repository)
│   │   ├── UserRepository.java
│   │   └── OrderRepository.java
│   │
│   ├── domain/ (JPA Entities)
│   │   ├── Product.java (@Entity)
│   │   ├── User.java (@Entity)
│   │   ├── Order.java (@Entity)
│   │   ├── Payment.java (@Entity)
│   │   └── Role.java (@Entity)
│   │
│   ├── dto/ (Data Transfer Objects)
│   │   ├── ProductCriteriaDto.java
│   │   ├── OrderRequestDto.java
│   │   └── PaymentResponseDto.java
│   │
│   ├── config/
│   │   ├── SecurityConfiguration.java (Spring Security config)
│   │   ├── PayConfig.java (VNPay config)
│   │   └── WebMvcConfig.java
│   │
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java (@RestControllerAdvice)
│   │   ├── ProductNotFoundException.java
│   │   └── UserNotFoundException.java
│   │
│   └── util/
│       └── AppConstants.java (Constants)
│
├── src/main/resources/
│   ├── application.properties (Spring Boot config)
│   │   ├── MySQL: jdbc:mysql://localhost:3306/laptopshop
│   │   ├── JPA: spring.jpa.hibernate.ddl-auto=update
│   │   ├── Session: spring.session.store-type=jdbc
│   │   ├── VNPay: vnpay.tmnCode, vnpay.secretKey
│   │   └── Upload: max-file-size=50MB
│   │
│   └── static/ (CSS, JS, Images - Bootstrap, OwlCarousel)
│
└── src/main/webapp/WEB-INF/view/ (JSP Templates)
    ├── admin/product/show.jsp
    ├── admin/product/create.jsp
    ├── client/auth/login.jsp
    ├── client/auth/register.jsp
    └── client/cart/show.jsp
```

---

## 🛠️ Công Nghệ Theo Lớp (Layered Architecture)

### **Presentation Layer (View)**
- JSP (Java Server Pages)
- JSTL (Tag Libraries)
- HTML/CSS/JavaScript
- Bootstrap (UI Framework)

### **Controller Layer**
- Spring MVC (`@Controller`, `@GetMapping`, `@PostMapping`)
- Spring Security (Authentication/Authorization)
- Model & View

### **Service Layer (Business Logic)**
- Spring Service (`@Service`)
- Business logic implementation
- Transaction management (`@Transactional`)

### **Repository Layer (Data Access)**
- Spring Data JPA (`@Repository`)
- JPA Query methods
- Custom queries

### **Persistence Layer**
- Hibernate ORM
- JPA (Jakarta Persistence)
- Entity mapping

### **Database Layer**
- MySQL 8.0+
- JDBC Driver
- Connection pooling

---

## 📈 Công Nghệ Bồ Sung Sau Refactoring

Các công nghệ được thêm vào trong quá trình refactor:
- **Logging**: SLF4J with Logback
- **Validation**: Hibernate Validator
- **Data Transfer**: DTOs (Data Transfer Objects)
- **Exception Handling**: Custom exceptions + GlobalExceptionHandler
- **API Documentation**: JavaDoc comments

---

## ✅ Tóm Tắt

| Lớp | Công Nghệ | Mục Đích |
|-----|-----------|---------|
| **Frontend** | JSP, JSTL, Bootstrap | Giao diện người dùng |
| **Controller** | Spring MVC, Security | Xử lý request/response |
| **Service** | Spring Service | Logic nghiệp vụ |
| **Repository** | Spring Data JPA | Truy cập dữ liệu |
| **ORM** | Hibernate, JPA | Ánh xạ đối tượng |
| **Database** | MySQL, JDBC | Lưu trữ dữ liệu |
| **Security** | Spring Security, BCrypt | Bảo mật ứng dụng |
| **Session** | Spring Session JDBC | Quản lý phiên |
| **Payment** | VNPay | Cổng thanh toán |
| **Utils** | Gson, Lombok, SLF4J | Tiện ích hỗ trợ |
| **Build** | Maven | Quản lý dự án |

