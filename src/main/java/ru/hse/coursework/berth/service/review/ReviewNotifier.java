package ru.hse.coursework.berth.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.entity.Review;
import ru.hse.coursework.berth.database.repository.BerthRepository;
import ru.hse.coursework.berth.database.repository.ReviewRepository;
import ru.hse.coursework.berth.service.account.AccountService;
import ru.hse.coursework.berth.service.email.EmailService;
import ru.hse.coursework.berth.service.email.dto.ReviewInfo;
import ru.hse.coursework.berth.service.event.review.ReviewPublishEvent;
import ru.hse.coursework.berth.service.review.dto.ReviewDto;
import ru.hse.coursework.berth.websocket.SocketMessageSender;
import ru.hse.coursework.berth.websocket.event.outgoing.ReviewPublishOutgoingDto;

@Component
@RequiredArgsConstructor
public class ReviewNotifier {

    private final ReviewFacade reviewFacade;
    private final ReviewRepository reviewRepository;
    private final EmailService emailService;
    private final AccountService accountService;
    private final SocketMessageSender socketMessageSender;
    private final BerthRepository berthRepository;


    @Async
    @EventListener
    @Transactional
    public void onReviewPublishEmail(ReviewPublishEvent event) {
        Review review = reviewRepository.findById(event.getReviewId()).orElseThrow(NotFoundException::new);

        String email = accountService.getAccountEmail(review.getBerth().getOwner());
        if (email == null) {
            return;
        }

        var reviewInfo = new ReviewInfo()
                .setBerthId(review.getBerth().getId())
                .setBerthName(review.getBerth().getName())
                .setStars(review.getRating() / 5)
                .setFrom(accountService.getAccountFullName(review.getReviewer()))
                .setTo(accountService.getAccountFullName(review.getBerth().getOwner()));

        emailService.sendNewReview(email, reviewInfo);
    }

    @Async
    @EventListener
    @Transactional
    public void onReviewPublishSocket(ReviewPublishEvent event) {
        ReviewDto.Resp review = reviewFacade.getReview(event.getReviewId());
        Berth berth = berthRepository.findById(event.getBerthId()).orElseThrow(NotFoundException::new);

        var data = new ReviewPublishOutgoingDto.D()
                .setBerthId(event.getBerthId())
                .setReview(review);

        var message = new ReviewPublishOutgoingDto(data);

        socketMessageSender.sendMessage(berth.getOwnerId(), message);
    }
}
