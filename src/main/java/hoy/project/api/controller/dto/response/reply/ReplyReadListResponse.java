package hoy.project.api.controller.dto.response.reply;

import lombok.Getter;

import java.util.List;

@Getter
public class ReplyReadListResponse {

    private final Boolean hasNext;

    private final List<ReplyReadResponse> replyReadResponses;

    public ReplyReadListResponse(Boolean hasNext, List<ReplyReadResponse> replyReadResponses) {
        this.hasNext = hasNext;
        this.replyReadResponses = replyReadResponses;
    }
}
