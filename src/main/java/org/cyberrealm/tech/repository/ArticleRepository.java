package org.cyberrealm.tech.repository;

import java.time.Instant;
import java.util.List;
import org.cyberrealm.tech.dto.article.AuthorStatsDto;
import org.cyberrealm.tech.model.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT new org.cyberrealm.tech.dto.article.AuthorStatsDto(a.author, COUNT(a)) "
            + "FROM Article a "
            + "WHERE a.publishDate >= :startDate "
            + "GROUP BY a.author "
            + "ORDER BY COUNT(a) DESC")
    List<AuthorStatsDto> findTopAuthors(@Param("startDate") Instant startDate, Pageable pageable);
}
