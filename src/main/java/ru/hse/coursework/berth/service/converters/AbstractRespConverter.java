package ru.hse.coursework.berth.service.converters;

import ru.hse.coursework.berth.config.exception.impl.ServiceException;

public abstract class AbstractRespConverter<E, DRESP> extends AbstractConverter<E, DRESP, Object> {

    @Override
    public E toEntity(E entity, Object dto) {
        throw new ServiceException();
    }
}
