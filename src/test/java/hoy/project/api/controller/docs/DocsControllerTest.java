package hoy.project.api.controller.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import hoy.project.api.controller.session.SessionConst;
import hoy.project.domain.Account;
import hoy.project.repository.AccountRepository;
import hoy.project.repository.ArticleRepository;
import hoy.project.repository.CommentRepository;
import hoy.project.service.AccountService;
import hoy.project.service.ArticleService;
import hoy.project.service.CommentService;
import hoy.project.service.ReplyService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@ExtendWith({RestDocumentationExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class DocsControllerTest {
    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;
    @Autowired
    protected AccountService accountService;
    @Autowired
    protected ArticleService articleService;
    @Autowired
    protected CommentService commentService;
    @Autowired
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

    @BeforeEach
    public void init(RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();
    }

    @BeforeEach
    protected void setUp() {
        account = accountRepository.save(new Account("test1", "!test123", "test@gmail.com"));
        session.setAttribute(SessionConst.attributeName, "test1");
    }

    @AfterEach
    protected void destroy() {
        accountRepository.deleteAll();
    }
}
