package app.service.converters;

import app.common.exception.ServiceException;

public abstract class AbstractRespConverter<E, DRESP> extends AbstractConverter<E, DRESP, Object> {

    @Override
    public E convertToEntity(E entity, Object dto) {
        throw new ServiceException();
    }
}
