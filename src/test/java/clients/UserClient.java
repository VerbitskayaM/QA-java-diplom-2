package clients;

import dto.User;
import io.restassured.response.Response;

public class UserClient extends BaseClient {
    public Response createUser(User user) {
    return basePostRequest(
            ApiUrls.MAIN_HOST + ApiUrls.CREATE_USER,
            user,
            "application/json"
    );
}
    public Response deleteUser(String token) {
        return baseDeleteRequest(
                ApiUrls.MAIN_HOST + ApiUrls.USER,
                token
        );
    }
    public Response loginUser(User user) {
        return basePostRequest(
                ApiUrls.MAIN_HOST + ApiUrls.LOGIN_USER,
                user,
                "application/json"
        );
    }
    public Response updateUser(User user, String token) {
        return basePatchRequest(
                ApiUrls.MAIN_HOST + ApiUrls.USER,
                user,
                "application/json",
                token
        );
    }
}
