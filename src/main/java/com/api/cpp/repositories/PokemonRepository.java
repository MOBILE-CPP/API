package com.api.cpp.repositories;

import com.api.cpp.models.PokemonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PokemonRepository extends JpaRepository<PokemonModel, UUID> {
    boolean existsByName(String name);

    @Query(value = "select name from pokemon where type like %?1%", nativeQuery = true)
    Optional<Object[]> findByTypeContainingIgnoreCase(String type);

    @Query(value = "" +
            "select p.name from pokemon p inner join pokemon_model_skills ps where ps.skills like CONCAT('%',?1,'%') and p.id = ps.pokemon_model_id;",
            nativeQuery = true)
    Optional<Object[]> findBySkillContainingIgnoreCase(String skill);
}
