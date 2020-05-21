package ru.hse.coursework.berth.service.payment.tinkoff;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@lombok.Data
public class InitReq {

    @JsonProperty("TerminalKey")
    private String terminalKey;

    @JsonProperty("Amount")
    private Integer amount;

    @JsonProperty("OrderId")
    private String orderId;

    @JsonProperty("PayType")
    private String payType = "O";

    @JsonProperty("Language")
    private String language = "en";

    @JsonProperty("DATA")
    private Data data;

    @JsonProperty("Receipt")
    private Receipt receipt;

    @JsonProperty("NotificationURL")
    private String notificationUrl;

    @JsonProperty("SuccessURL")
    private String successUrl;

    @JsonProperty("FailURL")
    private String failUrl;


    @lombok.Data
    public static class Data {

        @JsonProperty("Email")
        private String email;
    }


    @lombok.Data
    public static class Receipt {

        @JsonProperty("Items")
        private List<Item> items;

        @JsonProperty("Email")
        private String email;

        @JsonProperty("Taxation")
        private String taxation = "patent";


        @lombok.Data
        public static class Item {

            @JsonProperty("Name")
            private String name;

            @JsonProperty("Price")
            private Integer price;

            @JsonProperty("Quantity")
            private Integer quantity;

            @JsonProperty("Amount")
            private Integer amount;

            @JsonProperty("Tax")
            private String tax = "none";
        }
    }
}
