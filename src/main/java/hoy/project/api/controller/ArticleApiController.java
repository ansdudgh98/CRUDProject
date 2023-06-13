package hoy.project.api.controller;

import hoy.project.api.controller.argumentresolver.LoginAccountId;
import hoy.project.api.controller.dto.request.form.ArticleCreateForm;
import hoy.project.api.controller.dto.request.form.ArticleEditForm;
import hoy.project.api.controller.dto.response.article.ArticleDeleteResponse;
import hoy.project.api.controller.dto.response.article.ArticleEditResponse;
import hoy.project.api.controller.dto.response.article.ArticlePostResponse;
import hoy.project.api.controller.dto.response.article.ArticleReadResponse;
import hoy.project.api.controller.dto.response.image.ImageUploadResponse;
import hoy.project.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article")
public class ArticleApiController {

    private final ArticleService articleService;


    @GetMapping("/{id}")
    public ArticleReadResponse read(@PathVariable Long id) {
        return articleService.readArticle(id);
    }

    @PostMapping("/post")
    public ArticlePostResponse post(@Valid @RequestBody ArticleCreateForm form, @LoginAccountId String loginId) {
        return articleService.createArticle(form, loginId);
    }

    @PostMapping("/edit/{id}")
    public ArticleEditResponse editArticle(@Valid @RequestBody ArticleEditForm form,
                                           @PathVariable Long id,
                                           @LoginAccountId String loginId) {
        return articleService.editArticle(form, id, loginId);
    }

    @GetMapping("/delete/{id}")
    public ArticleDeleteResponse deleteArticle(@PathVariable Long id, @LoginAccountId String loginId) {
        return articleService.deleteArticle(id, loginId);
    }

    @PostMapping("/imageupload")
    public ImageUploadResponse imageUpload(@RequestParam MultipartFile file, @LoginAccountId String loginId) {
        return articleService.saveImage(file, loginId);
    }


}
