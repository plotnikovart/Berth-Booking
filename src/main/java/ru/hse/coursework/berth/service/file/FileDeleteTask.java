package ru.hse.coursework.berth.service.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.hse.coursework.berth.common.SMessageSource;
import ru.hse.coursework.berth.database.entity.UserInfo;
import ru.hse.coursework.berth.database.repository.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileDeleteTask {

    final static long EXPIRE_FILE_TIME = 2 * 60 * 60 * 1000;   // 2 hour in millis

    private final FileInfoRepository fileInfoRepository;
    private final UserInfoRepository userInfoRepository;
    private final BerthRepository berthRepository;
    private final ShipRepository shipRepository;
    private final BerthApplicationRepository berthApplicationRepository;


    @Scheduled(cron = "0 0 2 * * ?")    // каждый день в 2 ночи
    public void run() {
        try {
            Stream<UUID> userInfoPhotos = userInfoRepository.findAll().stream().map(UserInfo::getPhoto);
            Stream<UUID> shipFiles = shipRepository.findAll().stream().flatMap(it -> it.getPhotos().stream());
            Stream<UUID> berthFiles = berthRepository.findAll().stream().flatMap(it -> it.getPhotos().stream());
            Stream<UUID> berthApplicationFiles = berthApplicationRepository.findAll().stream().flatMap(it -> it.getAttachements().stream());

            var usedFiles = Stream.of(userInfoPhotos, shipFiles, berthFiles, berthApplicationFiles)
                    .flatMap(s -> s)
                    .map(UUID::toString)
                    .collect(Collectors.toSet());

            var foundFiles = new LinkedList<File>();
            var startDir = new File(FileStorageService.BASE_FILE_DIR);
            findExpiredFiles(startDir, foundFiles);

            var filesToDelete = foundFiles.stream()
                    .filter(file -> !usedFiles.contains(file.getName()))
                    .peek(File::delete)
                    .peek(file -> fileInfoRepository.deleteById(UUID.fromString(file.getName())))
                    .collect(Collectors.toList());

            log.info(SMessageSource.message("files.deletion_complete", filesToDelete.size()));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void findExpiredFiles(File startDir, List<File> foundFiles) throws IOException {
        if (!startDir.exists()) {
            return;
        }

        for (var file : startDir.listFiles()) {
            if (file.isDirectory()) {
                findExpiredFiles(file, foundFiles);
            }

            if (file.isFile()) {
                var attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                if (System.currentTimeMillis() - attr.creationTime().toMillis() > EXPIRE_FILE_TIME) {
                    foundFiles.add(file);
                }
            }
        }
    }
}
