package practicum.yandex.ru;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import practicum.yandex.ru.dataObjects.*;

import static practicum.yandex.ru.steps.CourierSteps.getCourierId;
import static practicum.yandex.ru.steps.CourierSteps.sendDeleteRequest;
import static practicum.yandex.ru.steps.OrderSteps.sendCancelOrder;
import static practicum.yandex.ru.steps.OrderSteps.sendFinishOrder;

public class BaseTest {
    private final String BASE_URL = "https://qa-scooter.praktikum-services.ru/";

    public Courier courier;
    public CourierId courierId;
    public OrderId orderId;
    public SingleOrder singleOrder;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @AfterEach
    public void teardown() {
        if (courier != null) {
            CourierId courierId = getCourierId(courier);
            sendDeleteRequest(courierId.getId());
        }
        if (courierId != null) {
            sendDeleteRequest(courierId.getId());
        }
        if (orderId != null) {
            sendCancelOrder(orderId.getTrack());
        }
        if (singleOrder != null) {
            sendFinishOrder(singleOrder.getOrder().getId());
        }


    }
}
