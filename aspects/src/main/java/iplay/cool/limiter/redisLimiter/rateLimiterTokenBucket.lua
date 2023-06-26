-- key
local key = KEYS[1]
-- 最大存储的令牌数
local max_permits = tonumber(KEYS[2])
-- 每秒钟产生的令牌数
local permits_per_second = tonumber(KEYS[3])
-- 请求的令牌数
local required_permits = tonumber(ARGV[1])

-- 上次令牌生成时间
local last_gen_ticket_micros = tonumber(redis.call('hget', key, 'last_gen_ticket_micros') or 0)

-- 当前时间
local time = redis.call('time')
local now_micros = tonumber(time[1]) * 1000000 + tonumber(time[2])

-- 当前存储的令牌数
local stored_permits = tonumber(redis.call('hget', key, 'stored_permits') or 0)
-- 添加令牌的时间间隔
--local stable_interval_micros = 1000000 / permits_per_second

-- 补充令牌 1 1000   x * （p/t）
local new_permits = (now_micros - last_gen_ticket_micros) * permits_per_second / 1000000
stored_permits = math.min(max_permits, stored_permits + new_permits)

-- 消耗令牌
if stored_permits < required_permits then
    return -1
end

redis.replicate_commands()
local remain = stored_permits - required_permits
redis.call('hset', key, 'stored_permits', remain)
if new_permits > 0 then
    redis.call('hset', key, 'last_gen_ticket_micros', now_micros)
end
redis.call('expire', key, 1000)

return remain