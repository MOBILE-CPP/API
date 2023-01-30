package com.api.cpp.dtos;

import jakarta.validation.constraints.NotBlank;

public class AuthDto {
    @NotBlank
    private String login;
    @NotBlank
    private String password;

    /*
     * GETTERS E SETTERS
     * */

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}