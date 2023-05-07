package hoy.project.api.controller.dto.response.comment;

import lombok.Getter;
import java.util.List;

@Getter
public class CommentsReadResponse {

    private final boolean hasNext;

    private final List<CommentReadResponse> commentReadResponses;

    public CommentsReadResponse(boolean hasNext, List<CommentReadResponse> commentReadResponses) {
        this.hasNext = hasNext;
        this.commentReadResponses = commentReadResponses;
    }


}


