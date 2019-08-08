package app.web;

import app.common.ValidationUtils;
import app.service.facade.BerthFacade;
import app.web.dto.BerthDto;
import app.web.dto.response.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/berths")
@RequiredArgsConstructor
public class BerthController {

    private final BerthFacade berthFacade;

    @PostMapping
    public IdResponse<Long> createBerth(@RequestBody BerthDto berthDto) {
        validateBerthDto(berthDto);
        long id = berthFacade.createBerth(berthDto);
        return new IdResponse<>(id);
    }

    @PutMapping("/{id}")
    public void updateBerth(@RequestBody BerthDto.WithId berthDto, @PathVariable Long id) {
        berthDto.setId(id);
        validateBerthDto(berthDto);
        berthFacade.updateBerth(berthDto);
    }

    @GetMapping
    public List<BerthDto.WithId> getAllBerths() {
        return berthFacade.getBerths();
    }

    @GetMapping("/{id}")
    public BerthDto.WithId getBerth(@PathVariable Long id) {
        return berthFacade.getBerth(id);
    }

    @DeleteMapping("/{id}")
    public void deleteBerth(@PathVariable Long id) {
        berthFacade.deleteBerth(id);
    }

    private void validateBerthDto(BerthDto berthDto) {
        ValidationUtils.validateEntity(berthDto);
        ValidationUtils.validateCollection(berthDto.getPlaceList());
        ValidationUtils.validateCollection(berthDto.getConvenienceList());
    }
}
