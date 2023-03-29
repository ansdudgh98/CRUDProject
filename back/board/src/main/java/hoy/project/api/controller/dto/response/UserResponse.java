package hoy.project.api.controller.dto.response;

import lombok.Getter;

@Getter
public class UserResponse {

    private final String userId;

    public UserResponse(String userId) {
        this.userId = userId;
    }
}
