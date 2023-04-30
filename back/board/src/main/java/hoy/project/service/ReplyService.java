package hoy.project.service;


import hoy.project.api.controller.dto.request.form.ReplyForm;
import hoy.project.api.controller.dto.response.reply.ReplyDeleteResponse;
import hoy.project.api.controller.dto.response.reply.ReplyReadListResponse;
import hoy.project.api.controller.dto.response.reply.ReplyWriteAndEditResponse;

public interface ReplyService {

    ReplyWriteAndEditResponse writeReply(ReplyForm replyForm, Long commentId, String userId);

    ReplyWriteAndEditResponse editReply(ReplyForm replyForm, Long replyId, String userId);

    ReplyReadListResponse readReplyLatest10(Long commentId, int index);

    ReplyDeleteResponse deActive(Long replyId, String userId);

}
