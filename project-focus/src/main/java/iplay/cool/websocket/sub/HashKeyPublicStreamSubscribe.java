package iplay.cool.websocket.sub;

import iplay.cool.websocket.config.ChannelConfig;
import iplay.cool.websocket.config.HashKeyConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author wu.dang
 * @date 2025/6/8 03:16
 */
@Slf4j
@Component
public class HashKeyPublicStreamSubscribe extends AbstractChannelSubscript {
    @Resource
    private HashKeyConfig hashKeyConfig;

    @Override
    protected ChannelConfig getChannelConfig() {
        return hashKeyConfig;
    }

    @Override
    protected boolean switchFlag() {
        return hashKeyConfig.isPublicEnabled();
    }


    @Override
    protected String getWebSocketUrl() {
        return hashKeyConfig.getWebSocketPublicUrl();
    }
}
