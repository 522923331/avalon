package iplay.cool.customValidator.annotation;


import iplay.cool.customValidator.validator.ListSizeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author dangwu
 */
@Documented
@Constraint(validatedBy = ListSizeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ListSize {
    String message() default "List size must be between {min} and {max}";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int min() default 0;

    int max() default Integer.MAX_VALUE;
}
