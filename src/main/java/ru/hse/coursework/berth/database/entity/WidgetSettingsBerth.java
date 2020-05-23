package ru.hse.coursework.berth.database.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import ru.hse.coursework.berth.common.enums.EnumHelper;
import ru.hse.coursework.berth.service.berth.dashboard.WidgetEnum;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Immutable
public class WidgetSettingsBerth extends WidgetSettings {

    @EmbeddedId
    private PK pk;

    public WidgetEnum getWidgetEnum() {
        return EnumHelper.getEnumByIdentifier(pk.code, WidgetEnum.class);
    }

    @Data
    @Embeddable
    public static class PK implements Serializable {

        private String code;
        private Long berthId;
    }
}
