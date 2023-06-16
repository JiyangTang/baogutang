local limit = tonumber(ARGV[1])
local current = tonumber(redis.call('get', KEYS[1]) or '0')
if current + 1 > limit
then
    return false
else
    redis.call('INCRBY', KEYS[1], '1')
    redis.call('expire', KEYS[1], '2')
end
return true