
--加锁
if redis.call('SETNX',KEYS[1],ARGV[1]) then
    if redis.call('GET',KEYS[1])==ARGV[1] then
        return redis.call('EXPIRE',KEYS[1],ARGV[2])
    else
        return 0
    end
end
