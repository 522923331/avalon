package iplay.cool.redis.publisher;


public interface Publisher {

    /**
     * 发布价格消息
     */
    void publish(String topic, String value);
}
