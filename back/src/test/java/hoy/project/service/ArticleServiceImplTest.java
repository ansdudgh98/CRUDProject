package hoy.project.service;

import hoy.project.api.controller.dto.request.form.ArticleCreateForm;
import hoy.project.api.controller.dto.request.form.ArticleEditForm;
import hoy.project.api.controller.dto.response.article.ArticleEditResponse;
import hoy.project.api.controller.dto.response.article.ArticlePostResponse;
import hoy.project.api.controller.dto.response.article.ArticleReadResponse;
import hoy.project.api.controller.dto.response.image.ImageUploadResponse;
import hoy.project.domain.Account;
import hoy.project.domain.Article;
import hoy.project.domain.Image;
import hoy.project.repository.AccountRepository;
import hoy.project.repository.ArticleRepository;
import hoy.project.repository.ImageRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ArticleServiceImplTest {

    @Value("${server.storage}")
    String path;

    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ImageRepository imageRepository;

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

    @Test
    @DisplayName("이미지 업로드 성공 테스트 - 이미지 내용이 동일한지 비교한다.")
    public void imageUploadSameFileContentsTest() throws Exception{
        
        String filename = "test.png";
        Account account = new Account("test3", "test2", "mytest@email.com");

        String testPath = "./src/test/resources/static/";
        MultipartFile file = new MockMultipartFile(filename,filename,"image/png",new FileInputStream(testPath +filename));

        ImageUploadResponse imageUploadResponse = articleService.saveImage(file, account.getUserId());

        File resultFile = new File(imageUploadResponse.getUrl());
        byte[] resultFileBytes = Files.readAllBytes(resultFile.toPath());
        byte[] fileBytes = file.getBytes();

        assertThat(fileBytes).containsExactly(resultFileBytes);
    }

    @Test
    @DisplayName("이미지 업로드 성공 테스트 - 이미지의 정보가 데이터베이스에 저장된 정보와 동일한지 비교한다.")
    public void imageUploadInfoConfirmTest() throws Exception{
        String filename = "test.png";
        Account account = new Account("test3", "test2", "mytest@email.com");

        accountRepository.save(account);

        String testPath = "./src/test/resources/static/";
        MultipartFile file = new MockMultipartFile(filename,filename,"image/png",new FileInputStream(testPath +filename));

        ImageUploadResponse imageUploadResponse = articleService.saveImage(file, account.getUserId());
        Image image = imageRepository.findImageByAccount_Id(account.getId());

        assertThat(image.getUrl()).isEqualTo(imageUploadResponse.getUrl());
        assertThat(image.getFilename()).isEqualTo(filename);
        assertThat(file.getSize()).isEqualTo(image.getCapacity());
    }

    @Test
    @DisplayName("이미지 업로드 실패 테스트 - 이미지의 형식이 만약 존재하지 않는 형식인 경우 예외를 반환한다.")
    void imageUploadFailTest() throws Exception {

        String filename = "text.txt";
        Account account = new Account("test3", "test2", "mytest@email.com");

        accountRepository.save(account);

        String testPath = "./src/test/resources/static/";
        MultipartFile file = new MockMultipartFile(filename,filename,"text/*",new FileInputStream(testPath +filename));

        assertThrows(IllegalArgumentException.class,()-> articleService.saveImage(file,account.getUserId()));
    }



}