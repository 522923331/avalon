package iplay.cool.controller;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
public class AlarmController {
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {

		String secret = "demo";
		int timestamp = 100;
		System.out.printf("sign: %s", GenSign(secret, timestamp));

	}
	private static String GenSign(String secret, int timestamp) throws NoSuchAlgorithmException, InvalidKeyException {
		//把timestamp+"\n"+密钥当做签名字符串
		String stringToSign = timestamp + "\n" + secret;

		//使用HmacSHA256算法计算签名
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
		byte[] signData = mac.doFinal(new byte[]{});
		return new String(Base64.encodeBase64(signData));
	}

	@GetMapping("/testAlarm")
	public String testAlarm(){
		int i = 1/0;
		return "success";
	}


	@GetMapping("/testAlarmText")
	public String testAlarmText(){
		String str ="{\"msg_type\": \"text\", \"content\": {\"text\": \"新更新提醒\"}}";

		HttpUtil.post("https://open.larksuite.com/open-apis/bot/v2/hook/f9c42b78-6334-416f-8e4a-d45123f2f025",str);
		return "success";
	}

	@GetMapping("/testAlarmCard")
	public String testAlarmCard(){
//		String str ="{\n" + "    \"msg_type\": \"interactive\",\n" + "    \"card\": {\n" + "        \"config\": {\n"
//				+ "                \"wide_screen_mode\": true,\n" + "                \"enable_forward\": true\n"
//				+ "        },\n" + "        \"elements\": [{\n" + "                \"tag\": \"div\",\n"
//				+ "                \"text\": {\n"
//				+ "                        \"content\": \"**西湖**，位于浙江省杭州市西湖区龙井路1号，杭州市区西部，景区总面积49平方千米，汇水面积为21.22平方千米，湖面面积为6.38平方千米。\",\n"
//				+ "                        \"tag\": \"lark_md\"\n" + "                }\n" + "        }, {\n"
//				+ "                \"actions\": [{\n" + "                        \"tag\": \"button\",\n"
//				+ "                        \"text\": {\n"
//				+ "                                \"content\": \"更多景点介绍 :玫瑰:\",\n"
//				+ "                                \"tag\": \"lark_md\"\n" + "                        },\n"
//				+ "                        \"url\": \"https://www.example.com\",\n"
//				+ "                        \"type\": \"default\",\n" + "                        \"value\": {}\n"
//				+ "                }],\n" + "                \"tag\": \"action\"\n" + "        }],\n"
//				+ "        \"header\": {\n" + "                \"title\": {\n"
//				+ "                        \"content\": \"今日旅游推荐\",\n"
//				+ "                        \"tag\": \"plain_text\"\n" + "                }\n" + "        }\n"
//				+ "    }\n" + "}";


//		String str = "{\"msg_type\":\"interactive\",\"card\":{\"config\":{\"wide_screen_mode\":true,\"enable_forward\":true},\"header\":{\"title\":{\"tag\":\"plain_text\",\"content\":\"错误日志告警\"},\"template\":\"red\"},\"elements\":[{\"text\":{\"tag\":\"lark_md\",\"content\":\"**日志类名：**org.apache.catalina.core.ContainerBase.[Tomcat].[localhost].[/].[dispatcherServlet]\"},\"tag\":\"div\"},{\"text\":{\"tag\":\"lark_md\",\"content\":\"线程名称：http-nio-8080-exec-3\"},\"tag\":\"div\"},{\"text\":{\"tag\":\"lark_md\",\"content\":\"**日志消息：**Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is java.lang.ArithmeticException: / by zero] with root cause\"},\"tag\":\"div\"},{\"text\":{\"tag\":\"lark_md\",\"content\":\"异常堆栈：java.lang.ArithmeticException: / by zero\"},\"tag\":\"div\"}]}}";
		String str = "{\"card\":{\"config\":{\"wideScreenMode\":true,\"enableForward\":true},\"header\":{\"title\":{\"tag\":\"plain_text\",\"content\":\"错误日志告警\"},\"template\":\"red\"},\"elements\":[{\"text\":{\"tag\":\"lark_md\",\"content\":\"**日志类名：**org.apache.catalina.core.ContainerBase.[Tomcat].[localhost].[/].[dispatcherServlet]\"},\"tag\":\"div\"},{\"text\":{\"tag\":\"lark_md\",\"content\":\"线程名称：http-nio-8080-exec-2\"},\"tag\":\"div\"},{\"text\":{\"tag\":\"lark_md\",\"content\":\"**日志消息：**Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is java.lang.ArithmeticException: / by zero] with root cause\"},\"tag\":\"div\"},{\"text\":{\"tag\":\"lark_md\",\"content\":\"异常堆栈：java.lang.ArithmeticException: / by zero\\n\\tat iplay.cool.controller.AlarmController.testAlarm(AlarmController.java:36)\\n\\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\\n\\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\\n\\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\\n\\tat java.base/java.lang.reflect.Method.invoke(Method.java:566)\\n\\tat org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)\\n\\tat org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:150)\\n\\tat org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:117)\\n\\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:895)\\n\\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:808)\\n\\tat org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\\n\\tat org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1067)\\n\\tat org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:963)\\n\\tat org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)\\n\\tat org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898)\\n\\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:655)\\n\\tat org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)\\n\\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:764)\\n\\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:227)\\n\\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\\n\\tat org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)\\n\\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\\n\\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\\n\\tat org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)\\n\\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\\n\\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\\n\\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\\n\\tat org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)\\n\\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\\n\\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\\n\\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\\n\\tat org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)\\n\\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\\n\\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\\n\\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\\n\\tat org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:197)\\n\\tat org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97)\\n\\tat org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:541)\\n\\tat org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:135)\\n\\tat org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)\\n\\tat org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78)\\n\\tat org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:360)\\n\\tat org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:399)\\n\\tat org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65)\\n\\tat org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:890)\\n\\tat org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1743)\\n\\tat org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)\\n\\tat org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191)\\n\\tat org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)\\n\\tat org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\\n\\tat java.base/java.lang.Thread.run(Thread.java:834)\\n\"},\"tag\":\"div\"}]},\"msgType\":\"interactive\"}";
		String s = CharSequenceUtil.toUnderlineCase(str);
		String post = HttpUtil.post(
				"https://open.larksuite.com/open-apis/bot/v2/hook/f9c42b78-6334-416f-8e4a-d45123f2f025", s);
		System.out.printf(post);
		return "success";
	}
}
