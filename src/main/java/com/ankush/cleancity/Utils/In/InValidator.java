package com.ankush.cleancity.Utils.In;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Slf4j
public class InValidator implements ConstraintValidator<In, String> {
    List<String> values = new ArrayList<>();

    @Override
    public void initialize(In constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        values.addAll(Arrays.asList(constraintAnnotation.values()));
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        log.info(value);
        if (value == null) return true;
        for (String x : values) {
            if (value.equalsIgnoreCase(x)) return true;
        }
        return false;
    }
}
