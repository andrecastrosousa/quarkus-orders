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
                    .post("/orders")
                    .then()
                    .statusCode(404);
        }

        @Test
        @DisplayName("Get a list of orders from an invalid user")
        public void getOrdersUserNotFound() {
            given()
                    .get("/orders")
                    .then()
                    .statusCode(404);
        }

        @Test
        @DisplayName("Get an order not founded")
        public void getOrderNotFound() {
            given()
                    .get("/orders/30")
                    .then()
                    .statusCode(400);
        }

        @Test
        @DisplayName("Delete an order not founded")
        public void deleteOrderNotFound() {
            given()
                    .delete("/orders/15")
                    .then()
                    .statusCode(400);
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
                    .get("/orders")
                    .then()
                    .statusCode(200)
                    .body("size()", is(1));
        }

        @Test
        @DisplayName("Delete an orders associated to a user")
        public void deleteOrder() {
            given()
                    .delete("/orders/1")
                    .then()
                    .statusCode(204);

        }
    }
}
