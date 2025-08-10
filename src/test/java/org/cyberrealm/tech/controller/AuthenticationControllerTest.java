package org.cyberrealm.tech.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.cyberrealm.tech.controller.AuthenticationControllerTest.TestResources.AUTH_DATASETS_PATH;
import static org.cyberrealm.tech.controller.AuthenticationControllerTest.TestResources.buildExpectedUserResponse;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.cyberrealm.tech.config.AbstractIntegrationTest;
import org.cyberrealm.tech.dto.user.UserLoginResponseDto;
import org.cyberrealm.tech.dto.user.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DBRider
@DBUnit(caseSensitiveTableNames = true, alwaysCleanAfter = true, alwaysCleanBefore = true)
class AuthenticationControllerTest extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/auth";
    }

    @Test
    @DataSet(AUTH_DATASETS_PATH + "roles_only.json")
    @ExpectedDataSet(value = AUTH_DATASETS_PATH
            + "user_after_registration.json", ignoreCols = "password")
    @SneakyThrows
    void givenValidRequest_whenRegister_shouldCreatesUserAndReturnsDto() {
        UserResponseDto responseDto = given()
                .contentType(ContentType.JSON)
                .body(TestResources.buildRegistrationRequestJson())
                .when()
                .post("/sign-up")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(UserResponseDto.class);

        assertThat(responseDto).isEqualTo(buildExpectedUserResponse());
    }

    @Test
    @DataSet(AUTH_DATASETS_PATH + "existing_user.json")
    void givenExistingUser_whenLogin_shouldReturnsAuthToken() {
        UserLoginResponseDto responseDto = given()
                .contentType(ContentType.JSON)
                .body(TestResources.buildLoginRequestJson())
                .when()
                .post("/sign-in")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(UserLoginResponseDto.class);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.token()).isNotBlank();
    }

    @Test
    @DataSet(AUTH_DATASETS_PATH + "existing_user.json")
    @ExpectedDataSet(AUTH_DATASETS_PATH + "existing_user.json")
    void givenExistingEmail_whenRegister_shouldReturnsConflict() {
        given()
                .contentType(ContentType.JSON)
                .body(TestResources.buildRegistrationRequestJson())
                .when()
                .post("/sign-up")
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    static class TestResources {
        static final String AUTH_DATASETS_PATH = "datasets/auth/";
        static final Long USER_ID = 1L;
        static final String USER_EMAIL = "user@ukr.net";
        static final String USER_PASSWORD = "password";
        static final String FIRST_NAME = "Vitalii";
        static final String LAST_NAME = "Stepanuk";

        static UserResponseDto buildExpectedUserResponse() {
            return UserResponseDto.builder()
                    .id(USER_ID)
                    .email(USER_EMAIL)
                    .firstName(FIRST_NAME)
                    .lastName(LAST_NAME)
                    .build();
        }

        static String buildRegistrationRequestJson() {
            return """
                    {
                      "email": "%s",
                      "password": "%s",
                      "repeatPassword": "%s",
                      "firstName": "%s",
                      "lastName": "%s"
                    }"""
                    .formatted(USER_EMAIL, USER_PASSWORD, USER_PASSWORD, FIRST_NAME, LAST_NAME);
        }

        static String buildLoginRequestJson() {
            return """
                    {
                      "email": "%s",
                      "password": "%s"
                    }"""
                    .formatted(USER_EMAIL, USER_PASSWORD);
        }
    }
}
