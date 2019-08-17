package app.web;

import app.common.ValidationUtils;
import app.service.facade.BerthFacade;
import app.service.facade.ReviewFacade;
import app.web.dto.BerthDto;
import app.web.dto.ReviewDto;
import app.web.dto.response.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/berths")
@RequiredArgsConstructor
public class BerthController {

    private final BerthFacade berthFacade;
    private final ReviewFacade reviewFacade;

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


    @PostMapping("{id}/reviews")
    public IdResponse<Long> createReview(@PathVariable Long id, @RequestBody ReviewDto reviewDto) {
        reviewDto.setBerthId(id);

        ValidationUtils.validateEntity(reviewDto);
        long reviewId = reviewFacade.createReview(reviewDto);
        return new IdResponse<>(reviewId);
    }

    @GetMapping("{id}/reviews")
    public List<ReviewDto.WithId> getReviews(@PathVariable Long id) {
        return reviewFacade.getReviews(id);
    }

    @PutMapping("{id}/reviews/{reviewId}")
    public void updateReview(@PathVariable Long id, @PathVariable Long reviewId, @RequestBody ReviewDto.WithId reviewDto) {
        reviewDto.setBerthId(id);
        reviewDto.setId(reviewId);

        ValidationUtils.validateEntity(reviewDto);
        reviewFacade.updateReview(reviewDto);
    }

    @DeleteMapping("{id}/reviews/{reviewId}")
    public void deleteReview(@PathVariable Long id, @PathVariable Long reviewId) {
        reviewFacade.deleteReview(reviewId);
    }


    private void validateBerthDto(BerthDto berthDto) {
        ValidationUtils.validateEntity(berthDto);
        ValidationUtils.validateCollection(berthDto.getPlaceList());
        ValidationUtils.validateCollection(berthDto.getConvenienceList());
    }
}
