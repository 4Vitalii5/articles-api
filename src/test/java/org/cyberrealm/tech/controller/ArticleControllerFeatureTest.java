package org.cyberrealm.tech.controller;

import static io.restassured.RestAssured.given;
import static org.cyberrealm.tech.controller.ArticleControllerFeatureTest.TestResources.BASE_DATASETS_PATH;
import static org.cyberrealm.tech.controller.ArticleControllerFeatureTest.TestResources.BASE_RESOURCE_PATH;
import static org.cyberrealm.tech.controller.ArticleControllerFeatureTest.TestResources.DEFAULT_USERNAME;
import static org.cyberrealm.tech.controller.ArticleControllerFeatureTest.TestResources.buildCreateArticleRqJson;
import static org.cyberrealm.tech.controller.ArticleControllerFeatureTest.TestResources.buildInvalidCreateArticleRqJson;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
class ArticleControllerFeatureTest extends AbstractIntegrationTest {

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
    @DataSet(BASE_DATASETS_PATH + "default_user.json")
    @ExpectedDataSet(value = BASE_DATASETS_PATH + "article_after_save.json", ignoreCols = "id")
    void givenCreateArticleRequest_whenCreateArticle_shouldCreateArticle() {
        String response = given()
                .body(buildCreateArticleRqJson())
                .header(tokenUtils.buildAuth(DEFAULT_USERNAME))
                .contentType(ContentType.JSON)
                .when()
                .post("/articles")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .body()
                .asString();

        JSONAssert.assertEquals(
                TestUtils.readResource(BASE_RESOURCE_PATH + "createArticle.json"),
                response,
                JSONCompareMode.STRICT
        );
    }

    @Test
    @DataSet(BASE_DATASETS_PATH + "default_user.json")
    @ExpectedDataSet(BASE_DATASETS_PATH + "empty_articles.json")
    void givenInvalidRequest_whenCreateArticle_shouldReturnBadRequest() {
        given()
                .body(buildInvalidCreateArticleRqJson())
                .header(tokenUtils.buildAuth(DEFAULT_USERNAME))
                .contentType(ContentType.JSON)
                .when()
                .post("/articles")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DataSet({BASE_DATASETS_PATH + "default_user.json", BASE_DATASETS_PATH + "single_article.json"})
    @ExpectedDataSet(BASE_DATASETS_PATH + "single_article.json")
    void givenDuplicateArticle_whenCreateArticle_shouldReturnConflict() {
        given()
                .body(buildCreateArticleRqJson())
                .header(tokenUtils.buildAuth(DEFAULT_USERNAME))
                .contentType(ContentType.JSON)
                .when()
                .post("/articles")
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void givenUnauthenticated_whenCreateArticle_shouldReturnUnauthorized() {
        given()
                .body(buildCreateArticleRqJson())
                .contentType(ContentType.JSON)
                .when()
                .post("/articles")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @SneakyThrows
    @DataSet({
            BASE_DATASETS_PATH + "default_user.json",
            BASE_DATASETS_PATH + "multiple_articles.json"})
    void givenArticlesInDb_whenGetArticles_shouldReturnListOfArticles() {
        String response = given()
                .header(tokenUtils.buildAuth(DEFAULT_USERNAME))
                .when()
                .get("/articles")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().asString();

        JSONAssert.assertEquals(
                TestUtils.readResource(BASE_RESOURCE_PATH + "getArticlesResponse.json"),
                response,
                JSONCompareMode.LENIENT
        );
    }

    static class TestResources {
        static final String BASE_RESOURCE_PATH = "rest/";
        static final String BASE_DATASETS_PATH = "datasets/";
        static final String DEFAULT_USERNAME = "user@ukr.net";

        static String buildCreateArticleRqJson() {
            return """
                    {
                      "title": "First title",
                      "author": "First author",
                      "content": "first content",
                      "publishDate": "2025-08-09T19:30:00Z"
                    }""";
        }

        static String buildInvalidCreateArticleRqJson() {
            return """
                    {
                      "title": "",
                      "author": "First author",
                      "content": "first content",
                      "publishDate": "2025-08-09T19:30:00Z"
                    }""";
        }
    }
}
