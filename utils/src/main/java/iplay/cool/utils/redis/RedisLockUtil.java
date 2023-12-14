package iplay.cool.utils.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author wu.dang
 * @since 2022/10/21
 */
@Slf4j
public class RedisLockUtil {


    private volatile static StringRedisTemplate stringRedisTemplate;

    /**
     * 锁前缀
     */
    private static final String LOCK_KEY_PREFIX = "Lock:%s";

    private static final Long RELEASE_SUCCESS = 1L;

    private static final String COMMON_LOCK_PRE = "common:lock:pre:%s";

    private static final long LOCK_OUT_TIME = 60L;

    private static final int LOCK_RETRY_TIMES = 5;

    public static StringRedisTemplate getRedisTemplate() {
        if (Objects.isNull(stringRedisTemplate)) {
            synchronized (RedisLockUtil.class) {
                if (Objects.isNull(stringRedisTemplate)) {
                    stringRedisTemplate = SpringContextUtil.getBean("stringRedisTemplate");
                }
            }
        }
        return stringRedisTemplate;
    }


    /**
     * 加锁 自定义有效时间
     *
     * @param key    加锁的key
     * @param reqId  标识值，一般为uuid或用户客户端标识，本意：谁加锁谁解锁
     * @param second 锁超时释放时间
     **/
    public static boolean lock(String key, String reqId, Long second) {
        String lockKey = String.format(LOCK_KEY_PREFIX, key);
        log.info("start lock,lockKey:{},reqId:{}", lockKey, reqId);
        boolean result = getRedisTemplate().opsForValue().setIfAbsent(lockKey, reqId, second, TimeUnit.SECONDS);
        log.info("lock result,key:{},reqId:{},result:{}", lockKey, reqId, result);
        return result;
    }

    /**
     * 重试锁
     *
     * @param key
     * @param reqId
     * @param retryTimes
     * @return
     * @throws InterruptedException
     */
    public static boolean lockRetry(String key, String reqId, int retryTimes) throws InterruptedException {
        for (int i = 0; i < retryTimes; i++) {
            if (lock(key, reqId, LOCK_OUT_TIME)) {
                return true;
            }

            Thread.sleep(50L);
        }
        return lock(key, reqId, LOCK_OUT_TIME);
    }

    /**
     * 解锁
     *
     * @param key
     * @param reqId
     **/
    public static boolean unLock(String key, String reqId) {
        String lockKey = String.format(LOCK_KEY_PREFIX, key);
        log.info("start unlock，lockKey：{},reqId:{}", lockKey, reqId);
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

        DefaultRedisScript<Long> unLockScript = new DefaultRedisScript<>();
        unLockScript.setResultType(Long.class);
        unLockScript.setScriptText(script);
        List<String> keyList = new ArrayList<>();
        keyList.add(lockKey);

        Long result = (Long) getRedisTemplate().execute(unLockScript, keyList, reqId);
        log.info("unlock result，key：{},reqId:{}，result：{}", lockKey, reqId, result);
        return RELEASE_SUCCESS.equals(result);
    }


    /**
     * 获取锁,默认60秒
     *
     * @param lockKey
     * @param func
     * @return
     **/
    public static <T> T operateLock(String lockKey, Supplier<T> func) {
        String redisLockKey = String.format(COMMON_LOCK_PRE, lockKey);
        String uuid = UUID.randomUUID().toString();
        try {
            if (RedisLockUtil.lock(redisLockKey, uuid, LOCK_OUT_TIME)) {
                return func.get();
            } else {
                throw new RuntimeException("Please try again later");
            }
        } finally {
            RedisLockUtil.unLock(redisLockKey, uuid);
        }
    }

    /**
     * 获取锁,默认60秒
     *
     * @param lockKey
     **/
    public static void operateLock(String lockKey, Runnable runnable) {
        String redisLockKey = String.format(COMMON_LOCK_PRE, lockKey);
        String uuid = UUID.randomUUID().toString();
        try {
            if (RedisLockUtil.lock(redisLockKey, uuid, LOCK_OUT_TIME)) {
                runnable.run();
            } else {
                throw new RuntimeException("Please try again later");
            }
        } finally {
            RedisLockUtil.unLock(redisLockKey, uuid);
        }
    }


    /**
     * 重试锁默认5次
     *
     **/
    public static void operateRetryLock(String lockKey, Runnable runnable) {
        operateRetryLock(lockKey,runnable,LOCK_RETRY_TIMES);
    }

    public static void operateRetryLock(String lockKey, Runnable runnable,int retryTimes) {
        String redisLockKey = String.format(COMMON_LOCK_PRE, lockKey);
        String uuid = UUID.randomUUID().toString();
        try {
            if (RedisLockUtil.lockRetry(redisLockKey, uuid, retryTimes)) {
                runnable.run();
            } else {
                throw new RuntimeException("Please try again later");
            }
        } catch (InterruptedException e) {
            log.error("lock fail");
            throw new RuntimeException("Please try again later");
        } finally {
            RedisLockUtil.unLock(redisLockKey, uuid);
        }
    }
}
