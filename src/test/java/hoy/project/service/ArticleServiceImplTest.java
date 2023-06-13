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
import hoy.project.repository.ArticleRepository;
import hoy.project.repository.ImageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArticleServiceImplTest extends ServiceTest {

    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ImageRepository imageRepository;

    @Value("${server.storage}")
    String path;

    @Test
    @DisplayName("게시물 생성 성공 테스트")
    public void createArticleTest() {

        Article newArticle = new Article("테스트 게시물1", "테스트 내용1", testAccount);

        ArticlePostResponse articlePostResponse = articleService.createArticle(new ArticleCreateForm(
                newArticle.getTitle(), newArticle.getContent()
        ), testAccount.getUserId());

        Article findArticle = em.find(Article.class, articlePostResponse.getId());

        assertThat(newArticle.getTitle()).isEqualTo(findArticle.getTitle());
        assertThat(newArticle.getContent()).isEqualTo(findArticle.getContent());
        assertThat(newArticle.getAccount().getId()).isEqualTo(testAccount.getId());
    }

    @Test
    @DisplayName("게시물 생성 실패 테스트 - form의 제목 값 null")
    public void createArticleFailTestEmptyFormTitle() {
        ArticleCreateForm articleCreateForm = new ArticleCreateForm(null, "123456");
        assertThrows(DataIntegrityViolationException.class, () -> articleService.createArticle(articleCreateForm, testAccount.getUserId()));
    }

    @Test
    @DisplayName("게시물 생성 실패 테스트 - form의 내용 값이 비었을 때")
    public void createArticleFailTestEmptyFormContent() {
        ArticleCreateForm articleCreateForm = new ArticleCreateForm("123123", null);
        assertThrows(DataIntegrityViolationException.class, () -> articleService.createArticle(articleCreateForm, testAccount.getUserId()));
    }

    @Test
    @DisplayName("게시물 읽기 성공 테스트")
    public void readArticleTes1() {
        Article article = new Article("myArticle", "myArticle", testAccount);
        articleRepository.save(article);

        ArticleReadResponse articleReadResponse = articleService.readArticle(article.getId());
        assertThat(articleReadResponse.getUserId()).isEqualTo(article.getAccount().getUserId());
        assertThat(articleReadResponse.getContent()).isEqualTo(article.getContent());
        assertThat(articleReadResponse.getTitle()).isEqualTo(article.getTitle());
    }

    @Test
    @DisplayName("게시물 읽기 실패 테스트 - 없는 게시물 번호 조회")
    public void readArticleTestNoneArticleId() {
        assertThrows(IllegalArgumentException.class, () -> articleService.readArticle(Long.MAX_VALUE));
    }

    @Test
    @DisplayName("게시물 수정 성공 테스트")
    public void editArticleTest() {
        Article article = new Article("myArticle1", "myArticle1", testAccount);
        articleRepository.save(article);

        ArticleEditForm articleEditForm = new ArticleEditForm("수정 테스트 게시물1", "수정 테스트");
        ArticleEditResponse articleEditResponse = articleService.editArticle(articleEditForm, article.getId(), testAccount.getUserId());

        Article findEditArticle = em.find(Article.class, articleEditResponse.getId());

        assertThat(articleEditForm.getTitle()).isEqualTo(findEditArticle.getTitle());
        assertThat(articleEditForm.getContent()).isEqualTo(findEditArticle.getContent());
    }

    @Test
    @DisplayName("게시물 수정 실패 테스트 - 작성자의 account가 다를 때")
    public void editArticleFailTestDiffAccount() {
        Article article = new Article("myArticle2", "myArticle", testAccount);

        articleRepository.save(article);

        ArticleEditForm articleEditForm = new ArticleEditForm("수정 테스트 게시물1", "수정 테스트");
        Account newAccount = new Account("newid", "1234", "test@test.com");
        assertThrows(IllegalArgumentException.class, () -> articleService.editArticle(articleEditForm, article.getId(), newAccount.getUserId()));
    }

    @Test
    @DisplayName("게시물 삭제 성공 테스트")
    public void deleteArticleTest() {
        Article article = new Article("myArticle3", "myArticle3", testAccount);

        accountRepository.save(testAccount);
        articleRepository.save(article);

        articleService.deleteArticle(article.getId(), testAccount.getUserId());
        Article findArticle = em.find(Article.class, article.getId());

        assertThat(findArticle.getActive()).isEqualTo(0);
    }

    @Test
    @DisplayName("이미지 업로드 성공 테스트 - 이미지 내용이 동일한지 비교한다.")
    public void imageUploadSameFileContentsTest() throws Exception {

        String filename = "test.png";

        String testPath = "./src/test/resources/static/";
        MultipartFile file = new MockMultipartFile(filename, filename, "image/png", new FileInputStream(testPath + filename));

        ImageUploadResponse imageUploadResponse = articleService.saveImage(file, testAccount.getUserId());

        File resultFile = new File(imageUploadResponse.getUrl());
        byte[] resultFileBytes = Files.readAllBytes(resultFile.toPath());
        byte[] fileBytes = file.getBytes();

        assertThat(fileBytes).containsExactly(resultFileBytes);
    }

    @Test
    @DisplayName("이미지 업로드 성공 테스트 - 이미지의 정보가 데이터베이스에 저장된 정보와 동일한지 비교한다.")
    public void imageUploadInfoConfirmTest() throws Exception {
        String filename = "test.png";

        String testPath = "./src/test/resources/static/";
        MultipartFile file = new MockMultipartFile(filename, filename, "image/png", new FileInputStream(testPath + filename));

        ImageUploadResponse imageUploadResponse = articleService.saveImage(file, testAccount.getUserId());
        Image image = imageRepository.findImageByAccount_Id(testAccount.getId());

        assertThat(image.getUrl()).isEqualTo(imageUploadResponse.getUrl());
        assertThat(image.getFilename()).isEqualTo(filename);
        assertThat(file.getSize()).isEqualTo(image.getCapacity());
    }

    @Test
    @DisplayName("이미지 업로드 실패 테스트 - 이미지의 형식이 만약 존재하지 않는 형식인 경우 예외를 반환한다.")
    void imageUploadFailTest() throws Exception {
        String filename = "text.txt";
        String testPath = "./src/test/resources/static/";
        MultipartFile file = new MockMultipartFile(filename, filename, "text/*", new FileInputStream(testPath + filename));

        assertThrows(IllegalArgumentException.class, () -> articleService.saveImage(file, testAccount.getUserId()));
    }


}