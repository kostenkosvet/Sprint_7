package practicum.yandex.ru.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import practicum.yandex.ru.dataObjects.Courier;
import practicum.yandex.ru.dataObjects.CourierId;

import static io.restassured.RestAssured.given;

public class CourierSteps {

    private static final String COURIER_API = "/api/v1/courier";

    @Step("Send Create Courier request")
    public static Response sendCreateCourierRequest(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(COURIER_API);
    }

    @Step("Send Login Courier request")
    public static Response sendLoginRequest(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(COURIER_API + "/login");
    }

    @Step("Send Delete Courier request")
    public static Response sendDeleteRequest(Integer id) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .delete(COURIER_API + "/" + id);
    }

    @Step("Get created Courier id")
    public static CourierId getCreatedCourierId() {
        Courier courier = new Courier("Practicum", "123", "Svetlana");

        sendCreateCourierRequest(courier);
        Response response = sendLoginRequest(courier);
        return response.getBody().as(CourierId.class);
    }

    @Step("Get existing Courier id")
    public static CourierId getCourierId(Courier courier) {
        Response response = sendLoginRequest(courier);
        return response.getBody().as(CourierId.class);
    }
}
