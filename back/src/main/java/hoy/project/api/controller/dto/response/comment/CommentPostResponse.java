package hoy.project.api.controller.dto.response.comment;

import hoy.project.domain.Comment;
import lombok.Getter;

@Getter
public class CommentPostResponse {

    private Long id;

    public CommentPostResponse(Long id) {
        this.id = id;
    }
}
