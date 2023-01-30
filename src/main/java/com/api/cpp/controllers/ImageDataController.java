package com.api.cpp.controllers;

import com.api.cpp.models.ImageData;
import com.api.cpp.services.ImageDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/image")
public class ImageDataController {

    @Autowired
    private ImageDataService imageDataService;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        Long response = imageDataService.uploadImage(file);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<?>  getImageInfoByName(@PathVariable("id") Long id) {
        ImageData image = imageDataService.getInfoByImageById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(image);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?>  getImageByName(@PathVariable("id") Long id) {
        byte[] image = imageDataService.getImage(id);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(image);
    }
}