package app.service.facade;

import app.common.exception.NotFoundException;
import app.database.entity.Review;
import app.database.repository.BerthRepository;
import app.database.repository.ReviewRepository;
import app.database.repository.UserInfoRepository;
import app.service.PermissionService;
import app.service.converters.impl.ReviewConverter;
import app.web.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewFacade {

    private final ReviewRepository reviewRepository;
    private final BerthRepository berthRepository;
    private final UserInfoRepository userInfoRepository;
    private final PermissionService permissionService;
    private final ReviewConverter converter;

    @Transactional
    public Long createReview(ReviewDto.Req reviewDto) {
        var userInfo = userInfoRepository.findCurrent();

        var review = new Review().setUserInfo(userInfo);
        converter.convertToEntity(review, reviewDto);
        return reviewRepository.save(review).getId();
    }

    @Transactional(readOnly = true)
    public List<ReviewDto.Resp> getReviews(Long berthId) {
        var berth = berthRepository.findById(berthId).orElseThrow(NotFoundException::new);
        var reviews = reviewRepository.findAllByBerth(berth);
        return converter.convertToDtos(reviews);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        var review = reviewRepository.findById(reviewId).orElseThrow(NotFoundException::new);
        permissionService.check(review);

        reviewRepository.delete(review);
    }
}
