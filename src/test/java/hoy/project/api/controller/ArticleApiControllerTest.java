package hoy.project.api.controller;

import hoy.project.api.controller.dto.request.form.ArticleCreateForm;
import hoy.project.api.controller.dto.request.form.ArticleEditForm;
import hoy.project.api.controller.dto.response.CommonErrorResponse;
import hoy.project.api.controller.dto.response.article.ArticleEditResponse;
import hoy.project.api.controller.dto.response.article.ArticleReadResponse;
import hoy.project.api.controller.session.SessionConst;
import hoy.project.domain.Account;
import hoy.project.domain.Article;
import hoy.project.repository.ImageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.FileInputStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ArticleApiControllerTest extends ControllerTest {

    @Autowired
    ImageRepository imageRepository;

    @BeforeEach
    void initData() {
        account = accountRepository.save(new Account("test1", "!test123", "test@gmail.com"));
        testArticle = articleRepository.save(new Article("테스트 제목1", "테스트 내용", account));
        session.setAttribute(SessionConst.attributeName, account.getUserId());
    }

    @AfterEach
    protected void destroy() {
        imageRepository.deleteAllInBatch();
        articleRepository.deleteAllInBatch();
        accountRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("[API][ARTICLE] 게시물 작성 성공 테스트")
    public void articlePostTest1() throws Exception {

        ArticleCreateForm articleCreateForm = new ArticleCreateForm("테스트 게시물1", "내용1");

        mockMvc.perform(post("/api/article/post")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleCreateForm)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("[API][ARTICLE] 게시물 작성 실패 테스트 - 제목이 빈칸일 때")
    public void articlePostTest2() throws Exception {
        ArticleCreateForm articleCreateForm = new ArticleCreateForm("", "내용1");
        CommonErrorResponse validationErrorResponse = new CommonErrorResponse("400", "잘못된 요청 입니다.");
        validationErrorResponse.addValidation("title", "제목을 입력하지 않았습니다.");

        mockMvc.perform(post("/api/article/post")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleCreateForm)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(validationErrorResponse)));
    }

    @Test
    @DisplayName("[API][ARTICLE] 게시물 작성 실패 테스트 - 내용이 빈칸일 때")
    public void articlePostTest3() throws Exception {
        ArticleCreateForm articleCreateForm = new ArticleCreateForm("제목1", "");
        CommonErrorResponse validationErrorResponse = new CommonErrorResponse("400", "잘못된 요청 입니다.");
        validationErrorResponse.addValidation("content", "내용을 입력하지 않았습니다.");

        mockMvc.perform(post("/api/article/post")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleCreateForm)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(validationErrorResponse)));
    }

    @Test
    @DisplayName("[API][ARTICLE] 게시물 작성 실패 테스트 - 제목과 내용이 빈칸일때")
    public void articlePostTest4() throws Exception {
        ArticleCreateForm articleCreateForm = new ArticleCreateForm("", "");
        CommonErrorResponse validationErrorResponse = new CommonErrorResponse("400", "잘못된 요청 입니다.");
        validationErrorResponse.addValidation("title", "제목을 입력하지 않았습니다.");
        validationErrorResponse.addValidation("content", "내용을 입력하지 않았습니다.");

        mockMvc.perform(post("/api/article/post")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleCreateForm)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(validationErrorResponse)));
    }


    @Test
    @DisplayName("[API][ARTICLE] 게시물 읽기 성공 테스트")
    public void articleReadTest1() throws Exception {

        ArticleReadResponse articleReadResponse = new ArticleReadResponse(testArticle.getTitle(), testArticle.getContent(), account.getUserId());

        String url = "/api/article/" + testArticle.getId();

        mockMvc.perform(get(url)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(articleReadResponse)));
    }

    @Test
    @DisplayName("[API][ARTICLE] 게시물 읽기 실패 테스트 - 없는 게시물 아이디 조회")
    public void articleReadTest2() throws Exception {
        String url = "/api/article/" + Long.MAX_VALUE;
        CommonErrorResponse validationErrorResponse = new CommonErrorResponse("400", "존재하지 않는 게시물 번호 입니다.");

        mockMvc.perform(get(url)
                        .session(session))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(validationErrorResponse)));
    }


    @Test
    @DisplayName("[API][ARTICLE] 게시물 수정 성공 테스트")
    public void articleEditTest1() throws Exception {
        ArticleEditForm form = new ArticleEditForm("수정된 게시물", "수정된 게시물 내용");
        ArticleEditResponse articleEditResponse = new ArticleEditResponse(testArticle.getId());


        String url = "/api/article/edit/" + testArticle.getId();

        mockMvc.perform(post(url)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(articleEditResponse)));

        Article fidnArticle = articleRepository.findArticleById(testArticle.getId());

        System.out.println("testArticle : " + testArticle.getId());
        assertThat(fidnArticle.getTitle()).isEqualTo(form.getTitle());
        assertThat(fidnArticle.getContent()).isEqualTo(form.getContent());
    }

    @Test
    @DisplayName("[API][ARTICLE] 게시물 수정 실패 테스트 - 작성자와 다른 account")
    public void articleEditTest2() throws Exception {
        Account account = new Account("newaccount1", "account1", "account@test.com");
        accountRepository.save(account);

        ArticleEditForm form = new ArticleEditForm("수정된 게시물", "수정된 게시물 내용");

        session.clearAttributes();
        session.setAttribute(SessionConst.attributeName, account.getUserId());

        CommonErrorResponse validationErrorResponse = new CommonErrorResponse("400", "게시글을 수정할 권한이 없습니다!");

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
    public void articleEditTest3() throws Exception {
        ArticleEditForm articleEditForm = new ArticleEditForm("", "내용1");
        CommonErrorResponse validationErrorResponse = new CommonErrorResponse("400", "잘못된 요청 입니다.");
        validationErrorResponse.addValidation("title", "제목을 입력하지 않았습니다.");

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
    public void articleEditTest4() throws Exception {
        ArticleEditForm articleEditForm = new ArticleEditForm("제목1", "");
        CommonErrorResponse validationErrorResponse = new CommonErrorResponse("400", "잘못된 요청 입니다.");
        validationErrorResponse.addValidation("content", "내용을 입력하지 않았습니다.");

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
    public void articleEditTest5() throws Exception {
        ArticleEditForm articleEditForm = new ArticleEditForm("", "");
        CommonErrorResponse validationErrorResponse = new CommonErrorResponse("400", "잘못된 요청 입니다.");
        validationErrorResponse.addValidation("title", "제목을 입력하지 않았습니다.");
        validationErrorResponse.addValidation("content", "내용을 입력하지 않았습니다.");

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
    public void articleDeleteTest1() throws Exception {

        String url = "/api/article/delete/" + testArticle.getId();


        mockMvc.perform(get(url)
                        .session(session)
                )
                .andExpect(status().isOk());

        Article findArticle = articleRepository.findArticleById(testArticle.getId());

        assertThat(findArticle.getActive()).isEqualTo(0);
    }

    @Test
    @DisplayName("[API][ARTICLE] 게시물 삭제 실패 테스트 - 작성자와 다른 account")
    public void articleDeleteTest2() throws Exception {
        String url = "/api/article/delete/" + testArticle.getId();

        Account account = new Account("newaccount1", "account1", "account@test.com");
        accountRepository.save(account);

        session.clearAttributes();
        session.setAttribute(SessionConst.attributeName, account.getUserId());

        CommonErrorResponse validationErrorResponse = new CommonErrorResponse("400", "게시글을 삭제할 권한이 없습니다!");


        mockMvc.perform(get(url)
                        .session(session))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(validationErrorResponse)));

        Article findArticle = articleRepository.findArticleById(testArticle.getId());

        assertThat(findArticle.getActive()).isEqualTo(1);
    }

    @Test
    @DisplayName("[API][ARTICLE] 이미지 업로드 성공 테스트")
    void imageUploadControllerTest() throws Exception {
        //given
        String filename = "test.png";
        String testPath = "./src/test/resources/static/";
        MockMultipartFile file = new MockMultipartFile("file", filename, "image/png", new FileInputStream(testPath + filename));

        //when

        //then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/article/imageupload")
                        .file(file)
                        .session(session)
                ).andDo(print())
                .andExpect(status().isOk());
    }
}