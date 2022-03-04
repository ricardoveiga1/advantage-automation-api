package advantage.api.support.api;

import advantage.api.support.domain.Login;
import advantage.api.support.domain.User;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class LoginApi{

    private static final String LOGIN_USER_ENDPOINT = "/login";


    public Login loginUserOrder(Login login) {
        return given().
                body(login).
                when().
                post(LOGIN_USER_ENDPOINT).
                then().
                statusCode(HttpStatus.SC_OK).
                extract().body().as(Login.class)
        ;
    }

    public Response loginUser(Login login) {

        return given().
                body(login).
                when().log().all().
                post(LOGIN_USER_ENDPOINT)
       ;
    }


    public String getToken(Login login){
        return given().
                when().
                body(login).
                when().
                post(LOGIN_USER_ENDPOINT).
                then().
                extract().
                path("statusMessage.token")
        ;
    }

    public int getUserId(Login login){
        return given().
                body(login).
                when().
                post(LOGIN_USER_ENDPOINT).
                thenReturn().
                path("statusMessage.userId")
        ;
    }

    public Login getLoginOrder(Login login) {
        return given().
                body(login).
                when().
                post(LOGIN_USER_ENDPOINT).
                then().log().all().
                extract().jsonPath().get("response")
        ;
    }

    public void getLoginOrderResponse(Login login) {
        Response response =
                given().
                    body(login).
                    when().
                    post(LOGIN_USER_ENDPOINT).
                    then().log().all().
                    extract().jsonPath().get("response")
        ;
        int getId = response.jsonPath().getInt("response.userId");
        String token = response.jsonPath().getString("response.token");
    }
}
