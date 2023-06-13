package hoy.project.api.controller.dto.request.form;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class CommentForm {

    @NotBlank(message = "제목을 입력하지 않았습니다.")
    @Size(max = 100)
    private String content;

    public CommentForm(String content) {
        this.content = content;
    }
}
