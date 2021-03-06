package ru.hse.coursework.berth.web.dto.resp;

import lombok.Getter;

@Getter
public class ObjectResp<T> extends Response {

    private T data;

    public ObjectResp(T data) {
        super(true);
        this.data = data;
    }
}
