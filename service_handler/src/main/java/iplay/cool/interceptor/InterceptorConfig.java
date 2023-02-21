package iplay.cool.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dove
 * @date 2023/2/17
 */
@Slf4j
public class InterceptorConfig  implements WebMvcConfigurer {

	@Resource
	private LogInterceptor logInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		log.info("启动拦截，日志处理拦截器");

		List<String> excludePathList = new ArrayList<>();
		excludePathList.add("/swagger**/**");
		excludePathList.add("/webjars/**");
		excludePathList.add("/v3/**");
		excludePathList.add("/doc.html");
		excludePathList.add("/swagger-resources/**");
		excludePathList.add("/webjars/**");
		excludePathList.add("/v2/**");
		excludePathList.add("/v2/**");
		excludePathList.add("/swagger-ui/**");
		excludePathList.add("/swagger**/**");
		excludePathList.add("/swagger-ui.html/**");
		// 自定义拦截器，添加拦截路径和排除拦截路径
		registry.addInterceptor(logInterceptor).addPathPatterns("/**")
				.excludePathPatterns(excludePathList);
	}
}
