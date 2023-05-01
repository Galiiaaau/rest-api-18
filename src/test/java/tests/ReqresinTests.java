package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresinTests {

    @Test
    void successfulLoginTest() {
        String body = "{\n" +
                "    \"email\": \"eve.holt@reqres.in\",\n" +
                "    \"password\": \"cityslicka\"\n" +
                "}";

        given()
                .log().uri()
                .body(body)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void unsuccessfulLoginWithMissingEmailTest() {
        String body = "{\"password\": \"cityslicka\"}";

        given()
                .log().uri()
                .body(body)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }

    @Test
    void unsuccessfulLoginWithMissingPasswordTest() {
        String body = "{\"email\": \"eve.holt@reqres.in\"}";

        given()
                .log().uri()
                .body(body)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    void checkNonExistentUser(){

        String body = "{ \"name\": \"John Wick\", \"id : 4 }";

        given()
                .body(body)
                .contentType(JSON)
                .log().body()
                .when()
                .patch("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(400);
    }

    @Test
    void changeMorpheusJobTest(){

        String body = "{ \"name\": \"John Wick\", \"job\": \"QA automation engineer\" }";

        String expectedJob = "QA automation engineer";

        String actualJob = given()
                .body(body)
                .contentType(JSON)
                .log().uri()
                .log().body()
                .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().path("job");

        assertEquals(expectedJob, actualJob);

    }
}
