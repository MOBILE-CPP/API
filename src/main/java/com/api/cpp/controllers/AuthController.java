package com.api.cpp.controllers;

import com.api.cpp.dtos.AuthDto;
import com.api.cpp.models.ApiResponse;
import com.api.cpp.models.AuthModel;
import com.api.cpp.services.AuthService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthController {

    final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Object> saveAuth(@RequestBody @Valid AuthDto authDto) {
        if (authService.existsByLogin(authDto.getLogin())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new ApiResponse(HttpStatus.CONFLICT.value(), "Login já está sendo utilizado!", null));
        }
        
        // aconselho aapgar este if
        if (authService.existsByPassword(authDto.getPassword())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new ApiResponse(HttpStatus.CONFLICT.value(), "Senha já está sendo utilizada!", null));
        }

        AuthModel authModel = new AuthModel();
        BeanUtils.copyProperties(authDto, authModel);
        AuthModel savedAuth = authService.save(authModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse(HttpStatus.CREATED.value(), "Autenticação criada com sucesso!", savedAuth));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginAuth(@RequestBody @Valid AuthDto authDto) throws JsonProcessingException {
        if(authService.existsByLogin(authDto.getLogin()) && authService.existsByPassword(authDto.getPassword())){
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.ACCEPTED.value(),"Login Realizaado com sucesso!",authDto));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(HttpStatus.NOT_FOUND.value(),"Login ou Senha incorretos.",null));
    }

    @GetMapping
    public ResponseEntity<Object> getAllAuth(){
    	return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(200,"Lista de todos os usuarios",authService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneAuth(@PathVariable(value = "id") UUID id) throws JsonProcessingException {
        Optional<AuthModel> authModelOptional = authService.findById(id);
        if (!authModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ObjectMapper().writeValueAsString(
                            Collections.singletonMap("message", "Usuário não encontrado.")
                    ));
        }
        return ResponseEntity.status(HttpStatus.OK).body(authModelOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAuth(@PathVariable(value = "id") UUID id) throws JsonProcessingException {
        Optional<AuthModel> authModelOptional = authService.findById(id);
        if (!authModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ObjectMapper().writeValueAsString(
                            Collections.singletonMap("message", "Usuário não encontrado.")
                    ));
        }
        authService.delete(authModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ObjectMapper().writeValueAsString(
                        Collections.singletonMap("message", "Usuário deletado com sucesso.")
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAuth(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid AuthDto authDto) throws JsonProcessingException {
        Optional<AuthModel> authModelOptional = authService.findById(id);
        if (!authModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ObjectMapper().writeValueAsString(
                            Collections.singletonMap("message", "Usuário não encontrado.")
                    ));
        }
        var authModel = new AuthModel();
        BeanUtils.copyProperties(authDto, authModel);
        authModel.setId(authModelOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(authService.save(authModel));
    }
}
