package ru.hse.coursework.berth.database.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import ru.hse.coursework.berth.service.berth.dashboard.WidgetEnum;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Immutable
public class WidgetSettingsBerth extends WidgetSettings {

    @EmbeddedId
    private PK pk;

    @MapsId("accountId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Berth berth;


    @Data
    @Embeddable
    public static class PK implements Serializable {

        @Column(name = "code")
        @Convert(converter = WidgetEnum.Converter.class)
        private WidgetEnum widgetEnum;

        private Long berthId;
    }
}
