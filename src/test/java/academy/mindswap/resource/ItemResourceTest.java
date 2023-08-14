package academy.mindswap.resource;

import academy.mindswap.converter.ItemConverter;
import academy.mindswap.dto.ItemCreateDto;
import academy.mindswap.dto.ItemDto;
import academy.mindswap.model.Item;
import academy.mindswap.model.User;
import academy.mindswap.repository.ItemRepository;
import academy.mindswap.repository.OrderItemRepository;
import academy.mindswap.repository.OrderRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.http.HttpStatus;
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

    ItemDto itemDto = new ItemDto(1L, 50.0F, "toalha");

    ItemCreateDto itemCreateDto = new ItemCreateDto(50.0F, "toalha");

    User admin = new User("admin", "admin@admin.pt", "123");

    User user = new User("andré", "andre@gmail.com", "ola123");

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

        itemRepository.persist(itemConverter.toEntityFromCreateDto(itemCreateDto));
    }

    @Nested
    @Tag("authorization")
    @DisplayName("Errors on authorization")
    class OrderItemAuthorizationError {
        @Test
        @DisplayName("Create an item without authorization")
        public void createItemWithoutAuthorization() {
            given()
                    .header("Content-Type", "application/json")
                    .body(itemCreateDto)
                    .auth().preemptive().basic(user.getEmail(), user.getPassword())
                    .when()
                    .post("/items")
                    .then()
                    .statusCode(HttpStatus.SC_FORBIDDEN);
        }

        @Test
        @DisplayName("Update an item without authorization")
        public void updateItemWithoutAuthorization() {
            given()
                    .header("Content-Type", "application/json")
                    .body(itemDto)
                    .auth().preemptive().basic(user.getEmail(), user.getPassword())
                    .when()
                    .put("/items/1")
                    .then()
                    .statusCode(HttpStatus.SC_FORBIDDEN);
        }

        @Test
        @DisplayName("Delete an item without authorization")
        public void deleteItemWithoutAuthorization() {
            given()
                    .auth().preemptive().basic(user.getEmail(), user.getPassword())
                    .when()
                    .delete("/items/1")
                    .then()
                    .statusCode(HttpStatus.SC_FORBIDDEN);
        }

        @Test
        @DisplayName("Get list of items without authentication")
        public void listItemsWithoutAuthentication() {
            given()
                    .when()
                    .get("/items")
                    .then()
                    .statusCode(HttpStatus.SC_UNAUTHORIZED);
        }

        @Test
        @DisplayName("Get an item without authentication")
        public void getItemWithoutAuthentication() {
            given()
                    .when()
                    .get("/items/1")
                    .then()
                    .statusCode(HttpStatus.SC_UNAUTHORIZED);
        }

        @Test
        @DisplayName("Create an item without authentication")
        public void createItemWithoutAuthentication() {
            given()
                    .header("Content-Type", "application/json")
                    .body(itemCreateDto)
                    .when()
                    .post("/items")
                    .then()
                    .statusCode(HttpStatus.SC_UNAUTHORIZED);
        }

        @Test
        @DisplayName("Update an item without authentication")
        public void updateItemWithoutAuthentication() {
            given()
                    .header("Content-Type", "application/json")
                    .body(itemDto)
                    .when()
                    .put("/items/1")
                    .then()
                    .statusCode(HttpStatus.SC_UNAUTHORIZED);
        }

        @Test
        @DisplayName("Delete an item without authentication")
        public void deleteItemWithoutAuthentication() {
            given()
                    .when()
                    .delete("/items/1")
                    .then()
                    .statusCode(HttpStatus.SC_UNAUTHORIZED);
        }
    }

    @Nested
    @Tag("validations")
    @DisplayName("item invalid crud")
    class ItemCrudValidator {


        @Test
        @DisplayName("Get an item which not exists")
        public void getItemNotFound() {
            given()
                    .auth().preemptive().basic(admin.getEmail(), admin.getPassword())
                    .when()
                    .get("/items/" + 100)
                    .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND);
        }

        @Test
        @DisplayName("Update an item which not exists")
        public void updateItemNotFound() {
            given()
                    .header("Content-Type", "application/json")
                    .body(itemDto)
                    .auth().preemptive().basic(admin.getEmail(), admin.getPassword())
                    .when()
                    .put("/items/" + 10)
                    .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND);
        }

        @Test
        @DisplayName("Delete an item which not exists")
        public void deleteItemNotFound() {
            given()
                    .auth().preemptive().basic(admin.getEmail(), admin.getPassword())
                    .when()
                    .delete("/items/15")
                    .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND);
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
                    .auth().preemptive().basic(admin.getEmail(), admin.getPassword())
                    .when()
                    .post("/items")
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .body("id", is(2))
                    .body("price", is(50.0F))
                    .body("name", is(itemCreateDto.getName()));
        }

        @Test
        @DisplayName("Get list of items as admin")
        public void listItemsAsAdmin() {
            given()
                    .auth().preemptive().basic(admin.getEmail(), admin.getPassword())
                    .when()
                    .get("/items")
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .body("size()", is(1));
        }

        @Test
        @DisplayName("Get an item as admin")
        public void listItemAsAdmin() {
            given()
                    .auth().preemptive().basic(admin.getEmail(), admin.getPassword())
                    .when()
                    .get("/items/1")
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .body("id", is(1))
                    .body("price", is(50.0F))
                    .body("name", is(itemCreateDto.getName()));
        }

        @Test
        @DisplayName("Get list of items as user")
        public void listItemsAsUser() {
            given()
                    .auth().preemptive().basic(user.getEmail(), user.getPassword())
                    .when()
                    .get("/items")
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .body("size()", is(1));
        }

        @Test
        @DisplayName("Get an item as user")
        public void listItemAsUser() {
            given()
                    .auth().preemptive().basic(user.getEmail(), user.getPassword())
                    .when()
                    .get("/items/1")
                    .then()
                    .statusCode(HttpStatus.SC_OK)
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
                    .auth().preemptive().basic(admin.getEmail(), admin.getPassword())
                    .when()
                    .put("/items/" + 1)
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .body("id", is(1))
                    .body("price", is(2.0F))
                    .body("name", is(itemUpdated.getName()));
        }


        @Test
        @DisplayName("Delete an item")
        public void deleteItem() {
            given()
                    .auth().preemptive().basic(admin.getEmail(), admin.getPassword())
                    .when()
                    .delete("/items/1")
                    .then()
                    .statusCode(HttpStatus.SC_NO_CONTENT);

        }
    }
}
