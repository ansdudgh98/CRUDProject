package hoy.project.api.controller;

import hoy.project.api.controller.argumentresolver.LoginAccountId;
import hoy.project.api.controller.dto.request.form.ReplyForm;
import hoy.project.api.controller.dto.response.reply.ReplyDeleteResponse;
import hoy.project.api.controller.dto.response.reply.ReplyReadListResponse;
import hoy.project.api.controller.dto.response.reply.ReplyWriteAndEditResponse;
import hoy.project.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reply")
public class ReplyApiController {

    private final ReplyService replyService;

    @PostMapping
    public ReplyWriteAndEditResponse postReply(@Valid @RequestBody ReplyForm replyForm,
                                               @RequestParam(name = "comment") Long commentId,
                                               @LoginAccountId String userId) {
        return replyService.writeReply(replyForm, commentId, userId);
    }

    @GetMapping("/read/{id}")
    public ReplyReadListResponse readReply10(@PathVariable Long id, @RequestParam int index) {
        return replyService.readReplyLatest10(id, index);
    }

    @PostMapping("/edit/{id}")
    public ReplyWriteAndEditResponse editReply(@Valid @RequestBody ReplyForm replyForm,
                                               @PathVariable Long id,
                                               @LoginAccountId String userId) {
        return replyService.editReply(replyForm, id, userId);
    }

    @GetMapping("/delete/{id}")
    public ReplyDeleteResponse delete(@PathVariable Long id, @LoginAccountId String userId) {
        return replyService.deActive(id, userId);

    }
}
