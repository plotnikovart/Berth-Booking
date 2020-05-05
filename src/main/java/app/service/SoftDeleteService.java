package app.service;

import app.database.entity.AuditEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class SoftDeleteService {

    private final EntityManager em;

    public <T extends AuditEntity> void delete(T entity) {
        entity.setIsDeleted(true);
        em.persist(entity);
    }

    public <T extends AuditEntity> void delete(Collection<T> entities) {
        entities.forEach(this::delete);
    }
}
