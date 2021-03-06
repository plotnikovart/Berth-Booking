package ru.hse.coursework.berth.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor(staticName = "of")
public class ListCount<T> {

    private List<T> items;
    private long totalCount;
}
