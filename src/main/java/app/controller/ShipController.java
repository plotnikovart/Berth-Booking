package app.controller;

import app.common.IdResponse;
import app.common.OperationContext;
import app.common.ValidationUtils;
import app.database.dao.ShipDao;
import app.database.dao.ShipPhotoDao;
import app.database.dao.UserDao;
import app.database.model.Ship;
import app.database.model.ShipPhoto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ships")
@RequiredArgsConstructor
public class ShipController {

    private final OperationContext operationContext;
    private final UserDao userDao;
    private final ShipDao shipDao;
    private final ShipPhotoDao shipPhotoDao;

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<IdResponse> createShip(@RequestBody ShipDTO shipDTO) {
        var user = userDao.findByEmail(operationContext.getUserLogin()).orElseThrow();
        var ship = convertToShip(shipDTO);
        ship.setUser(user);

        ValidationUtils.validateEntity(ship);
        shipDao.save(ship);
        return new ResponseEntity<>(new IdResponse<>(ship.getId()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<IdResponse> updateShip(@RequestBody ShipDTO shipDTO, @PathVariable Long id) {
        var user = userDao.findByEmail(operationContext.getUserLogin()).orElseThrow();
        var shipOpt = shipDao.findById(id);

        if (shipOpt.isPresent() && shipOpt.get().getUser().equals(user)) {
            var ship = shipOpt.get();
            convertToShip(shipDTO, ship);

            ValidationUtils.validateEntity(ship);
            shipDao.save(ship);
            return new ResponseEntity<>(new IdResponse<>(id), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<ShipDTO>> getShips() {
        var user = userDao.findByEmail(operationContext.getUserLogin()).orElseThrow();
        var dtoList = user.getShips().stream().map(this::convertToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<ShipDTO> getShip(@PathVariable Long id) {
        var user = userDao.findByEmail(operationContext.getUserLogin()).orElseThrow();
        var shipOpt = shipDao.findById(id);

        if (shipOpt.isPresent() && shipOpt.get().getUser().equals(user)) {
            var ship = shipOpt.get();
            return new ResponseEntity<>(convertToDTO(ship), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Void> deleteShip(@PathVariable Long id) {
        var user = userDao.findByEmail(operationContext.getUserLogin()).orElseThrow();
        var shipOpt = shipDao.findById(id);

        if (shipOpt.isPresent() && shipOpt.get().getUser().equals(user)) {
            shipDao.delete(shipOpt.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private Ship convertToShip(ShipDTO dto) {
        var ship = new Ship();
        convertToShip(dto, ship);
        return ship;
    }

    private void convertToShip(ShipDTO dto, Ship ship) {
        ship.setName(dto.getName());
        ship.setLength(dto.getLength());
        ship.setDraft(dto.getDraft());
        ship.setWidth(dto.getWidth());
        var i = new AtomicInteger(0);
        ship.setPhotos(dto.getFileNames().stream().map(fileName -> new ShipPhoto(ship, i.getAndIncrement(), fileName)).collect(Collectors.toList()));
    }

    private ShipDTO convertToDTO(Ship ship) {
        var dto = new ShipDTO();
        dto.setId(ship.getId());
        dto.setName(ship.getName());
        dto.setLength(ship.getLength());
        dto.setDraft(ship.getDraft());
        dto.setWidth(ship.getWidth());
        dto.setFileNames(ship.getPhotos().stream().map(ShipPhoto::getFileName).collect(Collectors.toList()));
        return dto;
    }

    @Data
    private static class ShipDTO {
        private Long id;
        private String name;
        private Double length;
        private Double draft;
        private Double width;
        private List<String> fileNames = new LinkedList<>();
    }
}
