package hoy.project.api.controller.dto.response.reply;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReplyReadResponse {

    private final Long id;

    private final String content;

    private final String author;

    private final LocalDateTime createdDateTime;

    private final LocalDateTime modifiedDateTime;

    public ReplyReadResponse(Long id, String content, String author, LocalDateTime createdDateTime, LocalDateTime modifiedDateTime) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.createdDateTime = createdDateTime;
        this.modifiedDateTime = modifiedDateTime;
    }
}
