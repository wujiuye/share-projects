package wjy.strategymvc.annotations;


import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {

    //属性名
    String name() default "";
    //是否必须
    boolean required() default true;
}
