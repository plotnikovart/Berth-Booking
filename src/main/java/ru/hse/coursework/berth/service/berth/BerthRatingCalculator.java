package ru.hse.coursework.berth.service.berth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.repository.BerthRepository;
import ru.hse.coursework.berth.service.event.ReviewDeleteEvent;
import ru.hse.coursework.berth.service.event.ReviewPublishEvent;

import static ru.hse.coursework.berth.config.AsyncConfig.RATING_CALCULATOR_EXECUTOR;

@Component
@RequiredArgsConstructor
public class BerthRatingCalculator {

    private final BerthRepository berthRepository;

    @EventListener
    @Async(RATING_CALCULATOR_EXECUTOR)
    public synchronized void reviewPublish(ReviewPublishEvent event) {
        Berth berth = berthRepository.getOne(event.getBerthId());
        berthRepository.updateRating(berth);
    }

    @EventListener
    @Async(RATING_CALCULATOR_EXECUTOR)
    public synchronized void reviewDelete(ReviewDeleteEvent event) {
        Berth berth = berthRepository.getOne(event.getBerthId());
        berthRepository.updateRating(berth);
    }
}
