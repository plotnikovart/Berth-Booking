package ru.hse.coursework.berth.web;


import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hse.coursework.berth.service.file.FileStorageService;
import ru.hse.coursework.berth.service.file.dto.FileInfoDto;
import ru.hse.coursework.berth.web.dto.response.ObjectResp;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.http.HttpHeaders.*;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping
    public ObjectResp<FileInfoDto> loadFile(@RequestPart MultipartFile file) throws IOException {
        FileInfoDto fileInfo = fileStorageService.saveFile(file.getBytes(), file.getResource().getFilename());
        return new ObjectResp<>(fileInfo);
    }

    @GetMapping(value = "/{code}/{fileId}")
    public ResponseEntity<FileSystemResource> getFile(@PathVariable String code, @PathVariable String fileId) {
        Pair<FileInfoDto, File> file = fileStorageService.getFile(UUID.fromString(fileId), code);

        var headers = new HttpHeaders();
        headers.set(CONTENT_TYPE, URLConnection.guessContentTypeFromName(file.getLeft().getFileName()));
        headers.set(CONTENT_DISPOSITION, format("inline; filename=\"%s\"", file.getLeft().getFileName()));
        headers.set(CACHE_CONTROL, "private, max-age=3600");

        return new ResponseEntity<>(new FileSystemResource(file.getRight()), headers, HttpStatus.OK);
    }
}
