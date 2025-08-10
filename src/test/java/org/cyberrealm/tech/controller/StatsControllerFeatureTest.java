package org.cyberrealm.tech.controller;

import static io.restassured.RestAssured.given;
import static org.cyberrealm.tech.controller.StatsControllerFeatureTest.TestResources.ADMIN_USERNAME;
import static org.cyberrealm.tech.controller.StatsControllerFeatureTest.TestResources.BASE_DATASETS_PATH;
import static org.cyberrealm.tech.controller.StatsControllerFeatureTest.TestResources.BASE_RESOURCE_PATH;
import static org.cyberrealm.tech.controller.StatsControllerFeatureTest.TestResources.DEFAULT_USERNAME;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import io.restassured.RestAssured;
import lombok.SneakyThrows;
import org.cyberrealm.tech.config.AbstractIntegrationTest;
import org.cyberrealm.tech.utils.TestUtils;
import org.cyberrealm.tech.utils.TokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DBRider
@DBUnit(caseSensitiveTableNames = true, alwaysCleanAfter = true, alwaysCleanBefore = true)
class StatsControllerFeatureTest extends AbstractIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TokenUtils tokenUtils;

    @BeforeEach
    void cleanUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api";
    }

    @Test
    @SneakyThrows
    @DataSet(BASE_DATASETS_PATH + "stats_data.json")
    void givenAdminUser_whenGetStats_shouldReturnTopAuthors() {
        String response = given()
                .header(tokenUtils.buildAuth(ADMIN_USERNAME))
                .when()
                .get("/stats/top-authors")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().asString();

        JSONAssert.assertEquals(
                TestUtils.readResource(BASE_RESOURCE_PATH + "getStatsResponse.json"),
                response,
                JSONCompareMode.STRICT
        );
    }

    @Test
    @DataSet(BASE_DATASETS_PATH + "stats_data.json")
    void givenRegularUser_whenGetStats_shouldReturnForbidden() {
        given()
                .header(tokenUtils.buildAuth(DEFAULT_USERNAME))
                .when()
                .get("/stats/top-authors")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void givenUnauthenticated_whenGetStats_shouldReturnUnauthorized() {
        given()
                .when()
                .get("/stats/top-authors")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    static class TestResources {
        static final String BASE_RESOURCE_PATH = "rest/";
        static final String BASE_DATASETS_PATH = "datasets/";

        static final String ADMIN_USERNAME = "admin@gmail.com";
        static final String DEFAULT_USERNAME = "user@ukr.net";
    }
}
