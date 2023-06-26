//package iplay.cool.utils;
//
//import com.alibaba.fastjson.JSON;
//import com.chain.notify.constant.NotifyConstants;
//import com.chain.notify.dto.CallBackResult;
//import com.chain.notify.dto.NotifyReqDTO;
//import lombok.extern.slf4j.Slf4j;
//import okhttp3.*;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.SynchronousQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
///**
// * 该工具类在高并发场景下，能正常使用！！！
// * 经过压测，高并发场景下，dispatcher 对应的setMaxRequests和setMaxRequestsPerHost的设置非常重要。
// * <dependency>
// *             <groupId>com.squareup.okhttp3</groupId>
// *             <artifactId>okhttp</artifactId>
// *             <version>4.9.3</version>
// *         </dependency>
// *         不使用kotlin就报错
// *         <dependency>
// *             <groupId>org.jetbrains.kotlin</groupId>
// *             <artifactId>kotlin-stdlib</artifactId>
// *             <version>1.3.70</version>
// *         </dependency>
// */
//@Slf4j
//@Component
//public class OkHttpUtils {
//
//    private static OkHttpClient mOkHttpClient = new OkHttpClient();
//
//    @Resource
//    private RocketMqUtils rocketMqHandler;
//
//    @Value("${rocketmq.producers.notify.topic}")
//    private String notifyTopic;
//
//    @Value("${rocketmq.producers.notify.tag}")
//    private String notifyTag;
//
//    @Value("${rocketmq.retryCount}")
//    private Integer mqRetryCount;
//
//    static {
//
//        int corePoolSize = Runtime.getRuntime().availableProcessors(); // 核心线程数
//        int maxPoolSize = corePoolSize * 2; // 最大线程数
//        long keepAliveTime = 60; // 线程空闲时间（秒）
//
//        ExecutorService executorService = new ThreadPoolExecutor(
//                corePoolSize,
//                maxPoolSize,
//                keepAliveTime,
//                TimeUnit.SECONDS,
//                new SynchronousQueue<>(),
//                new ThreadPoolExecutor.CallerRunsPolicy()
//        );
//        //经过压测，高并发场景下，dispatcher 对应的setMaxRequests和setMaxRequestsPerHost的设置非常重要。
//        //由于内部有线程池，自定义线程池的作用并不是很大，除非有异常抛出的情况，可以自定义异常处理
//        Dispatcher dispatcher = new Dispatcher(executorService);
//        dispatcher.setMaxRequests(100); // 最大并发请求数
//        dispatcher.setMaxRequestsPerHost(100); // 单个主机最大并发请求数
//
//        ConnectionPool connectionPool = new ConnectionPool(100, 5, TimeUnit.MINUTES);
//
//        mOkHttpClient = new OkHttpClient.Builder()
//                .connectionPool(connectionPool)
//                .dispatcher(dispatcher)
//                .protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1))
//                .connectTimeout(10, TimeUnit.SECONDS) // 连接超时时间
//                .readTimeout(5, TimeUnit.SECONDS) // 读超时时间
//                .writeTimeout(5, TimeUnit.SECONDS) // 写超时时间
////                .eventListenerFactory(new MetricEventListenerFactory(Tags.of("clientApp", "appName", "appId", "securityAppId")))
//                .build();
//    }
//
//    public void postSync(NotifyReqDTO notifyReqDTO, long startTime) {
//        String content = notifyReqDTO.getParams().get(NotifyConstants.MERCHANT_NOTIFY_KEY_CODE);
//        RequestBody formBody = FormBody.create(MediaType.parse("application/json;charset=utf-8"), content);
//        Request request = new Request.Builder()
//                .url(notifyReqDTO.getUrl())
//                .post(formBody)
//                .build();
//        Call call = mOkHttpClient.newCall(request);
//        try {
//            Response response = call.execute();
//            if (response.isSuccessful()) {
//                String result = response.body().string();
//                log.info("reqNo:{}, onResponse:{}, httpReqCostTime: {}", notifyReqDTO.getReqNo(), result, (System.currentTimeMillis() - startTime));
//                try {
//                    CallBackResult callBackResult = JSON.parseObject(result, CallBackResult.class);
//                    if (!callBackResult.getCode().equals("200") || !callBackResult.getMsg().equals("success")) {
//                        log.info("reqNo:{}, errorMsg:{},˚ 接受响应值不为200、success，重新推送结果", notifyReqDTO.getReqNo(), result);
//                        retry(notifyReqDTO);
//                    }
//                } catch (Exception e) {
//                    log.error("reqNo:{} onResponse error:{}, e:{}", notifyReqDTO.getReqNo(), result, e);
//                    retry(notifyReqDTO);
//                }
//            } else {
//                log.info("reqNo:{}, 未知服务，请检查相关服务", notifyReqDTO.getReqNo());
//            }
//            response.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void postAsyncHttp(NotifyReqDTO notifyReqDTO, long startTime) {
//        String content = notifyReqDTO.getParams().get(NotifyConstants.MERCHANT_NOTIFY_KEY_CODE);
//        RequestBody formBody = FormBody.create(MediaType.parse("application/json;charset=utf-8"), content);
//        Request request = new Request.Builder()
//                .url(notifyReqDTO.getUrl())
//                .post(formBody)
//                .build();
//        Call call = mOkHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String result = response.body().string();
//                    log.info("reqNo:{}, onResponse:{}, httpReqCostTime: {}", notifyReqDTO.getReqNo(), result, (System.currentTimeMillis() - startTime));
//                    try {
//                        CallBackResult callBackResult = JSON.parseObject(result, CallBackResult.class);
//                        if (!callBackResult.getCode().equals("200") || !callBackResult.getMsg().equals("success")) {
//                            log.info("reqNo:{}, errorMsg:{},˚ 接受响应值不为200、success，重新推送结果", notifyReqDTO.getReqNo(), result);
//                            retry(notifyReqDTO);
//                        }
//                    } catch (Exception e) {
//                        log.error("reqNo:{} onResponse error:{}, e:", notifyReqDTO.getReqNo(), result, e);
//                        retry(notifyReqDTO);
//                    }
//                } else {
//                    log.info("reqNo:{}, 未知服务，请检查相关服务", notifyReqDTO.getReqNo());
//                }
//            }
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                log.info("reqNo:{}, onFailure:", notifyReqDTO.getReqNo(), e);
//                retry(notifyReqDTO);
//            }
//        });
//    }
//
//    private void retry(NotifyReqDTO notifyReqDTO) {
//        Integer retryCount = notifyReqDTO.getRetryTimes();
//        if (retryCount < mqRetryCount) {
//            log.info("reqNO:{}, 重试次数:{}", notifyReqDTO.getReqNo(), notifyReqDTO.getRetryTimes() + 1);
//            notifyReqDTO.setRetryTimes(++retryCount);
//            rocketMqHandler.delayedSend(notifyTopic + ":" + notifyTag, notifyReqDTO, 1000, 3);
//        }
//    }
//}
