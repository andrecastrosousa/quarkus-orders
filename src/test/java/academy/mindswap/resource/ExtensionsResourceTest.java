package academy.mindswap.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
class ExtensionsResourceTest {

    @Test
    void whenGetById_thenReturnExtension() {
        given().when()
                .get("/callrestapi/id/io.quarkus:quarkus-resteasy-reactive")
                .then()
                .statusCode(200)
                //receive json array
                .body("[0].id", is("io.quarkus:quarkus-resteasy-reactive"));
    }

}