package org.cyberrealm.tech.dto.article;

import lombok.Builder;

@Builder
public record AuthorStatsDto(
        String author,
        Long articleCount
) {
}
