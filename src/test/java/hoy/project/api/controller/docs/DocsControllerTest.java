package hoy.project.api.controller.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import hoy.project.api.controller.session.SessionConst;
import hoy.project.domain.Account;
import hoy.project.repository.AccountRepository;
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

    protected ObjectMapper objectMapper = new ObjectMapper();
    protected MockHttpSession session = new MockHttpSession();
    @Autowired
    protected AccountRepository accountRepository;

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
