package practicum.yandex.ru.courierTests;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import practicum.yandex.ru.BaseTest;
import practicum.yandex.ru.dataObjects.CourierId;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static practicum.yandex.ru.steps.CourierSteps.getCreatedCourierId;
import static practicum.yandex.ru.steps.CourierSteps.sendDeleteRequest;

public class DeleteCourierTest extends BaseTest {

    @Test
    @DisplayName("Delete courier")
    @Description("Basic test for Delete courier")
    void deleteCourierAndCheckStatus() {

        CourierId courierId = getCreatedCourierId();

        Response response = sendDeleteRequest(courierId.getId());
        response.then()
                .assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Delete nonexistent courier")
    @Description("Delete nonexistent courier - 404 error")
    void deleteNonexistentCourierAndCheckStatus() {
        Response response = sendDeleteRequest(123456);
        response.then()
                .assertThat().body("message", equalTo("Курьера с таким id нет"))
                .and()
                .statusCode(404);
    }

    @Test
    @DisplayName("Delete courier without id")
    @Description("Delete courier without id - 400 error")
    void deleteCourierWithoutIdAndCheckStatus() {

        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/");
        response.then()
                .assertThat().body("message", equalTo("Недостаточно данных для удаления курьера"))
                .and()
                .statusCode(400);
    }
}
