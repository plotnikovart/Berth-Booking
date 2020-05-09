package ru.hse.coursework.berth.service.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.database.entity.Ship;
import ru.hse.coursework.berth.database.repository.AccountRepository;
import ru.hse.coursework.berth.database.repository.ShipRepository;
import ru.hse.coursework.berth.service.PermissionService;
import ru.hse.coursework.berth.service.SoftDeleteService;
import ru.hse.coursework.berth.service.converters.impl.ShipConverter;
import ru.hse.coursework.berth.web.dto.ShipDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShipCRUDService {

    private final AccountRepository accountRepository;
    private final ShipRepository shipRepository;
    private final ShipConverter converter;
    private final PermissionService permissionService;
    private final SoftDeleteService softDeleteService;

    public Long create(ShipDto dto) {
        var ship = new Ship().setOwner(accountRepository.findCurrent());
        converter.toEntity(ship, dto);

        return shipRepository.save(ship).getId();
    }

    @Transactional
    public void update(Long id, ShipDto dto) {
        var ship = shipRepository.findById(id).orElseThrow(NotFoundException::new);
        permissionService.check(ship);

        converter.toEntity(ship, dto);
    }

    public ShipDto.Resp get(Long shipId) {
        var ship = shipRepository.findById(shipId).orElseThrow(NotFoundException::new);
        return converter.toDto(ship);
    }

    public List<ShipDto.Resp> get() {
        List<Ship> ships = shipRepository.findAllByOwner(accountRepository.findCurrent());
        return converter.toDtos(ships);
    }

    @Transactional
    public void delete(Long shipId) {
        var ship = shipRepository.findById(shipId).orElseThrow(NotFoundException::new);
        permissionService.check(ship);
        softDeleteService.delete(ship);
    }
}
