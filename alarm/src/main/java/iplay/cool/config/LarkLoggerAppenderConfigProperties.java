package iplay.cool.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author dove
 * @date 2022/6/20
 */
@Data
@Component
@ConfigurationProperties(prefix = "alarm.lark")
public class LarkLoggerAppenderConfigProperties {
	/**
	 * 是否开启
	 */
	private boolean enabled;
	/**
	 * 告警url
	 */
	private String alertUrl;
}
