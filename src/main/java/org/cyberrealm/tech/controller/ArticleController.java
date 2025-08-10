package org.cyberrealm.tech.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.cyberrealm.tech.dto.article.ArticleDto;
import org.cyberrealm.tech.dto.article.CreateArticleRequestDto;
import org.cyberrealm.tech.service.ArticleService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    @PostMapping
    public ArticleDto createArticle(@RequestBody @Valid CreateArticleRequestDto requestDto) {
        return articleService.save(requestDto);
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    @GetMapping
    public List<ArticleDto> getAll(Pageable pageable) {
        return articleService.findAll(pageable);
    }
}
