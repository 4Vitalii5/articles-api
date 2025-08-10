package org.cyberrealm.tech.dto.article;

import java.time.Instant;

public record ArticleDto(
        Long id,
        String title,
        String author,
        String content,
        Instant publishDate
) {
}
