package iplay.cool.websocket.listener;

import iplay.cool.websocket.enums.ChannelEnums;
import iplay.cool.websocket.enums.EventEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author wu.dang
 * @date 2025/6/6 14:46
 */
@Slf4j
@Component
public class HashKeyKlineListener implements WebSocketMessageListener {

    @Override
    public void onMessage(String message) {
        log.info("[通道:{},topic:{} WS Message]: {}", channelCode(), topic(), message);
    }

    @Override
    public String channelCode() {
        return ChannelEnums.HASH_KEY.name();
    }

    @Override
    public String topic() {
        return EventEnums.KLINE.getValue();
    }
}

