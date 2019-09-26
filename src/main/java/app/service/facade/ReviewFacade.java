package app.service.facade;

import app.common.exception.NotFoundException;
import app.database.entity.Review;
import app.database.repository.BerthRepository;
import app.database.repository.ReviewRepository;
import app.database.repository.UserInfoRepository;
import app.service.PermissionService;
import app.web.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReviewFacade {

    private final ReviewRepository reviewRepository;
    private final BerthRepository berthRepository;
    private final UserInfoRepository userInfoRepository;
    private final PermissionService permissionService;

    @Transactional
    public Long createReview(ReviewDto reviewDto) {
        var userInfo = userInfoRepository.findCurrent();
        var berth = berthRepository.findById(reviewDto.getBerthId()).orElseThrow(NotFoundException::new);

        var review = new Review(berth, userInfo, reviewDto);
        return reviewRepository.save(review).getId();
    }

    @Transactional(readOnly = true)
    public List<ReviewDto.WithId> getReviews(Long berthId) {
        var berth = berthRepository.findById(berthId).orElseThrow(NotFoundException::new);
        var reviews = reviewRepository.findAllByBerth(berth);
        return reviews.stream().map(Review::getDto).collect(Collectors.toList());
    }

    @Transactional
    public void updateReview(ReviewDto.WithId dto) {
        var review = reviewRepository.findById(dto.getId()).orElseThrow(NotFoundException::new);
        permissionService.check(review);

        review.setDto(dto);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        var review = reviewRepository.findById(reviewId).orElseThrow(NotFoundException::new);
        permissionService.check(review);

        reviewRepository.delete(review);
    }
}
