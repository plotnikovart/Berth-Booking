package app.controller;


import app.service.fileStorage.ImageKind;
import app.common.ServiceException;
import app.service.fileStorage.FileStorageService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private FileStorageService fileStorageService;

    @Autowired
    public void setFileStorageService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/{imageKind}")
    public ResponseEntity<LoadResponse> loadImage(@PathVariable String imageKind, @RequestPart MultipartFile file) throws IOException {
        var fileName = fileStorageService.saveImage(file.getBytes(), file.getResource().getFilename(), ImageKind.get(imageKind));
        return ResponseEntity.ok(new LoadResponse(fileName));
    }

    @GetMapping("/{imageKind}/{fileName:.+}")
    public FileSystemResource getImage(@PathVariable String imageKind, @PathVariable String fileName) {
        var imageFile = fileStorageService.getImageFile(fileName, ImageKind.get(imageKind)).orElseThrow(() -> new ServiceException("Не найдено изображение"));
        return new FileSystemResource(imageFile);
    }

    @Getter
    @AllArgsConstructor
    private static class LoadResponse {
        private String fileName;
    }
}
