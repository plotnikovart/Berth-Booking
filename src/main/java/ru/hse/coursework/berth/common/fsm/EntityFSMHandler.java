package ru.hse.coursework.berth.common.fsm;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;
import org.springframework.statemachine.StateMachine;
import ru.hse.coursework.berth.config.exception.impl.ServiceException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@SuppressWarnings("unchecked")
public abstract class EntityFSMHandler<ENTITY,
        STATE extends Enum<STATE> & StateEnum,
        EVENT extends Enum<EVENT> & EventEnum> {

    private final Field stateField;
    private final LoadingCache<RefSameObj<ENTITY>, StateMachine<STATE, EVENT>> fsmCache;


    protected abstract StateMachine<STATE, EVENT> buildStateMachine(STATE baseState);

    public EntityFSMHandler() {
        Class<ENTITY> entityClass = (Class<ENTITY>) (((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(entityClass, StateField.class);

        if (fields.size() > 1) {
            throw new ServiceException("Too many StateField fields in the entity " + entityClass.getName());
        } else if (fields.isEmpty()) {
            throw new ServiceException("No StateField field found in the entity " + entityClass.getName());
        }

        stateField = fields.get(0);
        stateField.setAccessible(true);

        fsmCache = buildCache();
    }


    @SneakyThrows
    public void sendEvent(ENTITY obj, EVENT event) {
        obj = (ENTITY) Hibernate.unproxy(obj);
        StateMachine<STATE, EVENT> stateMachine = fsmCache.get(RefSameObj.of(obj));

        if (getState(obj) != null && getState(obj) != stateMachine.getState().getId()) {
            throw new ServiceException("Entity state mismatch");
        }

        boolean accepted = stateMachine.sendEvent(event);
        if (!accepted) {
            String stateDescription = stateMachine.getState().getId().getDescription();
            String eventDescription = event.getDescription();
            throw new ServiceException("Cannot transition \"" + eventDescription + "\" for an object in state \"" + stateDescription + "\". Interface data might be outdated");
        }
    }


    private LoadingCache<RefSameObj<ENTITY>, StateMachine<STATE, EVENT>> buildCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(buildCacheLoader());
    }

    private CacheLoader<RefSameObj<ENTITY>, StateMachine<STATE, EVENT>> buildCacheLoader() {
        return new CacheLoader<>() {
            @Override
            public StateMachine<STATE, EVENT> load(@NonNull RefSameObj<ENTITY> key) {
                STATE currentState = getState(key.getValue());

                StateMachine<STATE, EVENT> stateMachine = buildStateMachine(currentState);
                stateMachine.addStateListener(new FSMChangeStatusListener<>(key.getValue(), stateField));
                stateMachine.start();

                return stateMachine;
            }
        };
    }

    private STATE getState(ENTITY obj) {
        try {
            return (STATE) stateField.get(obj);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }
    }
}
