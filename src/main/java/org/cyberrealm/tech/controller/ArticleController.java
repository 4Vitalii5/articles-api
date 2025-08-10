package org.cyberrealm.tech.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.cyberrealm.tech.dto.article.ArticleDto;
import org.cyberrealm.tech.dto.article.CreateArticleRequestDto;
import org.cyberrealm.tech.service.ArticleService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Article Management", description = "Endpoints for creating and listing articles")
@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new article",
            description = "Creates a new article. All fields are mandatory.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Article created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409",
                    description = "Article with such title and author already exists")
    })
    public ArticleDto createArticle(@RequestBody @Valid CreateArticleRequestDto requestDto) {
        return articleService.save(requestDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    @Operation(summary = "Get all articles",
            description = "Returns a paginated list of all articles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public List<ArticleDto> getAll(@ParameterObject Pageable pageable) {
        return articleService.findAll(pageable);
    }
}
