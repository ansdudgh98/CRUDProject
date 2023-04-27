package hoy.project.service;

import hoy.project.api.controller.dto.request.form.CommentForm;
import hoy.project.api.controller.dto.response.comment.CommentEditResponse;
import hoy.project.api.controller.dto.response.comment.CommentPostResponse;
import hoy.project.api.controller.dto.response.comment.CommentReadResponse;
import hoy.project.api.controller.dto.response.comment.CommentsReadResponse;
import hoy.project.domain.Account;
import hoy.project.domain.Article;
import hoy.project.domain.Comment;
import hoy.project.repository.CommentRepository;
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

@SpringBootTest
@Transactional
class CommentServiceImplTest {

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    Account account = new Account("test123", "test123", "test@gmail.com");
    Article article = new Article("테스트 게시물1", "테스트 게시물 내용1", account);

    @Autowired
    EntityManager em;

    @BeforeEach
    @Transactional
    public void init() {
        em.persist(account);
        em.persist(article);

        em.clear();
        em.flush();
    }

    @Test
    @DisplayName("댓글 생성 성공 테스트")
    public void postTest1() {
        CommentForm form = new CommentForm("테스트 댓글 입니다.");
        CommentPostResponse post = commentService.post(form, article.getId(), account.getUserId());

        Comment findComment = commentRepository.findCommentById(post.getId());

        assertThat(post.getId()).isEqualTo(findComment.getId());
        assertThat(findComment.getContent()).isEqualTo(form.getContent());
        assertThat(findComment.getId()).isEqualTo(account.getId());
    }

    @Test
    @DisplayName("댓글 생성 실패 테스트 - 게시글이 없는 번호일 때")
    public void postTest2() {
        CommentForm form = new CommentForm("테스트 댓글 입니다.");
        assertThrows(IllegalArgumentException.class, () -> {
            commentService.post(form, Long.MAX_VALUE, account.getUserId());
        });
    }

    @Test
    @DisplayName("댓글 생성 실패 테스트 - 유저 ID가 없는 번호일 때")
    public void postTest4() {
        CommentForm form = new CommentForm("테스트 댓글 입니다.");
        assertThrows(IllegalArgumentException.class, () -> {
            commentService.post(form, article.getId(), null);
        });
    }

    @Test
    @DisplayName("댓글 생성 실패 테스트 - 유저 ID와 게시글 ID가 둘다 없는 번호일 때")
    public void postTest5() {
        CommentForm form = new CommentForm("테스트 댓글 입니다.");
        assertThrows(IllegalArgumentException.class, () -> {
            commentService.post(form, article.getId(), null);
        });
    }

    @Test
    @DisplayName("댓글 수정 테스트")
    public void editTest1() {
        CommentForm form = new CommentForm("댓글 수정 테스트 입니다.");
        Comment comment = new Comment("테스트 댓글 입니다.", account, article);

        em.persist(comment);
        em.flush();
        em.clear();

        CommentEditResponse editResponse = commentService.edit(form, comment.getId(), account.getUserId());
        Comment findComment = commentRepository.findCommentById(editResponse.getId());

        assertThat(editResponse.getId()).isEqualTo(comment.getId());
        assertThat(findComment.getContent()).isEqualTo(form.getContent());
    }

    @Test
    @DisplayName("댓글 수정 실패 테스트 - 게시물 번호가 다를 때")
    public void editTest2() {
        CommentForm form = new CommentForm("댓글 수정 테스트 입니다.");

        Comment comment = new Comment("테스트 댓글 입니다.", account, article);

        em.persist(comment);
        em.flush();
        em.clear();

        assertThrows(IllegalArgumentException.class, () -> {
            commentService.edit(form, Long.MAX_VALUE, account.getUserId());
        });
    }

    @Test
    @DisplayName("댓글 수정 실패 테스트 - account userid가 다를때")
    public void editTest3() {
        CommentForm form = new CommentForm("댓글 수정 테스트 입니다.");

        Comment comment = new Comment("테스트 댓글 입니다.", account, article);

        em.persist(comment);
        em.flush();
        em.clear();

        assertThrows(IllegalArgumentException.class, () -> {
            commentService.edit(form, comment.getId(), null);
        });
    }

    @Test
    @DisplayName("댓글 수정 실패 테스트 - 작성자,article id 둘다 다를때")
    public void editTest4() {
        CommentForm form = new CommentForm("댓글 수정 테스트 입니다.");

        Comment comment = new Comment("테스트 댓글 입니다.", account, article);

        em.persist(comment);
        em.flush();
        em.clear();

        assertThrows(IllegalArgumentException.class, () -> {
            commentService.edit(form, null, null);
        });
    }

    @Test
    @DisplayName("댓글 삭제 성공 테스트")
    public void deleteTest1() {
        Comment comment = new Comment("테스트 댓글 입니다.", account, article);

        em.persist(comment);
        em.flush();
        em.clear();


        commentService.delete(comment.getId(), account.getUserId());

        Comment findComment = em.find(Comment.class, comment.getId());

        assertThat(findComment.getActive()).isEqualTo(0);
    }

    @Test
    @DisplayName("댓글 삭제 실패 테스트 - 틀린 아이디 ")
    public void deleteTest2() {
        Comment comment = new Comment("테스트 댓글 입니다.", account, article);

        em.persist(comment);
        em.flush();
        em.clear();


        assertThrows(IllegalArgumentException.class, () -> {
            commentService.delete(comment.getId(), null);
        });
    }

    @Test
    @DisplayName("최신순 댓글 10개 읽기 테스트 - 다음 게시물이 있을때")
    public void readTest1() {
        List<Comment> list = new ArrayList<>();

        for (int i = 0; i < 35; i++) {
            list.add(new Comment("테스트 게시물 "+i+" 입니다.",account,article));
        }

        for(Comment comment : list){
            em.persist(comment);
        }

        em.flush();
        em.clear();

        CommentsReadResponse commentsReadResponse = commentService.readCommentLatest10(article.getId(), 0);
        List<CommentReadResponse> commentList = commentsReadResponse.getCommentReadResponses();

        assertThat(commentsReadResponse.isHasNext()).isTrue();
        assertThat(commentList.size()).isEqualTo(10);



        assertThat(commentList.get(0).getId()).isEqualTo(list.get(commentList.size()-1).getId());
        assertThat(commentList.get(commentList.size()-1).getId()).isEqualTo(list.get(0).getId());
        assertThat(commentList.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("최신순 댓글 10개 읽기 테스트 - 다음 게시물이 없을 때")
    public void readTest2() {
        List<Comment> list = new ArrayList<>();

        for (int i = 0; i < 35; i++) {
            list.add(new Comment("테스트 게시물 "+i+" 입니다.",account,article));
        }

        for(Comment comment : list){
            em.persist(comment);
        }

        em.flush();
        em.clear();


        CommentsReadResponse commentsReadResponse = commentService.readCommentLatest10(article.getId(), 3);
        List<CommentReadResponse> commentList = commentsReadResponse.getCommentReadResponses();

        assertThat(commentsReadResponse.isHasNext()).isFalse();

        assertThat(commentList.size()).isEqualTo(5);

        assertThat(commentList.get(0).getId()).isEqualTo(list.get((int)(commentList.get(0).getId()-1)).getId());
        assertThat(commentList.get(commentList.size()-1).getId()).isEqualTo(list.get((int)(commentList.get(commentList.size()-1).getId()-1)).getId());
    }

    @Test
    @DisplayName("댓글 읽기 성공 테스트 - 인덱스의 번호가 허용되는 인덱스의 수를 초과하였을때")
    public void readTest3(){
        List<Comment> list = new ArrayList<>();

        for (int i = 0; i < 35; i++) {
            list.add(new Comment("테스트 게시물 "+i+" 입니다.",account,article));
        }

        for(Comment comment : list){
            em.persist(comment);
        }

        em.flush();
        em.clear();

        CommentsReadResponse commentsReadResponse = commentService.readCommentLatest10(article.getId(), 4);

        assertThat(commentsReadResponse.isHasNext()).isFalse();

        List<CommentReadResponse> commentList = commentsReadResponse.getCommentReadResponses();

        assertThat(commentList.size()).isEqualTo(0);
    }
}