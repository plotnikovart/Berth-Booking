package app.web;

import app.service.berth.dto.DictAmenityDto;
import app.service.facade.AmenityFacade;
import app.web.dto.response.ListResp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/amenities")
@RequiredArgsConstructor
public class AmenitiesController {

    private final AmenityFacade amenityFacade;

    @GetMapping
    public ListResp<DictAmenityDto> getAll() {
        List<DictAmenityDto> resp = amenityFacade.getAll();
        return new ListResp<>(resp);
    }
}
