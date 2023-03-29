package hoy.project.api.controller;

import hoy.project.api.controller.dto.response.CommonErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public CommonErrorResponse notBlankRequestHandler(BindException e) {

        CommonErrorResponse errorResponse = new CommonErrorResponse("400", "잘못된 요청 입니다.");

        for (FieldError error : e.getFieldErrors()) {
            errorResponse.addValidation(error.getField(), error.getDefaultMessage());
        }

        return errorResponse;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public CommonErrorResponse wrongInfoRequestHandler(IllegalArgumentException e) {
        return new CommonErrorResponse("400", e.getMessage());
    }
}
