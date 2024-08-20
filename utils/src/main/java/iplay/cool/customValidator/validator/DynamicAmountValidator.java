package iplay.cool.customValidator.validator;


import iplay.cool.customValidator.annotation.ValidAmount;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author wu.dang
 * @since 2024/8/14
 */
public class DynamicAmountValidator implements ConstraintValidator<ValidAmount,String> {

    private int integerPart;
    private int fractionPart;

    @Override
    public void initialize(ValidAmount constraintAnnotation) {
        this.integerPart = constraintAnnotation.integerPart();
        this.fractionPart = constraintAnnotation.fractionPart();
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // Consider null or empty as valid or handle according to your needs
        }
        //校验金额类型  要么是整数，要么小数点后面限制X位。
        String regex = String.format("^(\\d{1,%d})(\\.\\d{1,%d})?$", integerPart, fractionPart);
        return Pattern.matches(regex, value);
    }
}
