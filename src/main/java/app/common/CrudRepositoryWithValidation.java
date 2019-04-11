package app.common;

import org.springframework.data.repository.CrudRepository;

public interface CrudRepositoryWithValidation<T, ID> extends CrudRepository<T, ID> {

    default T saveWithValidation(T entity){
        ValidationUtils.validateEntity(entity);
        return save(entity);
    }
}
