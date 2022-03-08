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

    public Response loginUser(Login login) {

        return given().
                body(login).
                when().log().all().
                post(LOGIN_USER_ENDPOINT)
       ;
    }


}
