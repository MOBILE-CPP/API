package com.api.cpp.services;

import com.api.cpp.models.PokemonModel;
import com.api.cpp.repositories.PokemonRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PokemonService {
    final PokemonRepository pokemonRepository;

    public PokemonService(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    @Transactional
    public PokemonModel save(PokemonModel pokemonModel) {
        return pokemonRepository.save(pokemonModel);
    }

    public Page<PokemonModel> findAll(Pageable pageable) { return pokemonRepository.findAll(pageable); }

    public Optional<PokemonModel> findById(UUID id) {
        return pokemonRepository.findById(id);
    }

    @Transactional
    public void delete(PokemonModel pokemonModel) {
        pokemonRepository.delete(pokemonModel);
    }

    public boolean existsByName(String name) {
        return pokemonRepository.existsByName(name);
    }

    public Optional<Object[]> findByType(String type) {
        return pokemonRepository.findByTypeContainingIgnoreCase(type);
    }

    public Optional<Object[]> findBySkill(String skill) { return pokemonRepository.findBySkillContainingIgnoreCase(skill); }
}