package tests;

import steps.OrderApiSteps;
import steps.ResponseChecksSteps;
import steps.UserApiSteps;
import dto.Ingredient;
import dto.IngredientsResponse;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.fail;
@DisplayName("Создание заказа")
public class CreateOrderTests {
    private String token;
    private List<Ingredient> ingredients = new ArrayList<>();
    private final OrderApiSteps orderApi = new OrderApiSteps();
    private final UserApiSteps userApi = new UserApiSteps();
    private final ResponseChecksSteps checks = new ResponseChecksSteps();

    @Before
    @Step("Подготовка тестовых данных")
    public void setUp() {
        String email = "e-mail_" + UUID.randomUUID() + "@mail.com";
        String password = "pass_" + UUID.randomUUID();
        String name = "name";

        // Создание пользователя
        Response response = userApi.createUser(email, password, name);
        checks.checkStatusCode(response, 200);

        // Получение токена
        if (response.getStatusCode() == 200) {
            token = userApi.getToken(response);
        }

        // Получение списка ингредиентов
        response = orderApi.getIngredientList();
        checks.checkStatusCode(response, 200);

        ingredients = response.body().as(IngredientsResponse.class).getData();

        if(token == null || ingredients.isEmpty())
            fail("Отсутствует токен или не получен список ингредиентов");
    }
    @After
    @Step("Удаление тестовых данных")
    public void cleanTestData() {
        if(token == null)
            return;

        checks.checkStatusCode(userApi.deleteUser(token), 202);
    }
    @Test
    @DisplayName("Создание заказа с авторизацией и с ингредиентами")
    public void createOrderWithAuthAndIngredients() {
        Response response = orderApi.createOrder(
                List.of(ingredients.get(0).get_id(), ingredients.get(ingredients.size() - 1).get_id()),
                token
        );

        checks.checkStatusCode(response, 200);
        checks.checkSuccessField(response, "true");
    }
    @Test
    @DisplayName("Создание заказа без авторизации и с ингредиентами")
    public void createOrderWithoutAuthAndWithIngredients() {
        Response response = orderApi.createOrder(
                List.of(ingredients.get(0).get_id(), ingredients.get(ingredients.size() - 1).get_id()),
                ""
        );

        checks.checkStatusCode(response, 200);
    }
    @Test
    @DisplayName("Создание заказа без авторизации и без ингредиентов")
    public void createOrderWithoutAuthAndWithoutIngredients() {
        Response response = orderApi.createOrder(
                List.of(""),
                ""
        );

        checks.checkStatusCode(response, 500);
    }
    @Test
    @DisplayName("Создание заказа с авторизацией и без ингредиентов")
    public void createOrderWithAuthAndWithoutIngredients() {
        Response response = orderApi.createOrder(
                List.of(),
                token
        );

        checks.checkStatusCode(response, 400);
        checks.checkSuccessField(response, "false");
        checks.checkMessageField(response, "Ingredient ids must be provided");
    }
    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithAuthAndIncorrectIngredients() {
        Response response = orderApi.createOrder(
                List.of(ingredients.get(0).get_id(), UUID.randomUUID().toString()),
                token
        );

        checks.checkStatusCode(response, 500);
    }
}
