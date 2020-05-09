package ru.hse.coursework.berth.common.enums;

import javax.persistence.AttributeConverter;
import java.lang.reflect.ParameterizedType;

@SuppressWarnings("unchecked")
public abstract class AbstractEnumConverter<E extends EnumWithIdentifier<T>, T> implements AttributeConverter<E, T> {

    private final Class<E> clazz;

    public AbstractEnumConverter() {
        clazz = (Class<E>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public T convertToDatabaseColumn(E attribute) {
        return attribute.getIdentifier();
    }

    @Override
    public E convertToEntityAttribute(T dbData) {
        return EnumHelper.getEnumByIdentifier(dbData, clazz);
    }
}
