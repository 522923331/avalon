package iplay.cool.websocket.listener;

/**
 * @author wu.dang
 * @date 2025/6/6 15:10
 */
public interface WebSocketMessageListener {
    void onMessage(String message);
    String channelCode();
    String topic();
}
