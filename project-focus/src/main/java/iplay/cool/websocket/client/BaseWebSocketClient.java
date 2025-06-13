package iplay.cool.websocket.client;

/**
 * @author wu.dang
 * @date 2025/6/6 14:58
 */

import com.alibaba.fastjson.JSONObject;
import iplay.cool.websocket.config.ChannelConfig;
import iplay.cool.websocket.listener.WebSocketMessageListener;
import iplay.cool.websocket.listener.WebSocketMessageListenerFactory;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
public abstract class BaseWebSocketClient extends WebSocketClient {
    private final WebSocketMessageListenerFactory webSocketMessageListenerFactory;
    private final ChannelConfig channelConfig;
    protected volatile long lastPongReceivedTime;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, r -> {
        Thread t = new Thread(r, "ws-scheduler");
        t.setDaemon(true);
        return t;
    });
    private final Map<String, ScheduledFuture<?>> futureMap = new ConcurrentHashMap<>();
    private static final long RECONNECT_DELAY_MS = 10_000;
    private volatile boolean isReConnecting = false;


    public BaseWebSocketClient(String uri, ChannelConfig channelConfig, WebSocketMessageListenerFactory webSocketMessageListenerFactory) {
        super(URI.create(uri));
        this.channelConfig = channelConfig;
        this.webSocketMessageListenerFactory = webSocketMessageListenerFactory;
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        log.info("{} webSocket已连接, serverURL: {}", channelCode(), getURI());
        isReConnecting = false;
        lastPongReceivedTime = System.currentTimeMillis();
        subscribe();
        startHeartbeat();

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("{} webSocket已关闭准备重连, channel: {}, reason: {}，remote：{}", channelCode(), getURI(), reason, remote);
        stopHeartbeat();
        scheduleReconnect();
    }

    @Override
    public void onError(Exception ex) {
        log.error("WebSocket error: {}", ex.getMessage(), ex);
        isReConnecting = false;
    }

    @Override
    public void close() {
        log.info("{} webSocket已关闭", channelCode());
        stopHeartbeat();
        scheduleReconnect();
    }

    @Override
    public void reconnect() {
        log.info("{} WebSocket重连开始", channelConfig.getChannelCode());
        try {
            if (isOpen()) {
                log.info("{} WebSocket已打开，无需重连,重启心跳", channelConfig.getChannelCode());
                stopHeartbeat();
                lastPongReceivedTime = System.currentTimeMillis();
                startHeartbeat();
                return;
            } else {
                if (this.getReadyState() != ReadyState.NOT_YET_CONNECTED) {
                    closeBlocking();
                }
            }
            this.reconnectBlocking();
        } catch (InterruptedException e) {
            log.error("通道{} websocket重连失败,e:", channelCode(), e);
        }
        isReConnecting = false;
    }

    private void scheduleReconnect() {
        if (isReConnecting) {
            log.info("{} WebSocket正在重连中，请稍后...", channelConfig.getChannelCode());
            return;
        }
        log.info("{} WebSocket正在重连...", channelConfig.getChannelCode());
        scheduler.schedule(() -> {
            log.info("{} WebSocket正在重连...isReConnecting:{}", channelConfig.getChannelCode(), isReConnecting);
            if (!isReConnecting) {
                isReConnecting = true;
                this.reconnect();
            }
        }, RECONNECT_DELAY_MS, TimeUnit.MILLISECONDS);

    }

    @Override
    public void sendPing() {
        if (!isOpen()) {
            log.warn("通道{}连接未建立，跳过发送ping", channelCode());
            return;
        }
        String ping = "{\"ping\":" + System.currentTimeMillis() + "}";
        log.debug("往通道{}发送ping: {}", channelCode(), ping);
        send(ping);
    }

    @Override
    public void onMessage(String message) {
        log.info("[通道 {} WS Message]: {}", channelCode(), message);
        JSONObject json = JSONObject.parseObject(message);
        if (json.containsKey("pong")) {
            lastPongReceivedTime = System.currentTimeMillis();
            return;
        }
        String topic = String.valueOf(json.get("topic"));
        WebSocketMessageListener messageListener = webSocketMessageListenerFactory.getMessageListener(channelCode(), topic);
        if (messageListener != null) {
            messageListener.onMessage(message);
        } else {
            log.warn("未找到对应的监听器，channel:{},topic: {},message:{}", channelCode(), topic, message);
        }
    }

    protected String channelCode() {
        return channelConfig.getChannelCode();
    }


    protected void startHeartbeat() {
        log.info("开始启动心跳任务，channel:{}", channelCode());
        if (futureMap.containsKey(channelCode())) {
            return;
        }
        Runnable task = () -> {
            try {
                if (isNeedReconnect()) {
                    log.warn("通道{}心跳超时，触发重连", channelCode());
                    this.reconnect();
                } else {
                    sendPing();
                }
            } catch (Exception e) {
                log.error("心跳任务运行异常，调度线程将继续: {}", e.getMessage(), e);
            }
        };
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(task, 0, channelConfig.getHeartbeatInterval(), TimeUnit.MILLISECONDS);
        futureMap.put(channelCode(), future);
    }

    protected void stopHeartbeat() {
        log.info("{}通道停止订阅消息心跳", channelCode());
        ScheduledFuture<?> future = futureMap.remove(channelCode());
        if (future != null) {
            future.cancel(true);
        }
    }

    protected boolean isNeedReconnect() {
        return System.currentTimeMillis() > getLastPongReceivedTime() + channelConfig.getHeartbeatInterval() * 2;
    }

    protected long getLastPongReceivedTime() {
        return lastPongReceivedTime;
    }

    protected abstract void subscribe();
}
