package ru.hse.coursework.berth.service.account.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthToken {

    private String tokenType = "bearer";

    private String accessToken;
    private String refreshToken;

    private Long accessExpiresIn;
    private Long refreshExpiresIn;
}
