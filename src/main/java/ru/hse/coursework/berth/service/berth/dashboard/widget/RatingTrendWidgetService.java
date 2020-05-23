package ru.hse.coursework.berth.service.berth.dashboard.widget;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.repository.BerthRepository;
import ru.hse.coursework.berth.database.repository.ReviewRepository;
import ru.hse.coursework.berth.service.berth.dashboard.WidgetEnum;
import ru.hse.coursework.berth.service.berth.dashboard.WidgetService;
import ru.hse.coursework.berth.service.berth.dashboard.widget.dto.RatingTrendDto;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class RatingTrendWidgetService implements WidgetService<RatingTrendDto> {

    private final Clock clock;  // for tests
    private final BerthRepository berthRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public RatingTrendDto getWidgetData(Long berthId) {
        Berth berth = berthRepository.findById(berthId).orElseThrow(NotFoundException::new);

        LocalDateTime from = LocalDate.now(clock).minusDays(30).atStartOfDay();
        LocalDateTime to = LocalDate.now(clock).plusDays(1).atStartOfDay();

        Double lastMonthRating = ofNullable(reviewRepository.calcRating(berth, from, to)).orElse(0.0);

        return new RatingTrendDto()
                .setLastMonthRating(lastMonthRating.intValue())
                .setTotalRating(berth.getAvgRating().intValue());
    }

    @Override
    public WidgetEnum getWidgetEnum() {
        return WidgetEnum.RATING_TREND;
    }
}
