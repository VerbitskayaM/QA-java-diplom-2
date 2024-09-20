package tests;

import steps.ResponseChecksSteps;
import steps.UserApiSteps;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.util.UUID;

import static org.junit.Assert.fail;

@DisplayName("Логин пользователя")
public class LoginUserTests {
    private String email, password, name, token;
    private final UserApiSteps userApi = new UserApiSteps();
    private final ResponseChecksSteps checks = new ResponseChecksSteps();

    @Before
    @Step("Подготовка тестовых данных")
    public void setUp() {
        email = "e-mail_" + UUID.randomUUID() + "@mail.com";
        password = "pass";
        name = "name";

        // Создание пользователя
        Response response = userApi.createUser(email, password, name);
        checks.checkStatusCode(response, 200);

        // Получение токена авторизации
        if (response.getStatusCode() == 200) {
            token = userApi.getToken(response);
        }
        if (token == null)
            fail("Пользователь не создан");

    }
    @After
    @Step("Удаление тестовых данных")
    public void tearDown() {
        if(token.isEmpty())
            return;

        checks.checkStatusCode(userApi.deleteUser(token), 202);
    }

    @Test
    @DisplayName("Логин существующим пользователем")
    public void loginUserWithExistData() {
        Response response = userApi.loginUser(email, password);

        checks.checkStatusCode(response, 200);
        checks.checkSuccessField(response, "true");
    }
    @Test
    @DisplayName("Логин с некорректным email")
    public void loginUserIncorrectEmail() {
        Response response = userApi.loginUser("newE-mail_" + UUID.randomUUID() + "@mail.com", password);

        checks.checkStatusCode(response, 401);
        checks.checkSuccessField(response, "false");
        checks.checkMessageField(response, "email or password are incorrect");
    }
    @Test
    @DisplayName("Логин с некорректным password")
    public void loginUserIncorrectPassword() {
        Response response = userApi.loginUser(email, password  + UUID.randomUUID());

        checks.checkStatusCode(response, 401);
        checks.checkSuccessField(response, "false");
        checks.checkMessageField(response, "email or password are incorrect");
    }
    @Test
    @DisplayName("Авторизация пользователя без email")
    public void loginUserMissedEmail() {
        Response response = userApi.loginUser("", password);

        checks.checkStatusCode(response, 401);
        checks.checkSuccessField(response, "false");
        checks.checkMessageField(response, "email or password are incorrect");
    }
    @Test
    @DisplayName("Авторизация пользователя без password")
    public void loginUserMissedPassword() {
        Response response = userApi.loginUser(email, "");

        checks.checkStatusCode(response, 401);
        checks.checkSuccessField(response, "false");
        checks.checkMessageField(response, "email or password are incorrect");
    }

}
