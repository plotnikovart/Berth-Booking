package ru.hse.coursework.berth.web;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hse.coursework.berth.service.ship.ShipCRUDService;
import ru.hse.coursework.berth.service.ship.dto.ShipDto;
import ru.hse.coursework.berth.web.dto.resp.EmptyResp;
import ru.hse.coursework.berth.web.dto.resp.ListResp;
import ru.hse.coursework.berth.web.dto.resp.ObjectResp;

import java.util.List;

@RestController
@RequestMapping("/api/ships")
@RequiredArgsConstructor
public class ShipController {

    private final ShipCRUDService shipCRUDService;

    @PostMapping
    public ObjectResp<ShipDto.Resp> createShip(@RequestBody ShipDto shipDto) {
        Long shipId = shipCRUDService.create(shipDto);
        ShipDto.Resp resp = shipCRUDService.get(shipId);
        return new ObjectResp<>(resp);
    }

    @PutMapping("/{id}")
    public ObjectResp<ShipDto.Resp> updateShip(@PathVariable Long id, @RequestBody ShipDto shipDto) {
        shipCRUDService.update(id, shipDto);
        ShipDto.Resp resp = shipCRUDService.get(id);
        return new ObjectResp<>(resp);
    }

    @GetMapping
    public ListResp<ShipDto.Resp> getShips() {
        List<ShipDto.Resp> resp = shipCRUDService.get();
        return new ListResp<>(resp);
    }

    @DeleteMapping("/{id}")
    public EmptyResp deleteShip(@PathVariable Long id) {
        shipCRUDService.delete(id);
        return new EmptyResp();
    }
}
