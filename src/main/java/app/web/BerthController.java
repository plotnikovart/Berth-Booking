package app.web;

import app.config.validation.ValidationUtils;
import app.service.berth.BerthFacade;
import app.service.berth.dto.BerthDto;
import app.service.facade.ReviewFacade;
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

    @PostMapping("/applications")
    public IdResponse<Long> createApplication(@RequestBody BerthDto berthDto) {
        validateBerthDto(berthDto);
        long id = berthFacade.createBerthApplication(berthDto);
        return new IdResponse<>(id);
    }

    @GetMapping("/applications")
    public List getApplications(@RequestBody BerthDto berthDto) {
        long id = berthFacade.createBerthApplication(berthDto);
        return List.of();
    }

    @PutMapping("/{id}")
    public void updateBerth(@RequestBody BerthDto berthDto, @PathVariable Long id) {
        validateBerthDto(berthDto);
        berthFacade.updateBerth(id, berthDto);
    }

    @GetMapping
    public List<BerthDto.Resp> getAllBerths() {
        return berthFacade.getBerths();
    }

    @GetMapping("/{id}")
    public BerthDto.Resp getBerth(@PathVariable Long id) {
        return berthFacade.getBerth(id);
    }

    @DeleteMapping("/{id}")
    public void deleteBerth(@PathVariable Long id) {
        berthFacade.deleteBerth(id);
    }


    @PostMapping("{id}/reviews")
    public IdResponse<Long> createReview(@PathVariable Long id, @RequestBody ReviewDto.Req reviewDto) {
        reviewDto.setBerthId(id);

        ValidationUtils.validateEntity(reviewDto);
        long reviewId = reviewFacade.createReview(reviewDto);
        return new IdResponse<>(reviewId);
    }

    @GetMapping("{id}/reviews")
    public List<ReviewDto.Resp> getReviews(@PathVariable Long id) {
        return reviewFacade.getReviews(id);
    }

    @DeleteMapping("{id}/reviews/{reviewId}")
    public void deleteReview(@PathVariable Long id, @PathVariable Long reviewId) {
        reviewFacade.deleteReview(reviewId);
    }


    private void validateBerthDto(BerthDto berthDto) {
        ValidationUtils.validateEntity(berthDto);
        ValidationUtils.validateCollection(berthDto.getPlaceList());
        ValidationUtils.validateCollection(berthDto.getAmenityList());
    }
}
