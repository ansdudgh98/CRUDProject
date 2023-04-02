package hoy.project.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hoy.project.api.controller.dto.request.form.LoginForm;
import hoy.project.api.controller.dto.response.CommonErrorResponse;
import hoy.project.api.controller.dto.response.UserResponse;
import hoy.project.api.controller.session.SessionConst;
import hoy.project.domain.Account;
import hoy.project.repository.AccountRepository;
import hoy.project.service.AccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment =SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class AccountApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;


    ObjectMapper objectMapper = new ObjectMapper();
    MockHttpSession session = new MockHttpSession();

    @BeforeEach
    public void initAccount(){
        Account testAccount = Account.builder()
                .userId("testid123")
                .password("testpw123")
                .email("testemail@test.com").build();

        accountRepository.save(testAccount);
    }

    @BeforeEach
    public void initSession(){
            session.setAttribute(SessionConst.ACCOUNT,SessionConst.GUEST);
    }

    @AfterEach
    public void after(){
        accountRepository.deleteAll();
    }




    @Test
    @DisplayName("[API][ACCOUNT]로그인 성공 테스트 - 로그인 성공시 헤더에는 세션을 바디에는 상태코드 200과 userId를 내려준다.")
    public void loginTest1() throws Exception {

        LoginForm loginForm = new LoginForm("testid123","testpw123");
        UserResponse loginResponse = new UserResponse("testid123");

        ResultActions result = mockMvc.perform(post("/api/account/login")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginForm))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(loginResponse)));


        MvcResult mvcResult = result.andReturn();

        String reqSession = mvcResult.getRequest()
                .getSession()
                .getAttribute(SessionConst.ACCOUNT)
                .toString();

        String jdbcSession = session.getAttribute(SessionConst.ACCOUNT).toString();

        assertThat(reqSession).isEqualTo(jdbcSession);
    }

    @Test
    @DisplayName("[API][ACCOUNT]로그인 실패 테스트 - 아무것도 입력하지 않았을 시 예외를 출력하고 세션은 GUEST 그대로 이다.")
    public void loginTest2() throws Exception{
        LoginForm loginForm = new LoginForm("","");
        CommonErrorResponse validationErrorResponse = new CommonErrorResponse("400","잘못된 요청 입니다.");
        validationErrorResponse.addValidation("loginId","아이디를 입력해주세요.");
        validationErrorResponse.addValidation("loginPassword","비밀번호를 입력해주세요.");

        MvcResult mvcResult = mockMvc.perform(post("/api/account/login")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginForm)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(validationErrorResponse)))
                .andReturn();

        String reqSession = mvcResult.getRequest()
                .getSession()
                .getAttribute(SessionConst.ACCOUNT)
                .toString();

        String jdbcSession = session.getAttribute(SessionConst.ACCOUNT).toString();

        assertThat(reqSession).isEqualTo(jdbcSession);
    }

    @Test
    @DisplayName("[API][ACCOUNT]로그인 실패 테스트 - 존재하지 않는 아이디를 입력시 예외를 출력하고 세션은 GUEST 그대로 이다..")
    public void loginTest3() throws Exception{
        LoginForm loginForm = new LoginForm("test11111","1111111");
        CommonErrorResponse loginErrorResponse = new CommonErrorResponse("400","존재하지 않는 아이디 입니다.");

        MvcResult mvcResult = mockMvc.perform(post("/api/account/login")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginForm)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(loginErrorResponse)))
                .andReturn();

        String reqSession = mvcResult.getRequest()
                .getSession()
                .getAttribute(SessionConst.ACCOUNT)
                .toString();

        String jdbcSession = session.getAttribute(SessionConst.ACCOUNT).toString();

        assertThat(reqSession).isEqualTo(jdbcSession);
    }

    @Test
    @DisplayName("[API][ACCOUNT]로그인 실패 테스트 - 틀린 비밀번호 입력시 예외를 출력한다.")
    public void loginTest4() throws Exception{
        LoginForm loginForm = new LoginForm("testid123","1111111");
        CommonErrorResponse loginErrorResponse = new CommonErrorResponse("400","비밀번호를 확인해주세요.");

        MvcResult mvcResult = mockMvc.perform(post("/api/account/login")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginForm)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(loginErrorResponse)))
                .andReturn();

        String reqSession = mvcResult.getRequest()
                .getSession()
                .getAttribute(SessionConst.ACCOUNT)
                .toString();

        String jdbcSession = session.getAttribute(SessionConst.ACCOUNT).toString();

        assertThat(reqSession).isEqualTo(jdbcSession);

    }

    @Test
    @DisplayName("[API][ACCOUNT]로그아웃 테스트 - 로그아웃시 세션 정보는 GUEST가 된다.")
    public void logoutTest() throws Exception{
        session.setAttribute(SessionConst.ACCOUNT,"test123");


        MvcResult mvcResult = mockMvc.perform(get("/api/account/logout")
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();

        String reqSession = mvcResult.getRequest()
                .getSession()
                .getAttribute(SessionConst.ACCOUNT)
                .toString();

        String jdbcSession = session.getAttribute(SessionConst.ACCOUNT).toString();

        assertThat(reqSession).isEqualTo(jdbcSession);

    }

}