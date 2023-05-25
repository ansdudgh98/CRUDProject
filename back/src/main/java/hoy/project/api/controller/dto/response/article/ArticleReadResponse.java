package hoy.project.api.controller.dto.response.article;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleReadResponse {

    private String title;

    private String content;

    private String userId;

    public ArticleReadResponse(String title, String content, String userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }
}
