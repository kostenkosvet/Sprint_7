package practicum.yandex.ru.orderTests;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import practicum.yandex.ru.BaseTest;
import practicum.yandex.ru.dataObjects.Order;
import practicum.yandex.ru.dataObjects.OrderId;
import practicum.yandex.ru.dataObjects.SingleOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static practicum.yandex.ru.steps.OrderSteps.*;

public class GetOrderByIdTest extends BaseTest {

    @Test
    @DisplayName("Get order by id")
    @Description("Basic test for GET /api/v1/orders/track")
    void getOrderByIdAndCheckStatus() {

        String firstName = "Svetlana";
        String lastName = "Baeva";
        String address = "Konoha, 142 apt.";
        String metroStation = "3";
        String phone = "+7 800 355 35 35";
        int rentTime = 5;
        String deliveryDate = "2026-06-06";
        String comment = "Что-то осмысленное";

        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment);

        Response response = sendCreateOrderRequest(order);
        OrderId orderId = response.getBody().as(OrderId.class);

        response = sendGetOrderByIdRequest(orderId.getTrack());
        SingleOrder singleOrder = response.getBody().as(SingleOrder.class);

        assertThat(singleOrder.getOrder().getId(), is(instanceOf(Integer.class)));
        assertEquals(firstName, singleOrder.getOrder().getFirstName());
        assertEquals(lastName, singleOrder.getOrder().getLastName());
        assertEquals(address, singleOrder.getOrder().getAddress());
        assertEquals(metroStation, singleOrder.getOrder().getMetroStation());
        assertEquals(phone, singleOrder.getOrder().getPhone());
        assertEquals(rentTime, singleOrder.getOrder().getRentTime());
        assertTrue(singleOrder.getOrder().getDeliveryDate().contains(deliveryDate));
        assertEquals(comment, singleOrder.getOrder().getComment());
        assertEquals(orderId.getTrack(), singleOrder.getOrder().getTrack());
        assertNull(singleOrder.getOrder().getColor());
        assertThat(singleOrder.getOrder().isCancelled(), is(instanceOf(Boolean.class)));
        assertThat(singleOrder.getOrder().isFinished(), is(instanceOf(Boolean.class)));
        assertThat(singleOrder.getOrder().isInDelivery(), is(instanceOf(Boolean.class)));
        assertNotNull(singleOrder.getOrder().getCreatedAt());
        assertNotNull(singleOrder.getOrder().getUpdatedAt());
        assertThat(singleOrder.getOrder().getStatus(), is(instanceOf(Integer.class)));

        response.then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Get order by id without id")
    @Description("Test for GET /api/v1/orders/track without order id")
    void getOrderByIdWithoutIdAndCheckStatus() {

        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .get(ORDER_API + "/track");
        response.then()
                .assertThat().body("message", equalTo("Недостаточно данных для поиска"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Get order by id with wrong id")
    @Description("Test for GET /api/v1/orders/track with nonexistent id")
    void getOrderByIdWithWrongIdAndCheckStatus() {

        Response response = sendGetOrderByIdRequest(999999);
        response.then()
                .assertThat().body("message", equalTo("Заказ не найден"))
                .and()
                .statusCode(404);
    }
}
