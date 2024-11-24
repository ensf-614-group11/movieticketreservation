package com.acmeplex.movieticketreservation.validation.annotation;

import com.acmeplex.movieticketreservation.validation.validator.CreatePaymentTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CreatePaymentTypeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CreateValidPaymentType {
    String message() default "Invalid payment type fields";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
