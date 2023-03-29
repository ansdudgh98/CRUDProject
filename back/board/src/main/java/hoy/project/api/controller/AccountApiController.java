package hoy.project.api.controller;

import hoy.project.api.controller.dto.request.form.LoginForm;
import hoy.project.api.controller.dto.request.form.SignupForm;
import hoy.project.api.controller.dto.response.UserResponse;
import hoy.project.api.controller.session.SessionConst;
import hoy.project.domain.Account;
import hoy.project.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
@Slf4j
public class AccountApiController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<UserResponse> sessionInit(HttpSession session){
        createGuestSession(session);

        return new ResponseEntity<>(new UserResponse(SessionConst.GUEST),HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginForm loginForm, HttpSession session){
        Account login = accountService.login(loginForm.getLoginId(), loginForm.getLoginPassword());

        createAccountSession(session, login);

        return new ResponseEntity<>(new UserResponse(login.getUserId()),HttpStatus.OK);
    }


    @GetMapping("/logout")
    public ResponseEntity<UserResponse> logout(HttpSession session){
        createGuestSession(session);
        return new ResponseEntity<>(new UserResponse(SessionConst.GUEST), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody SignupForm signupForm, HttpSession session){

        Account account = Account.createAccount(signupForm.getSignupId(), signupForm.getSignupPw(), signupForm.getEmail());
        accountService.createAccount(account);

        createAccountSession(session,account);

        return new ResponseEntity<>(new UserResponse(account.getUserId()),HttpStatus.OK);
    }

    private void createAccountSession(HttpSession session, Account login) {
        session.setAttribute(SessionConst.ACCOUNT, login.getUserId());
        session.setMaxInactiveInterval(3600);
    }

    private void createGuestSession(HttpSession session) {
        session.setAttribute(SessionConst.ACCOUNT, SessionConst.GUEST);
        session.setMaxInactiveInterval(3600);
    }


}
