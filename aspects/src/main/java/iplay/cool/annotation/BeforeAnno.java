package iplay.cool.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 后置通知和前置通知逻辑相同，不再重复写了
 * @author dove
 * @date 2022/5/14 0:25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BeforeAnno {
    String value() default "";
}
