package hoy.project.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hoy.project.api.controller.session.SessionConst;
import hoy.project.domain.Account;
import hoy.project.domain.Article;
import hoy.project.repository.AccountRepository;
import hoy.project.repository.ArticleRepository;
import hoy.project.repository.CommentRepository;
import hoy.project.service.AccountService;
import hoy.project.service.ArticleService;
import hoy.project.service.CommentService;
import hoy.project.service.ReplyService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ControllerTest {

    protected MockMvc mockMvc;

    @Autowired
    protected AccountService accountService;

    @Autowired
    protected ArticleService articleService;

    @MockBean
    protected CommentService commentService;

    @MockBean
    protected ReplyService replyService;

    protected ObjectMapper objectMapper = new ObjectMapper();
    protected MockHttpSession session = new MockHttpSession();

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected ArticleRepository articleRepository;

    @Autowired
    protected CommentRepository commentRepository;

    protected Account account;
    protected Article testArticle;

    @Autowired
    protected WebApplicationContext context;

    @BeforeEach
    protected void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        session.setAttribute(SessionConst.attributeName, "test1");
    }




}
