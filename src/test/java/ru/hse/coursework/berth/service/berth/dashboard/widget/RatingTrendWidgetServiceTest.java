package ru.hse.coursework.berth.service.berth.dashboard.widget;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.coursework.berth.service.berth.dashboard.DashboardBaseTest;
import ru.hse.coursework.berth.service.berth.dashboard.widget.dto.RatingTrendDto;


class RatingTrendWidgetServiceTest extends DashboardBaseTest {

    @Autowired
    RatingTrendWidgetService ratingTrendWidgetService;

    @Test
    void test() {
        var expected = new RatingTrendDto()
                .setTotalRating(0)
                .setLastMonthRating(0);

        var actual = ratingTrendWidgetService.getWidgetData(berth1.getId());

        Assertions.assertEquals(expected, actual);
    }
}