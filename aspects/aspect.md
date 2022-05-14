# 使用详解

主要是spring-boot-starter-aop依赖—>里面有需要的切面注解等配置

用到的jar包：

```xml
<!--引入提供Web开发场景依赖-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>    
<!--引入面向切面依赖-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

切入点表达式:
关键字（访问修饰符 返回值 包名.类名.方法名（参数）异常名）
示例：execution（public String com.it.Controller.findById（int））

* 单个独立的任意符号，可以独立出现，也可以作为前缀或者后缀的匹配符出现 
  示例：execution（public * com.it.*.UserService.find*（*））
  解释：匹配com.it包下的任意包中的UserService类或接口中所有find开头的带有一个参数的方法
  ..	多个连续的任意符号，可以独立出现，常用于简化包名与参数的书写
  实例: execution（public User com..UserService.findById（..））
  解释: 匹配com包下的任意包中的UserService类或接口中所有名称为findById并且返回是User实体的方法
+ 专用于匹配子类类型
  示例: execution(* *..*Service+.*(..))
  解释：匹配Service其子类方法
  

范例:
execution(* *(…))
execution(* …(…))
execution(* ….*(…))
execution(public * ….*(…))
execution(public int ….*(…))
execution(public void ….*(…))
execution(public void com….(…))
execution(public void com…service..(…))
execution(public void com.it.service..(…))
execution(public void com.it.service.User*.*(…))
execution(public void com.it.service.Service.(…))
execution(public void com.it.service.UserService.*(…))
execution(public User com.it.service.UserService.find*(…))
execution(public User com.it.service.UserService.*Id(…))
execution(public User com.it.service.UserService.findById(…))
execution(public User com.it.service.UserService.findById(int))
execution(public User com.it.service.UserService.findById(int,int))
execution(public User com.it.service.UserService.findById(int,*))
execution(public User com.it.service.UserService.findById(*,int))
execution(public User com.it.service.UserService.findById())
execution(List com.it.service.*Service+.findAll(…))

## AOP 为什么叫面向切面编程

**切** ：指的是横切逻辑，原有业务逻辑代码不动，只能操作横切逻辑代码，所以面向横切逻辑

**面** ：横切逻辑代码往往要影响的是很多个方法，每个方法如同一个点，多个点构成一个面。这里有一个面的概念。

AOP基于动态代理实现的：

如果对象实现了接口，那么就可以通过JDK Proxy来创建代理对象

如果对象没有实现接口，那么spring就会通过CGLIB生成一个被代理对象的子类来代理对象。

通知类型

### @Before 前置通知

- 前置通知：原始方法执行前执行，如果通知中抛出异常，阻止原始方法运行
- 应    用：数据校验

### @After 后置通知

- 后置通知：原始方法执行后执行，无论原始方法中是否出现异常，都将执行通知
- 应    用：现场清理

### @Around 环绕通知

- 环绕通知：在原始方法执行前后均有对应执行执行，还可以阻止原始方法的执行
- 应    用：十分强大，可以做任何事情

### @AfterReturning 返回后通知

- 返回后通知：原始方法正常执行完毕并返回结果后执行，如果原始方法中抛出异常，无法执行
- 应    用：返回值相关数据处理

### @AfterThrowing 抛出异常后通知

- 抛出异常后通知：原始方法抛出异常后执行，如果原始方法没有抛出异常，无法执行
- 应    用：对原始方法中出现的异常信息进行处理

# 封装成starter

如果想把切面代码封装成一个starter，只需在上面的基础上，再做如下两步(需要注意的是，该项目是聚合工程，如果封装starter，可以单独创建一个工程，如果直接通过这个项目打包成jar，也可以用，但是需要依赖avalon)：

## 1.创建配置类

```java
package iplay.cool.config;

import iplay.cool.annotation.SysLog;
import iplay.cool.aspect.SysLogAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义starter配置类
 * 当环境中存在SysLog修饰的方法时，才加载这个类，做到动态插拔作用
 * @author dove
 * @date 2022/5/14 23:44
 */
@Configuration
@ConditionalOnBean(annotation = SysLog.class)
public class MyLogConfiguration {
    @Bean
    public SysLogAspect sysLogAspect(){
        return new SysLogAspect();
    }
}
```

## 2.创建spring.factories

（starter的配置文件，必须放在resources/META-INF文件夹下）

```xml
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  iplay.cool.config.MyLogConfiguration
```





