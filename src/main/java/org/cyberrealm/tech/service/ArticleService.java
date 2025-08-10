package org.cyberrealm.tech.service;

import java.util.List;
import org.cyberrealm.tech.dto.article.ArticleDto;
import org.cyberrealm.tech.dto.article.AuthorStatsDto;
import org.cyberrealm.tech.dto.article.CreateArticleRequestDto;
import org.springframework.data.domain.Pageable;

public interface ArticleService {
    ArticleDto save(CreateArticleRequestDto requestDto);

    List<ArticleDto> findAll(Pageable pageable);

    List<AuthorStatsDto> getTopAuthors();
}
