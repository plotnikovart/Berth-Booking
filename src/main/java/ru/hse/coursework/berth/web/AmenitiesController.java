package ru.hse.coursework.berth.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.coursework.berth.service.AmenityService;
import ru.hse.coursework.berth.service.berth.dto.DictAmenityDto;
import ru.hse.coursework.berth.web.dto.response.ListResp;

import java.util.List;

@RestController
@RequestMapping("/api/amenities")
@RequiredArgsConstructor
public class AmenitiesController {

    private final AmenityService amenityService;

    @GetMapping
    public ListResp<DictAmenityDto> getAll() {
        List<DictAmenityDto> resp = amenityService.getAll();
        return new ListResp<>(resp);
    }
}
