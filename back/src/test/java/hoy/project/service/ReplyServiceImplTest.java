package hoy.project.service;

import hoy.project.api.controller.dto.request.form.ReplyForm;
import hoy.project.api.controller.dto.response.reply.ReplyReadListResponse;
import hoy.project.api.controller.dto.response.reply.ReplyReadResponse;
import hoy.project.api.controller.dto.response.reply.ReplyWriteAndEditResponse;
import hoy.project.domain.Account;
import hoy.project.domain.Article;
import hoy.project.domain.Comment;
import hoy.project.domain.Reply;
import hoy.project.repository.ReplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class ReplyServiceImplTest {

    @Autowired
    ReplyService replyService;

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    EntityManager em;

    Account account = new Account("test1","123","123");
    Article article = new Article("테스트 게시물1","테스트 게시물 콘텐츠1",account);
    Comment comment = new Comment("테스트 게시물 댓글1",account,article);

    @BeforeEach
    public void init(){
        em.persist(account);
        em.persist(article);
        em.persist(comment);

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("대댓글 생성 성공 테스트")
    public void replyWriteTest1() {
        ReplyForm form = new ReplyForm("테스트 대댓글");
        ReplyWriteAndEditResponse post = replyService.writeReply(form, comment.getId(), account.getUserId());

        Reply findReply = replyRepository.findReplyById(post.getId());

        assertThat(post.getId()).isEqualTo(findReply.getId());
        assertThat(findReply.getContent()).isEqualTo(form.getContent());
        assertThat(findReply.getAccount().getId()).isEqualTo(account.getId());
    }

    @Test
    @DisplayName("대댓글 생성 실패 테스트 - 댓글이 없는 번호일 때")
    public void replyWriteTest2() {
        ReplyForm form = new ReplyForm("테스트 대댓글");
        assertThrows(IllegalArgumentException.class, () -> {
            replyService.writeReply(form, Long.MAX_VALUE, account.getUserId());
        });
    }

    @Test
    @DisplayName("대댓글 생성 실패 테스트 - 대댓글과 account가 둘 다 없는 번호 일 때")
    public void replyWriteTest3(){
        ReplyForm form = new ReplyForm("테스트 대댓글");
        assertThrows(IllegalArgumentException.class, () -> {
            replyService.writeReply(form, Long.MAX_VALUE, null);
        });
    }

    @Test
    @DisplayName("댓글 수정 성공 테스트")
    public void editTest1() {
        ReplyForm form = new ReplyForm("대댓글 수정 테스트 입니다.");
        Reply reply = new Reply("테스트 댓글 입니다.", comment, account);

        em.persist(reply);
        em.flush();
        em.clear();

        ReplyWriteAndEditResponse response = replyService.editReply(form, reply.getId(), account.getUserId());
        Reply findReply = replyRepository.findReplyById(response.getId());

        assertThat(response.getId()).isEqualTo(reply.getId());
        assertThat(findReply.getContent()).isEqualTo(form.getContent());
    }

    @Test
    @DisplayName("대댓글 수정 실패 테스트 - 댓글 번호가 다를 때")
    public void editTest2() {
        ReplyForm form = new ReplyForm("대댓글 수정 테스트 입니다.");

        Reply reply = new Reply("테스트 댓글 입니다.", comment, account);

        em.persist(reply);
        em.flush();
        em.clear();

        assertThrows(IllegalArgumentException.class, () -> {
            replyService.editReply(form, Long.MAX_VALUE, account.getUserId());
        });
    }

    @Test
    @DisplayName("대 댓글 수정 실패 테스트 - account userid가 다를때")
    public void editTest3() {
        ReplyForm form = new ReplyForm("대댓글 수정 테스트 입니다.");

        Reply reply = new Reply("테스트 댓글 입니다.", comment, account);

        em.persist(reply);
        em.flush();
        em.clear();

        assertThrows(IllegalArgumentException.class, () -> {
            replyService.editReply(form, comment.getId(), null);
        });
    }

    @Test
    @DisplayName("댓글 수정 실패 테스트 - 작성자,article id 둘다 다를때")
    public void editTest4() {
        ReplyForm form = new ReplyForm("대댓글 수정 테스트 입니다.");

        Reply reply = new Reply("테스트 댓글 입니다.", comment, account);

        em.persist(reply);
        em.flush();
        em.clear();

        assertThrows(IllegalArgumentException.class, () -> {
            replyService.editReply(form, comment.getId(), null);
        });
    }

    @Test
    @DisplayName("댓글 삭제 성공 테스트")
    public void deleteTest1() {
        Reply reply = new Reply("테스트 댓글 입니다.", comment, account);

        em.persist(reply);
        em.flush();
        em.clear();


        replyService.deActive(reply.getId(), account.getUserId());

        Reply findReply = replyRepository.findReplyById(reply.getId());

        assertThat(findReply.getActive()).isEqualTo(0);
    }

    @Test
    @DisplayName("댓글 삭제 실패 테스트 - 틀린 아이디 ")
    public void deleteTest2() {
        Reply reply = new Reply("테스트 댓글 입니다.", comment, account);

        em.persist(reply);
        em.flush();
        em.clear();


        assertThrows(IllegalArgumentException.class, () -> {
            replyService.deActive(reply.getId(), null);
        });
    }

    @Test
    @DisplayName("최신순 댓글 10개 읽기 테스트 - 다음 게시물이 있을때")
    public void readTest1() {
        List<Reply> list = new ArrayList<>();

        for (int i = 0; i < 35; i++) {
            list.add(new Reply("테스트 대댓글 입니다. "+i+" 입니다.",comment,account));
        }

        for(Reply reply : list){
            em.persist(reply);
        }

        em.flush();
        em.clear();

        ReplyReadListResponse replyReadListResponse = replyService.readReplyLatest10(comment.getId(), 0);
        List<ReplyReadResponse> replyList = replyReadListResponse.getReplyReadResponses();

        assertThat(replyReadListResponse.getHasNext()).isTrue();
        assertThat(replyList.size()).isEqualTo(10);



        assertThat(replyList.get(0).getId()).isEqualTo(list.get(replyList.size()-1).getId());
        assertThat(replyList.get(replyList.size()-1).getId()).isEqualTo(list.get(0).getId());
        assertThat(replyList.size()).isEqualTo(10);
    }


    @Test
    @DisplayName("최신순 댓글 10개 읽기 테스트 - 다음 게시물이 없을 때")
    public void readTest2() {
        List<Reply> list = new ArrayList<>();

        for (int i = 0; i < 35; i++) {
            list.add(new Reply("테스트 대댓글 입니다. "+i+" 입니다.",comment,account));
        }

        for(Reply reply : list){
            em.persist(reply);
        }

        em.flush();
        em.clear();

        ReplyReadListResponse replyReadListResponse = replyService.readReplyLatest10(comment.getId(), 3);
        List<ReplyReadResponse> replyList = replyReadListResponse.getReplyReadResponses();

        assertThat(replyReadListResponse.getHasNext()).isFalse();

        assertThat(replyList.size()).isEqualTo(5);

    }


    @Test
    @DisplayName("댓글 읽기 성공 테스트 - 인덱스의 번호가 허용되는 인덱스의 수를 초과하였을때")
    public void readTest3(){
        List<Reply> list = new ArrayList<>();

        for (int i = 0; i < 35; i++) {
            list.add(new Reply("테스트 대댓글 입니다. "+i+" 입니다.",comment,account));
        }

        for(Reply reply : list){
            em.persist(reply);
        }

        em.flush();
        em.clear();


        ReplyReadListResponse replyReadListResponse = replyService.readReplyLatest10(comment.getId(), 4);

        assertThat(replyReadListResponse.getHasNext()).isFalse();

        List<ReplyReadResponse> replyReadResponses = replyReadListResponse.getReplyReadResponses();

        assertThat(replyReadResponses.size()).isEqualTo(0);
    }
}
