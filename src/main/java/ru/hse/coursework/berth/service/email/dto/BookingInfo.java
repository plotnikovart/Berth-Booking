package ru.hse.coursework.berth.service.email.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingInfo {

    private String owner;
    private String renter;

    private LocalDate fromDate;
    private LocalDate toDate;

    private String berthName;
    private String placeName;

    private Double price;
    private Double serviceFee;
}
