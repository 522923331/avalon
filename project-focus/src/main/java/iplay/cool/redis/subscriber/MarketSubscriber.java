package iplay.cool.redis.subscriber;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class MarketSubscriber implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String topic = new String(pattern);
        String payload = message.toString();

        log.info("收到频道 [{}] 消息：{}", topic, payload);

    }
}
