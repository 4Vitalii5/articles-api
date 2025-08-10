package org.cyberrealm.tech.service.impl;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.cyberrealm.tech.dto.article.ArticleDto;
import org.cyberrealm.tech.dto.article.AuthorStatsDto;
import org.cyberrealm.tech.dto.article.CreateArticleRequestDto;
import org.cyberrealm.tech.exception.DuplicateResourceException;
import org.cyberrealm.tech.mapper.ArticleMapper;
import org.cyberrealm.tech.model.Article;
import org.cyberrealm.tech.repository.ArticleRepository;
import org.cyberrealm.tech.security.context.UserContext;
import org.cyberrealm.tech.service.ArticleService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private static final int STATS_DAYS = 50;
    private static final int PAGE_SIZE = 3;
    private static final int PAGE_NUMBER = 0;

    private final ArticleRepository repository;
    private final ArticleMapper mapper;
    private final UserContext context;

    @Override
    public ArticleDto save(CreateArticleRequestDto requestDto) {
        Article entity = mapper.toEntity(requestDto, context);

        validateArticleUniqueness(requestDto.title(), requestDto.author());

        repository.save(entity);

        return mapper.toDto(entity);
    }

    @Override
    public List<ArticleDto> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<AuthorStatsDto> getTopAuthors() {
        ZonedDateTime sinceDate = ZonedDateTime.now().minusDays(STATS_DAYS);
        Pageable topThree = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        return repository.findTopAuthors(sinceDate, topThree);
    }

    private void validateArticleUniqueness(String title, String author) {
        if (repository.existsByTitleAndAuthor(title, author)) {
            throw new DuplicateResourceException(
                    "Article with title: '%s' and author: '%s' already exists."
                            .formatted(title, author)
            );
        }
    }
}
