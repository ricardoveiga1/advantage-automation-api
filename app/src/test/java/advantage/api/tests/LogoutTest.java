package advantage.api.tests;

import advantage.api.support.api.LoginApi;
import advantage.api.support.domain.Login;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.put;
import static org.hamcrest.CoreMatchers.is;

@DisplayName("Testes de logout do usuário")
public class LogoutTest extends BaseTest{

    private static final String LOGOUT_USER_ENDPOINT = "/logout";
    LoginApi loginApi = new LoginApi();
    int getId;
    String token;
    Login newLogin = Login.builder().build();

    @BeforeEach
    @DisplayName("Logando com usuário padrão do builder")
    void setupLogout(){
        Response response = loginApi.loginUser(newLogin);
        getId = response.jsonPath().getInt("statusMessage.userId");
        token = response.jsonPath().getString("statusMessage.token");
    }

    @Test
    public void logoutIncorrectAccId(){
        String str_userId = String.valueOf(getId);
        Map<String, String> logoutData = new HashMap<>();
        logoutData.put("accountId", "teste");
        logoutData.put("token", token);

//        FilterableRequestSpecification req1 = (FilterableRequestSpecification) RestAssured.requestSpecification;
//        req1.removeHeader("Content-Type");
//        FilterableResponseSpecification req = (FilterableResponseSpecification) RestAssured.responseSpecification;

        given().
                body(logoutData).
                when().
                post(LOGOUT_USER_ENDPOINT).
                then().log().all().
                statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR).
                header("Content-Type", "")
        ;
    }

    @Test
    public void logoutIncorrectToken(){
        String str_userId = String.valueOf(getId);

        Map<String, String> logoutData = new HashMap<>();
        logoutData.put("accountId", str_userId);
        logoutData.put("token", "teste");

        given().
                body(logoutData).
                when().
                post(LOGOUT_USER_ENDPOINT).
                then().log().all().
                statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR).
                header("Content-Type", "application/json").
                body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/logout-error.json")).
                body("response.success", is(false),
                        "response.userId", is(-1),
                        "response.reason", is("Invalid user name or password"))
        ;
    }

    @AfterEach
    public void logoutUser(){
        String str_userId = String.valueOf(getId);

        Map<String, String> logoutData = new HashMap<>();
        logoutData.put("accountId", str_userId);
        logoutData.put("token", token);

        given().
                body(logoutData).
                when().
                post(LOGOUT_USER_ENDPOINT).
                then().log().all().
                statusCode(HttpStatus.SC_OK).
                header("Content-Type", "application/json").
                body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/logout-succeeded.json")).
                body("response.success", is(true),
                        "response.userId", is(getId),
                        "response.reason", is("Logout Successful"))
        ;
    }

}
