package com.ya;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class CourierTest {

    private CourierClient courierClient;
    public int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() { // почистить после теста
        courierClient.delete(courierId);
    }

    @Test
    @DisplayName("Проверка,что курьера можно создать.")
    @Description("Тест ручки /api/v1/courier")
    public void checkCourierCanBeCreated(){
        // Arrange - Подготовка данных
        Courier courier = Courier.getRandom();

        // Act - Сделать какое-то действие
        ValidatableResponse validatableResponse = courierClient.create(courier);
        boolean isCourierCreated = validatableResponse.extract().path("ok");
        courierId = courierClient.login(CourierCredentials.form(courier)).extract().path("id");

        // Assert - Проверка
        validatableResponse.assertThat().statusCode(201);
        assertTrue("Courier is not created", isCourierCreated); // проверка создания курьера true/false
        assertThat("Courier ID incorrect",courierId, is(not(0))); // проверка id курьера, что он адекватный
    }

    @Test
    @DisplayName("Проверьте, что нельзя создать двух одинаковых курьеров.")
    @Description("Тест ручки /api/v1/courier")
    public void checkCourierCannotTwoIdenticalCreated(){
        // Arrange
        Courier courier = Courier.getRandom();

        // Act
        courierClient.create(courier);
        courierId = courierClient.login(CourierCredentials.form(courier)).extract().path("id");
        ValidatableResponse validatableResponse = courierClient.create(courier);

        // Assert
        validatableResponse.assertThat().statusCode(409);
        validatableResponse.assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Проверка регистрации курьера без логина.")
    @Description("Тест ручки /api/v1/courier")
    public void checkCreatCourierWithoutLogin() {
        Courier courier = Courier.getRandom(false,true, true);

        courierClient.create(courier);
        ValidatableResponse validatableResponse = courierClient.create(courier);

        validatableResponse.assertThat().statusCode(400);
        validatableResponse.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Проверка регистрации курьера без пароля.")
    @Description("Тест ручки /api/v1/courier")
    public void checkCreatCourierWithoutPassword() {
        Courier courier = Courier.getRandom(true,false, true);

        courierClient.create(courier);
        ValidatableResponse validatableResponse = courierClient.create(courier);

        validatableResponse.assertThat().statusCode(400);
        validatableResponse.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
