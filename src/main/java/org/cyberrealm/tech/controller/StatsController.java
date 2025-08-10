package org.cyberrealm.tech.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.cyberrealm.tech.dto.article.AuthorStatsDto;
import org.cyberrealm.tech.service.ArticleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Statistics Management",
        description = "Endpoints for retrieving application statistics")
@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {
    private final ArticleService articleService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/top-authors")
    @Operation(summary = "Get top authors statistics",
            description = "Returns top-3 authors by the number of their articles "
                    + "for the last 50 days. Requires ADMIN role.")
    @SecurityRequirement(name = "BearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved statistics",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorStatsDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied. Requires ADMIN role.")
    })
    public List<AuthorStatsDto> getAuthorsStats() {
        return articleService.getTopAuthors();
    }
}
