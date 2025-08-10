package org.cyberrealm.tech.mapper;

import org.cyberrealm.tech.dto.article.ArticleDto;
import org.cyberrealm.tech.dto.article.CreateArticleRequestDto;
import org.cyberrealm.tech.model.Article;
import org.cyberrealm.tech.security.context.UserContext;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    ArticleDto toDto(Article article);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", expression = "java(context.getUser())")
    Article toEntity(CreateArticleRequestDto requestDto, UserContext context);
}
