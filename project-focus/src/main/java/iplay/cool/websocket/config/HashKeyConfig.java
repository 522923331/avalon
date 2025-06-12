package iplay.cool.websocket.config;

import iplay.cool.websocket.enums.ChannelEnums;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author wu.dang
 * @date 2025/6/7 14:32
 */
@Configuration
@ConfigurationProperties(prefix = "channels.hashkey")
@Data
public class HashKeyConfig implements ChannelConfig {
    private String webSocketPrivateUrl;
    private String webSocketPublicUrl;
    private boolean privateEnabled = true;
    private boolean publicEnabled = true;
    private Long heartbeatInterval = 10000L;
    private final String channelCode = ChannelEnums.HASH_KEY.name();
    private List<String> privateTopicList;
    private List<String> publicTopicList;
    private String listenerKeyUrl;
}
