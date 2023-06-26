//package iplay.cool.limiter;
//
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.echain.entity.MerchantLimiter;
//import com.echain.enums.CommonUsedEnum;
//import com.echain.service.IMerchantLimiterService;
//import com.google.common.util.concurrent.RateLimiter;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
///**
// * guava 的单机限流器
// * @author wu.dang
// * @since 2023/5/10
// */
//@Component
//public class MerchantProducerRateLimiter {
//
//    @Value("${limiter.producerMaxPermits}")
//    private int maxPermits;
//    @Value("${limiter.producerInterval}")
//    private long interval;
//
//    @Resource
//    private IMerchantLimiterService merchantLimiterService;
//
//    private static RateLimiter defaultRateLimiter;
//
//    private static Map<String,RateLimiter> limiterMap;
//
//
//    public boolean tryAcquire(String merchantNo) {
//        RateLimiter rateLimiter = limiterMap.get(merchantNo);
//        if (rateLimiter == null) {
//            return defaultRateLimiter.tryAcquire(interval, TimeUnit.MILLISECONDS);
//        }
//        return rateLimiter.tryAcquire(interval, TimeUnit.MILLISECONDS);
//    }
//
//    public void acquire(String merchantNo) {
//        RateLimiter rateLimiter = limiterMap.get(merchantNo);
//        if (rateLimiter != null) {
//            rateLimiter.acquire();
//        }
//    }
//
//    @PostConstruct
//    private void initMerchantRateLimiter() {
//        limiterMap = new HashMap<>();
//        defaultRateLimiter = RateLimiter.create(maxPermits);
//        refreshCache();
//    }
//
//
//    public void refreshCache(){
//        LambdaQueryWrapper<MerchantLimiter> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(MerchantLimiter::getUsed, CommonUsedEnum.OPEN.getCode());
//        List<MerchantLimiter> merchantLimiters = merchantLimiterService.list(wrapper);
//        merchantLimiters.forEach(e -> {
//            RateLimiter rateLimiter = RateLimiter.create(e.getRateLimit());
//            limiterMap.put(e.getMerchantNo(),rateLimiter);
//        });
//    }
//
//}
//
//
//
