package br.com.pauloultra.desafioluizalabs.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = GuidValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidGuid {
    String message() default "Invalid GUID format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
