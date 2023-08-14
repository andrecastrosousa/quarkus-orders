package academy.mindswap.resource;

import academy.mindswap.converter.OrderConverter;
import academy.mindswap.dto.OrderCreateDto;
import academy.mindswap.model.Order;
import academy.mindswap.model.User;
import academy.mindswap.repository.ItemRepository;
import academy.mindswap.repository.OrderItemRepository;
import academy.mindswap.repository.OrderRepository;
import academy.mindswap.repository.UserRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;


@QuarkusTest
public class OrderResourceTest {

    @Inject
    UserRepository userRepository;

    @Inject
    OrderRepository orderRepository;

    @Inject
    OrderConverter orderConverter;

    @Inject
    OrderItemRepository orderItemRepository;

    @Inject
    ItemRepository itemRepository;

    User user = new User("andré", "andre@gmail.com", "ola123");

    User admin = new User("admin", "admin@admin.pt", "123");

    OrderCreateDto orderCreateDto = new OrderCreateDto();

    @BeforeEach
    @Transactional
    public void setup() {
        orderItemRepository.deleteAll();
        orderItemRepository.getEntityManager()
                .createNativeQuery("ALTER TABLE OrderItem AUTO_INCREMENT = 1")
                .executeUpdate();

        itemRepository.deleteAll();
        itemRepository.getEntityManager()
                .createNativeQuery("ALTER TABLE Items AUTO_INCREMENT = 1")
                .executeUpdate();

        orderRepository.deleteAll();
        orderRepository.getEntityManager()
                .createNativeQuery("ALTER TABLE Orders AUTO_INCREMENT = 1")
                .executeUpdate();

        User user = userRepository.findById(2L);

        orderCreateDto.setOrderDatetime(LocalDateTime.now());
        Order order = orderConverter.toEntityFromCreateDto(orderCreateDto);
        order.setUser(user);

        orderRepository.persist(order);
    }

    @Nested
    @Tag("authorization")
    @DisplayName("Errors on authorization")
    class OrderAuthorizationError {
        @Test
        @DisplayName("Create an order without authorization")
        public void createWithoutAuthorization() {
            given()
                    .header("Content-Type", "application/json")
                    .body(orderCreateDto)
                    .auth().preemptive().basic(admin.getEmail(), admin.getPassword())
                    .when()
                    .post("/orders")
                    .then()
                    .statusCode(HttpStatus.SC_FORBIDDEN);
        }

        @Test
        @DisplayName("Get a list of orders without authorization")
        public void getOrdersWithoutAuthorization() {
            given()
                    .auth().preemptive().basic(admin.getEmail(), admin.getPassword())
                    .get("/orders")
                    .then()
                    .statusCode(HttpStatus.SC_FORBIDDEN);
        }

        @Test
        @DisplayName("Get an order without authorization")
        public void getOrderWithoutAuthorization() {
            given()
                    .auth().preemptive().basic(admin.getEmail(), admin.getPassword())
                    .get("/orders/1")
                    .then()
                    .statusCode(HttpStatus.SC_FORBIDDEN);
        }

        @Test
        @DisplayName("Delete an order without authorization")
        public void deleteOrderWithoutAuthorization() {
            given()
                    .auth().preemptive().basic(admin.getEmail(), admin.getPassword())
                    .delete("/orders/1")
                    .then()
                    .statusCode(HttpStatus.SC_FORBIDDEN);
        }

        @Test
        @DisplayName("Create an order without authentication")
        public void createWithoutAuthentication() {
            given()
                    .header("Content-Type", "application/json")
                    .body(orderCreateDto)
                    .when()
                    .post("/orders")
                    .then()
                    .statusCode(HttpStatus.SC_UNAUTHORIZED);
        }

        @Test
        @DisplayName("Get a list of orders without authentication")
        public void getOrdersWithoutAuthentication() {
            given()
                    .get("/orders")
                    .then()
                    .statusCode(HttpStatus.SC_UNAUTHORIZED);
        }

        @Test
        @DisplayName("Get an order without authentication")
        public void getOrderWithoutAuthentication() {
            given()
                    .get("/orders/1")
                    .then()
                    .statusCode(HttpStatus.SC_UNAUTHORIZED);
        }

        @Test
        @DisplayName("Delete an order without authentication")
        public void deleteOrderWithoutAuthentication() {
            given()
                    .delete("/orders/1")
                    .then()
                    .statusCode(HttpStatus.SC_UNAUTHORIZED);
        }
    }

    @Nested
    @Tag("validations")
    @DisplayName("Orders invalid crud")
    class OrderCrudValidator {
        @Test
        @DisplayName("Get an order not founded")
        public void getOrderNotFound() {
            given()
                    .auth().preemptive().basic(user.getEmail(), user.getPassword())
                    .get("/orders/2")
                    .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND);
        }

        @Test
        @DisplayName("Delete an order not founded")
        public void deleteOrderNotFound() {
            given()
                    .auth().preemptive().basic(user.getEmail(), user.getPassword())
                    .delete("/orders/2")
                    .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND);
        }
    }

    @Nested
    @Tag("crud")
    @DisplayName("Orders valid crud")
    class ItemCrudTests {
        @Test
        @DisplayName("Create an order and associate to a user")
        public void post() {
            given()
                    .header("Content-Type", "application/json")
                    .body(orderCreateDto)
                    .auth().preemptive().basic(user.getEmail(), user.getPassword())
                    .when()
                    .post("/orders")
                    .then()
                    .statusCode(200)
                    .body("id", is(2))
                    .body("total", is(0.0F));
        }

        @Test
        @DisplayName("Get a list of orders associated to user")
        public void getOrders() {
            given()
                    .auth().preemptive().basic(user.getEmail(), user.getPassword())
                    .get("/orders")
                    .then()
                    .statusCode(200)
                    .body("size()", is(1));
        }

        @Test
        @DisplayName("Delete an orders associated to a user")
        public void deleteOrder() {
            given()
                    .auth().preemptive().basic(user.getEmail(), user.getPassword())
                    .delete("/orders/1")
                    .then()
                    .statusCode(204);

        }
    }
}
