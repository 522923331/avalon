//package iplay.cool.utils;
//
///**
// * @author wu.dang
// * @since 2023/6/21
// */
//
//import com.alibaba.fastjson.JSON;
//import com.chain.notify.dto.CallBackResult;
//import com.chain.notify.dto.NotifyReqDTO;
//import com.google.common.collect.Lists;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.methods.HttpUriRequest;
//import org.apache.http.concurrent.FutureCallback;
//import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
//import org.apache.http.impl.nio.client.HttpAsyncClients;
//import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
//import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
//import org.apache.http.impl.nio.reactor.IOReactorConfig;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.nio.reactor.ConnectingIOReactor;
//import org.apache.http.nio.reactor.IOReactorException;
//import org.apache.http.util.EntityUtils;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 该工具类在高并发场景下，不能正常使用！！！
// * 异步Http请求封装工具类  这个我也在压测的时候用到了，配置信息如下，setMaxTotal 和setDefaultMaxPerRoute我都设置了，但是高并发异步发送的场景下，计时一直在延长。后来改用okhttp了。在这一块两者配置相差不大，但是不知道为什么这个工具类不好使。
// * 几个重要的参数
// * ConnectTimeout : 连接超时,连接建立时间,三次握手完成时间。
// * SocketTimeout : 请求超时,数据传输过程中数据包之间间隔的最大时间。
// * ConnectionRequestTimeout : 使用连接池来管理连接,从连接池获取连接的超时时间。
// * 如果按照远超下游处理能力的速度发起请求，httpasyncclient的request队列就会堆积，等到event loop线程终于消费到队列末尾的request时，这个request已经排队很久了，会直接被标记为超时并callback告知失败
// * 所以如果网络调用吞吐量很高的话，要么自己控制好请求发送速率，要么适当调大setConnectionRequestTimeout来允许request被处理之前排队更久
// * ConnTotal:连接池中最大连接数;
// * ConnPerRoute(1000):分配给同一个route(路由)最大的并发连接数,route为运行环境机器到目标机器的一条线路
// * @author wu.dang
// * @date 2018/3/24
// */
//@Slf4j
//@Component
//public class AsyncHttpClientUtil {
//    private static CloseableHttpAsyncClient httpClient;
//
//    /**
//     * 创建CloseableHttpAsyncClient
//     *
//     * @return
//     */
//    private static CloseableHttpAsyncClient createCustomAsyncClient() {
//        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
//                .setIoThreadCount(Runtime.getRuntime().availableProcessors())
//                .setConnectTimeout(60000)
//                .setSoTimeout(60000)
//                .setSoKeepAlive(true)
//                .build();
//        // 设置超时时间 毫秒为单位
//        RequestConfig requestConfig = RequestConfig
//                .copy(RequestConfig.DEFAULT)
//                .setConnectTimeout(60000)
//                .setSocketTimeout(60000)
//                .build();
//        return HttpAsyncClients
//                .custom()
//                .setDefaultIOReactorConfig(ioReactorConfig)
//                .setDefaultRequestConfig(requestConfig)
//                .build();
//
//    }
//    public static CloseableHttpAsyncClient getHttpAsyncClient() {
//        RequestConfig requestConfig = RequestConfig.custom()
//                .setConnectTimeout(10000) // 请求超时,数据传输过程中数据包之间间隔的最大时间。
//                .setSocketTimeout(10000) // 连接超时,连接建立时间,三次握手完成时间。
//                .setConnectionRequestTimeout(50000) // 使用连接池来管理连接,从连接池获取连接的超时时间。
//                .build();
//        //配置io线程
//        IOReactorConfig ioReactorConfig = IOReactorConfig.custom().
////                setIoThreadCount(1)
//                setIoThreadCount(Runtime.getRuntime().availableProcessors()*2)
//                .setSoKeepAlive(true)
//                .build();
//        //设置连接池大小
//        ConnectingIOReactor ioReactor=null;
//        try {
//            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
//        } catch (IOReactorException e) {
//            e.printStackTrace();
//        }
//        PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);
//        // 连接池中最大连接数
//        connManager.setMaxTotal(1000);
////        connManager.setMaxTotal(10);
//        // 分配给同一个route(路由)最大的并发连接数,route为运行环境机器到目标机器的一条线路,举例来说,我们使用HttpClient的实现来分别请求
//        // www.baidu.com 的资源和 www.bing.com 的资源那么他就会产生两个route;
//        connManager.setDefaultMaxPerRoute(1000);
//        return HttpAsyncClients.custom().setConnectionManager(connManager)
//                .setDefaultRequestConfig(requestConfig)
//                .build();
//    }
//
//    @PostConstruct
//    public static void startHttpClient() {
//        httpClient = getHttpAsyncClient();
//        httpClient.start();
//    }
//
//    @PreDestroy
//    public static void closeHttpClient() {
//        try {
//            httpClient.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void get(String url, StringFutureCallback callback) {
//        HttpUriRequest request = new HttpGet(url);
//        httpClient.execute(request, new DefaultFutureCallback(callback));
//    }
//
//    public void post(NotifyReqDTO notifyReqDTO, long startTime) {
//        String url = notifyReqDTO.getUrl();
//        HttpPost httpPost = new HttpPost(url);
//        List<BasicNameValuePair> pairs = Lists.newArrayList();
//        Map<String, String> params = notifyReqDTO.getParams();
//        params.forEach((k, v) -> pairs.add(new BasicNameValuePair(k, v)));
//        UrlEncodedFormEntity entity = null;
//        try {
//            entity = new UrlEncodedFormEntity(pairs, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
//        httpPost.setEntity(entity);
//        httpClient.execute(httpPost, new FutureCallback<HttpResponse>() {
//            @Override
//            public void completed(HttpResponse httpResponse) {
//                HttpEntity entity = httpResponse.getEntity();
//                String content = "";
//                try {
//                    content = EntityUtils.toString(entity, "UTF-8");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                log.info("reqNo111:{}, onResponse:{}, httpReqCostTime: {}", notifyReqDTO.getReqNo(), content, (System.currentTimeMillis() - startTime));
//                try {
//                    CallBackResult callBackResult = JSON.parseObject(content, CallBackResult.class);
//                    if (!"200".equals(callBackResult.getCode()) || !"success".equals(callBackResult.getMsg())) {
//                        log.info("reqNo:{}, errorMsg:{},˚ 接受响应值不为200、success，重新推送结果", notifyReqDTO.getReqNo(), content);
//                    }
//                } catch (Exception e) {
//                    log.error("reqNo:{} onResponse error:{}, e:", notifyReqDTO.getReqNo(), content, e);
//                }
//
//            }
//
//            @Override
//            public void failed(Exception e) {
//
//            }
//
//            @Override
//            public void cancelled() {
//
//            }
//        });
//    }
//
//
//    public static void post(String url, Object parameter, StringFutureCallback callback) {
//        HttpPost httpPost = new HttpPost(url);
//        if (parameter != null) {
//            List<BasicNameValuePair> pairs = Lists.newArrayList();
//            UrlEncodedFormEntity entity = null;
//            try {
//                if (parameter instanceof HashMap) {
//                    Map<String, String> parameters = (Map<String, String>) parameter;
//                    parameters.forEach((k, v) -> pairs.add(new BasicNameValuePair(k, v)));
//                    entity = new UrlEncodedFormEntity(pairs, "UTF-8");
//                }
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            httpPost.setEntity(entity);
//        }
//        httpClient.execute(httpPost, new DefaultFutureCallback(callback));
//    }
//
//
//    /**
//     * 字符串类型结果回调
//     */
//    public interface StringFutureCallback {
//        void success(String content);
//    }
//
//
//    public static class DefaultFutureCallback implements FutureCallback<HttpResponse> {
//        private final StringFutureCallback callback;
//
//        public DefaultFutureCallback(StringFutureCallback callback) {
//            this.callback = callback;
//        }
//
//        @Override
//        public void completed(HttpResponse httpResponse) {
//            HttpEntity entity = httpResponse.getEntity();
//            String content = "";
//            try {
//                content = EntityUtils.toString(entity, "UTF-8");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            callback.success(content);
//        }
//
//        @Override
//        public void failed(Exception e) {
//            e.printStackTrace();
//        }
//
//        @Override
//        public void cancelled() {
//            log.debug("http request cancelled");
//        }
//    }
//}
