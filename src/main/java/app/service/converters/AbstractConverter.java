package app.service.converters;

import app.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@Slf4j
public abstract class AbstractConverter<E, DRESP, DREQ> {

    private final Class<E> entityClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    private final Class<DRESP> dtoClass = (Class<DRESP>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];

    public DRESP convertToDto(E entity) {
        try {
            DRESP dto = dtoClass.newInstance();
            return convertToDto(dto, entity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e);
        }
    }

    public E convertToEntity(DREQ dto) {
        try {
            E entity = entityClass.newInstance();
            return convertToEntity(entity, dto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e);
        }
    }

    public abstract DRESP convertToDto(DRESP dto, E entity);

    public abstract E convertToEntity(E entity, DREQ dto);


    public List<DRESP> convertToDtos(Collection<E> entities) {
        return entities.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
