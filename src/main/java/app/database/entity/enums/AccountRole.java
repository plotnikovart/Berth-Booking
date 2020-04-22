package app.database.entity.enums;

import app.common.enums.AbstractEnumConverter;
import app.common.enums.EnumWithIdentifier;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountRole implements EnumWithIdentifier<Integer> {

    USER(0),
    MODERATOR(1),
    ADMIN(2);

    private Integer identifier;

    public static class Converter extends AbstractEnumConverter<AccountRole, Integer> {
    }
}
