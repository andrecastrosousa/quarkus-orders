package academy.mindswap.resource;

import academy.mindswap.converter.OrderConverter;
import academy.mindswap.dto.OrderCreateDto;
import academy.mindswap.dto.OrderDto;
import academy.mindswap.model.Order;
import academy.mindswap.model.User;
import academy.mindswap.repository.OrderRepository;
import academy.mindswap.repository.UserRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

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

    User user = new User("andré", "test@gmail.com");

    OrderCreateDto orderCreateDto = new OrderCreateDto();

    @BeforeEach
    @Transactional
    public void beforeEach() {
        orderRepository.deleteAll();
        orderRepository.getEntityManager()
                .createNativeQuery("ALTER TABLE Orders AUTO_INCREMENT = 1")
                .executeUpdate();

        userRepository.deleteAll();
        userRepository.getEntityManager()
                .createNativeQuery("ALTER TABLE Users AUTO_INCREMENT = 1")
                .executeUpdate();

        userRepository.persist(user);

        orderCreateDto.setOrderDatetime(LocalDateTime.now());
        Order order = orderConverter.toEntityFromCreateDto(orderCreateDto);
        order.setUser(user);

        orderRepository.persist(order);
    }

    @Nested
    @Tag("validations")
    @DisplayName("Orders invalid crud")
    class OrderCrudValidator {
        @Test
        @DisplayName("Create an order from an invalid user")
        public void postUserNotFound() {
            given()
                    .header("Content-Type", "application/json")
                    .body(orderCreateDto)
                    .when()
                    .post("/users/10/orders")
                    .then()
                    .statusCode(404);
        }

        @Test
        @DisplayName("Get a list of orders from an invalid user")
        public void getOrdersUserNotFound() {
            given()
                    .get("/users/20/orders")
                    .then()
                    .statusCode(404);
        }

        @Test
        @DisplayName("Get an order not founded")
        public void getOrderNotFound() {
            given()
                    .get("/users/3/orders/30")
                    .then()
                    .statusCode(404);
        }

        @Test
        @DisplayName("Delete an order not founded")
        public void deleteOrderNotFound() {
            given()
                    .delete("/users/1/orders/15")
                    .then()
                    .statusCode(404);
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
                    .when()
                    .post("/users/1/orders")
                    .then()
                    .statusCode(200)
                    .body("id", is(3))
                    .body("total", is(0.0F))
                    .body("orderDatetime", is(orderCreateDto.getOrderDatetime().toString()));
        }

        @Test
        @DisplayName("Get a list of orders associated to user")
        public void getOrders() {
            given()
                    .get("/users/1/orders")
                    .then()
                    .statusCode(200)
                    .body("size()", is(1));
        }

        @Test
        @DisplayName("Delete an orders associated to a user")
        public void deleteOrder() {
            given()
                    .delete("/users/1/orders/1")
                    .then()
                    .statusCode(204);

        }
    }
}
