-- KEYS[1]: 限流的键（通常为用户ID或者API）
-- ARGV[1]: 最大允许请求数
-- ARGV[2]: 窗口时间（以秒为单位）

local current = redis.call('GET', KEYS[1])

if current and tonumber(current) >= tonumber(ARGV[1]) then
    return 0  -- 返回0表示超出限流
else
    current = redis.call('INCR', KEYS[1])
    if tonumber(current) == 1 then
        redis.call('EXPIRE', KEYS[1], ARGV[2])  -- 设置窗口时间
    end
    return 1  -- 返回1表示未超限
end





