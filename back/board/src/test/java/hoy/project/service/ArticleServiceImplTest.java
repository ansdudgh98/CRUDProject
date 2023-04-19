package hoy.project.service;

import hoy.project.api.controller.dto.request.form.ArticleCreateForm;
import hoy.project.api.controller.dto.request.form.ArticleEditForm;
import hoy.project.api.controller.dto.response.article.ArticleEditResponse;
import hoy.project.api.controller.dto.response.article.ArticlePostResponse;
import hoy.project.api.controller.dto.response.article.ArticleReadResponse;
import hoy.project.domain.Account;
import hoy.project.domain.Article;
import hoy.project.repository.AccountRepository;
import hoy.project.repository.ArticleRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ArticleServiceImplTest {


    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    EntityManager em;


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

        Account account = new Account("test1", "test1", "mytest@email.com");
        Article article = new Article("myArticle","myArticle",account);

        accountRepository.save(account);
        articleRepository.save(article);

        ArticleReadResponse articleReadResponse = articleService.readArticle(article.getId());
        assertThat(articleReadResponse.getUserId()).isEqualTo(article.getAccount().getUserId());
        assertThat(articleReadResponse.getContent()).isEqualTo(article.getContent());
        assertThat(articleReadResponse.getTitle()).isEqualTo(article.getTitle());
    }

    @Test
    @DisplayName("게시물 읽기 실패 테스트 - 없는 게시물 번호 조회")
    public void readArticleTest2(){
        assertThrows(IllegalArgumentException.class,()->articleService.readArticle(Long.MAX_VALUE));
    }

    @Test
    @DisplayName("게시물 수정 성공 테스트")
    public void editArticleTest1() {

        Account account = new Account("test0", "test0", "mytest@email.com");
        Article article = new Article("myArticle1","myArticle1",account);

        accountRepository.save(account);
        articleRepository.save(article);

        ArticleEditForm articleEditForm = new ArticleEditForm("수정 테스트 게시물1", "수정 테스트");
        ArticleEditResponse articleEditResponse = articleService.editArticle(articleEditForm, article.getId(), account.getUserId());

        Article findEditArticle = em.find(Article.class, articleEditResponse.getId());

        assertThat(articleEditForm.getTitle()).isEqualTo(findEditArticle.getTitle());
        assertThat(articleEditForm.getContent()).isEqualTo(findEditArticle.getContent());
    }

    @Test
    @DisplayName("게시물 수정 실패 테스트 - 작성자의 account가 다를 때")
    public void editArticleTest2(){

        Account account = new Account("test2", "test1", "mytest@email.com");
        Article article = new Article("myArticle2","myArticle",account);

        accountRepository.save(account);
        articleRepository.save(article);

        ArticleEditForm articleEditForm = new ArticleEditForm("수정 테스트 게시물1", "수정 테스트");
        Account newAccount = new Account("newid","1234","test@test.com");
        assertThrows(IllegalArgumentException.class, ()->articleService.editArticle(articleEditForm,article.getId(),newAccount.getUserId()));
    }

    @Test
    @DisplayName("게시물 삭제 성공 테스트")
    public void deleteArticleTest(){

        Account account = new Account("test3", "test2", "mytest@email.com");
        Article article = new Article("myArticle3","myArticle3",account);

        accountRepository.save(account);
        articleRepository.save(article);

        articleService.deleteArticle(article.getId(), account.getUserId());
        Article findArticle = em.find(Article.class, article.getId());

        assertThat(findArticle.getActive()).isEqualTo(0);
    }


}