local key     = KEYS[1]
local ttl     = KEYS[2]
local content = KEYS[3]

local lockSet = redis.call('setnx', key, content)

if lockSet == 1 then
    redis.call('pexpire', key, ttl)
end

return lockSet