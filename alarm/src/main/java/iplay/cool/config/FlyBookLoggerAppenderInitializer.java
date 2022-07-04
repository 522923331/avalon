package iplay.cool.config;

/**
 *
 * @author dove
 * @date 2022/6/20
 */

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import iplay.cool.append.LarkAppender;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 飞书日志初始化器
 *
 * @author venus
 * @version 1
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class FlyBookLoggerAppenderInitializer implements ApplicationListener<ApplicationReadyEvent> {

	private final LarkLoggerAppenderConfigProperties properties;

	/**
	 * Handle an application event.
	 *
	 * @param event the event to respond to
	 */
	@Override
	public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
		if (!this.properties.isEnabled()) {
			return;
		}
		// 添加飞书日志Appender
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		LarkAppender flyBookAppender = new LarkAppender();
		flyBookAppender.setName("FLY_BOOK");
		flyBookAppender.setAlertUrl(this.properties.getAlertUrl());
		flyBookAppender.setContext(loggerContext);
		flyBookAppender.start();

		Logger logger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
		logger.addAppender(flyBookAppender);
	}
}

