package iplay.cool.customValidator.validator;


import iplay.cool.customValidator.annotation.ValidAddress;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wu.dang
 * @since 2024/8/14
 */
public class AddressValidator implements ConstraintValidator<ValidAddress,String> {

    private int addressSize;


    @Override
    public void initialize(ValidAddress constraintAnnotation) {
        addressSize = constraintAnnotation.addressSize();
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        String regexEthereum = String.format("^0x[a-fA-F0-9]{%s}$",addressSize -2);
        Pattern patternEthereum = Pattern.compile(regexEthereum);
        Matcher matcherEthereum = patternEthereum.matcher(value);
        return matcherEthereum.matches();
    }
}
