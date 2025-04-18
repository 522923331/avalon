//package iplay.cool.limiter;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.echain.common.exception.BizException;
//import com.echain.entity.MerchantLimiter;
//import com.echain.enums.BizCodeEnum;
//import com.echain.enums.CommonUsedEnum;
//import com.echain.enums.RedisCacaheEnum;
//import com.echain.service.IMerchantLimiterService;
//import com.echain.utils.RedisUtils;
//import com.google.common.base.Preconditions;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.core.script.DefaultRedisScript;
//import org.springframework.scripting.support.ResourceScriptSource;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author wu.dang
// * @since 2023/5/13
// */
//@Slf4j
//@Component
//public class RedisLimiter {
//    @Resource
//    private RedisUtils redisUtils;
//    @Resource
//    private IMerchantLimiterService merchantLimiterService;
//    @Resource
//    StringRedisTemplate stringRedisTemplate;
//    @Value("${limiter.producerMaxPermits}")
//    private Long producerMaxPermits;
//    @Value("${limiter.producerInterval}")
//    private Long producerInterval;
//
//    private DefaultRedisScript<Long> getRedisScript;
//
//    @PostConstruct
//    private void init() {
//        getRedisScript = new DefaultRedisScript<>();
//        getRedisScript.setResultType(Long.class);
//        getRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/rateLimiterTokenBucket.lua")));
//        log.info("RateLimiter[分布式限流处理器]脚本加载完成");
//    }
//
//    /**
//     * @param limitKey 限流的key
//     * @param maxCount 时间窗口内可接受的最大请求次数
//     * @param winWidth 时间窗口宽度
//     */
//    private void limit(String limitKey, Long maxCount, Long winWidth) {
//        Preconditions.checkNotNull(limitKey);
//        List<String> keyList = new ArrayList<>();
//        keyList.add(limitKey);
////        log.info("keyList={}, maxCount={}, winWidth={}", keyList, maxCount, winWidth);
//        Long result = stringRedisTemplate.execute(getRedisScript, keyList, maxCount.toString(), winWidth.toString());
//        if (result == null || result == 0) {
//            log.error("由于超过窗口宽度={},允许{}的请求次数={}[触发限流]", winWidth, limitKey, maxCount);
//            throw new BizException(BizCodeEnum.RATE_LIMIT_EXCEEDED);
//        }
//    }
//
//    /**
//     * @param limitKey 限流的key
//     * @param maxPermits 最大存储的令牌数
//     * @param permitsPerSecond 每秒钟产生的令牌数
//     * @param requiredPermits 请求的令牌数
//     */
//    private void limit(String limitKey, Long maxPermits, Long permitsPerSecond,int requiredPermits) {
//        Preconditions.checkNotNull(limitKey);
//        List<String> keyList = new ArrayList<>();
//        keyList.add(limitKey);
//        keyList.add(String.valueOf(maxPermits));
//        keyList.add(String.valueOf(permitsPerSecond));
//        Long result = stringRedisTemplate.execute(getRedisScript, keyList, String.valueOf(requiredPermits));
//        if (result != null && result < 0) {
//            log.error("限流key={},最大存储的令牌数={},允许每秒钟产生的令牌数={},超过令牌上限[触发限流]",limitKey, maxPermits, permitsPerSecond);
//            throw new BizException(BizCodeEnum.RATE_LIMIT_EXCEEDED);
//        }
//    }
//
//    public void tryAcquire(String merchantNo, String cacheLimiterKey) {
//        Map<String, Integer> map = redisUtils.get(cacheLimiterKey, Map.class);
//        if (map != null) {
//            Integer rate = map.get(merchantNo);
//            if (rate != null) {
//                limit(merchantNo, cacheLimiterKey, (long) rate);
//            } else {
//                defaultLimit(cacheLimiterKey);
//            }
//        } else {
//            defaultLimit(cacheLimiterKey);
//        }
//    }
//
//    /**
//     * 对外限流接口
//     *
//     * @param merchantNo
//     * @param maxCount
//     */
//    private void limit(String merchantNo, String cacheLimiterKey, Long maxCount) {
//        String cacheKey = cacheLimiterKey + ":" + merchantNo;
////        limit(cacheKey, maxCount, producerInterval);
//        limit(cacheKey,maxCount,maxCount,1);
//    }
//
//    private void defaultLimit(String key) {
//        String cacheKey = key+":default";
////        limit(cacheKey, producerMaxPermits, producerInterval);
//        limit(cacheKey,producerMaxPermits,producerMaxPermits,1);
//    }
//
//    private String buildRedisKey(String merchantNo) {
//        return String.format(RedisCacaheEnum.LIMITER_KEY_PREFIX.getCacheKey(), merchantNo);
//    }
//
//
//    @PostConstruct
//    public void initMerchantLimiter() {
//        LambdaQueryWrapper<MerchantLimiter> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(MerchantLimiter::getUsed, CommonUsedEnum.OPEN.getCode());
//        List<MerchantLimiter> merchantLimiters = merchantLimiterService.list(wrapper);
//        Map<String, Integer> txLimitMap = new HashMap<>();
//        Map<String, Integer> queryLimitMap = new HashMap<>();
//        merchantLimiters.forEach(e -> {
//            txLimitMap.put(e.getMerchantNo(), e.getTxLimit());
//            queryLimitMap.put(e.getMerchantNo(), e.getQueryLimit());
//        });
//
//        redisUtils.set(RedisCacaheEnum.MERCHANT_LIMITER_TX_CACHE.getCacheKey(), txLimitMap, RedisCacaheEnum.MERCHANT_LIMITER_TX_CACHE.getExpireTime());
//        redisUtils.set(RedisCacaheEnum.MERCHANT_LIMITER_QUERY_CACHE.getCacheKey(), queryLimitMap, RedisCacaheEnum.MERCHANT_LIMITER_QUERY_CACHE.getExpireTime());
//    }
//}
