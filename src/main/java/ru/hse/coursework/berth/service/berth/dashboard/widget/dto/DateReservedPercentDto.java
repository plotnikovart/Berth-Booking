package ru.hse.coursework.berth.service.berth.dashboard.widget.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DateReservedPercentDto {

    private LocalDate date;
    private ReservedPercentDto percent;
}
