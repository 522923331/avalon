# java中有哪些元注解：

```java
@Target 目标即作用范围
@Retention 保留的生命周期
@Inherited 遗传，即注解是否可被继承
@Documented 文档，用javadoc命令生成API文档，会有相关信息

@Repeatable (java1.8 新增项)
当没有@Repeatable修饰的时候，注解在同一个位置，只能出现一次
```



## @Target

目标即作用范围,target可以选择的类型如下：

```java
ElementType.TYPE：能修饰类、接口或枚举类型
ElementType.FIELD：能修饰成员变量
ElementType.METHOD：能修饰方法
ElementType.PARAMETER：能修饰参数
ElementType.CONSTRUCTOR：能修饰构造器
ElementType.LOCAL_VARIABLE：能修饰局部变量
ElementType.ANNOTATION_TYPE：能修饰注解
ElementType.PACKAGE：能修饰包
ElementType.TYPE_PARAMETER（jdk8）：可以用于标注类型参数
ElementType.TYPE_USE（jdk8）：可以用于标注任意类型(不包括class)
ElementType.MODULE（jdk9）：修饰模块
```

多个可以用数组来表示，如下：

```java
@Target({METHOD,TYPE})，表示他可以用在方法和类型上（类和接口），但是不能放在属性等其他位置。
```



## @Retention

保留的生命周期,注解按生命周期来划分可分为3类:
1、RetentionPolicy.SOURCE：注解只保留在源文件，当Java文件编译成class文件的时候，注解被遗弃；
2、RetentionPolicy.CLASS：注解被保留到class文件，但jvm加载class文件时候被遗弃，这是默认的生命周期；
3、RetentionPolicy.RUNTIME：注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在；

这3个生命周期分别对应于：Java源文件(.java文件) ---> .class文件 ---> 内存中的字节码。

那怎么来选择合适的注解生命周期呢？

首先要明确生命周期长度 SOURCE < CLASS < RUNTIME ，所以前者能作用的地方后者一定也能作用。一般如果需要在运行时去动态获取注解信息，那只能用RUNTIME注解；如果要在编译时进行一些预处理操作，比如生成一些辅助代码（如ButterKnife），就用CLASS注解；如果只是做一些检查性的操作，比如@Override和@SuppressWarnings，则可选用SOURCE注解。

## @Inherited

注解是否可被继承,即如果一个使用了@Inherited 修饰的annotation 类型被用于一个class，则这个annotation 将被用于该class 的子类。

## @Documented

文档，用javadoc命令生成API文档，会有相关信息

## @Repeatable

元注解@Repeatable是JDK1.8新加入的，它表示在同一个位置重复相同的注解。在没有该注解前，一般是无法在同一个类型上使用相同的注解的。

```java
//Java8前无法这样使用
@FilterPath("/update")
@FilterPath("/add")
public class A {}
```

Java8前如果是想实现类似的功能，我们需要在定义@FilterPath注解时定义一个数组元素接收多个值如下：

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FilterPath {
    String [] value();
}

//使用
@FilterPath({"/update","/add"})
public class A { }
```

```java
@Target({ElementType.TYPE,ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(FilterPaths.class)//参数指明接收的注解class
public @interface FilterPath {
    String  value();
}

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface FilterPaths {
    FilterPath[] value();
}

//使用案例
@FilterPath("/update")
@FilterPath("/add")
@FilterPath("/delete")
class AA{ }
```

