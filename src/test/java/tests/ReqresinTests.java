package tests;

import lombok.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.Specs.*;

public class ReqresinTests {

    @Test
    @DisplayName("Get list of users")
    public void getListOfUsers() {
        int perPage = 5;

        given()
                .spec(request)
                .when()
                .get("/users/?per_page=" + perPage)
                .then()
                .spec(responseOk)
                .log().body()
                .assertThat()
                .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                        hasItem("emma.wong@reqres.in"))
                .and()
                .body("data.last_name[2]", equalTo("Wong"))
                .and()
                .body("data.findAll{it.last_name =~/^\\w{1,10}$/}.last_name.flatten()",
                        hasSize(perPage));
    }


    @Test
    @DisplayName("Successfully delete user")
    public void deleteUser() {
        int userId = 5;

        given()
                .spec(request)
                .when()
                .delete("/users/" + userId)
                .then()
                .spec(responseNoContent)
                .log().body();
    }

    @Test
    @DisplayName("Get user's info")
    void checkSingleUser() {
        UserData data =
                given()
                        .spec(request)
                        .when()
                        .get("/users/2")
                        .then()
                        .spec(responseOk)
                        .extract().as(UserData.class);

                assertEquals(2, data.getUser().getId());
                assertEquals("janet.weaver@reqres.in", data.getUser().getEmail());
    }
}
