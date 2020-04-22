package app.common;

import app.config.exception.impl.ServiceException;

import javax.annotation.Nullable;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationUtils {

    public static final String NOT_NULL_MESSAGE = "Параметр является обязательным";
    public static final String MIN_MESSAGE = "Значение параметра меньше минимального допустимого = {value}";
    public static final String MAX_MESSAGE = "Значение параметра больше максимально допустимого = {value}";
    public static final String RANGE_MESSAGE = "Значение параметра должно быть в пределах [{min}, {max}]";
    public static final String SIZE_MESSAGE = "Значение строки должно быть в пределах от {min} до {max}";
    public static final String LENGTH_MESSAGE = "Значение строки должно быть не больше, чем {max}";
    public static final String EMAIL_MESSAGE = "Значение почты некорректно";

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> void validateEntity(T entity) {
        Set<ConstraintViolation<T>> constraints = validator.validate(entity);
        if (!constraints.isEmpty()) {
            String resultMessage = constraints.stream()
                    .map(cv -> cv.getMessage() + " (" + cv.getPropertyPath().toString() + ")")
                    .collect(Collectors.joining(";\n"));

            throw new ServiceException(resultMessage);
        }
    }

    public static <T> void validateCollection(@Nullable Collection<T> entities) {
        if (entities != null) {
            entities.forEach(ValidationUtils::validateEntity);
        }
    }
}
