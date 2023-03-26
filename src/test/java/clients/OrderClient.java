package clients;


import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.pojo.OrderCreate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class OrderClient {

    static String createOrders = "/api/v1/orders";
    static String cancelOrder = "/api/v1/orders/cancel";

    @Step("Создание нового заказа")
    public static Response createNewOrder(OrderCreate orderCreate) {
        return given()
                .spec(Specification.requestSpec())
                .body(orderCreate)
                .post(createOrders);
    }

    @Step("Получение списка заказов")
    public static Response getAllOrders() {
        return given()
                .spec(Specification.requestSpec())
                .body("")
                .get(createOrders);
    }

    @Step("Получение трек-номера заказа")
    public static String getOrderTrack(Response response) {
        String trackNumber = response.then().extract().body().asString();
        JsonPath jsonPath = new JsonPath(trackNumber);
        return jsonPath.getString("track");
    }

    @Step("Удаление заказа по трек-номеру")
    public static Response deleteOrder(String track) {
        return given()
                .spec(Specification.requestSpec())
                .put(cancelOrder + "?track=" + track);
    }

    @Step("Сравнение успешного кода ответа с фактическим")
    public static void compareSuccessfulOrderSet(Response response, int responseCode) {
        response.then().assertThat().body("track", not(0)).and().statusCode(responseCode);
    }

    @Step("Сравнение успешного кода ответа об отмене с фактическим")
    public static void compareSuccessfulOrderCancel(Response response, int responseCode) {
        response.then().assertThat().body("ok", equalTo(true)).and().statusCode(responseCode);
    }

}
