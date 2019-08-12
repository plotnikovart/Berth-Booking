package app.service.file;

import app.common.OperationContext;
import app.common.exception.NotFoundException;
import app.config.AppConfig;
import app.database.repository.AbstractAccountTest;
import app.database.repository.BerthPhotoRepository;
import app.database.repository.ShipPhotoRepository;
import org.assertj.core.util.Files;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.unitils.reflectionassert.ReflectionAssert;

import java.io.File;
import java.io.IOException;

class FileStorageServiceTest extends AbstractAccountTest {

    @Autowired
    ShipPhotoRepository shipPhotoRepository;
    @Autowired
    BerthPhotoRepository berthPhotoRepository;
    @Autowired
    FileStorageService fileStorageService;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        OperationContext.setAccountId(userInfo.getAccountId());
        Files.delete(new File(AppConfig.FILES_FOLDER_PATH));
    }

    @Test
    void saveAndGet() throws IOException {
        var image = new byte[]{1, 2, 120};
        var originalFileName = "image1";
        var imageKind = ImageKind.USER;

        var file = fileStorageService.saveImage(image, originalFileName, imageKind);
        var actual = fileStorageService.getImage(imageKind, account.getId(), file.getName());
        ReflectionAssert.assertReflectionEquals(actual, image);

        image = new byte[]{1, 2};
        originalFileName = "image.png";
        imageKind = ImageKind.SHIP;

        file = fileStorageService.saveImage(image, originalFileName, imageKind);
        actual = fileStorageService.getImage(imageKind, account.getId(), file.getName());
        ReflectionAssert.assertReflectionEquals(actual, image);

        Assertions.assertThrows(NotFoundException.class, () -> fileStorageService.getImage(ImageKind.SHIP, account.getId(), "1"));
    }
}