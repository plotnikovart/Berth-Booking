package app.controller;


import app.common.ServiceException;
import app.service.fileStorage.FileStorageService;
import app.service.fileStorage.ImageKind;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final FileStorageService fileStorageService;

    @PostMapping("/{imageKind}")
    public ResponseEntity<LoadResponse> loadImage(@PathVariable String imageKind, @RequestPart MultipartFile file) throws IOException {
        var image = fileStorageService.saveImage(file.getBytes(), file.getResource().getFilename(), ImageKind.get(imageKind));
        return ResponseEntity.ok(new LoadResponse(image.getName()));
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
