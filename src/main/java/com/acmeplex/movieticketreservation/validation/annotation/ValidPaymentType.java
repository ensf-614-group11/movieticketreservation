package com.acmeplex.movieticketreservation.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SavePaymentTypeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPaymentType {
    String message() default "Invalid payment type fields";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
