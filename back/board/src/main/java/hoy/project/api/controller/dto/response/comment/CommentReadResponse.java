package hoy.project.api.controller.dto.response.comment;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentReadResponse {

    private final String author;

    private final String content;

    private final LocalDateTime createdDate;

    private final LocalDateTime modifiedDate;

    public CommentReadResponse(String author, String content, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.author = author;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}

