package ru.hse.coursework.berth.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.entity.Review;
import ru.hse.coursework.berth.database.entity.UserInfo;
import ru.hse.coursework.berth.database.repository.AccountRepository;
import ru.hse.coursework.berth.database.repository.BerthRepository;
import ru.hse.coursework.berth.database.repository.ReviewRepository;
import ru.hse.coursework.berth.database.repository.UserInfoRepository;
import ru.hse.coursework.berth.service.PermissionService;
import ru.hse.coursework.berth.service.SoftDeleteService;
import ru.hse.coursework.berth.service.account.dto.UserInfoDto;
import ru.hse.coursework.berth.service.converters.impl.ReviewConverter;
import ru.hse.coursework.berth.service.converters.impl.UserInfoConverter;
import ru.hse.coursework.berth.service.dto.ListCount;
import ru.hse.coursework.berth.service.event.EventPublisher;
import ru.hse.coursework.berth.service.file.dto.FileInfoDto;
import ru.hse.coursework.berth.service.review.dto.ReviewDto;
import ru.hse.coursework.berth.service.review.dto.ReviewFilter;

import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;
import static one.util.streamex.StreamEx.of;

@Component
@RequiredArgsConstructor
public class ReviewService {

    private final UserInfoRepository userInfoRepository;
    private final AccountRepository accountRepository;
    private final ReviewRepository reviewRepository;
    private final BerthRepository berthRepository;
    private final PermissionService permissionService;
    private final ReviewConverter converter;
    private final SoftDeleteService softDeleteService;
    private final UserInfoConverter userInfoConverter;
    private final EventPublisher eventPublisher;

    public Long createReview(Long berthId, ReviewDto reviewDto) {
        var review = new Review()
                .setReviewer(accountRepository.findCurrent())
                .setBerth(berthRepository.getOne(berthId));

        converter.toEntity(review, reviewDto);
        reviewRepository.save(review);

        eventPublisher.reviewPublish(review.getBerth().getId(), review.getId());
        return review.getId();
    }

    public ListCount<ReviewDto.Resp> getReviews(Long berthId, ReviewFilter reviewFilter) {
        Berth berth = berthRepository.getOne(berthId);
        Pageable pageable = createPageable(reviewFilter);

        Page<Review> reviews = reviewRepository.findAllByBerth(berth, pageable);
        List<ReviewDto.Resp> dtos = converter.toDtos(reviews.getContent());
        fillReviewersInfo(dtos);

        return ListCount.of(dtos, reviews.getTotalElements());
    }

    public ReviewDto.Resp getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NotFoundException::new);
        ReviewDto.Resp dto = converter.toDto(review);
        fillReviewersInfo(List.of(dto));

        return dto;
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NotFoundException::new);
        permissionService.check(review);
        softDeleteService.delete(review);
        reviewRepository.save(review);

        eventPublisher.reviewDelete(review.getBerth().getId(), review.getId());
    }

    private void fillReviewersInfo(List<ReviewDto.Resp> dtos) {
        if (dtos.isEmpty()) {
            return;
        }

        List<Long> ids = of(dtos).map(it -> it.getReviewer().getAccountId()).toList();
        Map<Long, UserInfoDto.Resp> idToUserInfo = of(userInfoRepository.findAllById(ids))
                .toMap(UserInfo::getAccountId, userInfoConverter::toDto);

        dtos.forEach(it -> {
            UserInfoDto.Resp info = idToUserInfo.get(it.getReviewer().getAccountId());
            it.getReviewer()
                    .setFirstName(info.getFirstName())
                    .setLastName(info.getLastName())
                    .setPhotoLink(ofNullable(info.getPhoto()).map(FileInfoDto::getFileLink).orElse(null));
        });
    }

    private Pageable createPageable(ReviewFilter filter) {
        var sort = new Sort(Sort.Direction.DESC, "createdAt");
        return PageRequest.of(filter.getPageNum(), filter.getPageSize(), sort);
    }
}
