package iplay.cool.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author dove
 * @date 2023/1/9
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	/**
	 * 默认是按拦截器的注册顺序执行，如果实现了order接口，那么会按照order对应的数组从小到大执行，数值越小，优先级越高
	 * @param registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LogInterceptor2());
		registry.addInterceptor(new LogInterceptor3());
		registry.addInterceptor(new LogInterceptor());
	}
}
