package app.common;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationUtils {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> void validateEntity(T entity) {
        Set<ConstraintViolation<T>> constraints = validator.validate(entity);
        if (!constraints.isEmpty()) {
            String resultMessage = constraints.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(";\n"));

            throw new ServiceException(resultMessage);
        }
    }
}
