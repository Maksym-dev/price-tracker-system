local key = KEYS[1]
local now = tonumber(ARGV[1])
local windowStart = tonumber(ARGV[2])
local limit = tonumber(ARGV[3])
local timeout = tonumber(ARGV[4])

-- 1. Clear old marks
redis.call('ZREMRANGEBYSCORE', key, 0, windowStart)

-- 2. Check the current count
local currentCount = redis.call('ZCARD', key)

if currentCount < limit then
    -- 3. Add a new mark
    redis.call('ZADD', key, now, now)

    -- 4. Set the TTL only if it hasn't already been set
    if redis.call('TTL', key) < 0 then
        redis.call('EXPIRE', key, timeout)
    end
    return 1
else
    return 0
end