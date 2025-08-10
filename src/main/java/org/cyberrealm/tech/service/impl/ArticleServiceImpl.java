package org.cyberrealm.tech.service.impl;

import java.time.Duration;
import java.time.Instant;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository repository;
    private final ArticleMapper mapper;
    private final UserContext context;

    @Override
    public ArticleDto save(CreateArticleRequestDto requestDto) {
        Article entity = mapper.toEntity(requestDto, context);

        try {
            repository.save(entity);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException(
                    "Article with title: %s and author: %s already exists."
                            .formatted(entity.getTitle(), entity.getAuthor())
            );
        }

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
        Instant startDate = Instant.now().minus(Duration.ofDays(50));
        Pageable topThree = PageRequest.of(0, 3);
        return repository.findTopAuthors(startDate, topThree);
    }
}
