package hoy.project.service;

import hoy.project.api.controller.dto.request.form.ArticleCreateForm;
import hoy.project.api.controller.dto.request.form.ArticleEditForm;
import hoy.project.api.controller.dto.response.article.ArticleDeleteResponse;
import hoy.project.api.controller.dto.response.article.ArticleEditResponse;
import hoy.project.api.controller.dto.response.article.ArticlePostResponse;
import hoy.project.api.controller.dto.response.article.ArticleReadResponse;
import hoy.project.api.controller.dto.response.image.ImageUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ArticleService {

    ArticlePostResponse createArticle(ArticleCreateForm form, String accountId);

    ArticleReadResponse readArticle(Long id);

    ArticleEditResponse editArticle(ArticleEditForm form, Long articleId, String accountId);

    ArticleDeleteResponse deleteArticle(Long id,String accountId);

    ImageUploadResponse saveImage(MultipartFile file, String loginId) ;


}
