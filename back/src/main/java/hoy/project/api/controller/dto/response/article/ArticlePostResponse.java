package hoy.project.api.controller.dto.response.article;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticlePostResponse {

    private Long id;

    public ArticlePostResponse(Long id) {
        this.id = id;
    }
}
