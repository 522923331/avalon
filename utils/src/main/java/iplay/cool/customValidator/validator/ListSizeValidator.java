package iplay.cool.customValidator.validator;


import iplay.cool.customValidator.annotation.ListSize;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * @author wu.dang
 * @since 2023/8/22
 */
public class ListSizeValidator implements ConstraintValidator<ListSize, List<?>> {
    private int min;
    private int max;
    @Override
    public void initialize(ListSize constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(List<?> objects, ConstraintValidatorContext constraintValidatorContext) {
        if (objects == null){
            return false;
        }
        int size = objects.size();
        return size >= min && size <= max;
    }
}
