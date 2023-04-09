package hoy.project.service;

import hoy.project.api.controller.dto.request.form.CommentForm;
import hoy.project.api.controller.dto.response.comment.CommentEditResponse;
import hoy.project.api.controller.dto.response.comment.CommentPostResponse;
import hoy.project.api.controller.dto.response.comment.CommentsReadResponse;

public interface CommentService {

    CommentPostResponse post(CommentForm commentForm,Long articleId,String userId);

    CommentEditResponse edit(CommentForm commentForm, Long commentId,String userId);

    CommentsReadResponse readCommentLatest10(Long articleId, int index);

    void delete(Long index, String userId);
}
