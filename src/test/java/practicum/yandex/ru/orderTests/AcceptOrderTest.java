package practicum.yandex.ru.orderTests;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import practicum.yandex.ru.BaseTest;
import practicum.yandex.ru.dataObjects.CourierId;
import practicum.yandex.ru.dataObjects.SingleOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static practicum.yandex.ru.steps.CourierSteps.getCreatedCourierId;
import static practicum.yandex.ru.steps.OrderSteps.*;

public class AcceptOrderTest extends BaseTest {

    @Test
    @DisplayName("Accept order")
    @Description("Basic test for GET /api/v1/orders/accept/:id")
    void acceptOrderAndCheckStatus() {

        SingleOrder singleOrder = getCreatedOrderInfo();
        CourierId courierId = getCreatedCourierId();

        Response response = sendAcceptOrderRequest(singleOrder.getOrder().getId(), courierId.getId());

        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Accept order with wrong courier id")
    @Description("Test for GET /api/v1/orders/accept/:id with Wrong Courier id - 404 error")
    void acceptOrderWithWrongCourierIdAndCheckStatus() {
        SingleOrder singleOrder = getCreatedOrderInfo();

        Response response = sendAcceptOrderRequest(singleOrder.getOrder().getId(), 1234545432);

        response.then()
                .assertThat().body("message", equalTo("Курьера с таким id не существует"))
                .and()
                .statusCode(404);
    }

    @Test
    @DisplayName("Accept order without courier id")
    @Description("Test for GET /api/v1/orders/accept/:id without courier id - 400 error")
    void acceptOrderWithoutCourierIdAndCheckStatus() {

        SingleOrder singleOrder = getCreatedOrderInfo();

        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .put(ORDER_API + "/accept/" + singleOrder.getOrder().getId());

        response.then()
                .assertThat().body("message", equalTo("Недостаточно данных для поиска"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Accept order with wrong order id")
    @Description("Test for GET /api/v1/orders/accept/:id with nonexistent order id - 404 error")
    void acceptOrderWithWrongOrderIdAndCheckStatus() {

        CourierId courierId = getCreatedCourierId();

        Response response = sendAcceptOrderRequest(1234545432, courierId.getId());

        response.then()
                .assertThat().body("message", equalTo("Заказа с таким id не существует"))
                .and()
                .statusCode(404);
    }

    @Test
    @DisplayName("Accept order without order id")
    @Description("Test for GET /api/v1/orders/accept/:id without order id - 400 error")
    void acceptOrderWithoutOrderIdAndCheckStatus() {

        CourierId courierId = getCreatedCourierId();

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .params("courierId", courierId.getId())
                .when()
                .put(ORDER_API + "/accept/");

        response.then()
                .assertThat().body("message", equalTo("Недостаточно данных для поиска"))
                .and()
                .statusCode(400);
    }

}
