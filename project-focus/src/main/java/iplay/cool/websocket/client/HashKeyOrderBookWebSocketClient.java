package iplay.cool.websocket.client;//package com.kun.owl.core.mp.websocket.client;
//
//import com.kun.owl.core.mp.websocket.config.HashKeyConfig;
//import com.kun.owl.core.mp.websocket.enums.EventEnums;
//
///**
// * @author wu.dang
// * @date 2025/6/8 01:26
// */
//public class HashKeyOrderBookWebSocketClient extends HashKeyWebSocketClient {
//
//    private final String symbol;
//
//    public HashKeyOrderBookWebSocketClient(HashKeyConfig hashKeyConfig, String symbol) {
//        super(hashKeyConfig);
//        this.symbol = symbol;
//    }
//
//    @Override
//    protected String symbol() {
//        return symbol;
//    }
//
//    @Override
//    protected String topic() {
//        return EventEnums.ORDER_BOOK.getValue();
//    }
//}
