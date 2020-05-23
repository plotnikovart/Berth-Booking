package ru.hse.coursework.berth.service.berth.dashboard.widget.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.service.file.dto.FileInfoDto;

import java.util.List;

@Data
public class LocationDto {

    @ApiModelProperty(required = true, position = 1)
    private Item me;

    @ApiModelProperty(required = true, position = 2)
    private Double radius;

    @ApiModelProperty(required = true, position = 3)
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
