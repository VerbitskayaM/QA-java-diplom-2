package steps;

import clients.UserClient;
import dto.User;
import dto.UserResponse;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Assert;


public class UserApiSteps extends UserClient {
    @Step("Отправка запроса на создание пользователя")
    public Response createUser(String email, String password, String name) {
        return super.createUser(new User(email, password, name));
    }
    @Step("Отправка запроса на логин пользователя")
    public Response loginUser(String email, String password) {
        return super.loginUser(new User(email, password));
    }
    @Step("Удаление пользователя")
    public Response deleteUser(String token) {
        return super.deleteUser(token);
    }
    @Step("Обновление информации о пользователе")
    public Response updateUser(String email, String password, String name, String token) {
        return super.updateUser(new User(email, password, name), token);
    }
    @Step("Проверка данных пользователя")
    public void checkUser(Response response, String expectedMail, String expectedPassword, String expectedName) {
        User actualUser = response.body().as(UserResponse.class).getUser();
        Allure.addAttachment("Новый пользователь", actualUser.toString());

        Assert.assertEquals("Не совпадают email-ы", actualUser.getEmail(), expectedMail);
        Assert.assertEquals("Не совпадают имена", actualUser.getName(), expectedName);

        new ResponseChecksSteps().checkStatusCode(loginUser(expectedMail, expectedPassword), 200);
    }
    @Step("Получение токена авторизации")
    public String getToken(Response response) {
        String token = response.body().as(UserResponse.class).getAccessToken().split(" ")[1];
        Allure.addAttachment("Ответ", response.getStatusLine());
        Allure.addAttachment("Токен", token);
        return token;
    }
}
