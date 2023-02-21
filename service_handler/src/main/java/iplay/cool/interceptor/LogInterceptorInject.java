package iplay.cool.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dove
 * @date 2023/2/17
 */
@Slf4j
@Configuration
public class LogInterceptorInject {
	@Bean
	@ConditionalOnMissingBean(LogInterceptor.class)
	public LogInterceptor logInterceptor() {
		log.info("请求响应日志初始化");
		return new LogInterceptor("appName");
	}

	@Bean
	@ConditionalOnMissingBean(InterceptorConfig.class)
	public InterceptorConfig interceptorConfig(){
		return new InterceptorConfig();
	}
}
