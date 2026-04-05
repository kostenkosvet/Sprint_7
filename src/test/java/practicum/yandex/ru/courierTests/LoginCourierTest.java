package practicum.yandex.ru.courierTests;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import practicum.yandex.ru.BaseTest;
import practicum.yandex.ru.dataObjects.Courier;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;
import static practicum.yandex.ru.steps.CourierSteps.sendLoginRequest;

public class LoginCourierTest extends BaseTest {

    public static Object[][] incompleteData() {
        return new Object[][]{
                {null, "123"},
                {"Practicum", null}
        };
    }

    @ParameterizedTest
    @MethodSource("incompleteData")
    @DisplayName("Login courier without required fields")
    @Description("Login courier without required fields - 400 error")
    void loginWithIncompleteCourierDataAndCheckStatus(String login, String password) {
        super.courier = new Courier(login, password, null);

        Response response = sendLoginRequest(courier);
        response.then()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Login courier")
    @Description("Basic test for Login courier")
    void loginCourierAndCheckStatus() {
        super.courier = new Courier("Practicum", "123", null);

        Response response = sendLoginRequest(super.courier);
        response.then()
                .assertThat().body("id", isA(Integer.class))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Login courier with invalid data")
    @Description("Login courier with invalid data - 404 error")
    void loginWithNonexistentCourierAndCheckStatus() {
        super.courier = new Courier("Practicum55", "123", null);

        Response response = sendLoginRequest(super.courier);
        response.then()
                .assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }


}
