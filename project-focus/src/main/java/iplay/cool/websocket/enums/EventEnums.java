package iplay.cool.websocket.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wu.dang
 * @date 2025/6/7 13:27
 */
@Getter
@AllArgsConstructor
public enum EventEnums {
    ORDER_BOOK("depth"),
    TRADE("trade"),
    TICKER("ticker"),
    KLINE("kline"),
    ;
    private final String value;
}
