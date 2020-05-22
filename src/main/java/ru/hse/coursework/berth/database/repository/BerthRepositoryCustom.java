package ru.hse.coursework.berth.database.repository;

import org.apache.commons.lang3.tuple.Triple;
import ru.hse.coursework.berth.database.entity.Berth;

import java.time.LocalDate;
import java.util.List;

public interface BerthRepositoryCustom {

    /**
     * Подсчет прибыли причала за месяцы в разрезе дат
     * @return Список из тройки значений год - месяц - выручка
     */
    List<Triple<Integer, Integer, Double>> calcMonthRevenue(Berth berth, LocalDate sinceDate, LocalDate tillDate);
}
