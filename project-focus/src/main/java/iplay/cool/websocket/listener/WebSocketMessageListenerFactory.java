package iplay.cool.websocket.listener;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author wu.dang
 * @date 2025/6/6 17:38
 */
@Component
public class WebSocketMessageListenerFactory {
    private final Map<String, WebSocketMessageListener> listenerMap = new ConcurrentHashMap<>();

    private WebSocketMessageListenerFactory(List<WebSocketMessageListener> listeners) {
        listeners.forEach(listener ->
                listenerMap.put(listener.channelCode() + ":" + listener.topic(), listener)
        );
    }

    public WebSocketMessageListener getMessageListener(String channelCode,String topic) {
        return listenerMap.get(channelCode+":"+topic);
    }

    public List<WebSocketMessageListener> getListenerListByChannelCode(String channelCode) {
        return listenerMap.values().stream()
                .filter(listener -> listener.channelCode().equals(channelCode))
                .collect(Collectors.toList());
    }
}
