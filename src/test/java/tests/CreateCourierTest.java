package tests;

import clients.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.pojo.CourierLogin;
import org.example.pojo.CourierRegister;
import org.junit.After;
import org.junit.Test;
import com.github.javafaker.Faker;

public class CreateCourierTest {

    String courierId;
    Faker faker = new Faker();

    CourierRegister courier = new CourierRegister(faker.name().name(), faker.random().toString(), faker.name().firstName());

    @After
    public void tearDown() {
        CourierClient.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Создание нового курьера, валидные данные")
    public void createNewCourierWithValidData() {
        Response createResponse = CourierClient.createNewCourier(this.courier);
        CourierClient.compareActualResponseCodeWithSuccessfulOne(createResponse, "ok", 201);

        CourierLogin courierLogin = new CourierLogin(this.courier.getLogin(), this.courier.getPassword());
        Response logInResponse = CourierClient.loginCourier(courierLogin);

        courierId = CourierClient.getCourierId(logInResponse);
    }

    @Test
    @DisplayName("Создание курьеров-дубликатов")
    public void createTwoDuplicateCouriers() {

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
    public void createCourierWithEmptyLogin() {
        CourierRegister courier = new CourierRegister("", RandomStringUtils.randomAlphanumeric(5), RandomStringUtils.randomAlphanumeric(5));
        Response response = CourierClient.createNewCourier(courier);
        CourierClient.compareErroneousResponseCodeWithActualOne(response, 400, "Недостаточно данных для создания учетной записи");

        CourierLogin courierLogin = new CourierLogin(this.courier.getLogin(), this.courier.getPassword());
        Response logInResponse = CourierClient.loginCourier(courierLogin);
        courierId = CourierClient.getCourierId(logInResponse);
    }

    @Test
    @DisplayName("Создание курьера, пароль пустой")
    public void createCourierWithEmptyPassword() {
        CourierRegister courier = new CourierRegister(faker.name().name(), "", faker.name().firstName());

        Response response = CourierClient.createNewCourier(courier);
        CourierClient.compareErroneousResponseCodeWithActualOne(response, 400, "Недостаточно данных для создания учетной записи");

        CourierLogin courierLogin = new CourierLogin(this.courier.getLogin(), this.courier.getPassword());
        Response logInResponse = CourierClient.loginCourier(courierLogin);
        courierId = CourierClient.getCourierId(logInResponse);
    }

    @Test
    @DisplayName("Создание курьера, имя пустое")
    public void createCourierWithEmptyName() {
        String login = faker.name().name();
        String password = faker.name().name();

        CourierRegister courier = new CourierRegister(login, password, null);

        Response response = CourierClient.createNewCourier(courier);
        CourierClient.compareActualResponseCodeWithSuccessfulOne(response, "ok", 201);

        CourierLogin courierLogin = new CourierLogin(login, password);
        Response logInResponse = CourierClient.loginCourier(courierLogin);

        courierId = CourierClient.getCourierId(logInResponse);

    }

    @Test
    @DisplayName("Создание курьера, все поля не заполнены")
    public void createCourierWithEmptyFields() {
        CourierRegister courier = new CourierRegister("", "", "");
        Response response = CourierClient.createNewCourier(courier);
        CourierClient.compareErroneousResponseCodeWithActualOne(response, 400, "Недостаточно данных для создания учетной записи");

        CourierLogin courierLogin = new CourierLogin(this.courier.getLogin(), this.courier.getPassword());
        Response logInResponse = CourierClient.loginCourier(courierLogin);
        courierId = CourierClient.getCourierId(logInResponse);
    }
}
