ORDER 订单表
ID 		订单ID
ORDER_NO 	订单编号
MERCHANT_ID  	商户ID
MERCHANT_NO	商户编号
CREATE_TIME	创建时间

ORDER_DETAIL订单明细表
ID 		订单明细ID
ORDER_ID		订单ID
ORDER_NO 	订单编号
PRODUCT_ID	商品ID
PRODUCT_NAME	商品名称
PRODUCT_DETAIL	商品描述


PAYMENT支付记录表
ID 		主键
OPEN_ID		用户OPEN_ID
PREPAY_ID	预付款ID
ORDER_NO	订单编号
TRADE_NO	交易单号
PAYMENT_NO	微信支付编号
PAY_FEE		支付金额(单位：分)
PAY_STATUS	支付状态（0未支付  1已支付  2已退款）
PAY_METHOD	支付方式(1微信 2支付宝 3银联)
PAY_TIME 	支付完成时间
CREATE_TIME 	创建时间


MEMBER 会员表
ID  			会员ID
MERCHANT_ID		所属商户ID
MERCHANT_NO		所属商户编号
MEMBER_NO		会员编号
MEMBER_NICK_NAME	会员昵称
MEMBER_NAME		会员名称
SEX			会员性别(M男 F女 N未知)
BIRTHDAY			出生日期(年月日)
MOBILE			手机号
CREATE_TIME		创建时间