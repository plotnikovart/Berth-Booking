package app.database.entity.enums;

import app.common.enums.AbstractEnumConverter;
import app.common.enums.EnumWithIdentifier;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
