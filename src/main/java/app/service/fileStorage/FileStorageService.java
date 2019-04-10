package app.service.fileStorage;

import app.common.OperationContext;
import app.common.ServiceException;
import app.config.AppConfig;
import app.database.dao.ShipPhotoDao;
import app.database.dao.UserDao;
import app.database.model.ShipPhoto;
import org.springframework.beans.factory.annotation.Autowired;
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
public class FileStorageService {

    private UserDao userDao;
    private ShipPhotoDao shipPhotoDao;
    private OperationContext operationContext;
    private Timer timer;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setShipPhotoDao(ShipPhotoDao shipPhotoDao) {
        this.shipPhotoDao = shipPhotoDao;
    }

    @Autowired
    public void setOperationContext(OperationContext operationContext) {
        this.operationContext = operationContext;
    }

    public String saveImage(byte[] imageBytes, String fileNameOriginal, ImageKind imageKind) throws IOException {
        var fileName = UUID.randomUUID().toString() + getExtension(fileNameOriginal);

        var dirPath = getImageDirectoryPath(imageKind);
        Files.createDirectories(dirPath);
        var imagePath = dirPath.resolve(fileName);

        Files.write(imagePath, imageBytes);
        return fileName;
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
        var task = new FileDeleteTask();
        task.setShipPhotoDao(shipPhotoDao);
        timer.scheduleAtFixedRate(task, 1000 * 60, FileDeleteTask.SURVIVE_TIME);
    }
}
