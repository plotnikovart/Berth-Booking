package app.service;

import app.common.exception.NotFoundException;
import app.database.entity.Ship;
import app.database.repository.ShipRepository;
import app.database.repository.UserRepository;
import app.web.dto.ShipDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShipFacade {

    private final UserRepository userRepository;
    private final ShipRepository shipRepository;
    private final PermissionService permissionService;

    @Transactional
    public Long createShip(ShipDto shipDto) {
        var user = userRepository.findCurrent();
        var ship = new Ship(user, shipDto);
        ship = shipRepository.save(ship);
        return ship.getId();
    }

    @Transactional
    public Long updateShip(ShipDto.WithId shipDto) {
        var ship = shipRepository.findById(shipDto.getId()).orElseThrow(NotFoundException::new);
        permissionService.checkPermission(ship);

        ship.setDto(shipDto);
        shipRepository.save(ship);
        return ship.getId();
    }

    @Transactional(readOnly = true)
    public ShipDto.WithId getShip(Long shipId) {
        var ship = shipRepository.findById(shipId).orElseThrow(NotFoundException::new);
        return ship.getDto();
    }

    @Transactional(readOnly = true)
    public List<ShipDto.WithId> getShips() {
        var currentUser = userRepository.findCurrent();
        return shipRepository.findAllByUser(currentUser).stream().map(Ship::getDto).collect(Collectors.toList());
    }

    @Transactional
    public void deleteShip(Long shipId) {
        var ship = shipRepository.findById(shipId).orElseThrow(NotFoundException::new);
        permissionService.checkPermission(ship);
        shipRepository.delete(ship);
    }
}
