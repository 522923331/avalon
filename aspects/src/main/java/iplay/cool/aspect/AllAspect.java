package iplay.cool.aspect;

import iplay.cool.annotation.AfterAnno;
import iplay.cool.annotation.BeforeAnno;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author dove
 * @date 2022/5/14 0:53
 */
@Slf4j
@Component  //注入到容器
@Aspect  //设置当前类为切面类
@Order(0) //执行时机
public class AllAspect {
    /**
     * @param joinPoint
     * @return void
     * @Aspect: 设置当前类为切面类
     * @Before("@annotation(iplay.cool.annotation.BeforeAnno)")
     * @Before：标注当前方法作为前置通知
     * @annotation：指定用注解进行切面 iplay.cool.annotation.BeforeAnno：注解的全路径名
     * JoinPoint: 主要是获取切入点方法相应数据
     * getSignature()): 是获取到这样的信息 :修饰符+ 包名+组件名(类名) +方法名
     * joinPoint.getArgs() ：这里返回的是切入点方法的参数列表
     * (MethodSignature) joinPoint.getSignature().getMethod().getAnnotation(BeforeAnno.class) :获取切入点方法上的@BeforeAnno注解
     * @date 2022/5/14 1:09
     */
    @Before("@annotation(iplay.cool.annotation.BeforeAnno)")
    public void before(JoinPoint joinPoint) {
        Signature pointSignature = joinPoint.getSignature();
        log.info("切入点方法的修饰符+ 包名+组件名(类名) +方法名-->:" + pointSignature);

        Object[] args = joinPoint.getArgs();
        log.info("切入点方法的参数列表-->:" + Arrays.toString(args));

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        BeforeAnno beforeAnno = method.getAnnotation(BeforeAnno.class);
        //获取切入点方法上BeforeAnno注解的值
        String value = beforeAnno.value();
        log.info("切面类中before方法--自定义注解BeforeAnno中的值为-->:" + value);
    }

    /**
     * @After("@annotation(afterAnno)")
     * @After：标注当前方法作为后置通知
     * @annotation：指定用注解进行切面 afterAnno：切面方法上的入参注解名
     * JoinPoint: 主要是获取切入点方法相应数据
     * getSignature()): 是获取到这样的信息 :修饰符+ 包名+组件名(类名) +方法名
     * joinPoint.getArgs() ：这里返回的是切入点方法的参数列表
     */
    @After("@annotation(afterAnno)")
    public void after(JoinPoint joinPoint, AfterAnno afterAnno) {
        Signature pointSignature = joinPoint.getSignature();
        System.err.println("切入点方法的修饰符+ 包名+组件名(类名) +方法名-->:" + pointSignature);

        Object[] args = joinPoint.getArgs();
        System.err.println("切入点方法的参数列表-->:" + Arrays.toString(args));

        String value = afterAnno.value();
        System.err.println("切面类中after方法--自定义注解中的值为->" + value);

    }

    //        @Pointcut(value = "@annotation(iplay.cool.annotation.BeforeAnno)")
    @Pointcut(value = "execution(public String iplay.cool.controller.AspectTestController.pointCutTest())")
    public void pointCut() {
    }

    /**
     * @param joinPoint
     * @return java.lang.Object
     * @Aspect: 设置当前类为切面类
     * @Around(“pointCut()”)------>@Pointcut(value = “execution(public String iplay.cool.controller.AspectTestController.pointCutTest())”)
     * @Around：标注当前方法作为环绕通知 pointCut()：自定义的方法,此方法上有一个注解 @Pointcut()
     * @Pointcut()：此注解的value值为切入点表达式 JoinPoint: 主要是获取切入点方法相应数据
     * getSignature()): 是获取到这样的信息 :修饰符+ 包名+组件名(类名) +方法名
     * joinPoint.getArgs() ：这里返回的是切入点方法的参数列表这里返回的是切入点方法的参数列表
     * ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotations()：获取切入点方法上的所有注解
     * …
     * @date 2022/5/14 15:23
     */
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取执行签名信息
        Signature signature = joinPoint.getSignature();
        //通过签名获取执行类型（接口名）
        String targetClass = signature.getDeclaringTypeName();
        //通过签名获取执行操作名称（方法名）
        String targetMethod = signature.getName();
        //获取操作前系统时间beginTime
        long beginTime = System.currentTimeMillis();
        //消息入参joinPoint.getArgs() 及执行结束 反参ret 之后return到请求页面
        System.err.println("环绕通知在此方法之前执行的代码");
        Object ret = joinPoint.proceed(joinPoint.getArgs());
        System.err.println("环绕通知在此方法之后执行的代码");
        //获取操作后系统时间endTime
        long endTime = System.currentTimeMillis();
        System.err.println(targetClass + " 中 " + targetMethod + " 运行时长 " + (endTime - beginTime) + "ms");


        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Annotation[] annotations = method.getAnnotations();
        System.err.println("此方法上的所有注解：" + Arrays.toString(annotations));


        System.err.println("真实反参--》" + ret);
        //这里可以修改返回数据
        return ret + "--》:通过环绕通知修改的参数";
    }

    /**
     *  标注当前方法作为返回后通知
     *
     * 当连接点方法成功执行后，返回通知方法才会执行，如果连接点方法出现异常，则返回通知方法不执行。
     * 返回通知方法在目标方法执行成功后才会执行，所以，返回通知方法可以拿到目标方法(连接点方法)执行后的结果。
     * returning ：设定使用通知方法参数接收返回值的变量名
     * */
    @AfterReturning(value = "execution(public String iplay.cool.controller.AspectTestController.afterReturningTest(int ,int ))", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        Signature pointSignature = joinPoint.getSignature();
        System.err.println("切入点方法的修饰符+ 包名+组件名(类名) +方法名-->:" + pointSignature);

        Object[] args = joinPoint.getArgs();
        System.err.println("切入点方法的参数列表-->:" + Arrays.toString(args));

        System.err.println("切入点返回参-->:" + result);
    }

    /**
     * 标注当前方法作为异常后通知
     * 异常通知方法只在连接点方法出现异常后才会执行，否则不执行。
     * 在异常通知方法中可以获取连接点方法出现的异常。在切面类中异常通知方法
     * throwing ：设定使用通知方法参数接收原始方法中抛出的异常对象名
     * */
    @AfterThrowing(value = "execution(public String iplay.cool.controller.AspectTestController.afterThrowingTest(String))", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) {
        Signature pointSignature = joinPoint.getSignature();
        System.err.println("切入点方法的修饰符+ 包名+组件名(类名) +方法名-->:" + pointSignature);

        Object[] args = joinPoint.getArgs();
        System.err.println("切入点方法的参数列表-->:" + Arrays.toString(args));

        System.err.println("切入点异常-->:" + e);
    }

}
