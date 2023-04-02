package hoy.project.api.controller;

import hoy.project.api.controller.dto.request.form.ArticleCreateForm;
import hoy.project.api.controller.dto.request.form.ArticleEditForm;
import hoy.project.api.controller.dto.response.article.ArticleDeleteResponse;
import hoy.project.api.controller.dto.response.article.ArticleEditResponse;
import hoy.project.api.controller.dto.response.article.ArticlePostResponse;
import hoy.project.api.controller.dto.response.article.ArticleReadResponse;
import hoy.project.domain.LoginAccountId;
import hoy.project.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/article")
public class ArticleApiController {

    private final ArticleService articleService;


    @GetMapping("/{id}")
    public ResponseEntity<ArticleReadResponse> read(@PathVariable Long id){
        return ResponseEntity.ok(articleService.readArticle(id));
    }

    @PostMapping("/post")
    public ResponseEntity<ArticlePostResponse> post(@Valid @RequestBody ArticleCreateForm form, @LoginAccountId String loginId){
        return ResponseEntity.ok(articleService.createArticle(form,loginId));
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<ArticleEditResponse> editArticle(@Valid @RequestBody ArticleEditForm form, @PathVariable Long id, @LoginAccountId String loginId){
        return ResponseEntity.ok(articleService.editArticle(form, id, loginId));
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<ArticleDeleteResponse> deleteArticle(@PathVariable Long id,@LoginAccountId String loginId){
        return ResponseEntity.ok(articleService.deleteArticle(id,loginId));
    }

}
