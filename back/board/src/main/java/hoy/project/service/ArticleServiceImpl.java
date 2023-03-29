package hoy.project.service;

import hoy.project.api.controller.dto.response.article.ArticleDeleteResponse;
import hoy.project.api.controller.dto.response.article.ArticleEditResponse;
import hoy.project.api.controller.dto.response.article.ArticlePostResponse;
import hoy.project.api.controller.dto.response.article.ArticleResponse;
import hoy.project.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

    private final ArticleRepository articleRepository;

    @Override
    @Transactional
    public ArticlePostResponse post() {
        return null;
    }

    @Override
    public ArticleResponse read() {
        return null;
    }

    @Override
    @Transactional
    public ArticleEditResponse edit() {
        return null;
    }

    @Override
    @Transactional
    public ArticleDeleteResponse delete() {
        return null;
    }
}
