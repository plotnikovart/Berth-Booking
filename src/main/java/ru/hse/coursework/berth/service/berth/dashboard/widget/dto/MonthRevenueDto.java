package ru.hse.coursework.berth.service.berth.dashboard.widget.dto;

import lombok.Data;

import java.time.Month;

@Data
public class MonthRevenueDto {

    private Integer year;
    private Month month;
    private Double revenue;
}
