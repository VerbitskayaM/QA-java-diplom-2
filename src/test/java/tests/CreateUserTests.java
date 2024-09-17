package tests;

import steps.ResponseChecksSteps;
import steps.UserApiSteps;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

@DisplayName("Создание пользователя")
public class CreateUserTests {
    private String email, password, name;
    private ArrayList<String> tokens = new ArrayList<>();
    private final UserApiSteps userApi = new UserApiSteps();
    private final ResponseChecksSteps checks = new ResponseChecksSteps();

    @Before
    @Step("Подготовка тестовых данных")
    public void setUp() {
        email = "e-mail_" + UUID.randomUUID() + "@mail.com";
        password = "pass_" + UUID.randomUUID();
        name = "name";
    }
    @After
    @Step("Удаление тестовых данных")
    public void tearDown() {
        if(tokens.isEmpty())
            return;
        for (String token: tokens) {
            checks.checkStatusCode(userApi.deleteUser(token), 202);
        }
    }

    @Test
    @DisplayName("Создание нового пользователя")
    public void createNewUser() {
        Response response = userApi.createUser(email, password, name);
        if (response.getStatusCode() == 200) {
            tokens.add(userApi.getToken(response));
        }

        checks.checkStatusCode(response, 200);
        checks.checkSuccessField(response, "true");
    }
    @Test
    @DisplayName("Создание пользователя с используемыми данными")
    public void createNewSimilarUser() {
        Response responseFirstUser = userApi.createUser(email, password, name);
        Response responseSecondUser = userApi.createUser(email, password, name);
        if (responseFirstUser.getStatusCode() == 200) {
            tokens.add(userApi.getToken(responseFirstUser));
        }
        if (responseSecondUser.getStatusCode() == 200) {
            tokens.add(userApi.getToken(responseSecondUser));
        }

        checks.checkStatusCode(responseSecondUser, 403);
        checks.checkSuccessField(responseSecondUser, "false");
        checks.checkMessageField(responseSecondUser, "User already exists");
    }
    @Test
    @DisplayName("Создание пользователя без email")
    public void createNewUserMissedEmail() {
        Response response = userApi.createUser("", password, name);
        if (response.getStatusCode() == 200) {
            tokens.add(userApi.getToken(response));
        }

        checks.checkStatusCode(response, 403);
        checks.checkSuccessField(response, "false");
        checks.checkMessageField(response, "Email, password and name are required fields");
    }
    @Test
    @DisplayName("Создание пользователя без password")
    public void createNewUserMissedPassword() {
        Response response = userApi.createUser(email, "", name);
        if (response.getStatusCode() == 200) {
            tokens.add(userApi.getToken(response));
        }

        checks.checkStatusCode(response, 403);
        checks.checkSuccessField(response, "false");
        checks.checkMessageField(response, "Email, password and name are required fields");
    }
    @Test
    @DisplayName("Создание пользователя без name")
    public void createNewUserMissedName() {
        Response response = userApi.createUser(email, password, "");
        if (response.getStatusCode() == 200) {
            tokens.add(userApi.getToken(response));
        }

        checks.checkStatusCode(response, 403);
        checks.checkSuccessField(response, "false");
        checks.checkMessageField(response, "Email, password and name are required fields");
    }
    @Test
    @DisplayName("Создание пользователя с незаполненными полями")
    public void createNewUserMissedAllParams() {
        Response response = userApi.createUser("", "", "");
        if (response.getStatusCode() == 200) {
            tokens.add(userApi.getToken(response));
        }

        checks.checkStatusCode(response, 403);
        checks.checkSuccessField(response, "false");
        checks.checkMessageField(response, "Email, password and name are required fields");
    }
}
