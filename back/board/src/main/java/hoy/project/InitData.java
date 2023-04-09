package hoy.project;

import hoy.project.domain.Account;
import hoy.project.domain.Article;
import hoy.project.domain.Comment;
import hoy.project.domain.Reply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Profile("dev")
@Component
@Slf4j
@Transactional
public class InitData implements ApplicationRunner {

    @PersistenceContext
    private EntityManager em;

    public void initData() {
        for (int i = 0; i < 10; i++) {
            log.info("{}% 시작", i * 10);
            Account account = new Account("testId" + i, "test1", "test@test.com");
            em.persist(account);
            for (int j = 0; j < 5; j++) {
                Article article = new Article("user " + i + "의 테스트 게시물 " + j, "테스트 게시물 내용", account);
                em.persist(article);
                for (int k = 0; k < 15; k++) {
                    Comment comment = new Comment("user " + i + "의테스트 댓글" + i, account, article);
                    em.persist(comment);
                    for (int p = 0; p < 25; p++) {
                        Reply reply = new Reply("테스트 대댓글" + i, comment, account);
                        em.persist(reply);
                    }
                    em.flush();
                    em.clear();
                }
            }
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initData();
    }
}

