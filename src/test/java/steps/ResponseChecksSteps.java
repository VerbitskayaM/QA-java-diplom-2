package steps;

import dto.UserResponse;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Assert;

public class ResponseChecksSteps {

    @Step("Проверяем код ответа")
    public void checkStatusCode(Response response, int code) {
        Allure.addAttachment("Ответ", response.getStatusLine());
        response.then().statusCode(code);
    }

    @Step("Проверяем успешность обращения")
    public void checkSuccessField(Response response, String expectedValue) {
        String actualValue = response.body().as(UserResponse.class).getSuccess();
        Assert.assertEquals(
                "Значение поля success не совпадает с ожидаемым",
                expectedValue,
                actualValue);
    }
    @Step("Проверяем сообщение ответа")
    public void checkMessageField(Response response, String expectedMessage) {
        String actualMessage = response.body().as(UserResponse.class).getMessage();
        Assert.assertEquals(
                "Значение поля message не совпадает с ожидаемым",
                expectedMessage,
                actualMessage);
    }
}
