package hoy.project.api.controller.dto.request.form;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
public class SignupForm {

    @NotEmpty(message = "아이디를 입력해주세요.")
    @Pattern(regexp = "^[a-z]+[a-z0-9]{4,10}$",message = "올바른 아이디 형식을 입력해주세요.")
    private String signupId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
//    @Pattern(regexp = "^[a-zA-Z0-9`~!@#$%^&*()-_=+]+[a-zA-Z0-9`~!@#$%^&*()-_=+]{8,24}$", message = "올바른 비밀번호 형식을 입력해주세요.")
    private String signupPw;

    @NotBlank(message = "이메일 주소를 확인해주세요..")
    @Pattern(regexp = "^[A-Za-z0-9]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;
}
