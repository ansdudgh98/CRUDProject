package hoy.project.service;

import hoy.project.api.controller.dto.request.form.ArticleCreateForm;
import hoy.project.api.controller.dto.request.form.ArticleEditForm;
import hoy.project.api.controller.dto.response.article.ArticleEditResponse;
import hoy.project.api.controller.dto.response.article.ArticlePostResponse;
import hoy.project.api.controller.dto.response.article.ArticleReadResponse;
import hoy.project.domain.Account;
import hoy.project.domain.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ArticleServiceImplTest {


    @Autowired
    ArticleService articleService;

    @Autowired
    EntityManager em;

    Account account = new Account("test123", "test123", "test123@test.com");
    Article article = new Article("테스트 게시물1", "테스트 게시물 내용1", account);

    @BeforeEach
    public void init(){
        em.persist(account);
        em.persist(article);
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("게시물 생성 성공 테스트")
    public void createArticleTest1(){

        Account account = new Account("account1","account1","account1@test.com");
        Article newArticle = new Article("테스트 게시물1","테스트 내용1",account);

        ArticlePostResponse articlePostResponse = articleService.createArticle(new ArticleCreateForm(
                newArticle.getTitle(), newArticle.getContent()
        ), account.getUserId());

        Article findArticle = em.find(Article.class, articlePostResponse.getId());

        assertThat(newArticle.getTitle()).isEqualTo(findArticle.getTitle());
        assertThat(newArticle.getContent()).isEqualTo(findArticle.getContent());
        assertThat(newArticle.getAccount().getId()).isEqualTo(account.getId());
    }

    @Test
    @DisplayName("게시물 생성 실패 테스트 - form의 제목 값 null")
    public void createArticleTest2(){
        ArticleCreateForm articleCreateForm = new ArticleCreateForm(null, "123456");
        Account account = new Account("account1","account1","account1@test.com");
        assertThrows(DataIntegrityViolationException.class,()->articleService.createArticle(articleCreateForm,account.getUserId()));
    }

    @Test
    @DisplayName("게시물 생성 실패 테스트 - form의 내용 값이 비었을 때")
    public void createArticleTest3(){
        ArticleCreateForm articleCreateForm = new ArticleCreateForm("123123", null);
        Account account = new Account("account1","account1","account1@test.com");
        assertThrows(DataIntegrityViolationException.class,()->articleService.createArticle(articleCreateForm,account.getUserId()));
    }

    @Test
    @DisplayName("게시물 읽기 성공 테스트")
    public void readArticleTest1(){
        ArticleReadResponse articleReadResponse = articleService.readArticle(article.getId());
        assertThat(articleReadResponse.getUserId()).isEqualTo(article.getAccount().getUserId());
        assertThat(articleReadResponse.getContent()).isEqualTo(article.getContent());
        assertThat(articleReadResponse.getTitle()).isEqualTo(article.getTitle());
    }

    @Test
    @DisplayName("게시물 읽기 실패 테스트 - 없는 게시물 번호 조회")
    public void readArticleTest2(){
        assertThrows(NoSuchElementException.class,()->articleService.readArticle(Long.MAX_VALUE));
    }

    @Test
    @DisplayName("게시물 수정 성공 테스트")
    public void editArticleTest1() {
        ArticleEditForm articleEditForm = new ArticleEditForm("수정 테스트 게시물1", "수정 테스트");
        ArticleEditResponse articleEditResponse = articleService.editArticle(articleEditForm, article.getId(), account.getUserId());

        Article findEditArticle = em.find(Article.class, articleEditResponse.getId());

        assertThat(articleEditForm.getTitle()).isEqualTo(findEditArticle.getTitle());
        assertThat(articleEditForm.getContent()).isEqualTo(findEditArticle.getContent());
    }

    @Test
    @DisplayName("게시물 수정 실패 테스트 - 작성자의 account가 다를 때")
    public void editArticleTest2(){
        ArticleEditForm articleEditForm = new ArticleEditForm("수정 테스트 게시물1", "수정 테스트");
        Account newAccount = new Account("newid","1234","test@test.com");
        assertThrows(IllegalArgumentException.class, ()->articleService.editArticle(articleEditForm,article.getId(),newAccount.getUserId()));
    }

    @Test
    @DisplayName("게시물 삭제 성공 테스트")
    public void deleteArticleTest(){
        articleService.deleteArticle(article.getId(), account.getUserId());
        Article findArticle = em.find(Article.class, article.getId());

        assertThat(findArticle.getActive()).isEqualTo(0);

    }


}