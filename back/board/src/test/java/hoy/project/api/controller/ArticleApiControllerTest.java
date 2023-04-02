package hoy.project.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hoy.project.api.controller.dto.request.form.ArticleCreateForm;
import hoy.project.api.controller.dto.request.form.ArticleEditForm;
import hoy.project.api.controller.dto.response.CommonErrorResponse;
import hoy.project.api.controller.dto.response.article.ArticleDeleteResponse;
import hoy.project.api.controller.dto.response.article.ArticleEditResponse;
import hoy.project.api.controller.dto.response.article.ArticlePostResponse;
import hoy.project.api.controller.dto.response.article.ArticleReadResponse;
import hoy.project.api.controller.session.SessionConst;
import hoy.project.domain.Account;
import hoy.project.domain.Article;
import hoy.project.repository.AccountRepository;
import hoy.project.repository.ArticleRepository;
import hoy.project.service.ArticleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




@SpringBootTest(webEnvironment =SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class ArticleApiControllerTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ArticleService articleService;

    ObjectMapper objectMapper = new ObjectMapper();

    MockHttpSession session;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ArticleRepository articleRepository;

    Account testAccount = Account.builder()
            .userId("testid123")
            .password("testpw123")
            .email("testemail@test.com").build();

    Article testArticle = new Article("게시물1", "내용1", testAccount);

    @BeforeEach
    public void init(){
        accountRepository.save(testAccount);
    }

    @BeforeEach
    public void initSession(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        session = new MockHttpSession();
        session.setAttribute(SessionConst.ACCOUNT,testAccount.getUserId());
    }

    @AfterEach
    public void after(){
        articleRepository.deleteAll();
        accountRepository.deleteAll();

    }

    @Test
    @DisplayName("[API][ARTICLE] 게시물 작성 성공 테스트")
    public void articlePostTest1() throws Exception{
        ArticleCreateForm articleCreateForm = new ArticleCreateForm("테스트 게시물1","내용1");
        ArticlePostResponse articlePostResponse = new ArticlePostResponse(1L);
        mockMvc.perform(post("/api/article/post")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(articleCreateForm)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(articlePostResponse)));
    }

    @Test
    @DisplayName("[API][ARTICLE] 게시물 작성 실패 테스트 - 제목이 빈칸일 때")
    public void articlePostTest2() throws Exception{
        ArticleCreateForm articleCreateForm = new ArticleCreateForm("","내용1");
        CommonErrorResponse validationErrorResponse = new CommonErrorResponse("400","잘못된 요청 입니다.");
        validationErrorResponse.addValidation("title","제목을 입력하지 않았습니다.");

        mockMvc.perform(post("/api/article/post")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleCreateForm)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(validationErrorResponse)));
    }

    @Test
    @DisplayName("[API][ARTICLE] 게시물 작성 실패 테스트 - 내용이 빈칸일 때")
    public void articlePostTest3() throws Exception{
        ArticleCreateForm articleCreateForm = new ArticleCreateForm("제목1","");
        CommonErrorResponse validationErrorResponse = new CommonErrorResponse("400","잘못된 요청 입니다.");
        validationErrorResponse.addValidation("content","내용을 입력하지 않았습니다.");

        mockMvc.perform(post("/api/article/post")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleCreateForm)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(validationErrorResponse)));
    }

    @Test
    @DisplayName("[API][ARTICLE] 게시물 작성 실패 테스트 - 제목과 내용이 빈칸일때")
    public void articlePostTest4() throws Exception{
        ArticleCreateForm articleCreateForm = new ArticleCreateForm("","");
        CommonErrorResponse validationErrorResponse = new CommonErrorResponse("400","잘못된 요청 입니다.");
        validationErrorResponse.addValidation("title","제목을 입력하지 않았습니다.");
        validationErrorResponse.addValidation("content","내용을 입력하지 않았습니다.");

        mockMvc.perform(post("/api/article/post")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleCreateForm)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(validationErrorResponse)));
    }

    @Test
    @DisplayName("[API][ARTICLE] 게시물 읽기 성공 테스트")
    public void articleReadTest1() throws Exception{

        ArticleReadResponse articleReadResponse = new ArticleReadResponse("게시물1","내용1","testid123");
        articleRepository.save(testArticle);

        String url = "/api/article/" + testArticle.getId();

        mockMvc.perform(get(url)
                .session(session))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(articleReadResponse)));
    }

    @Test
    @DisplayName("[API][ARTICLE] 게시물 읽기 실패 테스트 - 없는 게시물 아이디 조회")
    public void articleReadTest2() throws Exception{
        String url = "/api/article/" + Long.MAX_VALUE;
        CommonErrorResponse validationErrorResponse = new CommonErrorResponse("400","존재하지 않는 게시물 번호 입니다.");

        mockMvc.perform(get(url)
                        .session(session))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(validationErrorResponse)))
                .andDo(print());
    }

    @Test
    @DisplayName("[API][ARTICLE] 게시물 수정 성공 테스트")
    public void articleEditTest1() throws Exception{
        articleRepository.save(testArticle);
        ArticleEditForm form = new ArticleEditForm("수정된 게시물","수정된 게시물 내용");
        ArticleEditResponse articleEditResponse = new ArticleEditResponse(testArticle.getId());

        String url = "/api/article/edit/" + testArticle.getId();

        mockMvc.perform(post(url)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(articleEditResponse)));

        Article fidnArticle = articleRepository.findArticleById(testArticle.getId());

        assertThat(fidnArticle.getTitle()).isEqualTo(form.getTitle());
        assertThat(fidnArticle.getContent()).isEqualTo(form.getContent());
    }

    @Test
    @DisplayName("[API][ARTICLE] 게시물 수정 실패 테스트 - 작성자와 다른 account")
    public void articleEditTest2() throws Exception{
        Account account = new Account("newaccount1","account1","account@test.com");
        accountRepository.save(account);
        articleRepository.save(testArticle);

        ArticleEditForm form = new ArticleEditForm("수정된 게시물","수정된 게시물 내용");

        session.clearAttributes();
        session.setAttribute(SessionConst.ACCOUNT,account.getUserId());

        CommonErrorResponse validationErrorResponse = new CommonErrorResponse("400","게시글을 수정할 권한이 없습니다!");

        String url = "/api/article/edit/" + testArticle.getId();

        mockMvc.perform(post(url)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(validationErrorResponse)));
    }

    @Test
    @DisplayName("[API][ARTICLE] 게시물 수정 실패 테스트 - 제목이 빈칸일 때")
    public void articleEditTest3() throws Exception{
        ArticleEditForm articleEditForm = new ArticleEditForm("","내용1");
        CommonErrorResponse validationErrorResponse = new CommonErrorResponse("400","잘못된 요청 입니다.");
        validationErrorResponse.addValidation("title","제목을 입력하지 않았습니다.");

        String url = "/api/article/edit/" + testArticle.getId();

        mockMvc.perform(post(url)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleEditForm)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(validationErrorResponse)));
    }

    @Test
    @DisplayName("[API][ARTICLE] 게시물 수정 실패 테스트 - 내용이 빈칸일 때")
    public void articleEditTest4() throws Exception{
        ArticleEditForm articleEditForm = new ArticleEditForm("제목1","");
        CommonErrorResponse validationErrorResponse = new CommonErrorResponse("400","잘못된 요청 입니다.");
        validationErrorResponse.addValidation("content","내용을 입력하지 않았습니다.");

        String url = "/api/article/edit/" + testArticle.getId();

        mockMvc.perform(post(url)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleEditForm)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(validationErrorResponse)));
    }

    @Test
    @DisplayName("[API][ARTICLE] 게시물 수정 실패 테스트 - 제목과 내용이 빈칸일 때")
    public void articleEditTest5() throws Exception{
        ArticleEditForm articleEditForm = new ArticleEditForm("","");
        CommonErrorResponse validationErrorResponse = new CommonErrorResponse("400","잘못된 요청 입니다.");
        validationErrorResponse.addValidation("title","제목을 입력하지 않았습니다.");
        validationErrorResponse.addValidation("content","내용을 입력하지 않았습니다.");

        String url = "/api/article/edit/" + testArticle.getId();

        mockMvc.perform(post(url)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleEditForm)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(validationErrorResponse)));
    }

    @Test
    @DisplayName("[API][ARTICLE] 게시물 삭제 성공 테스트")
    public void articleDeleteTest1() throws Exception{
        articleRepository.save(testArticle);
        String url = "/api/article/delete/" + testArticle.getId();

        ArticleDeleteResponse articleDeleteResponse = new ArticleDeleteResponse("삭제가 완료되었습니다.");

        mockMvc.perform(get(url)
                .session(session))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(articleDeleteResponse)));

        Article findArticle = articleRepository.findArticleById(testArticle.getId());

        assertThat(findArticle.getActive()).isEqualTo(0);
    }

    @Test
    @DisplayName("[API][ARTICLE] 게시물 삭제 실패 테스트 - 작성자와 다른 account")
    public void articleDeleteTest2() throws Exception{
        articleRepository.save(testArticle);
        String url = "/api/article/delete/" + testArticle.getId();

        Account account = new Account("newaccount1","account1","account@test.com");
        accountRepository.save(account);
        articleRepository.save(testArticle);

        session.clearAttributes();
        session.setAttribute(SessionConst.ACCOUNT,account.getUserId());

        CommonErrorResponse validationErrorResponse = new CommonErrorResponse("400","게시글을 삭제할 권한이 없습니다!");


        mockMvc.perform(get(url)
                        .session(session))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(validationErrorResponse)));

        Article findArticle = articleRepository.findArticleById(testArticle.getId());

        assertThat(findArticle.getActive()).isEqualTo(1);
    }






}