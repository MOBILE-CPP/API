package com.api.cpp.repositories;

import com.api.cpp.models.AuthModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthRepository extends JpaRepository<AuthModel, UUID> {
    boolean existsByLogin(String login);
    boolean existsByPassword(String password);
}