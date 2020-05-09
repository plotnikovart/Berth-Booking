package ru.hse.coursework.berth.service.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.hse.coursework.berth.common.SMessageSource;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.config.exception.impl.ServiceException;
import ru.hse.coursework.berth.service.file.dto.FileInfoDto;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageService {

    public static final String BASE_FILE_DIR = "." + "/files";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");

    private final FileInfoService fileInfoService;


    public FileInfoDto saveFile(byte[] file, String fileName) {
        try {
            FileInfoDto fileInfo = fileInfoService.save(fileName);

            Path pathToDir = pathToDir(fileInfo.getFileId());
            Path pathToFile = pathToDir.resolve(fileInfo.getFileId().toString());

            Files.createDirectories(pathToDir);
            Files.write(pathToFile, file);

            return fileInfo;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(SMessageSource.message("file.not_save"), e);
        }
    }

    public Pair<FileInfoDto, File> getFile(UUID fileId, String code) throws NotFoundException {
        FileInfoDto fileInfo = fileInfoService.getWithCheck(fileId, code);
        File file = pathToDir(fileInfo.getFileId()).resolve(fileId.toString()).toFile();

        if (!file.exists()) {
            throw new NotFoundException();
        }

        return Pair.of(fileInfo, file);
    }


    private Path pathToDir(UUID fileId) {
        LocalDate date = toLocalDate(fileId);
        return Path.of(BASE_FILE_DIR, date.format(DATE_FORMATTER));
    }


    private LocalDate toLocalDate(UUID uuid) {
        return Instant.ofEpochMilli(uuid.timestamp() / 10000L - 12219292800000L).atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
