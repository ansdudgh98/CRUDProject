package hoy.project.repository;

import hoy.project.domain.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    Comment findCommentById(Long commentId);

    @Query(value = "select cm from Comment cm " +
            "join fetch cm.account " +
            "where cm.article.id = :articleId and cm.active = 1")
    Slice<Comment> findSliceComments(Long articleId, Pageable pageable);

    @Query(value = "select cm from Comment cm " +
            "join fetch cm.account " +
            "where cm.id = :commentId")
    Comment findCommentsById(Long commentId);

}
