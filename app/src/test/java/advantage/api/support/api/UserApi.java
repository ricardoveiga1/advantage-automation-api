package advantage.api.support.api;

import advantage.api.support.domain.Login;
import advantage.api.support.domain.User;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class UserApi{

    private static final String CREATE_USER_ENDPOINT = "/register";

    public User createUser(User user) {
        return given().
                    body(user).
                when().
                    post(CREATE_USER_ENDPOINT).
                then().
                    statusCode(HttpStatus.SC_OK).
                    body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/user-succeeded.json")).
                    body("response.success", is(true),
                            "response.userId", is(not(empty())),
                            "response.reason", is("New user created successfully.")).
                    extract().body().as(User.class)
        ;
    }

    public Response createUserResponse(User user) {
        return given().
                body(user).
                when().
                post(CREATE_USER_ENDPOINT)
        ;
    }
}
