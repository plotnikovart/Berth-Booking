package ru.hse.coursework.berth.service.email.dto;

import lombok.Data;

@Data
public class ReviewInfo {

    private String to;
    private String from;
    private Integer stars;
    private Long berthId;
    private String berthName;
}
