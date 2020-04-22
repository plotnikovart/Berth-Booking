package app.service.facade;

import app.config.exception.impl.NotFoundException;
import app.database.entity.Ship;
import app.database.repository.ShipRepository;
import app.database.repository.UserInfoRepository;
import app.service.PermissionService;
import app.service.converters.impl.ShipConverter;
import app.web.dto.ShipDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShipFacade {

    private final UserInfoRepository userInfoRepository;
    private final ShipRepository shipRepository;
    private final ShipConverter converter;
    private final PermissionService permissionService;

    @Transactional
    public Long createShip(ShipDto.Req shipDto) {
        var userInfo = userInfoRepository.findCurrent();

        var ship = new Ship().setUserInfo(userInfo);
        converter.toEntity(ship, shipDto);

        return shipRepository.save(ship).getId();
    }

    @Transactional
    public void updateShip(Long id, ShipDto.Req shipDto) {
        var ship = shipRepository.findById(id).orElseThrow(NotFoundException::new);
        permissionService.check(ship);

        converter.toEntity(ship, shipDto);
        shipRepository.save(ship);
    }

    @Transactional(readOnly = true)
    public ShipDto.Resp getShip(Long shipId) {
        var ship = shipRepository.findById(shipId).orElseThrow(NotFoundException::new);
        return converter.toDto(ship);
    }

    @Transactional(readOnly = true)
    public List<ShipDto.Resp> getShips() {
        var userInfo = userInfoRepository.findCurrent();
        var ships = userInfo.getShips();

        return converter.toDtos(ships);
    }

    @Transactional
    public void deleteShip(Long shipId) {
        var ship = shipRepository.findById(shipId).orElseThrow(NotFoundException::new);
        permissionService.check(ship);
        shipRepository.delete(ship);
    }
}
