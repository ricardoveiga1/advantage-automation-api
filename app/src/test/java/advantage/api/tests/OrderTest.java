package advantage.api.tests;

import advantage.api.support.api.LoginApi;
import advantage.api.support.api.UserApi;
import advantage.api.support.domain.Login;
import advantage.api.support.domain.User;
import com.github.javafaker.Faker;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class OrderTest extends BaseTest{

    private static final String BASE_URL = "http://www.advantageonlineshopping.com/order";
    private static final String BASE_PATH = "/api/v1";
    private static final String ORDER_ENDPOINT = "/carts/{id_user}/product/{id_product}/color/{code_color}?hasWarranty={hasW}&quantity={qtd}";
    private static final String REMOVE_ORDER_ENDPOINT = "/carts/{id_user}/product/{id_product}/color/{code_color}";

    private static final String LOGIN_USER_ENDPOINT = "/login";
    private static final String LOGOUT_USER_ENDPOINT = "/logout";

    private String getUri(String endpoint) {

        return BASE_URL + BASE_PATH + endpoint;
    }

    @Nested
    @DisplayName("Teste funcional, gerar usuário, logar, add produto no carrinho, remover produto e efetuar logout")
    public class createOrder {

        Faker fake = new Faker();
        int getId;
        String token;
        UserApi userApi = new UserApi();

        @BeforeEach
        void setupOrder(){
            //CREATE USER
            User orderUser = User.builder().loginName(fake.name().firstName()).build();
            Response expectedUser = userApi.createUserResponse(orderUser);

            expectedUser.
                    then().log().all().
                    statusCode(HttpStatus.SC_OK).
                    body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/user-succeeded.json")).
                    body("response.success", is(true),
                    "response.userId", is(not(empty())),
                    "response.reason", is("New user created successfully."))
            ;

            //LOGIN
            Map<String, String> logintData = new HashMap<>();
            logintData.put("email", orderUser.getEmail());
            logintData.put("loginPassword", orderUser.getPassword());
            logintData.put("loginUser", orderUser.getLoginName());

                Response response =
                        given().
                                body(logintData).log().all().
                                when().
                                post(LOGIN_USER_ENDPOINT)
                ;
                response.
                        then().log().all().
                        extract().body().jsonPath()
                ;

            getId = response.jsonPath().getInt("statusMessage.userId");
            token = response.jsonPath().getString("statusMessage.token");
        }

        @Test
        void orderAddCartTest(){
            System.out.println("ORDER TEST");
            String uri = getUri(ORDER_ENDPOINT);
            String str_userId = String.valueOf(getId);

            given().
                    header("Authorization", "Bearer " + token).
                    pathParam("id_user", str_userId).
                    pathParam("id_product", "15").
                    pathParam("code_color", "414141").
                    pathParam("hasW", false).
                    pathParam("qtd", 1).
                    log().all().
            when().
                    post(uri).
            then().log().all().
                    statusCode(HttpStatus.SC_CREATED).
                    body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/order-succeeded.json")).
                    body("userId", is(getId)).
                    body("productsInCart[0].productId", is (15)).
                    body("productsInCart[0].productName", is ("Beats Studio 2 Over-Ear Matte Black Headphones")).
                    body("productsInCart[0].price", is (179.99f)).
                    body("productsInCart[0].quantity", is (1)).
                    body("productsInCart[0].imageUrl", is ("2200")).
                    body("productsInCart[0].color.code", is ("414141")).
                    body("productsInCart[0].color.name", is ("BLACK")).
                    body("productsInCart[0].color.inStock", is (10)).
                    body("productsInCart[0].exists", is (true)).
                    body("productsInCart[0].hasWarranty", is (false))
            ;
        }

        @AfterEach
        void setDown(){
            deleteProductCart();
            logoutOrder();
        }

        void deleteProductCart(){
            System.out.println("Remove");
            String uri = getUri(REMOVE_ORDER_ENDPOINT);
            String str_userId = String.valueOf(getId);

            given().
                    header("Authorization", "Bearer " + token).
                    pathParam("id_user", str_userId).
                    pathParam("id_product", "15").
                    pathParam("code_color", "414141").
                    when().
                    delete(uri).
                    then().log().all().
                    statusCode(HttpStatus.SC_OK).
                    body("userId", is(getId)).
                    body("productsInCart", is(empty()))
            ;
        }

        void logoutOrder() {
            System.out.println("Logout");
            String str_userId = String.valueOf(getId);

            Map<String, String> logoutData = new HashMap<>();
            logoutData.put("accountId", str_userId);
            logoutData.put("token", token);

            given().
                    body(logoutData).log().all().
                    when().
                    post(LOGOUT_USER_ENDPOINT).
                    then().log().all().
                    statusCode(HttpStatus.SC_OK).
                    body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/logout-succeeded.json")).
                    body("response.success", is(true),
                            "response.userId", is(getId),
                            "response.reason", is("Logout Successful"))
            ;
        }
    }
}
