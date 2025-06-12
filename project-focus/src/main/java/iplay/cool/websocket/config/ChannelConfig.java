package iplay.cool.websocket.config;

import java.util.List;

/**
 * @author wu.dang
 * @date 2025/6/7 13:54
 */
public interface ChannelConfig {
    String getWebSocketPrivateUrl();
    String getWebSocketPublicUrl();
    boolean isPrivateEnabled();
    boolean isPublicEnabled();
    Long getHeartbeatInterval();
    String getChannelCode();
    List<String> getPrivateTopicList();
    List<String> getPublicTopicList();
    String getListenerKeyUrl();

}
