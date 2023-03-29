package hoy.project.api.controller;

import hoy.project.api.controller.dto.response.article.ArticlePostResponse;
import hoy.project.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ArticlePostResponse post(@Lo){
        return null;
    }


}
