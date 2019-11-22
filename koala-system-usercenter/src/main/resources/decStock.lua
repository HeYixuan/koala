local productId = KEYS[1];
local decStock = ARGV[1];
local total;
print('key为', productId);
print('value为', decStock);
local stock = redis.call('get', productId);
print('当前库存为 :', stock);
if stock == false or stock < decStock then
    total = 0
else
    total = redis.call('decrBy', productId, decStock)
end
return total;