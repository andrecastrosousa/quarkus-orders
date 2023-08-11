package academy.mindswap.resource;

import academy.mindswap.converter.OrderConverter;
import academy.mindswap.dto.OrderCreateDto;
import academy.mindswap.dto.OrderItemAddDto;
import academy.mindswap.dto.OrderItemUpdateDto;
import academy.mindswap.model.Item;
import academy.mindswap.model.Order;
import academy.mindswap.model.OrderItem;
import academy.mindswap.model.User;
import academy.mindswap.repository.ItemRepository;
import academy.mindswap.repository.OrderItemRepository;
import academy.mindswap.repository.OrderRepository;
import academy.mindswap.repository.UserRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class OrderItemResourceTest {

    @Inject
    UserRepository userRepository;

    @Inject
    OrderRepository orderRepository;

    @Inject
    ItemRepository itemRepository;

    @Inject
    OrderItemRepository orderItemRepository;

    @Inject
    OrderConverter orderConverter;

    OrderItemAddDto orderItemAddDto = new OrderItemAddDto();

    OrderItemUpdateDto orderItemUpdateDto = new OrderItemUpdateDto();

    @BeforeEach
    @Transactional
    public void beforeEach() {
        User user = new User("andré", "test@gmail.com");
        userRepository.persist(user);

        OrderCreateDto orderCreateDto = new OrderCreateDto();
        orderCreateDto.setOrderDatetime(LocalDateTime.now());
        Order order = orderConverter.toEntityFromCreateDto(orderCreateDto);
        order.setUser(user);
        orderRepository.persist(order);

        Item item = new Item("copo", 2);
        itemRepository.persist(item);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setItem(item);
        orderItem.setQuantity(3);
        orderItemRepository.persist(orderItem);

    }

    @Nested
    @Tag("crud")
    @DisplayName("Items of order CRUD")
    class OrderItemCrudTests {
        @Test
        @DisplayName("List items of an order")
        public void listItemsOfOrder() {
            given()
                    .when()
                    .get("/users/1/orders/1/items")
                    .then()
                    .statusCode(200)
                    .body("size()", is(1));
        }

        @Test
        @DisplayName("Add Item to Order")
        public void addItemToOrder() {
            Item item = new Item("copo", 2);
            item.setId(1L);

            orderItemAddDto.setItem(item);
            orderItemAddDto.setQuantity(5);

            given()
                    .contentType(ContentType.JSON)
                    .body(orderItemAddDto)
                    .when()
                    .post("/users/1/orders/1/items")
                    .then()
                    .statusCode(200)
                    .body("id", is(1))
                    .body("total", is(10.0F))
                    .body("orderItems.size()", is(1));

        }

        @Test
        @DisplayName("Update Item on Order")
        public void updateItemOnOrder() {
            orderItemUpdateDto.setId(1L);
            orderItemUpdateDto.setQuantity(3);

            given()
                    .contentType(ContentType.JSON)
                    .body(orderItemUpdateDto)
                    .when()
                    .put("/users/1/orders/1/items/1")
                    .then()
                    .statusCode(200)
                    .body("id", is(1))
                    .body("total", is(6.0F))
                    .body("orderItems.size()", is(1));
        }

        @Test
        @DisplayName("Remove Item from Order")
        public void removeItemFromOrder() {
            given()
                    .when()
                    .get("/users/1/orders/1/items")
                    .then()
                    .statusCode(200)
                    .body("size()", is(1));

            given()
                    .delete("/users/1/orders/1/items/1")
                    .then()
                    .statusCode(204);
        }
    }
}
