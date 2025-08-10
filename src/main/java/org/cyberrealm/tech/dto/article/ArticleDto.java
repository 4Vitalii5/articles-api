package org.cyberrealm.tech.dto.article;

import java.time.ZonedDateTime;

public record ArticleDto(
        Long id,
        String title,
        String author,
        String content,
        ZonedDateTime publishDate
) {
}
