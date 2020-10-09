package com.wujiuye.sck.common.validation;

import javax.validation.Constraint;
import java.lang.annotation.*;

/**
 * 参数校验=>时区
 *
 * @author wujiuye 2020/06/05
 */
@Documented
@Constraint(validatedBy = TimezoneValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Timezone {

    String message() default "Invalid timezone";

    Class[] groups() default {};

    Class[] payload() default {};

}
