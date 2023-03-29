package hoy.project.api.controller.dto.request.form;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
public class LoginForm {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String loginPassword;

    public LoginForm() {
    }

    public LoginForm(String loginId, String loginPassword) {
        this.loginId = loginId;
        this.loginPassword = loginPassword;
    }
}