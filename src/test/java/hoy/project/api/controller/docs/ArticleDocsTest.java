package hoy.project.api.controller.docs;

import hoy.project.api.controller.dto.request.form.ArticleCreateForm;
import hoy.project.api.controller.dto.request.form.ArticleEditForm;
import hoy.project.api.controller.session.SessionConst;
import hoy.project.domain.Article;
import hoy.project.repository.ArticleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ArticleDocsTest extends DocsControllerTest {

    Article testArticle;

    @Autowired
    ArticleRepository articleRepository;

    @BeforeEach
    @Transactional
    public void init() {
        testArticle = articleRepository.save(new Article("테스트 게시물1", "테스트 내용1", account));
        session.setAttribute(SessionConst.attributeName, account.getUserId());
    }

    @AfterEach
    public void after() {
        articleRepository.deleteAll();
    }

    @Test
    @DisplayName("[DOCS][DELETE]게시물 삭제 Rest Docs 적용")
    public void articleDeleteRestDocs() throws Exception {
        articleRepository.save(testArticle);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/article/delete/{id}", testArticle.getId())
                        .session(session))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("articledelete", pathParameters(
                        parameterWithName("id").description("삭제하고자 하는 게시물 Id")
                )));
    }

    @Test
    @DisplayName("[DOCS][ARTICLE] 게시물 작성 Rest DOCS 적용")
    public void articlePostRestDocs() throws Exception {
        ArticleCreateForm articleCreateForm = new ArticleCreateForm("테스트 게시물1", "내용1");

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/article/post")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleCreateForm)))
                .andExpect(status().isOk())
                .andDo(document("articlepost", requestFields(
                        fieldWithPath("title").description("제목입력 \n validation: 빈칸은 불가능 하며 100자 이내여야한다."),
                        fieldWithPath("content").description("내용입력 \n validation: 빈칸 입력 불가능"))))
                .andDo(document("articlepost", responseFields(
                        fieldWithPath("id").description("만들어진 articleId")
                )));

    }

    @Test
    @DisplayName("[DOCS][ARTICLE] 게시물 읽기 Rest docs 적용")
    public void articleReadRestDocs() throws Exception {

        articleRepository.save(testArticle);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/article/{id}", testArticle.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("articleread", pathParameters(
                        parameterWithName("id").description("읽고자 하는 게시물 ID")
                )));
    }


    @Test
    @DisplayName("[DOCS][ARTICLE] 게시글 수정 REST DOCS 적용")
    public void articleRestDocs() throws Exception {
        articleRepository.save(testArticle);
        ArticleEditForm form = new ArticleEditForm("수정된 게시물", "수정된 게시물 내용");

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/article/edit/{id}", String.valueOf(testArticle.getId()))
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andDo(document("articleEdit", pathParameters(
                        parameterWithName("id").description("수정하고자 하는 게시물 아이디")
                )))
                .andDo(document("articleEdit", requestFields(
                        fieldWithPath("title").description("수정할 제목 validation: 빈칸으로 입력시"),
                        fieldWithPath("content").description("수정할 내용 validation: 내용 입력시")
                )))
                .andDo(document("articleEdit", responseFields(
                        fieldWithPath("id").description("수정 완료된 id")
                )));
    }


}
