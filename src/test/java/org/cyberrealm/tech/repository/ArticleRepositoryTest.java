package org.cyberrealm.tech.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cyberrealm.tech.repository.ArticleRepositoryTest.TestResources.DATASETS_PATH;
import static org.cyberrealm.tech.repository.ArticleRepositoryTest.TestResources.START_DATE;
import static org.cyberrealm.tech.repository.ArticleRepositoryTest.TestResources.buildExpectedMultipleArticles;
import static org.cyberrealm.tech.repository.ArticleRepositoryTest.TestResources.buildExpectedStatsDto;
import static org.cyberrealm.tech.repository.ArticleRepositoryTest.TestResources.buildNewArticle;
import static org.cyberrealm.tech.repository.ArticleRepositoryTest.TestResources.buildPageable;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import java.time.ZonedDateTime;
import java.util.List;
import org.cyberrealm.tech.config.AbstractIntegrationTest;
import org.cyberrealm.tech.dto.article.AuthorStatsDto;
import org.cyberrealm.tech.model.Article;
import org.cyberrealm.tech.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DBRider
@DBUnit(caseSensitiveTableNames = true, alwaysCleanAfter = true, alwaysCleanBefore = true)
class ArticleRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    @DataSet(DATASETS_PATH + "default_user.json")
    @ExpectedDataSet(value = DATASETS_PATH + "article_after_save.json", ignoreCols = "id")
    void givenNewArticle_save_shouldSaveAndReturnArticle() {
        articleRepository.save(buildNewArticle());
    }

    @Test
    @DataSet({DATASETS_PATH + "default_user.json", DATASETS_PATH + "multiple_articles.json"})
    void givenMultipleArticles_findAllWithPagination_shouldReturnPagedArticles() {
        Page<Article> result = articleRepository.findAll(buildPageable());

        assertThat(result.getContent()).isEqualTo(buildExpectedMultipleArticles());
    }

    @Test
    @DataSet(DATASETS_PATH + "stats_data.json")
    void givenArticles_findTopAuthors_shouldReturnOnlyRecentAuthors() {
        List<AuthorStatsDto> result = articleRepository.findTopAuthors(START_DATE, buildPageable());

        assertThat(result).isEqualTo(buildExpectedStatsDto());
    }

    @Test
    void givenEmptyRepository_findAll_shouldReturnEmptyPage() {
        Page<Article> result = articleRepository.findAll(buildPageable());

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void givenArticles_findTopAuthors_shouldReturnEmptyList() {
        List<AuthorStatsDto> result = articleRepository.findTopAuthors(START_DATE, buildPageable());

        assertThat(result).isEmpty();
    }

    static class TestResources {
        static final String DATASETS_PATH = "datasets/";

        static final Long USER_ID = 1L;
        static final ZonedDateTime START_DATE = ZonedDateTime.now().minusDays(50);

        static final Long FIRST_ARTICLE_ID = 1L;
        static final String FIRST_TITLE = "First title";
        static final String FIRST_AUTHOR = "First author";
        static final String FIRST_CONTENT = "first content";
        static final ZonedDateTime PUBLISH_DATE = ZonedDateTime.parse("2025-08-09T19:30:00Z");

        static final Long SECOND_ARTICLE_ID = 2L;
        static final String SECOND_TITLE = "Second title";
        static final String SECOND_AUTHOR = "Second author";
        static final String SECOND_CONTENT = "Second content";

        static final Long THIRD_ARTICLE_ID = 3L;
        static final String THIRD_TITLE = "Third title";
        static final String THIRD_AUTHOR = "Third author";
        static final String THIRD_CONTENT = "Third content";

        static Article buildNewArticle() {
            return Article.builder()
                    .title(FIRST_TITLE)
                    .author(FIRST_AUTHOR)
                    .content(FIRST_CONTENT)
                    .publishDate(PUBLISH_DATE)
                    .user(buildTestUser())
                    .build();
        }

        static Article buildFirstArticle() {
            return Article.builder()
                    .id(FIRST_ARTICLE_ID)
                    .title(FIRST_TITLE)
                    .author(FIRST_AUTHOR)
                    .content(FIRST_CONTENT)
                    .publishDate(PUBLISH_DATE)
                    .user(buildTestUser())
                    .build();
        }

        static Article buildSecondArticle() {
            return Article.builder()
                    .id(SECOND_ARTICLE_ID)
                    .title(SECOND_TITLE)
                    .author(SECOND_AUTHOR)
                    .content(SECOND_CONTENT)
                    .publishDate(PUBLISH_DATE)
                    .user(buildTestUser())
                    .build();
        }

        static Article buildThirdArticle() {
            return Article.builder()
                    .id(THIRD_ARTICLE_ID)
                    .title(THIRD_TITLE)
                    .author(THIRD_AUTHOR)
                    .content(THIRD_CONTENT)
                    .publishDate(PUBLISH_DATE)
                    .user(buildTestUser())
                    .build();
        }

        static List<Article> buildExpectedMultipleArticles() {
            return List.of(buildFirstArticle(), buildSecondArticle(), buildThirdArticle());
        }

        static Pageable buildPageable() {
            return PageRequest.of(0, 3);
        }

        static User buildTestUser() {
            return User.builder()
                    .id(USER_ID)
                    .build();
        }

        static List<AuthorStatsDto> buildExpectedStatsDto() {
            AuthorStatsDto firstAuthorStats = AuthorStatsDto.builder()
                    .author("Author A")
                    .articleCount(3L)
                    .build();
            AuthorStatsDto secondAuthorStats = AuthorStatsDto.builder()
                    .author("Author B")
                    .articleCount(2L)
                    .build();
            AuthorStatsDto thirdAuthorStats = AuthorStatsDto.builder()
                    .author("Author C")
                    .articleCount(1L)
                    .build();

            return List.of(firstAuthorStats, secondAuthorStats, thirdAuthorStats);
        }
    }
}
