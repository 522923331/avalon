package iplay.cool.annotation;

import java.lang.annotation.*;

/**
 * @author dove
 * @date 2022/5/14 15:49
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    String value() default "";
}
