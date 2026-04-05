package practicum.yandex.ru.orderTests;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import practicum.yandex.ru.BaseTest;
import practicum.yandex.ru.dataObjects.Order;
import practicum.yandex.ru.dataObjects.OrderId;

import static org.hamcrest.Matchers.isA;
import static practicum.yandex.ru.steps.OrderSteps.sendCreateOrderRequest;

public class CreateOrderTest extends BaseTest {


    public static Object[][] orderDetails() {
        return new Object[][]{
                {"Svetlana", "Baeva", "Konoha, 142 apt.", "3", "+7 800 355 35 35", 5, "2026-06-06", "Что-то осмысленное", null},
                {"Ivan", "Moody", "Konoha 1", "4", "+7 800 355 35 77", 1, "2026-06-10", "Like it black", "BLACK"},
                {"Brad", "Pit", "Lenina 55", "9", "+7 800 355 35 77", 9, "2026-07-07", "Like it grey", "GREY"},
                {"Nicolas", "Cage", "Nekrasova 1, kv.56", "2", "+7 800 355 35 77", 3, "2026-09-01", "Colour doesn't matter", "BLACK and GREY"}
        };
    }

    @ParameterizedTest
    @MethodSource("orderDetails")
    @DisplayName("Create valid order and check status")
    @Description("Create user without required fields - 400 error")
    void createOrderAndCheckStatus(String firstName, String lastName, String address, String metroStation, String phone,
                                   int rentTime, String deliveryDate, String comment, String colour) {
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment);

        if (colour != null) {
            switch (colour) {
                case "BLACK":
                    order.setColor(new String[]{"BLACK"});
                    break;
                case "GREY":
                    order.setColor(new String[]{"GREY"});
                    break;
                case "BLACK and GREY":
                    order.setColor(new String[]{"BLACK", "GREY"});
                    break;
            }
        }

        Response response = sendCreateOrderRequest(order);
        super.orderId = response.getBody().as(OrderId.class);
        response.then()
                .assertThat().body("track", isA(Integer.class))
                .and()
                .statusCode(201);
    }
}
