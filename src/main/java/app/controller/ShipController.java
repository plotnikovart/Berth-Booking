package app.controller;

import app.common.OperationContext;
import app.common.ValidationUtils;
import app.database.dao.ShipDao;
import app.database.dao.UserDao;
import app.database.model.Ship;
import app.database.model.ShipPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/ships")
public class ShipController {

    private OperationContext operationContext;
    private UserDao userDao;
    private ShipDao shipDao;

    @Autowired
    public void setOperationContext(OperationContext operationContext) {
        this.operationContext = operationContext;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setShipDao(ShipDao shipDao) {
        this.shipDao = shipDao;
    }

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Long> createShip(Ship ship, @RequestPart(required = false) MultipartFile[] photoFiles) throws IOException {
        var user = userDao.findByEmail(operationContext.getUserLogin()).orElseThrow();
        ship.setUser(user);
        ship.setId(null);

        if (photoFiles != null) {
            for (int i = 0; i < photoFiles.length; i++) {
                var shipPhoto = new ShipPhoto(ship, i, photoFiles[i].getBytes());
                ship.getPhotos().add(shipPhoto);
            }
        }

        ValidationUtils.validateEntity(ship);
        shipDao.save(ship);
        return ResponseEntity.ok(ship.getId());
    }
}
