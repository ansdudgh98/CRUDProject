package hoy.project.api.controller.docs;

import hoy.project.api.controller.ControllerTest;
import hoy.project.api.controller.dto.request.form.LoginForm;
import hoy.project.api.controller.dto.request.form.SignupForm;
import hoy.project.api.controller.dto.response.UserResponse;
import hoy.project.api.controller.session.SessionConst;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountDocsTest extends DocsControllerTest{

    @Test
    @DisplayName("[DOCS][ACCOUNT]로그인 rest docs 적용")
    public void loginDocs() throws Exception {
        LoginForm loginForm = new LoginForm(account.getUserId(), account.getPassword());
        UserResponse loginResponse = new UserResponse(account.getUserId());

        mockMvc.perform(post("/api/account/login")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginForm))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(loginResponse)))
                .andDo(document("login", requestFields(
                        fieldWithPath("loginId").description("로그인시 사용할 아이디"),
                        fieldWithPath("loginPassword").description("로그인시 사용할 비밀번호")
                )))
                .andDo(document("login", responseFields(
                        fieldWithPath("userId").description("로그인에 성공한 유저 아이디 리턴 값")
                )));
    }

    @Test
    @DisplayName("[DOCS][Logout]로그아웃 rest docs 적용")
    public void logoutDocs() throws Exception{
        session.setAttribute(SessionConst.ACCOUNT,account.getUserId());

        mockMvc.perform(get("/api/account/logout")
                        .session(session))
                .andExpect(status().isOk())
                .andDo(document("logout"));
    }

    @Test
    @DisplayName("[DOCS][Acocunt] 회원가입 기능 Rest Docs 적용")
    public void signUpDocs() throws Exception{
        SignupForm signupForm = new SignupForm("signuptest","!test1234","test1@gmail.com");

        mockMvc.perform(post("/api/account/signup")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupForm)))
                .andExpect(status().isOk())
                .andDo(document("signup", requestFields(
                        fieldWithPath("signupId")
                                .description("회원가입 시 사용할 아이디\n"+ "valdation : 아이디는 4자~10자 사이, 소문자 영문 +_숫자 조합으로,특수문자는 불가능"),
                        fieldWithPath("signupPw")
                                .description("회원가입 시 사용할 비밀번호\n"+"validation : 비밀번호는 8자 ~ 16자 사이, 영문,숫자,특수문자조합으로 구성 해야됨"),
                        fieldWithPath("email")
                                .description("회원가입시 사용할 이메일\n"+"validation : 이메일 양식은 아이디@주소 형식으로"))))
                .andDo(document("signup", responseFields(
                        fieldWithPath("userId").description("회원가입 후 가입이 완료된 유저 아이디를 보내줌")
                )));

    }

}
