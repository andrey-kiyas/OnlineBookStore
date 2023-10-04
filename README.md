# "Online Bookstore"

---

### Project description:

The "Online Bookstore" application is a web application for the convenience of selecting and purchasing books online. In the application, you can add, store, select, buy saved books from different categories, which allows you to flexibly make purchases without the user directly visiting the store. The application implements such Java-based server technologies as Spring Framework, JWT, Docker.

---

### Current project functionality

The application has USER or ADMIN roles. The USER role is assigned automatically to each new user. You can also view the list of all application endpoints.

Servers
http://localhost:8080/api - Generated server url

Authentication management - Endpoints for managing authentication
- POST: /auth/register Register
- POST: /auth/login Login

Order management - Endpoints for managing users orders
- GET: /orders Retrieve user's order history
- POST: /orders Place an order
- PATCH: /orders/{id} Update order status
- GET: /orders/{orderId}/items Retrieve all OrderItems for a specific order
- GET: /orders/{orderId}/items/{itemId} Retrieve a specific OrderItem within an order

Category management - Endpoints for managing categories
- GET: /categories Find all categories
- POST: /categories Save a new category to DB
- GET: /categories/{id} Find category by id
- POST: /categories/{id} Update category data in DB
- DELETE: /categories/{id} Delete category by id
- GET /categories/{id}/books Get all books by category ID

Shopping cart management - Endpoints for shopping cart management
- PUT: /cart/cart-items/{cartItemId} Update quantity of a book in the shopping cart
- DELETE: /cart/cart-items/{cartItemId} Remove a book from the shopping cart
- GET: /cart Retrieve user's shopping cart
- POST: /cart Add book to the shopping cart

Book management - Endpoints for managing books
- GET: /books Find all books
- POST: /books Save a new book to DB
- GET: /books/{id} Find book by id
- POST: /books/{id} Update book data in DB
- DELETE: /books/{id} Delete book by id
- GET: /books/search Search book's by parameters
---
### Technologies
- Java 17
- Spring Boot, Spring Security, Spring data JPA
- MySQL
- Docker, Maven
- Swagger
---
### Running the project:
1. Docker must be installed on your system.
2. You can configure database parameters in the [.env](.env) file.
3. Open a terminal and navigate to the root directory of your project.
4. Run the application using Docker Compose: `docker-compose up`
5. Explore the endpoints using tools like Postman or Swagger
---
### Spring Boot Tasks (MA):
01 - [Spring Boot intro](https://mate.academy/learn/java-spring-boot/java-spring-boot-intro-v2?section=practice&videoId=5550&testTaskSlug=java_create_a_repo "Перейти к описанию задания")  
03 - [Spring Boot web](https://mate.academy/learn/java-spring-boot/java-spring-boot-web?section=practice&videoId=5561&testTaskSlug=java_spring_boot_web "Перейти к описанию задания")  
04 - [Spring Boot Data JPA](https://mate.academy/learn/java-spring-boot/java-spring-boot-data-jpa-v2?section=practice&videoId=5566&testTaskSlug=java_spring_boot_data_jpa "Перейти к описанию задания")  
05 - [GlobalExceptionHandler and data validation](https://mate.academy/learn/java-spring-boot/java-spring-boot-globalexceptionhandler?section=practice&videoId=5577&testTaskSlug=java_globalexceptionhandler_and_data_validation "Перейти к описанию задания")  
06 - [Spring Boot Security](https://mate.academy/learn/java-spring-boot/java-spring-boot-security?section=practice&videoId=5578&testTaskSlug=java_security "Перейти к описанию задания")  
07 - [Spring Boot Practice - Add Category model](https://mate.academy/learn/java-spring-boot/java-spring-boot-practice-v2?section=practice&testTaskSlug=java_add_category_model "Перейти к описанию задания")  
08 - [Spring Boot practice - Add ShoppingCart model](https://mate.academy/learn/java-spring-boot/java-spring-boot-practice-v2?section=practice&testTaskSlug=java_add_shoppingcart_model "Перейти к описанию задания")  
09 - [Spring Boot practice - Add Order model](https://mate.academy/learn/java-spring-boot/java-spring-boot-practice-v2?section=practice&testTaskSlug=java_add_order_model "Перейти к описанию задания")  
10 - [Docker in Spring Boot](https://mate.academy/learn/java-spring-boot/java-spring-boot-docker-v2?section=practice&videoId=5587&testTaskSlug=java_add_docker_to_the_project "Перейти к описанию задания")  
11 - [Add tests for a Book functionality](https://mate.academy/learn/java-spring-boot/java-spring-boot-testing-v2?section=practice&videoId=5595&testTaskSlug=java_add_tests_for_a_book_functionality "Перейти к описанию задания")  
12 - [Add tests for a ShoppingCart functionality](https://mate.academy/learn/java-spring-boot/java-spring-boot-testing-v2?section=practice&videoId=5595&testTaskSlug=java_add_tests_for_a_shoppingcart_functionality "Перейти к описанию задания")  
13 - [Add "README.md"](https://mate.academy/learn/java-spring-boot/java-spring-boot-deploy-the-project?section=practice&videoId=5757&testTaskSlug=java_add_readmemd "Перейти к описанию задания")  
---
#### [<span style="color:grey">OpenAPI definition (SWAGGER)</span>](http://localhost:8080/api/swagger-ui/index.html#/)  

---
