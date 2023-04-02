package hoy.project.api.controller.dto.response.article;


import lombok.Getter;

@Getter
public class ArticleDeleteResponse {

    private final String message;

    public ArticleDeleteResponse(String message) {
        this.message = message;
    }
}
