package spring.boot.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * This class is a validator of email address
 */
public class EmailValidator implements ConstraintValidator<CustomEmailVal, String> {


    @Override
    public void initialize(CustomEmailVal constraintAnnotation) {

    }

    @Override
    public boolean isValid(String emailToValidate, ConstraintValidatorContext constraintValidatorContext) {
        return emailToValidate.matches(".+@.+\\..+");
    }

    /**
     * This interface is used as a flag for group validation
     */
    public static interface ValidUsernamePassword {
    }
}
