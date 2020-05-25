package ru.hse.coursework.berth.database.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public abstract class WidgetSettings {

    @Column(nullable = false)
    private Integer columnNum;

    @Column(nullable = false)
    private Integer rowNum;

    @Column(nullable = false)
    private Boolean isVisible;
}
