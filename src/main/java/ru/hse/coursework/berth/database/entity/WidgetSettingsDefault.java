package ru.hse.coursework.berth.database.entity;

import lombok.Getter;
import org.hibernate.annotations.Immutable;
import ru.hse.coursework.berth.service.berth.dashboard.WidgetEnum;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@Immutable
public class WidgetSettingsDefault extends WidgetSettings {

    @Id
    @Column(name = "code")
    @Convert(converter = WidgetEnum.Converter.class)
    private WidgetEnum widgetEnum;
}
