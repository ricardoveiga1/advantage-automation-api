package advantage.api.tests;

import advantage.api.support.domain.User;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class BaseTest {

    @BeforeAll
    public static void setup() {

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        baseURI = "http://www.advantageonlineshopping.com/accountservice";
        basePath = "accountrest/api/v1";


        RestAssured.requestSpecification = new RequestSpecBuilder().
                setContentType(ContentType.JSON).
                setAccept("*/*").
                build();

        RestAssured.responseSpecification = new ResponseSpecBuilder().
                expectContentType(ContentType.JSON).
                build();

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

    }
}
