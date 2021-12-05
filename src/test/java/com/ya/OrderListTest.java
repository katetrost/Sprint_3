package com.ya;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OrderListTest {

    private final Order order = new Order();

    @Test
    @DisplayName("Проверь, что в тело ответа возвращается список и он не пустой.")
    @Description("Тест  GET /api/v1/orders")
    public void checkNonEmptyListIsReturned() {
        ValidatableResponse response =  order.getOrderList();
        List<Object> orderList = response.extract().jsonPath().getList("orders");

        response.assertThat().statusCode(200);
        assertThat(orderList.isEmpty(), is(false));
    }

    @Test
    @DisplayName("Проверь, что в теле ответа номер track не пустой.")
    @Description("Тест  GET /api/v1/orders")
    public void checkTrackIsNotEmpty() {
        ValidatableResponse response =  order.getOrderList();

        response.assertThat().body("orders.track", notNullValue());
    }

}
