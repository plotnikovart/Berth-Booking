package ru.hse.coursework.berth.database.entity;

import lombok.Getter;
import org.hibernate.annotations.Immutable;
import ru.hse.coursework.berth.common.enums.EnumHelper;
import ru.hse.coursework.berth.service.berth.dashboard.WidgetEnum;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@Immutable
public class WidgetSettingsDefault extends WidgetSettings {

    @Id
    private String code;

    public WidgetEnum getWidgetEnum() {
        return EnumHelper.getEnumByIdentifier(code, WidgetEnum.class);
    }
}
