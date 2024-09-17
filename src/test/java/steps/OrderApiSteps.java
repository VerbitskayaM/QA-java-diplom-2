package steps;

import clients.OrderClient;
import dto.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

public class OrderApiSteps extends OrderClient {
    @Step("Отправляем запрос на создание заказа")
    public Response createOrder(List<String> ingredients, String token) {
        return super.createOrder(new Order(ingredients), token);
    }

    @Step("Получаем список ингредиентов")
    public Response getIngredientList() {
        return super.getIngredientList();
    }

    @Step("Получаем список заказов")
    public Response getOrderList(String token) {
        return super.getOrderList(token);
    }

    @Step("Получаем список всех заказов")
    public Response getOrderListAll() {
        return super.getOrderListAll();
    }
}
