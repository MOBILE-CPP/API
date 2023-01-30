package com.api.cpp.services;

import com.api.cpp.models.ImageData;
import com.api.cpp.models.PokemonModel;
import com.api.cpp.repositories.ImageDataRepository;
import com.api.cpp.utils.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageDataService {
    @Autowired
    private ImageDataRepository imageDataRepository;

    public Long uploadImage(MultipartFile file) throws IOException {
        ImageData imageReturn = imageDataRepository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtil.compressImage(file.getBytes())).build());

        return imageReturn.getId();
    }

    @Transactional
    public ImageData getInfoByImageById(Long id) {
        Optional<ImageData> dbImage = imageDataRepository.findById(id);

        return ImageData.builder()
                .name(dbImage.get().getName())
                .type(dbImage.get().getType())
                .imageData(ImageUtil.decompressImage(dbImage.get().getImageData())).build();

    }

    @Transactional
    public byte[] getImage(Long id) {
        Optional<ImageData> dbImage = imageDataRepository.findById(id);
        byte[] image = ImageUtil.decompressImage(dbImage.get().getImageData());
        return image;
    }

    public Optional<ImageData> findById(Long id) {
        return imageDataRepository.findById(id);
    }
}