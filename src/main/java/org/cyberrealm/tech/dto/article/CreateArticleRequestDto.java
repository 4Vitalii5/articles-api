package org.cyberrealm.tech.dto.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.ZonedDateTime;

public record CreateArticleRequestDto(
        @NotBlank
        @Size(max = 100, message = "Title cannot exceed 100 characters")
        String title,
        @NotBlank
        String author,
        @NotBlank
        String content,
        @NotNull
        ZonedDateTime publishDate
) {
}
