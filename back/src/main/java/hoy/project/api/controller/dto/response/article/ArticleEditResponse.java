package hoy.project.api.controller.dto.response.article;

import lombok.Getter;

@Getter
public class ArticleEditResponse {

    private final Long id;

    public ArticleEditResponse(Long id) {
        this.id = id;
    }
}
