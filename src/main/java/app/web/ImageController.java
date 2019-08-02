package app.web;


import app.common.exception.NotFoundException;
import app.service.file.FileStorageService;
import app.service.file.ImageKind;
import app.web.dto.response.FileLoadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final FileStorageService fileStorageService;

    @PostMapping("/{imageKind}")
    public FileLoadResponse loadImage(@PathVariable String imageKind, @RequestPart MultipartFile file) throws IOException {
        var image = fileStorageService.saveImage(file.getBytes(), file.getResource().getFilename(), ImageKind.get(imageKind));
        return new FileLoadResponse(image.getName());
    }

    @GetMapping("/{imageKind}/{fileName:.+}")
    public FileSystemResource getImage(@PathVariable String imageKind, @PathVariable String fileName) {
        var imageFile = fileStorageService.getImageFile(fileName, ImageKind.get(imageKind)).orElseThrow(NotFoundException::new);
        return new FileSystemResource(imageFile);
    }
}
