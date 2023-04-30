package hoy.project.service;

import hoy.project.api.controller.dto.request.form.ReplyForm;
import hoy.project.api.controller.dto.response.reply.ReplyDeleteResponse;
import hoy.project.api.controller.dto.response.reply.ReplyReadListResponse;
import hoy.project.api.controller.dto.response.reply.ReplyReadResponse;
import hoy.project.api.controller.dto.response.reply.ReplyWriteAndEditResponse;
import hoy.project.domain.Account;
import hoy.project.domain.Comment;
import hoy.project.domain.Reply;
import hoy.project.repository.AccountRepository;
import hoy.project.repository.CommentRepository;
import hoy.project.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;
    private final AccountRepository accountRepository;
    private final CommentRepository commentRepository;

    @Override
    public ReplyWriteAndEditResponse writeReply(ReplyForm replyForm, Long commentId, String userId) {

        Account account = accountRepository.findByUserId(userId);
        Comment comment = commentRepository.findCommentById(commentId);

        if(comment == null){
            throw new IllegalArgumentException("존재하지 않는 댓글에 대댓글을 작성은 불가능 합니다!");
        }

        Reply reply = new Reply(replyForm.getContent(), comment, account);

        replyRepository.save(reply);

        return new ReplyWriteAndEditResponse(reply.getId());
    }

    @Override
    public ReplyWriteAndEditResponse editReply(ReplyForm replyForm, Long replyId, String userId) {

        Account account = accountRepository.findByUserId(userId);
        Reply reply = replyRepository.findReplyById(replyId);


        if(reply == null){
            throw new IllegalArgumentException("존재하지 않는 대댓글입니다.");
        }

        if (!reply.getAccount().equals(account)) {
            throw new IllegalArgumentException("대댓글을 수정할 권한이 없습니다.");
        }

        reply.changeReply(replyForm.getContent());

        return new ReplyWriteAndEditResponse(reply.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public ReplyReadListResponse readReplyLatest10(Long commentId, int index) {

        Slice<Reply> sliceReplies = replyRepository.findSliceReplies(commentId, PageRequest.of(index, 10));

        List<ReplyReadResponse> listReply = sliceReplies.getContent().stream()
                .map(reply -> new ReplyReadResponse(reply.getId(), reply.getContent(), reply.getAccount().getUserId(), reply.getCreatedDate(), reply.getLastModifiedDate()))
                .sorted(Comparator.comparing(ReplyReadResponse::getModifiedDateTime).reversed())
                .collect(Collectors.toList());

        return new ReplyReadListResponse(sliceReplies.hasNext(), listReply);
    }

    @Override
    public ReplyDeleteResponse deActive(Long replyId, String userId) {
        Account account = accountRepository.findByUserId(userId);
        Reply reply = replyRepository.findReplyById(replyId);

        if (!reply.getAccount().equals(account)) {
            throw new IllegalArgumentException("게시글을 삭제할 권한이 없습니다.");
        }

        reply.deActive();
        return new ReplyDeleteResponse();
    }

}
