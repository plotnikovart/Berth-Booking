package app.web;

import app.service.berth.BerthCRUDService;
import app.service.berth.BerthPart;
import app.service.berth.UserBerthApplicationService;
import app.service.berth.dto.BerthApplicationDto;
import app.service.berth.dto.BerthDto;
import app.service.facade.ReviewFacade;
import app.web.dto.BerthPlaceDto;
import app.web.dto.DictAmenityDto;
import app.web.dto.response.EmptyResp;
import app.web.dto.response.ListResp;
import app.web.dto.response.ObjectResp;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/berths")
@RequiredArgsConstructor
public class BerthController {

    private final ReviewFacade reviewFacade;
    private final UserBerthApplicationService berthApplicationService;
    private final BerthCRUDService berthCRUDService;

    @PostMapping("/applications")
    public ObjectResp<BerthApplicationDto.Resp> createApplication(@RequestBody BerthApplicationDto.Req applicationDto) {
        long id = berthApplicationService.create(applicationDto);
        BerthApplicationDto.Resp resp = berthApplicationService.get(id);
        return new ObjectResp<>(resp);
    }

    @GetMapping("/applications")
    public ListResp<BerthApplicationDto.Resp> getApplications() {
        List<BerthApplicationDto.Resp> resp = berthApplicationService.get();
        return new ListResp<>(resp);
    }

    @GetMapping
    @ApiOperation(value = "Получение всех причалов пользователя", notes = "Параметр include принимает список значений из 'places', 'amenities'")
    public ListResp<BerthDto.Resp> getAllBerths(@RequestParam(required = false) List<String> include) {
        BerthPart[] parts = toBerthParts(include);
        List<BerthDto.Resp> resp = berthCRUDService.get(parts);
        return new ListResp<>(resp);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Получение причала", notes = "Параметр include принимает список значений из 'places', 'amenities'")
    public ObjectResp<BerthDto.Resp> getBerth(@PathVariable Long id, @RequestParam(required = false) List<String> include) {
        BerthPart[] parts = toBerthParts(include);
        BerthDto.Resp resp = berthCRUDService.get(id, parts);
        return new ObjectResp<>(resp);
    }

    @PutMapping("/{id}")
    public ObjectResp<BerthDto.Resp> updateBerth(@RequestBody BerthDto berthDto, @PathVariable Long id) {
        berthCRUDService.update(id, berthDto);
        BerthDto.Resp resp = berthCRUDService.get(id, BerthPart.values());
        return new ObjectResp<>(resp);
    }

    @DeleteMapping("/{id}")
    public EmptyResp deleteBerth(@PathVariable Long id) {
        berthCRUDService.delete(id);
        return new EmptyResp();
    }

    @GetMapping("/{berthId}/places")
    public ListResp<BerthPlaceDto> getBerthPlaces(@PathVariable Long berthId) {
        List<BerthPlaceDto> resp = berthCRUDService.getPlaces(berthId);
        return new ListResp<>(resp);
    }

    @GetMapping("/{berthId}/amenities")
    public ListResp<DictAmenityDto> getBerthAmenities(@PathVariable Long berthId) {
        List<DictAmenityDto> resp = berthCRUDService.getAmenities(berthId);
        return new ListResp<>(resp);
    }



//    @PostMapping("{id}/reviews")
//    public IdResponse<Long> createReview(@PathVariable Long id, @RequestBody ReviewDto.Req reviewDto) {
//        reviewDto.setBerthId(id);
//
//        ValidationUtils.validateEntity(reviewDto);
//        long reviewId = reviewFacade.createReview(reviewDto);
//        return new IdResponse<>(reviewId);
//    }
//
//    @GetMapping("{id}/reviews")
//    public List<ReviewDto.Resp> getReviews(@PathVariable Long id) {
//        return reviewFacade.getReviews(id);
//    }
//
//    @DeleteMapping("{id}/reviews/{reviewId}")
//    public void deleteReview(@PathVariable Long id, @PathVariable Long reviewId) {
//        reviewFacade.deleteReview(reviewId);
//    }

    private BerthPart[] toBerthParts(@Nullable List<String> parts) {
        if (parts == null) {
            return new BerthPart[0];
        }

        return StreamEx.of(parts)
                .map(it -> {
                    try {
                        return BerthPart.valueOf(it.toUpperCase());
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toArray(new BerthPart[0]);
    }
}
