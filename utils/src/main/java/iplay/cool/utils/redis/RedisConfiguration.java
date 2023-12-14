package iplay.cool.utils.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@ConditionalOnClass(RedisTemplate.class)
public class RedisConfiguration {
    @Bean
    public RedisCacheUtils redisCacheUtils() {
        return new RedisCacheUtils();
    }
}
