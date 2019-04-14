package app.service.fileStorage;

import app.database.dao.ShipDao;
import app.database.model.Ship;
import app.database.model.ShipPhoto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;
import java.util.List;

class FileDeleteTaskTest extends FileStorageServiceTest {

    @Autowired
    ShipDao shipDao;

    private FileDeleteTask fileDeleteTask;

    @BeforeEach
    public void setUp() {
        super.setUp();
        fileDeleteTask = new FileDeleteTask(userDao, shipPhotoDao);
    }

    @Test
    void deletion() throws Exception {
        // todo
//        var userPh1 = fileStorageService.saveImage(new byte[]{1, 3, 4}, "userPh1.jpg", ImageKind.USER);
//        var userPhOld = fileStorageService.saveImage(new byte[]{1, 3, 4}, "userPh1.jpg", ImageKind.USER);

        var shipPh1 = fileStorageService.saveImage(new byte[]{1, 3, 4}, "ship1.jpg", ImageKind.SHIP);
        var shipPh2 = fileStorageService.saveImage(new byte[]{1, 3, 4}, "ship2.jpg", ImageKind.SHIP);
        var shipPh3 = fileStorageService.saveImage(new byte[]{1, 3, 4}, "ship3.jpg", ImageKind.SHIP);
        var shipPh4 = fileStorageService.saveImage(new byte[]{1, 3, 4}, "ship4.jpg", ImageKind.SHIP);

        var ship = new Ship();
        ship.setUser(user);
        ship.setName("ship1");
        ship.setLength(12.0);
        ship.setDraft(8.0);
        ship.setPhotos(List.of(new ShipPhoto(ship, 0, shipPh1.getName()), new ShipPhoto(ship, 1, shipPh2.getName())));
        shipDao.save(ship);

        setCreationTime(shipPh1, shipPh2, shipPh3);
        fileDeleteTask.run();

        Assertions.assertTrue(shipPh1.exists());
        Assertions.assertTrue(shipPh2.exists());
        Assertions.assertFalse(shipPh3.exists());
        Assertions.assertTrue(shipPh4.exists());    // new file
    }

    private void setCreationTime(File... files) throws IOException {
        for (var file : files) {
            var attr = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class);
            FileTime time = FileTime.fromMillis(System.currentTimeMillis() - FileDeleteTask.SURVIVE_TIME * 2);
            attr.setTimes(time, time, time);
        }
    }
}