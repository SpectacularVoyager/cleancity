package com.ankush.cleancity.Utils.In;

import jakarta.validation.Constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = InValidator.class)
@Target({ElementType.TYPE_USE, FIELD,TYPE,ElementType.PARAMETER})
@Retention(RUNTIME)
public @interface In {
    String[] values() default {};

    String message() default "Value Not Present";

    Class[] groups() default {};

    Class[] payload() default {};
}