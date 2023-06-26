package iplay.cool.limiter.redisLimiter;//package com.echain.limiter.redisLimiter;
//
//import java.lang.annotation.*;
//
///**
// * @author wu.dang
// * @since 2023/5/13
// */
//@Target(ElementType.METHOD)
//@Retention(RetentionPolicy.RUNTIME)
//@Documented
//public @interface RateLimiter {
//
//    /**
//     * 限流key
//     * @return
//     */
//    String key() default "rate:limiter";
//    /**
//     * 窗口允许最大请求数
//     * @return
//     */
//    long maxCount() default 10;
//
//    /**
//     * 窗口宽度，单位为ms
//     * @return
//     */
//    long winWidth() default 1000;
//
//    /**
//     * 限流提示语
//     * @return
//     */
//    String message() default "false";
//}
//
