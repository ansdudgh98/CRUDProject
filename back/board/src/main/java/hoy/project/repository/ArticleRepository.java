package hoy.project.repository;

import hoy.project.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {

//    @Query(value = "select ar from Article ar where ar.id = :id")
    Article findArticleById(Long id);

}
