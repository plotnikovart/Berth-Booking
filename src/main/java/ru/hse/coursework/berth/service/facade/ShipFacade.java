package ru.hse.coursework.berth.service.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.database.entity.Ship;
import ru.hse.coursework.berth.database.repository.ShipRepository;
import ru.hse.coursework.berth.database.repository.UserInfoRepository;
import ru.hse.coursework.berth.service.PermissionService;
import ru.hse.coursework.berth.service.converters.impl.ShipConverter;
import ru.hse.coursework.berth.web.dto.ShipDto;

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
