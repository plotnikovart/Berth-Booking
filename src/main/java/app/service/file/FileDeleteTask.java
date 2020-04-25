package app.service.file;

import app.database.repository.BerthPhotoRepository;
import app.database.repository.ShipPhotoRepository;
import app.database.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.TimerTask;

@Slf4j
@RequiredArgsConstructor
public class FileDeleteTask extends TimerTask {

    final static long SURVIVE_TIME = 24 * 60 * 60 * 1000;   // 1 day in millis

    private final UserInfoRepository userInfoRepository;
    private final ShipPhotoRepository shipPhotoRepository;
    private final BerthPhotoRepository berthPhotoRepository;

    @Override
    @Transactional(readOnly = true)
    public void run() {
//        try {
//            Stream<String> userFiles = Stream.of();//userInfoRepository.findAll().stream().map(UserInfo::getPhotoName);
//            Stream<String> shipFiles = shipPhotoRepository.findAll().stream().map(ShipPhoto::getFileName);
//            Stream<String> berthFiles = berthPhotoRepository.findAll().stream().map(BerthPhoto::getFileName);
//
//            var usedFiles = Stream.of(userFiles, shipFiles, berthFiles).flatMap(s -> s).collect(Collectors.toSet());
//
//            var foundFiles = new LinkedList<File>();
//            var startDir = new File(AppConfig.FILES_FOLDER_PATH);
//            findFiles(startDir, foundFiles);
//
//            var filesToDelete = foundFiles.stream()
//                    .filter(file -> !usedFiles.contains(file.getName()))
//                    .peek(File::delete)
//                    .collect(Collectors.toList());
//
//            log.info(SMessageSource.message("files.deletion_complete", filesToDelete.size()));
//        } catch (IOException e) {
//            log.error(e.getMessage(), e);
//        }
    }

    private void findFiles(File startDir, List<File> foundFiles) throws IOException {
        if (!startDir.exists()) {
            return;
        }

        for (var file : startDir.listFiles()) {
            if (file.isDirectory()) {
                findFiles(file, foundFiles);
            }

            if (file.isFile()) {
                var attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                if (System.currentTimeMillis() - attr.creationTime().toMillis() > SURVIVE_TIME) {
                    foundFiles.add(file);
                }
            }
        }
    }
}
