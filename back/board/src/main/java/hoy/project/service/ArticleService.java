package hoy.project.service;

import hoy.project.api.controller.dto.response.article.ArticleDeleteResponse;
import hoy.project.api.controller.dto.response.article.ArticleEditResponse;
import hoy.project.api.controller.dto.response.article.ArticlePostResponse;
import hoy.project.api.controller.dto.response.article.ArticleResponse;

public interface ArticleService{

    ArticlePostResponse post();

    ArticleResponse read();

    ArticleEditResponse edit();

    ArticleDeleteResponse delete();

}
