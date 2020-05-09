package ru.hse.coursework.berth.web.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class ListResp<T> extends Response {

    private final List<T> data;

    public ListResp(List<T> data) {
        super(true);
        this.data = data;
    }
}
