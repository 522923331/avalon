package iplay.cool.websocket.config;//package iplay.cool.websocket.config;
//
//import lombok.Data;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.List;
//
///**
// * @author wu.dang
// * @date 2025/6/7 14:33
// */
////@Configuration
////@ConfigurationProperties(prefix = "channels.binance")
//@Data
//public class BinanceConfig implements ChannelConfig {
//    private String webSocketPublicUrl;
//    private String webSocketPrivateUrl;
//    private boolean publicEnabled;
//    private boolean privateEnabled;
//    private Long heartbeatInterval = 30000L;
//    private final String channelCode = "BINANCE";
//
//    @Override
//    public List<String> getTopicList() {
//        return List.of();
//    }
//}
