package com.api.cpp.services;

import com.api.cpp.models.AuthModel;
import com.api.cpp.repositories.AuthRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Transactional
    public AuthModel save(AuthModel authModel) {
        return authRepository.save(authModel);
    }

    public Page<AuthModel> findAll(Pageable pageable) {
        return authRepository.findAll(pageable);
    }

    public Optional<AuthModel> findById(UUID id) {
        return authRepository.findById(id);
    }

    @Transactional
    public void delete(AuthModel authModel) {
        authRepository.delete(authModel);
    }

    public boolean existsByLogin(String login) {
        return authRepository.existsByLogin(login);
    }

    public boolean existsByPassword(String password) {
        return authRepository.existsByPassword(password);
    }
}
