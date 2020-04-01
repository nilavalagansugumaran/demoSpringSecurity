package com.example.demoSpringSecurity;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {

    }

    @Override
    public boolean isValid(String contactField, ConstraintValidatorContext constraintValidatorContext) {

        return  contactField != null &&
                contactField.matches("[0-9]+") &&
                (contactField.length() > 8) && (contactField.length() < 14);
    }
}
