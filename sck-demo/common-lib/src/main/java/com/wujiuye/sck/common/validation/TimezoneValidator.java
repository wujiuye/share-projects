package com.wujiuye.sck.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.TimeZone;

/**
 * 参数校验=>时区
 *
 * @author wujiuye 2020/06/05
 */
public class TimezoneValidator implements ConstraintValidator<Timezone, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return Arrays.asList(TimeZone.getAvailableIDs()).contains(value);
    }

}
