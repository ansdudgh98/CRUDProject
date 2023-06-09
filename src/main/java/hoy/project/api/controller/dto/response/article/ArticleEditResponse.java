package hoy.project.api.controller.dto.response.article;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleEditResponse {

    private Long id;

    public ArticleEditResponse(Long id) {
        this.id = id;
    }
}
