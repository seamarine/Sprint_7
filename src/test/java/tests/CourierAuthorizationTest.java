package tests;

import clients.CourierClient;
import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.pojo.CourierLogin;
import org.example.pojo.CourierRegister;
import org.junit.After;
import org.junit.Test;

import static constants.ResponseStatusCode.*;


public class CourierAuthorizationTest {

    Faker faker = new Faker();

    String courierId;

    CourierRegister courier = new CourierRegister(faker.name().name(), faker.random().toString(), faker.name().firstName());

    @After
    public void tearDown() {
        CourierClient.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Логин, валидные данные")
    public void loginWithExistingCourier() {
        CourierClient.createNewCourier(courier);

        CourierLogin courierLogin = new CourierLogin(this.courier.getLogin(), this.courier.getPassword());
        Response logInResponse = CourierClient.loginCourier(courierLogin);

        CourierClient.compareSuccessfulLoginResponseCodeWithActual(logInResponse, RESPONSE_STATUS_CODE_200);

        courierId = CourierClient.getCourierId(logInResponse);

    }

    @Test
    @DisplayName("Логин, поле логин пустое")
    public void loginWithEmptyLogin() {

        CourierClient.createNewCourier(courier);

        CourierLogin courierLoginCorrect = new CourierLogin(this.courier.getLogin(), this.courier.getPassword());
        Response logInResponseForGetId = CourierClient.loginCourier(courierLoginCorrect);

        courierId = CourierClient.getCourierId(logInResponseForGetId);

        CourierLogin courierLogin = new CourierLogin("", this.courier.getPassword());
        Response logInResponse = CourierClient.loginCourier(courierLogin);

        CourierClient.compareErroneousResponseCodeWithActualOne(logInResponse, RESPONSE_STATUS_CODE_400, "Недостаточно данных для входа");


    }

    @Test
    @DisplayName("Логин, логин не введен")
    public void loginWithoutLogin() {

        CourierClient.createNewCourier(courier);

        CourierLogin courierLoginCorrect = new CourierLogin(this.courier.getLogin(), this.courier.getPassword());
        Response logInResponseForGetId = CourierClient.loginCourier(courierLoginCorrect);

        courierId = CourierClient.getCourierId(logInResponseForGetId);

        CourierLogin courierLogin = new CourierLogin(null, this.courier.getPassword());
        Response logInResponse = CourierClient.loginCourier(courierLogin);

        CourierClient.compareErroneousResponseCodeWithActualOne(logInResponse, RESPONSE_STATUS_CODE_400, "Недостаточно данных для входа");

    }

    @Test
    @DisplayName("Логин, пароль не введен")
    public void loginWithEmptyPassword() {

        CourierClient.createNewCourier(courier);

        CourierLogin courierLoginCorrect = new CourierLogin(this.courier.getLogin(), this.courier.getPassword());
        Response logInResponseForGetId = CourierClient.loginCourier(courierLoginCorrect);

        courierId = CourierClient.getCourierId(logInResponseForGetId);

        CourierLogin courierLogin = new CourierLogin(this.courier.getLogin(), "");
        Response logInResponse = CourierClient.loginCourier(courierLogin);

        CourierClient.compareErroneousResponseCodeWithActualOne(logInResponse, RESPONSE_STATUS_CODE_400, "Недостаточно данных для входа");

    }

    @Test
    @DisplayName("Логин, невалидный пароль")
    public void loginWithIncorrectLogin() {

        CourierClient.createNewCourier(courier);

        CourierLogin courierLoginCorrect = new CourierLogin(this.courier.getLogin(), this.courier.getPassword());
        Response logInResponseForGetId = CourierClient.loginCourier(courierLoginCorrect);

        courierId = CourierClient.getCourierId(logInResponseForGetId);

        CourierLogin courierLogin = new CourierLogin("incorrect_login", this.courier.getPassword());
        Response logInResponse = CourierClient.loginCourier(courierLogin);

        CourierClient.compareErroneousResponseCodeWithActualOne(logInResponse, RESPONSE_STATUS_CODE_404, "Учетная запись не найдена");



    }

}
