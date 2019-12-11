package spring.boot.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AmountValidator implements ConstraintValidator<Amount, Long> {


    private long amount;

    @Override
    public void initialize(Amount amount) {
        this.amount=amount.value();
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
        return id<=amount;
    }
}
