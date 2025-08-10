package org.cyberrealm.tech.dto.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

public record CreateArticleRequestDto(
        @NotBlank
        @Size(max = 100)
        String title,
        @NotBlank
        String author,
        @NotBlank
        String content,
        @NotNull
        Instant publishDate
) {
}
