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

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.fail;

@DisplayName("Получение списка заказов")
public class GetUsersOrderListTests {
    private String token;
    private boolean isOrderCreated = false;
    private final OrderApiSteps orderApi = new OrderApiSteps();
    private final UserApiSteps userApi = new UserApiSteps();
    private final ResponseChecksSteps baseApi = new ResponseChecksSteps();

    @Before
    @Step("Подготовка тестовых данных")
    public void setUp() {
        String email = "e-mail_" + UUID.randomUUID() + "@mail.com";
        String password = "pass";
        String name = "name";

        // Создание пользователя
        Response response = userApi.createUser(email, password, name);
        baseApi.checkStatusCode(response, 200);

        // Получение токена авторизации
        if (response.getStatusCode() == 200) {
            token = userApi.getToken(response);
        }

        //Получение списка ингредиентов
        response = orderApi.getIngredientList();
        baseApi.checkStatusCode(response, 200);

        List<Ingredient> ingredients = response.body().as(IngredientsResponse.class).getData();

        // Создание заказа
        response = orderApi.createOrder(
                List.of(ingredients.get(0).get_id(), ingredients.get(ingredients.size() - 1).get_id()),
                token
        );
        baseApi.checkStatusCode(response, 200);

        if(response.getStatusCode() == 200) {
            isOrderCreated = true;
        }
    }
    @After
    @Step("Удаление тестовых данных")
    public void tearDown() {
        if(token == null)
            return;

        baseApi.checkStatusCode(userApi.deleteUser(token), 202);
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя (авторизованный пользователь)")
    public void getOrderListWithAuth() {
        if (token == null || !isOrderCreated)
            fail("Не создан тестовый пользователь или заказ");

        Response response = orderApi.getOrderList(token);

        baseApi.checkStatusCode(response, 200);
        baseApi.checkSuccessField(response, "true");
    }
    @Test
    @DisplayName("Получение заказов конкретного пользователя (неавторизованный пользователь)")
    public void getOrderListWithoutAuth() {
        if (token == null || !isOrderCreated)
            fail("Не создан тестовый пользователь или заказ");

        Response response = orderApi.getOrderList("");

        baseApi.checkStatusCode(response, 401);
        baseApi.checkSuccessField(response, "false");
        baseApi.checkMessageField(response, "You should be authorised");
    }
    @Test
    @DisplayName("Получение списка со всеми заказами")
    public void getOrderListAllOrders() {
        Response response = orderApi.getOrderListAll();

        baseApi.checkStatusCode(response, 200);
        baseApi.checkSuccessField(response, "true");
    }
}
