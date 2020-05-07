package ru.hse.coursework.berth.web;

import app.config.validation.ValidationUtils;
import app.service.facade.ShipFacade;
import app.web.dto.ShipDto;
import app.web.dto.response.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ships")
@RequiredArgsConstructor
public class ShipController {

    private final ShipFacade shipFacade;

    @PostMapping
    public IdResponse<Long> createShip(@RequestBody ShipDto.Req shipDto) {
        ValidationUtils.validateEntity(shipDto);
        Long shipId = shipFacade.createShip(shipDto);
        return new IdResponse<>(shipId);
    }

    @PutMapping("/{id}")
    public void updateShip(@RequestBody ShipDto.Req shipDto, @PathVariable Long id) {
        ValidationUtils.validateEntity(shipDto);
        shipFacade.updateShip(id, shipDto);
    }

    @GetMapping
    public List<ShipDto.Resp> getShips() {
        return shipFacade.getShips();
    }

    @GetMapping("/{id}")
    public ShipDto.Resp getShip(@PathVariable Long id) {
        return shipFacade.getShip(id);
    }

    @DeleteMapping("/{id}")
    public void deleteShip(@PathVariable Long id) {
        shipFacade.deleteShip(id);
    }
}
