package ru.hse.coursework.berth.database.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.hse.coursework.berth.common.enums.AbstractEnumConverter;
import ru.hse.coursework.berth.common.enums.EnumWithIdentifier;

@Getter
@AllArgsConstructor
public enum BerthApplicationStatus implements EnumWithIdentifier<Integer> {

    NEW(0),
    IN_PROGRESS(1),
    APPROVED(2),
    REJECTED(3);

    private Integer identifier;

    public static class Converter extends AbstractEnumConverter<BerthApplicationStatus, Integer> {
    }
}
