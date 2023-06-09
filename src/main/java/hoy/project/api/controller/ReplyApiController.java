package hoy.project.api.controller;

import hoy.project.api.controller.argumentresolver.LoginAccountId;
import hoy.project.api.controller.dto.request.form.ReplyForm;
import hoy.project.api.controller.dto.response.reply.ReplyDeleteResponse;
import hoy.project.api.controller.dto.response.reply.ReplyReadListResponse;
import hoy.project.api.controller.dto.response.reply.ReplyWriteAndEditResponse;
import hoy.project.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/reply")
public class ReplyApiController {

    private final ReplyService replyService;

    @PostMapping
    public ResponseEntity<ReplyWriteAndEditResponse> postReply(@Valid @RequestBody ReplyForm replyForm, @RequestParam(name = "comment") Long commentId, @LoginAccountId String userId){
        return ResponseEntity.ok(replyService.writeReply(replyForm,commentId,userId));
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<ReplyReadListResponse> readReply10(@PathVariable Long id, @RequestParam int index){
        return ResponseEntity.ok(replyService.readReplyLatest10(id,index));
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<ReplyWriteAndEditResponse> editReply(@Valid @RequestBody ReplyForm replyForm, @PathVariable Long id,@LoginAccountId String userId){
        return ResponseEntity.ok(replyService.editReply(replyForm,id,userId));
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<ReplyDeleteResponse> delete(@PathVariable Long id, @LoginAccountId String userId){
        return ResponseEntity.ok(replyService.deActive(id,userId));

    }
}
