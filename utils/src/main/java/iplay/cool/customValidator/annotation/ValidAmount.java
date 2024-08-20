package iplay.cool.customValidator.annotation;


import iplay.cool.customValidator.validator.DynamicAmountValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wu.dang
 * @since 2024/8/14
 */
@Constraint(validatedBy = DynamicAmountValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAmount {
    String message() default "Invalid amount";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int integerPart() default 10;  // 默认最大整数位数
    int fractionPart() default 6;  // 默认最大小数位数
}
