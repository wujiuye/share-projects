package com.wujiuye.sck.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 参数校验=>手机号码
 *
 * @author wujiuye 2020/06/05
 */
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public boolean isValid(String phoneField, ConstraintValidatorContext context) {
        if (phoneField == null) {
            return true;
        }
        return phoneField.matches("[0-9]+") && phoneField.length() > 8 && phoneField.length() < 14;
    }

}
