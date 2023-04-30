package hoy.project.api.controller.dto.response.comment;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentReadResponse {

    private final Long id;

    private final String author;

    private final String content;

    private final LocalDateTime createdDate;

    private final LocalDateTime modifiedDate;

    public CommentReadResponse(Long id, String author, String content, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}

