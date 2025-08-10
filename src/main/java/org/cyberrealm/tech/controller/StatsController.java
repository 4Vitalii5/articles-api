package org.cyberrealm.tech.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.cyberrealm.tech.dto.article.AuthorStatsDto;
import org.cyberrealm.tech.service.ArticleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {
    private final ArticleService articleService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/top-authors")
    public List<AuthorStatsDto> getAuthorsStats() {
        return articleService.getTopAuthors();
    }
}
