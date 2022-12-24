本模块涉及两个技术点：

# 1.在业务中使用是工厂策略模式

1.首先需要创建一个Handler，比如BlindBoxHandler

```java
public interface BlindBoxHandler {
	/**
	 * 获取类型
	 * @return String
	 */
	String getHandlerType();

	/**
	 * 开启盲盒 不同游戏开启方式不同
	 * @param param 入参
	 * @return BlindBoxVO
	 */
	BlindBoxVO openBox(BlindBoxOpenParam param);
}
```

2.创建一个初始化的HandlerFactory

```java
@Component
public class BlindBoxHandlerFactory implements ApplicationContextAware, InitializingBean {
	private ApplicationContext applicationContext;
	private static final Map<String, BlindBoxHandler> BOX_MAP = new ConcurrentHashMap<>();

	@Override
	public void afterPropertiesSet() throws Exception {
		//getBeansOfType，也就是说，BlindBoxHandler的子类都得是bean
		applicationContext.getBeansOfType(BlindBoxHandler.class).values().forEach(handler -> BOX_MAP.put(handler.getHandlerType(), handler));
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * 获取handler
	 * @param handlerType 类型
	 * @return BlindBoxHandler
	 */
	public BlindBoxHandler getBlindBoxHandler(String handlerType){
		return BOX_MAP.get(handlerType);
	}
}
```

3.创建handlerType的枚举类

```java
public enum BlindBoxEnum {
	/**
	 * 骑士与契约
	 */
	MIDGARD_SAGA("MidgardSaga", "骑士与契约"),
	OATH_OF_PEAK("OathofPeak", "玄中记"),
	;
	private String handlerType;
	private String desc;
	BlindBoxEnum(String handlerType,String desc){
		this.handlerType = handlerType;
		this.desc = desc;
	}

	public String getHandlerType() {
		return handlerType;
	}

	public String getDesc() {
		return desc;
	}
}
```

4.业务实现Handler，并且使用定义好的枚举类

```java
@Slf4j
@Component
public class MidgardBlindBoxHandler implements BlindBoxHandler{
	@Override
	public String getHandlerType() {
		return BlindBoxEnum.MIDGARD_SAGA.getHandlerType();
	}

	@Override
	public BlindBoxVO openBox(BlindBoxOpenParam param) {
		//具体业务处理。。。。
		return null;
	}
}

@Slf4j
@Component
public class OathOfPeakBlindBoxHandler implements BlindBoxHandler{
	@Override
	public String getHandlerType() {
		return BlindBoxEnum.OATH_OF_PEAK.getHandlerType();
	}

	@Override
	public BlindBoxVO openBox(BlindBoxOpenParam param) {
		//具体业务处理。。。。
		return null;
	}
}
```

参数类和返回的实例类都可以创建空类来代替。







# 2.actuator的使用

其中1.x和2.x的配置和使用差别挺大的。我目前使用相对较新的版本springboot 2.6.7，这里只说2.x版本

## 一、什么是actuator？

Spring Boot Actuator 模块提供了生产级别的功能，比如健康检查，审计，指标收集，HTTP 跟踪等，帮助我们监控和管理Spring Boot 应用。

这个模块是一个采集应用内部信息暴露给外部的模块，上述的功能都可以通过HTTP 和 JMX 访问。

因为暴露内部信息的特性，Actuator 也可以和一些外部的应用监控系统整合（Prometheus, Graphite, DataDog, Influx, Wavefront, New Relic等）。

这些监控系统提供了出色的仪表板，图形，分析和警报，可帮助你通过一个统一友好的界面，监视和管理你的应用程序。

## 二、集成actuator

1.添加maven 依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

添加上依赖后，就可以访问actuator和actuator/health信息，别的好像都不能访问，需要配置开启权限，配置如下：

```properties
# 可以這樣寫，就會開啟所有endpoints(不包含shutdown)
management.endpoints.web.exposure.include=*
 
# 也可以這樣寫，就只會開啟指定的endpoint，因此此處只會再額外開啟/actuator/beans和/actuator/mappings
management.endpoints.web.exposure.include=beans,mappings
 
# exclude可以用來關閉某些endpoints
# exclude通常會跟include一起用，就是先include了全部，然後再exclude /actuator/beans這個endpoint
management.endpoints.web.exposure.exclude=beans
management.endpoints.web.exposure.include=*
 
# 如果要開啟/actuator/shutdown，要額外再加這一行
management.endpoint.shutdown.enabled=true
```

## 三、Actuator提供了哪些监控功能？

为了了解应用程序运行时的内部状况，Actuator 提供了众多丰富的监控功能，随着版本的增加，可以监控的端点也在增加。

官网：https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready-endpoints 展示了所有的端点，并对每个端点的功能进行了说明，在Exposing Endpoints小节里面还介绍了JMX和WEB默认可以访问的端点有哪些。web默认可以访问health和info。



这些端点可以分为三大类：配置端点、度量端点和其它端点，完整的端点功能介绍如下表。

序号	端点	功能
1	/configprops	展示所有@ConfigurationProperties注解信息
2	/beans	展示spring容器中所有的bean
3	/threaddump	执行一个线程dump
4	/env	展示所有的环境变量
5	/flyway	获取已应用的所有Flyway数据库迁移信息，需要一个或多个 Flyway Bean
6	/health	展示应用的健康信息
7	/info	展示应用的基本信息
8	/mappings	展示所有@RequestMapping的路径
9	/metrics	展示应用的度量信息，比如内存用量和HTTP请求计数
10	/shutdown	优雅的关闭应用程序，默认不能通过web访问
11	/httptrace	显示HTTP足迹，默认展示最近100个HTTP request/repsponse
12	/auditevents	展示当前应用的审计事件信息，这个和AuditEventRepository有关
13	/caches	展示系统中的缓存数据
14	/startup	展示由ApplicationStartup收集的启动步骤数据，这个需要配置BufferingApplicationStartup
15	/scheduledtasks	展示系统中的缓存数据
16	/sessions	允许从Spring Session支持的会话存储中检索和删除用户会话，这个需要在基于Servlet的Web应用程序中使用
17	/integrationgraph	显示 Spring Integration 图。需要依赖 spring-integration-core
18	/loggers	显示和修改应用程序中日志的配置
19	/liquibase	获取已应用的所有Liquibase数据库迁移。需要一个或多个 Liquibase Bean
20	/heapdump	返回一个heap dump文件
21	/jolokia	通过HTTP暴露JMX bean（当Jolokia在类路径上时，不适用于WebFlux）。需要依赖 jolokia-core
22	/logfile	返回日志文件的内容（如果已设置logging.file.name或logging.file.path属性）
23	/prometheus	以Prometheus服务器可以抓取的格式暴露指标，需要依赖 micrometer-registry-prometheus
其中20-23只能在web环境下使用。