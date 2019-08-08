package app.service.file;

import app.common.exception.NotFoundException;
import app.config.AppConfig;
import app.database.repository.AccountRepository;
import app.database.repository.ShipPhotoRepository;
import app.database.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Timer;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final AccountRepository accountRepository;
    private final UserInfoRepository userInfoRepository;
    private final ShipPhotoRepository shipPhotoRepository;

    private final Timer timer = new Timer();

    public File saveImage(byte[] imageBytes, String fileNameOriginal, ImageKind imageKind) throws IOException {
        var fileName = UUID.randomUUID().toString() + getExtension(fileNameOriginal);

        var dirPath = getImageDirectoryPath(imageKind);
        Files.createDirectories(dirPath);
        var imagePath = dirPath.resolve(fileName);

        Files.write(imagePath, imageBytes);
        return imagePath.toFile();
    }

    public byte[] getImage(String fileName, ImageKind imageKind) throws NotFoundException, IOException {
        var imageFile = getImageFile(fileName, imageKind).orElseThrow(NotFoundException::new);
        return Files.readAllBytes(imageFile.toPath());
    }

    public Optional<File> getImageFile(String fileName, ImageKind imageKind) {
        var imageFile = new File(getImageDirectoryPath(imageKind).toFile(), fileName);
        return imageFile.exists() ? Optional.of(imageFile) : Optional.empty();
    }

    private Path getImageDirectoryPath(ImageKind imageKind) {
        var account = accountRepository.findCurrent();
        return Paths.get(AppConfig.FILES_FOLDER_PATH + "/" + imageKind.getFolderName() + "/" + account.getId());
    }

    private String getExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index > 0) {
            return fileName.substring(index);
        }

        return "";
    }

    @PostConstruct
    public void launchFileDeleteTask() {
        var task = new FileDeleteTask(userInfoRepository, shipPhotoRepository);
        timer.scheduleAtFixedRate(task, 1000 * 60, FileDeleteTask.SURVIVE_TIME);
    }
}
