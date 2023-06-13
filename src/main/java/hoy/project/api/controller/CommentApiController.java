package hoy.project.api.controller;

import hoy.project.api.controller.argumentresolver.LoginAccountId;
import hoy.project.api.controller.dto.request.form.CommentForm;
import hoy.project.api.controller.dto.response.comment.CommentDeleteResponse;
import hoy.project.api.controller.dto.response.comment.CommentEditResponse;
import hoy.project.api.controller.dto.response.comment.CommentPostResponse;
import hoy.project.api.controller.dto.response.comment.CommentsReadResponse;
import hoy.project.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentApiController {


    private final CommentService commentService;

    @PostMapping("/")
    public CommentPostResponse writeComment(@Valid @RequestBody CommentForm commentForm,
                                            @RequestParam(name = "article") Long articleId,
                                            @LoginAccountId String userId) {
        return commentService.post(commentForm, articleId, userId);
    }

    @PostMapping("/edit/{id}")
    public CommentEditResponse editComment(@Valid @RequestBody CommentForm commentForm,
                                           @PathVariable(name = "id") Long commentId,
                                           @LoginAccountId String userId) {
        return commentService.edit(commentForm, commentId, userId);
    }

    @GetMapping("/delete/{id}")
    public CommentDeleteResponse deleteComment(@PathVariable Long id, @LoginAccountId String userId) {
        return commentService.delete(id, userId);
    }

    @GetMapping("/{index}")
    public CommentsReadResponse readComments10Latest(@RequestParam(name = "article") Long articleId, @PathVariable int index) {
        return commentService.readCommentLatest10(articleId, index);
    }
}
