package academy.mindswap.resource;

import academy.mindswap.converter.ItemConverter;
import academy.mindswap.dto.ItemCreateDto;
import academy.mindswap.dto.ItemDto;
import academy.mindswap.repository.ItemRepository;
import academy.mindswap.repository.OrderItemRepository;
import academy.mindswap.repository.OrderRepository;
import academy.mindswap.repository.UserRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class ItemResourceTest {

    @Inject
    ItemRepository itemRepository;

    @Inject
    ItemConverter itemConverter;

    @Inject
    OrderItemRepository orderItemRepository;

    @Inject
    OrderRepository orderRepository;

    @Inject
    UserRepository userRepository;

    ItemDto itemDto = new ItemDto(1L, 50.0F, "toalha");

    ItemCreateDto itemCreateDto = new ItemCreateDto(50.0F, "toalha");

    @BeforeEach
    @Transactional
    void setup() {
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

        userRepository.deleteAll();
        userRepository.getEntityManager()
                .createNativeQuery("ALTER TABLE Users AUTO_INCREMENT = 1")
                .executeUpdate();

        itemRepository.persist(itemConverter.toEntityFromCreateDto(itemCreateDto));
    }

    @Nested
    @Tag("validations")
    @DisplayName("item invalid crud")
    class ItemCrudValidator {
        @Test
        @DisplayName("Get an item which not exists")
        public void getItemNotFound() {
            given()
                    .header("Content-Type", "application/json")
                    .body(itemDto)
                    .when()
                    .put("/items/" + 10)
                    .then()
                    .statusCode(400);
        }

        @Test
        @DisplayName("Update an item which not exists")
        public void updateItemNotFound() {
            given()
                    .header("Content-Type", "application/json")
                    .body(itemDto)
                    .when()
                    .put("/items/" + 10)
                    .then()
                    .statusCode(400);
        }

        @Test
        @DisplayName("Delete an item which not exists")
        public void deleteItemNotFound() {
            given()
                    .delete("/items/15")
                    .then()
                    .statusCode(400);
        }
    }

    @Nested
    @Tag("crud")
    @DisplayName("User valid crud")
    class ItemCrudTests {
        @Test
        @DisplayName("Create an item")
        public void createItem() {
            given()
                    .header("Content-Type", "application/json")
                    .body(itemCreateDto)
                    .when()
                    .post("/items")
                    .then()
                    .statusCode(200)
                    .body("id", is(2))
                    .body("price", is(50.0F))
                    .body("name", is(itemCreateDto.getName()));
        }

        @Test
        @DisplayName("Get list of items")
        public void listItems() {
            given()
                    .get("/items")
                    .then()
                    .statusCode(200)
                    .body("size()", is(1));
        }

        @Test
        @DisplayName("Get an item")
        public void listItem() {
            given()
                    .get("/items/1")
                    .then()
                    .statusCode(200)
                    .body("id", is(1))
                    .body("price", is(50.0F))
                    .body("name", is(itemCreateDto.getName()));
        }

        @Test
        @DisplayName("Update an item")
        public void updateItem() {
            ItemCreateDto itemUpdated = new ItemCreateDto(2, "copo");

            given()
                    .header("Content-Type", "application/json")
                    .body(itemUpdated)
                    .when()
                    .put("/items/" + 1)
                    .then()
                    .statusCode(200)
                    .body("id", is(1))
                    .body("price", is(2.0F))
                    .body("name", is(itemUpdated.getName()));
        }


        @Test
        @DisplayName("Delete an item")
        public void deleteItem() {

            given()
                    .delete("/items/1")
                    .then()
                    .statusCode(204);

        }
    }
}
