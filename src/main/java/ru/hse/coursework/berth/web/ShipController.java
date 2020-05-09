package ru.hse.coursework.berth.web;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hse.coursework.berth.service.facade.ShipCRUDService;
import ru.hse.coursework.berth.web.dto.ShipDto;
import ru.hse.coursework.berth.web.dto.response.EmptyResp;
import ru.hse.coursework.berth.web.dto.response.ListResp;
import ru.hse.coursework.berth.web.dto.response.ObjectResp;

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

    @GetMapping("/{id}")
    public ObjectResp<ShipDto.Resp> getShip(@PathVariable Long id) {
        ShipDto.Resp resp = shipCRUDService.get(id);
        return new ObjectResp<>(resp);
    }

    @DeleteMapping("/{id}")
    public EmptyResp deleteShip(@PathVariable Long id) {
        shipCRUDService.delete(id);
        return new EmptyResp();
    }
}
