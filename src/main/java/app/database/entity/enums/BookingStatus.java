package app.database.entity.enums;


import app.common.enums.AbstractEnumConverter;
import app.common.enums.EnumWithIdentifier;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BookingStatus implements EnumWithIdentifier<String> {

    NEW("N"),
    REJECTED("R"),
    APPROVED("A"),
    CANCELLED("C");

    private String code;

    @Override
    public String getIdentifier() {
        return code;
    }

    public static class Converter extends AbstractEnumConverter<BookingStatus, String> {
    }
}
