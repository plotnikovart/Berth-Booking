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

    @JsonProperty("email")
    private String email;

    @JsonProperty("picture")
    private Picture picture;

    @Data
    public static class Picture {

        private Data data;

        @lombok.Data
        public static class Data {

            private Integer height;
            private Integer width;
            private String url;
        }
    }
}
