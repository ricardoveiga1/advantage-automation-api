package advantage.api.tests;

import advantage.api.support.api.LoginApi;
import advantage.api.support.domain.Login;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.specification.FilterableRequestSpecification;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@DisplayName("Testes de logout do usuário")
public class LogoutTest extends BaseTest{

    private static final String LOGOUT_USER_ENDPOINT = "/logout";
    LoginApi loginApi = new LoginApi();

    @Test
    public void logoutUser(){
        Login newLogin = Login.builder().build();

        //Login para pegar token e userId e conversão em string
        String token = loginApi.getToken(newLogin);
        int userId = loginApi.getUserId(newLogin);
        String str_userId = String.valueOf(userId);

        Map<String, String> logoutData = new HashMap<>();
        logoutData.put("accountId", str_userId);
        logoutData.put("token", token);

        given().
                body(logoutData).
                when().
                post(LOGOUT_USER_ENDPOINT).
                then().log().all().
                statusCode(HttpStatus.SC_OK).
                body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/logout-succeeded.json")).
                body("response.success", is(true),
                        "response.userId", is(userId),
                        "response.reason", is("Logout Successful"))
        ;
    }

    @Test
    public void logoutIncorrectAccId(){
        Login newLogin = Login.builder().build();
        String token = loginApi.getToken(newLogin);
        int userId = loginApi.getUserId(newLogin);

        Map<String, String> logoutData = new HashMap<>();
        logoutData.put("accountId", "test-err");
        logoutData.put("token", token);

        //Removendo Header Content-Type, pois o Header de retorno é X-Content-Type-Options
        FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
        req.removeHeader("Content-Type");

        given().
                body(logoutData).
                when().
                post(LOGOUT_USER_ENDPOINT).
                then().log().all().
                statusCode(HttpStatus.SC_FORBIDDEN)
        ;
    }

    @Test
    public void logoutIncorrectToken(){
        Login newLogin = Login.builder().build();
        String token = loginApi.getToken(newLogin);
        int userId = loginApi.getUserId(newLogin);
        String str_userId = String.valueOf(userId);

        Map<String, String> logoutData = new HashMap<>();
        logoutData.put("accountId", str_userId);
        logoutData.put("token", "teste-erro");

        given().
                body(logoutData).
                when().
                post(LOGOUT_USER_ENDPOINT).
                then().log().all().
                statusCode(HttpStatus.SC_FORBIDDEN)
//                body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/logout-succeeded.json")).
//                body("response.success", is(false),
//                        "response.userId", is(-1),
//                        "response.reason", is("Invalid user name or password"))
        ;
    }


}
