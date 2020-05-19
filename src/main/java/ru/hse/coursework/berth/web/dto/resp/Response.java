package ru.hse.coursework.berth.web.dto.resp;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Response {

    private final boolean success;
}
