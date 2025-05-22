package com.motionparts.ecommerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class RegisterRequest {

    @NotNull(message = "El usuario no puede ser nulo")
    @Valid
    private UserDTO user;

    // Esta parte solo es necesaria si el usuario tiene info adicional
    private UserInfoDTO userInfo; // Ya no es obligatorio

    public RegisterRequest() {}

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public UserInfoDTO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoDTO userInfo) {
        this.userInfo = userInfo;
    }
}
