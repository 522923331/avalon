//package iplay.cool.utils;
//
//import cn.hutool.json.JSONUtil;
//import com.echain.constant.RocketMqConstant;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.client.producer.SendCallback;
//import org.apache.rocketmq.client.producer.SendResult;
//import org.apache.rocketmq.spring.core.RocketMQTemplate;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
//@Slf4j
//@Component
//public class RocketMqUtils {
//
//    @Resource(name = "rocketMQTemplate")
//    private RocketMQTemplate template;
//
//    /**
//     * 获取模板，如果封装的方法不够提供原生的使用方式
//     */
//    public RocketMQTemplate getTemplate() {
//        return template;
//    }
//
//    /**
//     * 构建目的地
//     */
//    public String buildDestination(String topic, String tag) {
//        return topic + RocketMqConstant.DELIMITER + tag;
//    }
//
//    public <T> void asyncSend(String topic, String tag, T data) {
//        String destination = buildDestination(topic, tag);
//        asyncSend(destination, data);
//    }
//
//    public <T> void asyncSend(String topic, String tag, T data, SendCallback sendCallback) {
//        String destination = buildDestination(topic, tag);
//        asyncSend(destination, data, sendCallback);
//    }
//
//    private <T> void asyncSend(String topic, T data) {
//        template.asyncSend(topic, data, new SendCallback() {
//            @Override
//            public void onSuccess(SendResult sendResult) {
//                log.info("异步消息发送成功,topic:{},发送结果：{}", topic, sendResult);
//            }
//
//            @Override
//            public void onException(Throwable throwable) {
//                log.error("异步消息发送失败,topic:{},msg:{},失败信息：", topic, JSONUtil.toJsonStr(data), throwable);
//                //报警
//
//            }
//        });
//    }
//
//    private <T> void asyncSend(String topic, T data, SendCallback sendCallback) {
//        template.asyncSend(topic, data, sendCallback);
//    }
//}