package app.service.file;

import app.database.repository.BerthPhotoRepository;
import app.database.repository.FileInfoRepository;
import app.database.repository.ShipPhotoRepository;
import app.database.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static app.common.SMessageSource.message;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileDeleteTask {

    final static long EXPIRE_FILE_TIME = 24 * 60 * 60 * 1000;   // 1 day in millis

    private final FileInfoRepository fileInfoRepository;
    private final UserInfoRepository userInfoRepository;
    private final ShipPhotoRepository shipPhotoRepository;
    private final BerthPhotoRepository berthPhotoRepository;


    @Scheduled(cron = "0 0 2 * * ?")    // каждый день в 2 ночи
    public void run() {
        try {
            var sinceDate = LocalDateTime.now().minusDays(1L);

            Stream<UUID> userInfoPhotos = userInfoRepository.findUpdatedPhotos(sinceDate).stream();
            Stream<UUID> shipFiles = Stream.of();//shipPhotoRepository.findAll().stream().map(ShipPhoto::getFileName);
            Stream<UUID> berthFiles = Stream.of();//berthPhotoRepository.findAll().stream().map(BerthPhoto::getFileName);

            var usedFiles = Stream.of(userInfoPhotos, shipFiles, berthFiles)
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

            log.info(message("files.deletion_complete", filesToDelete.size()));
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
