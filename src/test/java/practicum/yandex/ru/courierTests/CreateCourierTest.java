package practicum.yandex.ru.courierTests;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import practicum.yandex.ru.BaseTest;
import practicum.yandex.ru.dataObjects.Courier;

import static org.hamcrest.Matchers.equalTo;
import static practicum.yandex.ru.steps.CourierSteps.sendCreateCourierRequest;

public class CreateCourierTest extends BaseTest {

    public static Object[][] incompleteData() {
        return new Object[][]{
                {null, "123", "Svetlana"},
                {"Practicum", null, "Svetlana"},
                {"Practicum", "123", null}
        };
    }

    public static Object[][] duplicateUser() {
        return new Object[][]{
                {"Practicum1", "123", "Svetlana", "Practicum1", "123", "Svetlana"},
                {"Practicum2", "123", "Svetlana", "Practicum2", "546", "Pit"},
        };
    }

    @ParameterizedTest
    @MethodSource("incompleteData")
    @DisplayName("Create user without required fields")
        //   @Description("Create user without required fields - 400 error")
    void createIncompleteCourierAndCheckStatus(String login, String password, String firstname) {
        super.courier = new Courier(login, password, firstname);

        Response response = sendCreateCourierRequest(super.courier);
        response.then()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @ParameterizedTest
    @MethodSource("duplicateUser")
    void createDuplicateCourierAndCheckStatus(String login1, String password1, String firstname1,
                                              String login2, String password2, String firstname2) {
        super.courier = new Courier(login1, password1, firstname1);

        sendCreateCourierRequest(super.courier);

        super.courier = new Courier(login2, password2, firstname2);

        Response response = sendCreateCourierRequest(super.courier);

        response.then()
                .assertThat().body("message", equalTo("Этот логин уже используется"))
                .and()
                .statusCode(409);
    }

    @Test
    void createCourierAndCheckStatus() {
        super.courier = new Courier("Practicum", "123", "Svetlana");

        Response response = sendCreateCourierRequest(super.courier);

        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
    }
}
