package hoy.project.api.controller.dto.response.article;

import lombok.Getter;

@Getter
public class ArticlePostResponse {

    private final Long id;

    public ArticlePostResponse(Long id) {
        this.id = id;
    }
}
