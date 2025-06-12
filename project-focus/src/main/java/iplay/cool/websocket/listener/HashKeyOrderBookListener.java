package iplay.cool.websocket.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import iplay.cool.redis.publisher.impl.RedisPublisher;
import iplay.cool.websocket.enums.ChannelEnums;
import iplay.cool.websocket.enums.EventEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author wu.dang
 * @date 2025/6/6 14:46
 */
@Slf4j
@Component
public class HashKeyOrderBookListener implements WebSocketMessageListener {
    @Resource
    private RedisPublisher redisPublisher;

    @Override
    public void onMessage(String message) {
        log.info("[通道:{},topic:{} WS Message]: {}", channelCode(), topic(), message);
        JSONObject jsonMessage = com.alibaba.fastjson.JSON.parseObject(message);
        processOrderBookMessage(jsonMessage);

    }

    @Override
    public String channelCode() {
        return ChannelEnums.HASH_KEY.name();
    }

    @Override
    public String topic() {
        return EventEnums.ORDER_BOOK.getValue();
    }

    private void processOrderBookMessage(JSONObject jsonMessage) {
        String symbol = jsonMessage.getString("symbol");
        long sendTime = jsonMessage.getLong("sendTime");
        JSONObject data = jsonMessage.getJSONArray("data").getJSONObject(0);
        log.info("HashKey OrderBook data : {}", data.toJSONString());
        // 买单
        JSONArray buyOrderBook = data.getJSONArray("b");
        BigDecimal bidPrice = null;
        if (Objects.nonNull(buyOrderBook) && !buyOrderBook.isEmpty()) {
            log.info("买单订单薄数据为：{}", buyOrderBook);
            JSONArray jsonArray = buyOrderBook.getJSONArray(0);
            bidPrice = jsonArray.getBigDecimal(0);
        }
        // 卖单
        JSONArray sellOrderBook = data.getJSONArray("a");
        BigDecimal askPrice = null;
        if (Objects.nonNull(sellOrderBook) && !sellOrderBook.isEmpty()) {
            log.info("卖单订单薄数据为：{}", sellOrderBook);
            JSONArray jsonArray = sellOrderBook.getJSONArray(0);
            askPrice = jsonArray.getBigDecimal(0);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timestamp", sendTime);
        jsonObject.put("buyPrice", bidPrice);
        jsonObject.put("sellPrice", askPrice);

//        String key = String.format("owl:orderBook:cache:%s", channelCode());
//        redisCacheUtils.hPut(key,symbol, JSON.toJSONString(jsonObject));
//
//        SocketMessageNotifyDTO notifyDTO = new SocketMessageNotifyDTO();
//        notifyDTO.setChannelCode(channelCode());
//        notifyDTO.setSymbol(symbol);

//        redisPublisher.publish(key, JSON.toJSONString(notifyDTO));

        redisPublisher.publish(RedisPublisher.MARKET_TOPIC, jsonObject.toJSONString());
    }
}

