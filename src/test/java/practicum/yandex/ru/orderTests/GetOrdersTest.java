package practicum.yandex.ru.orderTests;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import practicum.yandex.ru.BaseTest;
import practicum.yandex.ru.dataObjects.OrderList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static practicum.yandex.ru.steps.OrderSteps.sendGetOrdersRequest;

public class GetOrdersTest extends BaseTest {

    @Test
    @DisplayName("Get list of orders")
    @Description("Basic test for GET /api/v1/orders")
    void getOrdersAndCheckStatus() {

        Response response = sendGetOrdersRequest(null, null, null, null);

        OrderList orderList = response.body().as(OrderList.class);

        assertFalse(orderList.getOrders().isEmpty());
        response.then().statusCode(200);
    }
}
