package com.api.cpp.dtos;

import com.api.cpp.models.ImageData;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PokemonDto {
    @NotBlank
    private String name;
    @NotBlank
    private String type;
    @NotNull
    private List<String> skills;
    @NotNull
    private Long imageData;
    @NotBlank
    private String username;

    /*
     * GETTERS E SETTERS
     * */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public Long getImageData() {
        return imageData;
    }

    public void setImageData(Long imageData) {
        this.imageData = imageData;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}