package clients;

import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.pojo.CourierLogin;
import org.example.pojo.CourierRegister;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class CourierClient {

    private static final String COURIER_REGISTER = "/api/v1/courier";
    private static final String COURIER_LOGIN = "/api/v1/courier/login";
    private static final String courierDelete = "/api/v1/courier/";


    @Step("Создание нового курьера {courier}")
    public static Response createNewCourier(CourierRegister courier) {
        return given()
                .spec(Specification.requestSpec())
                .body(courier)
                .when()
                .post(COURIER_REGISTER);
    }

    @Step("Логин курьера")
    public static Response loginCourier(CourierLogin courier) {
        return given()
                .spec(Specification.requestSpec())
                .body(courier)
                .post(COURIER_LOGIN);
    }

    @Step("Получение ID курьера")
    public static String getCourierId(Response response) {
        String courierId = response.then().extract().body().asString();
        JsonPath jsonPath = new JsonPath(courierId);
        return jsonPath.getString("id");
    }

    @Step("Удаление курьера")
    public static void deleteCourier(String id) {
        given()
                .spec(Specification.requestSpec())
                .delete(courierDelete + id);
    }

    @Step("Сравнение фактического кода ответа с успешным")
    public static void compareActualResponseCodeWithSuccessfulOne(Response response, String responseString, int responseStatusCode) {
        response.then().assertThat().body(responseString, equalTo(true)).and().statusCode(responseStatusCode);
    }

    @Step("Сравнение фактического кода логина юзера с успешным")
    public static void compareSuccessfulLoginResponseCodeWithActual(Response response, int responseStatusCode) {
        response.then().assertThat().body("id", not(0)).and().statusCode(responseStatusCode);
    }

    @Step("Сравнение ошибочного кода ответа с фактическим")
    public static void compareErroneousResponseCodeWithActualOne(Response response, int responseStatusCode, String message) {
        response.then().assertThat().body("message", equalTo(message)).and().statusCode(responseStatusCode);
    }

}
