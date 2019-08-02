package app.web;

import app.common.ValidationUtils;
import app.service.ShipFacade;
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
    public IdResponse<Long> createShip(@RequestBody ShipDto shipDto) {
        ValidationUtils.validateEntity(shipDto);
        Long shipId = shipFacade.createShip(shipDto);
        return new IdResponse<>(shipId);
    }

    @PutMapping("/{id}")
    public IdResponse<Long> updateShip(@RequestBody ShipDto.WithId shipDto, @PathVariable Long id) {
        shipDto.setId(id);
        ValidationUtils.validateEntity(shipDto);
        id = shipFacade.updateShip(shipDto);
        return new IdResponse<>(id);
    }

    @GetMapping
    public List<ShipDto.WithId> getShips() {
        return shipFacade.getShips();
    }

    @GetMapping("/{id}")
    public ShipDto.WithId getShip(@PathVariable Long id) {
        return shipFacade.getShip(id);
    }

    @DeleteMapping("/{id}")
    public void deleteShip(@PathVariable Long id) {
        shipFacade.deleteShip(id);
    }
}
