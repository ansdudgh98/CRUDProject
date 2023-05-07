package hoy.project.api.controller.dto.response.article;

import lombok.Getter;

@Getter
public class ArticleReadResponse {

    private final String title;

    private final String content;

    private final String userId;

    public ArticleReadResponse(String title, String content, String userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }
}
