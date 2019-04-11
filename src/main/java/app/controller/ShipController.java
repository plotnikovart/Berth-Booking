package app.controller;

import app.common.OperationContext;
import app.database.dao.ShipDao;
import app.database.dao.UserDao;
import app.database.model.Ship;
import app.database.model.ShipPhoto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ships")
@RequiredArgsConstructor
public class ShipController {

    private final OperationContext operationContext;
    private final UserDao userDao;
    private final ShipDao shipDao;

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Long> createShip(ShipDTO shipDTO) {
        var user = userDao.findByEmail(operationContext.getUserLogin()).orElseThrow();
        var ship = convertToShip(shipDTO);
        ship.setUser(user);

        shipDao.saveWithValidation(ship);
        return ResponseEntity.ok(ship.getId());
    }

    private Ship convertToShip(ShipDTO dto) {
        var ship = new Ship();
        ship.setName(dto.getName());
        ship.setLength(dto.getLength());
        ship.setDraft(dto.getDraft());
        ship.setWidth(dto.getWidth());
        ship.setPhotos(dto.getFileNames().stream().map(fileName -> new ShipPhoto(ship, 0, fileName)).collect(Collectors.toList()));
        return ship;
    }

    @Data
    private static class ShipDTO {
        private String name;
        private Double length;
        private Double draft;
        private Double width;
        private List<String> fileNames;
    }
}
