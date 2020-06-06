package ru.hse.coursework.berth.service.account.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetAccessTokenResp {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private Integer expiresIn;
}
