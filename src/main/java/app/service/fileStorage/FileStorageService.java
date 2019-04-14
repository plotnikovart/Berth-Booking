package app.service.fileStorage;

import app.common.OperationContext;
import app.common.ServiceException;
import app.config.AppConfig;
import app.database.dao.ShipPhotoDao;
import app.database.dao.UserDao;
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

    private final OperationContext operationContext;
    private final UserDao userDao;

    private final ShipPhotoDao shipPhotoDao;
    private Timer timer = new Timer();

    public File saveImage(byte[] imageBytes, String fileNameOriginal, ImageKind imageKind) throws IOException {
        var fileName = UUID.randomUUID().toString() + getExtension(fileNameOriginal);

        var dirPath = getImageDirectoryPath(imageKind);
        Files.createDirectories(dirPath);
        var imagePath = dirPath.resolve(fileName);

        Files.write(imagePath, imageBytes);
        return imagePath.toFile();
    }

    public byte[] getImage(String fileName, ImageKind imageKind) throws IOException {
        var imageFile = getImageFile(fileName, imageKind).orElseThrow(() -> new ServiceException("Не найдено изображение" + fileName));
        return Files.readAllBytes(imageFile.toPath());
    }

    public Optional<File> getImageFile(String fileName, ImageKind imageKind) {
        var imageFile = new File(getImageDirectoryPath(imageKind).toFile(), fileName);
        return imageFile.exists() ? Optional.of(imageFile) : Optional.empty();
    }

    private Path getImageDirectoryPath(ImageKind imageKind) {
        var user = userDao.findByEmail(operationContext.getUserLogin()).orElseThrow();
        return Paths.get(AppConfig.FILES_FOLDER_PATH + "/" + imageKind.getFolderName() + "/" + user.getId());
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
        var task = new FileDeleteTask(userDao, shipPhotoDao);
        timer.scheduleAtFixedRate(task, 1000 * 60, FileDeleteTask.SURVIVE_TIME);
    }
}
