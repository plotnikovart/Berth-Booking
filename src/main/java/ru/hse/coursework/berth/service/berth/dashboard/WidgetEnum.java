package ru.hse.coursework.berth.service.berth.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.hse.coursework.berth.common.enums.EnumWithIdentifier;

@Getter
@AllArgsConstructor
public enum WidgetEnum implements EnumWithIdentifier<String> {

    YEAR_REVENUE("year_revenue");

    private final String identifier;
}
