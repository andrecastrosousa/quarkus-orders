package academy.mindswap.resource;

import academy.mindswap.dto.OrderItemAddDto;
import academy.mindswap.model.Item;
import academy.mindswap.model.Order;
import academy.mindswap.model.User;
import academy.mindswap.repository.ItemRepository;
import academy.mindswap.repository.OrderRepository;
import academy.mindswap.repository.UserRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static io.restassured.RestAssured.given;

@QuarkusTest
public class OrderItemResourceTest {

    @Inject
    UserRepository userRepository;

    @Inject
    OrderRepository orderRepository;

    @Inject
    ItemRepository itemRepository;

    @BeforeEach
    @Transactional
    public void beforeEach() {
        User user = new User();
        user.setName("andré");
        user.setEmail("andré@email.com");
        userRepository.persist(user);
        userRepository.flush();

        Order order = new Order();
        order.setTotal(50);
        order.setUser(user);

        orderRepository.persist(order);
        orderRepository.flush();

        Item item = new Item();
        item.setName("copo");
        item.setPrice(2);
        itemRepository.persist(item);
        itemRepository.flush();
    }

    @Test
    public void listItemsOfOrder() {
        given()
                .when()
                .get("/users/1/orders/1/items")
                .then()
                .statusCode(200);
    }

    @Test
    public void addItemToOrder() {
        Item item = new Item();
        item.setId(1L);
        item.setName("copo");
        item.setPrice(2);

        OrderItemAddDto orderItemAddDto = new OrderItemAddDto();
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
                .body("orderItems.size()", is(1));

    }
}
