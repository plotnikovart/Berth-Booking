package ru.hse.coursework.berth.web;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.springframework.web.bind.annotation.*;
import ru.hse.coursework.berth.service.berth.BerthCRUDService;
import ru.hse.coursework.berth.service.berth.BerthPart;
import ru.hse.coursework.berth.service.berth.UserBerthApplicationService;
import ru.hse.coursework.berth.service.berth.dto.BerthApplicationDto;
import ru.hse.coursework.berth.service.berth.dto.BerthDto;
import ru.hse.coursework.berth.service.berth.dto.BerthPlaceDto;
import ru.hse.coursework.berth.service.berth.dto.DictAmenityDto;
import ru.hse.coursework.berth.service.dto.ListCount;
import ru.hse.coursework.berth.service.review.ReviewService;
import ru.hse.coursework.berth.service.review.dto.ReviewDto;
import ru.hse.coursework.berth.service.review.dto.ReviewFilter;
import ru.hse.coursework.berth.web.dto.response.EmptyResp;
import ru.hse.coursework.berth.web.dto.response.ListResp;
import ru.hse.coursework.berth.web.dto.response.ObjectResp;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/berths")
@RequiredArgsConstructor
public class BerthController {

    private final ReviewService reviewService;
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

    @PostMapping("{berthId}/reviews")
    public ObjectResp<ReviewDto.Resp> createReview(@PathVariable Long berthId, @RequestBody ReviewDto reviewDto) {
        long reviewId = reviewService.createReview(berthId, reviewDto);
        ReviewDto.Resp resp = reviewService.getReview(reviewId);
        return new ObjectResp<>(resp);
    }

    @PostMapping("{berthId}/reviews/filter")
    public ObjectResp<ListCount<ReviewDto.Resp>> getReviews(@PathVariable Long berthId, @RequestBody ReviewFilter filter) {
        ListCount<ReviewDto.Resp> resp = reviewService.getReviews(berthId, filter);
        return new ObjectResp<>(resp);
    }

    @DeleteMapping("{berthId}/reviews/{reviewId}")
    public EmptyResp deleteReview(@PathVariable Long berthId, @PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return new EmptyResp();
    }

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
