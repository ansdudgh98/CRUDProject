package hoy.project.api.controller.dto.response.comment;

import lombok.Getter;

@Getter
public class CommentEditResponse {

    private Long id;

    public CommentEditResponse(Long id) {
        this.id = id;
    }
}
