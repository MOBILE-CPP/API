package com.api.cpp.models;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "AUTH")
public class AuthModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @JsonProperty
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @JsonProperty
    @Column(nullable = false, length = 11)
    private String login;
    @JsonProperty
    @Column(nullable = false)
    private String password;

    /*
     * GETTERS E SETTERS
     * */

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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