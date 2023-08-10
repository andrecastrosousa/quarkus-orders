package academy.mindswap.resource;

import academy.mindswap.model.Order;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class ItemResourceTest {

    @Test
    public void post() {
        Order order = new Order();
        order.setTotal(50.0F);

        given()
                .header("Content-Type", "application/json")
                .body(order)
                .when()
                .post("/items")
                .then()
                .statusCode(200)
                .body("id", is(7))
                .body("total", is(50.0F))
                .body("orderDateTime", is(order.getOrderDatetime()));
    }

    @Test
    public void getOrders() {
        given()
                .get("/items")
                .then()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    public void getOrdersUserNotFound() {
        given()
                .get("/users/20/orders")
                .then()
                .statusCode(404);
    }

    @Test
    public void updateOrder() {
        Order order = new Order();
        order.setTotal(100.0);
        order.setId(2L);

        given()
                .header("Content-Type", "application/json")
                .body(order)
                .when()
                .put("/users/2/orders/" + order.getId())
                .then()
                .statusCode(200)
                .body("id", is(2))
                .body("total", is(100.0F))
                .body("orderDateTime", is(order.getOrderDatetime()));
    }

    @Test
    public void updateOrderUserNotFound() {
        Order order = new Order();
        order.setTotal(100);
        order.setId(2L);

        given()
                .header("Content-Type", "application/json")
                .body(order)
                .when()
                .put("/users/3/orders/" + order.getId())
                .then()
                .statusCode(404);
    }

    @Test
    public void updateOrderNotFound() {
        Order order = new Order();
        order.setTotal(100);
        order.setId(30L);

        given()
                .header("Content-Type", "application/json")
                .body(order)
                .when()
                .put("/users/3/orders/" + order.getId())
                .then()
                .statusCode(404);
    }

    @Test
    public void deleteOrder() {
        given()
                .delete("/users/1/orders/1")
                .then()
                .statusCode(204);

    }

    @Test
    public void deleteOrderNotFound() {
        given()
                .delete("/users/1/orders/15")
                .then()
                .statusCode(404);
    }
}
