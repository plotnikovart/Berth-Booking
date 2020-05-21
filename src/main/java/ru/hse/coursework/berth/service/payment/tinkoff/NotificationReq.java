package ru.hse.coursework.berth.service.payment.tinkoff;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NotificationReq {

    @JsonProperty("TerminalKey")
    private String terminalKey;

    @JsonProperty("OrderId")
    private String orderId;

    @JsonProperty("Success")
    private Boolean success;

    @JsonProperty("Status")
    private PaymentStatus status;
}
