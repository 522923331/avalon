package iplay.cool.redis.subscriber;


import iplay.cool.redis.publisher.impl.RedisPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

//@Configuration
public class RedisPubSubConfig {

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new ChannelTopic(RedisPublisher.MARKET_TOPIC));
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(MarketSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "onMessage");
    }
}

