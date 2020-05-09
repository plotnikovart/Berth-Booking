package ru.hse.coursework.berth.service.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.database.entity.Review;
import ru.hse.coursework.berth.database.repository.BerthRepository;
import ru.hse.coursework.berth.database.repository.ReviewRepository;
import ru.hse.coursework.berth.database.repository.UserInfoRepository;
import ru.hse.coursework.berth.service.PermissionService;
import ru.hse.coursework.berth.service.converters.impl.ReviewConverter;
import ru.hse.coursework.berth.web.dto.ReviewDto;

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
        converter.toEntity(review, reviewDto);
        return reviewRepository.save(review).getId();
    }

    @Transactional(readOnly = true)
    public List<ReviewDto.Resp> getReviews(Long berthId) {
        var berth = berthRepository.findById(berthId).orElseThrow(NotFoundException::new);
        var reviews = reviewRepository.findAllByBerth(berth);
        return converter.toDtos(reviews);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        var review = reviewRepository.findById(reviewId).orElseThrow(NotFoundException::new);
        permissionService.check(review);

        reviewRepository.delete(review);
    }
}
