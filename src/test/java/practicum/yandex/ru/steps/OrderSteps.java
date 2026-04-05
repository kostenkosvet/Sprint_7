package practicum.yandex.ru.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import practicum.yandex.ru.dataObjects.Order;
import practicum.yandex.ru.dataObjects.OrderId;
import practicum.yandex.ru.dataObjects.SingleOrder;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    public static final String ORDER_API = "/api/v1/orders";

    @Step("Send Create Order request")
    public static Response sendCreateOrderRequest(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(ORDER_API);
    }

    @Step("Send Get list of orders request")
    public static Response sendGetOrdersRequest(Integer courierId, String nearestStation,
                                                Integer limit, Integer page) {

        Map<String, Object> params = new HashMap<>();
        if (courierId != null) {
            params.put("courierId", courierId);
        }
        if (nearestStation != null) {
            params.put("nearestStation", nearestStation);
        }
        if (limit != null) {
            params.put("limit", limit);
        }
        if (page != null) {
            params.put("page", page);
        }

        return given()
                .header("Content-type", "application/json")
                .and()
                .params(params)
                .when()
                .get(ORDER_API);
    }

    @Step("Send Get Order by id request")
    public static Response sendGetOrderByIdRequest(Integer id) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .params("t", id)
                .when()
                .get(ORDER_API + "/track");
    }

    @Step("Send Accept Order Courier request")
    public static Response sendAcceptOrderRequest(Integer id, Integer courierId) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .params("courierId", courierId)
                .when()
                .put(ORDER_API + "/accept/" + id);
    }

    @Step("Get Order info after creation")
    public static SingleOrder getCreatedOrderInfo() {
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
        return response.getBody().as(SingleOrder.class);
    }

    @Step("Cancel order request")
    public static void sendCancelOrder(Integer orderTrack) {
        given()
                .header("Content-type", "application/json")
                .and()
                .params("track", orderTrack)
                .when()
                .put(ORDER_API + "/cancel/");
    }

    @Step("Finish order request")
    public static void sendFinishOrder(Integer id) {
        given()
                .header("Content-type", "application/json")
                .and()
                .params("id", id)
                .when()
                .put(ORDER_API + "/finish/");
    }

}
