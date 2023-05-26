package hoy.project.service;

import hoy.project.domain.Account;
import hoy.project.domain.Article;
import hoy.project.domain.Comment;
import hoy.project.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
public class ServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    ArticleService articleService;

    @Autowired
    CommentService commentService;

    @Autowired
    ReplyService replyService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    EntityManager em;

    Account testAccount;

    @BeforeEach
    void setup(){
        testAccount = accountRepository.save(new Account("testid1","!testpassword1","test@gmail.com"));
    }





}
