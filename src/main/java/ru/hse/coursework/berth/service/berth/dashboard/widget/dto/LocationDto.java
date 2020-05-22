package ru.hse.coursework.berth.service.berth.dashboard.widget.dto;

import lombok.Data;
import ru.hse.coursework.berth.service.file.dto.FileInfoDto;

import java.util.List;

@Data
public class LocationDto {

    private Item me;
    private Double radius;
    private List<Item> neighbours;

    @Data
    public static class Item {

        private Long Id;
        private String title;
        private FileInfoDto photo;
        private Double lat;
        private Double lng;
        private Integer rating;
    }
}
