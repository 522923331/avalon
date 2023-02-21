package iplay.cool.interceptor;

import com.google.gson.Gson;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author dove
 * @date 2023/1/9
 */
@Slf4j
public class LogInterceptor implements HandlerInterceptor, Ordered {

	private final String appName;
	private static final String HEADER_USER = "user";
	public LogInterceptor(String appName) {
		this.appName = appName;
	}

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3)
			throws Exception {
		String ipAddress = "";
		String requestURI = request.getRequestURI();
		String method = request.getMethod();
		String domainName = "";
		String responseParameter;

		boolean b = (requestURI.contains("export") || requestURI.contains("upload") || requestURI.contains("swagger-ui"));
		responseParameter = "ResponseUntil.getResponseParam(response)";
		long total = -1;
		try {
			long startTime = (long) request.getAttribute("start_time");
			long endTime = System.currentTimeMillis();
			total = endTime - startTime;
		} catch (Exception e) {
			logger.error("计算耗时出错：", e);
		}
		logger.debug("[{}]响应日志:请求的域名={},请求的api={},返回参数={},响应的时间={},请求的ip={},接口耗时毫秒值={},响应的uuid={},请求方式{}",
				appName, domainName, requestURI, responseParameter,
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(System.currentTimeMillis()), ipAddress, total, request.getAttribute("unique_request"),method);
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		log.info("============LogInterceptor111 preHandle==============");
		String uniqueRequest = UUID.randomUUID().toString();
		uniqueRequest = uniqueRequest.replace("-","");
		request.setAttribute("unique_request", uniqueRequest);
		request.setAttribute("start_time", System.currentTimeMillis());
		String ipAddress = "";
		String requestURI = request.getRequestURI();
		Map<String, String> parameterMap = new HashMap<>();
		String domainName = "";

		String userStr = request.getHeader(HEADER_USER);
		String uid = "";
		if(StringUtils.isNotBlank(userStr)){
			Gson gson = new Gson();
			Map<String,Object> map = gson.fromJson(userStr, Map.class);
			uid = (String)map.get("uid");
		}

		logger.info("[{}]请求日志:操作用户的id={},请求的域名={},请求的api={},请求参数={},请求的时间={},请求的ip={},请求的uuid={}", appName,uid,domainName, requestURI, parameterMap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(System.currentTimeMillis()), ipAddress, uniqueRequest);
		return true;
	}

	@Override
	public int getOrder() {
		return -1;
	}
}
