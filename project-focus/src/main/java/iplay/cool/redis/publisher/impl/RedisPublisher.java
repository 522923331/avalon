package iplay.cool.redis.publisher.impl;

import iplay.cool.redis.publisher.Publisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
@Slf4j
public class RedisPublisher implements Publisher {

    public static final String MARKET_TOPIC = "publisher:marketData";


    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void publish(String key, String value) {
        stringRedisTemplate.convertAndSend(key, value);
        log.info("最新的发布,{},{}", key, value);
    }

}
