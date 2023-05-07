package hoy.project.api.controller.dto.response.comment;

import lombok.Getter;

@Getter
public class CommentDeleteResponse {

    private String message;

    public CommentDeleteResponse() {
        this.message = "success";
    }
}
