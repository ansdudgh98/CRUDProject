package hoy.project.api.controller.dto.request.form;


import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
public class ReplyForm {

    @NotEmpty(message = "제목을 입력하지 않았습니다.")
    @Size(max = 100)
    private String content;

    public ReplyForm(String content) {
        this.content = content;
    }
}
