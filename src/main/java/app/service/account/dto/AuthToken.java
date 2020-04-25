package app.service.account.dto;

import lombok.Setter;

@Setter
public class AuthToken {

    private String tokenType = "bearer";

    private String accessToken;
    private String refreshToken;

    private Long accessExpiresIn;
    private Long refreshExpiresIn;
}
