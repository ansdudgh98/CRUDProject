package hoy.project.service;

import hoy.project.api.controller.dto.request.form.ArticleCreateForm;
import hoy.project.api.controller.dto.request.form.ArticleEditForm;
import hoy.project.api.controller.dto.response.article.ArticleDeleteResponse;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

    @Value("${server.storage}")
    private String path;

    private final ArticleRepository articleRepository;
    private final AccountRepository accountRepository;
    private final ImageRepository imageRepository;

    @Override
    @Transactional
    public ArticlePostResponse createArticle(ArticleCreateForm form, String accountId) {

        Account account = accountRepository.findByUserId(accountId);

        Article article = articleRepository.save(new Article(form.getTitle(), form.getContent(), account));

        return new ArticlePostResponse(article.getId());

    }

    @Override
    public ArticleReadResponse readArticle(Long id) {

        Article article = articleRepository.findArticleById(id);

        if(article == null){
            throw new IllegalArgumentException("존재하지 않는 게시물 번호 입니다.");
        }

        return new ArticleReadResponse(
                article.getTitle(),
                article.getContent(),
                article.getAccount().getUserId()
        );
    }

    @Override
    @Transactional
    public ArticleEditResponse editArticle(ArticleEditForm form, Long articleId, String accountId) {

        Account writer = accountRepository.findByUserId(accountId);
        Article editArticle = articleRepository.findById(articleId).get();

        if(!editArticle.getAccount().equals(writer)){
            throw new IllegalArgumentException("게시글을 수정할 권한이 없습니다!");
        }

        editArticle.changeTitleAndContent(form.getTitle(),form.getContent());
        return new ArticleEditResponse(editArticle.getId());
    }

    @Override
    @Transactional
    public ArticleDeleteResponse deleteArticle(Long id,String accountId) {

        Account writer = accountRepository.findByUserId(accountId);
        Article deleteArticle = articleRepository.findById(id).get();


        if(!deleteArticle.getAccount().equals(writer)){
            throw new IllegalArgumentException("게시글을 삭제할 권한이 없습니다!");
        }

        deleteArticle.deActive();

        return new ArticleDeleteResponse("삭제가 완료되었습니다.");

    }

    @Override
    @Transactional
    public ImageUploadResponse saveImage(MultipartFile multipartFile, String loginId) {

        String contentType = getContentType(multipartFile);

        Account findAccount = accountRepository.findByUserId(loginId);

        if(!contentType.equals("image")){
            throw new IllegalArgumentException("지원하지 않는 파일 형식 입니다.");
        }

        String filepath = saveFileInStorage(multipartFile);

        Image image = Image.builder()
                .url(filepath)
                .capacity(multipartFile.getSize())
                .filename(multipartFile.getResource().getFilename())
                .account(findAccount)
                .build();

        imageRepository.save(image);


        return new ImageUploadResponse(filepath);
    }

    private String saveFileInStorage(MultipartFile multipartFile) {

        String fileType = extractFileType(multipartFile);
        String filePath = setFilePath(fileType);

        try {
            multipartFile.transferTo(new File(filePath));
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String getContentType(MultipartFile multipartFile) {
        return multipartFile.getContentType().split("/")[0];
    }

    private String setFilePath(String fileType) {
        return path + UUID.randomUUID() + "." + fileType;
    }

    private String extractFileType(MultipartFile multipartFile) {
        return multipartFile.getContentType().split("/")[1];
    }

}
