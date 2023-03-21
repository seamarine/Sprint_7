package tests;


import clients.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.pojo.OrderCreate;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] colour;

    OrderClient orderClient;
    String orderTrack;


    public CreateOrderTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] colour) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.colour = colour;

    }

    @Parameterized.Parameters(name = "Заказ самокатов разных цветов. Тестовые данные: {0}{1}{2}{3}{4}")
    public static Object[][] getOrderData() {
        return new Object[][]{
                {"Jennifer", "Lawrence", "New York, Bronx", "Bronx Park East", "800 434 0050", 30, "2023-04-04", "I want to ride my bicycle", new String[]{}},
                {"Jennifer", "Lawrence", "New York, Bronx", "Bronx Park East", "800 434 0050", 30, "2023-04-04", "I want to ride my bicycle", new String[]{"RED"}},
                {"Jennifer", "Lawrence", "New York, Bronx", "Bronx Park East", "800 434 0050", 30, "2023-04-04", "I want to ride my bicycle", new String[]{"GRAY", "RED"}},
                {"Jennifer", "Lawrence", "New York, Bronx", "Bronx Park East", "800 434 0050", 30, "2023-04-04", "I want to ride my bicycle", new String[]{"BLACK"}},
                {"Jennifer", "Lawrence", "New York, Bronx", "Bronx Park East", "800 434 0050", 30, "2023-04-04", "I want to ride my bicycle", new String[]{"GRAY"}},
        };
    }

    @After
    public void tearDown() {
        OrderClient.deleteOrder(orderTrack);
    }

    @Test
    @DisplayName("Создание заказов, параметризация цветов")
    public void createOrderParameterizedColorScooterTest() {
        OrderCreate orderCreate = new OrderCreate(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, colour);
        Response createResponse = OrderClient.createNewOrder(orderCreate);
        OrderClient.comparingSuccessfulOrderSet(createResponse, 201);
        orderTrack = OrderClient.getOrderTrack(createResponse);

    }
}
