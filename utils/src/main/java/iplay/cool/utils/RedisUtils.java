package iplay.cool.utils;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author dove
 */
@Slf4j
@Component
public class RedisUtils {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    private static final Gson gson = new Gson();

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("redis设置过期时间异常：", e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("判断key是否存在error，key:{}", key, e);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 键
     */
    public void del(String key) {
        if (key != null) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 返回指定类型
     */
    public <T> T get(String key, Class<T> clazz) {
        Object valueObj = redisTemplate.opsForValue().get(key);
        if (clazz.isInstance(valueObj)) {
            return (T) valueObj;
        } else if (clazz == Long.class && valueObj instanceof Integer) {
            Integer obj = (Integer) valueObj;
            return (T) Long.valueOf(obj.longValue());
        }
        return null;
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("redis set方法执行异常，key={},value={}", key, value, e);
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("redis set方法执行异常，key={},value={},time={}", key, value, time, e);
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @param unit  时间单位
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time, TimeUnit unit) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, unit);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("redis set方法执行异常，key={},value={},time={}", key, value, time, e);
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return 值
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<?, ?> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<?, ?> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("redis hmset方法执行异常，key={},value={}", key, map, e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<?, ?> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("redis hmset方法执行异常，key={},value={},time={}", key, map, time, e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error("redis hset方法执行异常，key={}, item={}, value={},time={}", key, item, value, e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("redis hset方法执行异常，key={}, item={}, value={},time={}", key, item, value, e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("redis sGet方法执行异常，key={}", key, e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error("redis sHasKey方法执行异常，key={}, value={}", key, value, e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("redis sSet方法执行异常，key={}, values={}", key, values, e);
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            log.error("redis sSet方法执行异常，key={}, values={}, time={}", key, values, time, e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return 值
     */
    @SuppressWarnings("ConstantConditions")
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T lGetIndex(String key, long index, Class<T> clazz) {
        try {
            Object o = redisTemplate.opsForList().index(key, index);
            if (Objects.nonNull(o)) {
                return gson.fromJson(gson.toJson(o), clazz);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     */
    public long lRemove(String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * zset 添加一个元素
     * 如果value存在，则更新score
     *
     * @param key   键
     * @param value 值
     * @param score 分数
     */
    public void zadd(String key, Object value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 从 zset 中移除一个元素
     *
     * @param key    键
     * @param values 值
     * @return 删除的成员数，不包括不存在的成员
     */
    public long zrem(String key, Object... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

    /**
     * 从排序集中获取起始和结束之间的元素。
     * 例如 start=0,end=1 则会返回第 0 和 1 两个元素
     *
     * @param key   键
     * @param start 起始序号 0表示第一个
     * @param end   结束序号
     * @return
     */
    public Set<Object> zrange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 从排序集中获取起始和结束之间的元素并携带分数。
     * 例如 start=0,end=1 则会返回第 0 和 1 两个元素
     *
     * @param key   键
     * @param start 起始序号 0表示第一个
     * @param end   结束序号
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> zrangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    /**
     * 从排序集中按照score倒序获取起始和结束之间的元素。
     *
     * @param key   键
     * @param start 起始序号 0表示第一个
     * @param end   结束序号
     * @return
     */
    public Set<Object> zrevrange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * list弹出数据
     *
     * @param key
     * @param clazz
     * @return T
     * @author Alan
     * @date 2022/5/18 7:02 PM
     **/
    public <T> T leftPopList(String key, Class<T> clazz) {
        Object o = redisTemplate.opsForList().leftPop(key);
        if (Objects.nonNull(o)) {
            return gson.fromJson(gson.toJson(o), clazz);
        }
        return null;
    }

    public Object leftPopList(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 批量删除列表数据
     * @param key key
     * @param start 保留的数据的起始索引
     * @param end 保留的数据的终点索引
     * @return
     */
    public boolean batchPopList(String key,int start,int end){
        try {
            redisTemplate.opsForList().trim(key,start,end);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 往队列中添加元素
     *
     * @param key
     * @param value
     * @return
     */
    public Long leftPushList(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }


    /**
     * set 添加一个元素
     *
     * @param key   键
     * @param value 值
     */
    public void sadd(String key, Object... value) {
        redisTemplate.opsForSet().add(key, value);
    }

    /**
     * 从 set 中移除元素
     *
     * @param key    键
     * @param values 值
     * @return 删除的成员数，不包括不存在的成员
     */
    public long sremove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 返回set的所有元素。
     *
     * @param key 键
     * @return
     */
    public Set<Object> members(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 如果为空就set值，并返回1；如果存在(不为空)不进行操作，并返回0
     *
     * @param key      键
     * @param value    值
     * @param time     缓存时间
     * @param timeUnit 缓存单位
     * @return * @return: boolean
     */
    public boolean setIfAbsent(String key, Object value, Long time, TimeUnit timeUnit) {
//        if (time > 0) {
//            redisTemplate.setEnableTransactionSupport(true);
//            redisTemplate.multi();
//            redisTemplate.opsForValue().setIfAbsent(key, value);
//            redisTemplate.expire(key,time,timeUnit);
//            List<Object> exec = redisTemplate.exec();
//            return exec.get(0).equals(true);
//        }
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(key, value);
        redisTemplate.expire(key,time,timeUnit);
        return Boolean.TRUE.equals(aBoolean);
    }

    /**
     * 如果为空就set值，并返回1；如果存在(不为空)不进行操作，并返回0
     *
     * @param key   键
     * @param value 值
     * @param time  缓存时间，单位秒
     * @return * @return: boolean
     */
    public boolean setIfAbsent(String key, Object value, Long time) {
        return setIfAbsent(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 右侧批量入列
     *
     * @param key
     * @param list
     * @author Alan
     * @date 2022/8/11 4:53 PM
     **/
    public <T> void rightPushAll(String key, List<T> list) {
        redisTemplate.opsForList().rightPushAll(key, list.toArray());
    }

    /**
     * 看集合的size
     *
     * @param key
     * @return Long
     * @author Alan
     * @date 2022/8/11 4:55 PM
     **/
    public Long listSize(String key) {
        Long size = redisTemplate.opsForList().size(key);
        if (Objects.isNull(size)) {
            return 0L;
        }
        return size;
    }

    public <T> Long rightPush(String key, T t) {
        return redisTemplate.opsForList().rightPush(key, t);
    }

    public Object rightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }


}