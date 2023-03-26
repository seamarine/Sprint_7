package tests;

import clients.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.pojo.CourierLogin;
import org.example.pojo.CourierRegister;
import org.junit.After;
import org.junit.Test;

public class CreateCourierTest {

    CourierClient courierClient;
    String courierId;

    CourierRegister courier = new CourierRegister("MichaelJ", "EarthSong", "Michael");

    @After
    public void tearDown() {
        CourierClient.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Создание нового курьера, валидные данные")
    public void creatingNewCourierWithValidData() {
        Response createResponse = CourierClient.createNewCourier(this.courier);
        CourierClient.compareActualResponseCodeWithSuccessfulOne(createResponse, "ok", 201);

        CourierLogin courierLogin = new CourierLogin(this.courier.getLogin(), this.courier.getPassword());
        Response logInResponse = CourierClient.loginCourier(courierLogin);

        courierId = CourierClient.getCourierId(logInResponse);
    }

    @Test
    @DisplayName("Создание курьеров-дубликатов")
    public void creatingTwoDuplicateCouriers() {
        Response response = CourierClient.createNewCourier(this.courier);
        CourierClient.compareActualResponseCodeWithSuccessfulOne(response, "ok", 201);

        Response secondResponse = CourierClient.createNewCourier(this.courier);
        CourierClient.compareErroneousResponseCodeWithActualOne(secondResponse, 409, "Этот логин уже используется. Попробуйте другой.");

        CourierLogin courierLogin = new CourierLogin(this.courier.getLogin(), this.courier.getPassword());
        Response logInResponse = CourierClient.loginCourier(courierLogin);

        courierId = CourierClient.getCourierId(logInResponse);

    }

    @Test
    @DisplayName("Создание курьера, логин пустой")
    public void creationCourierWithEmptyLogin() {
        CourierRegister courier = new CourierRegister("", RandomStringUtils.randomAlphanumeric(5), RandomStringUtils.randomAlphanumeric(5));
        Response response = CourierClient.createNewCourier(courier);
        CourierClient.compareErroneousResponseCodeWithActualOne(response, 400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Создание курьера, пароль пустой")
    public void creationCourierWithEmptyPassword() {
        CourierRegister courier = new CourierRegister(RandomStringUtils.randomAlphanumeric(5), "", RandomStringUtils.randomAlphanumeric(5));

        Response response = CourierClient.createNewCourier(courier);
        CourierClient.compareErroneousResponseCodeWithActualOne(response, 400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Создание курьера, имя пустое")
    public void creationCourierWithEmptyName() {
        String login = RandomStringUtils.randomAlphanumeric(5);
        String password = RandomStringUtils.randomAlphanumeric(5);

        CourierRegister courier = new CourierRegister(login, password, null);

        Response response = CourierClient.createNewCourier(courier);
        CourierClient.compareActualResponseCodeWithSuccessfulOne(response, "ok", 201);

        CourierLogin courierLogin = new CourierLogin(login, password);
        Response logInResponse = CourierClient.loginCourier(courierLogin);

        courierId = CourierClient.getCourierId(logInResponse);

    }

    @Test
    @DisplayName("Создание курьера, все поля не заполнены")
    public void creationCourierWithEmptyFields() {
        CourierRegister courier = new CourierRegister("", "", "");
        Response response = CourierClient.createNewCourier(courier);
        CourierClient.compareErroneousResponseCodeWithActualOne(response, 400, "Недостаточно данных для создания учетной записи");
    }
}
