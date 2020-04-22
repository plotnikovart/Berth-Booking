package app.database.entity.enums;

import app.common.enums.AbstractEnumConverter;
import app.common.enums.EnumWithIdentifier;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountKind implements EnumWithIdentifier<Integer> {

    EMAIL(0),
    GOOGLE(1),
    FACEBOOK(2);

    private final Integer identifier;

    public static class Converter extends AbstractEnumConverter<AccountKind, Integer> {
    }
}
