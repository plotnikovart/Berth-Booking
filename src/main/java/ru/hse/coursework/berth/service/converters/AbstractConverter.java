package ru.hse.coursework.berth.service.converters;

import lombok.extern.slf4j.Slf4j;
import ru.hse.coursework.berth.config.exception.impl.ServiceException;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@Slf4j
public abstract class AbstractConverter<E, DRESP, DREQ> {

    private final Class<E> entityClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    private final Class<DRESP> dtoClass = (Class<DRESP>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];

    public DRESP toDto(E entity) {
        try {
            DRESP dto = dtoClass.newInstance();
            return toDto(dto, entity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e);
        }
    }

    public E toEntity(DREQ dto) {
        try {
            E entity = entityClass.newInstance();
            return toEntity(entity, dto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e);
        }
    }

    public abstract DRESP toDto(DRESP dto, E entity);

    public abstract E toEntity(E entity, DREQ dto);


    public List<DRESP> toDtos(Collection<E> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }
}
