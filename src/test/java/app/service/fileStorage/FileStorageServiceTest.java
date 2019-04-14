package app.service.fileStorage;

import app.common.OperationContext;
import app.config.AppConfig;
import app.database.dao.AbstractUserTest;
import app.database.dao.ShipPhotoDao;
import org.assertj.core.util.Files;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.unitils.reflectionassert.ReflectionAssert;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.when;

@Transactional
@RunWith(MockitoJUnitRunner.class)
class FileStorageServiceTest extends AbstractUserTest {

    @Mock
    OperationContext context;
    @Autowired
    ShipPhotoDao shipPhotoDao;
    FileStorageService fileStorageService;

    @BeforeEach
    public void setUp() {
        super.setUp();
        when(context.getUserLogin()).thenReturn(user.getEmail());
        fileStorageService = new FileStorageService(context, userDao, shipPhotoDao);

        Files.delete(new File(AppConfig.FILES_FOLDER_PATH));
    }

    @Test
    void saveGet() throws IOException {
        var image = new byte[]{1, 2, 120};
        var originalFileName = "image1";
        var imageKind = ImageKind.USER;

        var file = fileStorageService.saveImage(image, originalFileName, imageKind);
        var actual = fileStorageService.getImage(file.getName(), imageKind);

        ReflectionAssert.assertReflectionEquals(actual, image);
    }
}