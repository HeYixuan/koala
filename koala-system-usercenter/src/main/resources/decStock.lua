local goodsId = KEYS[1];
local decStock = ARGV[1];
local total;
print('key为', goodsId);
print('value为', decStock);
local stock = redis.call('get', goodsId);
print('当前库存为 :', stock);
if stock == false or stock < decStock then
    total = 0
else
    total = redis.call('decrBy', goodsId, decStock)
end
return total;