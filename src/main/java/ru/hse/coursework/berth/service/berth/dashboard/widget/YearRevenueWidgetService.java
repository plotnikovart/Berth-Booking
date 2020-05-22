package ru.hse.coursework.berth.service.berth.dashboard.widget;

import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.stereotype.Service;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.repository.BerthRepository;
import ru.hse.coursework.berth.service.berth.dashboard.WidgetEnum;
import ru.hse.coursework.berth.service.berth.dashboard.WidgetService;
import ru.hse.coursework.berth.service.berth.dashboard.widget.dto.MonthRevenueDto;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YearRevenueWidgetService implements WidgetService<List<MonthRevenueDto>> {

    private final BerthRepository berthRepository;

    @Override
    public List<MonthRevenueDto> getWidgetData(Long berthId) {
        Berth berth = berthRepository.getOne(berthId);

        LocalDate startDate = LocalDate.now()
                .minusYears(1)
                .plusMonths(1)
                .withDayOfMonth(1);

        List<Triple<Integer, Integer, Double>> yearMonthRevenue = berthRepository.calcMonthRevenue(berth, startDate, LocalDate.now());

        return StreamEx.of(yearMonthRevenue)
                .map(it -> new MonthRevenueDto()
                        .setYear(it.getLeft())
                        .setMonth(Month.of(it.getMiddle()))
                        .setRevenue(it.getRight()))
                .toList();
    }

    @Override
    public WidgetEnum getWidgetEnum() {
        return WidgetEnum.YEAR_REVENUE;
    }
}
