package hoy.project.service;

import hoy.project.api.controller.dto.request.form.ArticleCreateForm;
import hoy.project.api.controller.dto.request.form.ArticleEditForm;
import hoy.project.api.controller.dto.response.article.ArticleDeleteResponse;
import hoy.project.api.controller.dto.response.article.ArticleEditResponse;
import hoy.project.api.controller.dto.response.article.ArticlePostResponse;
import hoy.project.api.controller.dto.response.article.ArticleReadResponse;
import hoy.project.domain.Account;
import hoy.project.domain.Article;
import hoy.project.domain.View;
import hoy.project.repository.AccountRepository;
import hoy.project.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

    private final ArticleRepository articleRepository;
    private final AccountRepository accountRepository;

    @Override
    public ArticlePostResponse createArticle(ArticleCreateForm form, String accountId) {

        Account account = accountRepository.findByUserId(accountId);

        Article article = articleRepository.save(new Article(form.getTitle(), form.getContent(), account));

        return new ArticlePostResponse(article.getId());

    }

    @Override
    @Transactional(readOnly = true)
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
    public ArticleDeleteResponse deleteArticle(Long id,String accountId) {

        Account writer = accountRepository.findByUserId(accountId);
        Article deleteArticle = articleRepository.findById(id).get();


        if(!deleteArticle.getAccount().equals(writer)){
            throw new IllegalArgumentException("게시글을 삭제할 권한이 없습니다!");
        }

        deleteArticle.deActive();

        return new ArticleDeleteResponse("삭제가 완료되었습니다.");

    }
}
