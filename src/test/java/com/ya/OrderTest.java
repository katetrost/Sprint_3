package com.ya;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(Parameterized.class)
public class OrderTest {
    private final String color;
    private int orderTrack;

    Order order = new Order();

    @After
    public void tearDown() { // почистить после теста
        order.deleteOrder( orderTrack);
    }

    public OrderTest(String color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] OrderCreateTestParamApiTestData() {
        return new Object[][]{
                {"BLACK"},
                {""},
                {"GREY"},
                {"GREY, BLACK"},
        };
    }

    @Test
    @DisplayName("Проверка, что можно создаёшь заказ с разными вариантами: BLACK или GREY, оба цвета, не указывать цвет")
    @Description("Тест  POST /api/v1/orders")
    public void checkCreateOrderWithBlack() throws JsonProcessingException {
        Response response = order.createOrder(new String[] {color});
        orderTrack = response.then().extract().body().path("track");

        response.then().assertThat().statusCode(201);
        assertThat("Order track incorrect",orderTrack, is(not(0)));
    }
}
