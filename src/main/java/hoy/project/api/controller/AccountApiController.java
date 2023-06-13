package hoy.project.api.controller;

import hoy.project.api.controller.dto.request.form.LoginForm;
import hoy.project.api.controller.dto.request.form.SignupForm;
import hoy.project.api.controller.dto.response.UserResponse;
import hoy.project.api.controller.session.SessionConst;
import hoy.project.domain.Account;
import hoy.project.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountApiController {

    private final AccountService accountService;

    @PostMapping("/login")
    public UserResponse login(@Valid @RequestBody LoginForm loginForm, HttpSession session) {
        Account login = accountService.login(loginForm.getLoginId(), loginForm.getLoginPassword());
        createAccountSession(session, login);

        return new UserResponse(login.getUserId());
    }


    @GetMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @PostMapping("/signup")
    public UserResponse signup(@Valid @RequestBody SignupForm signupForm, HttpSession session) {

        Account account = Account.createAccount(signupForm.getSignupId(), signupForm.getSignupPw(), signupForm.getEmail());

        accountService.createAccount(account);

        createAccountSession(session, account);

        return new UserResponse(account.getUserId());
    }

    private void createAccountSession(HttpSession session, Account account) {
        session.setAttribute(SessionConst.attributeName, account.getUserId());
        session.setMaxInactiveInterval(3600);
    }

}
