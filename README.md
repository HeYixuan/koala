# koala
koala是基于spring cloud 开发的spring cloud数据权限框架

密码模式授权：
<br/>
http://localhost:9001/oauth/token?username=admin2&password=123456&grant_type=password&scope=all

手机号验证码模式授权：
<br/>
http://localhost:9001/oauth/mobile/login?mobile=15218725510&captcha=123456&grant_type=password&scope=all


授权码模式自动授权：
<br/>
第一步：http://localhost:9001/oauth/authorize?client_id=test&response_type=code&redirect_uri=https://www.baidu.com/
<br/>跳转登录页 输入用户名：admin 123456 
登录成功拿到授权码输入(注意：redirect_uri地址和数据库的地址要一致，不然会导致url不匹配)：

第二步：http://localhost:9001/oauth/token?grant_type=authorization_code&code=HklLOx&client_id=test&client_secret=test&redirect_uri=https://www.baidu.com/

校验token:
客户端ID：test 密钥:test (Base64加密)<br/>
请求头：Authorization:Basic dGVzdDp0ZXN0 (Base64加密)
http://localhost:9001/oauth/check_token?token=b78a15ce-0faa-4ee8-95d5-433e121b5a2a

手动授权 用户名：admin 123456
<br/>
http://localhost:9001/oauth/authorize?client_id=test2&response_type=code&redirect_uri=https://www.baidu.com/


1.使用了Feign增强,不需要手动写Feign实现,只需要写Controller的Mapping映射和Feign的映射一致
2.网关
3.登陆密码需要传入AES加密过的密码,因为会走网关密码拦截器拦截。<br/>
3.1：AES加密链接:http://tool.chacuo.net/cryptaes <br/>
加密模式：CBC  
填充：ZERO_PADDING  <br/>
密码：0oYL0SBdNjXm8DkQ  <br/>
偏移量：0oYL0SBdNjXm8DkQ <br/>
输出:Base64  <br/>
加密明文：123456  <br/>
加密后密文：Y3rzvgYsYvBBLGjOCl4jTg==  <br/>
密文urlEncode后字符串是：Y3rzvgYsYvBBLGjOCl4jTg%3D%3D  <br/>


通过网关访问登录
http://localhost:9999/oauth/oauth/token?username=admin2&password=Y3rzvgYsYvBBLGjOCl4jTg%3D%3D&grant_type=password&scope=all
<br/>携带请求头
Authorization: Basic dGVzdDI6dGVzdDI=
<br/>
TENANT-ID: 000000
注：TENANT-ID 默认000000

获取到token后请求
http://localhost:9999/system/systemUser/getList
<br/>携带请求头
Authorization: Bearer 6265b580-f096-4826-ab2d-b61b45cb19a6


jar启动方法一:
jar启动命令：nohup java -Xms64m -Xmx128m -jar koala-register-1.0-SNAPSHOT.jar --server.port=8848 &


nohup java -Xms64m -Xmx512m -jar koala-system-1.0-SNAPSHOT.jar --server.port=9001 &

//nohup 意思是不挂断运行命令,当账户退出或终端关闭时,程序仍然运行
//当用 nohup 命令执行作业时，缺省情况下该作业的所有输出被重定向到nohup.out的文件中
//除非另外指定了输出文件。


jar启动方法二:
$ nohup java -jar test.jar >temp.txt &
 
//这种方法会把日志文件输入到你指定的文件中，没有则会自动创建

$ jobs
//那么就会列出所有后台执行的作业，并且每个作业前面都有个编号。
//如果想将某个作业调回前台控制，只需要 fg + 编号即可。
$ fg 2

查看某端口占用的线程的pid
netstat -nlp |grep :8080


120.78.66.117
root
123456
Hyx930512


<executions>
    <execution>
        <goals>
            <goal>repackage</goal>
        </goals>
    </execution>
</executions>

https://blog.csdn.net/progammer10086/article/details/94737570

public static void main(String [] args){
        String xml = "<xml><appid><![CDATA[wx2421b1c4370ec43b]]></appid><attach><![CDATA[支付测试]]></attach><bank_type><![CDATA[CFT]]></bank_type><fee_type><![CDATA[CNY]]></fee_type> <is_subscribe><![CDATA[Y]]></is_subscribe> <mch_id><![CDATA[10000100]]></mch_id> <nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str> <openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid> <out_trade_no><![CDATA[1409811653]]></out_trade_no> <result_code><![CDATA[SUCCESS]]></result_code> <return_code><![CDATA[SUCCESS]]></return_code> <sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign> <sub_mch_id><![CDATA[10000100]]></sub_mch_id> <time_end><![CDATA[20140903131540]]></time_end> <total_fee>1</total_fee> <coupon_fee_0><![CDATA[10]]></coupon_fee_0> <coupon_count><![CDATA[1]]></coupon_count> <coupon_type><![CDATA[CASH]]></coupon_type> <coupon_id><![CDATA[10000]]></coupon_id> <trade_type><![CDATA[JSAPI]]></trade_type> <transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id> </xml>";

        Document doc = XmlKit.parse(xml);
        Element element = doc.getDocumentElement();
        String appid = element.getAttribute("appid");
        System.err.println(appid);
    }
    
https://gitee.com/dendi.ke/weixin-service/blob/master/src/main/java/com/ld/tamp/web/controller/WeChatController.java

Minio:
Access Key:SMW1YMPP287R9UO8IVIV
Secret Key:YdddvFSI2mdCgiG4c6Wy+HEXYIQmK2cF9yA4zAG+



> 由于本项目主要目的在于使用RocketMQ进行削峰填谷，因此对于部分秒杀场景的业务处理尚未优化，此处列出待优化的点，供读者参考

1. 分布式减库存：使用Redis的decr进行分布式原子减库存
2. 预热库存时候将库存适当调大，防止恶意刷库存导致正常用户不能进行正常的秒杀订单投递
3. 【注意点】数据库侧的库存校验万万不能少，本demo已经加上了该校验
4. 秒杀接口需要做防刷处理，可以在前端通过倒计时方式定时开放接口、增加验证码减少下单频率、增加下单前校验收货地址等方式


## windows下单点RocketMQ搭建
0. 解压rocketmq-all-4.4.0-bin-release.zip到C盘根目录，或者某个盘的根目录（我是C盘）

        配置环境变量，增加环境变量
        ROCKETMQ_HOME=C:\rocketmq-all-4.4.0-bin-release（不要出现空格和中文）

1. 启动nameServer

        mqnamesrv.cmd
2. 启动broker

        start mqbroker.cmd -n 127.0.0.1:9876 autoCreateTopicEnable=true
3. 启动admin-console

        java -jar -Drocketmq.config.namesrvAddr=127.0.0.1:9876 rocketmq-console-ng.jar

[windows下RocketMQ安装部署](https://www.jianshu.com/p/4a275e779afa)

