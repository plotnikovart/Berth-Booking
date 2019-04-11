package app.service.fileStorage;

import app.config.AppConfig;
import app.database.dao.ShipPhotoDao;
import app.database.dao.UserDao;
import app.database.model.ShipPhoto;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class FileDeleteTask extends TimerTask {

    private final static Logger LOGGER = Logger.getRootLogger();
    final static int SURVIVE_TIME = 24 * 60 * 60 * 1000;   // 1 day in millis
    private UserDao userDao;
    private ShipPhotoDao shipPhotoDao;

    void setShipPhotoDao(ShipPhotoDao shipPhotoDao) {
        this.shipPhotoDao = shipPhotoDao;
    }

    @Override
    @Transactional(readOnly = true)
    public void run() {
        try {
            Stream<String> userFiles = Stream.empty();
            Stream<String> shipFiles = StreamSupport.stream(shipPhotoDao.findAll().spliterator(), false).map(ShipPhoto::getFileName);
            Stream<String> berthFiles = Stream.empty();

            var usedFiles = Stream.of(userFiles, shipFiles, berthFiles).flatMap(s -> s).collect(Collectors.toSet());

            var foundFiles = new LinkedList<File>();
            var startDir = new File(AppConfig.FILES_FOLDER_PATH);
            findFiles(startDir, foundFiles);

            var filesToDelete = foundFiles.stream()
                    .filter(file -> !usedFiles.contains(file.getName()))
                    .peek(File::delete)
                    .collect(Collectors.toList());

            LOGGER.info("Процедура удаления файлов завершена. Удалено - " + filesToDelete.size());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void findFiles(File startDir, List<File> foundFiles) throws IOException {
        for (var file : startDir.listFiles()) {
            if (file.isDirectory()) {
                findFiles(startDir, foundFiles);
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
