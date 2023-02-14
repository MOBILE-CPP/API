package com.api.cpp.controllers;

import com.api.cpp.dtos.PokemonDto;
import com.api.cpp.models.ImageData;
import com.api.cpp.models.PokemonModel;
import com.api.cpp.services.ImageDataService;
import com.api.cpp.services.PokemonService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/pokemon")
public class PokemonController {

    final PokemonService pokemonService;
    final ImageDataService imageDataService;

    public PokemonController(PokemonService pokemonService, ImageDataService imageDataService) {
        this.pokemonService = pokemonService;
        this.imageDataService = imageDataService;
    }

    @PostMapping
    public ResponseEntity<Object> savePokemon(@RequestBody @Valid PokemonDto pokemonDto) throws JsonProcessingException {
        if (pokemonService.existsByName(pokemonDto.getName())){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ObjectMapper().writeValueAsString(
                            Collections.singletonMap("message", "Conflito: O nome do Pokémon já está sendo utilizado!")
                    ));
        }

        var pokemonModel = new PokemonModel();
        Optional<ImageData> imageDataOptional = imageDataService.findById(pokemonDto.getImageData());

        if (imageDataOptional.isPresent()) {
            pokemonModel.setName(pokemonDto.getName());
            pokemonModel.setType(pokemonDto.getType());
            pokemonModel.setSkills(pokemonDto.getSkills());
            pokemonModel.setImageData(imageDataOptional.get());
            pokemonModel.setUsername(pokemonDto.getUsername());

            return ResponseEntity.status(HttpStatus.CREATED).body(pokemonService.save(pokemonModel));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ObjectMapper().writeValueAsString(
                            Collections.singletonMap("message", "A Imagem com o id informado não foi encontrada.")
                    ));
        }
    }

    @GetMapping
    public ResponseEntity<List<PokemonModel>> getAllPokemon(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ){
        return ResponseEntity.status(HttpStatus.OK).body(pokemonService.findAll(pageable).getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOnePokemon(@PathVariable(value = "id") UUID id) throws JsonProcessingException {
        Optional<PokemonModel> pokemonModelOptional = pokemonService.findById(id);
        if (!pokemonModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ObjectMapper().writeValueAsString(
                            Collections.singletonMap("message", "Pokemon não encontrado.")
                    ));
        }
        return ResponseEntity.status(HttpStatus.OK).body(pokemonModelOptional.get());
    }

    @GetMapping("/search/type")
    public ResponseEntity<?> getPokemonByType(@RequestParam(value = "type") String type) throws JsonProcessingException {
        Optional<Object[]> pokemonModelOptional = pokemonService.findByType(type);
        if (pokemonModelOptional.get().length == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ObjectMapper().writeValueAsString(
                            Collections.singletonMap("message", "Tipo não encontrado.")
                    ));
        }

        return ResponseEntity.status(HttpStatus.OK).body(pokemonModelOptional.get());
    }

    @GetMapping("/search/skill")
    public ResponseEntity<?> getPokemonBySkill(@RequestParam(value = "skill") String skill) throws JsonProcessingException {
        Optional<Object[]> pokemonModelOptional = pokemonService.findBySkill(skill);
        if (pokemonModelOptional.get().length == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ObjectMapper().writeValueAsString(
                            Collections.singletonMap("message", "Habilidade não encontrada.")
                    ));
        }

        return ResponseEntity.status(HttpStatus.OK).body(pokemonModelOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePokemon(@PathVariable(value = "id") UUID id) throws JsonProcessingException {
        Optional<PokemonModel> pokemonModelOptional = pokemonService.findById(id);
        if (!pokemonModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ObjectMapper().writeValueAsString(
                            Collections.singletonMap("message", "Pokemon não encontrado.")
                    ));
        }
        pokemonService.delete(pokemonModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ObjectMapper().writeValueAsString(
                        Collections.singletonMap("message", "Pokemon deletado com sucesso.")
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePokemon(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid PokemonDto pokemonDto) throws JsonProcessingException {
        Optional<PokemonModel> pokemonModelOptional = pokemonService.findById(id);
        if (!pokemonModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ObjectMapper().writeValueAsString(
                            Collections.singletonMap("message", "Pokémon não encontrado.")
                    ));
        }

        if (pokemonService.existsByName(pokemonDto.getName())){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ObjectMapper().writeValueAsString(
                            Collections.singletonMap("message", "Conflito: O nome do Pokémon já está sendo utilizado!")
                    ));
        }

        var pokemonModel = new PokemonModel();
        Optional<ImageData> imageDataOptional = imageDataService.findById(pokemonDto.getImageData());

        if (imageDataOptional.isPresent()) {
            pokemonModel.setId(pokemonModelOptional.get().getId());
            pokemonModel.setName(pokemonDto.getName());
            pokemonModel.setType(pokemonDto.getType());
            pokemonModel.setSkills(pokemonDto.getSkills());
            pokemonModel.setImageData(imageDataOptional.get());
            pokemonModel.setUsername(pokemonDto.getUsername());

            return ResponseEntity.status(HttpStatus.OK).body(pokemonService.save(pokemonModel));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ObjectMapper().writeValueAsString(
                            Collections.singletonMap("message", "A Imagem com o id informado não foi encontrada.")
                    ));
        }
    }
}