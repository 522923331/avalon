package iplay.cool.websocket.sub;

import com.alibaba.fastjson.JSONObject;
import iplay.cool.websocket.client.BaseWebSocketClient;
import iplay.cool.websocket.config.ChannelConfig;
import iplay.cool.websocket.listener.WebSocketMessageListener;
import iplay.cool.websocket.listener.WebSocketMessageListenerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author k02
 */
@Slf4j
public abstract class AbstractChannelSubscript implements InitializingBean {
    @Resource
    private WebSocketMessageListenerFactory webSocketMessageListenerFactory;

    public List<String> getSubscriptCurrencyPairList() {

        List<String> currencyPairList = new ArrayList<>();
        currencyPairList.add("BTCUSDT");
        currencyPairList.add("ETHUSDT");

        return currencyPairList;
    }

    public void subscribe(String uri) {
        log.info("{} WebSocket订阅的uri:{}", getChannelConfig().getChannelCode(), uri);
        if (!StringUtils.hasText(uri)) {
            return;
        }
        ChannelConfig channelConfig = getChannelConfig();
        if (channelConfig == null) {
            log.info("websocket订阅，未找到对应的通道配置！！！");
            return;
        }
        if (Boolean.FALSE.equals(switchFlag())) {
            log.info("{} WebSocket订阅关闭，未开启订阅", channelConfig.getChannelCode());
            return;
        }
        if (CollectionUtils.isEmpty(getSubscriptTopicList())) {
            log.info("{} WebSocket订阅关闭，未配置订阅的Topic", channelConfig.getChannelCode());
            return;
        }
        doSubscribe(uri, channelConfig);
    }

    private void doSubscribe(String uri, ChannelConfig channelConfig) {
        BaseWebSocketClient webSocketClient = new BaseWebSocketClient(uri, channelConfig, webSocketMessageListenerFactory) {
            @Override
            protected void subscribe() {
                for (String topic : getSubscriptTopicList()) {
                    for (String symbol : getSubscriptCurrencyPairList()) {
                        String subMsg = subscribeParam(topic, symbol);
                        this.send(subMsg);
                        log.info("{} WebSocket订阅成功，订阅的TopicParam:{}", channelConfig.getChannelCode(), subMsg);
                    }
                }
            }
        };
        webSocketClient.connect();
    }

    protected abstract boolean switchFlag();

    protected abstract String getWebSocketUrl();

    protected Object getSubMessageId() {
        return getChannelConfig().getChannelCode() + "_" + System.currentTimeMillis();
    }

    protected List<String> getSubscriptTopicList() {
        List<WebSocketMessageListener> listenerListByChannelCode = webSocketMessageListenerFactory.getListenerListByChannelCode(getChannelConfig().getChannelCode());
        return listenerListByChannelCode.stream().map(WebSocketMessageListener::topic).collect(Collectors.toList());
    }

    @Override
    public void afterPropertiesSet() {
        subscribe(getWebSocketUrl());
    }

    protected abstract ChannelConfig getChannelConfig();

    protected String subscribeParam(String topic, String symbol) {
        JSONObject subscribeMessage = new JSONObject();
        subscribeMessage.put("symbol", symbol);
        subscribeMessage.put("topic", topic);
        subscribeMessage.put("event", "sub");
        JSONObject params = new JSONObject();
        params.put("binary", false);
        subscribeMessage.put("params", params);
        subscribeMessage.put("id", getSubMessageId());
        return subscribeMessage.toJSONString();
    }

}
