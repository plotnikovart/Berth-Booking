package ru.hse.coursework.berth.web.dto.resp;

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
