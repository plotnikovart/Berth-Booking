package app.service.facade;

import app.common.exception.NotFoundException;
import app.database.entity.Ship;
import app.database.repository.ShipRepository;
import app.database.repository.UserInfoRepository;
import app.service.PermissionService;
import app.web.dto.ShipDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShipFacade {

    private final UserInfoRepository userInfoRepository;
    private final ShipRepository shipRepository;
    private final PermissionService permissionService;

    @Transactional
    public Long createShip(ShipDto shipDto) {
        var userInfo = userInfoRepository.findCurrent();

        var ship = new Ship(userInfo, shipDto);
        return shipRepository.save(ship).getId();
    }

    @Transactional
    public void updateShip(ShipDto.WithId shipDto) {
        var ship = shipRepository.findById(shipDto.getId()).orElseThrow(NotFoundException::new);
        permissionService.changeEntity(ship);

        ship.setDto(shipDto);
    }

    @Transactional(readOnly = true)
    public ShipDto.WithId getShip(Long shipId) {
        var ship = shipRepository.findById(shipId).orElseThrow(NotFoundException::new);
        return ship.getDto();
    }

    @Transactional(readOnly = true)
    public List<ShipDto.WithId> getShips() {
        var userInfo = userInfoRepository.findCurrent();
        return userInfo.getShips().stream().map(Ship::getDto).collect(Collectors.toList());
    }

    @Transactional
    public void deleteShip(Long shipId) {
        var ship = shipRepository.findById(shipId).orElseThrow(NotFoundException::new);
        permissionService.changeEntity(ship);
        shipRepository.delete(ship);
    }
}
