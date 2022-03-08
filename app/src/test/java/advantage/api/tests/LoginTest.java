package advantage.api.tests;

import advantage.api.support.api.LoginApi;
import advantage.api.support.domain.Login;
import advantage.api.support.domain.User;
import com.github.javafaker.Faker;
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

@DisplayName("Testes de login de usuário")
public class LoginTest extends BaseTest{

    LoginApi loginApi = new LoginApi();

    @Test
    public void loginUserApi(){
        Login newLogin = Login.builder().build();
        Response login = loginApi.loginUser(newLogin);

        login.
                then().log().all()
                .statusCode(HttpStatus.SC_OK).
                body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/login-succeeded.json")).
                body("statusMessage.success", is(true),
                        "statusMessage.userId", is(not(empty())),
                        "statusMessage.reason", is("Login Successful"),
                        "statusMessage.token", is(not(empty())),
                        "statusMessage.sessionId", is(not(empty())))
        ;
    }

    @Test
    public void loginWithWrongPasswordApi(){
        Faker fake = new Faker();
        Login newLogin = Login.builder().password("teste_error").build();
        Response login = loginApi.loginUser(newLogin);

        login.
                then().log().all()
                .statusCode(HttpStatus.SC_FORBIDDEN).
                body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/login-error.json")).
                body("statusMessage.success", is(false),
                        "statusMessage.userId", is(not(empty())),
                        "statusMessage.reason", is("Incorrect user name or password."))
        ;
    }

    @Test
    public void loginWithWrongUserApi(){
        Faker fake = new Faker();
        Login newLogin = Login.builder().loginName("teste_error").build();
        Response login = loginApi.loginUser(newLogin);

        login.
                then().log().all()
                .statusCode(HttpStatus.SC_FORBIDDEN).
                body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/login-error.json")).
                body("statusMessage.success", is(false),
                        "statusMessage.userId", is(not(empty())),
                        "statusMessage.reason", is("Incorrect user name or password."))
        ;
    }


}
