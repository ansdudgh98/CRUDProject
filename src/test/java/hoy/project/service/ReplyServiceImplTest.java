package hoy.project.service;

import hoy.project.api.controller.dto.request.form.ReplyForm;
import hoy.project.api.controller.dto.response.reply.ReplyReadListResponse;
import hoy.project.api.controller.dto.response.reply.ReplyReadResponse;
import hoy.project.api.controller.dto.response.reply.ReplyWriteAndEditResponse;
import hoy.project.domain.Article;
import hoy.project.domain.Comment;
import hoy.project.domain.Reply;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReplyServiceImplTest extends ServiceTest {

    Article article;
    Comment comment;

    @BeforeEach
    public void initArticleAndComment() {
        article = articleRepository.save(new Article("테스트 게시물", "테스트 내용", testAccount));
        comment = commentRepository.save(new Comment("테스트 댓글", testAccount, article));
    }

    @Test
    @DisplayName("대댓글 생성 성공 테스트")
    public void replyWriteTest() {
        ReplyForm form = new ReplyForm("테스트 대댓글");
        ReplyWriteAndEditResponse post = replyService.writeReply(form, comment.getId(), testAccount.getUserId());

        Reply findReply = replyRepository.findReplyById(post.getId());

        assertThat(post.getId()).isEqualTo(findReply.getId());
        assertThat(findReply.getContent()).isEqualTo(form.getContent());
        assertThat(findReply.getAccount().getId()).isEqualTo(testAccount.getId());
    }

    @Test
    @DisplayName("대댓글 생성 실패 테스트 - 댓글이 없는 번호일 때")
    public void replyWriteFailTestNotExistCommentId() {
        ReplyForm form = new ReplyForm("테스트 대댓글");
        assertThrows(IllegalArgumentException.class, () -> {
            replyService.writeReply(form, Long.MAX_VALUE, testAccount.getUserId());
        });
    }

    @Test
    @DisplayName("대댓글 생성 실패 테스트 - 대댓글과 account가 둘 다 없는 번호 일 때")
    public void replyWriteFailTestNotExistCommentAndAccount() {
        ReplyForm form = new ReplyForm("테스트 대댓글");
        assertThrows(IllegalArgumentException.class, () -> {
            replyService.writeReply(form, Long.MAX_VALUE, null);
        });
    }

    @Test
    @DisplayName("대댓글 수정 성공 테스트")
    public void editTest() {
        ReplyForm form = new ReplyForm("대댓글 수정 테스트 입니다.");
        Reply reply = new Reply("테스트 댓글 입니다.", comment, testAccount);

        replyRepository.save(reply);

        ReplyWriteAndEditResponse response = replyService.editReply(form, reply.getId(), testAccount.getUserId());
        Reply findReply = replyRepository.findReplyById(response.getId());

        assertThat(response.getId()).isEqualTo(reply.getId());
        assertThat(findReply.getContent()).isEqualTo(form.getContent());
    }

    @Test
    @DisplayName("대댓글 수정 실패 테스트 - 댓글 번호가 다를 때")
    public void editFailTestDiffCommentId() {
        ReplyForm form = new ReplyForm("대댓글 수정 테스트 입니다.");

        Reply reply = new Reply("테스트 댓글 입니다.", comment, testAccount);

        replyRepository.save(reply);

        assertThrows(IllegalArgumentException.class, () -> {
            replyService.editReply(form, Long.MAX_VALUE, testAccount.getUserId());
        });
    }

    @Test
    @DisplayName("대댓글 수정 실패 테스트 - account userid가 다를때")
    public void editFailTestDiffAccountId() {
        ReplyForm form = new ReplyForm("대댓글 수정 테스트 입니다.");

        Reply reply = new Reply("테스트 댓글 입니다.", comment, testAccount);

        replyRepository.save(reply);

        assertThrows(IllegalArgumentException.class, () -> {
            replyService.editReply(form, comment.getId(), null);
        });
    }

    @Test
    @DisplayName("대댓글 수정 실패 테스트 - 작성자,article id 둘다 다를때")
    public void editFailTestDiffAuthorAndArticleId() {
        ReplyForm form = new ReplyForm("대댓글 수정 테스트 입니다.");

        Reply reply = new Reply("테스트 댓글 입니다.", comment, testAccount);

        replyRepository.save(reply);

        assertThrows(IllegalArgumentException.class, () -> {
            replyService.editReply(form, comment.getId(), null);
        });
    }

    @Test
    @DisplayName("대댓글 삭제 성공 테스트")
    public void deleteTest() {
        Reply reply = new Reply("테스트 댓글 입니다.", comment, testAccount);

        replyRepository.save(reply);

        replyService.deActive(reply.getId(), testAccount.getUserId());

        Reply findReply = replyRepository.findReplyById(reply.getId());

        assertThat(findReply.getActive()).isEqualTo(0);
    }

    @Test
    @DisplayName("대댓글 삭제 실패 테스트 - 틀린 아이디 ")
    public void deleteFailTestDiffAccount() {
        Reply reply = new Reply("테스트 댓글 입니다.", comment, testAccount);

        replyRepository.save(reply);

        assertThrows(IllegalArgumentException.class, () -> {
            replyService.deActive(reply.getId(), null);
        });
    }

    @Test
    @DisplayName("최신순 대댓글 10개 읽기 테스트 - 다음 대댓글이 있을때")
    public void readsReplyListTestIfExistNextReply() {
        List<Reply> list = new ArrayList<>();

        for (int i = 0; i < 35; i++) {
            list.add(new Reply("테스트 대댓글 입니다. " + i + " 입니다.", comment, testAccount));
        }

        replyRepository.saveAll(list);

        ReplyReadListResponse replyReadListResponse = replyService.readReplyLatest10(comment.getId(), 0);
        List<ReplyReadResponse> replyList = replyReadListResponse.getReplyReadResponses();

        assertThat(replyReadListResponse.getHasNext()).isTrue();
        assertThat(replyList.size()).isEqualTo(10);


        assertThat(replyList.get(0).getId()).isEqualTo(list.get(replyList.size() - 1).getId());
        assertThat(replyList.get(replyList.size() - 1).getId()).isEqualTo(list.get(0).getId());
        assertThat(replyList.size()).isEqualTo(10);
    }


    @Test
    @DisplayName("최신순 대댓글 10개 읽기 테스트 - 다음 게시물이 없을 때")
    public void readsReplyListTestIfNotExistNextReply() {
        List<Reply> list = new ArrayList<>();

        for (int i = 0; i < 35; i++) {
            list.add(new Reply("테스트 대댓글 입니다. " + i + " 입니다.", comment, testAccount));
        }

        replyRepository.saveAll(list);

        ReplyReadListResponse replyReadListResponse = replyService.readReplyLatest10(comment.getId(), 3);
        List<ReplyReadResponse> replyList = replyReadListResponse.getReplyReadResponses();

        assertThat(replyReadListResponse.getHasNext()).isFalse();

        assertThat(replyList.size()).isEqualTo(5);

    }


    @Test
    @DisplayName("댓글 읽기 성공 테스트 - 인덱스의 번호가 허용되는 인덱스의 수를 초과하였을때")
    public void readsReplyListTestOverIndex() {
        List<Reply> list = new ArrayList<>();

        for (int i = 0; i < 35; i++) {
            list.add(new Reply("테스트 대댓글 입니다. " + i + " 입니다.", comment, testAccount));
        }

        replyRepository.saveAll(list);

        ReplyReadListResponse replyReadListResponse = replyService.readReplyLatest10(comment.getId(), 4);

        assertThat(replyReadListResponse.getHasNext()).isFalse();

        List<ReplyReadResponse> replyReadResponses = replyReadListResponse.getReplyReadResponses();

        assertThat(replyReadResponses.size()).isEqualTo(0);
    }
}
