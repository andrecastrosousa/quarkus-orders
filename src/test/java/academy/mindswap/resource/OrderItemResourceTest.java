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

    OrderItemAddDto orderItemAddDto = new OrderItemAddDto();

    OrderItemUpdateDto orderItemUpdateDto = new OrderItemUpdateDto();

    @BeforeEach
    @Transactional
    public void beforeEach() {
        User user = new User("andré", "test@gmail.com");
        userRepository.persist(user);

        Order order = new Order();
        order.setUser(user);
        order.setOrderDatetime(LocalDateTime.now());
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
    @Tag("errors")
    @DisplayName("Errors on Items of order CRUD")
    class OrderItemErrorCrud {
        @Test
        @DisplayName("List items of a non existent order")
        public void listItemsOfNonExistentOrder() {
            given()
                    .when()
                    .get("/users/1/orders/20/items")
                    .then()
                    .statusCode(400);
        }

        @Test
        @DisplayName("List items of a non existent user")
        public void listItemsOfNonExistentUser() {
            given()
                    .when()
                    .get("/users/20/orders/1/items")
                    .then()
                    .statusCode(400);
        }

        @Test
        @DisplayName("Add a non existent item to an order")
        public void addNonExistentItemToOrder() {
            Item item = new Item("copo", 2);
            item.setId(20L);

            orderItemAddDto.setItem(item);
            orderItemAddDto.setQuantity(5);

            given()
                    .contentType(ContentType.JSON)
                    .body(orderItemAddDto)
                    .when()
                    .post("/users/1/orders/1/items")
                    .then()
                    .statusCode(400);
        }

        @Test
        @DisplayName("Add item to a non existent order")
        public void addItemToNonExistentOrder() {
            Item item = new Item("copo", 2);
            item.setId(1L);

            orderItemAddDto.setItem(item);
            orderItemAddDto.setQuantity(5);

            given()
                    .contentType(ContentType.JSON)
                    .body(orderItemAddDto)
                    .when()
                    .post("/users/1/orders/20/items")
                    .then()
                    .statusCode(400);
        }

        @Test
        @DisplayName("Add Item to a non existent user")
        public void addItemToNonExistentUser() {
            Item item = new Item("copo", 2);
            item.setId(1L);

            orderItemAddDto.setItem(item);
            orderItemAddDto.setQuantity(5);

            given()
                    .contentType(ContentType.JSON)
                    .body(orderItemAddDto)
                    .when()
                    .post("/users/20/orders/1/items")
                    .then()
                    .statusCode(400);
        }

        @Test
        @DisplayName("Update a non existent item on order")
        public void updateNonExistentItemOnOrder() {
            orderItemUpdateDto.setId(1L);
            orderItemUpdateDto.setQuantity(3);

            given()
                    .contentType(ContentType.JSON)
                    .body(orderItemUpdateDto)
                    .when()
                    .put("/users/1/orders/1/items/20")
                    .then()
                    .statusCode(400);
        }

        @Test
        @DisplayName("Update item on non existent user")
        public void updateItemOnNonExistentUser() {
            orderItemUpdateDto.setId(1L);
            orderItemUpdateDto.setQuantity(3);

            given()
                    .contentType(ContentType.JSON)
                    .body(orderItemUpdateDto)
                    .when()
                    .put("/users/20/orders/1/items/1")
                    .then()
                    .statusCode(400);
        }

        @Test
        @DisplayName("Update item on non existent order")
        public void updateItemOnNonExistentOrder() {
            orderItemUpdateDto.setId(1L);
            orderItemUpdateDto.setQuantity(3);

            given()
                    .contentType(ContentType.JSON)
                    .body(orderItemUpdateDto)
                    .when()
                    .put("/users/1/orders/20/items/1")
                    .then()
                    .statusCode(400);
        }

        @Test
        @DisplayName("Remove a non existent item from order")
        public void removeItemFromOrder() {
            given()
                    .delete("/users/1/orders/1/items/20")
                    .then()
                    .statusCode(400);
        }

        @Test
        @DisplayName("Remove item from non existent user")
        public void removeItemFromNonExistentUser() {
            given()
                    .delete("/users/20/orders/1/items/1")
                    .then()
                    .statusCode(400);
        }

        @Test
        @DisplayName("Remove item from non existent order")
        public void removeItemFromNonExistentOrder() {
            given()
                    .delete("/users/1/orders/20/items/1")
                    .then()
                    .statusCode(400);
        }
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
