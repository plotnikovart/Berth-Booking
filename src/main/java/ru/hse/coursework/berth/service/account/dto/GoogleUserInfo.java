package ru.hse.coursework.berth.service.account.dto;

import lombok.Data;

@Data
public class GoogleUserInfo {

    private String gmail;
    private String firstName;
    private String lastName;
    private String photoLink;
}
