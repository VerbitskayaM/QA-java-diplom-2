package clients;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BaseClient {
    public Response basePostRequest(String url, Object requestBody, String contentType) {
        return given(this.baseRequest(contentType))
                .body(requestBody)
                .when()
                .post(url);
    }
    public Response basePostRequest(String url, Object requestBody, String contentType, String token) {
        return given(this.baseRequest(contentType))
                .auth().oauth2(token)
                .body(requestBody)
                .when()
                .post(url);
    }

    public Response baseGetRequest(String url) {
        return given(this.baseRequest())
                .get(url);
    }

    public Response baseGetRequest(String url, String token) {
        return given(this.baseRequest())
                .auth().oauth2(token)
                .when()
                .get(url);
    }

    public Response baseDeleteRequest(String url, String token) {
        return given(this.baseRequest())
                .auth().oauth2(token)
                .delete(url);
    }

    public Response basePatchRequest(String url, Object requestBody, String contentType, String token) {
        return given(this.baseRequest(contentType))
                .auth().oauth2(token)
                .body(requestBody)
                .when()
                .patch(url);
    }

    private RequestSpecification baseRequest() {
        return new RequestSpecBuilder()
                .addFilter(new AllureRestAssured())
                .setRelaxedHTTPSValidation()
                .build();
    }

    private RequestSpecification baseRequest(String contentType) {
        return new RequestSpecBuilder()
                .addHeader("Content-type", contentType)
                .addFilter(new AllureRestAssured())
                .setRelaxedHTTPSValidation()
                .build();
    }
}
