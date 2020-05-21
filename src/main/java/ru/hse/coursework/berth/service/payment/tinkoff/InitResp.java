package ru.hse.coursework.berth.service.payment.tinkoff;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InitResp {

    @JsonProperty("Status")
    private String status;

    @JsonProperty("PaymentURL")
    private String paymentURL;

    @JsonProperty("Success")
    private Boolean success;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("Details")
    private String details;
}
