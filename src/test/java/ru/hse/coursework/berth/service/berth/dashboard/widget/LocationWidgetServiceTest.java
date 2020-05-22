package ru.hse.coursework.berth.service.berth.dashboard.widget;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.coursework.berth.service.berth.dashboard.DashboardBaseTest;
import ru.hse.coursework.berth.service.berth.dashboard.widget.dto.LocationDto;

class LocationWidgetServiceTest extends DashboardBaseTest {

    @Autowired
    LocationWidgetService locationWidgetService;

    @Test
    void test() {
        LocationDto widgetData = locationWidgetService.getWidgetData(berth1.getId());

        Assertions.assertEquals(1, widgetData.getNeighbours().size());
        Assertions.assertEquals(berth2.getId(), widgetData.getNeighbours().get(0).getId());
    }
}