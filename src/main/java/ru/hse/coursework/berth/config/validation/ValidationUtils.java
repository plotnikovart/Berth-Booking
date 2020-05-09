package ru.hse.coursework.berth.config.validation;

import ru.hse.coursework.berth.config.exception.impl.ServiceException;

import javax.annotation.Nullable;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationUtils {

    public static final String NOT_NULL_MESSAGE = "The parameter is required";
    public static final String MIN_MESSAGE = "The parameter value is less than the minimum acceptable = {value}";
    public static final String MAX_MESSAGE = "The parameter value is greater than the maximum allowed = {value}";
    public static final String RANGE_MESSAGE = "The parameter value must be within [{min}, {max}]";
    public static final String SIZE_MESSAGE = "The string value must be between {min} до {max}";
    public static final String LENGTH_MESSAGE = "The string value must be no greater than {max}";
    public static final String EMAIL_MESSAGE = "Email value is incorrect";

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
