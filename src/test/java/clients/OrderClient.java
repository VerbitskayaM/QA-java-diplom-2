package clients;

import dto.Order;
import io.restassured.response.Response;


public class OrderClient extends BaseClient {
    public Response createOrder(Order order, String token) {
        return basePostRequest(
                ApiUrls.MAIN_HOST + ApiUrls.ORDERS,
                order,
                "application/json",
                token
        );
    }
    public Response getIngredientList() {
        return baseGetRequest(
                ApiUrls.MAIN_HOST + ApiUrls.INGREDIENTS
        );
    }

    public Response getOrderList(String token) {
        return baseGetRequest(
                ApiUrls.MAIN_HOST + ApiUrls.ORDERS,
                token
        );
    }

    public Response getOrderListAll() {
        return baseGetRequest(
                ApiUrls.MAIN_HOST + ApiUrls.ORDERS_ALL
        );
    }
}
