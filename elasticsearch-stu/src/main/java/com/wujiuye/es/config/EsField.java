package com.wujiuye.es.config;

import java.lang.annotation.*;

/**
 * ES索引字段映射，用于代码创建索引 （仅测试使用）
 *
 * @author wujiuye
 * @date 2020/03/04
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EsField {

    /**
     * 是否创建索引
     *
     * @return
     */
    boolean index() default true;

    /**
     * 字段类型
     *
     * @return
     */
    String type() default "text";

}
