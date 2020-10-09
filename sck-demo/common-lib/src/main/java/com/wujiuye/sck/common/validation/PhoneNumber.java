package com.wujiuye.sck.common.validation;

import javax.validation.Constraint;
import java.lang.annotation.*;

/**
 * 参数校验=>手机号码
 *
 * @author wujiuye 2020/06/05
 */
@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {

    String message() default "Invalid phone number";

    Class[] groups() default {};

    Class[] payload() default {};

}
