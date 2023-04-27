package hoy.project.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hoy.project.api.controller.dto.request.form.CommentForm;
import hoy.project.api.controller.dto.response.comment.CommentPostResponse;
import hoy.project.api.controller.session.SessionConst;
import hoy.project.domain.Account;
import hoy.project.domain.Article;
import hoy.project.domain.Comment;
import hoy.project.repository.AccountRepository;
import hoy.project.repository.ArticleRepository;
import hoy.project.repository.CommentRepository;
import hoy.project.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment =SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class CommentApiControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    AccountService accountService;

    ObjectMapper objectMapper = new ObjectMapper();
    MockHttpSession session = new MockHttpSession();

    @Autowired
    EntityManager em;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CommentRepository commentRepository;

    Account account;
    Article article;
    @BeforeEach
    public void init(){
        account = new Account("test123","test123","test@gmail.com");
        article = new Article("테스트 게시물1입니다.","테스트 게시물 내용 입니다.",account);

        accountRepository.save(account);
        articleRepository.save(article);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        session = new MockHttpSession();
        session.setAttribute(SessionConst.ACCOUNT,account.getUserId());

    }


    @Test
    @DisplayName("[API][Comment]댓글 작성 성공 테스트")
    public void commentPostTest1() throws Exception {
        CommentForm commentForm = new CommentForm("테스트 게시물 입니다.");

        ResultActions result = mockMvc.perform(post("/api/comment/?article=" + article.getId())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentForm)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("[API][Comment]댓글 작성 실패 테스트 - 게시물이 없을때")
    public void commentPostTest2() throws Exception {
        CommentForm commentForm = new CommentForm("테스트 게시물 입니다.");

        ResultActions result = mockMvc.perform(post("/api/comment/?article=" + Long.MAX_VALUE)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentForm)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("[API][Comment]댓글 수정 성공 테스트")
    public void commentEditTest1() throws Exception {
        Comment comment = new Comment("테스트 댓글 입니다.",account,article);
        commentRepository.save(comment);

        CommentForm commentForm = new CommentForm("테스트 수정 댓글 입니다.");
        CommentPostResponse commentPostResponse = new CommentPostResponse(comment.getId());

        mockMvc.perform(post("/api/comment/edit/" + comment.getId())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentForm)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(commentPostResponse)));

        Comment findComment = commentRepository.findCommentById(commentPostResponse.getId());
        assertThat(commentForm.getContent()).isEqualTo(findComment.getContent());
    }

    @Test
    @DisplayName("[API][Comment]댓글 수정 실패 테스트 - 다른 계정일 때")
    public void commentEditTest2() throws Exception {
        Comment comment = new Comment("테스트 댓글 입니다.",account,article);
        commentRepository.save(comment);

        CommentForm commentForm = new CommentForm("테스트 수정 댓글 입니다.");

        session.setAttribute(SessionConst.ACCOUNT,"mockaccount");

        mockMvc.perform(post("/api/comment/edit/" + comment.getId())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentForm)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[API][Comment]댓글 수정 실패 테스트 - 존재하지 않는 댓글 일 때")
    public void commentEditTest3() throws Exception {
        Comment comment = new Comment("테스트 댓글 입니다.",account,article);
        commentRepository.save(comment);

        CommentForm commentForm = new CommentForm("테스트 수정 댓글 입니다.");

        mockMvc.perform(post("/api/comment/edit/" + Long.MAX_VALUE)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentForm)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[API][Comment]댓글 삭제 성공 테스트")
    public void commentDeleteTest1() throws Exception {
        Comment comment = new Comment("테스트 댓글 입니다.",account,article);
        commentRepository.save(comment);

        mockMvc.perform(get("/api/comment/delete/" + comment.getId())
                        .session(session))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("[API][Comment]댓글 삭제 실패 테스트 - 계정 권한이 없을 때")
    public void commentDeleteTest2() throws Exception {
        Comment comment = new Comment("테스트 댓글 입니다.",account,article);
        commentRepository.save(comment);

        session.setAttribute(SessionConst.ACCOUNT,"account");

        mockMvc.perform(get("/api/comment/delete/" + comment.getId())
                        .session(session))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[API][Comment]댓글 삭제 실패 테스트 - 없는 댓글일 때")
    public void commentDeleteTest3() throws Exception {
        Comment comment = new Comment("테스트 댓글 입니다.",account,article);
        commentRepository.save(comment);

        mockMvc.perform(get("/api/comment/delete/" + Long.MAX_VALUE)
                        .session(session))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[API][Comment]댓글 10개 읽기 성공 테스트 - 다음 댓글이 존재할때")
    public void ListCommentReadTest1() throws Exception {
        for(int i=0;i<15;i++){
            commentRepository.save(new Comment("테스트 게시물"+i,account,article));
        }

        mockMvc.perform(get("/api/comment/0?article=0"))
                .andExpect(status().isOk())
                .andDo(print());
    }





}