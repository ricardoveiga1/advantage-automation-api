package advantage.api.tests;

import advantage.api.support.api.UserApi;
import advantage.api.support.domain.User;
import com.github.javafaker.Faker;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;

@DisplayName("Testes de criação usuário")
public class UserTest extends BaseTest{

    UserApi userApi = new UserApi();

    @Test
    @DisplayName("Não deve criar usuario existente")
    void userNameAlreadyExists(){
        User user = User.builder().build();
        Response newUserResponse = userApi.createUserResponse(user);

        newUserResponse.
                then().log().all().
                statusCode(HttpStatus.SC_FORBIDDEN).
                body("response.success", is(false),
                        "response.userId", is(-1),
                        "response.reason", is("User name already exists")).
                body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/user-succeeded.json")).
                 header("Content-Type", "application/json")
        ;
    }

    @Test
    @DisplayName("Não deve criar usuario com senha inválida")
    void incorrectPassword(){
        User newUser = User.builder().password("abc123").build();//alterando password
        Response newUserResponse = userApi.createUserResponse(newUser);

        newUserResponse.
                then().log().all().
                statusCode(HttpStatus.SC_FORBIDDEN).
                body("response.success", is(false),
                        "response.userId", is(-1),
                        "response.reason", is("Incorrect user name or password.")).
                body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/user-succeeded.json"));
    }

    @Test
    @DisplayName("Não deve criar usuario com senha inválida")
    void incorrectUserName(){
        User newUser = User.builder().loginName("rikteste*").build();//alterando password
        Response newUserResponse = userApi.createUserResponse(newUser);

        newUserResponse.
                then().log().all().
                statusCode(HttpStatus.SC_FORBIDDEN).
                body("response.success", is(false),
                        "response.userId", is(-1),
                        "response.reason", is("Incorrect user name or password.")).
                body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/user-succeeded.json"));
    }

    @Test
    @DisplayName("Deve criar novo usuario")
    void createNewUserResponse(){
        Faker fake = new Faker();
        User newUser = User.builder().loginName(fake.name().firstName()).build();//alterando userName
        Response newUserResponse = userApi.createUserResponse(newUser);

        newUserResponse.
                then().log().all().
                statusCode(HttpStatus.SC_OK).
                body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/user-succeeded.json")). //validação de contrato
                body("response.success", is(true), //validação funcional
                "response.userId", is(not(empty())),
                "response.reason", is("New user created successfully."))
        ;
    }

}
