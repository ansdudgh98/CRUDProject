package hoy.project.repository;

import hoy.project.domain.Reply;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ReplyRepository extends JpaRepository<Reply,Long> {

    @Query(value = "select re from Reply re " +
            "join fetch re.account " +
            "where re.id = :id")
    Reply findReplyById(Long id);

    @Query(value = "select re from Reply re " +
            "join fetch re.account " +
            "where re.comment.id = :commentId and re.active = 1")
    Slice<Reply> findSliceReplies(Long commentId, Pageable pageable);

}
