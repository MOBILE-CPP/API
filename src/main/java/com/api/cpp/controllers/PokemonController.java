package com.api.cpp.controllers;

import com.api.cpp.dtos.PokemonDto;
import com.api.cpp.models.ImageData;
import com.api.cpp.models.PokemonModel;
import com.api.cpp.services.ImageDataService;
import com.api.cpp.services.PokemonService;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

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
    public ResponseEntity<Object> savePokemon(@RequestBody @Valid PokemonDto pokemonDto){
        if (pokemonService.existsByName(pokemonDto.getName())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito: O nome do Pokémon já está sendo utilizado!");
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("É necessário informar o id da imagem.");
        }
    }

    @GetMapping
    public ResponseEntity<Page<PokemonModel>> getAllPokemon(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ){
        return ResponseEntity.status(HttpStatus.OK).body(pokemonService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOnePokemon(@PathVariable(value = "id") UUID id){
        Optional<PokemonModel> pokemonModelOptional = pokemonService.findById(id);
        if (!pokemonModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pokemon não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(pokemonModelOptional.get());
    }

    @GetMapping("/search/type")
    public ResponseEntity<?> getPokemonByType(@RequestParam(value = "type") String type){
        Optional<List<Object[]>> pokemonModelOptional = pokemonService.findByType(type);
        if (pokemonModelOptional.get().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo não encontrado.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(pokemonModelOptional.get());
    }

    @GetMapping("/search/skill")
    public ResponseEntity<?> getPokemonBySkill(@RequestParam(value = "skill") String skill){
        Optional<List<Object[]>> pokemonModelOptional = pokemonService.findBySkill(skill);
        if (pokemonModelOptional.get().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Habilidade não encontrada.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(pokemonModelOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePokemon(@PathVariable(value = "id") UUID id){
        Optional<PokemonModel> pokemonModelOptional = pokemonService.findById(id);
        if (!pokemonModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pokemon não encontrado.");
        }
        pokemonService.delete(pokemonModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Pokemon deletado com sucesso.");
    }

    @PutMapping("/{id}")//atualizar pra poder pegar imagens
    public ResponseEntity<Object> updatePokemon(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid PokemonDto pokemonDto){
        Optional<PokemonModel> pokemonModelOptional = pokemonService.findById(id);
        if (!pokemonModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pokemon não encontrado.");
        }
        var pokemonModel = new PokemonModel();
        BeanUtils.copyProperties(pokemonDto, pokemonModel);
        pokemonModel.setId(pokemonModelOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(pokemonService.save(pokemonModel));
    }
}