package ru.hse.coursework.berth.service.payment;

import lombok.Data;

import java.util.List;

@Data
public class OrderDto {

    private String id;
    private String email;
    private List<Item> items;

    @Data
    public static class Item {

        private String name;
        private Integer price;
    }
}
