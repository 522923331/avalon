package iplay.cool.strategyFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dove
 * @date 2022/9/28
 */
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
