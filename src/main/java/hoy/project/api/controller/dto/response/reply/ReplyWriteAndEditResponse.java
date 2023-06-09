package hoy.project.api.controller.dto.response.reply;

import lombok.Getter;

@Getter
public class ReplyWriteAndEditResponse {

    private final Long id;

    public ReplyWriteAndEditResponse(Long id) {
        this.id = id;
    }
}
