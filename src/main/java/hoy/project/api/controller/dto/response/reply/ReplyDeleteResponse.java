package hoy.project.api.controller.dto.response.reply;

import lombok.Getter;

@Getter
public class ReplyDeleteResponse {

    private String message;

    public ReplyDeleteResponse() {
        this.message = "성공!";
    }
}
