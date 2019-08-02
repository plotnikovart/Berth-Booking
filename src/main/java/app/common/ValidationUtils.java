package app.common;

import app.common.exception.ServiceException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationUtils {

    public static final String NOT_NULL_MESSAGE = "Параметр является обязательным";
    public static final String MIN_MESSAGE = "Значение параметра меньше минимального допустимого = {value}";
    public static final String MAX_MESSAGE = "Значение параметра больше максимально допустимого = {value}";
    public static final String SIZE_MESSAGE = "Значение строки должно быть в пределах от {min} до {max}";
    public static final String LENGTH_MESSAGE = "Значение строки должно быть не больше, чем {max}";

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> void validateEntity(T entity) {
        Set<ConstraintViolation<T>> constraints = validator.validate(entity);
        if (!constraints.isEmpty()) {
            String resultMessage = constraints.stream()
                    .map(cv -> cv.getMessage() + " (" + cv.getPropertyPath().toString() + ")")
                    .collect(Collectors.joining(";\n"));

            throw new ServiceException(resultMessage);
        }
    }
}
