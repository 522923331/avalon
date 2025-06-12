package iplay.cool.websocket.client;//package iplay.cool.websocket.client;
//
//import iplay.cool.websocket.config.HashKeyConfig;
//import iplay.cool.websocket.enums.ChannelEnums;
//import iplay.cool.websocket.listener.WebSocketMessageListenerFactory;
//
//import java.util.List;
//
///**
// * @author wu.dang
// * @date 2025/6/6 17:12
// */
//public class HashKeyWebSocketClient extends BaseWebSocketClient {
//
//    private final List<String> subscriptTopicList;
//
//    public HashKeyWebSocketClient(String uri, HashKeyConfig hashKeyConfig, WebSocketMessageListenerFactory webSocketMessageListenerFactory, List<String> subscriptTopicList) {
//        super(uri,hashKeyConfig, webSocketMessageListenerFactory);
//        this.subscriptTopicList = subscriptTopicList;
//    }
//
//    @Override
//    protected String channelCode() {
//        return ChannelEnums.HASH_KEY.name();
//    }
//
//    @Override
//    protected void subscribe() {
//        subscriptTopicList.forEach(this::send);
//    }
//
//}
