package org.cyberrealm.tech.repository;

import java.time.ZonedDateTime;
import java.util.List;
import org.cyberrealm.tech.dto.article.AuthorStatsDto;
import org.cyberrealm.tech.model.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("""
                SELECT new org.cyberrealm.tech.dto.article.AuthorStatsDto(a.author, COUNT(a.id))
                FROM Article a
                WHERE a.publishDate >= :sinceDate
                GROUP BY a.author
                ORDER BY COUNT(a.id) DESC
            """)
    List<AuthorStatsDto> findTopAuthors(ZonedDateTime sinceDate, Pageable pageable);
}
