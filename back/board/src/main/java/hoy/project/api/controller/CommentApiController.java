package hoy.project.api.controller;

import hoy.project.api.controller.argumentresolver.LoginAccountId;
import hoy.project.api.controller.dto.request.form.CommentForm;
import hoy.project.api.controller.dto.response.comment.CommentEditResponse;
import hoy.project.api.controller.dto.response.comment.CommentPostResponse;
import hoy.project.api.controller.dto.response.comment.CommentsReadResponse;
import hoy.project.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/comment")
@Slf4j
public class CommentApiController {


    private final CommentService commentService;

    @PostMapping("/")
    public ResponseEntity<CommentPostResponse> writeComment(@Valid @RequestBody CommentForm commentForm, @RequestParam Long articleId, @LoginAccountId String userId){
        return ResponseEntity.ok(commentService.post(commentForm,articleId,userId));
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<CommentEditResponse> editComment(@Valid @RequestBody CommentForm commentForm, @RequestParam Long commentId, @LoginAccountId String userId){
        return ResponseEntity.ok(commentService.edit(commentForm,commentId,userId));
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity deleteComment(@PathVariable Long id,@LoginAccountId String userId){
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{index}")
    public ResponseEntity<CommentsReadResponse> readComments10Latest(@RequestParam Long articleId, @PathVariable int index){
        log.info("readComment");
        return ResponseEntity.ok(commentService.readCommentLatest10(articleId,index));
    }
}
