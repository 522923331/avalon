//package iplay.cool.utils;
//
//import io.micrometer.core.instrument.Counter;
//import io.micrometer.core.instrument.Metrics;
//import io.micrometer.core.instrument.Tag;
//import io.micrometer.core.instrument.Tags;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import okhttp3.*;
//
//import javax.annotation.Nullable;
//import javax.validation.constraints.NotNull;
//import java.io.IOException;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.Proxy;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
///**
// * 这个监测类，使用到了Metric，可以与普罗米修斯搭配使用。版本：
// * <dependency>
// *             <groupId>io.micrometer</groupId>
// *             <artifactId>micrometer-core</artifactId>
// *             <version>1.11.1</version>
// *         </dependency>
// * 实际上项目中我并没有用到Metric，而是通过日志的方式查看的。如果不需要可以去除该部分
// * @author wu.dang
// * @since 2023/6/21
// */
//@Slf4j
//public class MetricEventListenerFactory implements EventListener.Factory {
//    public static final String connectTimer = "ok.connect.timer";
//    public static final String requestCallTimer = "ok.request.call.timer";
//    public static final String requestSendTimer = "ok.request.send.timer";
//    public static final String requestTTFBTimer = "ok.request.ttfb.timer";
//    public static final String responseReceiveTimer = "ok.response.receive.timer";
//    public static final long dnsSlowThresholdMills = 5;
//    private final Counter requestTotal;
//    private Tags tags;
//    private Counter requestBusy;
//
//    public MetricEventListenerFactory(Tags tags) {
//        this.tags = tags;
//        requestBusy = Metrics.counter("ok.request.busy", tags);
//        requestTotal = Metrics.counter("ok.request.total", tags);
//
////        log.info("requestBusy={},requestTotal={}", requestBusy, requestTotal);
//    }
//
//
//    @Override
//    public EventListener create(Call call) {
//        return new MonitorEventListener(tags);
//    }
//
//    public final class MonitorEventListener extends EventListener {
//        public MonitorEventListener(Iterable<? extends Tag> tags) {
//            this.tags = tags;
//        }
//
//        private Iterable<? extends Tag> tags;
//        private long callStart;
//        private long callFinished;
//        private long requestHeadersStart;
//        private long requestHeadersEnd;
//        private long requestBodyStart;
//        private long requestBodyEnd;
//        private long requestSendFinished;
//        private long responseHeadersStart;
//        private long responseHeadersEnd;
//        private long responseReceiveFinished;
//        private long responseBodyStart;
//        private long connectStart;
//        private long connectFinished;
//        private long dnsStart;
//        private long dnsEnd;
//        private String domainName;
//        private InetSocketAddress inetSocketAddress;
//        private Exception requestFailedException;
//        private IOException responseFailedException;
//        private Exception callFailedException;
//        private long secureConnectStart;
//        private long secureConnectEnd;
//
//        private long connectionAcquired;
//        private long connectionReleased;
//
//        private long proxyStart;
//        private long proxyEnd;
//
//        private long cacheHit;
//        private long cacheMiss;
//        private long cacheConditionHit;
//        private long canceled;
//
//        @Override
//        public void callEnd(Call call) {
//            recodeAfterCallFinished(call);
//        }
//
//        @Override
//        public void callFailed(Call call, IOException ioe) {
//            callFailedException = ioe;
//            recodeAfterCallFinished(call);
//        }
//
//        private void recodeAfterCallFinished(Call call) {
//            callFinished = System.nanoTime();
//            requestBusy.increment(-1);
//            Metrics.timer(requestCallTimer, getTags(call, callFailedException)).record(callFinished - callStart, TimeUnit.NANOSECONDS);
//            if (requestSendFinished > 0) {
//                Metrics.timer(requestSendTimer, getTags(call, requestFailedException)).record(requestSendFinished - requestHeadersStart, TimeUnit.NANOSECONDS);
//            }
//            if (responseHeadersStart > 0) {
//                Metrics.timer(requestTTFBTimer, getTags(call)).record(responseHeadersStart - requestSendFinished, TimeUnit.NANOSECONDS);
//            }
//            if (responseReceiveFinished > 0) {
//                Metrics.timer(responseReceiveTimer, getTags(call, responseFailedException)).record(responseReceiveFinished - responseHeadersStart, TimeUnit.NANOSECONDS);
//            }
//
//            log.info("请求整体用时：{}ms，dns时间：{},创建socket连接时间：{},请求头发生用时：{}ms，请求体发生时间：{},服务端处理时间：{},响应用时：{}ms,连接持有时间：{},安全连接持有时间:{},代理时间：{},缓存命中时间:{}",
//                    (callFinished - callStart) / 1000 / 1000,
//                    (dnsEnd - dnsStart) / 1000 / 1000,
//                    (connectFinished - connectStart) / 1000 / 1000,
//                    (requestHeadersEnd - requestHeadersStart) / 1000 / 1000,
//                    (requestBodyEnd - requestBodyStart)/ 1000 / 1000,
//                    (responseHeadersStart - requestBodyEnd) / 1000 / 1000,
//                    (responseReceiveFinished - responseHeadersStart) / 1000 / 1000,
//                    (connectionReleased - connectionAcquired) / 1000 / 1000,
//                    (secureConnectStart - secureConnectEnd) / 1000 / 1000,
//                    (proxyEnd - proxyStart) / 1000 / 1000,
//                    (cacheMiss -cacheHit) / 1000 / 1000
//            );
////            log.info("请求整体用时：{}ms，到dns时间：{},到创建socket连接时间：{},请求头发生用时：{}ms，请求体发生时间：{},服务端处理时间：{},响应用时：{}ms,连接持有时间：{},安全连接持有时间:{},代理时间：{},缓存命中时间:{},缓存未命中时间：{}",
////                    (callFinished - callStart) / 1000 / 1000,
////                    (dnsStart - callStart) / 1000 / 1000,
////                    (connectStart -callStart) / 1000 / 1000,
////                    (requestHeadersStart-callStart) / 1000 / 1000,
////                    (requestBodyStart-callStart)/ 1000 / 1000,
////                    (responseHeadersStart - callStart) / 1000 / 1000,
////                    (responseReceiveFinished - callStart) / 1000 / 1000,
////                    (connectionAcquired -callStart) / 1000 / 1000,
////                    (secureConnectStart - callStart) / 1000 / 1000,
////                    (proxyStart - callStart) / 1000 / 1000,
////                    (cacheHit - callStart) / 1000 / 1000,
////                    (cacheMiss - callStart) / 1000 / 1000
////            );
//
//        }
//
//
//        private Tags getTags(Call call) {
//            return getTags(call, null);
//        }
//
//        private Tags getTags(Call call, Exception ex) {
//            String remoteServiceName = call.request().url().pathSegments().get(0);
////            String apiClass = MeopenapiInvocationHandler.getCurrentMethod().get().getDeclaringClass().getSimpleName();
////            String apiMethod = MeopenapiInvocationHandler.getCurrentMethod().get().getName();
////            call.request().url().url().getPath();
//            return Tags.concat(this.tags, "remoteService", remoteServiceName, "apiClass", "apiClass", "apiMethod", "apiMethod", "exception", ex == null ? "none" : ex.getClass().getSimpleName());
//        }
//
//        @Override
//        public void callStart(Call call) {
//            callStart = System.nanoTime();
//            requestTotal.increment();
//            requestBusy.increment();
//        }
//
//        @Override
//        public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
//            connectFinished = System.nanoTime();
//            Metrics.timer(connectTimer, getTags(call)).record(connectFinished - connectStart, TimeUnit.NANOSECONDS);
//        }
//
//        @Override
//        public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol, IOException ioe) {
//            connectFinished = System.nanoTime();
//            Metrics.timer(connectTimer, getTags(call, ioe)).record(connectFinished - connectStart, TimeUnit.NANOSECONDS);
//        }
//
//        @Override
//        public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
//            connectStart = System.nanoTime();
//            this.inetSocketAddress = inetSocketAddress;
//        }
//
//        @Override
//        public void connectionAcquired(@NotNull Call call, @NotNull Connection connection) {
//            connectionAcquired = System.nanoTime();
//        }
//
//        @Override
//        public void connectionReleased(@NotNull Call call, @NotNull Connection connection) {
//            connectionReleased = System.nanoTime();
//        }
//
//        @Override
//        public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
//            dnsEnd = System.nanoTime();
//            this.domainName = domainName;
//            if (dnsEnd - dnsStart > dnsSlowThresholdMills * 1000_1000) {
//                log.warn("dns lookup too slow: {} ms", (dnsEnd - dnsStart) / 1000_000);
//            }
//        }
//
//        @Override
//        public void dnsStart(Call call, String domainName) {
//            dnsStart = System.nanoTime();
//        }
//
//
//        @Override
//        public void requestBodyEnd(Call call, long byteCount) {
//            requestSendFinished = System.nanoTime();
//            requestBodyEnd = System.nanoTime();
//        }
//
//        @Override
//        public void requestBodyStart(Call call) {
//            requestBodyStart = System.nanoTime();
////            try {
////                Thread.sleep(2000);
////            } catch (InterruptedException e) {
////                throw new RuntimeException(e);
////            }
//        }
//
//        @Override
//        public void requestFailed(Call call, IOException ioe) {
//            requestSendFinished = System.nanoTime();
//            requestFailedException = ioe;
//        }
//
//        @SneakyThrows
//        @Override
//        public void requestHeadersEnd(Call call, Request request) {
//            requestSendFinished = System.nanoTime();
//            requestHeadersEnd = System.nanoTime();
//        }
//
//        @Override
//        public void requestHeadersStart(Call call) {
//            requestHeadersStart = System.nanoTime();
//        }
//
//        @Override
//        public void responseBodyEnd(Call call, long byteCount) {
//            responseReceiveFinished = System.nanoTime();
//        }
//
//        @Override
//        public void responseBodyStart(Call call) {
//            responseBodyStart = System.nanoTime();
//        }
//
//        @Override
//        public void responseFailed(Call call, IOException ioe) {
//            responseReceiveFinished = System.nanoTime();
//            responseFailedException = ioe;
//        }
//
//        @Override
//        public void responseHeadersEnd(Call call, Response response) {
//            responseReceiveFinished = System.nanoTime();
//        }
//
//        @Override
//        public void responseHeadersStart(Call call) {
//            responseHeadersStart = System.nanoTime();
//        }
//
//
//        @Override
//        public void secureConnectEnd(@NotNull Call call, @Nullable Handshake handshake) {
//            secureConnectEnd = System.nanoTime();
//        }
//
//        @Override
//        public void secureConnectStart(@NotNull Call call) {
//            secureConnectStart = System.nanoTime();
//        }
//
//        @Override
//        public void proxySelectEnd(@org.jetbrains.annotations.NotNull Call call, @org.jetbrains.annotations.NotNull HttpUrl url, @org.jetbrains.annotations.NotNull List<Proxy> proxies) {
//            proxyEnd = System.nanoTime();
//        }
//
//        @Override
//        public void proxySelectStart(@org.jetbrains.annotations.NotNull Call call, @org.jetbrains.annotations.NotNull HttpUrl url) {
//            proxyStart = System.nanoTime();
//        }
//
//        @Override
//        public void cacheConditionalHit(@org.jetbrains.annotations.NotNull Call call, @org.jetbrains.annotations.NotNull Response cachedResponse) {
//            super.cacheConditionalHit(call, cachedResponse);
//        }
//
//        @Override
//        public void cacheHit(@org.jetbrains.annotations.NotNull Call call, @org.jetbrains.annotations.NotNull Response response) {
//            cacheHit =System.nanoTime();
//        }
//
//        @Override
//        public void cacheMiss(@org.jetbrains.annotations.NotNull Call call) {
//            cacheMiss =System.nanoTime();
//        }
//
//        @Override
//        public void canceled(@org.jetbrains.annotations.NotNull Call call) {
//            canceled =System.nanoTime();
//        }
//
//        @Override
//        public void satisfactionFailure(@org.jetbrains.annotations.NotNull Call call, @org.jetbrains.annotations.NotNull Response response) {
//            super.satisfactionFailure(call, response);
//        }
//    }
//
//}
