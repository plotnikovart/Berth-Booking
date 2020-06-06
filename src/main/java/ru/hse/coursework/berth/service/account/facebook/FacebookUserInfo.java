package ru.hse.coursework.berth.service.account.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FacebookUserInfo {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("profile_pic")
    private String photoUri;

    @JsonProperty("email")
    private String email;
}
